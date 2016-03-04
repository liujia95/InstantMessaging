package me.liujia95.instantmessaging.manager;

import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

import me.liujia95.instantmessaging.utils.LogUtils;
import me.liujia95.instantmessaging.utils.UIUtils;

/**
 * Created by Administrator on 2016/3/4 14:56.
 * desc：消息红点管理类
 */
public class RedPointManager {

    private static Map<String, ImageView> mMap;

    public static RedPointManager instance;

    private RedPointManager() {
    }

    public static RedPointManager getInstance() {
        if (instance == null) {
            synchronized (RedPointManager.class) {
                if (instance == null) {
                    //初始化
                    mMap = new HashMap<>();
                    instance = new RedPointManager();
                }
            }
        }
        return instance;
    }


    public void add(String name, ImageView iv) {
        mMap.put(name, iv);
    }

    public void remove(String name) {
        mMap.remove(name);
    }

    public ImageView get(String name) {
        return mMap.get(name);
    }

    public void show(String name) {
        final ImageView iv = get(name);
        LogUtils.d("$$ name:" + name);
        if (iv.getVisibility() == View.GONE || iv.getVisibility() == View.INVISIBLE) {
            UIUtils.post(new Runnable() {
                @Override
                public void run() {
                    iv.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public void disShow(String name) {
        final ImageView iv = get(name);
        if (iv.getVisibility() == View.VISIBLE) {
            UIUtils.post(new Runnable() {
                @Override
                public void run() {
                    iv.setVisibility(View.GONE);
                }
            });
        }
    }
}
