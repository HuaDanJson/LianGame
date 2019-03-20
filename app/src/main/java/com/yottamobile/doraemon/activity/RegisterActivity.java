package com.yottamobile.doraemon.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.yottamobile.doraemon.R;
import com.yottamobile.doraemon.base.BaseActivity;
import com.yottamobile.doraemon.bean.CurrentUser;
import com.yottamobile.doraemon.utils.ToastHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class RegisterActivity extends BaseActivity {

    @BindView(R.id.edt_user_name) EditText mUserName;
    @BindView(R.id.edt_pwd) EditText mPwd;
    @BindView(R.id.btn_register) Button mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_register)
    public void register() {
        String userName = mUserName.getText().toString();
        String pwd = mPwd.getText().toString();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)) {
            ToastHelper.showShortMessage("账号或密码不能为空");
        } else {
            final CurrentUser user = new CurrentUser();
            user.setUsername(userName);
            user.setPassword(pwd);
            user.setScore(0);
            user.setTextColor(0);
            user.setHelperCards(0);
            user.setDoubleCards(0);
            user.signUp(new SaveListener<CurrentUser>() {
                @Override
                public void done(CurrentUser user, BmobException e) {
                    if (e == null) {
                        BmobUser.logOut();
                        ToastHelper.showShortMessage("注册成功");
                        finish();
                    } else {
                        ToastHelper.showShortMessage("注册失败：" + e.getMessage());
                    }
                }
            });
        }
    }
}
