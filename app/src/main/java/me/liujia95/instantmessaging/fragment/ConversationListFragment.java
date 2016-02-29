package me.liujia95.instantmessaging.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.adapter.ConversationAdapter;
import me.liujia95.instantmessaging.base.ParentFragment;
import me.liujia95.instantmessaging.db.dao.ConversationDao;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.db.model.MessageState;
import me.liujia95.instantmessaging.utils.LogUtils;
import me.liujia95.instantmessaging.utils.UIUtils;

/**
 * Created by Administrator on 2016/2/12 16:25.
 */
public class ConversationListFragment extends ParentFragment {

    @InjectView(R.id.conversation_recyclerview)
    RecyclerView mRecyclerView;

    List<ConversationModel> mDatas;
    private ConversationAdapter mAdapter;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_conversation_list, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        mDatas = ConversationDao.selectAll(EMClient.getInstance().getCurrentUser());
        ;

        mAdapter = new ConversationAdapter(mDatas);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        EMMessageListener msgListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                LogUtils.d("收到消息：" + messages.size() + "个");
                for (EMMessage message : messages) {
                    LogUtils.d("message:" + message.toString());
                    LogUtils.d("getBody:[" + message.getBody().toString() + "]");
                    LogUtils.d("getMsgId:" + message.getMsgId());
                    LogUtils.d("getUserName:" + message.getUserName());
                    LogUtils.d("getFrom:" + message.getFrom());
                    LogUtils.d("getTo:" + message.getTo());
                    long msgMillis = message.getMsgTime();
                    LogUtils.d("getMsgTime:" + msgMillis);
                    Date date = new Date(msgMillis);
                    LogUtils.d("11时间：" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds());


                    ConversationModel model = new ConversationModel();
                    model.from = message.getFrom();
                    model.to = message.getTo();
                    model.messageType = message.getType();
                    model.messageState = MessageState.UNDELIVERED;//默认未送达
                    model.date = message.getMsgTime();
                    String body = message.getBody().toString();
                    body = body.substring((body.indexOf(":\"") + 1), body.lastIndexOf("\""));
                    LogUtils.d("body:---" + body);
                    model.message = body;

                    //添加到数据库
                    ConversationDao.insert(model);

                    updateUI();
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
                LogUtils.d("收到透传消息：----");
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {
                //收到已读回执
                LogUtils.d("已读：----");

            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
                //收到已送达回执
                LogUtils.d("已送达：----");
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
                LogUtils.d("消息状态变动: message:" + message + "--change:" + change);
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    public void updateUI() {
        LogUtils.d("----currentUser::" + EMClient.getInstance().getCurrentUser());
        mDatas = ConversationDao.selectAll(EMClient.getInstance().getCurrentUser());
        LogUtils.d("mDatas count:" + mDatas.size());
        mAdapter.notifyDataSetChanged();
    }
}
