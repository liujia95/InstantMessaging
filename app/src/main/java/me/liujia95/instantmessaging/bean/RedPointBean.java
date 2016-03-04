package me.liujia95.instantmessaging.bean;

import android.widget.ImageView;

/**
 * Created by Administrator on 2016/3/4 18:03.
 */
public class RedPointBean {

    public boolean   isShow;    //是否显示小红点
    public ImageView ivRedPoint;//红点图片

    public RedPointBean(boolean isShow, ImageView ivRedPoint) {
        this.isShow = isShow;
        this.ivRedPoint = ivRedPoint;
    }
}
