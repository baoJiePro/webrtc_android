package com.baojie.baselib.utils;


import androidx.annotation.Nullable;

/**
 * @author Castiel on 2018/9/17 14:23
 * <p>
 */
public class NewStringUtils {

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(@Nullable CharSequence str) {
    /*    if (str == null || str.length() == 0) {
            return true;
        } else {
            return false;
        }*/
        return (str == null || str.length() == 0);
    }
}
