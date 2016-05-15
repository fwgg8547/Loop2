package com.fwgg8547.loop2.gamebase.scorebase;

import com.fwgg8547.loop2.gamebase.util.*;
import com.fwgg8547.loop2.gamebase.modelbase.*;
import com.fwgg8547.loop2.gamebase.preferencebase.*;
import com.fwgg8547.loop2.gamebase.controllerbase.*;
import com.fwgg8547.loop2.gamebase.controllerbase.GLControllerBase.Callback.*;
import com.fwgg8547.loop2.gamebase.sequencerbase.*;

import android.content.Context;
import android.graphics.PointF;
import android.preference.*;

public class ScoreBase implements GLControllerBase.Callback
{
	private final static String TAG = ScoreBase.class.getSimpleName();
	private Context mContext;
	private PreferenceLoader mPrefLoader;
	protected static int mScore;
	protected static int mHiScore;
	protected static int mLevel;
	protected TextModelBase mTextModel;
	protected NotifyScore mCallback;
	
	
	public interface NotifyScore {
		public void levelChanged(int newLevel);
	}
	
	public ScoreBase(Context ctx, NotifyScore cb){
		mContext = ctx;
		mCallback = cb;
	}

	@Override
	public void notifyEvent(GLControllerBase.Callback.Event e)
	{
		switch(e){
			case GameOver:
				updateHiscore();
				break;
		}
	}

	public void clear(){
		mContext = null;
		if(mTextModel != null){
			mTextModel.clear();
			mTextModel = null;
		}
		mScore = 0;
		mCallback = null;
	}
	
	public void addTextModel(TextModelBase m) {
		mTextModel = m;
	}

	public void onUpdate()
	{
		mTextModel.onUpdate();
	}

	public void initialize(){
		Lg.d(TAG, "Score initialize");
		mScore = 0;
		mLevel = 0;
	}

	public void add(int s){
		mScore += s;
	}
	
	public void updateHiscore(int s){
		int saved = mPrefLoader.getHiscore();
		if(saved < s){
			mPrefLoader.setHiscore(s);
			mHiScore = s;
		} else {
			mHiScore = saved;
		}
		mPrefLoader.setScore(mScore);
	}
	
	public void updateHiscore(){
		updateHiscore(mScore);
	}
	
	public int getHiScore(){
		if(mHiScore == 0){
			mHiScore = mPrefLoader.getHiscore();
		}
		return mHiScore;
	}
	
	static public int getCurrentScore(){
		return mScore;
	}
}
