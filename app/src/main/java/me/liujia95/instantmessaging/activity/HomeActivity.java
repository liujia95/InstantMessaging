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

import com.david.gradientuilibrary.GradientIconView;
import com.david.gradientuilibrary.GradientTextView;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.base.ParentFragment;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.fragment.ConversationListFragment;
import me.liujia95.instantmessaging.fragment.FriendsListFragment;
import me.liujia95.instantmessaging.fragment.SettingFragment;
import me.liujia95.instantmessaging.utils.ConversationUtils;
import me.liujia95.instantmessaging.utils.LogUtils;
import me.liujia95.instantmessaging.view.ChangeColorIconWithText;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    @InjectView(R.id.home_viewpager)
    ViewPager mViewPager;

    private List<GradientIconView> mTabIconIndicator = new ArrayList<>();
    private List<GradientTextView> mTabTextIndicator = new ArrayList<>();
    @InjectView(R.id.id_iconfont_chat)
    GradientIconView mChatsIconView;
    @InjectView(R.id.id_chats_tv)
    GradientTextView mTvChats;
    @InjectView(R.id.id_iconfont_friend)
    GradientIconView mContactsIconView;
    @InjectView(R.id.id_contacts_tv)
    GradientTextView mTvContacts;
    @InjectView(R.id.id_iconfont_me)
    GradientIconView mAboutMeIconView;
    @InjectView(R.id.id_about_me_tv)
    GradientTextView mTvAboutMe;

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

        mChatsIconView.setOnClickListener(this);
        mTabIconIndicator.add(mChatsIconView);
        mChatsIconView.setIconAlpha(1.0f);

        mContactsIconView.setOnClickListener(this);
        mTabIconIndicator.add(mContactsIconView);

        mAboutMeIconView.setOnClickListener(this);
        mTabIconIndicator.add(mAboutMeIconView);

        mTvChats.setOnClickListener(this);
        mTabTextIndicator.add(mTvChats);
        mTvChats.setTextViewAlpha(1.0f);

        mTvContacts.setOnClickListener(this);
        mTabTextIndicator.add(mTvContacts);

        mTvAboutMe.setOnClickListener(this);
        mTabTextIndicator.add(mTvAboutMe);

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
     * 重置其他的Tab
     */
    private void resetOtherTabs() {
        resetOtherTabIcons();
        resetOtherTabText();
    }

    /**
     * 重置其他的Tab icon
     */
    private void resetOtherTabIcons() {
        for (int i = 0; i < mTabIconIndicator.size(); i++) {
            mTabIconIndicator.get(i).setIconAlpha(0);
        }
    }

    /**
     * 重置其他的Tab text
     */
    private void resetOtherTabText() {
        for (int i = 0; i < mTabTextIndicator.size(); i++) {
            mTabTextIndicator.get(i).setTextViewAlpha(0);
        }
    }

    /**
     * 初始化事件
     */
    private void initListener() {

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0) {
                    GradientIconView iconLeft = mTabIconIndicator.get(position);
                    GradientIconView iconRight = mTabIconIndicator.get(position + 1);

                    GradientTextView textLeft = mTabTextIndicator.get(position);
                    GradientTextView textRight = mTabTextIndicator.get(position + 1);

                    iconLeft.setIconAlpha(1 - positionOffset);
                    textLeft.setTextViewAlpha(1 - positionOffset);
                    iconRight.setIconAlpha(positionOffset);
                    textRight.setTextViewAlpha(positionOffset);
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
                ConversationUtils.receivedMessage(messages, mMessageListener);
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
                LogUtils.d("@@收到透传消息：----");
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {
                //收到已读回执
                LogUtils.d("@@已读：----");

            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
                //收到已送达回执
                LogUtils.d("@@已送达：----");
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
                LogUtils.d("@@消息状态变动: message:" + message + "--change:" + change);
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }


    @Override
    public void onClick(View v) {
        resetOtherTabs();
        switch (v.getId()) {
            case R.id.id_iconfont_chat:
            case R.id.id_chats_tv:
                mTabIconIndicator.get(0).setIconAlpha(1.0f);
                mTabTextIndicator.get(0).setTextViewAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.id_iconfont_friend:
            case R.id.id_contacts_tv:
                mTabIconIndicator.get(1).setIconAlpha(1.0f);
                mTabTextIndicator.get(1).setTextViewAlpha(1.0f);
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.id_iconfont_me:
            case R.id.id_about_me_tv:
                mTabIconIndicator.get(2).setIconAlpha(1.0f);
                mTabTextIndicator.get(2).setTextViewAlpha(1.0f);
                mViewPager.setCurrentItem(2, false);
                break;
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
