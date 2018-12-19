package com.reactnative.ivpusic.imagepicker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableMap;
import id.zelory.compressor.Compressor;

import java.io.File;
import java.io.IOException;

/**
 * Created by ipusic on 12/27/16.
 */

class Compression {

    public final static String TAG = "image-crop-picker";

    File compressImage(final Activity activity, final ReadableMap options, final String originalImagePath) throws IOException {
        Integer maxWidth = options.hasKey("compressImageMaxWidth") ? options.getInt("compressImageMaxWidth") : null;
        Integer maxHeight = options.hasKey("compressImageMaxHeight") ? options.getInt("compressImageMaxHeight") : null;
        Double quality = options.hasKey("compressImageQuality") ? options.getDouble("compressImageQuality") : null;

        if (maxWidth == null && maxHeight == null && quality == null) {
            Log.d("image-crop-picker", "Skipping image compression");
            return new File(originalImagePath);
        }

        Log.d("image-crop-picker", "Image compression activated");
        Compressor compressor = new Compressor(activity)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getAbsolutePath());

        if (quality == null) {
            Log.d("image-crop-picker", "Compressing image with quality 100");
            compressor.setQuality(100);
        } else {
            Log.d("image-crop-picker", "Compressing image with quality " + (quality * 100));
            compressor.setQuality((int) (quality * 100));
        }

        if (maxWidth != null) {
            Log.d("image-crop-picker", "Compressing image with max width " + maxWidth);
            compressor.setMaxWidth(maxWidth);
        }

        if (maxHeight != null) {
            Log.d("image-crop-picker", "Compressing image with max height " + maxHeight);
            compressor.setMaxHeight(maxHeight);
        }

        File image = new File(originalImagePath); 

        String[] paths = image.getName().split("\\.(?=[^\\.]+$)");
        String compressedFileName = paths[0] + "-compressed";
        
        if(paths.length > 1)
            compressedFileName += "." + paths[1];
        
        return compressor
                .compressToFile(image, compressedFileName);
    }

    synchronized void compressVideo(final Activity activity, final ReadableMap options, final String originalVideo, final String compressedVideo, final Promise promise) {
        // todo: video compression
        // failed attempt 1: ffmpeg => slow and licensing issues
        promise.resolve(originalVideo);
    }

    public File compressImage(final ReadableMap options, final String originalImagePath) throws IOException{
        Integer maxSize = options.hasKey("compressImageSize") ? options.getInt("compressImageSize") : null;
        String appName = options.hasKey("appName") ? options.getString("appName") : "ImagePicker";
        if(maxSize == null){
            Log.d(TAG, "Skipping image compression");
            return new File(originalImagePath);
        }
        File file = CompressBySizeUtil.saveBitmapToFile(CompressBySizeUtil.getimage(originalImagePath,maxSize),appName);
        return file;
    }

}
