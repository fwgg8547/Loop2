package com.fwgg8547.loop2.model;

import com.fwgg8547.loop2.gamebase.modelbase.*;
import com.fwgg8547.loop2.gamebase.sequencerbase.*;
import com.fwgg8547.loop2.gamebase.util.*;

import com.fwgg8547.loop2.generater.*;
import com.fwgg8547.loop2.R;

import android.graphics.*;
import com.fwgg8547.loop2.*;

public class WallModel2 extends CollisionModel
{
	private static final String TAG = "";//WallModel2.class.getSimpleName();
	private static final int MAX_BLOCK = 30;
	private static final int SCORERATE = 100;
	private static boolean mIsFirstUpdate;
	private WallItem2 mLatestItemR;
	private WallItem2 mLatestItemL;
	private Generater mGenerater;
	private int mScoreMeter;
	
	public WallModel2(){
		//mIsFirstUpdate = true;
		//mGenerater = new Generater();
		//mScoreMeter = 0;
	}

	public void initialize(ReadersWriterLock lock, int offset, ModelGroup mg, int p){
		super.initialize(lock, offset, MAX_BLOCK, mg, p);

		// repleace own iremlists
		WallItem2[] items = new WallItem2[MAX_BLOCK];
		for(int i=0;i<MAX_BLOCK; i++){
			items[i] = new WallItem2();
			items[i].mIsDeleted = true;
			items[i].mIndex = i;
		}
		mItemList.initialize(items);
		mLatestItemR = null;
		mLatestItemL = null;
		mIsFirstUpdate = true;
		mGenerater = new Generater();
		mGenerater.init(offset, MAX_BLOCK);
		
	}

	@Override
	public void onUpdate()
	{
		Lg.a(TAG, "update");
		mIsFirstUpdate = false;
		try{
			mLock.writeLock();
			for(int i=0; i<mItemList.size();i++){
				WallItem2 itm = (WallItem2)mItemList.get(i);
				if(itm.mIsDeleted){
					Lg.i(TAG, "block was deleted id =" +itm.getId());
					freeItem(i);
					i--; // mblock was reduced
				}
				// animation check
				if(!itm.mIsDeleted) {
					PointF pos = itm.moveAnimation();
					mScoreMeter += 1;
				}
				
				if(itm.getPosition().y < -600){
					itm.mIsDeleted = true;
				}
			}
			
		} catch(Exception e){
			Lg.e(TAG, "a "+e.toString());
			Lg.stack(TAG, e.getStackTrace());
		} finally{
			mLock.writeUnlock();
		}
		
		if(mScoreMeter/SCORERATE > 0){
			mScore.add(1);
			mScoreMeter = 0;
		}
	}

	@Override
	public void moveScroll()
	{
		// TODO: Implement this method
	}


	@Override
	public ItemBase createItem(int pattern)
	{
		WallItem2 ib = null;
		try{
			mLock.writeLock();
			ib = (WallItem2)super.createItem();
			mGenerater.createItem(ib, pattern);
		} catch (Exception e){

		} finally {
			mLock.writeUnlock();
		}
		return ib;
	}

	public ItemBase createItem(int p, float y){
		WallItem2 ib = null;
		try{
			mLock.writeLock();
			ib = (WallItem2)super.createItem();
			ib.setType(GLEngine.WALLMODELINDX);
			mGenerater.createItem(ib, p);
		} catch (Exception e){

		} finally {
			mLock.writeUnlock();
		}
		return ib;
	}

	@Override
	public int getTextureId()
	{
		return R.drawable.wall_image;
	}

	@Override
	public int getTextureCount()
	{
		return 1;
	}

	public class Generater {		
		private int mOffset;
		private int mCurr;
		private int mMax;

		public Generater (){
		}

		public void init(int offset, int max){
			mMax = max;
			mOffset = offset;
			mCurr =0;
		}

		public WallItem2 createItem(WallItem2 i, int pattern){
			if(i == null){
				return null;
			}
			
			WallItem2 it = i;
			try {
				//mLock.writeLock();
				ItemPattern p = createPattern(pattern);
				
				WallSprite s = new WallSprite(mOffset + mCurr);
				it.setId(mOffset+mCurr);
				it.setSprite(s);
				if(p.mQuad != null){
					it.setQuadrilateral(p.mQuad);
				}
				if(p.mType == 0){
					if(mLatestItemL != null){
						it.setPositionY(mLatestItemL.getPosition().y-mLatestItemL.getHight());
					} else {
						it.setPosition(
							0, 
							990,
							0.0f, 0.0f);					
					}
					mLatestItemL = it;
				} else {
					if(mLatestItemR != null){
						it.setPositionY(mLatestItemR.getPosition().y-mLatestItemR.getHight());
					} else {
						it.setPosition(
							1080, 
							990,
							0.0f, 0.0f);					
					}
					mLatestItemR = it;
				}
				
				
				if(p.mMotionPattern != null){
					it.setMotionPattern(p.mMotionPattern, null);
				}
				if(p.mScalePattern != null){
					it.setScalePattern(p.mScalePattern, null);
				}
				if(p.mTexturePattern != null){
					it.setTexturePattern(p.mTexturePattern, null);
				}
				//it.setBlockType(p.mItemId);
				it.setColor(new float[] {1,1,1,1});
				it.setAnimationValid(true);
				it.moveAnimation(); // init 
				it.updateVertix();
				it.mIsDeleted = false;
				
				Lg.i(TAG, "create block = "+it.getId()+"|"+pattern);
				mCurr++;

			}catch(Exception e){
				Lg.stack(TAG, e.getStackTrace());
			}finally{
				//mLock.writeUnlock();
			}
			return it;
		}
		
		private ItemPattern createPattern(int p){
			int rightLeft = (p & 0x0F000000) >> 24;
			int bottomX20 = (p & 0x00FF0000) >> 16;
			int topX20 =    (p & 0x0000FF00) >> 8;
			int hight80 =   (p & 0x000000FF);
			
			ItemPattern ip = new ItemPattern();
			Quadrilateral q = null;
			
			if(rightLeft == 1) {
				int bottomX = (bottomX20*20 >= 1080)? 1079:bottomX20*20;
				int topX = (topX20*20 >= 1080)? 1079:topX20*20;
				q = new Quadrilateral(
					new PointF(bottomX,0),
					new PointF(1080, 0),
					new PointF(1080, hight80*80),
					new PointF(topX,hight80*80)
					
				);
			} else {
				q = new Quadrilateral(
					new PointF(0,0),
					new PointF(bottomX20*20,0),
					new PointF(topX20*20, hight80*80),
					new PointF(0,hight80*80)
					
				);
			}
			
			ip.mInitPos = new PointF(0,0);
			ip.mType = rightLeft;
			MotionSequnce ms = new MotionSequnce();
			ms.frame = 3000; ms.ppf = 8; ms.direct = new Vec2(0, -1);
			ip.mMotionPattern = new MotionSequnce[]{
				ms
			};
			ip.mQuad = q;
			
			return ip;
		}
	}

	public void deleteItem(CollidableItem it){
		ItemPattern p = ResourceFileReader.getPattern(ResourceFileReader.Type.Block, 0);
		it.delete(p);
	}
	
	
}
