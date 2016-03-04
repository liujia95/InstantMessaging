package me.liujia95.instantmessaging.manager;

import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

import me.liujia95.instantmessaging.bean.RedPointBean;
import me.liujia95.instantmessaging.utils.UIUtils;

/**
 * Created by Administrator on 2016/3/4 14:56.
 * desc：消息红点管理类
 */
public class RedPointManager {

    private static Map<String, RedPointBean> mMap;

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


    public void add(String name, RedPointBean bean) {
        if (!exist(name)) {
            mMap.put(name, bean);
        }
    }

    public boolean exist(String name) {
        if (get(name) != null) {
            return true;
        }
        return false;
    }

    public void remove(String name) {
        mMap.remove(name);
    }

    public RedPointBean get(String name) {
        return mMap.get(name);
    }

    public void show(String name) {
        RedPointBean bean = get(name);
        bean.isShow = true;

        final ImageView iv = bean.ivRedPoint;
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
        RedPointBean bean = get(name);
        bean.isShow = false;
        final ImageView iv = bean.ivRedPoint;
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
