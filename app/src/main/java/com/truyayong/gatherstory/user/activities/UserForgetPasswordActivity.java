package com.truyayong.gatherstory.user.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.MVPBaseAppCompatActivity;
import com.truyayong.gatherstory.user.presenters.UserForgetPasswordPresenter;
import com.truyayong.gatherstory.user.view.IUserForgetPasswordView;

import butterknife.Bind;

public class UserForgetPasswordActivity extends MVPBaseAppCompatActivity<IUserForgetPasswordView, UserForgetPasswordPresenter> implements  IUserForgetPasswordView {

    @Bind(R.id.pb_progress_forget_password)
    ProgressBar pb_progress_forget_password;
    @Bind(R.id.sv_form_forget_password)
    ScrollView sv_form_forget_password;
    @Bind(R.id.actv_phone_forget_password)
    AutoCompleteTextView actv_phone_forget_password;
    @Bind(R.id.et_verifycode_forget_password)
    EditText et_verifycode_forget_password;
    @Bind(R.id.tv_verifycode_forget_password)
    TextView tv_verifycode_forget_password;
    @Bind(R.id.et_password_forget_password)
    EditText et_password_forget_password;
    @Bind(R.id.et_re_password_forget_password)
    EditText et_re_password_forget_password;
    @Bind(R.id.btn_forget_password)
    Button btn_forget_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.displayView();
    }

    @Override
    protected UserForgetPasswordPresenter createPresenter() {
        return new UserForgetPasswordPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_user_forget_password;
    }

    @Override
    public ProgressBar getProcess() {
        return pb_progress_forget_password;
    }

    @Override
    public ScrollView getSVForm() {
        return sv_form_forget_password;
    }

    @Override
    public AutoCompleteTextView getACTVUserPhone() {
        return actv_phone_forget_password;
    }

    @Override
    public EditText getETVerifyCode() {
        return et_verifycode_forget_password;
    }

    @Override
    public TextView getTVVerifyCode() {
        return tv_verifycode_forget_password;
    }

    @Override
    public EditText getETPassword() {
        return et_password_forget_password;
    }

    @Override
    public EditText getETRePassword() {
        return et_re_password_forget_password;
    }

    @Override
    public Button getBTNForgetPassword() {
        return btn_forget_password;
    }
}
