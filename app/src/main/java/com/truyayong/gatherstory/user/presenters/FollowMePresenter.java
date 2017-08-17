package com.truyayong.gatherstory.user.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.truyayong.gatherstory.base.BasePresenter;
import com.truyayong.gatherstory.content.bean.Article;
import com.truyayong.gatherstory.user.adapter.FollowArticleRecyclerViewAdapter;
import com.truyayong.gatherstory.user.adapter.FollowMeRecyclerViewAdapter;
import com.truyayong.gatherstory.user.bean.FollowMe;
import com.truyayong.gatherstory.user.bean.User;
import com.truyayong.gatherstory.user.view.IFollowMeView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.StreamHandler;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by alley_qiu on 2017/3/15.
 */

public class FollowMePresenter extends BasePresenter<IFollowMeView> {

    private Context mContext;

    public FollowMePresenter(Context mContext) {
        this.mContext = mContext;
    }

    public void setupView() {
        User user = getUser();
        BmobQuery<FollowMe> query = new BmobQuery<>();
        query.addWhereEqualTo("meId", user.getObjectId());
        query.findObjects(new FindListener<FollowMe>() {
            @Override
            public void done(List<FollowMe> list, BmobException e) {
                List<String> ids = new ArrayList<String>();
                for (FollowMe f : list) {
                    ids.add(f.getFollowMeId());
                }
                BmobQuery<User> query1 = new BmobQuery<User>();
                query1.addWhereContainedIn("objectId", ids);
                query1.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        getView().getRVUsers().setLayoutManager(new LinearLayoutManager(mContext));
                        FollowMeRecyclerViewAdapter adapter = new FollowMeRecyclerViewAdapter(mContext, list);
                        getView().getRVUsers().setAdapter(adapter);
                    }
                });
            }
        });
    }

    private User getUser() {
        Intent intent = ((Activity)mContext).getIntent();
        Bundle bundle = intent.getExtras();
        return (User) bundle.get("com.truyayong.oldhouse.user.bean.user");
    }
}
