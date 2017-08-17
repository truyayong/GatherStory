package com.truyayong.gatherstory.im.messages;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.newim.bean.BmobIMExtraMessage;
import cn.bmob.newim.bean.BmobIMMessage;

/**
 * Created by alley_qiu on 2017/3/13.
 */

public class UpdateSentenceMessage  extends BmobIMExtraMessage{

    public static final String TYPE_UPDATE_SENTENCE_MSG = "update_aticle_message";

    private String title;
    private String content;

    public UpdateSentenceMessage() {
    }

    private UpdateSentenceMessage(BmobIMMessage msg) {
        super.parse(msg);
    }

    public static UpdateSentenceMessage convert(BmobIMMessage msg) {
        UpdateSentenceMessage update = new UpdateSentenceMessage(msg);
        String extra = msg.getExtra();
        try {
            if (!TextUtils.isEmpty(extra)) {
                JSONObject json = new JSONObject(extra);
                String msgTitle = json.getString("title");
                String msgContent = json.getString("content");
                update.setTitle(msgTitle);
                update.setContent(msgContent);
            } else {
                Log.e("truyayong", "UpdateSentenceMessage的extra为空");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return update;
    }

    @Override
    public String getMsgType() {
        return TYPE_UPDATE_SENTENCE_MSG;
    }

    @Override
    public boolean isTransient() {
        //设置为true,表明为暂态消息，那么这条消息并不会保存到对方的本地db中
        //设置为false,则会保存到对方指定会话的本地数据库中
        return true;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
