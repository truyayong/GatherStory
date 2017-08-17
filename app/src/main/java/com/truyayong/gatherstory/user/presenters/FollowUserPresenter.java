package com.truyayong.gatherstory.user.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.truyayong.gatherstory.base.BasePresenter;
import com.truyayong.gatherstory.user.adapter.FollowMeRecyclerViewAdapter;
import com.truyayong.gatherstory.user.adapter.FollowUserRecyclerViewAdapter;
import com.truyayong.gatherstory.user.bean.FollowMe;
import com.truyayong.gatherstory.user.bean.User;
import com.truyayong.gatherstory.user.view.IFollowUserView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by alley_qiu on 2017/3/16.
 */

public class FollowUserPresenter extends BasePresenter<IFollowUserView> {

    private Context mContext;

    public FollowUserPresenter(Context mContext) {
        this.mContext = mContext;
    }

    public void setupView() {
        User user = getUser();
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereRelatedTo("followUser", new BmobPointer(user));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                Log.e("truyayong", "list : " + list.size());
                getView().getRVUsers().setLayoutManager(new LinearLayoutManager(mContext));
                FollowUserRecyclerViewAdapter adapter = new FollowUserRecyclerViewAdapter(mContext, list);
                getView().getRVUsers().setAdapter(adapter);
            }
        });
    }

    private User getUser() {
        Intent intent = ((Activity)mContext).getIntent();
        Bundle bundle = intent.getExtras();
        return (User) bundle.get("com.truyayong.oldhouse.user.bean.user");
    }
}
