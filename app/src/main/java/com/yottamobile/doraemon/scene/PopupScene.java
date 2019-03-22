package com.yottamobile.doraemon.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.Scene;

public class PopupScene extends CameraScene {
    public PopupScene(final Camera pCamera, final Scene pParentScene, final float pDurationSeconds) {
        this(pCamera, pParentScene, pDurationSeconds, null);
    }

    public PopupScene(final Camera pCamera, final Scene pParentScene, final float pDurationSeconds, final Runnable pRunnable) {
        super(pCamera);
        this.setBackgroundEnabled(false);

        pParentScene.setChildScene(this, false, true, true);

        if (pDurationSeconds > 0) {
            this.registerUpdateHandler(new TimerHandler(pDurationSeconds, new ITimerCallback() {

                @Override
                public void onTimePassed(final TimerHandler pTimerHandler) {
                    PopupScene.this.unregisterUpdateHandler(pTimerHandler);
                    pParentScene.clearChildScene();
                    if (pRunnable != null) {
                        pRunnable.run();
                    }
                }
            }));
        }
    }
}
