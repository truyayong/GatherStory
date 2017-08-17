package com.truyayong.gatherstory.user.view;

import android.support.design.widget.FloatingActionButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by alley_qiu on 2017/3/5.
 */

public interface IUserEditView {

    LinearLayout getLLHeadView();
    CircleImageView getCIVHeadView();
    EditText getETName();
    EditText getETDescripetion();
    RadioGroup getRGGender();
    EditText getETLocation();

}
