package com.truyayong.gatherstory.user.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.MVPBaseAppCompatActivity;
import com.truyayong.gatherstory.user.presenters.UserLoginPresenter;
import com.truyayong.gatherstory.user.view.IUserLoginView;

import butterknife.Bind;

public class UserLoginActivity extends MVPBaseAppCompatActivity<IUserLoginView, UserLoginPresenter> implements IUserLoginView {

    @Bind(R.id.pb_progress_user_login)
    ProgressBar pb_progress_user_login;
    @Bind(R.id.sv_form_user_login)
    ScrollView sv_form_user_login;
    @Bind(R.id.actv_user_phone_user_login)
    AutoCompleteTextView actv_user_phone_user_login;
    @Bind(R.id.et_password_user_login)
    EditText et_password_user_login;
    @Bind(R.id.btn_forget_password_user_login)
    Button btn_forget_password_user_login;
    @Bind(R.id.btn_sign_in_user_login)
    Button btn_sign_in_user_login;
    @Bind(R.id.btn_register_user_login)
    Button btn_register_user_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        mPresenter.displayView();
    }

    @Override
    protected UserLoginPresenter createPresenter() {
        return new UserLoginPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_user_login;
    }

    @Override
    public ProgressBar getProcess() {
        return pb_progress_user_login;
    }

    @Override
    public ScrollView getSVForm() {
        return sv_form_user_login;
    }

    @Override
    public AutoCompleteTextView getACTVUserPhone() {
        return actv_user_phone_user_login;
    }

    @Override
    public EditText getETPassword() {
        return et_password_user_login;
    }

    @Override
    public Button getBTNForgetPassword() {
        return btn_forget_password_user_login;
    }

    @Override
    public Button getBTNLogin() {
        return btn_sign_in_user_login;
    }

    @Override
    public Button getBTNRegister() {
        return btn_register_user_login;
    }
}
