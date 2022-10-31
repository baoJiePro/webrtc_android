package com.baojie.baselib.utils;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @Description: 手机号工具类
 * @Author wangjianzhou@qding.me
 * @Date 2020-02-12 14:03
 * @Version
 */
public class PhoneUtil {
    /**
     * 手机号为1开头的11位数字
     */
    public static final String REGEX_MOBILE = "^1\\d{10}$";

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        if (mobile == null || mobile.length() == 0) {
            return false;
        }
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    /**
     * 拨打电话，出现提醒框
     *
     * @param activity
     * @param confirmTitle 提示标题
     * @param phoneNumber
     */
//    public static void callPhone(final Activity activity, String confirmTitle, final String phoneNumber) {
//        DialogUtil.showConfirm(activity, confirmTitle, phoneNumber, dialog -> {
//            if (isExistsSIMCard(activity)) {
//                PermissionsUtils.getInstance().chekPermissions(activity, new PermissionsUtils.IPermissionsResult() {
//                    @Override
//                    public void passPermissons() {
//                        Intent intent = new Intent();
//                        intent.setAction("android.intent.action.CALL");
//                        intent.setData( Uri.parse("tel:" + phoneNumber));
//                        activity.startActivity(intent);
//                    }
//                    @Override
//                    public void forbitPermissons() {
//
//                    }
//                },PermissionsUtils.CALL_PHONE);
//            } else {
//                Toast.makeText(activity, "请插入SIM卡", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    /**
     * 判断是否存在SIM卡
     *
     * @param context
     * @return
     */
    public static boolean isExistsSIMCard(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService( Context.TELEPHONY_SERVICE);
        return manager.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    /**
     * 判断是否是android10
     * @return
     */
    public static boolean isAndroid10(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 获取移动设备本地IP
     *
     * @return
     */
    public static String getMac() {
        try {
            InetAddress ip = getLocalInetAddress();
            if (ip == null) {
                return null;
            }
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(ip);
            if (networkInterface == null) {
                return null;
            }

            byte[] b = networkInterface.getHardwareAddress();
            if (b == null || b.length <= 0) {
                return null;
            }
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            return buffer.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static InetAddress getLocalInetAddress() {
        try {
            //列举
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            if (networkInterfaces == null) {
                return null;
            }
            while (networkInterfaces.hasMoreElements()) {//是否还有元素
                NetworkInterface ni = (NetworkInterface) networkInterfaces.nextElement();//得到下一个元素
                if (ni == null) {
                    continue;
                }
                Enumeration addresses = ni.getInetAddresses();//得到一个ip地址的列举
                if (addresses == null) {
                    continue;
                }
                while (addresses.hasMoreElements()) {
                    InetAddress ip = (InetAddress) addresses.nextElement();
                    if (ip == null) {
                        continue;
                    }
                    if (!ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")) {
                        return ip;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取deviceId
     * @param context
     * @return
     */
    public static String getDeviceId(Application context){
        UtilSpManager.setApplication(context);
        String androidId = UtilSpManager.getInstance().getAndroidId();
        if (StringUtils.isEmpty(androidId)) {
            androidId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            if (StringUtils.isEmpty(androidId)) {
                androidId = UUID.randomUUID().toString();
            }
            UtilSpManager.getInstance().setAndroidId(androidId);
        }
        return androidId;
    }

    /**
     * 判断app是否安装
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName){
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packageName)) {
                    return true;
                }
            }
        }

        return false;
    }
}
