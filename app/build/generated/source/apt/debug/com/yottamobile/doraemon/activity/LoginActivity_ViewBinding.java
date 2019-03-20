// Generated code from Butter Knife. Do not modify!
package com.yottamobile.doraemon.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.yottamobile.doraemon.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginActivity_ViewBinding implements Unbinder {
  private LoginActivity target;

  private View view2131427450;

  private View view2131427451;

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LoginActivity_ViewBinding(final LoginActivity target, View source) {
    this.target = target;

    View view;
    target.mUserNameEditText = Utils.findRequiredViewAsType(source, R.id.edt_user_name, "field 'mUserNameEditText'", EditText.class);
    target.mPwdEditText = Utils.findRequiredViewAsType(source, R.id.edt_pwd, "field 'mPwdEditText'", EditText.class);
    view = Utils.findRequiredView(source, R.id.btn_login, "field 'mLoginButton' and method 'loginClicked'");
    target.mLoginButton = Utils.castView(view, R.id.btn_login, "field 'mLoginButton'", Button.class);
    view2131427450 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.loginClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_register, "field 'mRegisterButton' and method 'registerClicked'");
    target.mRegisterButton = Utils.castView(view, R.id.btn_register, "field 'mRegisterButton'", Button.class);
    view2131427451 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.registerClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    LoginActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mUserNameEditText = null;
    target.mPwdEditText = null;
    target.mLoginButton = null;
    target.mRegisterButton = null;

    view2131427450.setOnClickListener(null);
    view2131427450 = null;
    view2131427451.setOnClickListener(null);
    view2131427451 = null;
  }
}
