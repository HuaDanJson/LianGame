package font;

import java.util.Hashtable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class ImageFont extends IFont {

	protected final int CURRENT_HEIGHT = 8;
	protected final int CURRENT_WIDTH = 7;

	protected int size;
	protected Bitmap bmp;
	protected Hashtable<Character, Rect> hash = new Hashtable<Character, Rect>();
	protected Paint paint;

	public ImageFont(int size, Bitmap bmp) {
		this.size = size;
		this.bmp = bmp;

		paint = new Paint();
		paint.setTextSize(size);
		init();
	}

	public void init() {
		hash.put('0', new Rect(0, 0, 6, 8));
		hash.put('1', new Rect(0, 8, 6, 16));
		hash.put('2', new Rect(0, 16, 6, 24));
		hash.put('3', new Rect(0, 24, 6, 32));
		hash.put('4', new Rect(0, 32, 6, 40));
		hash.put('5', new Rect(0, 40, 6, 48));
		hash.put('6', new Rect(0, 48, 6, 56));
		hash.put('7', new Rect(0, 56, 6, 64));
		hash.put('8', new Rect(0, 64, 6, 72));
		hash.put('9', new Rect(0, 72, 6, 80));

		hash.put('+', new Rect(0, 80, 6, 88));
		hash.put('-', new Rect(0, 88, 6, 96));
		hash.put('%', new Rect(0, 96, 7, 104));
		hash.put('$', new Rect(0, 104, 6, 112));
		hash.put(':', new Rect(0, 112, 6, 120));

		hash.put('A', new Rect(0, 120, 6, 128));
		hash.put('B', new Rect(0, 128, 6, 136));
		hash.put('C', new Rect(0, 136, 6, 144));
		hash.put('D', new Rect(0, 144, 6, 152));
		hash.put('E', new Rect(0, 152, 6, 160));
		hash.put('F', new Rect(0, 160, 6, 168));
		hash.put('G', new Rect(0, 168, 6, 176));
		hash.put('H', new Rect(0, 176, 6, 184));
		hash.put('I', new Rect(0, 184, 6, 192));
		hash.put('J', new Rect(0, 192, 6, 200));
		hash.put('K', new Rect(0, 200, 6, 208));
		hash.put('L', new Rect(0, 208, 6, 216));
		hash.put('M', new Rect(0, 216, 7, 224));
		hash.put('N', new Rect(0, 224, 7, 232));
		hash.put('O', new Rect(0, 232, 6, 240));
		hash.put('P', new Rect(0, 240, 6, 248));
		hash.put('Q', new Rect(0, 248, 6, 256));
		hash.put('R', new Rect(0, 256, 6, 264));
		hash.put('S', new Rect(0, 264, 6, 272));
		hash.put('T', new Rect(0, 272, 6, 280));
		hash.put('U', new Rect(0, 280, 6, 288));
		hash.put('V', new Rect(0, 288, 6, 296));
		hash.put('W', new Rect(0, 296, 7, 304));
		hash.put('X', new Rect(0, 304, 6, 312));
		hash.put('Y', new Rect(0, 312, 6, 320));
		hash.put('Z', new Rect(0, 320, 6, 328));

		hash.put('a', new Rect(0, 120, 6, 128));
		hash.put('b', new Rect(0, 128, 6, 136));
		hash.put('c', new Rect(0, 136, 6, 144));
		hash.put('d', new Rect(0, 144, 6, 152));
		hash.put('e', new Rect(0, 152, 6, 160));
		hash.put('f', new Rect(0, 160, 6, 168));
		hash.put('g', new Rect(0, 168, 6, 176));
		hash.put('h', new Rect(0, 176, 6, 184));
		hash.put('i', new Rect(0, 184, 6, 192));
		hash.put('j', new Rect(0, 192, 6, 200));
		hash.put('k', new Rect(0, 200, 6, 208));
		hash.put('l', new Rect(0, 208, 6, 216));
		hash.put('m', new Rect(0, 216, 7, 224));
		hash.put('n', new Rect(0, 224, 7, 232));
		hash.put('o', new Rect(0, 232, 6, 240));
		hash.put('p', new Rect(0, 240, 6, 248));
		hash.put('q', new Rect(0, 248, 6, 256));
		hash.put('r', new Rect(0, 256, 6, 264));
		hash.put('s', new Rect(0, 264, 6, 272));
		hash.put('t', new Rect(0, 272, 6, 280));
		hash.put('u', new Rect(0, 280, 6, 288));
		hash.put('v', new Rect(0, 288, 6, 296));
		hash.put('w', new Rect(0, 296, 7, 304));
		hash.put('x', new Rect(0, 304, 6, 312));
		hash.put('y', new Rect(0, 312, 6, 320));
		hash.put('z', new Rect(0, 320, 6, 328));
	}

	//////@Override
	public void drawString(Canvas canvas, String aString, int aX, int aY) {
		// TODO Auto-generated method stub
		int length = aString.length();
		int ratingH = size / CURRENT_HEIGHT;

		for (int i = 0; i < length; i++) {
			int cLeft = aX + ratingH * CURRENT_WIDTH * i;
			if (hash.containsKey(aString.charAt(i))) {
				// Rect dst = new Rect(cLeft, aY - ratingH * CURRENT_HEIGHT,
				// (cLeft + ratingH * CURRENT_WIDTH), (aY));
				Rect dst = new Rect(cLeft, aY,
						(cLeft + ratingH * CURRENT_WIDTH), (aY + ratingH
								* CURRENT_HEIGHT));
				Rect src = hash.get(aString.charAt(i));

				canvas.drawBitmap(bmp, src, dst, null);

			} else
				canvas.drawText(Character.toString(aString.charAt(i)), cLeft,
						aY, paint);
		}
	}

	//////@Override
	public void drawString(Canvas canvas, String aString, int aX, int aY,
			int aAnchor) {
		// TODO Auto-generated method stub

	}

	//////@Override
	public int getRowHeight() {
		return size;
	}

	//////@Override
	public int getStringWidth(String aText) {
		// TODO Auto-generated method stub
		return 0;
	}

}
