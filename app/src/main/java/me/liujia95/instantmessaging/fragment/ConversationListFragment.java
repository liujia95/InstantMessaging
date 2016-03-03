package me.liujia95.instantmessaging.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.hyphenate.chat.EMClient;

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
import me.liujia95.instantmessaging.utils.UIUtils;

/**
 * Created by Administrator on 2016/2/12 16:25.
 */
public class ConversationListFragment extends ParentFragment implements ConversationListAdapter.OnItemClickListener {

    @InjectView(R.id.conversation_list_recyclerview)
    RecyclerView mRecyclerView;

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
