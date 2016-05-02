package com.fwgg8547.loop2.gamebase.modelbase;

import android.graphics.*;

public class ScreenModel
{
	private static ScreenModel instance;
	private static RectF mScreen;
	private static Callback mCallback;
	public static final float DEFAULT_W = 1080f;
	public static final float DEFAULT_H = 1980f;
	
	public interface Callback{
		abstract public void onScreenChange();
	}
	
	private ScreenModel(){
		mScreen = new RectF(0,0,DEFAULT_W,DEFAULT_H);
	}
	
	public static ScreenModel getInstance()
	{
		if(instance == null){
			instance = new ScreenModel();
		}
		
		return instance;
	}
	
	public static void clear(){
		mCallback = null;
		mScreen = null;
		instance = null;
	}
	
	public void setCallback(Callback cb){
		mCallback = cb;
	}
	
	public RectF getScreenInfo(){
		return mScreen;
	}
	
	public float getHeight(){
		return mScreen.bottom;
	}
	
	public void setScreenInfo(float w, float h){
		mScreen.set(0.0f,0.0f,w-1.0f,h-1.0f);
		mCallback.onScreenChange();
	}
}
