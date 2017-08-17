package com.truyayong.richtext.richview.parser;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by alley_qiu on 2017/3/24.
 */

public class Html2SpannedParser implements SpannedParser {

    private Html.TagHandler tagHandler;

    public Html2SpannedParser(Html.TagHandler tagHandler) {
        this.tagHandler = tagHandler;
    }

    @Override
    public Spanned parse(String source) {
        return Html.fromHtml(source, null, tagHandler);
    }
}
