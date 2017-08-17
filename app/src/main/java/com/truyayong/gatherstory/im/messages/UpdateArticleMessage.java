package com.truyayong.gatherstory.im.messages;

import cn.bmob.newim.bean.BmobIMExtraMessage;

/**
 * Created by alley_qiu on 2017/3/13.
 */

public class UpdateArticleMessage extends BmobIMExtraMessage {

    public static final String TYPE_UPDATE_ARTICLE_MSG = "update_aticle_message";

    public UpdateArticleMessage() {
    }

    @Override
    public String getMsgType() {
        return TYPE_UPDATE_ARTICLE_MSG;
    }

    @Override
    public boolean isTransient() {
        //设置为true,表明为暂态消息，那么这条消息并不会保存到对方的本地db中
        //设置为false,则会保存到对方指定会话的本地数据库中
        return true;
    }
}
