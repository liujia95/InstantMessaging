package me.liujia95.instantmessaging.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;
import me.liujia95.instantmessaging.activity.LoginActivity;
import me.liujia95.instantmessaging.base.ParentFragment;
import me.liujia95.instantmessaging.manager.ThreadPoolManager;
import me.liujia95.instantmessaging.utils.LogUtils;


/**
 * Created by Administrator on 2016/2/12 16:27.
 */
public class SettingFragment extends ParentFragment implements View.OnClickListener {

    @InjectView(R.id.setting_btn_logout)
    Button mBtnLogout;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_setting, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        mBtnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnLogout) {
            clickLogout();
        }
    }

    /**
     * 登出
     */
    private void clickLogout() {
        ThreadPoolManager.getLongPool().execute(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        LogUtils.d("登出成功:"+EMClient.getInstance().getCurrentUser());
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        // TODO Auto-generated method stub
                        LogUtils.d("登出 progress:" + progress + " -- status:" + status);
                    }

                    @Override
                    public void onError(int code, String message) {
                        // TODO Auto-generated method stub
                        LogUtils.d("登出异常：--code:" + code + " -- message:" + message);
                    }
                });
            }
        });
    }
}
