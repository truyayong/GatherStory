package com.truyayong.gatherstory.Bus.events;

/**
 * Created by alley_qiu on 2017/3/23.
 */

public class FgUpdateSentenceEvent {

    String title;
    String content;

    public String getContent() {
        return content;
    }

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
