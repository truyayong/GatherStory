package com.truyayong.gatherstory.user.presenters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.BasePresenter;
import com.truyayong.gatherstory.content.bean.Article;
import com.truyayong.gatherstory.im.activities.ChatActivity;
import com.truyayong.gatherstory.user.activities.FollowMeActivity;
import com.truyayong.gatherstory.user.activities.FollowUserActivity;
import com.truyayong.gatherstory.user.activities.ListFollowArticleActivity;
import com.truyayong.gatherstory.user.activities.UserWriteArticlesActivity;
import com.truyayong.gatherstory.user.activities.UserWriteSentencesActivity;
import com.truyayong.gatherstory.user.bean.FollowMe;
import com.truyayong.gatherstory.user.bean.User;
import com.truyayong.gatherstory.user.view.IUserInformationView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by alley_qiu on 2017/3/5.
 */

public class UserInfomationPresenter extends BasePresenter<IUserInformationView> {

    private Context mContext;
    private User user;
    private User mUser;

    private boolean isFollowed = false;

    public UserInfomationPresenter(Context context) {
        this.mContext = context;
    }

    private User getUser() {
        Intent intent = ((Activity)mContext).getIntent();
        Bundle bundle = intent.getExtras();
        return  (User)bundle.get("com.truyayong.oldhouse.user.bean.user");
    }

    public void initView() {
        //开始初始化界面，显示进度条
        showProgress(true);
        setupView();
    }

    private void setupView() {
        user = getUser();
        mUser = BmobUser.getCurrentUser(User.class);
        getView().getCollapsingToolbarLayout().setTitle(user.getUserName());
        Picasso.with(mContext).load(mUser.getUserHeadUrl()).into(getView().getCIVHeadview());
        //设置关注文章数
        if (user.getFollowArticleCount() == null) {
            getView().getTVFollowArticleCount().setText("0");
        } else {
            getView().getTVFollowArticleCount().setText("" + user.getFollowArticleCount());
        }
        //设置关注的用户数
        if (user.getFollowUserCount() == null) {
            getView().getTVFollowUser().setText("0");
        } else {
            getView().getTVFollowUser().setText("" + user.getFollowUserCount());
        }
        //计算关注mUser的人数
        BmobQuery<FollowMe> followMeQuery = new BmobQuery<>();
        followMeQuery.addWhereEqualTo("meId", user.getObjectId());
        followMeQuery.findObjects(new FindListener<FollowMe>() {
            @Override
            public void done(List<FollowMe> list, BmobException e) {
                getView().getTVFollowedUser().setText("" + list.size());
            }
        });

        getView().getTVDescription().setText("" + user.getUserDescription());
        if (user.getGender() == true) {
            getView().getTVUserGender().setText("Boy");
        } else {
            getView().getTVUserGender().setText("Girl");
        }
        getView().getLLFollowArticle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ListFollowArticleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("com.truyayong.oldhouse.user.bean.user", user);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        getView().getLLFollowUser().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FollowUserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("com.truyayong.oldhouse.user.bean.user", user);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        getView().getLLFollowedUser().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FollowMeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("com.truyayong.oldhouse.user.bean.user", user);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        getView().getRLWriteArticle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserWriteArticlesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("com.truyayong.oldhouse.user.bean.user", user);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        getView().getRLWriteSentence().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserWriteSentencesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("com.truyayong.oldhouse.user.bean.user", mUser);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        getView().getRLSendMessage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        //关注按钮逻辑
        BmobQuery<FollowMe> query = new BmobQuery<>();
        query.addWhereEqualTo("meId", user.getObjectId());
        query.addWhereEqualTo("followMeId", mUser.getObjectId());
        query.findObjects(new FindListener<FollowMe>() {
            @Override
            public void done(List<FollowMe> list, BmobException e) {
                if (e != null || list.isEmpty()) {
                    //没关注
                    changeBtnActionFollow(true);
                } else {
                    changeBtnActionFollow(false);
                }

                getView().getBTNActionFollow().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isFollowed) {
                            disFollow(mUser, user);
                            disFollowMe(user, mUser);
                            changeBtnActionFollow(true);
                        } else {
                            follow(mUser, user);
                            followMe(user, mUser);
                            changeBtnActionFollow(false);
                        }
                    }
                });
                //界面初始化成功，隐藏进度条
                showProgress(false);

            }
        });

    }

    /**
     * 改变关注按钮状态
     * @param change
     */
    private void changeBtnActionFollow(boolean change) {
        if (change) {
            isFollowed = false;
            getView().getBTNActionFollow().setText("关注");
            getView().getBTNActionFollow().setBackgroundColor(Color.parseColor("#6D4A41"));
        } else {
            isFollowed = true;
            getView().getBTNActionFollow().setText("已关注");
            getView().getBTNActionFollow().setBackgroundColor(Color.parseColor("#e0e0e0"));
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = mContext.getResources().getInteger(android.R.integer.config_shortAnimTime);

            getView().getPBProcess().setVisibility(show ? View.GONE : View.VISIBLE);
            getView().getLLForm().animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    getView().getLLForm().setVisibility(show ? View.GONE : View.VISIBLE);
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
            getView().getLLForm().setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * me关注host
     * @param me
     * @param host
     */
    private void follow(User me, User host) {
        BmobRelation bmobRelation = new BmobRelation();
        bmobRelation.add(host);
        User user = new User();
        user.setFollowUser(bmobRelation);
        user.increment("followUserCount");
        user.update(me.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e != null) {
                    Toast.makeText(mContext, "关注失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * me取消关注host
     * @param me
     * @param host
     */
    private void disFollow(User me, User host) {
        BmobRelation bmobRelation = new BmobRelation();
        bmobRelation.remove(host);
        User user = new User();
        user.setFollowUser(bmobRelation);
        user.increment("followUserCount", -1);
        user.update(me.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e != null) {
                    Toast.makeText(mContext, "取消关注失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * followMe被me关注
     * @param me
     * @param followMe
     */
    private void followMe(User me, User followMe) {
        FollowMe tFollowMe = new FollowMe();
        tFollowMe.setMeId(me.getObjectId());
        tFollowMe.setFollowMeId(followMe.getObjectId());
        tFollowMe.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e != null) {
                    Toast.makeText(mContext, "被关注失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 取消me对followMe的关注
     * @param me
     * @param followMe
     */
    private void disFollowMe(User me, User followMe) {
        BmobQuery<FollowMe> query = new BmobQuery<>();
        query.addWhereEqualTo("meId", me.getObjectId());
        query.addWhereEqualTo("followMeId", followMe.getObjectId());
        query.findObjects(new FindListener<FollowMe>() {
            @Override
            public void done(List<FollowMe> list, BmobException e) {
                if (e == null && !list.isEmpty()) {
                    list.get(0).delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {

                            } else {
                                Toast.makeText(mContext, "取消被关注失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(mContext, "取消被关注失败", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void sendMessage() {
        BmobIMUserInfo userInfo = new BmobIMUserInfo(user.getObjectId(), user.getUserName(), user.getUserHeadUrl());
        BmobIM.getInstance().startPrivateConversation(userInfo, new ConversationListener() {
            @Override
            public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                if (e == null) {
                    Intent chatIntent = new Intent(mContext, ChatActivity.class);
                    Bundle chatBundle = new Bundle();
                    chatBundle.putSerializable("com.truyayong.oldhouse.BmobIMConversation", bmobIMConversation);
                    chatIntent.putExtras(chatBundle);
                    mContext.startActivity(chatIntent);
                } else {
                    Toast.makeText(mContext, "开启会话失败", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
