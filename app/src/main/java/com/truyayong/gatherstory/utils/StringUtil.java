package com.truyayong.gatherstory.utils;

import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by alley_qiu on 2017/3/4.
 */

public class StringUtil {

    public static boolean isPhoneValid(@Nullable String phone) {
        if (phone == null) {
            return false;
        }
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(phone)) {
            return false;
        } else {
            return phone.matches(telRegex);
        }
    }

    public static boolean isPasswordValid(@Nullable String password) {
        if (password == null) {
            return false;
        }
        return password.length() > 4;
    }

    public static boolean isVerifyCodeValid(@Nullable String code) {
        if (code == null) {
            return false;
        }
        return code.length() == 6;
    }
}
