package com.yottamobile.doraemon.scene;

import android.graphics.Bitmap;

import com.yottamobile.doraemon.PikachuActivity;
import com.yottamobile.doraemon.SceneType;
import com.yottamobile.doraemon.ui.MButton;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.ui.activity.BaseGameActivity;

public class PikaLevelScene extends CustomScene implements IMyScene, OnClickListener {

    private BaseGameActivity game;
    public static Bitmap search;
    private Camera mCamera;

    @Override
    public Scene onCreateScene(BaseGameActivity game, Camera mCamera) {
        PikachuActivity.getPikachu().detectView(PikachuActivity.SHOW_LEVEL_SELECT);
        Sprite bg = new Sprite(0, 0, PikachuActivity.getPikachu().background, game.getVertexBufferObjectManager());
        attachChild(bg);
        Sprite select = new Sprite(88, 13, PikachuActivity.getPikachu().bgLevelselect, game.getVertexBufferObjectManager());
        attachChild(select);
        MButton btnBack = new MButton(14, 14, PikachuActivity.getPikachu().textureBack, game.getVertexBufferObjectManager());
        btnBack.setOnClickListener(this);
        registerTouchArea(btnBack);
        attachChild(btnBack);
        return this;
    }

    @Override
    public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        PikachuActivity.getPikachu().detectView(PikachuActivity.VISIBLE_LEVEL_SELECT);
        PikachuActivity.getPikachu().changeScene(SceneType.SELECT_GAME);
    }

    public void destroyed() {
        clearChildScene();
        clearEntityModifiers();
        clearTouchAreas();
        clearUpdateHandlers();
    }

}
