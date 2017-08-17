package com.truyayong.gatherstory.content.presenters;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.truyayong.gatherstory.App;
import com.truyayong.gatherstory.Bus.events.FgUpdateSentenceEvent;
import com.truyayong.gatherstory.Bus.events.FgUserMessageEvent;
import com.truyayong.gatherstory.Bus.events.UpdateSentenceEvent;
import com.truyayong.gatherstory.Bus.events.IndexUserMessageEvent;
import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.BasePresenter;
import com.truyayong.gatherstory.content.adapter.FragmentTabAdapter;
import com.truyayong.gatherstory.content.fragments.HomeFragment;
import com.truyayong.gatherstory.content.fragments.MessageFragment;
import com.truyayong.gatherstory.content.fragments.NotificationFragment;
import com.truyayong.gatherstory.content.fragments.UserCenterFragment;
import com.truyayong.gatherstory.content.view.IIndexView;
import com.truyayong.gatherstory.daemon.Daemon;
import com.truyayong.gatherstory.search.activities.SearchActivity;
import com.truyayong.gatherstory.user.bean.User;
import com.truyayong.searchmodule.ISearchAidl;
import com.truyayong.searchmodule.SearchFragment;
import com.truyayong.searchmodule.custom.IOnSearchClickListener;
import com.truyayong.searchmodule.services.SearchKeywordService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.core.service.BmobIMService;
import cn.bmob.newim.event.IMEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by alley_qiu on 2017/3/6.
 */

public class IndexPresenter extends BasePresenter<IIndexView> {

    private Context mContext;

    private FgUpdateSentenceEvent fgUpdateSentenceEvent;
    private FgUserMessageEvent fgUserMessageEvent;

    private List<Fragment> fragments = new ArrayList<>();
    private int currentIndex = 0;
    private ISearchAidl mSearchAidl;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mSearchAidl = ISearchAidl.Stub.asInterface(service);
            try {
                mSearchAidl.saveSearchLucene();
            } catch (RemoteException e) {
                Log.e("truyayong", "remote exception : " + e.toString());
            }
            try {
                //binder设置死亡代理
                service.linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //正常关闭连接，mSearchAidl置为null
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


    public IndexPresenter(Context mContext) {
        this.mContext = mContext;
        EventBus.getDefault().register(this);
    }

    public void displayView() {
        fragments.add(new HomeFragment());
        fragments.add(new NotificationFragment());
        fragments.add(new MessageFragment());
        fragments.add(new UserCenterFragment());
        FragmentTabAdapter tabAdapter = new FragmentTabAdapter((FragmentActivity) mContext, fragments, R.id.fl_content_content_index, getView().getRGTabsContent());
        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
                super.OnRgsExtraCheckedChanged(radioGroup, checkedId, index);
                currentIndex = index;
                resetView();
                switch (index) {
                    case 0:
                        getView().getAppBar().setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        if (fgUpdateSentenceEvent != null) {
                            EventBus.getDefault().post(fgUpdateSentenceEvent);
                            fgUpdateSentenceEvent = null;
                        }
                        getView().getRBTabNotify().setActivated(false);
                        break;
                    case 2:
                        if (fgUserMessageEvent != null) {
                            EventBus.getDefault().post(fgUserMessageEvent);
                            fgUserMessageEvent = null;
                        }
                        getView().getRBTabMessage().setActivated(false);
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
            }
        });

        Intent serviceIntent = new Intent(mContext, SearchKeywordService.class);
        mContext.bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);

        //应用主界面启动时连接IM模块。
        final User mUser = BmobUser.getCurrentUser(User.class);
        BmobIM.connect(mUser.getObjectId(), new ConnectListener() {
            @Override
            public void done(String s, BmobException e) {
                if (e != null) {
                    Toast.makeText(mContext, "连接IM模块失败", Toast.LENGTH_SHORT).show();
                } else {
                }
            }
        });

    }

    public void onStart() {
        App.mSearchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
            @Override
            public void OnSearchClick(String keyword) {
                Intent intent = new Intent(mContext, SearchActivity.class);
                intent.putExtra("keyword", keyword);
                mContext.startActivity(intent);
            }
        });

        getView().getRLSearch().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.mSearchFragment.show(((FragmentActivity)mContext).getSupportFragmentManager(), SearchFragment.TAG);
            }
        });

    }

    private void resetView() {
        getView().getAppBar().setVisibility(View.GONE);
    }

    public void onBackPressed() {
        if (currentIndex != 0) {
            getView().getRBTabHome().setChecked(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateSentenceEvent(UpdateSentenceEvent event) {
        fgUpdateSentenceEvent = new FgUpdateSentenceEvent();
        fgUpdateSentenceEvent.setTitle(event.getTitle());
        fgUpdateSentenceEvent.setContent(event.getContent());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserMessageEvent(IndexUserMessageEvent event) {
        fgUserMessageEvent = new FgUserMessageEvent();
        fgUserMessageEvent.setUserId(event.getUserId());
        fgUserMessageEvent.setUserName(event.getUserName());
        fgUserMessageEvent.setUserHead(event.getUserHead());
        fgUserMessageEvent.setMessage(event.getMessage());
    }

    public void onDestroy() {
        mContext.unbindService(connection);
        EventBus.getDefault().unregister(this);
    }
}
