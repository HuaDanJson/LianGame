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

public class RegisterActivity_ViewBinding implements Unbinder {
  private RegisterActivity target;

  private View view2131427451;

  @UiThread
  public RegisterActivity_ViewBinding(RegisterActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public RegisterActivity_ViewBinding(final RegisterActivity target, View source) {
    this.target = target;

    View view;
    target.mUserName = Utils.findRequiredViewAsType(source, R.id.edt_user_name, "field 'mUserName'", EditText.class);
    target.mPwd = Utils.findRequiredViewAsType(source, R.id.edt_pwd, "field 'mPwd'", EditText.class);
    view = Utils.findRequiredView(source, R.id.btn_register, "field 'mRegister' and method 'register'");
    target.mRegister = Utils.castView(view, R.id.btn_register, "field 'mRegister'", Button.class);
    view2131427451 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.register();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    RegisterActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mUserName = null;
    target.mPwd = null;
    target.mRegister = null;

    view2131427451.setOnClickListener(null);
    view2131427451 = null;
  }
}
