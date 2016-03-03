package me.liujia95.instantmessaging.utils;

import java.util.Comparator;

import me.liujia95.instantmessaging.bean.FriendInfoBean;

/**
 * 自定义排序器
 */
public class PinyinComparator implements Comparator<FriendInfoBean> {
    public int compare(FriendInfoBean o1, FriendInfoBean o2) {
        if (o1.item_en.equals("@")
                || o2.item_en.equals("#")) {
            return -1;
        } else if (o1.item_en.equals("#")
                || o2.item_en.equals("@")) {
            return 1;
        } else {
            return o1.item_en.compareTo(o2.item_en);
        }
    }
}