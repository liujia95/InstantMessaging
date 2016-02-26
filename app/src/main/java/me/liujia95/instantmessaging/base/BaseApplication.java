package me.liujia95.instantmessaging.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

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

        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        //初始化
        EMClient.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }
}
