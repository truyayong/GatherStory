package com.truyayong.gatherstory.content.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.RadioGroup;

import java.util.List;

/**
 * Created by alley_qiu on 2017/3/6.
 */

public class FragmentTabAdapter implements RadioGroup.OnCheckedChangeListener {

    private List<Fragment> fragments;
    private RadioGroup rgs;
    private FragmentActivity fragmentActivity;
    private int fragmentContentId;

    private int currentTab;

    private OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener;

    public FragmentTabAdapter(FragmentActivity fragmentActivity, List<Fragment> fragments, int fragmentContentId, RadioGroup rgs) {
        this.fragmentActivity = fragmentActivity;
        this.fragments = fragments;
        this.fragmentContentId = fragmentContentId;
        this.rgs = rgs;

        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        ft.add(fragmentContentId, fragments.get(0));
        ft.commit();

        rgs.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < rgs.getChildCount(); i++) {
            if (rgs.getChildAt(i).getId() == checkedId) {
                Fragment fragment = fragments.get(i);
                FragmentTransaction ft = obtainFragmentTransaction(i);
                getCurrentFragment().onPause();
                if (fragment.isAdded()) {
                    fragment.onResume();
                } else {
                    ft.add(fragmentContentId, fragment);
                }
                showTab(i);
                ft.commit();

                if (null != onRgsExtraCheckedChangedListener) {
                    onRgsExtraCheckedChangedListener.OnRgsExtraCheckedChanged(
                            group, checkedId, i);
                }
            }
        }
    }

    /**
     *
     *
     * @param index
     * @return
     */
    private FragmentTransaction obtainFragmentTransaction(int index) {
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager()
                .beginTransaction();
        return ft;
    }

    public Fragment getCurrentFragment() {
        return fragments.get(currentTab);
    }

    private void showTab(int idx) {
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(idx);
            Log.e("FragmentTabAdapter", " i = " + i + " idx = " + idx);

            if (idx == i) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commit();
        }
        currentTab = idx;
    }

    /**
     *
     */
    public static class OnRgsExtraCheckedChangedListener {
        public void OnRgsExtraCheckedChanged(RadioGroup radioGroup,
                                             int checkedId, int index) {

        }
    }

    public void setOnRgsExtraCheckedChangedListener(
            OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener) {
        this.onRgsExtraCheckedChangedListener = onRgsExtraCheckedChangedListener;
    }

    public OnRgsExtraCheckedChangedListener getOnRgsExtraCheckedChangedListener() {
        return onRgsExtraCheckedChangedListener;
    }
}
