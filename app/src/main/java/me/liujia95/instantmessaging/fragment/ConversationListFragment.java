package me.liujia95.instantmessaging.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.NetUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.activity.ChattingActivity;
import me.liujia95.instantmessaging.activity.HomeActivity;
import me.liujia95.instantmessaging.adapter.ConversationListAdapter;
import me.liujia95.instantmessaging.base.ParentFragment;
import me.liujia95.instantmessaging.db.dao.RecentConversationDao;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.utils.ConversationUtils;
import me.liujia95.instantmessaging.utils.LogUtils;
import me.liujia95.instantmessaging.utils.UIUtils;

/**
 * Created by Administrator on 2016/2/12 16:25.
 */
public class ConversationListFragment extends ParentFragment implements ConversationListAdapter.OnItemClickListener {

    @InjectView(R.id.conversation_list_recyclerview)
    RecyclerView   mRecyclerView;
    @InjectView(R.id.conversation_list_rl_network_anomaly)
    RelativeLayout mRlNetworkAnomaly; //网络异常

    List<ConversationModel> mDatas;
    private ConversationListAdapter mAdapter;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_conversation_list, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        mDatas = RecentConversationDao.selectAll(EMClient.getInstance().getCurrentUser());

        mAdapter = new ConversationListAdapter(mDatas);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        mAdapter.setOnItemClickListener(this);

        HomeActivity homeActivity = (HomeActivity) getActivity();
        homeActivity.setMessageListener(new HomeActivity.OnMessageListener() {
            @Override
            public void onHasNewMessage(ConversationModel model) {
                refreshUI();
            }
        });

        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
    }

    //实现ConnectionListener接口 自动重连
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
            LogUtils.d("@@已连接到服务器");
            disShowNetworkAnomaly();
        }

        @Override
        public void onDisconnected(final int error) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                        LogUtils.d("@@帐号被移除");
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登陆
                        LogUtils.d("@@帐号在其他设备登陆");
                    } else {
                        if (NetUtils.hasNetwork(getActivity())) {
                            //连接不到聊天服务器
                            LogUtils.d("@@连接不到聊天服务器");
                        } else {
                            //当前网络不可用，请检查网络设置
                            LogUtils.d("@@当前网络不可用，请检查网络设置");
                            showNetworkAnomaly();
                        }
                    }
                }
            });
        }
    }

    public void showNetworkAnomaly() {
        UIUtils.post(new Runnable() {
            @Override
            public void run() {
                if (mRlNetworkAnomaly.getVisibility() == View.GONE) {
                    LogUtils.d("@@显示");
                    mRlNetworkAnomaly.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void disShowNetworkAnomaly() {
        UIUtils.post(new Runnable() {
            @Override
            public void run() {
                if (mRlNetworkAnomaly.getVisibility() == View.VISIBLE) {
                    LogUtils.d("@@不显示");
                    mRlNetworkAnomaly.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 刷新界面
     */
    public void refreshUI() {
        mDatas = RecentConversationDao.selectAll(EMClient.getInstance().getCurrentUser());
        UIUtils.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.setData(mDatas);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(View view, ConversationModel model) {
        //获取会话对象
        String chatObj = ConversationUtils.getChatObj(model);

        Intent intent = new Intent(getActivity(), ChattingActivity.class);
        intent.putExtra(ChattingActivity.KEY_CHAT_OBJ, chatObj);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshUI();
    }


}
