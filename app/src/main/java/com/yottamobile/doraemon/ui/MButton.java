package com.yottamobile.doraemon.ui;

import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class MButton extends ButtonSprite {

	public MButton(float pX, float pY, ITextureRegion pNormalTextureRegion, ITextureRegion pPressedTextureRegion,
			ITextureRegion pDisabledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pNormalTextureRegion, pPressedTextureRegion, pDisabledTextureRegion, pVertexBufferObjectManager);
	}
	
	public MButton(float pX, float pY, ITextureRegion pNormalTextureRegion,VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pNormalTextureRegion, pVertexBufferObjectManager);
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
			ScaleModifier toBig = new ScaleModifier(0.1f, 1, 1.1f);
			ScaleModifier toNormal = new ScaleModifier(0.1f, 1.1f, 1);
			SequenceEntityModifier sequen = new SequenceEntityModifier(toBig, toNormal);
			registerEntityModifier(sequen);						
		}
		return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	}

}
