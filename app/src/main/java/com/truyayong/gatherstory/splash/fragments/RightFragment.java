package com.truyayong.gatherstory.splash.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.GenericFont;
import com.truyayong.gatherstory.R;

/**
 * Created by alley_qiu on 2017/3/19.
 */

public class RightFragment extends Fragment {

    private View contentView;
    private ImageView btnLeft;
    private View.OnClickListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_right_splash, null);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        btnLeft = (ImageView) contentView.findViewById(R.id.btn_right_left);
        btnLeft.setImageDrawable(new IconicsDrawable(getContext()).icon(FontAwesome.Icon.faw_quote_left).color(Color.parseColor("#6D4A41")).sizeDp(24));
        btnLeft.setOnClickListener(mListener);
    }

    public void setPagerOnClickListener(View.OnClickListener listener) {
        mListener = listener;
    }
}
