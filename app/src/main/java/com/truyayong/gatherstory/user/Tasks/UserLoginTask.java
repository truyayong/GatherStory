package com.truyayong.gatherstory.user.Tasks;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import com.truyayong.gatherstory.content.activities.IndexActivity;
import com.truyayong.gatherstory.user.activities.UserLoginActivity;
import com.truyayong.gatherstory.user.bean.User;
import com.truyayong.gatherstory.user.presenters.UserLoginPresenter;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * Created by alley_qiu on 2017/3/5.
 */

public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private final String mPhone;
    private final String mPassword;
    private boolean doBackgroundResult = false;
    private UserLoginPresenter mPresenter;
    private Context mContext;


    public UserLoginTask(Context context, UserLoginPresenter presenter, String phone, String password) {
        this.mContext = context;
        this.mPassword = password;
        this.mPhone = phone;
        this.mPresenter = presenter;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        // TODO: attempt authentication against a network service.
        // Simulate network access.
        BmobUser.loginByAccount(mPhone, mPassword, new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    mPresenter.showProgress(false);
                    doBackgroundResult = true;
                    Intent intent = new Intent(mContext, IndexActivity.class);
                    mContext.startActivity(intent);
                } else {
                    mPresenter.showProgress(false);
                    Toast.makeText(mContext, "login fail", Toast.LENGTH_SHORT).show();
                    doBackgroundResult = false;
                }
            }
        });

        // TODO: register the new account here.
        return doBackgroundResult;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        mPresenter.mAuthTask = null;

        if (success) {
            Activity activity = (Activity) mPresenter.getView();
            activity.finish();
        } else {
            //mPasswordView.setError(getString(R.string.error_login));
            mPresenter.getView().getETPassword().requestFocus();
        }
    }

    @Override
    protected void onCancelled() {
        mPresenter.mAuthTask = null;
        mPresenter.showProgress(false);
    }

    public void cancel() {
        onCancelled();
    }
}
