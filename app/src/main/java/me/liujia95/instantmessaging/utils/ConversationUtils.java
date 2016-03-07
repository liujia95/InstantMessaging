package me.liujia95.instantmessaging.utils;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

import me.liujia95.instantmessaging.db.dao.ConversationDao;
import me.liujia95.instantmessaging.db.dao.RecentConversationDao;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.db.model.MessageState;

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
        body = body.substring((body.indexOf("\"") + 1), body.lastIndexOf("\""));
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
            model.messageType = EMMessage.Type.IMAGE;
            model.messageState = MessageState.UNDELIVERED;
            model.message = imagePath;
            model.date = System.currentTimeMillis();
            //把图片文件地址保存到聊天记录数据库中
            ConversationDao.insert(model);

            model.message = "[图片]"; //会话列表中的显示
            if (RecentConversationDao.isChated(chatObj)) {
                //如果跟他聊过，更新最新聊天记录
                RecentConversationDao.update(model, chatObj);
            } else {
                //如果没跟他聊过，插入一条
                RecentConversationDao.insert(model);
            }

            list.add(model);
        }
        return list;
    }
}
