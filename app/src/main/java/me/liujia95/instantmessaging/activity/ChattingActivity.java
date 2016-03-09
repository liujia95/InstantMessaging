package me.liujia95.instantmessaging.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.PanelLayout;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.adapter.ConversationAdapter;
import me.liujia95.instantmessaging.adapter.FaceGVAdapter;
import me.liujia95.instantmessaging.adapter.FaceVPAdapter;
import me.liujia95.instantmessaging.adapter.TusijiGVAdapter;
import me.liujia95.instantmessaging.db.dao.ConversationDao;
import me.liujia95.instantmessaging.db.dao.RecentConversationDao;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.db.model.MessageState;
import me.liujia95.instantmessaging.manager.RedPointManager;
import me.liujia95.instantmessaging.utils.ConversationUtils;
import me.liujia95.instantmessaging.utils.LogUtils;
import me.liujia95.instantmessaging.utils.UIUtils;

/**
 * 适配了 Panel<->Keybord 切换冲突
 */
public class ChattingActivity extends AppCompatActivity implements View.OnClickListener, View.OnLayoutChangeListener {

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
    @InjectView(R.id.conversation_iv_face)
    ImageView    mIvFace;
    @InjectView(R.id.send_img_iv)
    ImageView    mIvImg;
    @InjectView(R.id.chatting_ll_plus_container)
    LinearLayout mPlusContainer;
    @InjectView(R.id.chatting_ll_face_container)
    LinearLayout mFaceContainer;
    @InjectView(R.id.root_layout)
    View         activityRootView;//Activity最外层的Layout视图

    /**
     * #################################################################
     * 表情部分
     **/

    @InjectView(R.id.face_emoji_viewpager)
    ViewPager    mEmojiViewPager; //表情符号viewpager
    @InjectView(R.id.face_tusiji_viewpager)
    ViewPager    mTusijiViewPager; //兔斯基viewpager
    @InjectView(R.id.face_dots_container)
    LinearLayout mDotsLayout;   //表情下的小圆点
    @InjectView(R.id.face_type_emoji)
    ImageView    mIvTypeEmoji;     //表情符号图标
    @InjectView(R.id.face_type_tusiji)
    ImageView    mIvTypeTusiji;    //兔斯基图标

    private List<View> views = new ArrayList<View>(); //每一页表情的集合
    private List<String> staticFacesList; //表情符号的静态图片名称
    // 7列3行
    private int columns = 7;
    private int rows    = 3;

    private List<View> tusijiViews = new ArrayList<>();
    private List<String> staticTusijiList; //兔斯基的静态图片名称
    private int tusijiPagerCount = 2; //2页

    /**
     * ################################################################
     **/

    //软件盘弹起后所占高度阀值设置为屏幕高度的1/3
    private int keyHeight = UIUtils.getScreenHeight() / 3;

    private String                  mChatObj;
    private List<ConversationModel> mDatas;
    private ConversationAdapter     mAdapter;


    public static String KEY_CHAT_OBJ              = "key_chat_obj"; //聊天对象
    public static int    REQUEST_CODE_SWITCH_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        ButterKnife.inject(this);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        //TODO：待优化
        //初始化表情列表staticFacesList
        initStaticFaces();
        //初始化表情
        initViewPager();

        initTusiji();

