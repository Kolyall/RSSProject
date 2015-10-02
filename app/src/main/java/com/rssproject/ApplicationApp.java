package com.rssproject;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


/**
 * Created by Unuchek on 04.02.2015.
 */
//@ReportsCrashes( formUri = "http://www.bugsense.com/api/acra?api_key=3ad39950", formKey="",
//        mailTo = "unimaximaze@gmail.com",
//        mode = ReportingInteractionMode.DIALOG,
//        resDialogText = R.string.crash_dialog_text, resDialogIcon = android.R.drawable.ic_dialog_info, // optional.
//        resDialogTitle = R.string.app_name, // optional. default is your application
//        resDialogCommentPrompt = R.string.crash_dialog_comment_prompt, // optional. when
//        resDialogOkToast = R.string.crash_dialog_ok_toast // optional. displays a Toast
//)
public class ApplicationApp extends Application {

    private String tag="ApplicationApp";

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityDestroyed() {
        activityVisible = false;
    }

    private static boolean activityVisible;

    public void onLowMemory() {
        super.onLowMemory();
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().clearMemoryCache();
    }

    public void onCreate() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
        int memClass = ( (ActivityManager)this.getSystemService( Context.ACTIVITY_SERVICE ) )
                .getMemoryClass();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .threadPoolSize(3)
                .denyCacheImageMultipleSizesInMemory()
                .threadPriority(Thread.MAX_PRIORITY)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                        //                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .memoryCache(new LruMemoryCache(memClass * 1024 * 1024 / 8))
                .diskCacheExtraOptions(480, 320, null)

                .memoryCacheExtraOptions(480, 320) // default = device screen dimensions
                .memoryCacheSize(memClass * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(1500)
                .writeDebugLogs()

                .build();
        ImageLoader.getInstance().init(config);
        //        ImageLoader.getInstance() .init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
        super.onCreate();



    }
}
