package font;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class NewFontImage extends ImageFont {

	private float padding = 2;
	private float scaleX = 1f;
	private float scaleY = 1f;

	public NewFontImage(int size, Bitmap bmp) {
		super(size, bmp);
	}

	@Override
	public void init() {
		hash.put('0', new Rect(0, 0, 37, 39));
		hash.put('1', new Rect(40, 0, 66, 39));
		hash.put('2', new Rect(74, 0, 107, 39));
		hash.put('3', new Rect(114, 0, 147, 39));
		hash.put('4', new Rect(150, 0, 186, 39));
		hash.put('5', new Rect(191, 0, 225, 39));
		hash.put('6', new Rect(229, 0, 264, 39));
		hash.put('7', new Rect(270, 0, 304, 39));
		hash.put('8', new Rect(307, 0, 343, 39));
		hash.put('9', new Rect(347, 0, 383, 39));
	}

	public void drawString(Canvas canvas, String aString, int aX, int aY) {
		int length = aString.length();
		int ratingH = size / CURRENT_HEIGHT;
		int cLeft = aX;

		for (int i = 0; i < length; i++) {

			if (hash.containsKey(aString.charAt(i))) {
				Rect src = hash.get(aString.charAt(i));
				Rect dst = new Rect(cLeft, aY, cLeft + (int) (src.width() * scaleX), aY + (int) (src.height() * scaleY));

				canvas.drawBitmap(bmp, src, dst, null);
				cLeft += dst.width() + padding;
			}
		}
	}
}
