package com.baojie.baselib.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Description: Image的工具类(bitmap 根据View生成bitmap等)
 * @Author gaoyongjie@qding.me
 * @Date 16/12/2 下午5:59
 * @Version V2.8.0
 */
public class ImageUtil {

    /**
     * 根据本地路径获取对应的bitmap对象
     *
     * @param filePath 本地路径
     * @return Bitmap对象
     */
    public static Bitmap getBitmap(String filePath) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inDensity = DisplayMetrics.DENSITY_XHIGH;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o);
        return bitmap;
    }

    /**
     * 根据本地路径获取对应的Drawable对象
     *
     * @param mContext 上下文
     * @param filePath 本地路径
     * @return Drawable对象
     */
    public static Drawable getDrawable(Context mContext, String filePath) {
        return new BitmapDrawable(mContext.getResources(), getBitmap(filePath));
    }

    /**
     * 根据drawable获取bitmap
     * @param drawable
     * @return
     */
    public static Bitmap getBitmapByDrawable(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;

    }



    /**
     * 将View保存到本地
     *
     * @param mContext
     * @param view
     */
    public static void imageToSdcard(final Context mContext, final View view) {
        view.setDrawingCacheEnabled(false);
        view.setDrawingCacheEnabled(true);
        final Bitmap bmp = view.getDrawingCache();
        new Thread( new Runnable() {

            @Override
            public void run() {
                long currentDate = System.currentTimeMillis();

                StringBuffer imageUrl = new StringBuffer();
                imageUrl.append(currentDate);
                imageUrl.append(".png");
                final File cacheDir = getFormDiskCacheDir(mContext.getApplicationContext(), imageUrl.toString());
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(cacheDir);
                    bmp.compress( Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
                            MediaScannerConnection.scanFile(mContext, new String[]{cacheDir.getAbsolutePath()}, null, null);
                        }
                    });
                } catch (Exception e) {
                    view.post(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(mContext, "存储失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                } finally {
                    fos = null;
                    bmp.recycle();
                }

            }
        }).start();

    }

    /**
     * 获取阵型可以使用的缓存目录
     *
     * @param context
     * @param uniqueName 目录名称
     * @return
     */
    private static File getFormDiskCacheDir(Context context, String uniqueName) {
        final String cachePath = Environment.MEDIA_MOUNTED.equals( Environment.getExternalStorageState()) ? getExternalCacheDir(
            context).getPath() : context.getCacheDir().getPath();
        File cacheDir = new File(cachePath);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return new File(cachePath, uniqueName);
    }

    /**
     * 获取程序外部的缓存目录
     *
     * @param context
     * @return
     */
    private static File getExternalCacheDir(Context context) {
        File cacheDir = new File("/Pictures/");
        return new File( Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }


    /**
     * 保存图片文件
     *
     * @param fileFolderStr
     * @param isDir
     * @param croppedImage
     * @return
     * @throws IOException
     */
    public static String saveToFile(String fileFolderStr, boolean isDir, Bitmap croppedImage) throws IOException {
        File jpgFile;
        if (isDir) {
            File fileFolder = new File(fileFolderStr);
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
            String filename = format.format(date) + ".jpg";
            if (!fileFolder.exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
                mkdir(fileFolder);
            }
            jpgFile = new File(fileFolder, filename);
        } else {
            jpgFile = new File(fileFolderStr);
            if (!jpgFile.getParentFile().exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
                mkdir(jpgFile.getParentFile());
            }
        }
        FileOutputStream outputStream = new FileOutputStream(jpgFile); // 文件输出流

        croppedImage.compress( Bitmap.CompressFormat.JPEG, 100, outputStream);

        ImageIOUtil.closeStream(outputStream);
        return jpgFile.getPath();
    }

    private static boolean mkdir(File file) {
        while (!file.getParentFile().exists()) {
            mkdir(file.getParentFile());
        }
        return file.mkdir();
    }


    /**
     * 将View保存到本地 并返回File的路径
     *
     * @param mContext
     * @param view
     */
    public static String saveViewToSD(final Context mContext, View view) {
        view.setDrawingCacheEnabled(false);
        view.setDrawingCacheEnabled(true);
        final Bitmap bmp = view.getDrawingCache();
        long currentDate = System.currentTimeMillis();
        StringBuffer imageUrl = new StringBuffer();
        imageUrl.append(currentDate);
        imageUrl.append(".png");
        final File cacheDir = getFormDiskCacheDir(mContext.getApplicationContext(), imageUrl.toString());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(cacheDir);
            bmp.compress( Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return cacheDir.getAbsolutePath();
        } catch (Exception e) {
        } finally {
            fos = null;
            bmp.recycle();
        }
        return null;
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        return bitmapToBase64(bitmap, 100);
    }

    public static boolean isSquare(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        return options.outHeight == options.outWidth;
    }

    /**
     * @param imgPath
     * @param imgEntity
     * @return
     */
    public static String imgToBase64(String imgPath, Integer imgEntity) {
        Bitmap bitmap = null;
        if (imgPath != null && imgPath.length() > 0) {
            bitmap = readBitmap(imgPath);
        }
        if (bitmap == null) {
            //bitmap not found!!
        }
        return bitmapToBase64(bitmap, imgEntity);
    }


    public static String bitmapToBase64(Bitmap bitmap, Integer imgEntity) {
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            bitmap.compress( Bitmap.CompressFormat.JPEG, imgEntity, out);

            out.flush();
            out.close();

            byte[] imgBytes = out.toByteArray();
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Bitmap readBitmap(String imgPath) {
        try {
            return BitmapFactory.decodeFile(imgPath);
        } catch (Exception e) {
            return null;
        }

    }

    public static Bitmap base64ToBitmap(Context mContext, String base64Data) {
        try {
            String base64Replace = base64Data.replaceAll(" ", "+");
            byte[] bytes = Base64.decode(base64Replace, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
//            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.common_img_shop_default);
//            return bitmap;
            return null;
        }
    }

    public static byte[] bitmapToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress( Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * View to bitmap.
     *
     * @param view The view.
     * @return bitmap
     */
    public static Bitmap view2Bitmap(final View view) {
        if (view == null) return null;
        boolean drawingCacheEnabled = view.isDrawingCacheEnabled();
        boolean willNotCacheDrawing = view.willNotCacheDrawing();
        view.setDrawingCacheEnabled(true);
        view.setWillNotCacheDrawing(false);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (null == drawingCache) {
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            view.buildDrawingCache();
            bitmap = Bitmap.createBitmap(view.getDrawingCache());
        } else {
            bitmap = Bitmap.createBitmap(drawingCache);
        }
        view.destroyDrawingCache();
        view.setWillNotCacheDrawing(willNotCacheDrawing);
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        Bitmap bitmap1 = compressImage(bitmap, 15);
        return bitmap1;
    }

    /**
     * 图片压缩 质量压缩
     * @param image
     * @param size  大小 kb
     * @return
     */
    public static Bitmap compressImage(Bitmap image, int size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>size) {  //循环判断如果压缩后图片是否大于10kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 图片压缩采样率压缩
     * @param srcPath
     * @return
     */
    private Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是1280*720分辨率，所以高和宽我们设置为
        float hh = 1280f;//这里设置高度为1280f
        float ww = 720f;//这里设置宽度为720f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }

    public static void compressBitmapToFile(Bitmap bmp, File file){
        // 尺寸压缩倍数,值越大，图片尺寸越小
        int ratio = 2;
        // 压缩Bitmap到对应尺寸
        Bitmap result = Bitmap.createBitmap(bmp.getWidth() / ratio, bmp.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, bmp.getWidth() / ratio, bmp.getHeight() / ratio);
        canvas.drawBitmap(bmp, null, rect, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        result.compress(Bitmap.CompressFormat.JPEG, 100 ,baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Bitmap compressBitmapScare(Bitmap image, int size){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 85, out);
        float zoom = (float)Math.sqrt(size * 1024 / (float)out.toByteArray().length);

        Matrix matrix = new Matrix();
        matrix.setScale(zoom, zoom);

        Bitmap result = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);

        out.reset();
        result.compress(Bitmap.CompressFormat.JPEG, 85, out);
        while(out.toByteArray().length > size * 1024){
            System.out.println(out.toByteArray().length);
            matrix.setScale(0.9f, 0.9f);
            result = Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), matrix, true);
            out.reset();
            result.compress(Bitmap.CompressFormat.JPEG, 85, out);
        }

        return result;
    }

    /**
     * 把流读成字符串
     *
     * @param is
     *            输入流
     * @return 字符串
     */
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {

        } finally {
            try {
                is.close();
            } catch (IOException e) {

            }
        }
        return sb.toString();
    }

    /**
     * 关闭流
     *
     * @param stream
     *            可关闭的流
     */
    public static void closeStream(Closeable stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {

        }
    }

    public static byte[] InputStreamToByte(InputStream is) throws IOException {

        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            bytestream.write(ch);
        }
        byte byteData[] = bytestream.toByteArray();
        bytestream.close();
        return byteData;
    }

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

    private static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
