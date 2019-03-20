package com.yottamobile.doraemon.object;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.PathModifier;

public class PikaPath extends PathModifier{

	public PikaPath(float pDuration, Path pPath) {
		super(pDuration, pPath);		
	}
	
	
	@Override
	public float onUpdate(float pSecondsElapsed, IEntity pEntity) {
		// TODO Auto-generated method stub
		return super.onUpdate(pSecondsElapsed, pEntity);
	}
	

}
