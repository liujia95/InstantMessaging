package me.liujia95.instantmessaging.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.adapter.ConversationAdapter;
import me.liujia95.instantmessaging.base.ParentFragment;
import me.liujia95.instantmessaging.bean.ConversationBean;
import me.liujia95.instantmessaging.data.ConversationDatas;
import me.liujia95.instantmessaging.manager.ThreadPoolManager;
import me.liujia95.instantmessaging.utils.UIUtils;

/**
 * Created by Administrator on 2016/2/12 16:25.
 */
public class ConversationFragment extends ParentFragment {

    @InjectView(R.id.conversation_recyclerview)
    RecyclerView mRecyclerView;

    List<ConversationBean> mDatas;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_conversation, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        mDatas = ConversationDatas.createDatas();

        ThreadPoolManager.getLongPool().execute(new Runnable() {
            @Override
            public void run() {
                //获取好友列表 开发者需要根据username去自己服务器获取好友的详情
                // 注：SDK不提供好友查找的服务, 如需要查找好友, 需要调用开发者自己服务器的用户查询接口
                try {
                    List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mRecyclerView.setAdapter(new ConversationAdapter(mDatas));
    }
}
