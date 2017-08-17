package com.truyayong.gatherstory.splash.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.content.activities.IndexActivity;
import com.truyayong.gatherstory.splash.adapter.HorizontalPagerAdapter;
import com.truyayong.gatherstory.splash.fragments.HomeFragment;
import com.truyayong.gatherstory.splash.fragments.LeftFragment;
import com.truyayong.gatherstory.splash.fragments.RightFragment;
import com.truyayong.gatherstory.splash.interfaces.IScrollListener;
import com.truyayong.gatherstory.splash.interfaces.IViewPagerCurrent;
import com.truyayong.gatherstory.splash.widget.FixedSpeedScroller;
import com.truyayong.gatherstory.splash.widget.HorizontalViewPager;
import com.truyayong.gatherstory.user.activities.UserLoginActivity;
import com.truyayong.gatherstory.user.bean.User;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class SplashActivity extends FragmentActivity implements  ViewPager.OnPageChangeListener, View.OnClickListener {

    /**
     * 左侧fragment
     */
    private final int INDEX_LEFT = 0;
    /**
     * 主页面fragment
     */
    private final int INDEX_HOME = 1;
    /**
     * 右侧fragment
     */
    private final int INDEX_RIGHT = 2;

    private List<Fragment> mFragments = new ArrayList<>();
    private HorizontalViewPager horizontalViewPager;
    private HorizontalPagerAdapter adapter;
    private Bitmap bgBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        horizontalViewPager = (HorizontalViewPager)findViewById(R.id.horizontal_pager_splash);
        bgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bg_home);
        horizontalViewPager.setBackGround(bgBitmap);
        initViewPagerSpeed();
        LeftFragment leftFragment = new LeftFragment();
        RightFragment rightFragment = new RightFragment();
        HomeFragment homeFragment = new HomeFragment();
        mFragments.add(leftFragment);
        mFragments.add(homeFragment);
        mFragments.add(rightFragment);
        leftFragment.setPagerOnClickListener(this);
        rightFragment.setPagerOnClickListener(this);
        homeFragment.setPagerOnClickListener(this);
        adapter = new HorizontalPagerAdapter(getSupportFragmentManager(), mFragments);
        horizontalViewPager.setOffscreenPageLimit(mFragments.size() - 1);
        horizontalViewPager.setAdapter(adapter);
        horizontalViewPager.setCurrentItem(INDEX_LEFT);
        horizontalViewPager.setOnPageChangeListener(this);
    }

    private void initViewPagerSpeed() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(horizontalViewPager.getContext());
            mScroller.set(horizontalViewPager, scroller);
        } catch (NoSuchFieldException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_right_left: {
                User user = BmobUser.getCurrentUser(User.class);
                if (user == null) {
                    Intent intent = new Intent(this, UserLoginActivity.class);
                    startActivity(intent);
                    bgBitmap.recycle();
                    finish();
                } else {
                    Intent intent = new Intent(this, IndexActivity.class);
                    startActivity(intent);
                    bgBitmap.recycle();
                    finish();
                }
            }
            break;
        }
    }
}
