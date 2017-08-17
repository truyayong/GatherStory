package com.truyayong.gatherstory.search.presenters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.truyayong.gatherstory.Bus.events.SearchKeyWordEvent;
import com.truyayong.gatherstory.base.BasePresenter;
import com.truyayong.gatherstory.content.bean.Sentence;
import com.truyayong.gatherstory.search.adapter.SearchRecyclerViewAdapter;
import com.truyayong.gatherstory.search.view.ISearchContentFgView;
import com.truyayong.searchmodule.IOnSearchResultArrivedListener;
import com.truyayong.searchmodule.ISearchAidl;
import com.truyayong.searchmodule.services.SearchKeywordService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by alley_qiu on 2017/3/20.
 */

public class SearchContentFgPresenter extends BasePresenter<ISearchContentFgView> {

    private Context mContext;
    private List<Sentence> sentences = new ArrayList<>();
    SearchRecyclerViewAdapter adapter;

    private ISearchAidl mSearchAidl;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mSearchAidl = ISearchAidl.Stub.asInterface(service);
            try {
                mSearchAidl.registerListener(mOnSearchResultArrivedListener);
            } catch (RemoteException e) {
                Log.e("truyayong", "remote exception : " + e.toString());
            }
            try {
                //binder设置死亡代理
                service.linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            //第一次启动需要进行查询
            String key = getKeyWord();
            if (key != null && !"".equals(key)) {
                try {
                    mSearchAidl.searchKeyword(key);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //正常关闭连接，mSearchAidl置为null
            try {
                mSearchAidl.unregisterListener(mOnSearchResultArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mSearchAidl = null;
        }
    };

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mSearchAidl == null) {
                return;
            }
            mSearchAidl.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mSearchAidl = null;
            //binder非正常死亡之后重新连接
            Intent intent = new Intent(mContext, SearchKeywordService.class);
            mContext.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }
    };

    private IOnSearchResultArrivedListener mOnSearchResultArrivedListener = new IOnSearchResultArrivedListener.Stub() {
        @Override
        public void onSearchResultArrived(List<String> result) throws RemoteException {
            Log.e("truyayong", "arrived result : " + result.size());
            queryResult(result);
        }
    };


    public SearchContentFgPresenter(Context mContext) {
        this.mContext = mContext;
        EventBus.getDefault().register(this);
        Intent serviceIntent = new Intent(mContext, SearchKeywordService.class);
        mContext.bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
    }



    public void setupView() {
        showProgress(true);
    }

    public void queryResult(@NonNull List<String> objIds) {
        BmobQuery<Sentence> query = new BmobQuery<>();
        query.addWhereContainedIn("objectId", objIds);
        query.findObjects(new FindListener<Sentence>() {
            @Override
            public void done(List<Sentence> list, BmobException e) {
                if (e == null) {
                    sentences = list;
                    Log.e("truyayong", "find sentence size : " + list.size());
                    getView().getRVSentences().setLayoutManager(new LinearLayoutManager(mContext));
                    adapter = new SearchRecyclerViewAdapter(mContext, sentences);
                    getView().getRVSentences().setAdapter(adapter);
                    showProgress(false);
                    if (sentences.isEmpty()) {
                        getView().getTVEmpty().setVisibility(View.VISIBLE);
                        getView().getRVSentences().setVisibility(View.GONE);
                    } else {
                        getView().getTVEmpty().setVisibility(View.GONE);
                        getView().getRVSentences().setVisibility(View.VISIBLE);
                    }
                } else {

                }
            }
        });
    }

    @Subscribe
    public void onSearchResultEvent(SearchKeyWordEvent event) {
        try {
            mSearchAidl.searchKeyword(event.getKeyword());
            showProgress(true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

   public void onDestroy() {
       EventBus.getDefault().unregister(this);
       mContext.unbindService(connection);
   }

    private String getKeyWord() {
        Intent intent = ((Activity)mContext).getIntent();
        String keyword = intent.getStringExtra("keyword");
        return keyword;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = mContext.getResources().getInteger(android.R.integer.config_shortAnimTime);

            getView().getForm().setVisibility(show ? View.GONE : View.VISIBLE);
            getView().getForm().animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    getView().getForm().setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            getView().getProcess().setVisibility(show ? View.VISIBLE : View.GONE);
            getView().getProcess().animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    getView().getProcess().setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            getView().getProcess().setVisibility(show ? View.VISIBLE : View.GONE);
            getView().getForm().setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
