package com.truyayong.gatherstory.content.view;

import android.support.design.widget.AppBarLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

/**
 * Created by alley_qiu on 2017/3/6.
 */

public interface IIndexView {

    AppBarLayout getAppBar();
    RelativeLayout getRLSearch();
    FrameLayout getFLContent();
    LinearLayout getLLBottom();
    RadioGroup getRGTabsContent();
    RadioButton getRBTabHome();
    RadioButton getRBTabNotify();
    RadioButton getRBTabMessage();
    RadioButton getRBTabUser();

}
