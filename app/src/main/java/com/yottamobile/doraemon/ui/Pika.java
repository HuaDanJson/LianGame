package com.yottamobile.doraemon.ui;

import java.util.Vector;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;

import com.yottamobile.doraemon.Pikachu;

public class Pika extends Sprite {

	public Pika pikaMark;
	public boolean isHidden = false;
	public boolean addIce = false;
	Sprite sHidden;
	public Vector<IEntityModifier> vModifier = new Vector<IEntityModifier>();

	public Pika(float pX, float pY, float pWidth, float pHeight, ITextureRegion pTextureRegion, ISpriteVertexBufferObject pSpriteVertexBufferObject) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pSpriteVertexBufferObject);
		// TODO Auto-generated constructor stub
	}

	public Pika(int pX, int pY, TextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
		super(pX, pY, textureRegion, vertexBufferObjectManager);
	}

	public Pika(int pX, int pY, int pIKA_SIZE, int pIKA_SIZE2, TextureRegion textureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
		super(pX, pY, pIKA_SIZE, pIKA_SIZE2, textureRegion, vertexBufferObjectManager);
	}

	public Pika(float pX, float pY, int pIKA_SIZE, int pIKA_SIZE2, TextureRegion pTextureRegion, VertexBufferObjectManager vertexBufferObjectManager) {
		super(pX, pY, pIKA_SIZE, pIKA_SIZE2, pTextureRegion, vertexBufferObjectManager);
	}

	public boolean isMark = false;
	protected int TIME_FLIP_UP = 10;
	

	public void flipUp() {
		if (sHidden != null && sHidden.isVisible()) {
	
			ScaleModifier toMin = new ScaleModifier(0.1f, 1, 0, 1, 1) {
				protected void onModifierFinished(IEntity pItem) {
					sHidden.setVisible(false);

					super.onModifierFinished(pItem);

				};
			};

			ScaleModifier toMax = new ScaleModifier(0.1f, 0, 1, 1, 1);

			SequenceEntityModifier sequen = new SequenceEntityModifier(toMin, toMax);
			registerEntityModifier(sequen);
		}

	}

	@Override
	public void clearEntityModifiers() {
		// TODO Auto-generated method stub
		super.clearEntityModifiers();
		if (isHidden && !sHidden.isVisible())
			flipDown();
	}

	public void flipDown() {
		DelayModifier delay = new DelayModifier(TIME_FLIP_UP);
		ScaleModifier toMin1 = new ScaleModifier(0.1f, 1, 0, 1, 1) {
			protected void onModifierFinished(IEntity pItem) {
				sHidden.setVisible(true);
				super.onModifierFinished(pItem);
			};
		};
		ScaleModifier toMax1 = new ScaleModifier(0.1f, 0, 1, 1, 1) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				super.onModifierFinished(pItem);
			}
		};

		SequenceEntityModifier sequen = new SequenceEntityModifier(delay, toMin1, toMax1);
		registerEntityModifier(sequen);

	}

	public void setHidden(Sprite sHidden) {
		if (this.sHidden == null) {
			isHidden = true;
			this.sHidden = sHidden;
			attachChild(sHidden);

		}
	}

	public void setMark(boolean isMark) {
		this.isMark = isMark;
	}

	public boolean isMark() {
		return isMark;
	}

	public void addModifiertoSequen(IEntityModifier entity) {
		vModifier.add(entity);
	}

	public void startSequen() {
		if (vModifier.size() >= 2) {
			IEntityModifier[] listM = new IEntityModifier[vModifier.size()];
			for (int i = 0; i < listM.length; i++)
				listM[i] = vModifier.get(i);
			SequenceEntityModifier sequen = new SequenceEntityModifier(listM);
			registerEntityModifier(sequen);
		} else if (vModifier.size() == 1) {
			registerEntityModifier(vModifier.get(0));
		}
		vModifier.removeAllElements();
	}

}
