package com.fwgg8547.loop2.gamebase.util;

import com.fwgg8547.loop2.gamebase.enginebase.*;

public class FpsController
{
	private long mStartTime;
	private int mCount;
	private float mFps;
	//private static final int N = 60;
	public static int mTestCounrt = 0;
	public FpsController(){
		mCount =0;
		mFps =0;
	}
	
	public boolean onUpdate(){
		boolean isUpdated = false;
		if( mCount == 0 ){ //1フレーム目なら時刻を記憶
			mStartTime = System.currentTimeMillis();
		}
		if( mCount == GLEngineBase.FRAME_RATE ){ //60フレーム目なら平均を計算する
			long t = System.currentTimeMillis();
			mFps = (t-mStartTime)/(float)GLEngineBase.FRATE_PRIOD_MS;
			mCount = 0;
			mStartTime = t;
			isUpdated = true;
		}
		mCount++;
		return isUpdated;
	}
	
	public float getFps(){
		return mFps;
	}
}
