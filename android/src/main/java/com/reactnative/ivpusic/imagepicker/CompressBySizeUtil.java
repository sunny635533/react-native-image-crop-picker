package com.reactnative.ivpusic.imagepicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhanglei
 * @version 3.0
 * @created 2018/9/29 11:21
 * @title
 * @description
 * @changeRecord 2018/9/29 11:21 modify  by zhanglei
 */
public class CompressBySizeUtil {

    public final static String TAG = "CompressBySizeUtil";

    /**
     * 根据路径获取InputStream输入流，可以配置getBitmap方法使用获取bitmap
     *
     * @param srcPath
     * @return
     */
    public static InputStream getimage(String srcPath, Integer maxSize) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是1920*1080分辨率，所以高和宽我们设置为
        float hh = 960f;// 这里设置高度为800f
        float ww = 540f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        Log.i("lihe", "比例大小==" + be);

        newOpts.inSampleSize = be;// 设置缩放比例

        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap, maxSize);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 将Bitmap转化为InputStream流，带质量压缩，配合 上面一个方法使用
     *
     * @param image
     * @return
     */
    public static InputStream compressImage(Bitmap image, Integer maxSize) {
        Integer imageMaxSize = maxSize <= 0 ? 300: maxSize;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > imageMaxSize) { // 循环判断如果压缩后图片是否大于800kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中

        return isBm;
    }

    /**
     * 将InputStrem输入流转化为Bitmap
     *
     * @param is
     * @return
     */
    public static Bitmap getBitmap(InputStream is) {
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }


    /**
     * 生成文件
     *
     * @return
     */
    public static File getOutputMediaFile(String appName) {
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            Log.w(TAG, "getOutputMediaFile: Environment storage not writable");
            return null;
        }
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appName);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;

        String mImageName = timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;

    }

    /**
     * 把图片bitmap转成文件保存
     *
     * @param bitmap
     * @return
     */
    public static File saveBitmapToFile(Bitmap bitmap,String appName) {

        File pictureFile = getOutputMediaFile(appName);

        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions: ");
            return null;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
        return pictureFile;
    }

    /**
     * 把图片流转成文件保存
     *
     * @param inputStream
     * @return
     */
    public static File saveBitmapToFile(InputStream inputStream,String appName) {
        File pictureFile = getOutputMediaFile(appName);
        copyInputStreamToFile(inputStream, pictureFile);
        return pictureFile;
    }


    private static void copyInputStreamToFile(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


