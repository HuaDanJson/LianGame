package com.yottamobile.doraemon.scene;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import com.yottamobile.doraemon.Pikachu;
import com.yottamobile.doraemon.R;
import com.yottamobile.doraemon.SceneType;
import com.yottamobile.doraemon.ui.MButton;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;

public class PikaLevelScene extends CustomScene implements IMyScene, OnClickListener {

	private BaseGameActivity game;
	public static Bitmap search;
	private Camera mCamera;

	public Scene onCreateScene(BaseGameActivity game, Camera mCamera) {

		
		Pikachu.getPikachu().detectView(Pikachu.SHOW_LEVEL_SELECT);

		Sprite bg = new Sprite(0, 0, Pikachu.getPikachu().background, game.getVertexBufferObjectManager());
		attachChild(bg);
		Sprite select = new Sprite(88, 13, Pikachu.getPikachu().		bgLevelselect, game.getVertexBufferObjectManager());
		attachChild(select);

		// Sprite bgLevel = new Sprite(14, 23, Pikachu.getPikachu().bgLevelGrid,
		// game.getVertexBufferObjectManager());
		// attachChild(bgLevel);

		MButton btnBack = new MButton(14, 14, Pikachu.getPikachu().textureBack, game.getVertexBufferObjectManager());

		btnBack.setOnClickListener(this);
		registerTouchArea(btnBack);
		attachChild(btnBack);

		// ButtonSprite btnPause = new ButtonSprite(14, 14, textureBack,
		// getVertexBufferObjectManager()){
		// @Override
		// public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float
		// pTouchAreaLocalX, float pTouchAreaLocalY) {
		// if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
		// ScaleModifier toBig = new ScaleModifier(0.1f, 1, 1.1f);
		// ScaleModifier toNormal = new ScaleModifier(0.1f, 1.1f, 1);
		// SequenceEntityModifier sequen = new SequenceEntityModifier(toBig,
		// toNormal);
		// registerEntityModifier(sequen);
		// }
		// return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX,
		// pTouchAreaLocalY);
		// }
		// };
		//
		// mScene.setOnSceneTouchListener(this);
		// mScene.registerTouchArea(btnBack);
		// mScene.attachChild(btnBack);
		//
		return this;
	}

	public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		Pikachu.getPikachu().detectView(Pikachu.VISIBLE_LEVEL_SELECT);
		Pikachu.getPikachu().changeScene(SceneType.SELECT_GAME);
	}

	public void destroyed() {
		clearChildScene();
		clearEntityModifiers();
		clearTouchAreas();
		clearUpdateHandlers();
	}

}
