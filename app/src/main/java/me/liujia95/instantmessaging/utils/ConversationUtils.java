package me.liujia95.instantmessaging.utils;

import android.content.Intent;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.liujia95.instantmessaging.activity.ChattingActivity;
import me.liujia95.instantmessaging.activity.HomeActivity;
import me.liujia95.instantmessaging.db.dao.ConversationDao;
import me.liujia95.instantmessaging.db.dao.RecentConversationDao;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.db.model.MessageState;
import me.liujia95.instantmessaging.receiver.GCMPushBroadCast;

import static com.hyphenate.chat.EMMessage.Type.IMAGE;

/**
 * Created by Administrator on 2016/3/1 13:33.
 */
public class ConversationUtils {

    /**
     * 获取会话对象
     *
     * @return
     */
    public static String getChatObj(ConversationModel model) {
        if (EMClient.getInstance().getCurrentUser().equals(model.from)) {
            return model.to;
        }
        return model.from;
    }

    /**
     * 遍历所有的会话
     */
    public static void ergodicConvertion(List<ConversationModel> list) {
        for (ConversationModel model : list) {
            LogUtils.d("------------------------");
            LogUtils.d("date:" + DateUtils.getDateFormat(model.date));
            LogUtils.d("from:" + model.from);
            LogUtils.d("to:" + model.to);
            LogUtils.d("message:" + model.message);
            LogUtils.d("------------------------");
        }
    }

    public static ConversationModel getMessageToModel(EMMessage message) {
        ConversationModel model = new ConversationModel();
        model.from = message.getFrom();
        model.to = message.getTo();
        model.messageType = message.getType();
        model.messageState = MessageState.UNREAD;//如果接受到了，是未读状态
        model.date = message.getMsgTime();
        String body = message.getBody().toString();
        switch (message.getType()) {
            case IMAGE:
                body = body.split("remoteurl:")[1].split(",thumbnial")[0];
                LogUtils.d("@@ image body:" + body);
                break;
            case TXT:
                body = body.substring((body.indexOf("\"") + 1), body.lastIndexOf("\""));
                break;

        }
        model.message = body;

        return model;
    }

    /**
     * 发送图片
     *
     * @param selectedImage 选中的图片路径
     * @param chatObj       聊天对象
     * @return 聊天集合
     */
    public static ArrayList<ConversationModel> sendImage(List<String> selectedImage, String chatObj) {
        ArrayList<ConversationModel> list = new ArrayList<>();

        for (String imagePath : selectedImage) {
            LogUtils.d("@@ img path:" + imagePath);

            //imagePath为图片本地路径，false为不发送原图(默认超过100k的图片会压缩后发给对方),需要发送原图传false
            EMMessage message = EMMessage.createImageSendMessage(imagePath, false, chatObj);
            EMClient.getInstance().chatManager().sendMessage(message);

            ConversationModel model = new ConversationModel();
            model.from = EMClient.getInstance().getCurrentUser();
            model.to = chatObj;
            model.messageType = IMAGE;
            model.messageState = MessageState.UNDELIVERED;
            model.message = "[图片]"; //会话列表中的显示
            model.date = System.currentTimeMillis();
            if (RecentConversationDao.isChated(chatObj)) {
                //如果跟他聊过，更新最新聊天记录
                RecentConversationDao.update(model, chatObj);
            } else {
                //如果没跟他聊过，插入一条
                RecentConversationDao.insert(model);
            }

            model.message = imagePath;
            //把图片文件地址保存到聊天记录数据库中
            ConversationDao.insert(model);
            list.add(model);
        }
        return list;
    }


    /**
     * 接收图片
     *
     * @param messages 消息集合
     */
    public static void receiveImage(List<EMMessage> messages, HomeActivity.OnMessageListener listener) {
        LogUtils.d("===收到消息：" + messages.size() + "个");
        for (EMMessage message : messages) {
            LogUtils.d("===message:" + message.toString());
            LogUtils.d("===getBody:[" + message.getBody().toString() + "]");
            LogUtils.d("===getMsgId:" + message.getMsgId());
            LogUtils.d("===getUserName:" + message.getUserName());
            LogUtils.d("===getFrom:" + message.getFrom());
            LogUtils.d("===getTo:" + message.getTo());
            long msgMillis = message.getMsgTime();
            LogUtils.d("===getMsgTime:" + msgMillis);
            Date date = new Date(msgMillis);
            LogUtils.d("===时间：" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds());

            ConversationModel model = ConversationUtils.getMessageToModel(message);

            if (!UIUtils.getRunningActivityName().equals(ChattingActivity.class.getName())) {
                Intent intent = new Intent();
                intent.putExtra(GCMPushBroadCast.NOTIFICATION_MESSAGE, model.message);
                intent.setAction("com.hyphenate.sdk.push");
                intent.addCategory("com.hyphenate.chatuidemo");
                UIUtils.getContext().sendBroadcast(intent);
                LogUtils.d("@@发了一条广播");
            }

            //添加到会话的数据库
            ConversationDao.insert(model);

            model.message = "[图片]";
            if (RecentConversationDao.isChated(message.getFrom())) {
                //如果跟他聊过，更新最新聊天记录
                RecentConversationDao.update(model, message.getFrom());
            } else {
                //如果没跟他聊过，插入一条
                RecentConversationDao.insert(model);
            }

            //设置监听
            listener.onHasNewMessage(model);
        }
    }
}
