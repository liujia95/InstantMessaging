package me.liujia95.instantmessaging.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/1/20 10:04.
 */
public abstract class ParentFragment extends Fragment {
    protected View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = initView(inflater);
        return mRootView;
    }

    public View getRootView() {
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
        loadData();
    }

    /**
     * @param inflater
     * @return
     * @des 初始化view，需要子类复写
     */
    protected abstract View initView(LayoutInflater inflater);

    public void loadData() {

    }

    /**
     * @des 初始化数据
     */
    public void initData() {
    }

    /**
     * @des 初始化事件
     */
    public void initListener() {
    }
}
