package com.truyayong.gatherstory.content.bean;

/**
 * Created by alley_qiu on 2017/3/16.
 */

public class RecentArticle {

    private String title;
    private String content;

    public RecentArticle() {
    }

    public RecentArticle(String title, String content) {
        this.content = content;
        this.title = title;
    }

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
