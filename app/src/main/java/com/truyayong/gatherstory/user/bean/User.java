package com.truyayong.gatherstory.user.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by alley_qiu on 2017/3/5.
 */

public class User extends BmobUser implements Serializable {
    private static final long serialVersionUID = 1002L;
    /**
     * 用户名
     */
    String userName;
    /**
     * 性别
     */
    Boolean gender;
    /**
     * 用户签名
     */
    String userDescription;
    /**
     * 用户头像
     */
    String userHeadUrl;
    /**
     * 居住地
     */
    String userLocation;


    /**
     * Ta关注的人
     */
    BmobRelation followUser;

    /**
     * Ta关注的人数
     */
    Integer followUserCount;
    /**
     * Ta关注的文章
     */
    BmobRelation followArticle;

    /**
     * Ta关注的文章数
     */
    Integer followArticleCount;

    /**
     * Ta点赞的文章
     */
    BmobRelation favoriteArticle;

    /**
     * Ta点赞的文章数
     */
    Integer favoriteArticleCount;

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getUserHeadUrl() {
        return userHeadUrl;
    }

    public void setUserHeadUrl(String userHeadUrl) {
        this.userHeadUrl = userHeadUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String location) {
        this.userLocation = location;
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

    public Integer getFollowArticleCount() {
        return followArticleCount;
    }

    public void setFollowArticleCount(Integer followArticleCount) {
        this.followArticleCount = followArticleCount;
    }

    public BmobRelation getFollowArticle() {
        return followArticle;
    }

    public void setFollowArticle(BmobRelation followArticle) {
        this.followArticle = followArticle;
    }

    public BmobRelation getFavoriteArticle() {
        return favoriteArticle;
    }

    public void setFavoriteArticle(BmobRelation favoriteArticle) {
        this.favoriteArticle = favoriteArticle;
    }

    public Integer getFavoriteArticleCount() {
        return favoriteArticleCount;
    }

    public void setFavoriteArticleCount(Integer favoriteArticleCount) {
        this.favoriteArticleCount = favoriteArticleCount;
    }
}
