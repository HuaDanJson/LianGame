package com.yottamobile.doraemon.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.util.ScreenGrabber;
import org.andengine.opengl.util.GLState;

public class MSreenGrabber extends ScreenGrabber{
	
	public void onManagerDrawing(GLState pGLState,Camera pCamera){
		super.onManagedDraw(pGLState, pCamera);
	}
}
