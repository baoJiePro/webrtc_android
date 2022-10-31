package com.baojie.baselib.utils;

/**
 * @Description: TODO
 * @Author baojie@qding.me
 * @Date 2020-02-20 00:14
 * @Version TODO
 */
public class FontUtil {

    public static String ToDBC(String input){
        char[] c = input.toCharArray();

        for(int i = 0; i < c.length; ++i) {
            if (c[i] == 12288) {
                c[i] = ' ';
            } else if (c[i] > '\uff00' && c[i] < '｟') {
                c[i] -= 'ﻠ';
            }
        }

        return new String(c);
    }
}
