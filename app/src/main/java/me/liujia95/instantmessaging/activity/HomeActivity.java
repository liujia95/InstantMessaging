package me.liujia95.instantmessaging.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.base.ParentFragment;
import me.liujia95.instantmessaging.db.dao.ConversationDao;
import me.liujia95.instantmessaging.db.dao.RecentConversationDao;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.fragment.ConversationListFragment;
import me.liujia95.instantmessaging.fragment.FriendsListFragment;
import me.liujia95.instantmessaging.fragment.SettingFragment;
import me.liujia95.instantmessaging.utils.ConversationUtils;
import me.liujia95.instantmessaging.utils.LogUtils;
import me.liujia95.instantmessaging.view.ChangeColorIconWithText;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    @InjectView(R.id.home_viewpager)
    ViewPager               mViewPager;
    @InjectView(R.id.home_indicator_one)
    ChangeColorIconWithText mIndicatorOne;
    @InjectView(R.id.home_indicator_two)
    ChangeColorIconWithText mIndicatorTwo;
    @InjectView(R.id.home_indicator_four)
    ChangeColorIconWithText mIndicatorFour;

    private List<ParentFragment> mTabs = new ArrayList<>();

    private FragmentPagerAdapter mAdapter;

    private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<>();
    private ConversationListFragment mConversationListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        initListener();
        initDatas();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        ButterKnife.inject(this);
        mTabIndicators.add(mIndicatorOne);
        mTabIndicators.add(mIndicatorTwo);
        mTabIndicators.add(mIndicatorFour);
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        mConversationListFragment = new ConversationListFragment();
        mTabs.add(mConversationListFragment);
        mTabs.add(new FriendsListFragment());
        mTabs.add(new SettingFragment());

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mTabs.get(position);
            }

            @Override
            public int getCount() {
                return mTabs.size();
            }
        };

        mViewPager.setAdapter(mAdapter);
        //因为viewpager初始化是不会走onPageSelected事件，要手动让它加载一次
        mViewPager.setCurrentItem(1);
        mViewPager.setCurrentItem(0);
    }

    /**
     * 初始化事件
     */
    private void initListener() {
        mIndicatorOne.setOnClickListener(this);
        mIndicatorTwo.setOnClickListener(this);
        mIndicatorFour.setOnClickListener(this);

        //默认第一个是不透明
        mIndicatorOne.setIconAlpha(1.0f);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.e("TAG", "position = " + position + " , positionOffset = " + positionOffset);

                if (positionOffset > 0) {
                    ChangeColorIconWithText left = mTabIndicators.get(position);
                    ChangeColorIconWithText right = mTabIndicators.get(position + 1);

                    left.setIconAlpha(1 - positionOffset);
                    right.setIconAlpha(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
                ParentFragment fragment = mTabs.get(position);
                fragment.loadData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        EMMessageListener msgListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
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
                    LogUtils.d("===11时间：" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds());


                    ConversationModel model = ConversationUtils.getMessageToModel(message);

                    //添加到会话的数据库
                    ConversationDao.insert(model);
                    if (RecentConversationDao.isChated(message.getFrom())) {
                        //如果跟他聊过，更新最新聊天记录
                        RecentConversationDao.update(model, EMClient.getInstance().getCurrentUser());
                    } else {
                        //如果没跟他聊过，插入一条
                        RecentConversationDao.insert(model);
                    }

                    //设置监听
                    mMessageListener.onHasNewMessage(model);
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

    @Override
    public void onClick(View v) {
        resetOtherTabs();
        switch (v.getId()) {
            case R.id.home_indicator_one:
                mTabIndicators.get(0).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.home_indicator_two:
                mTabIndicators.get(1).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.home_indicator_four:
                mTabIndicators.get(2).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(2, false);
                break;
        }
    }

    /**
     * 重置其他的TabIndicator的颜色
     */
    private void resetOtherTabs() {
        for (int i = 0; i < mTabIndicators.size(); i++) {
            mTabIndicators.get(i).setIconAlpha(0);
        }
    }

    /**
     * 设置menu显示icon
     *
     * @param featureId
     * @param menu
     * @return
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_friend:
                clickAddFriend();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 点击添加朋友
     */
    private void clickAddFriend() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("添加好友");
        View view = View.inflate(this, R.layout.dialog_add_friend, null);
        builder.setView(view);

        final EditText et_username = (EditText) view.findViewById(R.id.addfriend_et_username);
        final EditText et_reason = (EditText) view.findViewById(R.id.addfriend_et_reason);
        Button btn_add = (Button) view.findViewById(R.id.addfriend_btn_add);
        Button btn_cancel = (Button) view.findViewById(R.id.addfriend_btn_cancel);

        //弹出Dialog
        builder.create();
        final AlertDialog dialog = builder.show();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO：添加好友
                String toAddUsername = et_username.getText().toString();
                String reason = et_reason.getText().toString();
                if (TextUtils.isEmpty(toAddUsername)) {
                    Toast.makeText(HomeActivity.this, "请输入好友帐号", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(reason)) {
                    Toast.makeText(HomeActivity.this, "请输入添加理由", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    EMClient.getInstance().contactManager().addContact(toAddUsername, reason);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }

                Toast.makeText(HomeActivity.this, "发送请求成功，等待对方验证", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    private OnMessageListener mMessageListener;

    public void setMessageListener(OnMessageListener messageListener) {
        this.mMessageListener = messageListener;
    }

    public interface OnMessageListener {
        void onHasNewMessage(ConversationModel model);
    }


}
