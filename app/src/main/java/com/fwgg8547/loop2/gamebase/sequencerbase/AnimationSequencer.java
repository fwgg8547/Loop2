package com.fwgg8547.loop2.gamebase.sequencerbase;

import com.fwgg8547.loop2.gamebase.util.*;
import com.fwgg8547.loop2.gamebase.modelbase.*;
import java.util.List;
import java.util.ArrayList;

public class AnimationSequencer
{
	private final static  String TAG = AnimationSequencer.class.getSimpleName();
	private final int MAX_TICK_COUNT = 60000;
	private int mTick;
	private MotionSequnce[]  mMotionSequence;
	private RotateSequence[] mRotateSequence;
	private ScaleSequence[]  mScaleSequence;
	private TextureSequence[] mTextureSequence;
	private Vec2 mCurrentVect;
	private List<Callback> mCallbacks;
	private ItemBase mOwnerItem;
	private CurrentFrame mAnimFrame;
	
	public interface Callback{
		abstract public void notify(ItemBase i, int type);
	}
	
	public static class CurrentFrame {
		public int mtick;
		public int rtick;
		public int stick;
		public int ttick;
		
		public int mindex;
		public int rindex;
		public int sindex;
		public int tindex;
		
		public boolean mvalid;
		public boolean rvalid;
		public boolean svalid;
		public boolean tvalid;
	}
	
	public AnimationSequencer(){
		mCurrentVect = new Vec2(0,0);
		mAnimFrame = new CurrentFrame();
		mCallbacks = new ArrayList<Callback>();
	}

	public void Initialize(MotionSequnce[] ms, ItemBase i, Callback cb){
		Lg.d(TAG, "initialize");
		mAnimFrame.mvalid = false;
		mAnimFrame.mindex = 0;
		mMotionSequence = ms;
		mTick = 0;
		mAnimFrame.mtick = mMotionSequence[0].frame;
		
		if(cb != null){
			mCallbacks.add(cb);
			mOwnerItem = i;
		}
		mAnimFrame.mvalid = true;
	}

	public void Initialize(RotateSequence[] rs, ItemBase i, Callback cb){
		mAnimFrame.rvalid = false;
		mAnimFrame.rindex = 0;
		mRotateSequence = rs;
		mTick = 0;
		mAnimFrame.rtick = mRotateSequence[0].frame;

		if(cb != null){
			mCallbacks.add(cb);
			mOwnerItem = i;
		}
		mAnimFrame.rvalid = true;
	}
	
	public void clear(){
		
	}
	
	public void Initialize(ScaleSequence[] ms, ItemBase i, Callback cb){
		mAnimFrame.svalid = false;
		mAnimFrame.sindex = 0;
		mScaleSequence = ms;
		mTick = 0;
		mAnimFrame.stick = mScaleSequence[0].frame;
		if(cb != null){
			mCallbacks.add(cb);
			mOwnerItem = i;
		}
		mAnimFrame.svalid = true;
	}
	
	public void Initialize(TextureSequence[] ms, ItemBase i, Callback cb){
		mAnimFrame.tvalid = false;
		mAnimFrame.tindex = 0;
		mTextureSequence = ms;
		mTick = 0;
		mAnimFrame.ttick = mTextureSequence[0].frame;
		if(cb != null){
			mCallbacks.add(cb);
			mOwnerItem = i;
		}
		mAnimFrame.tvalid = true;
	}
	
	public void Initialize(MotionSequnce[] ms, RotateSequence[] rs ,ScaleSequence[] as, TextureSequence[] ts, ItemBase i, Callback cb){
		mAnimFrame.mvalid = false;
		mAnimFrame.rvalid = false;
		mAnimFrame.svalid = false;
		mAnimFrame.tvalid = false;
		
		mScaleSequence = as;
		mMotionSequence = ms;
		mTextureSequence = ts;
		mRotateSequence = rs;
		
		mCallbacks.add(cb);
		
		mTick = 0;
		mAnimFrame.sindex = 0;
		mAnimFrame.stick = mScaleSequence[0].frame;
		mAnimFrame.svalid = true;
		
		mAnimFrame.mindex = 0;
		mAnimFrame.mtick = mMotionSequence[0].frame;
		mAnimFrame.mvalid = true;
		
		mAnimFrame.rindex = 0;
		mAnimFrame.rtick = mRotateSequence[0].frame;
		mAnimFrame.rvalid = true;
		
		mAnimFrame.tindex = 0;
		mAnimFrame.ttick = mTextureSequence[0].frame;
		mAnimFrame.tvalid = true;
		
		if(cb != null){
			mCallbacks.add(cb);
			mOwnerItem = i;
		}
	}

	public void stopMotion(){
		mAnimFrame.mvalid = false;
	}
	
	public void stopRotate(){
		mAnimFrame.rvalid = false;
	}
	
	public void stopScale(){
		mAnimFrame.svalid = false;
		
	}
	
	public void stopTexture(){
		mAnimFrame.tvalid = false;

	}
	
