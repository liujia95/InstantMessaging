package me.liujia95.instantmessaging.fragment;

import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.adapter.FriendListAdapter;
import me.liujia95.instantmessaging.base.ParentFragment;
import me.liujia95.instantmessaging.bean.FriendInfoBean;
import me.liujia95.instantmessaging.manager.ThreadPoolManager;
import me.liujia95.instantmessaging.utils.ListUtil;
import me.liujia95.instantmessaging.utils.LogUtils;
import me.liujia95.instantmessaging.utils.UIUtils;
import me.liujia95.instantmessaging.view.SlideBar;


/**
 * Created by Administrator on 2016/2/12 16:26.
 */
public class FriendsListFragment extends ParentFragment implements FriendListAdapter.OnItemClickListener, SlideBar.OnTouchAssortListener {

    private List<FriendInfoBean> mDatas;
    private FriendListAdapter    mAdapter;

    @InjectView(R.id.friendlist_recyclerview)
    RecyclerView mRecyclerView;
    @InjectView(R.id.friendlist_slidebar)
    SlideBar     mSlidebar;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_friendslist, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        mDatas = new ArrayList<>();

        //TODO:测试用
        String[] names = UIUtils.getStringArray(R.array.array_name);
        for (String s : names) {
            FriendInfoBean bean = new FriendInfoBean(R.drawable.em_default_avatar, s, FriendInfoBean.TYPE_DATA);
            mDatas.add(bean);
        }

        ThreadPoolManager.getLongPool().execute(new Runnable() {
            @Override
            public void run() {
                //获取好友列表 开发者需要根据username去自己服务器获取好友的详情
                // 注：SDK不提供好友查找的服务, 如需要查找好友, 需要调用开发者自己服务器的用户查询接口
                try {
                    List<String> list = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    //都设成默认头像
                    for (String s : list) {
                        LogUtils.d("-- 好友名字：" + s);
                        FriendInfoBean bean = new FriendInfoBean(R.drawable.em_default_avatar, s, FriendInfoBean.TYPE_DATA);
                        mDatas.add(bean);
                    }
                    LogUtils.d("*** 好友(个)：" + mDatas.size());

                    UIUtils.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setData(mDatas);
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });

        SystemClock.sleep(200);//TODO：测试用

        //排序
        ListUtil.sortList(mDatas);

        mAdapter = new FriendListAdapter(mDatas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public void initListener() {

        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {

            @Override
            public void onContactAgreed(String username) {
                //好友请求被同意
                LogUtils.d("好友请求被同意 username:" + username);
            }

            @Override
            public void onContactRefused(String username) {
                //好友请求被拒绝
                LogUtils.d("好友请求被拒绝 username:" + username);
            }

            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友邀请
                LogUtils.d("收到好友邀请 username:" + username + "--reason:" + reason);

            }

            @Override
            public void onContactDeleted(String username) {
                //被删除时回调此方法
                LogUtils.d("被删除 username:" + username);
            }


            @Override
            public void onContactAdded(String username) {
                //增加了联系人时回调此方法
                LogUtils.d("增加了联系人 username" + username);
            }
        });

        mAdapter.setOnItemClickListener(this);
        mSlidebar.setOnTouchAssortListener(this);
    }

    @Override
    public void onItemClick(View view, FriendInfoBean bean) {
        if (bean.item_type == FriendInfoBean.TYPE_CHARACTER) {
            return;
        }
        if (bean.name.equals(UIUtils.getString(R.string.apply_and_notification))) {

        } else if (bean.name.equals(UIUtils.getString(R.string.group_chat))) {

        } else if (bean.name.equals(UIUtils.getString(R.string.chat_room))) {

        } else if (bean.name.equals(UIUtils.getString(R.string.huanxin_helper))) {

        }
    }

    /**
     * @param s 右侧索引点击下去的字母
     */
    @Override
    public void onTouchAssortListener(String s) {
        if (s.equals("↑")) {
            //如果是箭头回到最上面
            LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            manager.scrollToPositionWithOffset(0, 0);
            return;
        }
        int select = getSelectIndex(s);
        if (select != -1) {
            select += 4; //TODO:定位到的位置还需+应用自带的4个item
            LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            manager.scrollToPositionWithOffset(select, 0);
        }
    }

    private int getSelectIndex(String s) {
        for (int i = 0; i < mDatas.size(); i++) {
            String name = mDatas.get(i).name;
            if (name.equals(s)) {
                return i;
            }
        }
        return -1;
    }

}
