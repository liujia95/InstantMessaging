package me.liujia95.instantmessaging.utils;

import com.hyphenate.chat.EMClient;

import java.util.List;

import me.liujia95.instantmessaging.db.model.ConversationModel;

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
            LogUtils.d("from0.0:" + model.from);
            LogUtils.d("to:" + model.to);
            LogUtils.d("message:" + model.message);
            LogUtils.d("------------------------");
        }
    }

}
