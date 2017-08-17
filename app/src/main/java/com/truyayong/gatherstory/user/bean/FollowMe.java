package com.truyayong.gatherstory.user.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by alley_qiu on 2017/3/15.
 */

public class FollowMe extends BmobObject implements Serializable {
    private static final long serialVersionUID = 1003L;

    /**
     * me
     */
    String meId;

    /**
     * 关注me的人
     */
    String followMeId;

    public String getFollowMeId() {
        return followMeId;
    }

    public void setFollowMeId(String followMeId) {
        this.followMeId = followMeId;
    }

    public String getMeId() {
        return meId;
    }

    public void setMeId(String meId) {
        this.meId = meId;
    }
}
