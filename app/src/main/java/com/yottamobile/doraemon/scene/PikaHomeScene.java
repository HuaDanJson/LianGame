package com.yottamobile.doraemon.scene;

import android.graphics.Bitmap;
import android.view.View;

import com.yottamobile.doraemon.PikachuActivity;
import com.yottamobile.doraemon.R;
import com.yottamobile.doraemon.SceneType;
import com.yottamobile.doraemon.data.PikaSaveGamer;
import com.yottamobile.doraemon.ui.MButton;
import com.yottamobile.doraemon.ui.Pika;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.JumpModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.text.TickerText;
import org.andengine.entity.text.TickerText.TickerTextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.ease.EaseBackInOut;
import org.andengine.util.modifier.ease.EaseQuadOut;

public class PikaHomeScene extends CustomScene implements IMyScene, OnClickListener {

    public static Bitmap search;
    public static String BTN_PLAY = "PLAY";
    public static String BTN_INFO = "INFO";
    public static String BTN_QUESTION = "QUESTION";
    BaseGameActivity game;
    protected MButton btnFish;
    protected MButton btnSoundBg;
    protected MButton btnInfo;
    protected MButton btnQuestion;
    private Sprite banner, playNow;
    int countEffect = 0;
    int doneEffect = 6;
    protected MButton btnSoundGame;
    private Camera mCamera;
    private int currentStep = 1;
    private int totalStep = 5;

    public void goIn() {
        countEffect = 0;
        MoveYModifier moveBanner = new MoveYModifier(0.5f, banner.getY(), 10, EaseBackInOut.getInstance()) {
            @Override
            protected void onModifierFinished(IEntity pItem) {
                countEffect++;
                super.onModifierFinished(pItem);
            }
        };
        banner.registerEntityModifier(moveBanner);

        MoveXModifier moveBtnSoundBG = new MoveXModifier(0.5f, btnSoundBg.getX(), 15, EaseBackInOut.getInstance()) {
            @Override
            protected void onModifierFinished(IEntity pItem) {
                countEffect++;
                super.onModifierFinished(pItem);
            }
        };

        btnSoundBg.registerEntityModifier(moveBtnSoundBG);

        MoveXModifier moveBtnSoundGame = new MoveXModifier(0.5f, btnSoundGame.getX(), 15, EaseBackInOut.getInstance()) {
            @Override
            protected void onModifierFinished(IEntity pItem) {
                countEffect++;
                super.onModifierFinished(pItem);
            }
        };

        btnSoundGame.registerEntityModifier(moveBtnSoundGame);

        MoveXModifier moveBtnInfo = new MoveXModifier(0.5f, btnInfo.getX(), 723, EaseBackInOut.getInstance()) {
            @Override
            protected void onModifierFinished(IEntity pItem) {
                countEffect++;
                super.onModifierFinished(pItem);
            }
        };

        btnInfo.registerEntityModifier(moveBtnInfo);

        MoveXModifier moveBtnQuestion = new MoveXModifier(0.5f, btnQuestion.getX(), 723, EaseBackInOut.getInstance()) {
            @Override
            protected void onModifierFinished(IEntity pItem) {
                countEffect++;
                super.onModifierFinished(pItem);
            }
        };
        moveBtnInfo.setAutoUnregisterWhenFinished(true);
        btnQuestion.registerEntityModifier(moveBtnQuestion);

        JumpModifier jump = new JumpModifier(0.5f, PikachuActivity.CAMERA_WIDTH + PikachuActivity.getPikachu().textureFish.getWidth(), 283, 178, 178, 200) {
            @Override
            protected void onModifierFinished(IEntity pItem) {
                countEffect++;
                playNow.setVisible(true);
                super.onModifierFinished(pItem);
            }
        };
        jump.setAutoUnregisterWhenFinished(true);
        btnFish.registerEntityModifier(jump);
    }

