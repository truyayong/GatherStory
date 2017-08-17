package com.truyayong.gatherstory.user.view;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by alley_qiu on 2017/3/5.
 */

public interface IUserInformationView {

    CollapsingToolbarLayout getCollapsingToolbarLayout();
    CircleImageView getCIVHeadview();
    ProgressBar getPBProcess();
    LinearLayout getLLForm();
    LinearLayout getLLFollowArticle();
    TextView getTVFollowArticleCount();
    LinearLayout getLLFollowUser();
    TextView getTVFollowUser();
    LinearLayout getLLFollowedUser();
    TextView getTVFollowedUser();
    TextView getTVDescription();
    TextView getTVUserLocation();
    TextView getTVUserGender();
    Button getBTNActionFollow();
    RelativeLayout getRLWriteArticle();
    RelativeLayout getRLWriteSentence();
    RelativeLayout getRLSendMessage();

}
