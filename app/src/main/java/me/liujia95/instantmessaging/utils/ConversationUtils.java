package me.liujia95.instantmessaging.utils;

import android.content.Intent;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.liujia95.instantmessaging.activity.ChattingActivity;
import me.liujia95.instantmessaging.activity.HomeActivity;
import me.liujia95.instantmessaging.db.dao.ConversationDao;
import me.liujia95.instantmessaging.db.dao.RecentConversationDao;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.db.model.MessageState;
import me.liujia95.instantmessaging.receiver.GCMPushBroadCast;

import static com.hyphenate.chat.EMMessage.Type;
import static com.hyphenate.chat.EMMessage.Type.IMAGE;
import static com.hyphenate.chat.EMMessage.createImageSendMessage;

/**
 * Created by Administrator on 2016/3/1 13:33.
 */
public class ConversationUtils {

    public static final String KEY_ASSETS_NAME = "key_assets_name";

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
            LogUtils.d("**------------------------");
            LogUtils.d("**date:" + DateUtils.getDateFormat(model.date));
            LogUtils.d("**type:" + model.messageType);
            LogUtils.d("**state:" + model.messageState);
            LogUtils.d("**from:" + model.from);
            LogUtils.d("**to:" + model.to);
            LogUtils.d("**message:" + model.message);
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
                //是不是gif，如果是，修改消息类型
                boolean isGif = body.split(",localurl:")[0].endsWith(".gif");
                LogUtils.d("@@ is Gif:" + isGif);
                if (isGif) {
                    model.messageType = Type.CMD;
                }
                body = body.split("remoteurl:")[1].split(",thumbnial")[0];
                break;
            case TXT:
                body = body.substring((body.indexOf("\"") + 1), body.lastIndexOf("\""));
                break;
            case CMD:
                Toast.makeText(UIUtils.getContext(), body, Toast.LENGTH_LONG).show();
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
     * @return 选中的图片数
     */
    public static int sendImage(List<String> selectedImage, String chatObj) {
        int count = 0;
        for (String imagePath : selectedImage) {
            LogUtils.d("@@ img path:" + imagePath);

            //imagePath为图片本地路径，false为不发送原图(默认超过100k的图片会压缩后发给对方),需要发送原图传false
            EMMessage message = createImageSendMessage(imagePath, false, chatObj);
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
            count++;
        }
        return count;
    }


    /**
     * 接收消息
     *
     * @param messages 消息集合
     */
    public static void receivedMessage(List<EMMessage> messages, HomeActivity.OnMessageListener listener) {
        LogUtils.d("===收到消息：" + messages.size() + "个");
        for (EMMessage message : messages) {
            LogUtils.d("===message:" + message.toString());
            LogUtils.d("===getBody:[" + message.getBody().toString() + "]");
            LogUtils.d("===getMsgId:" + message.getMsgId());
            LogUtils.d("===getUserName:" + message.getUserName());
            LogUtils.d("===getFrom:" + message.getFrom());
            LogUtils.d("===getTo:" + message.getTo());
            LogUtils.d("===getType:" + message.getType());
            long msgMillis = message.getMsgTime();
            LogUtils.d("===getMsgTime:" + msgMillis);
            Date date = new Date(msgMillis);
            LogUtils.d("===时间：" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds());

            ConversationModel model = getMessageToModel(message);
            //添加到会话的数据库
            ConversationDao.insert(model);

            switch (model.messageType) {
                case IMAGE:
                    model.message = "[图片]";
                    break;
                case CMD:
                    model.message = "[动画表情]";
                    break;
            }

            if (RecentConversationDao.isChated(message.getFrom())) {
                //如果跟他聊过，更新最新聊天记录
                RecentConversationDao.update(model, message.getFrom());
            } else {
                //如果没跟他聊过，插入一条
                RecentConversationDao.insert(model);
            }

            if (!UIUtils.getRunningActivityName().equals(ChattingActivity.class.getName())) {
                Intent intent = new Intent();
                intent.putExtra(GCMPushBroadCast.NOTIFICATION_MESSAGE, model.message);
                intent.putExtra(GCMPushBroadCast.NOTIFICATION_CHAT_OBJ,model.from);
                intent.setAction("com.hyphenate.sdk.push");
                intent.addCategory("com.hyphenate.chatuidemo");
                UIUtils.getContext().sendBroadcast(intent);
                LogUtils.d("@@发了一条广播");
            }
            //设置监听
            listener.onHasNewMessage(model);
        }
    }

    /**
     * 发送本地的动态表情
     */
    public static void sendGifFaceAssets(final String toUsername, final String assetsName) {
        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);

        Map<String, String> map = new HashMap<>();
        map.put(KEY_ASSETS_NAME, assetsName);

        String action = "face";//action可以自定义
        EMCmdMessageBody cmdBody = new EMCmdMessageBody(action, map);
        cmdMsg.setReceipt(toUsername);//发送给某个人
        cmdMsg.addBody(cmdBody);
        EMClient.getInstance().chatManager().sendMessage(cmdMsg);
        LogUtils.d("@@已发送" + cmdBody);
    }

    /**
     * 发送静态表情
     *
     * @param toUsername
     */
    public static void sendStaticFace(String toUsername) {

    }
}
