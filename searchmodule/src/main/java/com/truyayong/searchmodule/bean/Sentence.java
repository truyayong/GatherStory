package com.truyayong.searchmodule.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by alley_qiu on 2017/3/21.
 */

public class Sentence extends BmobObject implements Serializable {

    private static final long serialVersionUID = 1001L;
    /**
     * 所属文章
     */
    Article article;

    /**
     * 所属文章标题
     */
    String title;

    /**
     * 节点内容
     */
    String content;

    /**
     * 作者
     */
    User author;

    /**
     * 作者用户名
     */
    String authorName;

    /**
     * 作者头像
     */
    String authorHeadUrl;


    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorHeadUrl() {
        return authorHeadUrl;
    }

    public void setAuthorHeadUrl(String authorHeadUrl) {
        this.authorHeadUrl = authorHeadUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
