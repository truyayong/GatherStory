package com.truyayong.gatherstory.content.bean;

import com.truyayong.gatherstory.user.bean.User;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by alley_qiu on 2017/3/6.
 */

public class Article extends BmobObject implements Serializable {

    private static final long serialVersionUID = 1000L;
    /**
     * 文章标题
     */
    String title;

    /**
     * 作者
     */
    User author;

    /**
     * 作者姓名
     */
    String authorName;

    /**
     * 作者头像Url
     */
    String authorHeadUrl;

    /**
     * 点赞用户
     */
    BmobRelation favoriteUser;

    /**
     * 赞数
     */
    Integer favoriteCount;

    /**
     * 关注者
     */
    BmobRelation followUser;

    /**
     * 关注人数
     */
    Integer followUserCount;

    /**
     * 文章首个节点内容
     */
    String content;


    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Integer getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Integer favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public BmobRelation getFollowUser() {
        return followUser;
    }

    public void setFollowUser(BmobRelation followUser) {
        this.followUser = followUser;
    }

    public Integer getFollowUserCount() {
        return followUserCount;
    }

    public void setFollowUserCount(Integer followUserCount) {
        this.followUserCount = followUserCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public BmobRelation getFavoriteUser() {
        return favoriteUser;
    }

    public void setFavoriteUser(BmobRelation favoriteUser) {
        this.favoriteUser = favoriteUser;
    }

}
