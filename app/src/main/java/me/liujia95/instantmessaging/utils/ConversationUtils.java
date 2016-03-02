package me.liujia95.instantmessaging.utils;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.List;

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

}
