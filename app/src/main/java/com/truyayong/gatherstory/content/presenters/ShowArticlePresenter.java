package com.truyayong.gatherstory.content.presenters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.provider.Telephony;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.BasePresenter;
import com.truyayong.gatherstory.content.activities.AddSentenceActivity;
import com.truyayong.gatherstory.content.adapter.ShowArticleAdapter;
import com.truyayong.gatherstory.content.bean.Article;
import com.truyayong.gatherstory.content.bean.RecentArticle;
import com.truyayong.gatherstory.content.bean.Sentence;
import com.truyayong.gatherstory.content.view.IShowArticleView;
import com.truyayong.gatherstory.user.bean.User;
import com.truyayong.gatherstory.utils.SharedPreferencesUtil;
import com.truyayong.searchmodule.utils.TimeUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by alley_qiu on 2017/3/6.
 */

public class ShowArticlePresenter extends BasePresenter<IShowArticleView> implements SwipeRefreshLayout.OnRefreshListener{

    private Context mContext;
    private View headView;
    private View tailView;
    private ShowArticleAdapter adapter;
    private List<Sentence> datas;
    private boolean changeFollowRelation = false;
    private boolean changeFavoriteRelation = false;
    private User mUser = BmobUser.getCurrentUser(User.class);
    private SwipeRefreshLayout mRefreshLayout;
    private String updateTime = "";
    private String mTitle = "";
    private CompositeSubscription compositeSubscription= new CompositeSubscription();

    public ShowArticlePresenter(Context context) {
        this.mContext = context;
    }

