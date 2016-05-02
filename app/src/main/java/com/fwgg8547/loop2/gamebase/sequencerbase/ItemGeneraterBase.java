package com.fwgg8547.loop2.gamebase.sequencerbase;

import com.fwgg8547.loop2.gamebase.util.Lg;
import com.fwgg8547.loop2.gamebase.modelbase.ItemBase;
import android.content.Context;

abstract public class ItemGeneraterBase
{
	private final String TAG = ItemBase.class.getSimpleName();
	
	public interface GeneraterNotify {
		abstract public ItemBase createItem(int pattern);
	}
	
	public static class Sequence{
		public int tick;
		public int type;
		public int type2;
		public int repeat;
		public String stype;
		public String stype2;

		public Sequence(){

		}
		
		public Sequence(int tick, int type, int type2, int repeat){
			this.tick = tick;
			this.type = type;
			this.type2 = type2;
			this.repeat = repeat;
			this.stype = null;
			this.stype2 = null;
		}
		
		public Sequence(int tick, String stype, String stype2, int repeat){
			this.tick = tick;
			this.type = 0;
			this.type2 = 0;
			this.repeat = repeat;
			this.stype = stype;
			this.stype2 = stype2;
		}
	}
	
	protected GeneraterNotify mModel;
	protected boolean mIsAutoMode;
	
	// sequencer param
	protected int mCounter;
	protected Sequence mCurrent;
	protected int mRepeat;
	protected Sequence[] mSq;
	protected int mSqIndex;
	protected boolean mSequenceEnd;
	
	// level param
	protected int[] mLevel;
	protected int mLevelIndex;
	
	public ItemGeneraterBase(GeneraterNotify m){
		mModel = m;
		mIsAutoMode = false;
	}
		
	public void setAutoMode(boolean b)
	{
		mIsAutoMode = b;
	}
	
	public void clear(){
		mIsAutoMode = false;
	}
	
	public boolean generateNext(){
		ItemBase i = mModel.createItem(mCurrent.type);
		mSqIndex++;
		if (mSqIndex >= mSq.length){
			// end
			//mSequenceEnd = true;
			Lg.i(TAG, "sequence end");
			mSqIndex = 0;
		} else {
			mCurrent=mSq[mSqIndex];
		}
		return (i != null)? true:false;
	}
	
	public void tick(){
		boolean isReady =true;
		if(mSequenceEnd || !mIsAutoMode){
			return;
		}
		mCounter++;
		
		if(mCounter >= mCurrent.tick){
			if(mCurrent.type >= 0){
				ItemBase ib = mModel.createItem(mCurrent.type);
				if(ib == null){
					mCounter --;
					return;
				}
			}
			if(mCurrent.type2 >= 0){
				ItemBase ib = mModel.createItem(mCurrent.type2);
				if(ib == null){
					mCounter --;
					return;
				}
			}
			
			mCounter=0;
			mRepeat++;
			if(mRepeat >= mCurrent.repeat){
				mRepeat =0;
				mCounter =0;
				mSqIndex++;
				if (mSqIndex >= mSq.length){
					// end
					//mSequenceEnd = true;
					Lg.i(TAG, "sequence end");
					mSqIndex = 0;
					
				}
				mCurrent=mSq[mSqIndex];
			}
		}
		return;
	}
	
	public int getlevel(int score){
		int next = mLevel[mLevelIndex];
		if(score>=next){
			mLevelIndex++;
			if(mLevelIndex >= mLevel.length){
				mLevelIndex = 0;
			}
		}
		return mLevelIndex+1;//level start 1
	}
	
	abstract public void loadSequence(Context ctx, int level);
	abstract public void loadLevel(Context ctx);
	abstract public void createInitialItem();
}
