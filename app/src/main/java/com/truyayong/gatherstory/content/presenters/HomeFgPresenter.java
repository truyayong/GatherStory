package com.truyayong.gatherstory.content.presenters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.BasePresenter;
import com.truyayong.gatherstory.content.activities.AddArticleActivity;
import com.truyayong.gatherstory.content.adapter.HomeRecyclerViewAdapter;
import com.truyayong.gatherstory.content.bean.Article;
import com.truyayong.gatherstory.content.view.IHomeFgView;
import com.truyayong.gatherstory.user.bean.User;
import com.truyayong.searchmodule.utils.TimeUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by alley_qiu on 2017/3/6.
 */

public class HomeFgPresenter extends BasePresenter<IHomeFgView> implements SwipeRefreshLayout.OnRefreshListener {

    private Context mContext;

    private SwipeRefreshLayout mRefreshLayout;
    private View headView;
    private List<Article> datas = new ArrayList<>();
    private HomeRecyclerViewAdapter adapter;
    private String updateTime = "";

    CompositeSubscription compositeSubscription = new CompositeSubscription();

    public HomeFgPresenter(Context context) {
        this.mContext = context;
    }

    public void displayView() {
        showProgress(true);
        headView = LayoutInflater.from(mContext).inflate(R.layout.headview_fragment_home, null);
        mRefreshLayout = getView().getSwipeRefreshLayout();
        setupSwipeRefresh();
        compositeSubscription.add(queryArticles());
        getView().getFABWriteArticle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddArticleActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    public Subscription queryArticles() {
        BmobQuery<Article> query = new BmobQuery<>();
        final Subscription subscription = query.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> list, BmobException e) {
                if (list != null && !list.isEmpty()) {
                    datas = list;
                } else {
                    showProgress(false);
                    return;
                }
                updateTime = list.get(list.size() - 1).getCreatedAt();
                adapter = new HomeRecyclerViewAdapter(mContext, datas);
                View headView = LayoutInflater.from(mContext).inflate(R.layout.headview_fragment_home, null);
                adapter.setHeadView(headView);
                getView().getRecyclerView().setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                getView().getRecyclerView().setAdapter(adapter);
                getView().getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (Math.abs(dy) > 5) {
                            if (dy > 0) {
                                getView().getFAM().hideMenu(true);
                            } else {
                                getView().getFAM().showMenu(true);
                            }
                        }
                    }
                });
                showProgress(false);
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

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //再次再次请求网络数据
                BmobQuery<Article> query = new BmobQuery<>();
                BmobDate bmobDate = null;
                try {
                    bmobDate = TimeUtil.string2BmobDate(updateTime);
                    Log.e("truyayong", " updatetime : " + updateTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (bmobDate != null) {
                    query.addWhereGreaterThan("createdAt", bmobDate);
                    Subscription subscription = query.findObjects(new FindListener<Article>() {
                        @Override
                        public void done(List<Article> list, BmobException e) {
                            if (e == null && list.size() > 1) {
                                updateTime = list.get(list.size() - 1).getCreatedAt();
                                List<Article> realUpdateItems = list.subList(1, list.size());
                                adapter.add(realUpdateItems);
                            } else {
                                Toast.makeText(mContext, "没有新内容", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    compositeSubscription.add(subscription);
                }

                mRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = mContext.getResources().getInteger(android.R.integer.config_shortAnimTime);

            getView().getSwipeRefreshLayout().setVisibility(show ? View.GONE : View.VISIBLE);
            getView().getSwipeRefreshLayout().animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    getView().getSwipeRefreshLayout().setVisibility(show ? View.GONE : View.VISIBLE);
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
            getView().getSwipeRefreshLayout().setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void onDestroy() {
        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }
}
