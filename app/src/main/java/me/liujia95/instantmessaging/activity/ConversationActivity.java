package me.liujia95.instantmessaging.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.adapter.ConversationAdapter;
import me.liujia95.instantmessaging.db.dao.ConversationDao;
import me.liujia95.instantmessaging.db.dao.RecentConversationDao;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.db.model.MessageState;
import me.liujia95.instantmessaging.fragment.ConversationListFragment;
import me.liujia95.instantmessaging.utils.ConversationUtils;
import me.liujia95.instantmessaging.utils.LogUtils;

/**
 * Created by Administrator on 2016/3/1 14:14.
 */
public class ConversationActivity extends AppCompatActivity implements View.OnClickListener {

    @InjectView(R.id.conversation_recyclerview)
    RecyclerView mRecyclerview;
    @InjectView(R.id.conversation_et_input)
    EditText     mEtInput;
    @InjectView(R.id.conversation_iv_voice)
    ImageView    mIvVoice;
    @InjectView(R.id.conversation_tv_press_speak)
    TextView     mTvPressSpeak;
    @InjectView(R.id.conversation_tv_send)
    TextView     mTvSend;
    @InjectView(R.id.conversation_iv_type)
    ImageView    mIvType;
    @InjectView(R.id.conversation_iv_biaoqing)
    ImageView    mIvBiaoqing;


    private ConversationAdapter     mAdapter;
    private String                  mChatObj;
    private List<ConversationModel> mDatas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.activity_conversation);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        ButterKnife.inject(this);
    }

    private void initData() {
        Intent intent = getIntent();
        mChatObj = intent.getStringExtra(ConversationListFragment.KEY_CHAT_OBJ);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mChatObj);
        }

        //查询本用户与此会话对象的所有会话
        mDatas = ConversationDao.selectAllByChatObj(mChatObj, EMClient.getInstance().getCurrentUser());
        //遍历与其的所有会话
        ConversationUtils.ergodicConvertion(mDatas);

        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ConversationAdapter(mDatas);
        mRecyclerview.setAdapter(mAdapter);
    }

    private void initListener() {
        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                LogUtils.d("+++beforeTextChanged s:" + s + "start:" + start + "count:" + count + "after:" + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LogUtils.d("+++onTextChanged s:" + s + "start:" + start + "count:" + count);
                if (s.length() > 0) {
                    mIvType.setVisibility(View.GONE);
                    mTvSend.setVisibility(View.VISIBLE);
                } else {
                    mIvType.setVisibility(View.VISIBLE);
                    mTvSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                LogUtils.d("+++afterTextChanged s:" + s);
            }
        });
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
                    model.messageState = MessageState.UNREAD;//如果接受到了，是未读状态
                    model.date = message.getMsgTime();
                    String body = message.getBody().toString();
                    body = body.substring((body.indexOf("\"") + 1), body.lastIndexOf("\""));
                    model.message = body;

                    //添加到会话的数据库
                    long result = ConversationDao.insert(model);
                    if (RecentConversationDao.isChated(message.getFrom())) {
                        //如果跟他聊过，更新最新聊天记录
                        int update = RecentConversationDao.update(model, EMClient.getInstance().getCurrentUser());
                    } else {
                        //如果没跟他聊过，插入一条
                        long insert = RecentConversationDao.insert(model);
                    }
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

        mTvSend.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_conversation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                Toast.makeText(this, "信息", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == mTvSend) {
            //点击发送按钮
            clickSendTXTMessage();
        }
    }

    private void clickSendTXTMessage() {
        //创建一条文本消息,content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        String inputStr = mEtInput.getText().toString();
        if (TextUtils.isEmpty(inputStr)) {
            return;
        }
        EMMessage message = EMMessage.createTxtSendMessage(inputStr, mChatObj);
        //如果是群聊，设置chattype,默认是单聊
        //message.setChatType(EMMessage.ChatType.GroupChat);
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
        //TODO：添加到数据库
        ConversationModel model = new ConversationModel();
        model.from = EMClient.getInstance().getCurrentUser();
        model.to = mChatObj;
        model.messageType = EMMessage.Type.TXT;
        model.messageState = MessageState.UNDELIVERED;
        model.message = inputStr;

        ConversationDao.insert(model);
        if (RecentConversationDao.isChated(message.getFrom())) {
            //如果跟他聊过，更新最新聊天记录
            int update = RecentConversationDao.update(model, EMClient.getInstance().getCurrentUser());
        } else {
            //如果没跟他聊过，插入一条
            long insert = RecentConversationDao.insert(model);
        }

        mDatas.add(model);
        mAdapter.notifyDataSetChanged();

        //把RecyclerView定位到最底下
        mRecyclerview.smoothScrollToPosition(mDatas.size());
        //把输入框清空
        mEtInput.setText("");
    }
}
