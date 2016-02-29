package me.liujia95.instantmessaging.db.model;

import com.hyphenate.chat.EMMessage;

/**
 * Created by Administrator on 2016/2/29 16:30.
 */
public class ConversationModel {

    public String         id;
    public String         from;   //发送者
    public String         to;     //接收者
    public EMMessage.Type messageType;    //消息类型
    public MessageState   messageState;   //消息状态
    public String         message;        //消息
    public long           date;           //时间

}
