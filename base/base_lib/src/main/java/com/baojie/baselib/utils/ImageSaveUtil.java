package com.baojie.baselib.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Description: 图片存储工具类
 * @Author wangjianzhou@qding.me
 * @Date 2019/1/15 11:31 AM
 * @Version v4.5
 */
public class ImageSaveUtil {

    /**
     * 保存base64到本地相册
     *
     * @param context
     * @param base64DataStr
     * @return
     */
    public static boolean saveBase64ToGallery(final Context context, String base64DataStr) {
        boolean result = false;
        if (TextUtils.isEmpty(base64DataStr)) {
            Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
            return false;
        }
        // 1.去掉base64中的前缀
        String base64Str = base64DataStr.substring(base64DataStr.indexOf(",") + 1, base64DataStr.length());
        // 2.这行代码很重要，Android端获取的和IOS端不一致
        base64Str = base64Str.replaceAll(" ", "+");
        // 3.获取手机相册的路径地址, 参照sdk中的CameraLoader中的处理方法
        File file = null;
        try {
            file = getOutputFile();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
            return result;
        }
        String fileName = file.toString();
        // 3. 解析保存图片
        byte[] data = new byte[0];
        try {
            // 这里使用
            data = Base64.decode(base64Str, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
            return result;
        }

        OutputStream os = null;
        try {
            os = new FileOutputStream(fileName);
            os.write(data);
            os.flush();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //通知相册更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);

        if (result) {
            Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
        }

        return result;
    }

    public static String saveBase64ToSd(String base64DataStr) {

        // 1.去掉base64中的前缀
        String base64Str = base64DataStr.substring(base64DataStr.indexOf(",") + 1, base64DataStr.length());
        // 2.这行代码很重要，Android端获取的和IOS端不一致
        base64Str = base64Str.replaceAll(" ", "+");
        // 3.获取手机相册的路径地址, 参照sdk中的CameraLoader中的处理方法
        File file = null;
        try {
            file = getOutputFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String fileName = file.toString();
        // 3. 解析保存图片
        byte[] data = new byte[0];
        try {
            // 这里使用
            data = Base64.decode(base64Str, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OutputStream os = null;
        try {
            os = new FileOutputStream(fileName);
            os.write(data);
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file.getAbsolutePath();
    }


    /**
     * 获取输出路径，这里输出到相册，参照CameraLoader
     *
     * @return
     * @throws
     */
    private static File getOutputFile() throws Exception {
        String storageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_REMOVED.equals(storageState)) {
            throw new Exception();
        }

        File mediaStorageDir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                , "QDFolder");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
//                throw new CameraException(CameraLoader.MSG_MKDIRS_FAIL);
            }
        }
        return new File(getFilePath(mediaStorageDir));
    }

    /**
     * 生成保存路径
     *
     * @param mediaStorageDir
     * @return
     */
    private static String getFilePath(File mediaStorageDir) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        String filePath = mediaStorageDir.getPath() + File.separator;
        filePath += ("IMG_" + timeStamp + ".png");
        return filePath;
    }

    /**
     * 给图片添加水印
     * @param context
     * @param path
     * @param labels
     * @param fontSize
     * @return
     */
    public static String getWaterMarkPath(Context context, String path, List<String> labels, int fontSize){
        try {
            InputStream is = new FileInputStream(path);
            BitmapFactory.Options opts=new BitmapFactory.Options();
            opts.inTempStorage = new byte[100 * 1024];
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            opts.inPurgeable = true;
            opts.inSampleSize = 4;
            opts.inInputShareable = true;
            Bitmap bitmap = BitmapFactory.decodeStream(is,null, opts);
            return getBitmapWaterMark(context, bitmap, labels, fontSize);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getBitmapWaterMark(Context context, Bitmap src, List<String> labels, int fontSize){
        Bitmap bitmap = addTextWaterMark(context, src, labels, -30, fontSize);
        String path = saveBitmap(context, bitmap);
        return path;
    }

    private static Bitmap addTextWaterMark(Context context, Bitmap src, List<String> labels, int degress, int fontSize){
        if (src != null){
            Bitmap copy = src.copy(src.getConfig(), true);

            int width = copy.getWidth();
            int height = copy.getHeight();
            LogUtils.d("baojie=== width  " + width + "  " + height);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            Canvas canvas = new Canvas(copy);
            canvas.drawColor(Color.parseColor("#40F3F5F9"));
            paint.setColor(Color.parseColor("#50AEAEAE"));
//            canvas.save();
            canvas.rotate(degress);
            paint.setAntiAlias(true);
            paint.setTextSize(fontSize);
            LogUtils.d("baojie====  fontSize: " + sp2px(context, fontSize));
            float textWidth = 0;
            for (int i = 0; i < labels.size(); i++) {
                float tWidth = paint.measureText(labels.get(i));
                if (textWidth < tWidth){
                    textWidth = tWidth;
                }
            }

            LogUtils.d("baojie=== textwidth  "  + "  " + textWidth);
            int index = 0;
            for (int positionY = height / 10; positionY <= height; positionY += height / 10+80) {
                float fromX = -width + (((index++) % 2) * textWidth);
                LogUtils.d("baojie=== index  "  + "  " + index);
                LogUtils.d("baojie=== fromX  "  + "  " + fromX);
                for (float positionX = fromX; positionX < width; positionX += textWidth * 2) {
                    int spacing  = 0;//间距
                    for(String label:labels){
                        LogUtils.d("baojie=== text  " + label +  "  " + positionX + "   " + positionY);
                        canvas.drawText(label, positionX, positionY+spacing, paint);
                        spacing = spacing+20;
                    }

                }
            }
            return copy;

        }
        return null;

    }

    private static String saveBitmap(Context context, Bitmap mBitmap) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "waterMark" + File.separator;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + File.separator + "waterMark" + File.separator;
        }
        try {
            filePic = new File(savePath + generateFileName() + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return filePic.getAbsolutePath();
    }

    private static String generateFileName() {
        return UUID.randomUUID().toString();
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
