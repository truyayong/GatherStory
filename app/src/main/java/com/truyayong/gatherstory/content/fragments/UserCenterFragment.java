package com.truyayong.gatherstory.content.fragments;

import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikepenz.entypo_typeface_library.Entypo;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.Iconics;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.GenericFont;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.typeface.ITypeface;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mikepenz.typeicons_typeface_library.Typeicons;
import com.squareup.leakcanary.RefWatcher;
import com.truyayong.gatherstory.App;
import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.MVPBaseFragment;
import com.truyayong.gatherstory.content.presenters.UserCenterFgPresenter;
import com.truyayong.gatherstory.content.view.IUserCenterFgView;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by alley_qiu on 2017/3/6.
 */

public class UserCenterFragment extends MVPBaseFragment<IUserCenterFgView, UserCenterFgPresenter> implements IUserCenterFgView {

    @Bind(R.id.appbar_fragment_usercenter)
    AppBarLayout appbar_fragment_usercenter;
    @Bind(R.id.pb_progress_usercenter)
    ProgressBar pb_progress_usercenter;
    @Bind(R.id.ll_form_usercenter)
    LinearLayout ll_form_usercenter;
    @Bind(R.id.collapsing_toolbar_fragment_usercenter)
    CollapsingToolbarLayout collapsing_toolbar_fragment_usercenter;
    @Bind(R.id.headview_fragment_usercenter)
    CircleImageView headview_fragment_usercenter;

    @Bind(R.id.ll_follow_artcile_fragment_usercenter)
    LinearLayout ll_follow_artcile_fragment_usercenter;
    @Bind(R.id.tv_follow_article_count_fragment_usercenter)
    TextView tv_follow_article_count_fragment_usercenter;
    @Bind(R.id.ll_follow_user_fragment_usercenter)
    LinearLayout ll_follow_user_fragment_usercenter;
    @Bind(R.id.tv_follow_user_fragment_usercenter)
    TextView tv_follow_user_fragment_usercenter;
    @Bind(R.id.ll_followed_user_fragment_usercenter)
    LinearLayout ll_followed_user_fragment_usercenter;
    @Bind(R.id.tv_followed_user_fragment_usercenter)
    TextView tv_followed_user_fragment_usercenter;

    @Bind(R.id.tv_user_description_fragment_usercenter)
    TextView tv_user_description_fragment_usercenter;
    @Bind(R.id.tv_user_location_fragment_usercenter)
    TextView tv_user_location_fragment_usercenter;
    @Bind(R.id.tv_user_gender_fragment_usercenter)
    TextView tv_user_gender_fragment_usercenter;
    @Bind(R.id.rl_user_write_article_fragment_usercenter)
    RelativeLayout rl_user_write_article_fragment_usercenter;
    @Bind(R.id.rl_user_write_sentence_fragment_usercenter)
    RelativeLayout rl_user_write_sentence_fragment_usercenter;
    @Bind(R.id.rl_user_recent_read_fragment_usercenter)
    RelativeLayout rl_user_recent_read_fragment_usercenter;
    @Bind(R.id.rl_user_about_truyayong_fragment_usercenter)
    RelativeLayout rl_user_about_truyayong_fragment_usercenter;

    @Bind(R.id.fab_fragment_usercenter)
    FloatingActionButton fab_fragment_usercenter;

    @Bind(R.id.iv_user_article_fragment_usercenter)
    ImageView iv_user_article_fragment_usercenter;
    @Bind(R.id.iv_user_sentence_fragment_usercenter)
    ImageView iv_user_sentence_fragment_usercenter;
    @Bind(R.id.iv_user_recent_fragment_usercenter)
    ImageView iv_user_recent_fragment_usercenter;
    @Bind(R.id.iv_about_truyayong_fragment_usercenter)
    ImageView iv_about_truyayong_fragment_usercenter;

    @Override
    protected UserCenterFgPresenter createPresenter() {
        return new UserCenterFgPresenter(getContext());
    }

    @Override
    protected int createViewLayoutId() {
        return R.layout.fragment_usercenter;
    }

    @Override
    protected void initView(View rootView) {
        iv_user_article_fragment_usercenter.setImageDrawable(new IconicsDrawable(getContext()).
                icon(Entypo.Icon.ent_flow_cascade).sizeDp(24).color(Color.parseColor("#e0e0e0")));
        iv_user_sentence_fragment_usercenter.setImageDrawable(new IconicsDrawable(getContext()).
                icon(Entypo.Icon.ent_flow_branch).sizeDp(24).color(Color.parseColor("#e0e0e0")));
        iv_user_recent_fragment_usercenter.setImageDrawable(new IconicsDrawable(getContext()).
                icon(Entypo.Icon.ent_documents).sizeDp(24).color(Color.parseColor("#e0e0e0")));
        iv_about_truyayong_fragment_usercenter.setImageDrawable(new IconicsDrawable(getContext()).
                icon(Entypo.Icon.ent_key).sizeDp(24).color(Color.parseColor("#e0e0e0")));
        fab_fragment_usercenter.setImageDrawable(new IconicsDrawable(getContext()).
                icon(Entypo.Icon.ent_brush).sizeDp(24).color(Color.parseColor("#e0e0e0")));
        mPresenter.initView();
    }

    @Override
    public AppBarLayout getAppBar() {
        return appbar_fragment_usercenter;
    }

    @Override
    public CollapsingToolbarLayout getCollapsingToolbarLayout() {
        return collapsing_toolbar_fragment_usercenter;
    }

    @Override
    public CircleImageView getHeadView() {
        return headview_fragment_usercenter;
    }

    @Override
    public ProgressBar getPBProcess() {
        return pb_progress_usercenter;
    }

    @Override
    public LinearLayout getLLForm() {
        return ll_form_usercenter;
    }

    @Override
    public LinearLayout getLLFollowArticle() {
        return ll_follow_artcile_fragment_usercenter;
    }

    @Override
    public TextView getTVFollowArticle() {
        return tv_follow_article_count_fragment_usercenter;
    }

    @Override
    public LinearLayout getLLFollowUser() {
        return ll_follow_user_fragment_usercenter;
    }

    @Override
    public TextView getTVFollowUser() {
        return tv_follow_user_fragment_usercenter;
    }

    @Override
    public LinearLayout getLLFollowedUser() {
        return ll_followed_user_fragment_usercenter;
    }

    @Override
    public TextView getTVFollowedUser() {
        return tv_followed_user_fragment_usercenter;
    }

    @Override
    public TextView getTVUserDescription() {
        return tv_user_description_fragment_usercenter;
    }

    @Override
    public TextView getTVUserLocation() {
        return tv_user_location_fragment_usercenter;
    }

    @Override
    public TextView getUserGender() {
        return tv_user_gender_fragment_usercenter;
    }

    @Override
    public RelativeLayout getRLWriteArticle() {
        return rl_user_write_article_fragment_usercenter;
    }

    @Override
    public RelativeLayout getRLWriteSentence() {
        return rl_user_write_sentence_fragment_usercenter;
    }

    @Override
    public RelativeLayout getRLRecentRead() {
        return rl_user_recent_read_fragment_usercenter;
    }

    @Override
    public RelativeLayout getRLAboutTruyayong() {
        return rl_user_about_truyayong_fragment_usercenter;
    }

    @Override
    public FloatingActionButton getFAB() {
        return fab_fragment_usercenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = App.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