    public void displayView(Intent intent) {
        showProgress(true);
        mRefreshLayout = getView().getSwipeRefreshLayout();
        setupSwipeRefresh();
        mTitle = intent.getStringExtra("title");
        getView().getRecyclerView().setLayoutManager(new LinearLayoutManager(mContext));
        headView = LayoutInflater.from(mContext).inflate(R.layout.headview_show_article, null);
        TextView tvArticleTitle = (TextView) headView.findViewById(R.id.tv_article_title);
        tvArticleTitle.setText(mTitle);
        tailView = LayoutInflater.from(mContext).inflate(R.layout.tailview_show_article, null);
        //查询文章所有节点
        compositeSubscription.add(querySentences(mTitle));

        //检查之前是否已经关注
        checkFollowState(mTitle);
        getView().getIBFollowArticle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeFollowRelation) {
                    changeFollowRelation = false;
                    compositeSubscription.add(articleDisFollowBmob(mTitle));
                } else {
                    changeFollowRelation = true;
                    compositeSubscription.add(articleFollowBmob(mTitle));
                }
            }
        });

        //检查之前是否点赞
        checkFavoriteState(mTitle);
        getView().getIBFavoriteArticle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeFavoriteRelation) {
                    changeFavoriteRelation = false;
                    compositeSubscription.add(articleDisFavoriteBmob(mTitle));
                } else {
                    changeFavoriteRelation = true;
                    compositeSubscription.add(articleFavoriteBmob(mTitle));
                }

            }
        });
        //添加节点
        getView().getIBAddSentence().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddSentenceActivity.class);
                intent.putExtra("title", mTitle);
                ((Activity)mContext).startActivityForResult(intent, 1);
            }
        });
    }

    //查询文章所有节点
    public Subscription querySentences(String title) {

        BmobQuery<Sentence> query = new BmobQuery<>();
        query.addWhereEqualTo("title", title);
        Subscription subscription = query.findObjects(new FindListener<Sentence>() {
            @Override
            public void done(List<Sentence> list, BmobException e) {
                if (e == null) {
                    updateTime = list.get(list.size() - 1).getCreatedAt();
                    datas = list;
                    adapter = new ShowArticleAdapter(mContext, headView, tailView, datas);
                    getView().getRecyclerView().setAdapter(adapter);
                    //保存最近阅读
                    saveRecent(list.get(0));
                    //文章节点加载完成
                    showProgress(false);
                } else {
                    Toast.makeText(mContext, "请求sentence数据失败", Toast.LENGTH_SHORT).show();
                    Log.e("truyayong", e.toString());
                }
            }
        });
        return subscription;

    }

    public void saveRecent(Sentence sentence) {
        List<RecentArticle> recents = SharedPreferencesUtil.getShares(mContext, "recent_share", "recent_text", RecentArticle.class);
        if (recents == null) {
            recents = new LinkedList<>();
        }
        RecentArticle recentArticle = new RecentArticle(sentence.getTitle(), sentence.getContent());
        Iterator<RecentArticle> iterator = recents.iterator();
        while (iterator.hasNext()) {
            RecentArticle node = iterator.next();
            if (node.getTitle().equals(recentArticle.getTitle())) {
                iterator.remove();
                break;
            }
        }
        SharedPreferencesUtil.saveShares(mContext, "recent_share", "recent_text", recentArticle, recents);
    }

    //检查是否关注
    public void checkFollowState(String title) {
        BmobQuery<Article> query = new BmobQuery<>();
        query.addWhereRelatedTo("followArticle", new BmobPointer(mUser));
        query.addWhereEqualTo("title",title);
        query.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> list, BmobException e) {
                if (list.isEmpty()) {
                    changeFollowRelation = false;
                    getView().getIBFollowArticle().setSelected(false);
                } else {
                    changeFollowRelation = true;
                    getView().getIBFollowArticle().setSelected(true);
                }
            }
        });
    }

    //关注文章
    public Subscription articleFollowBmob(String title) {
        getView().getIBFollowArticle().setSelected(true);
        BmobQuery<Article> query = new BmobQuery<>();
        query.addWhereEqualTo("title", title);
        Subscription subscription = query.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> list, BmobException e) {
                if (e == null && !list.isEmpty()) {
                    Article article = list.get(0);
                    article.increment("followUserCount");
                    BmobRelation relation = new BmobRelation();
                    relation.add(mUser);
                    article.setFollowUser(relation);
                    article.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(mContext, "关注成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "关注失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    User user = new User();
                    BmobRelation userFollowRelation = new BmobRelation();
                    userFollowRelation.add(article);
                    user.setFollowArticle(userFollowRelation);
                    user.increment("followArticleCount");
                    user.update(mUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(mContext, "关注成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "关注失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(mContext, "请求文章数据失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return subscription;
    }

    //取消关注文章
    public Subscription articleDisFollowBmob(String title) {
        getView().getIBFollowArticle().setSelected(false);
        BmobQuery<Article> query = new BmobQuery<>();
        query.addWhereEqualTo("title", title);
        Subscription subscription = query.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> list, BmobException e) {
                if (e == null && !list.isEmpty()) {
                    Article article = list.get(0);
                    article.increment("followUserCount", -1);
                    BmobRelation relation = new BmobRelation();
                    relation.remove(mUser);
                    article.setFollowUser(relation);
                    article.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(mContext, "取消关注成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "取消关注失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    User user = new User();
                    BmobRelation userFollowRelation = new BmobRelation();
                    userFollowRelation.remove(article);
                    user.setFollowArticle(userFollowRelation);
                    user.increment("followArticleCount", -1);
                    user.update(mUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(mContext, "取消关注成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "取消关注失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(mContext, "请求文章数据失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return subscription;
    }

    //检查是否已经点赞
    public void checkFavoriteState(String title) {
        BmobQuery<Article> query = new BmobQuery<>();
        query.addWhereRelatedTo("favoriteArticle", new BmobPointer(mUser));
        query.addWhereEqualTo("title",title);
        query.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> list, BmobException e) {
                if (list.isEmpty()) {
                    changeFavoriteRelation = false;
                    getView().getIBFavoriteArticle().setSelected(false);
                } else {
                    changeFavoriteRelation = true;
                    getView().getIBFavoriteArticle().setSelected(true);
                }
            }
        });
    }

    //给文章点赞
    public Subscription articleFavoriteBmob(String title) {
        getView().getIBFavoriteArticle().setSelected(true);
        BmobQuery<Article> query = new BmobQuery<>();
        query.addWhereEqualTo("title", title);
        Subscription subscription = query.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> list, BmobException e) {
                if (e == null && !list.isEmpty()) {
                    Article article = list.get(0);
                    article.increment("favoriteCount");
                    BmobRelation relation = new BmobRelation();
                    relation.add(mUser);
                    article.setFavoriteUser(relation);
                    article.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(mContext, "点赞成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "点赞失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    User user = new User();
                    BmobRelation favoriteArticle = new BmobRelation();
                    favoriteArticle.add(article);
                    user.setFavoriteArticle(favoriteArticle);
                    user.increment("favoriteArticleCount");
                    user.update(mUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(mContext, "点赞成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "点赞失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(mContext, "请求article数据失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return subscription;
    }

    //取消文章点赞
    public Subscription articleDisFavoriteBmob(String title) {
        getView().getIBFavoriteArticle().setSelected(false);
        BmobQuery<Article> query = new BmobQuery<>();
        query.addWhereEqualTo("title", title);
        Subscription subscription = query.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> list, BmobException e) {
                if (e == null && !list.isEmpty()) {
                    Article article = list.get(0);
                    article.increment("favoriteCount", -1);
                    BmobRelation relation = new BmobRelation();
                    relation.remove(mUser);
                    article.setFavoriteUser(relation);
                    article.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(mContext, "取消点赞成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "取消点赞失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    User user = new User();
                    BmobRelation favoriteArticle = new BmobRelation();
                    favoriteArticle.remove(article);
                    user.setFavoriteArticle(favoriteArticle);
                    user.increment("favoriteArticleCount", -1);
                    user.update(mUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(mContext, "取消点赞成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "取消点赞失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(mContext, "请求article数据失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return subscription;
    }

    private void setupSwipeRefresh(){
        mRefreshLayout = getView().getSwipeRefreshLayout();
        if(mRefreshLayout != null){
            mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
            mRefreshLayout.setProgressViewOffset(false, 0, (int) (mContext.getResources().getDisplayMetrics().density * 64));
            mRefreshLayout.setOnRefreshListener(this);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = mContext.getResources().getInteger(android.R.integer.config_shortAnimTime);

            getView().getRecyclerView().setVisibility(show ? View.GONE : View.VISIBLE);
            getView().getRecyclerView().animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    getView().getRecyclerView().setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            getView().getPBProcess().setVisibility(show ? View.VISIBLE : View.GONE);
            getView().getPBProcess().animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    getView().getPBProcess().setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            getView().getPBProcess().setVisibility(show ? View.VISIBLE : View.GONE);
            getView().getRecyclerView().setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void onActivityResult(Intent data) {
        Sentence newItem = (Sentence) data.getSerializableExtra("new_item");
        if (adapter != null) {
            adapter.add(newItem);
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BmobQuery<Sentence> query = new BmobQuery<Sentence>();
                query.addWhereEqualTo("title", mTitle);
                BmobDate bmobDate = null;
                try {
                    bmobDate = TimeUtil.string2BmobDate(updateTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (bmobDate != null) {
                    //最新时间会把相等的查出来
                    query.addWhereGreaterThan("createdAt", bmobDate);
                    query.findObjects(new FindListener<Sentence>() {
                        @Override
                        public void done(List<Sentence> list, BmobException e) {
                            if (e == null && list.size() > 1) {
                                List<Sentence> realUpdateItems = list.subList(1,list.size());
                                adapter.add(realUpdateItems);
                                updateTime = list.get(list.size() - 1).getCreatedAt();
                            } else {
                                Toast.makeText(mContext, "没有新内容", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                mRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    public void onDestroy() {
        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }
}
