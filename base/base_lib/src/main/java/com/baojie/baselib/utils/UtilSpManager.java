package com.baojie.baselib.utils;

import android.app.Application;


/**
 * @Description: TODO
 * @Author baojie@qding.me
 * @Date 2020-02-13 10:22
 * @Version TODO
 */
public class UtilSpManager {
    private static UtilSpManager instance;
    private SPUtil appSp;
    private static Application application;

    public static void setApplication(Application context){
        if (application == null){
            application = context;
        }
    }

    public static UtilSpManager getInstance() {
        if (instance == null) {
            instance = new UtilSpManager(application);
        }
        return instance;
    }

    private UtilSpManager(Application context) {
        appSp = new SPUtil(context, "UtilSpWeb");
    }


    /**
     * androidId 设备唯一标识
     **/
    public static final String SP_KEY_ANDROID = "android_id";


    /***
     * 获取androidId
     *
     * @return
     */
    public String getAndroidId() {
        return appSp.getValue(SP_KEY_ANDROID, "");
    }

    /***
     * 设置androidId
     *
     * @param androidId
     */
    public void setAndroidId(String androidId) {
        appSp.setValue(SP_KEY_ANDROID, androidId);
    }

}
