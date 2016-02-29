package me.liujia95.instantmessaging.fragment;

import android.view.View;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;

import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.base.BaseFragment;
import me.liujia95.instantmessaging.utils.LogUtils;
import me.liujia95.instantmessaging.utils.UIUtils;


/**
 * Created by Administrator on 2016/2/12 16:26.
 */
public class FriendsListFragment extends BaseFragment {

    @Override
    protected View onInitSuccessView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.fragment_friendslist, null);

        return view;
    }

    @Override
    protected LoadingUI.ResultState onStartLoadData() {
        return LoadingUI.ResultState.SUCCESS;
    }

    @Override
    public void initData() {
//        ThreadPoolManager.getLongPool().execute(new Runnable() {
//            @Override
//            public void run() {
//                //获取好友列表 开发者需要根据username去自己服务器获取好友的详情
//                // 注：SDK不提供好友查找的服务, 如需要查找好友, 需要调用开发者自己服务器的用户查询接口
//                try {
//
//                    usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
//
//                } catch (HyphenateException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
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
    }
}
