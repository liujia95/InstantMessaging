package me.liujia95.instantmessaging.utils;

import android.content.Context;
import android.os.Handler;

import me.liujia95.instantmessaging.base.BaseApplication;

public class UIUtils {

    public static Context getContext() {
        return BaseApplication.getContext();
    }

    public static Handler getMainHandler() {
        return BaseApplication.getMainHandler();
    }

    public static void post(Runnable task) {
        getMainHandler().post(task);
    }

    public static void postDelayed(Runnable task, long delayMillis) {
        getMainHandler().postDelayed(task, delayMillis);
    }

    public static void runInThread(Runnable task) {
        new Thread(task).start();
    }
}
