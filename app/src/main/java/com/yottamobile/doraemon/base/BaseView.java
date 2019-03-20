package com.yottamobile.doraemon.base;

import android.app.Activity;

public interface BaseView<T> {

    void setPresenter(T presenter);

    Activity getActivityContext();

}
