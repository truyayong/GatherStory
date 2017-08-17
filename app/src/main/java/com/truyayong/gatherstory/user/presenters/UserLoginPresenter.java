package com.truyayong.gatherstory.user.presenters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.truyayong.gatherstory.R;
import com.truyayong.gatherstory.base.BasePresenter;
import com.truyayong.gatherstory.user.Tasks.UserLoginTask;
import com.truyayong.gatherstory.user.activities.UserForgetPasswordActivity;
import com.truyayong.gatherstory.user.activities.UserRegisterActivity;
import com.truyayong.gatherstory.user.view.IUserLoginView;
import com.truyayong.gatherstory.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by alley_qiu on 2017/3/4.
 */

public class UserLoginPresenter extends BasePresenter<IUserLoginView> {

    private Context mContext;
    public UserLoginTask mAuthTask;

    public UserLoginPresenter(Context context) {
        mContext = context;
    }

    public void displayView() {
        populateAutoComplete();
        getView().getETPassword().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.login || actionId == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return true;
            }
        });

        getView().getBTNLogin().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = mContext.getSharedPreferences("phone_history", mContext.MODE_APPEND);
                Set<String> historys = sp.getStringSet("history", new HashSet<String>());
                historys.add(getView().getACTVUserPhone().getText().toString());
                SharedPreferences.Editor editor = sp.edit();
                editor.putStringSet("history", historys).commit();
                attemptLogin();
            }
        });

        getView().getBTNForgetPassword().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserForgetPasswordActivity.class);
                mContext.startActivity(intent);
            }
        });

        getView().getBTNRegister().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserRegisterActivity.class);
                mContext.startActivity(intent);
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

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        getView().getACTVUserPhone().setError(null);
        getView().getETPassword().setError(null);

        // Store values at the time of the login attempt.
        String phone = getView().getACTVUserPhone().getText().toString();
        String password = getView().getETPassword().getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !StringUtil.isPasswordValid(password)) {
            getView().getETPassword().setError("密码错误");
            focusView = getView().getETPassword();
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(phone)) {
            getView().getACTVUserPhone().setError("电话号码错误");
            focusView = getView().getACTVUserPhone();
            cancel = true;
        } else if (!StringUtil.isPhoneValid(phone)) {
            getView().getACTVUserPhone().setError("电话号码错误");
            focusView = getView().getACTVUserPhone();
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
            mAuthTask = new UserLoginTask(mContext, this, phone, password);
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
        if (mAuthTask != null) {
            mAuthTask.cancel();
        }
    }
}
