package me.liujia95.instantmessaging.bean;

import android.widget.ImageView;

/**
 * Created by Administrator on 2016/3/4 18:03.
 */
public class RedPointBean {

    public boolean   isShow;
    public ImageView ivRedPoint;

    public RedPointBean(boolean isShow, ImageView ivRedPoint) {
        this.isShow = isShow;
        this.ivRedPoint = ivRedPoint;
    }
}
