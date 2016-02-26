package me.liujia95.instantmessaging.fragment;

import android.view.View;

import me.liujia95.instantmessaging.base.BaseFragment;


/**
 * Created by Administrator on 2016/2/12 16:27.
 */
public class SettingFragment extends BaseFragment {

    @Override
    protected View onInitSuccessView() {
        return null;
    }

    @Override
    protected LoadingUI.ResultState onStartLoadData() {
        return LoadingUI.ResultState.EMPTY;
    }
}
