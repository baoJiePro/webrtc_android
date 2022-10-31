package com.baojie.baselib.utils;

import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Base64;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: TODO
 * @Author baojie@qding.me
 * @Date 2020-02-21 11:26
 * @Version TODO
 */
public class StringDealUtil {

    public static String list2base64(List list) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(list);
        String SceneListString = new String(Base64.encode(byteArrayOutputStream.toByteArray(), 0));
        objectOutputStream.close();
        return SceneListString;
    }

    public static List base64toList(String string) throws IOException, ClassNotFoundException {
        byte[] mobileBytes = Base64.decode(string.getBytes(), 0);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        List SceneList = (List)objectInputStream.readObject();
        objectInputStream.close();
        return SceneList;
    }

    public static boolean isContainChinese(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        } else {
            Pattern p = Pattern.compile("[一-龥|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]");
            Matcher m = p.matcher(str);
            return m.find();
        }
    }

    public static boolean compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return false;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        LogUtils.d("version1Array===" + version1Array.length);
        LogUtils.d("version2Array===" + version2Array.length);
        int index = 0;
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return false;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return true;
                }
            }
            return false;
        } else {
            return diff < 0;
        }
    }

    public static String autoSplitText(TextView tv){
        final String rawText = tv.getText().toString(); //原始文本
        final Paint tvPaint = tv.getPaint(); //paint，包含字体等信息
        final float tvWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight(); //控件可用宽度

        //将原始文本按行拆分
        String [] rawTextLines = rawText.replaceAll("\r", "").split("\n");
        StringBuilder sbNewText = new StringBuilder();
        for (String rawTextLine : rawTextLines) {
            if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                //如果整行宽度在控件可用宽度之内，就不处理了
                sbNewText.append(rawTextLine);
            } else {
                //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                float lineWidth = 0;
                for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                    char ch = rawTextLine.charAt(cnt);
                    lineWidth += tvPaint.measureText(String.valueOf(ch));
                    if (lineWidth <= tvWidth) {
                        sbNewText.append(ch);
                    } else {
                        sbNewText.append("\n");
                        lineWidth = 0;
                        --cnt;
                    }
                }
            }
            sbNewText.append("\n");
        }

        //把结尾多余的\n去掉
        if (!rawText.endsWith("\n")) {
            sbNewText.deleteCharAt(sbNewText.length() - 1);
        }

        return sbNewText.toString();
    }

    /**
     * 显示textview，换行参差不齐问题
     * @param textView
     */
    public static void showTextView(TextView textView) {
        textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                final String newTxt = StringDealUtil.autoSplitText(textView);
                if (!StringUtils.isEmpty(newTxt)){
                    textView.setText(newTxt);
                }
            }
        });
    }
}
