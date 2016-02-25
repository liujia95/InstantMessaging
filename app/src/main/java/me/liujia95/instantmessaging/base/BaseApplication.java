package me.liujia95.instantmessaging.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.easemob.chat.EMChat;

/**
 * Created by Administrator on 2016/2/25 14:37.
 */
public class BaseApplication extends Application {

    private static Handler mHandler;
    private static Context mContext;

    public static Handler getMainHandler() {
        return mHandler;
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mHandler = new Handler();
        mContext = this;

        EMChat.getInstance().init(this);
        //设置需要自动登录
        EMChat.getInstance().setAutoLogin(true);

        /**
         * debugMode == true 时为打开，sdk 会在log里输入调试信息
         * @param debugMode
         * 在做代码混淆的时候需要设置成false
         */
        EMChat.getInstance().setDebugMode(true);//在做打包混淆时，要关闭debug模式，避免消耗不必要的资源
    }
}
