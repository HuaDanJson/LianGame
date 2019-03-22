package com.yottamobile.doraemon.scene;

import android.graphics.Bitmap;
import android.view.View;

import com.yottamobile.doraemon.PikachuActivity;
import com.yottamobile.doraemon.R;
import com.yottamobile.doraemon.SceneType;
import com.yottamobile.doraemon.ui.MButton;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;

public class PikaGameScene extends CustomScene implements IMyScene, OnClickListener {

	private BaseGameActivity game;
	public static Bitmap search;
	private Camera mCamera;
	private Sprite gameDetail;
	private Text mDetail;

	@Override
	public Scene onCreateScene(BaseGameActivity game, Camera mCamera) {
		PikachuActivity.getPikachu().detectView(PikachuActivity.SHOW_GAME_SELECT);

		Sprite bg = new Sprite(0, 0, PikachuActivity.getPikachu().background, game.getVertexBufferObjectManager());
		attachChild(bg);

		MButton btnBack = new MButton(9, 400, PikachuActivity.getPikachu().textureBack, game.getVertexBufferObjectManager());

		btnBack.setOnClickListener(this);
		registerTouchArea(btnBack);

		gameDetail = new Sprite(17, 281, PikachuActivity.getPikachu().regionGame_detail, game.getVertexBufferObjectManager());
		mDetail = new Text(54, 22, PikachuActivity.getPikachu().fontTektonProWhite35, game.getString(R.string.classic), 1000, game.getVertexBufferObjectManager());
		//mDetail = new Text(54, 22, PikachuActivity.getPikachu().fontTektonProWhite35, "Classic mode\nAccomplish all pairs of icons within \na limited time.", 1000, game.getVertexBufferObjectManager());

		gameDetail.attachChild(mDetail);
		attachChild(gameDetail);
		attachChild(btnBack);

		final MButton btnGo = new MButton(600, 300, PikachuActivity.getPikachu().textureGo, game.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
					View currentGame = PikachuActivity.getPikachu().currentGame;
					
					if (currentGame.getTag().toString().equals("mission")) {
						PikachuActivity.getPikachu().game_mode = PikachuActivity.GAME_CLASSIC;
						PikachuActivity.getPikachu().detectView(PikachuActivity.VISIBLE_GAME_SELECT);
						PikachuActivity.getPikachu().changeScene(SceneType.LEVEL);
					} else if (currentGame.getTag().toString().equals("top")) {
						PikachuActivity.getPikachu().game_mode = PikachuActivity.GAME_KING;
						PikachuActivity.getPikachu().detectView(PikachuActivity.VISIBLE_GAME_SELECT);
						PikachuActivity.getPikachu().changeScene(SceneType.GAME);
					} else {
						PikachuActivity.getPikachu().detectView(PikachuActivity.SHOW_MORE);
					}
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};

		
		registerTouchArea(btnGo);
		
		ScaleModifier mScale1 = new ScaleModifier(0.25f, 1, 1.1f);
		ScaleModifier mScale2 = new ScaleModifier(0.25f, 1.1f, 1f);
		SequenceEntityModifier sequen = new SequenceEntityModifier(mScale1, mScale2);

		LoopEntityModifier loopScale = new LoopEntityModifier(sequen);
		btnGo.registerEntityModifier(loopScale);
		attachChild(btnGo);
		return this;
	}

	public void setGameDetail(String text) {
		ScaleModifier toBig = new ScaleModifier(0.1f, 1, 1.1f);
		ScaleModifier toSmall = new ScaleModifier(0.1f, 1.1f, 1);
		SequenceEntityModifier sequen = new SequenceEntityModifier(toBig, toSmall);
		gameDetail.registerEntityModifier(sequen);
		mDetail.setText(text);
	}

	@Override
	public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		PikachuActivity.getPikachu().detectView(PikachuActivity.VISIBLE_GAME_SELECT);
		PikachuActivity.getPikachu().changeScene(SceneType.HOME);

	}

	public void destroyed() {
		clearChildScene();
		clearEntityModifiers();
		clearTouchAreas();
		clearUpdateHandlers();
	}

}
