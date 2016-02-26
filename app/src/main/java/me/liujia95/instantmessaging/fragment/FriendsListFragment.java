package me.liujia95.instantmessaging.fragment;

import android.view.View;

import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.base.BaseFragment;
import me.liujia95.instantmessaging.utils.UIUtils;


/**
 * Created by Administrator on 2016/2/12 16:26.
 */
public class FriendsListFragment extends BaseFragment {

    @Override
    protected View onInitSuccessView() {
        View.inflate(UIUtils.getContext(), R.layout.fragment_friendslist,null);
        return null;
    }

    @Override
    protected LoadingUI.ResultState onStartLoadData() {
        return LoadingUI.ResultState.EMPTY;
    }
}
