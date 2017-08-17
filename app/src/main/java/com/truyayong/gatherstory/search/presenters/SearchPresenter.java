package com.truyayong.gatherstory.search.presenters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.truyayong.gatherstory.App;
import com.truyayong.gatherstory.Bus.events.SearchKeyWordEvent;
import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.BasePresenter;
import com.truyayong.gatherstory.search.fragments.SearchContentFragment;
import com.truyayong.gatherstory.search.view.ISearchView;
import com.truyayong.searchmodule.SearchFragment;
import com.truyayong.searchmodule.custom.IOnSearchClickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alley_qiu on 2017/3/20.
 */

public class SearchPresenter extends BasePresenter<ISearchView> {

    private Context mContext;
    private List<Fragment> fragments = new ArrayList<>();




    public SearchPresenter(Context mContext) {
        this.mContext = mContext;
    }

    public void displayView() {
        fragments.add(new SearchContentFragment());
        FragmentTransaction fragmentTransaction = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_content_search_content, fragments.get(0));
        fragmentTransaction.commit();

    }

    public void onStart() {
        App.mSearchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
            @Override
            public void OnSearchClick(String keyword) {
                SearchKeyWordEvent event = new SearchKeyWordEvent();
                event.setKeyword(keyword);
                EventBus.getDefault().post(event);
            }
        });
        getView().getRLSearch().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.mSearchFragment.show(((FragmentActivity)mContext).getSupportFragmentManager(), SearchFragment.TAG);
            }
        });
    }


    public void onDestroy() {
    }
}
