package com.yottamobile.doraemon.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.yottamobile.doraemon.Pikachu;
import com.yottamobile.doraemon.R;
import com.yottamobile.doraemon.base.BaseActivity;
import com.yottamobile.doraemon.bean.CurrentUser;
import com.yottamobile.doraemon.utils.CurrentUserHelper;
import com.yottamobile.doraemon.utils.ToastHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class LoginActivity extends BaseActivity {

    @BindView(R.id.edt_user_name) EditText mUserNameEditText;
    @BindView(R.id.edt_pwd) EditText mPwdEditText;
    @BindView(R.id.btn_login) Button mLoginButton;
    @BindView(R.id.btn_register) Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Bmob.initialize(this, "f234cdf0e315906726f19e377fced71e");
    }

    @OnClick(R.id.btn_login)
    public void loginClicked() {
        String userName = mUserNameEditText.getText().toString();
        String pwd = mPwdEditText.getText().toString();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)) {
            ToastHelper.showShortMessage("账号或密码不能为空");
        } else {
            final CurrentUser user = new CurrentUser();
            user.setUsername(userName);
            user.setPassword(pwd);
            user.login(new SaveListener<CurrentUser>() {
                @Override
                public void done(CurrentUser bmobUser, BmobException e) {
                    if (e == null) {
                        CurrentUser user = BmobUser.getCurrentUser(CurrentUser.class);
                        toActivity(Pikachu.class);
                        ToastHelper.showShortMessage("登录成功");
                        CurrentUserHelper.getInstance().updateCurrentUser(user);
                        finish();
                    } else {
                        ToastHelper.showShortMessage("登录失败：" + e.getMessage());
                    }
                }
            });
        }
    }

    @OnClick(R.id.btn_register)
    public void registerClicked() {
        toActivity(RegisterActivity.class);
    }
}
