package com.yottamobile.doraemon;

import org.andengine.entity.util.ScreenCapture;

import android.graphics.Bitmap;

public class MScreenCapture extends ScreenCapture{
	public Bitmap bm;
	
	
	
	
	
	@Override
	public void onScreenGrabbed(Bitmap pBitmap) {
		bm = pBitmap;
		super.onScreenGrabbed(pBitmap);
	}
	
}