    public void goOut() {
        countEffect = 0;
        MoveYModifier moveBanner = new MoveYModifier(0.5f, banner.getY(), -PikachuActivity.getPikachu().background.getHeight(), EaseBackInOut.getInstance()) {
            @Override
            protected void onModifierFinished(IEntity pItem) {
                countEffect++;
                super.onModifierFinished(pItem);
            }
        };
        banner.registerEntityModifier(moveBanner);

        MoveXModifier moveBtnSoundBG = new MoveXModifier(0.5f, btnSoundBg.getX(), -PikachuActivity.getTextureRegionSoundBG().getWidth() - 500,
                EaseBackInOut.getInstance()) {
            @Override
            protected void onModifierFinished(IEntity pItem) {
                countEffect++;
                super.onModifierFinished(pItem);
            }
        };

        btnSoundBg.registerEntityModifier(moveBtnSoundBG);

        MoveXModifier moveBtnSoundGame = new MoveXModifier(0.5f, btnSoundGame.getX(), -PikachuActivity.getTextureRegionSoundBG().getWidth() - 500,
                EaseBackInOut.getInstance()) {
            @Override
            protected void onModifierFinished(IEntity pItem) {
                countEffect++;
                super.onModifierFinished(pItem);
            }
        };

        btnSoundGame.registerEntityModifier(moveBtnSoundGame);

        MoveXModifier moveBtnInfo = new MoveXModifier(0.5f, btnInfo.getX(), PikachuActivity.CAMERA_WIDTH + 500, EaseBackInOut.getInstance()) {
            @Override
            protected void onModifierFinished(IEntity pItem) {
                countEffect++;
                super.onModifierFinished(pItem);
            }
        };

        btnInfo.registerEntityModifier(moveBtnInfo);

        MoveXModifier moveBtnQuestion = new MoveXModifier(0.5f, btnQuestion.getX(), PikachuActivity.CAMERA_WIDTH + 500, EaseBackInOut.getInstance()) {
            @Override
            protected void onModifierFinished(IEntity pItem) {
                countEffect++;
                super.onModifierFinished(pItem);
            }
        };

        btnQuestion.registerEntityModifier(moveBtnQuestion);

        JumpModifier jump = new JumpModifier(0.5f, btnFish.getX(), -PikachuActivity.getPikachu().textureFish.getWidth() - 20, 178, 178, 200) {
            @Override
            protected void onModifierFinished(IEntity pItem) {
                countEffect++;
                super.onModifierFinished(pItem);
            }

            @Override
            protected void onModifierStarted(IEntity pItem) {
                playNow.setVisible(false);
                super.onModifierStarted(pItem);
            }
        };
        jump.setAutoUnregisterWhenFinished(true);
        btnFish.registerEntityModifier(jump);

    }

