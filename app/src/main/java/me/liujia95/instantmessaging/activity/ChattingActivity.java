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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.PanelLayout;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.adapter.ConversationAdapter;
import me.liujia95.instantmessaging.db.dao.ConversationDao;
import me.liujia95.instantmessaging.db.dao.RecentConversationDao;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.db.model.MessageState;
import me.liujia95.instantmessaging.utils.ConversationUtils;
import me.liujia95.instantmessaging.utils.LogUtils;

/**
 * 适配了 Panel<->Keybord 切换冲突
 */
public class ChattingActivity extends AppCompatActivity implements View.OnClickListener {

    @InjectView(R.id.content_ryv)
    RecyclerView mRecyclerview;
    @InjectView(R.id.chatting_send_edt)
    EditText     mEtInput;
    @InjectView(R.id.panel_root)
    PanelLayout  mPanelRoot;
    @InjectView(R.id.plus_iv)
    ImageView    mIvPlus;
    @InjectView(R.id.chatting_send_tv)
    TextView     mTvSend;
    @InjectView(R.id.voice_text_switch_iv)
    ImageView    mIvVoice;
    @InjectView(R.id.chatting_send_voice_btn)
    TextView     mTvPressSay;
    @InjectView(R.id.conversation_fl_input)
    FrameLayout  mFlInput;
    @InjectView(R.id.conversation_iv_biaoqing)
    ImageView    mIvBiaoqing;

    private String                  mChatObj;
    private List<ConversationModel> mDatas;
    private ConversationAdapter     mAdapter;

    public static String KEY_CHAT_OBJ = "key_chat_obj"; //聊天对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        ButterKnife.inject(this);

        initData();
        initListener();
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        // 点击notification bar进入聊天页面，保证只有一个聊天页面
//        String username = intent.getStringExtra(KEY_CHAT_OBJ);
//        if (mChatObj.equals(username))
//            super.onNewIntent(intent);
//        else {
//            finish();
//            startActivity(intent);
//        }
//
//    }

    private void initData() {
        Intent intent = getIntent();
        mChatObj = intent.getStringExtra(KEY_CHAT_OBJ);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mChatObj);
        }

        //查询本用户与此会话对象的所有会话
        mDatas = ConversationDao.selectAllByChatObj(mChatObj, EMClient.getInstance().getCurrentUser());

        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ConversationAdapter(mDatas);
        mRecyclerview.setAdapter(mAdapter);

        //将recyclerview定位到最底部
        scrollToLast();
    }

    private void initListener() {
        mRecyclerview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    KeyboardUtil.hideKeyboard(mEtInput);
                    mPanelRoot.setVisibility(View.GONE);
                }
                return false;
            }
        });

        //EditText焦点改变的监听
        mEtInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //如果获取到焦点，滚动到最下面
                    scrollToLast();
                }
            }
        });

        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mIvPlus.setVisibility(View.GONE);
                    mTvSend.setVisibility(View.VISIBLE);
                } else {
                    mIvPlus.setVisibility(View.VISIBLE);
                    mTvSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        EMMessageListener msgListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                for (EMMessage message : messages) {
                    ConversationModel model = ConversationUtils.getMessageToModel(message);

                    LogUtils.d("##Chatting中收到消息");

                    mDatas.add(model);
                    mAdapter.setData(mDatas);

                    scrollToLast();
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

        mIvPlus.setOnClickListener(this);
        mIvVoice.setOnClickListener(this);
        mTvSend.setOnClickListener(this);
        mIvBiaoqing.setOnClickListener(this);

    }

    /**
     * 滑动到最底下
     */
    public void scrollToLast() {
        mRecyclerview.scrollToPosition(mDatas.size() - 1);
    }

    @Override
    public void onClick(View v) {
        if (v == mIvPlus) {
            if (mPanelRoot.getVisibility() == View.VISIBLE) {
                KeyboardUtil.showKeyboard(mEtInput);
            } else {
                KeyboardUtil.hideKeyboard(mEtInput);
                mPanelRoot.setVisibility(View.VISIBLE);

                scrollToLast();
            }
        } else if (v == mIvVoice) {
            clickVoice();
        } else if (v == mTvSend) {
            clickSendTXTMessage();
        } else if (v == mIvBiaoqing) {
            clickBiaoqing();
        }
    }

    private void clickVoice() {
        //如果输入栏是显示的，点击一次就隐藏
        if (mFlInput.getVisibility() == View.VISIBLE) {
            //改变图标为键盘
            mIvVoice.setImageResource(R.drawable.conversation_keyboard_selector);
            //隐藏输入栏
            mFlInput.setVisibility(View.GONE);
            //显示[按住说话]
            mTvPressSay.setVisibility(View.VISIBLE);
            //隐藏软键盘
            KeyboardUtil.hideKeyboard(mEtInput);
        } else {
            //改变图标为语音
            mIvVoice.setImageResource(R.drawable.conversation_setmoed_voice_selector);
            //显示输入栏
            mFlInput.setVisibility(View.VISIBLE);
            //隐藏[按住说话]
            mTvPressSay.setVisibility(View.GONE);
            //显示软键盘
            KeyboardUtil.showKeyboard(mEtInput);
        }
    }


    /**
     * 点击消息发送
     */
    private void clickSendTXTMessage() {
        //创建一条文本消息,content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        String inputStr = mEtInput.getText().toString();
        if (TextUtils.isEmpty(inputStr)) {
            return;
        }

        EMMessage message = EMMessage.createTxtSendMessage(inputStr, mChatObj);
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);

        ConversationModel model = new ConversationModel();
        model.from = EMClient.getInstance().getCurrentUser();
        model.to = mChatObj;
        model.messageType = EMMessage.Type.TXT;
        model.messageState = MessageState.UNDELIVERED;
        model.message = inputStr;

        //添加到数据库
        ConversationDao.insert(model);
        if (RecentConversationDao.isChated(message.getFrom())) {
            //如果跟他聊过，更新最新聊天记录
            RecentConversationDao.update(model, EMClient.getInstance().getCurrentUser());
        } else {
            //如果没跟他聊过，插入一条
            RecentConversationDao.insert(model);
        }

        mDatas.add(model);
        mAdapter.notifyDataSetChanged();
        //把RecyclerView定位到最底下
        scrollToLast();
        //把输入框清空
        mEtInput.setText("");
    }

    private void clickBiaoqing() {
        if (mIvBiaoqing.isEnabled()) {
            mIvBiaoqing.setEnabled(false);
        } else {
            mIvBiaoqing.setEnabled(true);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP &&
                event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mPanelRoot.getVisibility() == View.VISIBLE) {
                mPanelRoot.setVisibility(View.GONE);
                return true;
            }
        }

        return super.dispatchKeyEvent(event);
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

}
