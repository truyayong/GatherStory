package com.truyayong.gatherstory.user.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.MVPBaseAppCompatActivity;
import com.truyayong.gatherstory.user.presenters.UserEditPresenter;
import com.truyayong.gatherstory.user.view.IUserEditView;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserEditActivity extends MVPBaseAppCompatActivity<IUserEditView, UserEditPresenter> implements IUserEditView {

    @Bind(R.id.ll_head_view_user_edit)
    LinearLayout ll_head_view_user_edit;
    @Bind(R.id.civ_head_view_user_edit)
    CircleImageView civ_head_view_user_edit;
    @Bind(R.id.et_name_user_edit)
    EditText et_name_user_edit;
    @Bind(R.id.et_description_user_edit)
    EditText et_description_user_edit;
    @Bind(R.id.rg_gender_user_edit)
    RadioGroup rg_gender_user_edit;
    @Bind(R.id.et_location_user_edit)
    EditText et_location_user_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("个人设置");
        mPresenter.displayView();
    }

    @Override
    protected UserEditPresenter createPresenter() {
        return new UserEditPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_user_edit;
    }

    @Override
    public LinearLayout getLLHeadView() {
        return ll_head_view_user_edit;
    }

    @Override
    public CircleImageView getCIVHeadView() {
        return civ_head_view_user_edit;
    }

    @Override
    public EditText getETName() {
        return et_name_user_edit;
    }

    @Override
    public EditText getETDescripetion() {
        return et_description_user_edit;
    }

    @Override
    public RadioGroup getRGGender() {
        return rg_gender_user_edit;
    }

    @Override
    public EditText getETLocation() {
        return et_location_user_edit;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mPresenter.onActivityResult(requestCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mPresenter.onCreateOptionsMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPresenter.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }
}
