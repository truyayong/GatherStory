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
import com.truyayong.gatherstory.user.presenters.UserRegisterPresenter;
import com.truyayong.gatherstory.user.view.IUserRegisterView;

import butterknife.Bind;

public class UserRegisterActivity extends MVPBaseAppCompatActivity<IUserRegisterView, UserRegisterPresenter> implements IUserRegisterView {

    @Bind(R.id.pb_progress_user_register)
    ProgressBar pb_progress_user_register;
    @Bind(R.id.sv_form_user_register)
    ScrollView sv_form_user_register;
    @Bind(R.id.actv_phone_user_register)
    AutoCompleteTextView actv_phone_user_register;
    @Bind(R.id.et_verifycode_user_register)
    EditText et_verifycode_user_register;
    @Bind(R.id.tv_get_verifycode_user_register)
    TextView tv_get_verifycode_user_register;
    @Bind(R.id.et_password_user_register)
    EditText et_password_user_register;
    @Bind(R.id.btn_register_user_register)
    Button btn_register_user_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.displayView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    protected UserRegisterPresenter createPresenter() {
        return new UserRegisterPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_user_register;
    }

    @Override
    public ProgressBar getProcess() {
        return pb_progress_user_register;
    }

    @Override
    public ScrollView getSVForm() {
        return sv_form_user_register;
    }

    @Override
    public AutoCompleteTextView getACTVUserPhone() {
        return actv_phone_user_register;
    }

    @Override
    public EditText getETVerifyCode() {
        return et_verifycode_user_register;
    }

    @Override
    public TextView getTVVerifyCode() {
        return tv_get_verifycode_user_register;
    }

    @Override
    public EditText getETPassword() {
        return et_password_user_register;
    }

    @Override
    public Button getBTNRegister() {
        return btn_register_user_register;
    }
}
