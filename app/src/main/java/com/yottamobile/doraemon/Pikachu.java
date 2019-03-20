package com.yottamobile.doraemon;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.GLES20;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.flurry.android.FlurryAgent;
import com.newqm.sdkoffer.AdView;
import com.newqm.sdkoffer.QuMiConnect;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.platformtools.Util;
import com.yottamobile.doraemon.adapter.ImageAdapter;
import com.yottamobile.doraemon.data.PikaLevelData;
import com.yottamobile.doraemon.data.PikaSaveGamer;
import com.yottamobile.doraemon.scene.PikaGameScene;
import com.yottamobile.doraemon.scene.PikaHomeScene;
import com.yottamobile.doraemon.scene.PikaLevelScene;
import com.yottamobile.doraemon.scene.PikaPlayScene;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.BitmapFont;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.StrokeFont;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.SimpleLayoutGameActivity;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.Random;
import java.util.Vector;

import cn.bmob.v3.Bmob;

public class Pikachu extends SimpleLayoutGameActivity implements
        IOnSceneTouchListener, ICapture {

    public static int CAMERA_WIDTH = 800;
    public static int CAMERA_HEIGHT = 480;
    private Camera mCamera;
    public static int LVL = 10;
    public static int LVL_MAX = 60;
    public static float scaleX, scaleY;

    public static String BTN_SOUND_BG = "SOUND_BG";
    public static String BTN_SOUND_GAME = "SOUND_GAME";
    public static boolean soundBG = true;
    public static boolean soundGame = true;

    static Pikachu mPikachu;
    public SceneType currentScene = SceneType.SPLASH;

    private static Music musicBG;
    public static TextureRegion[] regionSoundBG, regionSoundGame;

    public TextureRegion background, textureBack, texturePause, textureFish,
            textureNext, textureRetry, textureGo, regionStar0, regionStar1;
    public TextureRegion regionBanner, bgLevelselect, regionTopGameBG,
            regionComboText, regionBangDiem, regionIconInfo,
            regionIconQuestion;

    public TextureRegion[] texturePikachu, random, boom, lockTime;
    public TextureRegion textFire, ball, regionGame_detail, regionFinish,
            regionGameOver, regionMission, regionShareFace, wave;
    public TiledTextureRegion tiledEffectBom, tiledEffectFire,
            tiledEffectCombo, tiledEffectIce, tiledEffectWater;
    public TextureRegion textureItemBoard, bgBarTimeProcess, bgBarComboProcess,
            textureLevelIcon, textureStop, bgBarTime, bgBarCombo, textCombo;
    public Font fontTektonProWhite80, fontTektonProScore20,
            fontTektonProWhite35, fontTektonProRedBoom;
    public BitmapFont font;
    public Sound soundPoint, soundLose, soundStar;
    public static Vector<PikaLevelData> vData;
    public static float WIN_DOW_W;
    public static float WIN_DOW_H;
    public static int HIGH_SCORE = 0;
    public static boolean showTutorial = true;
    public StrokeFont mStrokeFont, fontTektonProRed, mStrokeFontYellow;
    public final static int SHOW_GAME_SELECT = 0;
    public final static int VISIBLE_GAME_SELECT = 1;
    public final static int SHOW_LEVEL_SELECT = 2;
    public final static int VISIBLE_LEVEL_SELECT = 3;
    public final static int VISIBLE_ADS = 4;
    public final static int SHOW_ADS = 5;
    public final static int SHOW_INT = 6;
    public final static int SHOW_MORE = 7;
    public int game_mode;
    public TextureRegion mParticleTextureRegion, regionPlaynow;
    public final static int GAME_CLASSIC = 0;
    public final static int GAME_KING = 1;
    public static final int SOUND_POINT = 0;
    public static final int SOUND_LOSE = 2;
    public static final int SOUND_STAR = 3;
    private AdView adView;

    // private Scene splashScene;
    private TextureRegion splashTextureRegion;
    private Sprite splash;
    private boolean isInitialzed = false;

    private LinearLayout gameMission, gameTop, gameMore;

    public static Pikachu getPikachu() {
        return mPikachu;
    }

    public RenderSurfaceView getRenderSurfaceView() {
        return mRenderSurfaceView;
    }

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        Bmob.initialize(this, "f234cdf0e315906726f19e377fced71e");
        QuMiConnect.ConnectQuMi(this, "3f51dab249117da5", "4a3988ef46a0e5d3");
        QuMiConnect.getQumiConnectInstance(this).initPopAd(this);
        String app_id = getString(R.string.app_id);
        api = WXAPIFactory.createWXAPI(this, app_id);
        api.registerApp(app_id);
    }

    public void playSound(int soundID) {
        if (isSoundGame()) {
            switch (soundID) {
                case SOUND_POINT:
                    soundPoint.play();
                    break;
                case SOUND_LOSE:
                    soundLose.play();
                    break;
                case SOUND_STAR:
                    soundStar.play();
                    break;

                default:
                    break;
            }
        }
    }

    public EngineOptions onCreateEngineOptions() {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        WIN_DOW_W = metrics.widthPixels;
        WIN_DOW_H = metrics.heightPixels;
        scaleX = (float) metrics.widthPixels / (float) CAMERA_WIDTH;
        scaleY = (float) metrics.heightPixels / (float) CAMERA_HEIGHT;

        // System.out.println(scaleX + " - " + scaleY);

        this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        // EngineOptions engine = new EngineOptions(true,
        // ScreenOrientation.LANDSCAPE_FIXED, new
        // RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
        EngineOptions engine = new EngineOptions(true,
                ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
                this.mCamera);

        engine.getAudioOptions().setNeedsMusic(true);

        engine.getAudioOptions().setNeedsSound(true);
        engine.getTouchOptions().setNeedsMultiTouch(true);
        return engine;
    }

    public static boolean isSoundBG() {
        return soundBG;

    }

    public static boolean isSoundGame() {
        return soundGame;
    }

    public static void setSoundBG(boolean soundBG) {
        Pikachu.soundBG = soundBG;
        if (soundBG) {
            musicBG.play();
        } else {
            musicBG.pause();
        }
        PikaSaveGamer.saveSoundGame(PikaSaveGamer.KEY_SOUND_BG, soundBG);
    }

    public static void setSoundGame(boolean soundGame) {
        Pikachu.soundGame = soundGame;
        PikaSaveGamer.saveSoundGame(PikaSaveGamer.KEY_SOUND_GAME, soundGame);
    }

    public static TextureRegion getTextureRegionSoundBG() {
        if (soundBG)
            return regionSoundBG[1];
        else
            return regionSoundBG[0];
    }

    public static TextureRegion getTextureRegionSoundGame() {
        if (soundGame)
            return regionSoundGame[1];
        else
            return regionSoundGame[0];
    }

    @Override
    protected void onCreateResources() {

        BitmapTextureAtlas splashTextureAtlas = new BitmapTextureAtlas(
                this.getTextureManager(), 163, 215);
        splashTextureRegion = BitmapTextureAtlasTextureRegionFactory
                .createFromAsset(splashTextureAtlas, this,
                        "background/splash.png", 0, 0);
        splashTextureAtlas.load();

        /*
         * try { musicBG = MusicFactory.createMusicFromAsset(getMusicManager(),
         * this, "sound/bg.mp3"); } catch (IOException e) { // TODO
         * Auto-generated catch block e.printStackTrace(); }
         * musicBG.setLooping(true); musicBG.setVolume(0.1f);
         */

        currentScene = SceneType.SPLASH;

        mEngine.registerUpdateHandler(new TimerHandler(1f,
                new ITimerCallback() {
                    public void onTimePassed(final TimerHandler pTimerHandler) {
                        mEngine.unregisterUpdateHandler(pTimerHandler);
                        loadResources();
                        // loadScenes();
                        splash.detachSelf();
                        currentScene = SceneType.HOME;

                        // TODO moved from onCreateScene
                        setSoundBG(PikaSaveGamer
                                .getSound(PikaSaveGamer.KEY_SOUND_BG));
                        setSoundGame(PikaSaveGamer
                                .getSound(PikaSaveGamer.KEY_SOUND_GAME));
                        if (isSoundBG())
                            musicBG.play();

                        isInitialzed = true;

                        mEngine.setScene(switchScene(currentScene));
                    }
                }));
    }

    protected void loadResources() {

        try {

            BitmapTextureAtlas wave = new BitmapTextureAtlas(
                    getTextureManager(), 1024, 1024);
            this.wave = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                    wave, getApplicationContext(), "effect/wave.png", 0, 0);
            wave.load();

            BitmapTextureAtlas regionShareFace = new BitmapTextureAtlas(
                    this.getTextureManager(), 128, 128);
            this.regionShareFace = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(regionShareFace, this,
                            "background/share_fgb.png", 0, 0);
            regionShareFace.load();

            BitmapTextureAtlas regionFinish = new BitmapTextureAtlas(
                    this.getTextureManager(), 512, 128);
            this.regionFinish = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(regionFinish, this,
                            "background/finish.png", 0, 0);
            regionFinish.load();

            BitmapTextureAtlas regionGameOver = new BitmapTextureAtlas(
                    this.getTextureManager(), 512, 128);
            this.regionGameOver = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(regionGameOver, this,
                            "background/gameover.png", 0, 0);
            regionGameOver.load();

            BitmapTextureAtlas regionMission = new BitmapTextureAtlas(
                    this.getTextureManager(), 512, 128);
            this.regionMission = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(regionMission, this,
                            "background/mission.png", 0, 0);
            regionMission.load();

            BitmapTextureAtlas fontStroke = new BitmapTextureAtlas(
                    getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
            mStrokeFont = FontFactory.createStrokeFromAsset(getFontManager(),
                    fontStroke, getAssets(), "font.ttf", 80, true,
                    android.graphics.Color.WHITE, 3,
                    android.graphics.Color.BLACK, false);
            mStrokeFont.load();

            BitmapTextureAtlas mStrokeFontYello = new BitmapTextureAtlas(
                    getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
            this.mStrokeFontYellow = FontFactory.createStrokeFromAsset(
                    getFontManager(), mStrokeFontYello, getAssets(),
                    "font.ttf", 80, true,
                    android.graphics.Color.parseColor("#ffef38"), 3,
                    android.graphics.Color.parseColor("#003a50"), false);
            this.mStrokeFontYellow.load();

            BitmapTextureAtlas fontTextureAtlasRed = new BitmapTextureAtlas(
                    getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
            fontTektonProRed = FontFactory.createStrokeFromAsset(
                    getFontManager(), fontTextureAtlasRed, getAssets(),
                    "font.ttf", 75, true, android.graphics.Color.WHITE, 3,
                    android.graphics.Color.BLACK, false);
            fontTektonProRed.load();

            BitmapTextureAtlas mBitmapPlaynow = new BitmapTextureAtlas(
                    this.getTextureManager(), 512, 128);
            regionPlaynow = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(mBitmapPlaynow, this,
                            "background/play_now.png", 0, 0);
            mBitmapPlaynow.load();

            BitmapTextureAtlas mBitmapTextureAtlas1 = new BitmapTextureAtlas(
                    this.getTextureManager(), 32, 32,
                    TextureOptions.BILINEAR_PREMULTIPLYALPHA);
            mParticleTextureRegion = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(mBitmapTextureAtlas1, this,
                            "effect/particle_point.png", 0, 0);

            mBitmapTextureAtlas1.load();

            BitmapTextureAtlas atlasGameStar = new BitmapTextureAtlas(
                    getTextureManager(), 256, 128);
            regionStar0 = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasGameStar, getApplicationContext(),
                            "background/star_0.png", 0, 0);
            regionStar1 = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasGameStar, getApplicationContext(),
                            "background/star_1.png", 64, 0);
            atlasGameStar.load();

            BitmapTextureAtlas atlasGameDetail = new BitmapTextureAtlas(
                    getTextureManager(), 1024, 256);
            regionGame_detail = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasGameDetail, getApplicationContext(),
                            "background/game_detail.png", 0, 0);
            atlasGameDetail.load();

            BitmapTextureAtlas atlasInfor = new BitmapTextureAtlas(
                    getTextureManager(), 128, 128);
            regionIconInfo = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasInfor, getApplicationContext(),
                            "background/icon_information.png", 0, 0);
            atlasInfor.load();

            BitmapTextureAtlas atlasQuestion = new BitmapTextureAtlas(
                    getTextureManager(), 128, 128);
            regionIconQuestion = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasQuestion, getApplicationContext(),
                            "background/icon_question.png", 0, 0);
            atlasQuestion.load();

            BitmapTextureAtlas atlasBangDiem = new BitmapTextureAtlas(
                    getTextureManager(), 1208, 512);
            regionBangDiem = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasBangDiem, getApplicationContext(),
                            "background/bangdiem.png", 0, 0);
            atlasBangDiem.load();

            BitmapTextureAtlas atlasComboText = new BitmapTextureAtlas(
                    getTextureManager(), 128, 32);
            regionComboText = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasComboText, getApplicationContext(),
                            "effect/combo_text.png", 0, 0);
            atlasComboText.load();

            BitmapTextureAtlas atlasTopGameBG = new BitmapTextureAtlas(
                    getTextureManager(), 1024, 256);
            regionTopGameBG = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasTopGameBG, getApplicationContext(),
                            "background/bg_top_game_play.png", 0, 0);
            atlasTopGameBG.load();

            BitmapTextureAtlas atlasBanner = new BitmapTextureAtlas(
                    getTextureManager(), 1024, 256);
            regionBanner = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasBanner, getApplicationContext(),
                            "background/banner.png", 0, 0);
            atlasBanner.load();

            BitmapTextureAtlas atlasFish = new BitmapTextureAtlas(
                    getTextureManager(), 256, 256);
            textureFish = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasFish, getApplicationContext(),
                            "background/fish.png", 0, 0);
            atlasFish.load();

            BitmapTextureAtlas atlasBoard = new BitmapTextureAtlas(
                    getTextureManager(), 128, 512);
            textureItemBoard = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasBoard, getApplicationContext(),
                            "background/item_board.png", 0, 0);
            atlasBoard.load();

            BitmapTextureAtlas fontTextureAtlas2 = new BitmapTextureAtlas(
                    getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
            fontTektonProWhite35 = FontFactory.createFromAsset(
                    getFontManager(), fontTextureAtlas2, getAssets(),
                    "TektonPro.otf", 35, true, android.graphics.Color.WHITE);
            fontTektonProWhite35.load();

            BitmapTextureAtlas fontTektonProRedBoom = new BitmapTextureAtlas(
                    getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
            this.fontTektonProRedBoom = FontFactory.createFromAsset(
                    getFontManager(), fontTektonProRedBoom, getAssets(),
                    "TektonPro.otf", 50, true, android.graphics.Color.RED);
            this.fontTektonProRedBoom.load();

            BitmapTextureAtlas fontTextureAtlas = new BitmapTextureAtlas(
                    getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
            fontTektonProWhite80 = FontFactory.createFromAsset(
                    getFontManager(), fontTextureAtlas, getAssets(),
                    "TektonPro.otf", 80, true, android.graphics.Color.WHITE);
            fontTektonProWhite80.load();

            BitmapTextureAtlas fontTextureAtlas1 = new BitmapTextureAtlas(
                    getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
            fontTektonProScore20 = FontFactory.createFromAsset(
                    getFontManager(), fontTextureAtlas1, getAssets(),
                    "font.ttf", 40, true,
                    android.graphics.Color.parseColor("#ff4e00"));
            fontTektonProScore20.load();

            BitmapTextureAtlas atlasLevelIcon = new BitmapTextureAtlas(
                    getTextureManager(), 128, 128);
            textureLevelIcon = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasLevelIcon, getApplicationContext(),
                            "background/level_icon.png", 0, 0);
            atlasLevelIcon.load();

            BitmapTextureAtlas atlasStop = new BitmapTextureAtlas(
                    getTextureManager(), 128, 128);
            textureStop = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasStop, getApplicationContext(),
                            "background/stop.png", 0, 0);
            atlasStop.load();

            BitmapTextureAtlas effIce = new BitmapTextureAtlas(
                    getTextureManager(), 1024, 128);
            tiledEffectIce = BitmapTextureAtlasTextureRegionFactory
                    .createTiledFromAsset(effIce, getAssets(),
                            "effect/ice.png", 0, 0, 6, 1);
            effIce.load();

            BitmapTextureAtlas effWater = new BitmapTextureAtlas(
                    getTextureManager(), 1024, 128);
            tiledEffectWater = BitmapTextureAtlasTextureRegionFactory
                    .createTiledFromAsset(effWater, getAssets(),
                            "effect/frezee_water.png", 0, 0, 6, 1);
            effWater.load();

            lockTime = new TextureRegion[2];
            BitmapTextureAtlas atlasLockTime = new BitmapTextureAtlas(
                    getTextureManager(), 256, 256);
            lockTime[0] = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasLockTime, getApplicationContext(),
                            "background/item_clock_0.png", 0, 0);
            lockTime[1] = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasLockTime, getApplicationContext(),
                            "background/item_clock_1.png", 128, 128);
            atlasLockTime.load();

            BitmapTextureAtlas atlasBack = new BitmapTextureAtlas(
                    getTextureManager(), 128, 128);
            textureBack = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasBack, getApplicationContext(),
                            "background/back.png", 0, 0);
            atlasBack.load();

            BitmapTextureAtlas atlasNext = new BitmapTextureAtlas(
                    getTextureManager(), 95, 95);
            textureNext = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasNext, getApplicationContext(),
                            "background/next.png", 0, 0);
            atlasNext.load();

            BitmapTextureAtlas atlasRetry = new BitmapTextureAtlas(
                    getTextureManager(), 128, 128);
            textureRetry = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasRetry, getApplicationContext(),
                            "background/retry.png", 0, 0);
            atlasRetry.load();

            BitmapTextureAtlas textureGo = new BitmapTextureAtlas(
                    getTextureManager(), 256, 256);
            this.textureGo = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(textureGo, getApplicationContext(),
                            "background/play.png", 0, 0);
            textureGo.load();

            BitmapTextureAtlas mBitmapTextureAtlas = new BitmapTextureAtlas(
                    getTextureManager(), 128, 32);
            textCombo = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                    mBitmapTextureAtlas, this, "background/combo.png", 0, 0);
            mBitmapTextureAtlas.load();

            font = new BitmapFont(getTextureManager(), getAssets(),
                    "number.fnt");
            font.load();
            BitmapTextureAtlas atlasProBgCombo = new BitmapTextureAtlas(
                    getTextureManager(), 256, 64);
            bgBarCombo = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasProBgCombo, getApplicationContext(),
                            "background/combo3.png", 0, 0);

            bgBarComboProcess = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasProBgCombo, getApplicationContext(),
                            "background/combo2.png", 0, 35);
            atlasProBgCombo.load();

            BitmapTextureAtlas atlasProBg = new BitmapTextureAtlas(
                    getTextureManager(), 1024, 64);
            bgBarTime = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                    atlasProBg, getApplicationContext(), "background/bar2.png",
                    0, 0);

            bgBarTimeProcess = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasProBg, getApplicationContext(),
                            "background/bar.png", 0, 0);
            atlasProBg.load();

            BitmapTextureAtlas effCombo = new BitmapTextureAtlas(
                    getTextureManager(), 1024, 512);
            tiledEffectCombo = BitmapTextureAtlasTextureRegionFactory
                    .createTiledFromAsset(effCombo, getAssets(),
                            "effect/combo.png", 0, 0, 5, 4);
            effCombo.load();

            BitmapTextureAtlas effFire = new BitmapTextureAtlas(
                    getTextureManager(), 512, 128);
            tiledEffectFire = BitmapTextureAtlasTextureRegionFactory
                    .createTiledFromAsset(effFire, getAssets(),
                            "effect/fire.png", 0, 0, 6, 1);
            effFire.load();

            BitmapTextureAtlas effBom = new BitmapTextureAtlas(
                    getTextureManager(), 512, 256);
            tiledEffectBom = BitmapTextureAtlasTextureRegionFactory
                    .createTiledFromAsset(effBom, getAssets(),
                            "effect/bom.png", 0, 0, 4, 1);
            effBom.load();

            soundPoint = SoundFactory.createSoundFromAsset(getEngine()
                    .getSoundManager(), this, "sound/point.mp3");
            soundPoint.setVolume(1f);

            soundLose = SoundFactory.createSoundFromAsset(getEngine()
                    .getSoundManager(), this, "sound/mission_failure.mp3");
            soundLose.setVolume(1f);

            soundStar = SoundFactory.createSoundFromAsset(getEngine()
                    .getSoundManager(), this, "sound/star.mp3");
            soundStar.setVolume(1f);

            BitmapTextureAtlas pBuildableBitmapTextureAtlas = new BitmapTextureAtlas(
                    getTextureManager(), 1024, 1024);
            texturePikachu = new TextureRegion[PikaPlayScene.PIKA_LENGH];

            int row = 0;
            int col = 0;
            int iconSize = 89;
            for (int i = 0; i < texturePikachu.length; i++) {
                texturePikachu[i] = BitmapTextureAtlasTextureRegionFactory
                        .createFromAsset(pBuildableBitmapTextureAtlas,
                                getApplicationContext(), "pikachu/" + (i + 1)
                                        + ".png", col * iconSize, row
                                        * iconSize);
                col++;
                if (col > 4) {
                    col = 0;
                    row++;
                }
            }

            pBuildableBitmapTextureAtlas.load();

            BitmapTextureAtlas atlasBg = new BitmapTextureAtlas(
                    getTextureManager(), 1024, 1024);
            background = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasBg, getApplicationContext(),
                            "background/bg.jpg", 0, 0);
            atlasBg.load();

            random = new TextureRegion[2];
            BitmapTextureAtlas atlasPause = new BitmapTextureAtlas(
                    getTextureManager(), 256, 256);
            random[0] = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                    atlasPause, getApplicationContext(),
                    "background/item_random_0.png", 0, 0);
            random[1] = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                    atlasPause, getApplicationContext(),
                    "background/item_random_1.png", 128, 128);
            atlasPause.load();

            boom = new TextureRegion[2];
            BitmapTextureAtlas atlasBoom = new BitmapTextureAtlas(
                    getTextureManager(), 256, 256);
            boom[0] = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                    atlasBoom, getApplicationContext(),
                    "background/item_boom_0.png", 0, 0);
            boom[1] = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                    atlasBoom, getApplicationContext(),
                    "background/item_boom_1.png", 128, 128);
            atlasBoom.load();

            BitmapTextureAtlas atlasFire = new BitmapTextureAtlas(
                    getTextureManager(), 128, 128);
            textFire = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                    atlasFire, getApplicationContext(), "background/fire.png",
                    0, 0);
            atlasFire.load();

            BitmapTextureAtlas atlasBall = new BitmapTextureAtlas(
                    getTextureManager(), 128, 128);
            ball = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                    atlasBall, getApplicationContext(), "background/ball.png",
                    0, 0);
            atlasBall.load();

            regionSoundBG = new TextureRegion[2];
            BitmapTextureAtlas atlasSoundBG0 = new BitmapTextureAtlas(
                    getTextureManager(), 128, 128);
            regionSoundBG[0] = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasSoundBG0, getApplicationContext(),
                            "background/sound_bg_0.png", 0, 0);
            atlasSoundBG0.load();

            BitmapTextureAtlas atlasSoundBG1 = new BitmapTextureAtlas(
                    getTextureManager(), 128, 128);
            regionSoundBG[1] = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasSoundBG1, getApplicationContext(),
                            "background/sound_bg_1.png", 0, 0);
            atlasSoundBG1.load();

            regionSoundGame = new TextureRegion[2];

            BitmapTextureAtlas atlasSoundGame0 = new BitmapTextureAtlas(
                    getTextureManager(), 128, 128);
            regionSoundGame[0] = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasSoundGame0, getApplicationContext(),
                            "background/sound_game_0.png", 0, 0);
            atlasSoundGame0.load();

            BitmapTextureAtlas atlasSoundGame1 = new BitmapTextureAtlas(
                    getTextureManager(), 128, 128);
            regionSoundGame[1] = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasSoundGame1, getApplicationContext(),
                            "background/sound_game_1.png", 0, 0);
            atlasSoundGame1.load();

            musicBG = MusicFactory.createMusicFromAsset(getMusicManager(),
                    this, "sound/bg.mp3");
            musicBG.setLooping(true);
            musicBG.setVolume(0.1f);

            BitmapTextureAtlas atlasBgLevel = new BitmapTextureAtlas(
                    getTextureManager(), 1024, 64);
            bgLevelselect = BitmapTextureAtlasTextureRegionFactory
                    .createFromAsset(atlasBgLevel, getApplicationContext(),
                            "background/select_game.png", 0, 0);
            atlasBgLevel.load();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        currentScene = SceneType.HOME;

    }

    public void detectView(final int view) {
        new Thread() {
            public void run() {
                Message msg = new Message();
                msg.arg1 = view;
                mHandler.sendMessage(msg);
            }

            ;
        }.start();
    }

    @Override
    public Scene onCreateScene() {
        mPikachu = this;
        vData = PikaSaveGamer.getDataLevel();
        HIGH_SCORE = PikaSaveGamer.getHighScore();
        showTutorial = PikaSaveGamer.getSound(PikaSaveGamer.KEY_TUTORIAL);

        /*
         * setSoundBG(PikaSaveGamer.getSound(PikaSaveGamer.KEY_SOUND_BG));
         * setSoundGame(PikaSaveGamer.getSound(PikaSaveGamer.KEY_SOUND_GAME));
         * if (isSoundBG()) musicBG.play();
         */

        return switchScene(currentScene);
    }

    public void editLevelData(int level, int state) {
        if (level < vData.size()) {
            PikaLevelData levelData = vData.get(level);
            if (levelData.state < state) {
                levelData.state = state;
            }
            PikaSaveGamer.saveDataLevel(vData);
        }
    }

    @Override
    public synchronized void onPauseGame() {

        if (mEngine.getScene() instanceof PikaPlayScene) {
            ((PikaPlayScene) mEngine.getScene()).showPopUpPause();
        }

        if (isInitialzed) {
            musicBG.pause();
        }
        super.onPauseGame();
    }

    @Override
    public synchronized void onResumeGame() {
        if ((isInitialzed) && (isSoundBG())) {
            musicBG.play();
        }
        super.onResumeGame();
    }

    public View currentGame;

    public void selectGame(View v) {
        if (currentGame == v) {
            if (currentGame.getTag().toString().equals("mission")) {
                Pikachu.getPikachu().game_mode = Pikachu.GAME_CLASSIC;
                Pikachu.getPikachu().detectView(Pikachu.VISIBLE_GAME_SELECT);
                Pikachu.getPikachu().changeScene(SceneType.LEVEL);
            } else if (currentGame.getTag().toString().equals("top")) {
                Pikachu.getPikachu().game_mode = Pikachu.GAME_KING;
                Pikachu.getPikachu().detectView(Pikachu.VISIBLE_GAME_SELECT);
                Pikachu.getPikachu().changeScene(SceneType.GAME);
            } else {
                Pikachu.getPikachu().detectView(Pikachu.SHOW_MORE);
            }
        } else {
            gameMission.setBackgroundColor(android.graphics.Color.TRANSPARENT);
            gameTop.setBackgroundColor(android.graphics.Color.TRANSPARENT);
            gameMore.setBackgroundColor(android.graphics.Color.TRANSPARENT);
            v.setBackgroundResource(R.drawable.hover);
            currentGame = v;

            if (currentGame.getTag().toString().equals("mission")) {

                ((PikaGameScene) mEngine.getScene())
                        .setGameDetail(getString(R.string.classic));
            } else if (currentGame.getTag().toString().equals("top")) {
                ((PikaGameScene) mEngine.getScene())
                        .setGameDetail(getString(R.string.crazy));
            } else {
                ((PikaGameScene) mEngine.getScene())
                        .setGameDetail(getString(R.string.more));
            }
        }
    }

    public Scene switchScene(SceneType scene) {
        currentScene = scene;
        Scene mScene = null;

        if (scene == SceneType.GAME) {
            Pikachu.getPikachu().detectView(Pikachu.VISIBLE_ADS);
            PikaPlayScene playScene = new PikaPlayScene();
            mScene = playScene.onCreateScene(this, mCamera);
        } else if (scene == SceneType.LEVEL) {
            Pikachu.getPikachu().detectView(Pikachu.SHOW_ADS);
            PikaLevelScene levelScene = new PikaLevelScene();
            mScene = levelScene.onCreateScene(this, mCamera);
        } else if (scene == SceneType.HOME) {
            if (adView !=
                    null) Pikachu.getPikachu().detectView(Pikachu.SHOW_ADS);
            Pikachu.getPikachu().detectView(Pikachu.VISIBLE_ADS);
            PikaHomeScene homeScene = new PikaHomeScene();
            mScene = homeScene.onCreateScene(this, mCamera);
        } else if (scene == SceneType.SELECT_GAME) {
            Pikachu.getPikachu().detectView(Pikachu.SHOW_ADS);
            PikaGameScene selectGame = new PikaGameScene();
            mScene = selectGame.onCreateScene(this, mCamera);
        } else if (scene == SceneType.SPLASH) {
            Pikachu.getPikachu().detectView(Pikachu.VISIBLE_ADS);
            mScene = new Scene();
            splash = new Sprite(0, 0, splashTextureRegion,
                    mEngine.getVertexBufferObjectManager()) {
                @Override
                protected void preDraw(GLState pGLState, Camera pCamera) {
                    super.preDraw(pGLState, pCamera);
                    pGLState.enableDither();
                }
            };

            mScene.setScale(1.5f);
            mScene.setPosition((CAMERA_WIDTH - splash.getWidth()) * 0.4f,
                    (CAMERA_HEIGHT - splash.getHeight()) * 0.4f);
            mScene.attachChild(splash);

            /*
             * Text mLoading = new TickerText(100, -70,
             * Pikachu.getPikachu().mStrokeFont, getString(R.string.loading),
             * new TickerTextOptions(30), getVertexBufferObjectManager());
             * mLoading.setScale(0.5f); mScene.attachChild(mLoading);
             */
        }

        return mScene;

    }

    public void changeScene(SceneType scene) {
        mEngine.setScene(switchScene(scene));

    }

    @Override
    protected void onSetContentView() {
        // TODO Auto-generated method stub
        super.onSetContentView();
        // final RelativeLayout layout = (RelativeLayout)
        // findViewById(R.id.layout_render);
        // View vi = getLayoutInflater().inflate(R.layout.game_select_pikachu,
        // null, false);
        // Gallery g = (Gallery) vi.findViewById(R.id.gallery);
        // g.setAdapter(new SelectGameAdapter(getApplicationContext()));
        // layout.addView(vi);
        // vi.setVisibility(View.GONE);
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout_render);
        // View vi =
        // Pikachu.getPikachu().getLayoutInflater().inflate(R.layout.game_select_pikachu,
        // null, false);
        // layout.addView(vi);

        // View level =
        // Pikachu.getPikachu().getLayoutInflater().inflate(R.layout.level_pikachu,
        // null, false);
        // layout.addView(level);
    }

    public final Handler mHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            final RelativeLayout layout = (RelativeLayout) Pikachu.getPikachu()
                    .findViewById(R.id.layout_render);
            adView = (AdView) layout.findViewById(R.id.ads);
            switch (msg.arg1) {
                case SHOW_MORE:
                    //QuMiConnect.getQumiConnectInstance().showOffers(null);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://support.evanhe.com/download/")));
                    break;

                case SHOW_INT:
                    Random randomGenerator = new Random();
                    int randomInt = randomGenerator.nextInt(5);
                    if (randomInt < 3) {
                        QuMiConnect.getQumiConnectInstance(mPikachu)
                                .showPopUpAd(mPikachu);
                    }

                    adView.setVisibility(View.VISIBLE);
                    break;
                case SHOW_ADS:
                    adView.setVisibility(View.VISIBLE);
                    break;
                case VISIBLE_ADS:
                    adView.setVisibility(View.GONE);
                    break;
                case SHOW_GAME_SELECT:
                    View vi = (View) layout.findViewById(R.id.layout_game_select);
                    if (vi == null) {
                        vi = Pikachu.getPikachu().getLayoutInflater()
                                .inflate(R.layout.game_select_pikachu, null, false);
                        layout.addView(vi);
                    }

                    HorizontalScrollView g = (HorizontalScrollView) vi
                            .findViewById(R.id.horizontalScrollView1);
                    g.setVisibility(View.VISIBLE);
                    g.setVerticalFadingEdgeEnabled(true);
                    g.setHorizontalFadingEdgeEnabled(true);

                    gameMission = (LinearLayout) vi.findViewById(R.id.game_mission);
                    gameTop = (LinearLayout) vi.findViewById(R.id.game_top);
                    gameMore = (LinearLayout) vi.findViewById(R.id.game_more);

                    gameTop.setBackgroundColor(android.graphics.Color.TRANSPARENT);
                    gameMore.setBackgroundColor(android.graphics.Color.TRANSPARENT);
                    gameMission.setBackgroundResource(R.drawable.hover);
                    currentGame = gameMission;

                    if (mEngine.getScene() instanceof PikaPlayScene) {
                        if (currentGame.getTag().toString().equals("mission")) {

                            ((PikaGameScene) mEngine.getScene())
                                    .setGameDetail(getString(R.string.classic));
                        } else if (currentGame.getTag().toString().equals("top")) {
                            ((PikaGameScene) mEngine.getScene())
                                    .setGameDetail(getString(R.string.crazy));
                        } else {
                            ((PikaGameScene) mEngine.getScene())
                                    .setGameDetail(getString(R.string.more));

                        }
                    }
                    // g.setAdapter(new SelectGameAdapter(getApplicationContext()));
                    // g.setSelection(1);
                    // g.setOnItemClickListener(new OnItemClickListener() {
                    //
                    // public void onItemClick(AdapterView<?> parent, View v, int
                    // position, long id) {
                    //
                    // game_mode = position;
                    // if (game_mode == GAME_CLASSIC) {
                    // Pikachu.getPikachu().detectView(Pikachu.VISIBLE_GAME_SELECT);
                    // mEngine.setScene(switchScene(SceneType.LEVEL));
                    // } else if (game_mode == GAME_KING) {
                    // Pikachu.getPikachu().detectView(Pikachu.VISIBLE_GAME_SELECT);
                    // mEngine.setScene(switchScene(SceneType.GAME));
                    // } else {
                    // Intent myIntent = new Intent(Pikachu.this,
                    // FriendsActivity.class);
                    // startActivity(myIntent);
                    // }
                    // }
                    // });

                    // g.setOnItemSelectedListener(new OnItemSelectedListener() {
                    //
                    // private View lastview;
                    //
                    // public void onItemSelected(AdapterView<?> arg0, View arg1,
                    // int arg2, long arg3) {
                    // if (mEngine.getScene() instanceof PikaGameScene) {
                    //
                    // if(lastview!=null)
                    // lastview.setBackgroundColor(android.graphics.Color.TRANSPARENT);
                    // lastview = arg1;
                    // arg1.setBackgroundResource(R.drawable.hover);
                    //
                    // game_mode = arg2;
                    // if (game_mode == GAME_CLASSIC) {
                    // ((PikaGameScene) mEngine.getScene())
                    // .setGameDetail("Classic mode\nAccomplish all pairs of icons within \na limited time.");
                    // } else if (game_mode == GAME_KING) {
                    // ((PikaGameScene)
                    // mEngine.getScene()).setGameDetail("Crazy mode\nThe rest of time will be added \nto next level.");
                    // } else {
                    // ((PikaGameScene)
                    // mEngine.getScene()).setGameDetail("More game\n\nBest game here");
                    // }
                    // }
                    // }

                    // public void onNothingSelected(AdapterView<?> arg0) {
                    // // TODO Auto-generated method stub
                    //
                    // }
                    // });

                    // Animation slide = AnimationUtils.loadAnimation(Pikachu.this,
                    // R.anim.slide_in_level_table);
                    // g.startAnimation(slide);

                    break;
                case VISIBLE_GAME_SELECT:
                    final HorizontalScrollView g1 = (HorizontalScrollView) layout
                            .findViewById(R.id.horizontalScrollView1);

                    // slide = AnimationUtils.loadAnimation(Pikachu.this,
                    // R.anim.slide_out_level_table);

                    // slide.setAnimationListener(new AnimationListener() {

                    // public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub

                    // }

                    // public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                    // }

                    // public void onAnimationEnd(Animation animation) {

                    g1.setVisibility(View.GONE);
                    // }
                    // });
                    // g1.startAnimation(slide);
                    break;
                case SHOW_LEVEL_SELECT:
                    adView.setVisibility(View.GONE);
                    View level = (View) layout
                            .findViewById(R.id.layout_table_level);
                    if (level == null) {
                        level = Pikachu.getPikachu().getLayoutInflater()
                                .inflate(R.layout.level_pikachu, null, false);
                        layout.addView(level);
                    }

                    level.setVisibility(View.VISIBLE);
                    GridView gridview = (GridView) level
                            .findViewById(R.id.gridView1);

                    ImageAdapter imageAdap = new ImageAdapter(
                            getApplicationContext());

                    gridview.setAdapter(imageAdap);

                    imageAdap.vData = vData;

                    gridview.setOnItemClickListener(new OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v,
                                                int position, long id) {
                            if (vData.get(position).state >= PikaSaveGamer.LV_UNLOCK) {
                                Pikachu.getPikachu().detectView(
                                        Pikachu.VISIBLE_LEVEL_SELECT);
                                Pikachu.LVL = position + 1;
                                mEngine.setScene(switchScene(SceneType.GAME));
                            }
                        }
                    });

                    // slide = AnimationUtils.loadAnimation(Pikachu.this,
                    // R.anim.slide_in_level_table);
                    // level.startAnimation(slide);

                    break;
                case VISIBLE_LEVEL_SELECT:

                    final View g2 = (View) layout
                            .findViewById(R.id.layout_table_level);

                    // slide = AnimationUtils.loadAnimation(Pikachu.this,
                    // R.anim.slide_out_level_table);

                    // slide.setAnimationListener(new AnimationListener() {

                    // public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub

                    // }

                    // public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                    // }

                    // public void onAnimationEnd(Animation animation) {
                    g2.setVisibility(View.GONE);
                    // }
                    // });
                    // g2.startAnimation(slide);

                    break;
                default:
                    break;
            }
        }
    };

    public Bitmap getBitMapFromAsset(String imagePath) {
        AssetManager mngr = getAssets();
        // Create an input stream to read from the asset folder
        InputStream is = null;
        try {
            is = mngr.open(imagePath);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(is);

        } finally {
            try {
                is.close();
                is = null;
            } catch (IOException e) {
            }
        }

        return bitmap;
    }

    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {

        switch (pSceneTouchEvent.getAction()) {
            case TouchEvent.ACTION_DOWN:

                // this.mEngine.setScene(sceneSplash);
                // currentScene = SceneType.SPLASH;
                break;
            case TouchEvent.ACTION_UP:
                // this.mEngine.setScene(sceneGame);
                // sceneattachChild(mySprite);
                // currentScene = SceneType.GAME;
                // sound.play();
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    protected int getLayoutID() {
        // TODO Auto-generated method stub
        return R.layout.pikachu_gameplay;

    }

    @Override
    protected int getRenderSurfaceViewID() {
        // TODO Auto-generated method stub
        return R.id.rendersurfaceview;
    }

    public void saveHighScore(int score) {
        if (score > HIGH_SCORE) {
            HIGH_SCORE = score;
            PikaSaveGamer.saveHighScore(HIGH_SCORE);
        }
    }

    private static Bitmap grab(final int pGrabX, final int pGrabY,
                               final int pGrabWidth, final int pGrabHeight) {
        int width = 300;
        int height = 300;
        int screenshotSize = width * height;
        ByteBuffer bb = ByteBuffer.allocateDirect(screenshotSize * 4);
        bb.order(ByteOrder.nativeOrder());
        GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE, bb);
        int pixelsBuffer[] = new int[screenshotSize];
        bb.asIntBuffer().get(pixelsBuffer);
        bb = null;
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        bitmap.setPixels(pixelsBuffer, screenshotSize - width, -width, 0, 0,
                width, height);
        pixelsBuffer = null;

        short sBuffer[] = new short[screenshotSize];
        ShortBuffer sb = ShortBuffer.wrap(sBuffer);
        bitmap.copyPixelsToBuffer(sb);

        // Making created bitmap (from OpenGL points) compatible with
        // Android bitmap
        for (int i = 0; i < screenshotSize; ++i) {
            short v = sBuffer[i];
            sBuffer[i] = (short) (((v & 0x1f) << 11) | (v & 0x7e0) | ((v & 0xf800) >> 11));
        }
        sb.rewind();
        bitmap.copyPixelsFromBuffer(sb);

        return bitmap.copy(Bitmap.Config.ARGB_8888, false);
    }

    Bitmap mTmpBitMap;
    private static final int THUMB_SIZE = 150;

    @Override
    public void captureImage(Bitmap b) {
        mTmpBitMap = b;

        // Bitmap bmp = BitmapFactory.decodeResource(getResources(),
        // R.drawable.send_img);
        WXImageObject imgObj = new WXImageObject(mTmpBitMap);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(mTmpBitMap, THUMB_SIZE,
                THUMB_SIZE, true);
        mTmpBitMap.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true); // 设置缩略�?
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, "Y3MND4DDVV6QD72XZCW7");
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }
}