    @Override
    public Scene onCreateScene(BaseGameActivity game, Camera mCamera) {
        this.game = game;
        this.mCamera = mCamera;
        setBackground(new Background(Color.WHITE));

        final Sprite bg = new Sprite(0, 0, PikachuActivity.getPikachu().background, game.getVertexBufferObjectManager());
        bg.setVisible(false);
        attachChild(bg);

        banner = new Sprite(156, PikachuActivity.CAMERA_HEIGHT / 2 - PikachuActivity.getPikachu().regionBanner.getHeight() / 2, PikachuActivity.getPikachu().regionBanner,
                game.getVertexBufferObjectManager());

        playNow = new Sprite(243, 390, PikachuActivity.getPikachu().regionPlaynow, game.getVertexBufferObjectManager());
        playNow.setVisible(false);
        attachChild(playNow);

        banner.setAlpha(0);
        AlphaModifier alpha = new AlphaModifier(1f, 0, 1);

        DelayModifier delay = new DelayModifier(1) {

            @Override
            protected void onModifierFinished(IEntity pItem) {
                bg.setAlpha(0);
                bg.setVisible(true);
                AlphaModifier alpha = new AlphaModifier(1, 0, 1);
                bg.registerEntityModifier(alpha);

                btnFish = new MButton(PikachuActivity.CAMERA_WIDTH + PikachuActivity.getPikachu().textureFish.getWidth(), 178, PikachuActivity.getPikachu().textureFish,
                        PikaHomeScene.this.game.getVertexBufferObjectManager());

                JumpModifier jump = new JumpModifier(0.8f, btnFish.getX(), 283, 178, 178, 200) {

                    @Override
                    protected void onModifierFinished(IEntity pItem) {

                        ScaleModifier toBig = new ScaleModifier(0.5f, 1, 1.1f);
                        ScaleModifier toNormal = new ScaleModifier(0.5f, 1.1f, 1);
                        SequenceEntityModifier sequenFish = new SequenceEntityModifier(toBig, toNormal);
                        LoopEntityModifier loopScale = new LoopEntityModifier(sequenFish);
                        btnFish.registerEntityModifier(loopScale);
                        playNow.setVisible(true);
                        btnFish.setUserData(BTN_PLAY);
                        btnFish.setOnClickListener(PikaHomeScene.this);
                        PikaHomeScene.this.registerTouchArea(btnFish);

                        super.onModifierFinished(pItem);
                    }
                };
                jump.setAutoUnregisterWhenFinished(true);
                btnFish.registerEntityModifier(jump);
                PikaHomeScene.this.attachChild(btnFish);

                btnSoundBg = new MButton(-PikachuActivity.getTextureRegionSoundBG().getWidth() - 500, 315, PikachuActivity.getTextureRegionSoundBG(),
                        PikaHomeScene.this.game.getVertexBufferObjectManager());
                MoveXModifier moveSoundBg = new MoveXModifier(0.8f, btnSoundBg.getX(), 15, EaseBackInOut.getInstance());
                btnSoundBg.setUserData(PikachuActivity.BTN_SOUND_BG);
                btnSoundBg.setOnClickListener(PikaHomeScene.this);
                PikaHomeScene.this.registerTouchArea(btnSoundBg);
                PikaHomeScene.this.attachChild(btnSoundBg);
                btnSoundBg.registerEntityModifier(moveSoundBg);

                btnSoundGame = new MButton(-PikachuActivity.getTextureRegionSoundBG().getWidth() - 500, 391, PikachuActivity.getTextureRegionSoundGame(),
                        PikaHomeScene.this.game.getVertexBufferObjectManager());
                MoveXModifier moveSoundGame = new MoveXModifier(0.8f, btnSoundGame.getX(), 15, EaseBackInOut.getInstance());
                btnSoundGame.setUserData(PikachuActivity.BTN_SOUND_GAME);
                btnSoundGame.setOnClickListener(PikaHomeScene.this);
                PikaHomeScene.this.registerTouchArea(btnSoundGame);
                PikaHomeScene.this.attachChild(btnSoundGame);
                btnSoundGame.registerEntityModifier(moveSoundGame);

                btnInfo = new MButton(PikachuActivity.CAMERA_WIDTH + 500, 315, PikachuActivity.getPikachu().regionIconInfo,
                        PikaHomeScene.this.game.getVertexBufferObjectManager());
                MoveXModifier moveInfo = new MoveXModifier(0.8f, btnInfo.getX(), 723, EaseBackInOut.getInstance());
                btnInfo.setUserData(BTN_INFO);
                btnInfo.setOnClickListener(PikaHomeScene.this);
                PikaHomeScene.this.registerTouchArea(btnInfo);
                PikaHomeScene.this.attachChild(btnInfo);
                btnInfo.registerEntityModifier(moveInfo);

                btnQuestion = new MButton(PikachuActivity.CAMERA_WIDTH + 500, 391, PikachuActivity.getPikachu().regionIconQuestion,
                        PikaHomeScene.this.game.getVertexBufferObjectManager());
                MoveXModifier moveQuestion = new MoveXModifier(0.8f, btnQuestion.getX(), 723, EaseBackInOut.getInstance());
                btnQuestion.setUserData(BTN_QUESTION);
                btnQuestion.setOnClickListener(PikaHomeScene.this);
                PikaHomeScene.this.registerTouchArea(btnQuestion);
                PikaHomeScene.this.attachChild(btnQuestion);
                btnQuestion.registerEntityModifier(moveQuestion);

                super.onModifierFinished(pItem);
            }
        };

        MoveYModifier moveBanner = new MoveYModifier(1, banner.getY(), 10, EaseBackInOut.getInstance());
        SequenceEntityModifier sequen = new SequenceEntityModifier(new ParallelEntityModifier(delay, alpha), moveBanner);
        banner.registerEntityModifier(sequen);
        attachChild(banner);
        return this;
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if (pButtonSprite.getUserData().equals(BTN_PLAY)) {
            goOut();
            TimerHandler handler = new TimerHandler(0.1f, true, new ITimerCallback() {

                @Override
                public void onTimePassed(TimerHandler pTimerHandler) {
                    if (countEffect == doneEffect) {
                        PikachuActivity.getPikachu().getEngine().setScene(PikachuActivity.getPikachu().switchScene(SceneType.SELECT_GAME));
                    }
                }
            });
            registerUpdateHandler(handler);

        } else if (pButtonSprite.getUserData().equals(PikachuActivity.BTN_SOUND_BG)) {
            PikachuActivity.setSoundBG(!PikachuActivity.isSoundBG());

            btnSoundBg.attachChild(new MButton(0, 0, PikachuActivity.getTextureRegionSoundBG(), PikaHomeScene.this.game.getVertexBufferObjectManager()));

        } else if (pButtonSprite.getUserData().equals(PikachuActivity.BTN_SOUND_GAME)) {
            PikachuActivity.setSoundGame(!PikachuActivity.isSoundGame());
            btnSoundGame.attachChild(new MButton(0, 0, PikachuActivity.getTextureRegionSoundGame(), PikaHomeScene.this.game.getVertexBufferObjectManager()));
        } else if (pButtonSprite.getUserData().equals(BTN_QUESTION)) {
            // unlock map
//			PikaSaveGamer.unlockAllMap();
            PikachuActivity.vData = PikaSaveGamer.getDataLevel();
            showTutorial();
        } else if (pButtonSprite.getUserData().equals(BTN_INFO)) {
            // lock map
//			PikaSaveGamer.lockAllMap();
            PikachuActivity.vData = PikaSaveGamer.getDataLevel();
            final PopupScene pop = new PopupScene(PikaHomeScene.this.mCamera, PikaHomeScene.this, 0);

            MoveModifier move = new MoveModifier(0.3f, 0, 0, -PikachuActivity.getPikachu().regionBangDiem.getHeight(), 0, EaseQuadOut.getInstance());
            Sprite bg = new Sprite(0, 0, PikachuActivity.getPikachu().regionBangDiem, PikaHomeScene.this.game.getVertexBufferObjectManager());
            pop.registerEntityModifier(move);
            pop.attachChild(bg);


            Text mMission = new TickerText(100, -70, PikachuActivity.getPikachu().mStrokeFont,
                    game.getString(R.string.about),
                    new TickerTextOptions(30), game.getVertexBufferObjectManager());
            mMission.setScale(0.5f);
            pop.attachChild(mMission);

            MButton btnRetry = new MButton(380, 358, PikachuActivity.getPikachu().textureBack, game.getVertexBufferObjectManager()) {
                @Override
                public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                    MoveYModifier moveY = new MoveYModifier(0.3f, pop.getY(), -PikachuActivity.getPikachu().regionBangDiem.getHeight(),
                            EaseBackInOut.getInstance()) {
                        @Override
                        protected void onModifierFinished(IEntity pItem) {
                            PikaHomeScene.this.clearChildScene();
                            super.onModifierFinished(pItem);
                        }
                    };
                    pop.registerEntityModifier(moveY);
                    return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                }
            };
            pop.attachChild(btnRetry);
            pop.registerTouchArea(btnRetry);
        }


    }

    private void showTutorial() {
        final PopupScene pop = new PopupScene(PikaHomeScene.this.mCamera, PikaHomeScene.this, 0);

        MoveModifier move = new MoveModifier(0.3f, 0, 0, -PikachuActivity.getPikachu().regionBangDiem.getHeight(), 0, EaseQuadOut.getInstance());
        Sprite bg = new Sprite(0, 0, PikachuActivity.getPikachu().regionBangDiem, PikaHomeScene.this.game.getVertexBufferObjectManager());
        pop.registerEntityModifier(move);
        pop.attachChild(bg);
        final Text mStep = new Text(145, 56, PikachuActivity.getPikachu().fontTektonProWhite35, currentStep + "/" + totalStep,
                game.getVertexBufferObjectManager());
        pop.attachChild(mStep);

        final Entity content = new Entity(0, 0);
        pop.attachChild(content);
        setContent(content, pop);

        Text mText = new Text(500, 56, PikachuActivity.getPikachu().fontTektonProWhite35, game.getString(R.string.info1), game.getVertexBufferObjectManager());
        pop.attachChild(mText);
        MButton btnBack = new MButton(145, 200, PikachuActivity.getPikachu().textureBack, game.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    currentStep--;
                    if (currentStep < 1) {
                        currentStep = totalStep;
                    }
                    mStep.setText(currentStep + "/" + totalStep);
                    setContent(content, pop);
                }
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }

        };
        pop.attachChild(btnBack);
        pop.registerTouchArea(btnBack);

        MButton btnRetry = new MButton(375, 338, PikachuActivity.getPikachu().textureBack, game.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                MoveYModifier moveY = new MoveYModifier(0.3f, pop.getY(), -PikachuActivity.getPikachu().regionBangDiem.getHeight(),
                        EaseBackInOut.getInstance()) {
                    @Override
                    protected void onModifierFinished(IEntity pItem) {
                        PikaHomeScene.this.clearChildScene();
                        super.onModifierFinished(pItem);
                    }
                };
                pop.registerEntityModifier(moveY);
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        pop.attachChild(btnRetry);
        pop.registerTouchArea(btnRetry);

        MButton btnNext = new MButton(600, 200, PikachuActivity.getPikachu().textureNext, game.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.isActionDown()) {
                    currentStep++;
                    if (currentStep > totalStep) {
                        currentStep = 1;
                    }
                    mStep.setText(currentStep + "/" + totalStep);
                    setContent(content, pop);
                }
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        pop.attachChild(btnNext);
        pop.registerTouchArea(btnNext);
    }

    private void setContent(final Entity content, final PopupScene pop) {
        content.detachChildren();
        int pX = 254;
        int pY = 230;
        final int size = 90;
        if (currentStep == 1) {
            Pika p = new Pika(pX, pY, PikachuActivity.getPikachu().texturePikachu[0], game.getVertexBufferObjectManager());
            content.attachChild(p);
            Pika p1 = new Pika(pX + size, pY, PikachuActivity.getPikachu().texturePikachu[1], game.getVertexBufferObjectManager());
            content.attachChild(p1);
            Pika p2 = new Pika(pX + size, pY - size, PikachuActivity.getPikachu().texturePikachu[2], game.getVertexBufferObjectManager());
            content.attachChild(p2);
            Pika p3 = new Pika(pX + size * 2, pY, PikachuActivity.getPikachu().texturePikachu[0], game.getVertexBufferObjectManager());
            content.attachChild(p3);

            MoveModifier move1 = new MoveModifier(1, pX + size / 2, pX + size / 2, pY - 10, pY - size - 10) {
                @Override
                protected void onSetValues(IEntity pEntity, float pPercentageDone, float pX, float pY) {
                    super.onSetValues(pEntity, pPercentageDone, pX, pY);
                    Line line1 = new Line(getFromValueA(), getFromValueB(), pX, pY, 5, game.getVertexBufferObjectManager());
                    content.attachChild(line1);
                }

                @Override
                protected void onModifierFinished(IEntity pItem) {
                    Text mT = new Text(getFromValueA() - 30, getFromValueB() - size / 2, PikachuActivity.getPikachu().fontTektonProScore20, "1",
                            game.getVertexBufferObjectManager());
                    content.attachChild(mT);
                    super.onModifierFinished(pItem);
                }
            };

            MoveModifier move2 = new MoveModifier(1, pX + size / 2, pX + size * 2 + size / 2, pY - size - 10, pY - size - 10) {
                @Override
                protected void onSetValues(IEntity pEntity, float pPercentageDone, float pX, float pY) {
                    super.onSetValues(pEntity, pPercentageDone, pX, pY);
                    Line line1 = new Line(getFromValueA(), getFromValueB(), pX, pY, 5, game.getVertexBufferObjectManager());
                    content.attachChild(line1);
                }

                @Override
                protected void onModifierFinished(IEntity pItem) {
                    Text mT = new Text(getFromValueA() + size, getFromValueB() - 33, PikachuActivity.getPikachu().fontTektonProScore20, "2",
                            game.getVertexBufferObjectManager());
                    content.attachChild(mT);
                    super.onModifierFinished(pItem);
                }
            };

            MoveModifier move3 = new MoveModifier(1, pX + size * 2 + size / 2, pX + size * 2 + size / 2, pY - size - 10, pY - 10) {
                @Override
                protected void onSetValues(IEntity pEntity, float pPercentageDone, float pX, float pY) {
                    super.onSetValues(pEntity, pPercentageDone, pX, pY);
                    Line line1 = new Line(getFromValueA(), getFromValueB(), pX, pY, 5, game.getVertexBufferObjectManager());
                    content.attachChild(line1);
                }

                @Override
                protected void onModifierFinished(IEntity pItem) {
                    Text mT = new Text(getFromValueA() + 5, getFromValueB() + size / 2 - 5, PikachuActivity.getPikachu().fontTektonProScore20, "3",
                            game.getVertexBufferObjectManager());
                    content.attachChild(mT);
                    super.onModifierFinished(pItem);
                }
            };

            SequenceEntityModifier squen = new SequenceEntityModifier(move1, move2, move3);
            Entity tmp = new Entity();
            tmp.registerEntityModifier(squen);
            content.attachChild(tmp);

        } else if (currentStep == 2) {

            Pika p2 = new Pika(pX + size, pY - size, PikachuActivity.getPikachu().texturePikachu[0], game.getVertexBufferObjectManager());
            final ScaleModifier mScale = new ScaleModifier(0.2f, 2, 1);

            AnimatedSprite aniIce = new AnimatedSprite(0, 0, PikachuActivity.getPikachu().tiledEffectIce,
                    game.getVertexBufferObjectManager());

            aniIce.animate(100);
            aniIce.registerEntityModifier(mScale);
            p2.attachChild(aniIce);

            content.attachChild(p2);
            Text mT = new TickerText(pX, pY + 30, PikachuActivity.getPikachu().fontTektonProWhite35, game.getString(R.string.info2), new TickerTextOptions(30),
                    game.getVertexBufferObjectManager());
            content.attachChild(mT);
        } else if (currentStep == 3) {
            final Pika pika2 = new Pika(pX + size, pY - size, PikachuActivity.getPikachu().texturePikachu[0], game.getVertexBufferObjectManager()) {
                @Override
                public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                    if (pSceneTouchEvent.isActionDown()) {
                        flipUp();
                    }
                    return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                }
            };
            content.attachChild(pika2);
            Text mT = new TickerText(pX, pY + 30, PikachuActivity.getPikachu().fontTektonProWhite35, game.getString(R.string.info3), new TickerTextOptions(
                    30), game.getVertexBufferObjectManager());
            content.attachChild(mT);
            pika2.setHidden(new Sprite(0, 0, PikachuActivity.getPikachu().texturePikachu[19], game.getVertexBufferObjectManager()));
            pop.registerTouchArea(pika2);
        } else if (currentStep == 4) {
            final Pika pika2 = new Pika(pX + size, pY - size, PikachuActivity.getPikachu().texturePikachu[18], game.getVertexBufferObjectManager());
            content.attachChild(pika2);

            final Text time = new Text(17, 17, PikachuActivity.getPikachu().fontTektonProRed, "10", new TextOptions(HorizontalAlign.CENTER),
                    game.getVertexBufferObjectManager());
            time.setScale(0.5f);
            pika2.attachChild(time);

            Text mT = new TickerText(pX, pY + 30, PikachuActivity.getPikachu().fontTektonProWhite35, game.getString(R.string.info4),
                    new TickerTextOptions(30), game.getVertexBufferObjectManager());
            content.attachChild(mT);

        } else if (currentStep == 5) {
            Sprite board = new Sprite(pX, 80, PikachuActivity.getPikachu().textureItemBoard, game.getVertexBufferObjectManager());
            content.attachChild(board);

            MButton btnRandom = new MButton(17, 26, PikachuActivity.getPikachu().random[1], game.getVertexBufferObjectManager());
            board.attachChild(btnRandom);

            MButton btnBoom = new MButton(17, 105, PikachuActivity.getPikachu().boom[1], game.getVertexBufferObjectManager());
            board.attachChild(btnBoom);

            MButton btnLockTime = new MButton(17, 185, PikachuActivity.getPikachu().lockTime[1], game.getVertexBufferObjectManager());
            board.attachChild(btnLockTime);

            Text mText = new Text(90, 30, PikachuActivity.getPikachu().fontTektonProWhite35, game.getString(R.string.info5),
                    game.getVertexBufferObjectManager());
            board.attachChild(mText);
        }
    }
}