        mIvTypeEmoji.setSelected(true);
    }

    /**
     * 初始化兔斯基
     */
    private void initTusiji() {
        try {
            staticTusijiList = new ArrayList<String>();
            String[] faces = getAssets().list("tusiji/png");
            //将Assets中的表情名称转为字符串一一添加进staticFacesList
            for (int i = 0; i < faces.length; i++) {
                staticTusijiList.add(faces[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < tusijiPagerCount; i++) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.face_gridview, null);//表情布局
            GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);
            gridview.setNumColumns(4);
            List<String> subList = staticTusijiList.subList(i * staticTusijiList.size() / tusijiPagerCount, (i + 1) * staticTusijiList.size() / tusijiPagerCount);
            gridview.setAdapter(new TusijiGVAdapter(subList, this));
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String content = staticTusijiList.get(position);
                    ConversationModel model = new ConversationModel();
                    model.from = EMClient.getInstance().getCurrentUser();
                    model.to = mChatObj;
                    model.messageType = EMMessage.Type.CMD;
                    model.messageState = MessageState.UNDELIVERED;
                    model.message = content;
                    model.date = System.currentTimeMillis();

                    //添加到数据库
                    ConversationDao.insert(model);
                    if (RecentConversationDao.isChated(mChatObj)) {
                        //如果跟他聊过，更新最新聊天记录
                        RecentConversationDao.update(model, mChatObj);
                        LogUtils.d("___chatting 聊过");
                    } else {
                        //如果没跟他聊过，插入一条
                        RecentConversationDao.insert(model);
                        LogUtils.d("___chatting 没聊过");
                    }

                    mDatas.add(model);
                    mAdapter.notifyDataSetChanged();
                    //把RecyclerView定位到最底下
                    scrollToLast();
                }
            });

            tusijiViews.add(gridview);
        }
        mTusijiViewPager.setAdapter(new FaceVPAdapter(tusijiViews));
        mTusijiViewPager.setOnPageChangeListener(new PageChange());
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
        activityRootView.addOnLayoutChangeListener(this);

        mEmojiViewPager.setOnPageChangeListener(new PageChange());

        mRecyclerview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    KeyboardUtil.hideKeyboard(mEtInput);
                    mPanelRoot.setVisibility(View.GONE);
                    mIvFace.setSelected(false);
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

                    LogUtils.d("##Chatting中收到消息0.0");
                    mDatas.add(model);

                    UIUtils.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                            scrollToLast();
                        }
                    });

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
        mIvFace.setOnClickListener(this);
        mIvImg.setOnClickListener(this);
        mIvTypeEmoji.setOnClickListener(this);
        mIvTypeTusiji.setOnClickListener(this);
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
            clickPlus();
        } else if (v == mIvVoice) {
            clickVoice();
        } else if (v == mTvSend) {
            clickSendTXTMessage();
        } else if (v == mIvFace) {
            clickFace();
        } else if (v == mIvImg) {
            Intent intent = new Intent(this, SwitchImgActivity.class);
            intent.putExtra(KEY_CHAT_OBJ, mChatObj);
            startActivityForResult(intent, REQUEST_CODE_SWITCH_IMAGE);
        } else if (v == mIvTypeEmoji) {
            clickTypeEmoji();
        } else if (v == mIvTypeTusiji) {
            clickTypeTusiji();
        }
    }

    /**
     * 点击了兔斯基图标
     */
    private void clickTypeTusiji() {
        mIvTypeTusiji.setSelected(true);
        mIvTypeEmoji.setSelected(false);

        mEmojiViewPager.setVisibility(View.GONE);
        mTusijiViewPager.setVisibility(View.VISIBLE);


        mDotsLayout.removeAllViews();
        for (int i = 0; i < tusijiPagerCount; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(12, 12);
            params.leftMargin = UIUtils.dip2px(10);
            mDotsLayout.addView(dotsItem(i), params); //小圆点的显示
        }
        //默认使圆点选中第一个
        mDotsLayout.getChildAt(mTusijiViewPager.getCurrentItem()).setSelected(true);
    }

    /**
     * 点击了表情符号图标
     */
    private void clickTypeEmoji() {
        mIvTypeTusiji.setSelected(false);
        mIvTypeEmoji.setSelected(true);

        mEmojiViewPager.setVisibility(View.VISIBLE);
        mTusijiViewPager.setVisibility(View.GONE);

        mDotsLayout.removeAllViews();
        for (int i = 0; i < getPagerCount(); i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(12, 12);
            params.leftMargin = UIUtils.dip2px(10);
            mDotsLayout.addView(dotsItem(i), params); //小圆点的显示
        }
        //默认使圆点选中第一个
        mDotsLayout.getChildAt(mEmojiViewPager.getCurrentItem()).setSelected(true);
    }

    /**
     * 点击加号
     */
    private void clickPlus() {
        if (mPanelRoot.getVisibility() == View.VISIBLE) {
            if (mFaceContainer.getVisibility() == View.VISIBLE) {
                mFaceContainer.setVisibility(View.GONE);
                mPlusContainer.setVisibility(View.VISIBLE);
                mIvFace.setSelected(false);
            } else {
                KeyboardUtil.showKeyboard(mEtInput);
            }
        } else {
            KeyboardUtil.hideKeyboard(mEtInput);
            mPanelRoot.setVisibility(View.VISIBLE);
            mPlusContainer.setVisibility(View.VISIBLE);
            mFaceContainer.setVisibility(View.GONE);
            mIvFace.setSelected(false);
            scrollToLast();
        }
    }

    /**
     * 点击表情按钮
     */
    private void clickFace() {
        if (mIvFace.isSelected()) {
            //如果表情是亮的，再点一次，显示键盘
            KeyboardUtil.showKeyboard(mEtInput);
            mIvFace.setSelected(false);
        } else {
            //如果表情是灭的，再点一次，隐藏键盘
            KeyboardUtil.hideKeyboard(mEtInput);
            mPanelRoot.setVisibility(View.VISIBLE);
            mFaceContainer.setVisibility(View.VISIBLE);
            mPlusContainer.setVisibility(View.GONE);
            mIvFace.setSelected(true);
            scrollToLast();
        }
    }

    /**
     * 点击语言按钮
     */
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
        if (inputStr.matches("\\s+")) {
            Toast.makeText(this, "不能发送空白消息", Toast.LENGTH_SHORT).show();
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
        model.date = System.currentTimeMillis();

        //添加到数据库
        ConversationDao.insert(model);
        if (RecentConversationDao.isChated(mChatObj)) {
            //如果跟他聊过，更新最新聊天记录
            RecentConversationDao.update(model, mChatObj);
            LogUtils.d("___chatting 聊过");
        } else {
            //如果没跟他聊过，插入一条
            RecentConversationDao.insert(model);
            LogUtils.d("___chatting 没聊过");
        }

        mDatas.add(model);
        mAdapter.notifyDataSetChanged();
        //把RecyclerView定位到最底下
        scrollToLast();
        //把输入框清空
        mEtInput.setText("");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE_SWITCH_IMAGE && resultCode == RESULT_OK) {
            int count = intent.getIntExtra(SwitchImgActivity.KEY_SWITCH_IMAGE, 0);

            //根据选择的图片数，查询数据库中最后几条图片
            ArrayList<ConversationModel> models = ConversationDao.selectLastDatas(mChatObj, EMClient.getInstance().getCurrentUser(), count);
            mDatas.addAll(models);
            mAdapter.setDatas(mDatas);

            mAdapter.notifyDataSetChanged();
            scrollToLast();
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP &&
                event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mPanelRoot.getVisibility() == View.VISIBLE) {
                mPanelRoot.setVisibility(View.GONE);
                mIvFace.setSelected(false);
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

    @Override
    protected void onPause() {
        super.onPause();
        //TODO:先在这里取消小红点，因为暂时没有想到在会话界面显示红点的时候怎么对会话窗口的聊天对象进行区分
        RedPointManager.getInstance().disShow(mChatObj);
    }


    @Override
    public void onLayoutChange(View v, int left, int top, int right,
                               int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
            //Toast.makeText(this, "监听到软键盘弹起...", Toast.LENGTH_SHORT).show();
            mIvFace.setSelected(false);
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            //Toast.makeText(this, "监听到软件盘关闭...", Toast.LENGTH_SHORT).show();

        }

    }


    /**#################################################################**/
    /**                         表情部分                                **/


    /**
     * 初始化表情
     */
    private void initViewPager() {
        // 获取页数
        for (int i = 0; i < getPagerCount(); i++) {
            views.add(viewPagerItem(i));//给每一页添加对应的表情
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(12, 12);
            params.leftMargin = UIUtils.dip2px(10);
            mDotsLayout.addView(dotsItem(i), params); //小圆点的显示
        }
        //给表情Viewpager的适配器添加表情
        FaceVPAdapter mVpAdapter = new FaceVPAdapter(views);
        //绑定适配器
        mEmojiViewPager.setAdapter(mVpAdapter);
        //默认使圆点选中第一个
        mDotsLayout.getChildAt(0).setSelected(true);
    }

    /**
     * 初始化表情列表staticFacesList
     */
    private void initStaticFaces() {
        try {
            staticFacesList = new ArrayList<String>();
            String[] faces = getAssets().list("face/png");
            //将Assets中的表情名称转为字符串一一添加进staticFacesList
            for (int i = 0; i < faces.length; i++) {
                staticFacesList.add(faces[i]);
            }

            //去掉删除图片
            staticFacesList.remove("emotion_del_normal.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据页数，初始化每页显示的表情
     *
     * @param position
     * @return
     */
    private View viewPagerItem(int position) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.face_gridview, null);//表情布局
        GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);
        /**
         * 注：因为每一页末尾都有一个删除图标，所以每一页的实际表情columns *　rows　－　1; 空出最后一个位置给删除图标
         * */
        List<String> subList = new ArrayList<String>();
        subList.addAll(staticFacesList.subList(position * (columns * rows - 1),
                (columns * rows - 1) * (position + 1) > staticFacesList.size() ?
                        staticFacesList.size() : (columns * rows - 1) * (position + 1)));
        /**
         * 末尾添加删除图标
         * */
        subList.add("emotion_del_normal.png");
        FaceGVAdapter mGvAdapter = new FaceGVAdapter(subList, this);
        gridview.setAdapter(mGvAdapter);
        gridview.setNumColumns(columns);
        // 单击表情执行的操作
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String png = ((TextView) ((LinearLayout) view).getChildAt(1)).getText().toString();
                    if (!png.contains("emotion_del_normal")) {// 如果不是删除图标
                        insert(getFace(png)); //插入表情
                    } else {
                        delete(); //删除表情
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return gridview;
    }

    /**
     * 根据表情数量以及GridView设置的行数和列数计算Pager数量
     *
     * @return
     */
    private int getPagerCount() {
        int count = staticFacesList.size();
        return count % (columns * rows - 1) == 0 ? count / (columns * rows - 1)
                : count / (columns * rows - 1) + 1;
    }

    /**
     * 向输入框里添加表情
     */
    private void insert(CharSequence text) {
        int iCursorStart = Selection.getSelectionStart((mEtInput.getText()));
        int iCursorEnd = Selection.getSelectionEnd((mEtInput.getText()));
        if (iCursorStart != iCursorEnd) {
            mEtInput.getText().replace(iCursorStart, iCursorEnd, "");
        }
        int iCursor = Selection.getSelectionEnd((mEtInput.getText()));
        mEtInput.getText().insert(iCursor, text);
    }

    /**
     * 删除图标执行事件
     * 注：如果删除的是表情，在删除时实际删除的是tempText即图片占位的字符串，所以必需一次性删除掉tempText，才能将图片删除
     */
    private void delete() {
        if (mEtInput.getText().length() != 0) {
            int iCursorEnd = Selection.getSelectionEnd(mEtInput.getText());
            int iCursorStart = Selection.getSelectionStart(mEtInput.getText());
            if (iCursorEnd > 0) {
                if (iCursorEnd == iCursorStart) {
                    if (isDeletePng(iCursorEnd)) {
                        String st = "#[face/png/f_static_000.png]#";
                        ((Editable) mEtInput.getText()).delete(
                                iCursorEnd - st.length(), iCursorEnd);
                    } else {
                        ((Editable) mEtInput.getText()).delete(iCursorEnd - 1,
                                iCursorEnd);
                    }
                } else {
                    ((Editable) mEtInput.getText()).delete(iCursorStart,
                            iCursorEnd);
                }
            }
        }
    }


    private ImageView dotsItem(int position) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dot_image, null);
        ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
        iv.setId(position);
        return iv;
    }

    /**
     * 判断即将删除的字符串是否是图片占位字符串tempText 如果是：则将删除整个tempText
     **/
    private boolean isDeletePng(int cursor) {
        String st = "#[face/png/f_static_000.png]#";
        String content = mEtInput.getText().toString().substring(0, cursor);
        if (content.length() >= st.length()) {
            String checkStr = content.substring(content.length() - st.length(),
                    content.length());
            String regex = "(\\#\\[face/png/f_static_)\\d{3}(.png\\]\\#)";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(checkStr);
            return m.matches();
        }
        return false;
    }

    /**
     * 返回的是一个可以和文字并存的图片+文字
     *
     * @param png
     * @return
     */
    private SpannableStringBuilder getFace(String png) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        try {
            /**
             * 经过测试，虽然这里tempText被替换为png显示，但是但我单击发送按钮时，获取到輸入框的内容是tempText的值而不是png
             * 所以这里对这个tempText值做特殊处理
             * 格式：#[face/png/f_static_000.png]#，以方便判斷當前圖片是哪一個
             * */
            String tempText = "#[" + png + "]#";
            sb.append(tempText);
            sb.setSpan(
                    new ImageSpan(ChattingActivity.this, BitmapFactory
                            .decodeStream(getAssets().open(png))), sb.length()
                            - tempText.length(), sb.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//前后都不包括，即在指定范围的前面和后面插入新字符都不会应用新样式

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }

    /**
     * 表情页改变时，dots效果也要跟着改变
     */
    class PageChange implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
                mDotsLayout.getChildAt(i).setSelected(false);
            }
            mDotsLayout.getChildAt(arg0).setSelected(true);
        }

    }
}
