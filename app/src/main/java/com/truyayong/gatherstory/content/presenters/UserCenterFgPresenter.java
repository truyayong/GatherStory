package com.truyayong.gatherstory.content.presenters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.squareup.picasso.Picasso;
import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.BasePresenter;
import com.truyayong.gatherstory.content.bean.Article;
import com.truyayong.gatherstory.content.bean.RecentArticle;
import com.truyayong.gatherstory.content.view.IUserCenterFgView;
import com.truyayong.gatherstory.user.activities.FollowMeActivity;
import com.truyayong.gatherstory.user.activities.FollowUserActivity;
import com.truyayong.gatherstory.user.activities.ListFollowArticleActivity;
import com.truyayong.gatherstory.user.activities.RecentReadActivity;
import com.truyayong.gatherstory.user.activities.UserEditActivity;
import com.truyayong.gatherstory.user.activities.UserWriteArticlesActivity;
import com.truyayong.gatherstory.user.activities.UserWriteSentencesActivity;
import com.truyayong.gatherstory.user.bean.FollowMe;
import com.truyayong.gatherstory.user.bean.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by alley_qiu on 2017/3/6.
 */

public class UserCenterFgPresenter extends BasePresenter<IUserCenterFgView> {

    private Context mContext;
    private User mUser;

    public UserCenterFgPresenter(Context context) {
        this.mContext = context;
    }

    public void initView() {
        showProgress(true);
        displayView();
    }

    public void displayView() {
        mUser = BmobUser.getCurrentUser(User.class);
        //设置头像，用户名
        getView().getCollapsingToolbarLayout().setTitle(mUser.getUserName());
        Picasso.with(mContext).load(mUser.getUserHeadUrl()).placeholder(R.drawable.profile).into(getView().getHeadView());
        getView().getFAB().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserEditActivity.class);
                mContext.startActivity(intent);
            }
        });

        //查看用户关注关系
        if (mUser.getFollowArticleCount() == null) {
            getView().getTVFollowArticle().setText("0");
        } else {
            getView().getTVFollowArticle().setText("" + mUser.getFollowArticleCount());
        }

        if (mUser.getFollowUserCount() == null) {
            getView().getTVFollowUser().setText("0");
        } else {
            getView().getTVFollowUser().setText("" + mUser.getFollowUserCount());
        }

        BmobQuery<FollowMe> query = new BmobQuery<>();
        query.addWhereEqualTo("meId", mUser.getObjectId());
        query.findObjects(new FindListener<FollowMe>() {
            @Override
            public void done(List<FollowMe> list, BmobException e) {
                if (list == null) {
                    getView().getTVFollowedUser().setText("0");
                } else {
                    getView().getTVFollowedUser().setText("" + list.size());
                }
                showProgress(false);
            }
        });

        getView().getLLFollowArticle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ListFollowArticleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("com.truyayong.oldhouse.user.bean.user", mUser);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        getView().getLLFollowUser().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FollowUserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("com.truyayong.oldhouse.user.bean.user", mUser);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        getView().getLLFollowedUser().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FollowMeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("com.truyayong.oldhouse.user.bean.user", mUser);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        //用户性别，签名
        getView().getTVUserDescription().setText(mUser.getUserDescription());
        getView().getTVUserLocation().setText(mUser.getUserLocation());
        if (mUser.getGender() == null || mUser.getGender() == true) {
            getView().getUserGender().setText("Boy");
        } else {
            getView().getUserGender().setText("Girl");
        }

        //查看用户创作部分
        getView().getRLWriteArticle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserWriteArticlesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("com.truyayong.oldhouse.user.bean.user", mUser);
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
        getView().getRLRecentRead().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RecentReadActivity.class);
                mContext.startActivity(intent);
            }
        });
        getView().getRLAboutTruyayong().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {

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

            getView().getPBProcess().setVisibility(show ? View.VISIBLE : View.GONE);
            getView().getLLForm().setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
