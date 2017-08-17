package com.truyayong.gatherstory.user.Tasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.truyayong.gatherstory.user.activities.UserForgetPasswordActivity;
import com.truyayong.gatherstory.user.activities.UserLoginActivity;
import com.truyayong.gatherstory.user.presenters.UserForgetPasswordPresenter;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by alley_qiu on 2017/3/5.
 */

public class UserForgetTask extends AsyncTask<Void, Void, Boolean> {

    private UserForgetPasswordPresenter mPresenter;
    private Context mContext;

    private final String mTaskPhone;
    private final String mTaskPassword;
    private final String mTaskVerifycode;
    private boolean doBackgroudResult = false;

    public UserForgetTask(Context context, UserForgetPasswordPresenter presenter, String phone, String password, String verifycode) {
        mContext = context;
        mPresenter = presenter;
        mTaskPhone = phone;
        mTaskPassword = password;
        mTaskVerifycode = verifycode;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        // TODO: attempt authentication against a network service.
        BmobUser.resetPasswordBySMSCode(mTaskVerifycode, mTaskPassword, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mPresenter.showProgress(false);
                    doBackgroudResult = true;
                    Intent intent = new Intent(mContext, UserLoginActivity.class);
                    mContext.startActivity(intent);
                    Toast.makeText(mContext, "重置成功", Toast.LENGTH_LONG).show();
                } else {
                    mPresenter.showProgress(false);
                    Toast.makeText(mContext, "重置失败", Toast.LENGTH_LONG).show();
                    Log.e("UserFogetActivity", " exception " + e.toString());
                    doBackgroudResult = false;
                }
            }
        });

        // TODO: register the new account here.
        return doBackgroudResult;
    }

    @Override
    protected void onPostExecute(Boolean success) {

        if (success) {
            ((Activity)mContext).finish();
        } else {
            mPresenter.getView().getETRePassword().setError("重置密码失败");
            mPresenter.getView().getETRePassword().requestFocus();
        }
    }

    @Override
    protected void onCancelled() {
        mPresenter.showProgress(false);
    }

    public void cancel() {
        onCancelled();
    }
}
