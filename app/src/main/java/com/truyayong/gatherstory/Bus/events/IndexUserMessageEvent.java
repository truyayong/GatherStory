package com.truyayong.gatherstory.Bus.events;

/**
 * Created by alley_qiu on 2017/3/18.
 */

public class IndexUserMessageEvent {

    private String userId;
    private String userName;
    private String userHead;
    private String message;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
