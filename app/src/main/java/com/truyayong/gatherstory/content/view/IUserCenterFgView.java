package com.truyayong.gatherstory.content.view;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by alley_qiu on 2017/3/6.
 */

public interface IUserCenterFgView {

    AppBarLayout getAppBar();
    CollapsingToolbarLayout getCollapsingToolbarLayout();
    CircleImageView getHeadView();

    ProgressBar getPBProcess();
    LinearLayout getLLForm();

    LinearLayout getLLFollowArticle();
    TextView getTVFollowArticle();
    LinearLayout getLLFollowUser();
    TextView getTVFollowUser();
    LinearLayout getLLFollowedUser();
    TextView getTVFollowedUser();

    TextView getTVUserDescription();
    TextView getTVUserLocation();
    TextView getUserGender();
    RelativeLayout getRLWriteArticle();
    RelativeLayout getRLWriteSentence();
    RelativeLayout getRLRecentRead();
    RelativeLayout getRLAboutTruyayong();

    FloatingActionButton getFAB();
}
