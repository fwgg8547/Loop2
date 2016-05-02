package com.fwgg8547.loop2.model;
import com.fwgg8547.loop2.gamebase.modelbase.*;
import com.fwgg8547.loop2.gamebase.sequencerbase.*;
import com.fwgg8547.loop2.gamebase.util.*;
import com.fwgg8547.loop2.generater.ResourceFileReader;
import com.fwgg8547.loop2.R;
import android.graphics.*;


public class WallModel extends CollisionModel
{
	private static final String TAG = WallModel.class.getSimpleName();
	private static final int MAX_BLOCK = 30;
	private static boolean mIsFirstUpdate;
	private Generater mGenerater;

	public WallModel(){
		super();
		mIsFirstUpdate = true;
		mGenerater = new Generater();
		
	}

	public void initialize(ReadersWriterLock lock, int offset, ModelGroup mg, int p){
		super.initialize(lock, offset, MAX_BLOCK, mg, p);

		// repleace own iremlists
		WallItem[] items = new WallItem[MAX_BLOCK];
		for(int i=0;i<MAX_BLOCK; i++){
			items[i] = new WallItem();
			items[i].mIsDeleted = true;
			items[i].mIndex = i;
		}
		mItemList.initialize(items);
		mIsFirstUpdate = true;
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
				WallItem itm = (WallItem)mItemList.get(i);
				if(itm.mIsDeleted){
					Lg.i(TAG, "block was deleted id =" +itm.getId());
					freeItem(i);
					i--; // mblock was reduced
				}
				// animation check
				if(!itm.mIsDeleted) {
					PointF pos = itm.moveAnimation();
				}
			}
		} catch(Exception e){
			Lg.e(TAG, "a "+e.toString());
			Lg.stack(TAG, e.getStackTrace());
		} finally{
			mLock.writeUnlock();
		}
	}

	@Override
	public void moveScroll()
	{
		// TODO: Implement this method
		return;
	}

	@Override
	public ItemBase createItem(int pattern)
	{
		WallItem ib = null;
		try{
			mLock.writeLock();
			ib = (WallItem)super.createItem();
			mGenerater.createItem(ib, pattern);
		} catch (Exception e){

		} finally {
			mLock.writeUnlock();
		}
		return ib;
	}

	public ItemBase createItem(int p, float y){
		WallItem ib = null;
		try{
			mLock.writeLock();
			ib = (WallItem)super.createItem();
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
		return R.drawable.brightnessswitch_icon;
	}

	@Override
	public int getTextureCount()
	{
		// TODO: Implement this method
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

		public WallItem createItem(WallItem i, int pattern){
			WallItem it = i;
			try {
				//mLock.writeLock();
				ItemPattern p = ResourceFileReader.getPattern(ResourceFileReader.Type.Wall,pattern);
				Sprite s = new Sprite(mOffset + mCurr);
				it.setId(mOffset+mCurr);
				it.setSprite(s);

				if(p.mInitPos != null){
					it.setPosition(p.mInitPos.x, p.mInitPos.y, 0.0f, 0.0f);
				}
				if(p.mRect != null){
					it.setRect(p.mRect);
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
				it.setAnimationValid(true);
				it.moveAnimation(); // init 
				it.updateVertix();
				it.mIsDeleted = false;
				Lg.i(TAG, "create block = "+it.getId());
				mCurr++;

			}catch(Exception e){
				Lg.stack(TAG, e.getStackTrace());
			}finally{
				//mLock.writeUnlock();
			}
			return it;
		}
	}

	public void deleteItem(CollidableItem it){
		ItemPattern p = ResourceFileReader.getPattern(ResourceFileReader.Type.Block, 0);
		it.delete(p);
	}
}
