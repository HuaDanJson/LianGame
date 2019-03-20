package com.yottamobile.doraemon.activity;

import android.os.Bundle;

import com.yottamobile.doraemon.Pikachu;
import com.yottamobile.doraemon.R;
import com.yottamobile.doraemon.base.BaseActivity;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //第一：默认初始化
        Bmob.initialize(this, "f234cdf0e315906726f19e377fced71e");
        if (BmobUser.isLogin()) {
            toActivity(Pikachu.class);
        } else {
            toActivity(LoginActivity.class);
        }
        finish();
    }
}
