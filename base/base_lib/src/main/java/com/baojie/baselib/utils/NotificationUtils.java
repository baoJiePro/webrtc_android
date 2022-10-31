package com.baojie.baselib.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

/**
 * @author Castiel on 2018/12/29 15:19
 * <p> 自定义通知；解决原工具类不适配8.0及以上机型问题；
 */

public class NotificationUtils extends ContextWrapper {

    private NotificationManager manager;
    public String channelName;
    public String channelId;
    /**
     * 在channel更改时修改此CHANNEL_NAME 友盟channel
     */
    public static final String CHANNEL_NAME_U = "channel_umeng";
    /**
     * 在channel更改时修改此CHANNEL_NAME 融云channel
     */
    public static final String CHANNEL_NAME_R = "channel_rongyun";
    private Context mContext;

    public NotificationUtils(Context context) {
        super(context);
        this.mContext = context;
    }

    @RequiresApi(26)
    public void createNotificationChannel(boolean isSound, Uri sound) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        if (!isSound) {
            channel.setSound(null, Notification.AUDIO_ATTRIBUTES_DEFAULT);
        } else {
            channel.setSound(sound, Notification.AUDIO_ATTRIBUTES_DEFAULT);
        }
        //设置绕过免打扰模式
        channel.setBypassDnd(true);
        //检测是否绕过免打扰模式
        channel.canBypassDnd();
        //设置在锁屏界面上显示这条通知
        channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
        getManager().createNotificationChannel(channel);
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    /**
     * api 26及以上通知
     *
     * @param title   题目
     * @param content 内容
     * @return notification
     */

    @RequiresApi(26)
    public Notification.Builder getChannelNotification(String title, String content) {
        return new Notification.Builder(getApplicationContext(), channelId)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true);
    }

    /**
     * api 26及以上自定义通知（融云聊天）通知类型
     *
     * @param title   题目
     * @param content 内容
     * @param intent
     * @param ticker  ticker
     * @return notification
     */
    @RequiresApi(26)
    public Notification.Builder getChannelNotificationRemoteView(String title, String content, int smallIcon, PendingIntent intent, String ticker) {
        Notification.Builder builder = new Notification.Builder(getApplicationContext(), channelId)
                .setSmallIcon(smallIcon)
                .setContentTitle(title)
                .setContentText(content)
                .setTicker(ticker)
                .setAutoCancel(true);
        if (intent != null) {
            builder.setContentIntent(intent);
        }
        return builder;
    }


    /**
     * 创建通知 默认样式
     *
     * @param title   题目
     * @param content 内容
     * @return notification
     */
    public NotificationCompat.Builder getNotification25(String title, String content) {
        return new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true);
    }

    /**
     * 创建通知 自定义 适配融云（聊天）通知样式
     *
     * @param title   通知标题
     * @param content 通知内容
     * @param intent
     * @param ticker  ticker
     * @return notification
     */
    public NotificationCompat.Builder getNotification25RemoteView(String title, String content, int smallIcon, PendingIntent intent, String ticker, boolean isSound, Uri soundUri) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
        builder.setContentIntent(intent);
        builder.setSmallIcon(smallIcon);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setTicker(ticker);
        builder.setAutoCancel(true);
        if (isSound) {
            builder.setSound(soundUri);
        } else {
            builder.setDefaults(Notification.DEFAULT_SOUND);
        }
        return builder;
    }


    /**
     * 创建并发送通知 自定义通知，接入融云聊天及友盟样式通知；注意：融云聊天会带PendingIntent存在参数，方法里会针对PendingIntent是否为null做判断；
     *
     * @param title    标题
     * @param content  内容
     * @param ticker   tiker
     * @param intent  pendingIntent若为null，则默认是友盟调用，不自己notify Notication，融云相反；
     * @param isSound  是否需要自定义声音
     * @param SoundUri 若需要声音，声音文件uri
     */
    public Notification sendNotification(String title, String content, int smallIcon, PendingIntent intent, String ticker, boolean isSound, Uri SoundUri) {
        Notification notification;
        if (intent == null) {
            channelId = "0";
            channelName = CHANNEL_NAME_U;
        }else {
            channelId = "1";
            channelName = CHANNEL_NAME_R;
        }
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel(isSound, SoundUri);
            notification = getChannelNotificationRemoteView(title, content,smallIcon, intent, ticker).build();
            if (null != intent) {
                getManager().notify(0, notification);
            }
        } else {
            notification = getNotification25RemoteView(title, content, smallIcon,intent, ticker, isSound, SoundUri).build();
            if (null != intent) {
                getManager().notify(0, notification);
            }
        }
        return notification;
    }

}
