package com.yottamobile.doraemon.ui;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.yottamobile.doraemon.Pikachu;

import android.opengl.GLES20;

public class ProgressBar extends HUD {
	private final Sprite mBackgroundRectangle;
	private final Sprite mProgressRectangle;

	private final float mPixelsPerPercentRatio;
	private float percent = 100;

	public ProgressBar(final Camera pCamera, final float pX, final float pY, final TextureRegion background, final TextureRegion process,
			final VertexBufferObjectManager ver) {
		super();
		super.setCamera(pCamera);

		this.mBackgroundRectangle = new Sprite(pX, pY, background, ver);

		this.mProgressRectangle = new Sprite(pX, pY, process, ver) {
			@Override
			protected void onManagedDraw(GLState glState, Camera pCamera) {
				GLES20.glClearColor(1, 0, 0, 1);
				glState.pushProjectionGLMatrix();
				glState.enableScissorTest();
				
				float clipX = 0;
				float clipY = 0;
				float clipW =  (percent * getWidth()) / 100;
				float clipH = getHeight();

//				System.out.println(Pikachu.pio.);
				
				GLES20.glScissor((int)((getX() + clipX)*Pikachu.scaleX), (int) ((pCamera.getHeight() - ((getY() + clipY) + clipH))*Pikachu.scaleY), (int)(clipW*Pikachu.scaleX), (int)(clipH*Pikachu.scaleY));//

				super.onManagedDraw(glState, pCamera);

				glState.disableScissorTest();
				glState.popProjectionGLMatrix();
			}
		};

		super.attachChild(this.mBackgroundRectangle);
		super.attachChild(this.mProgressRectangle);

		this.mPixelsPerPercentRatio = mProgressRectangle.getWidth() / 100;
	}

	public void setProgress(final float pProgress) {
		if (pProgress < 0)
			this.mProgressRectangle.setWidth(0);
		this.percent = pProgress;
		// this.mProgressRectangle.setWidth(this.mPixelsPerPercentRatio *
		// pProgress);
	}

	public float getPercent() {
		return percent;
	}

}
