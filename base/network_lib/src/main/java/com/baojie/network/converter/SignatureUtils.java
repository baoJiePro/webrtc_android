package com.baojie.network.converter;


import android.util.Base64;

import com.baojie.network.retrofit.intercept.HttpLogInterceptor;
import com.blankj.utilcode.util.LogUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Description: 加解密工具
 * @Author baojie@qding.me
 * @Date 2021/11/13 11:43 上午
 */
public class SignatureUtils {

    /**
     * 秘钥
     */
    public static final String SIGNATURE_KEY = "abc1234";

    /**
     * 加密
     * @param datasource
     * @param secretKey
     * @return
     */
    public static String encrypt(byte[] datasource, String secretKey){
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            // 创建AES密钥
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes("UTf-8"), "AES");
            // 初始化密码器
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptByte = cipher.doFinal(datasource);
            String result = Base64.encodeToString(encryptByte, Base64.NO_WRAP);
            result = result.replace("+", "-").replace("/", "_").replaceAll("(\r\n|\n\r|\r|\n)", "");
            LogUtils.dTag(HttpLogInterceptor.TAG, "加密参数：" + new String(datasource), "加密结果：" + result);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }

    }

    public static String decrypt(String datasource, String secretKey){
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            // 创建AES密钥
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes("UTf-8"), "AES");
            // 初始化密码器
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptByte = cipher.doFinal(Base64.decode(datasource, Base64.NO_WRAP));
            return new String(decryptByte);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
}
