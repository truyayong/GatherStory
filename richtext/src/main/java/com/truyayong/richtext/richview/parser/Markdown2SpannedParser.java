package com.truyayong.richtext.richview.parser;

import android.text.Spanned;
import android.widget.TextView;

import com.zzhoujay.markdown.MarkDown;

/**
 * Created by alley_qiu on 2017/3/24.
 */

public class Markdown2SpannedParser implements SpannedParser {

    private TextView textView;

    public Markdown2SpannedParser(TextView textView) {
        this.textView = textView;
    }

    @Override
    public Spanned parse(String source) {
        return MarkDown.fromMarkdown(source, null, textView);
    }
}
