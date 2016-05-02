package com.fwgg8547.loop2.generater;
import com.fwgg8547.loop2.gamebase.sequencerbase.*;
import com.fwgg8547.loop2.gamebase.modelbase.*;
import com.fwgg8547.loop2.gamebase.util.*;
import com.fwgg8547.loop2.model.*;
import android.content.Context;
import java.util.Random;

public class WallGenerater extends ItemGeneraterBase
{
	private final String TAG = WallGenerater.class.getSimpleName();
	private int mSequenceGroupNumber;
	private int mSequenceGroupLength;
	private Random mRand = new Random();
	
	public WallGenerater(WallModel2 m){
		super(m);
		mSequenceEnd = false;
		mSequenceGroupNumber = 0;
		mSequenceGroupLength = 0;
		
	}

	@Override
	public void loadSequence(Context ctx, int level){
		mCounter = 0;
		mSqIndex = 0;
		mSq = null;
		mRepeat = 0;
		mSequenceEnd = false;

		mSq = ResourceFileReader.getSequence(mSequenceGroupNumber);
		mSequenceGroupLength = ResourceFileReader.getSequenceSize();
		mCurrent = mSq[mSqIndex];
	}

	@Override
	public void loadLevel(Context ctx){
		mLevel = ResourceFileReader.getLevelThreash();
	}
	
	@Override
	public void tick(){
		// debug
		return;
		
		/*
		if(mSequenceEnd || !mIsAutoMode){
			return;
		}
		mCounter++;

		if(mCounter >= mCurrent.tick){
			if(mCurrent.type >= 0){
				ItemBase ib = mModel.createItem(mCurrent.type);
				if(ib == null){
					mCounter =0;
					return;
				}
			}
			if(mCurrent.type2 >= 0){
				ItemBase ib = mModel.createItem(mCurrent.type2);
				if(ib == null){
					mCounter =0;
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
					Lg.w(TAG, "change sequence");
					mSequenceGroupNumber++;
					mSqIndex = 0;
					if(mSequenceGroupNumber > (mSequenceGroupLength-1)){
						mSequenceGroupNumber = mRand.nextInt(5)+1;
						Lg.w(TAG,"Rand = "+mSequenceGroupNumber);
					}
					mSq = ResourceFileReader.getSequence(mSequenceGroupNumber);
				}
				mCurrent=mSq[mSqIndex];
			}
		}
		*/
	}

	@Override
	public void createInitialItem()
	{
		// TODO: Implement this method
	}

	public void free(){
	}
	
}
