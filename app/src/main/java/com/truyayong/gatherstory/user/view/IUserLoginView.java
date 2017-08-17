package com.truyayong.gatherstory.user.view;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

/**
 * Created by alley_qiu on 2017/3/4.
 */

public interface IUserLoginView {

    ProgressBar getProcess();
    ScrollView getSVForm();
    AutoCompleteTextView getACTVUserPhone();
    EditText getETPassword();
    Button getBTNForgetPassword();
    Button getBTNLogin();
    Button getBTNRegister();
}
