package com.truyayong.gatherstory.splash.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.truyayong.gatherstory.R;

/**
 * Created by alley_qiu on 2017/3/19.
 */

public class LeftFragment extends Fragment {

    private View contentView;
    private ImageView btnRight;
    private View.OnClickListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_left_splash, null);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

//    private void initView() {
//        btnRight = (ImageView) contentView.findViewById(R.id.btn_right);
//        btnRight.setOnClickListener(mListener);
//    }

    public void setPagerOnClickListener(View.OnClickListener listener) {
        mListener = listener;
    }
}