	public void valid(boolean v) {
		if(v){
			mAnimFrame.mvalid = true;
			mAnimFrame.svalid = true;
			mAnimFrame.tvalid = true;
			mAnimFrame.rvalid = true;
		} else {
			mAnimFrame.mvalid = false;
			mAnimFrame.svalid = false;
			mAnimFrame.tvalid = false;
			mAnimFrame.rvalid = false;
		}
	}

	public boolean getValid() {
		return (mAnimFrame.mvalid ||mAnimFrame.rvalid || mAnimFrame.svalid || mAnimFrame.tvalid);
	}

	public boolean getMotionValid() {
		return mAnimFrame.mvalid;
	}
	
	public boolean getRotateValid() {
		return mAnimFrame.rvalid;
	}
	
	public void tick(){
		if(!(mAnimFrame.mvalid || mAnimFrame.rvalid || mAnimFrame.svalid || mAnimFrame.tvalid)){
			return;
		}
		
		if (mMotionSequence != null 
		&& mAnimFrame.mvalid 
		&& mAnimFrame.mtick != 0
		&& mTick > 0
		&& (mTick % mAnimFrame.mtick) == 0){
			mAnimFrame.mindex++;
			if (mAnimFrame.mindex >= mMotionSequence.length) {
				mAnimFrame.mindex = 0;
			}
			mAnimFrame.mtick = mMotionSequence[mAnimFrame.mindex].frame;
			
			if (mAnimFrame.mtick < 0){
				mAnimFrame.mvalid = false;
				for(int i=0, n=mCallbacks.size();i<n;i++){
					mCallbacks.remove(i).notify(mOwnerItem,0);
				}
			}
		}
		
		if (mRotateSequence != null 
				&& mAnimFrame.rvalid 
				&& mAnimFrame.rtick != 0
				&& mTick > 0
				&& (mTick % mAnimFrame.rtick) == 0){
			mAnimFrame.rindex++;
			if (mAnimFrame.rindex >= mRotateSequence.length) {
				mAnimFrame.rindex = 0;
			}
			mAnimFrame.rtick = mRotateSequence[mAnimFrame.rindex].frame;
			if (mAnimFrame.rtick < 0){
				mAnimFrame.rvalid = false;
				for(int i=0, n=mCallbacks.size();i<n;i++){
					mCallbacks.remove(i).notify(mOwnerItem,1);
				}
			}
		}
		
		if (mScaleSequence != null 
		&& mAnimFrame.svalid
		&& mAnimFrame.stick != 0
		&& mTick > 0
		&&(mTick % mAnimFrame.stick) == 0){
			mAnimFrame.sindex++;
			if (mAnimFrame.sindex >= mScaleSequence.length) {
				mAnimFrame.sindex = 0;
			}
			
			mAnimFrame.stick = mScaleSequence[mAnimFrame.sindex].frame;
			if (mAnimFrame.stick < 0){
				mAnimFrame.svalid = false;
				for(int i=0, n=mCallbacks.size();i<n;i++){
					mCallbacks.remove(i).notify(mOwnerItem,2);
				}
			}
		}

		if (mTextureSequence != null 
		&& mAnimFrame.tvalid
		&& mAnimFrame.ttick != 0
		&& mTick > 0
		&&(mTick % mAnimFrame.ttick) == 0){
			mAnimFrame.tindex++;
			if (mAnimFrame.tindex >= mTextureSequence.length) {
				mAnimFrame.tindex = 0;
			}

			mAnimFrame.ttick = mTextureSequence[mAnimFrame.tindex].frame;
			if (mAnimFrame.ttick < 0){
				mAnimFrame.tvalid = false;
				for(int i=0, n=mCallbacks.size();i<n;i++){
					mCallbacks.remove(i).notify(mOwnerItem,3);
				}
			}
		}
		
		mTick++;
		if(mTick >= MAX_TICK_COUNT){
			mTick = 0;
		}
	}

	public Vec2 getVector() {
		if(mMotionSequence != null){
			float ppf = mMotionSequence[mAnimFrame.mindex].ppf;
			mCurrentVect.x = mMotionSequence[mAnimFrame.mindex].direct.x * ppf;
			mCurrentVect.y = mMotionSequence[mAnimFrame.mindex].direct.y * ppf;
			return mCurrentVect;
		} else {
			return null;
		}
	}

	public float getRotate() {
		if(mRotateSequence != null){
			float dpf = mRotateSequence[mAnimFrame.rindex].dpf;
			return dpf;
		} else {
			return 0;
		}
	}
	
	public Vec2 getScale(){
		try{
			return new Vec2(
				mScaleSequence[mAnimFrame.sindex].scalex,
				mScaleSequence[mAnimFrame.sindex].scaley
			);
		}catch(Exception e){
			return null;
		}
	}
	
	public float[] getTextureUv(){
		try{
			return mTextureSequence[mAnimFrame.tindex].uv;
		}catch(Exception e){
			return null;
		}
	}
}
