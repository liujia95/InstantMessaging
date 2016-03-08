package me.liujia95.instantmessaging.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Administrator on 2016/2/25 14:37.
 */
public class BaseApplication extends Application {

    private static Handler mHandler;
    private static Context mContext;
    private static Thread  mMainThread;

    public static Handler getMainHandler() {
        return mHandler;
    }

    public static Context getContext() {
        return mContext;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mHandler = new Handler();
        mContext = this;
        mMainThread = Thread.currentThread();

        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);

        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);

        //初始化环信的一些配置信息
        EMOptions options = initChatOptions();
        //初始化
        EMClient.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }

    /**
     * 初始化环信的一些配置信息
     *
     * @return
     */
    private EMOptions initChatOptions() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        return options;
    }
}
