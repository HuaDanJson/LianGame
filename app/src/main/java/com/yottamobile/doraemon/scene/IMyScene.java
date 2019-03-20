package com.yottamobile.doraemon.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

public interface IMyScene {
		
	public Scene onCreateScene(BaseGameActivity game,Camera mCamera);	
}
