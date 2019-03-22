package com.yottamobile.doraemon;

import android.graphics.Bitmap;

import org.andengine.entity.util.ScreenCapture;

public class MScreenCapture extends ScreenCapture{
	public Bitmap bm;

	@Override
	public void onScreenGrabbed(Bitmap pBitmap) {
		bm = pBitmap;
		super.onScreenGrabbed(pBitmap);
	}
	
}
