package com.yottamobile.doraemon.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class SlowGallery extends Gallery {

	public SlowGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SlowGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SlowGallery(Context context) {
		super(context);
	}

//	@Override
//	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//
//		// limit the max speed in either direction
//		// if (velocityX > Pikachu.WIN_DOW_W-300) {
//		// velocityX = Pikachu.WIN_DOW_W-300;
//		// } else if (velocityX < Pikachu.WIN_DOW_W-300) {
//		// velocityX = -(Pikachu.WIN_DOW_W-300f);
//		// }
//		return super.onFling(e1, e2, velocityX, velocityY);
//	}

	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
		return e2.getX() > e1.getX();
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		int kEvent;
		if (isScrollingLeft(e1, e2)) { // Check if scrolling left
			kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
		} else { // Otherwise scrolling right
			kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
		}
//		onKeyDown(kEvent, null);
		return false;
	}
}
