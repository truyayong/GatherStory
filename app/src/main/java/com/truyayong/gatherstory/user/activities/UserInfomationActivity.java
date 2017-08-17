package com.truyayong.gatherstory.user.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikepenz.entypo_typeface_library.Entypo;
import com.mikepenz.iconics.IconicsDrawable;
import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.MVPBaseAppCompatActivity;
import com.truyayong.gatherstory.base.MVPBaseFragmentActivity;
import com.truyayong.gatherstory.user.bean.User;
import com.truyayong.gatherstory.user.presenters.UserInfomationPresenter;
import com.truyayong.gatherstory.user.view.IUserInformationView;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfomationActivity extends MVPBaseFragmentActivity<IUserInformationView, UserInfomationPresenter> implements IUserInformationView {

    @Bind(R.id.collapsing_toolbar_user_info)
    CollapsingToolbarLayout collapsing_toolbar_user_info;
    @Bind(R.id.toolbar_user_info)
    Toolbar toolbar_user_info;
    @Bind(R.id.headview_user_info)
    CircleImageView headview_user_info;

    @Bind(R.id.pb_progress_user_info)
    ProgressBar pb_progress_user_info;
    @Bind(R.id.ll_form_user_info)
    LinearLayout ll_form_user_info;

    @Bind(R.id.ll_follow_article_user_info)
    LinearLayout ll_follow_article_user_info;
    @Bind(R.id.tv_follow_article_count_user_info)
    TextView tv_follow_article_count_user_info;
    @Bind(R.id.ll_follow_user_user_info)
    LinearLayout ll_follow_user_user_info;
    @Bind(R.id.tv_follow_user_user_info)
    TextView tv_follow_user_user_info;
    @Bind(R.id.ll_followed_user_user_info)
    LinearLayout ll_followed_user_user_info;
    @Bind(R.id.tv_followed_user_user_info)
    TextView tv_followed_user_user_info;
    @Bind(R.id.tv_description_user_info)
    TextView tv_description_user_info;
    @Bind(R.id.tv_user_location_user_info)
    TextView tv_user_location_user_info;
    @Bind(R.id.tv_user_gender_user_info)
    TextView tv_user_gender_user_info;
    @Bind(R.id.btn_action_follow_user_info)
    Button btn_action_follow_user_info;
    @Bind(R.id.rl_write_aticle_user_info)
    RelativeLayout rl_write_aticle_user_info;
    @Bind(R.id.rl_write_sentence_user_info)
    RelativeLayout rl_write_sentence_user_info;
    @Bind(R.id.rl_send_message_user_info)
    RelativeLayout rl_send_message_user_info;

    @Bind(R.id.iv_write_article_user_info)
    ImageView iv_write_article_user_info;
    @Bind(R.id.iv_write_sentence_user_info)
    ImageView iv_write_sentence_user_info;
    @Bind(R.id.iv_send_message_user_info)
    ImageView iv_send_message_user_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.initView();
        iv_write_article_user_info.setImageDrawable(new IconicsDrawable(this).
                icon(Entypo.Icon.ent_flow_cascade).sizeDp(24).color(Color.parseColor("#e0e0e0")));
        iv_write_sentence_user_info.setImageDrawable(new IconicsDrawable(this).
                icon(Entypo.Icon.ent_flow_branch).sizeDp(24).color(Color.parseColor("#e0e0e0")));
        iv_send_message_user_info.setImageDrawable(new IconicsDrawable(this).icon(Entypo.Icon.ent_popup).sizeDp(24).color(Color.parseColor("#e0e0e0")));
    }


    @Override
    protected UserInfomationPresenter createPresenter() {
        return new UserInfomationPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_user_infomation;
    }


    @Override
    public CollapsingToolbarLayout getCollapsingToolbarLayout() {
        return collapsing_toolbar_user_info;
    }

    @Override
    public CircleImageView getCIVHeadview() {
        return headview_user_info;
    }

    @Override
    public ProgressBar getPBProcess() {
        return pb_progress_user_info;
    }

    @Override
    public LinearLayout getLLForm() {
        return ll_form_user_info;
    }

    @Override
    public LinearLayout getLLFollowArticle() {
        return ll_follow_article_user_info;
    }

    @Override
    public TextView getTVFollowArticleCount() {
        return tv_follow_article_count_user_info;
    }

    @Override
    public LinearLayout getLLFollowUser() {
        return ll_follow_user_user_info;
    }

    @Override
    public TextView getTVFollowUser() {
        return tv_follow_user_user_info;
    }

    @Override
    public LinearLayout getLLFollowedUser() {
        return ll_followed_user_user_info;
    }

    @Override
    public TextView getTVFollowedUser() {
        return tv_followed_user_user_info;
    }

    @Override
    public TextView getTVDescription() {
        return tv_description_user_info;
    }

    @Override
    public TextView getTVUserLocation() {
        return tv_user_location_user_info;
    }

    @Override
    public TextView getTVUserGender() {
        return tv_user_gender_user_info;
    }

    @Override
    public Button getBTNActionFollow() {
        return btn_action_follow_user_info;
    }

    @Override
    public RelativeLayout getRLWriteArticle() {
        return rl_write_aticle_user_info;
    }

    @Override
    public RelativeLayout getRLWriteSentence() {
        return rl_write_sentence_user_info;
    }

    @Override
    public RelativeLayout getRLSendMessage() {
        return rl_send_message_user_info;
    }
}
