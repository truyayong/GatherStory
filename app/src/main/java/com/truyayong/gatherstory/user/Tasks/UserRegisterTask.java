package com.truyayong.gatherstory.user.Tasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.truyayong.gatherstory.user.activities.UserLoginActivity;
import com.truyayong.gatherstory.user.bean.User;
import com.truyayong.gatherstory.user.presenters.UserRegisterPresenter;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by alley_qiu on 2017/3/5.
 */

public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

    private final String mTaskPhone;
    private final String mTaskPassword;
    private final String mTaskVerifycode;
    private boolean doBackgroundResult = false;
    private Context mContext;
    private UserRegisterPresenter mPresenter;
    UserRegisterTask mAuthTask;

    public UserRegisterTask(Context context, UserRegisterPresenter presenter, String phone, String password, String verifycode) {
        this.mContext = context;
        this.mPresenter = presenter;
        this.mTaskPassword = password;
        this.mTaskPhone = phone;
        this.mTaskVerifycode = verifycode;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        // TODO: attempt authentication against a network service.

        User mUser = new User();
        mUser.setMobilePhoneNumber(mTaskPhone);
        mUser.setPassword(mTaskPassword);
        mUser.signOrLogin(mTaskVerifycode, new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    mPresenter.showProgress(false);
                    doBackgroundResult = true;
                    Intent intent = new Intent(mContext, UserLoginActivity.class);
                    mContext.startActivity(intent);
                } else {
                    mPresenter.showProgress(false);
                    doBackgroundResult = false;
                    Toast.makeText(mContext, "注册失败", Toast.LENGTH_SHORT).show();
                    Log.e("UserRegisterTask", "exception : " + e.toString());
                }
            }
        });

        // TODO: register the new account here.
        return doBackgroundResult;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        mAuthTask = null;

        if (success) {
            ((Activity)mContext).finish();
        } else {
            mPresenter.getView().getETPassword().requestFocus();
        }
    }

    @Override
    protected void onCancelled() {
        mAuthTask = null;
        mPresenter.showProgress(false);
    }

    public void cancel() {
        onCancelled();
    }
}
