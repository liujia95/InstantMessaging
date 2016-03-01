package me.liujia95.instantmessaging.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.liujia95.instantmessaging.R;

/**
 * Created by Administrator on 2016/2/25 14:56.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    @InjectView(R.id.et_username)
    EditText mEtUsername;
    @InjectView(R.id.et_password)
    EditText mEtPassword;
    @InjectView(R.id.btn_login)
    Button   mBtnLogin;
    @InjectView(R.id.btn_register)
    Button   mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initListener();
    }

    private void initView() {
        ButterKnife.inject(this);
    }

    private void initListener() {
        mBtnLogin.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnLogin) {
            clickLogin();
        } else if (v == mBtnRegister) {
            clickRegister();
        }
    }

    /**
     * 点击登录
     */
    private void clickLogin() {
        final String username = mEtUsername.getText().toString();
        final String password = mEtPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            mEtUsername.setError("用户名不能为空");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            mEtPassword.setError("密码不能为空");
            return;
        }

        EMClient.getInstance().login(username, password, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        Log.d("main", username + "登陆聊天服务器成功！");
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);

                        finish();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登陆聊天服务器失败！code:" + code + "--message:" + message);
            }
        });
    }

    /**
     * 点击注册
     */
    private void clickRegister() {
    }
}
