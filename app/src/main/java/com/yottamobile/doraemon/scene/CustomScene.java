package com.yottamobile.doraemon.scene;

import java.util.ArrayList;
import java.util.Vector;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.ScreenGrabber.IScreenGrabberCallback;
import org.andengine.opengl.util.GLState;

import android.graphics.Bitmap;

import com.yottamobile.doraemon.ICapture;
import com.yottamobile.doraemon.ui.Pika;

public class CustomScene extends Scene{
	
	public IEntity checkOverlap(IEntity entity, float touchX, float touchY) {
		if (isHighestDepth(entity)) {
			return entity;
		}

		ArrayList<IEntity> list = new ArrayList<IEntity>();
		Sprite sprite;
		int length = this.mChildren.size();
		for (int i = 0; i < length; i++) {
			// check if other object is overlapped
			if (this.mChildren.get(i) instanceof Sprite) {
				sprite = (Sprite) this.mChildren.get(i);
				// add sprites which placed in the same touch point area
				if (touchX >= sprite.getX() && touchX <= sprite.getX() + sprite.getWidth() && touchY >= sprite.getY()
						&& touchY <= sprite.getY() + sprite.getHeight()) {
					list.add(sprite);
				}
			}
		}

		// check which one has the highest z index
		if (list.size() > 0) {
			int highestIdx = list.get(0).getZIndex();
			for (int i = 0; i < list.size(); i++) {
				highestIdx = Math.max(highestIdx, list.get(i).getZIndex());
			}
			return getEntityAtZIndex(highestIdx);
		}

		return entity;
	}

	public Vector<Pika> getAllPika() {

		Vector<Pika> vPika = new Vector<Pika>();
		try {
			int length = this.mChildren.size();
			IEntity child;
			for (int i = 0; i < length; i++) {
				child = this.mChildren.get(i);
				if (child instanceof Pika) {
					vPika.add((Pika) child);
				}

			}
		} catch (Exception e) {
			
		}
		return vPika;
	}

	private IEntity getEntityAtZIndex(int highestIdx) {
		IEntity mchild = null;
		IEntity child;
		int length = this.mChildren.size();
		for (int i = 0; i < length; i++) {
			child = this.mChildren.get(i);
			if (child.getZIndex() == highestIdx) {
				mchild = child;
				break;
			}
		}
		return mchild;
	}

	public void setHighestDepth(IEntity entity) {
		if (isHighestDepth(entity)) {
			return;
		}

		// set max index to selected one
		int maxIdx = this.mChildren.size() - 1;
		int selectedIdx = entity.getZIndex();
		entity.setZIndex(maxIdx);

		// decrement index of the sprite which is greater than selected one
		Entity child;
		int length = this.mChildren.size();
		for (int i = 0; i < length; i++) {
			child = (Entity) this.mChildren.get(i);
			if (!child.equals(entity) && child.getZIndex() > selectedIdx) {
				child.setZIndex(child.getZIndex() - 1);
			}
		}
		// update the view
		this.sortChildren();
	}

	private boolean isHighestDepth(IEntity entity) {
		boolean check = true;
		int selectedIdx = entity.getZIndex();
		Entity child;
		int length = this.mChildren.size();

		for (int i = 0; i < length; i++) {
			child = (Entity) this.mChildren.get(i);

			if (!child.equals(entity) && child.getZIndex() > selectedIdx) {
				check = false;

			}
		}

		return check;
	}

}
