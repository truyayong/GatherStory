package com.truyayong.gatherstory.user.presenters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.BasePresenter;
import com.truyayong.gatherstory.user.Tasks.UserRegisterTask;
import com.truyayong.gatherstory.user.view.IUserRegisterView;
import com.truyayong.gatherstory.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by alley_qiu on 2017/3/5.
 */

public class UserRegisterPresenter extends BasePresenter<IUserRegisterView> {

    private Context mContext;
    private UserRegisterTask mAuthTask;

    String mPhone = "";
    String mPassword = "";
    String mVerifyCode = "";

    CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            if (millisUntilFinished / 1000 == 60) {
                millisUntilFinished = 59 * 1000;
            }
            getView().getTVVerifyCode().setText("重新发送" + "(" + millisUntilFinished / 1000 + ")");
            getView().getTVVerifyCode().setEnabled(false);
        }

        @Override
        public void onFinish() {
            getView().getTVVerifyCode().setText("获取验证码");
            getView().getTVVerifyCode().setEnabled(true);
        }
    };

    public UserRegisterPresenter(Context context) {
        this.mContext = context;
    }

    public void displayView() {
        populateAutoComplete();
        getView().getETPassword().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.login || actionId == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        getView().getTVVerifyCode().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = getView().getACTVUserPhone().getText().toString();
                Log.e("smile", "短信id："+phone);
                if (!StringUtil.isPhoneValid(phone)) {
                    getView().getACTVUserPhone().setError("手机号输入有误");
                    return;
                }
                BmobSMS.requestSMSCode(phone, "Sentences", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if (e == null) {
                            Log.e("smile", "短信id："+integer);//用于后续的查询本次短信发送状态
                        } else {
                            Log.e("smile", "短信发送失败id："+integer);
                        }
                    }
                });
                countDownTimer.start();
            }
        });

        getView().getBTNRegister().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("smile", "register button：");
                SharedPreferences sp = mContext.getSharedPreferences("phone_history", mContext.MODE_APPEND);
                Set<String> historys = sp.getStringSet("history", new HashSet<String>());
                historys.add(getView().getACTVUserPhone().getText().toString());
                SharedPreferences.Editor editor = sp.edit();
                editor.putStringSet("history", historys).commit();
                attemptRegister();
            }
        });
    }
    private void populateAutoComplete() {
        SharedPreferences sp = mContext.getSharedPreferences("phone_history", mContext.MODE_PRIVATE);
        Set<String> historys = sp.getStringSet("history", new HashSet<String>());
        addPhonesToAutoComplete(new ArrayList<String>(historys));
        return;
    }

    private void addPhonesToAutoComplete(List<String> phoneNumberCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(mContext,
                        android.R.layout.simple_dropdown_item_1line, phoneNumberCollection);
        getView().getACTVUserPhone().setAdapter(adapter);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        getView().getACTVUserPhone().setError(null);
        getView().getETPassword().setError(null);
        getView().getETVerifyCode().setError(null);

        // Store values at the time of the login attempt.
        mPhone = getView().getACTVUserPhone().getText().toString();
        mPassword = getView().getETPassword().getText().toString();
        mVerifyCode = getView().getETVerifyCode().getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(mPassword) || !StringUtil.isPasswordValid(mPassword)) {
            getView().getETPassword().setError("密码错误");
            focusView = getView().getETPassword();
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mPhone)) {
            getView().getACTVUserPhone().setError("号码错误");
            focusView = getView().getACTVUserPhone();
            cancel = true;
        } else if (!StringUtil.isPhoneValid(mPhone)) {
            getView().getACTVUserPhone().setError("号码不能为空");
            focusView = getView().getACTVUserPhone();
            cancel = true;
        }

        if (!StringUtil.isVerifyCodeValid(mVerifyCode)) {
            getView().getETVerifyCode().setError("验证码错误");
            focusView = getView().getETVerifyCode();
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserRegisterTask(mContext, this, mPhone, mPassword, mVerifyCode);
            mAuthTask.execute((Void) null);
        }
    }
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = mContext.getResources().getInteger(android.R.integer.config_shortAnimTime);

            getView().getSVForm().setVisibility(show ? View.GONE : View.VISIBLE);
            getView().getSVForm().animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    getView().getSVForm().setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            getView().getProcess().setVisibility(show ? View.VISIBLE : View.GONE);
            getView().getProcess().animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    getView().getProcess().setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            getView().getProcess().setVisibility(show ? View.VISIBLE : View.GONE);
            getView().getSVForm().setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void destroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (mAuthTask != null) {
            mAuthTask.cancel();
        }
    }
}
