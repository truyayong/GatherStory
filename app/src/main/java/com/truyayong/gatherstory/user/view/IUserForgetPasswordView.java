package com.truyayong.gatherstory.user.view;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by alley_qiu on 2017/3/5.
 */

public interface IUserForgetPasswordView {

    ProgressBar getProcess();
    ScrollView getSVForm();
    AutoCompleteTextView getACTVUserPhone();
    EditText getETVerifyCode();
    TextView getTVVerifyCode();
    EditText getETPassword();
    EditText getETRePassword();
    Button getBTNForgetPassword();
}
