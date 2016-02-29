package me.liujia95.instantmessaging.db.model;

/**
 * Created by Administrator on 2016/2/29 16:40.
 */
public enum MessageState {
    //未送达 --> 已送达 --> 未读 --> 已读
    READ, UNREAD, UNDELIVERED, DELIVERED
}
