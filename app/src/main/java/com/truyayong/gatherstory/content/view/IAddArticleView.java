package com.truyayong.gatherstory.content.view;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by alley_qiu on 2017/3/6.
 */

public interface IAddArticleView {

    EditText getETTitle();
    TextView getTVWarning();
    RichEditor getREContent();
    ImageButton getIBSetting();
    ImageButton getIBLink();
    ImageButton getIBPhoto();

}
