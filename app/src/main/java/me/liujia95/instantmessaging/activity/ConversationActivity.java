package me.liujia95.instantmessaging.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hyphenate.chat.EMClient;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.adapter.ConversationAdapter;
import me.liujia95.instantmessaging.db.dao.ConversationDao;
import me.liujia95.instantmessaging.db.model.ConversationModel;
import me.liujia95.instantmessaging.fragment.ConversationListFragment;
import me.liujia95.instantmessaging.utils.ConversationUtils;

/**
 * Created by Administrator on 2016/3/1 14:14.
 */
public class ConversationActivity extends Activity {

    @InjectView(R.id.conversation_recyclerview)
    RecyclerView mRecyclerview;
    private ConversationAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        initView();
        initData();
    }

    private void initView() {
        ButterKnife.inject(this);
    }

    private void initData() {
        Intent intent = getIntent();
        String chatObj = intent.getStringExtra(ConversationListFragment.KEY_CHAT_OBJ);
        //查询本用户与此会话对象的所有会话
        List<ConversationModel> list = ConversationDao.selectAllByChatObj(chatObj, EMClient.getInstance().getCurrentUser());
        //遍历与其的所有会话
        ConversationUtils.ergodicConvertion(list);

        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ConversationAdapter(list);
        mRecyclerview.setAdapter(mAdapter);
    }
}
