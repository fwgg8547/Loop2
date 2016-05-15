package com.fwgg8547.loop2.gamebase.modelbase;

import com.fwgg8547.loop2.gamebase.util.ReadersWriterLock;
import com.fwgg8547.loop2.gamebase.util.Lg;
import com.fwgg8547.loop2.gamebase.util.Vec2;
import com.fwgg8547.loop2.gamebase.sequencerbase.ItemGeneraterBase;
import com.fwgg8547.loop2.gamebase.sequencerbase.MotionSequnce;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import android.graphics.*;
import com.fwgg8547.loop2.gamebase.sequencerbase.*;

abstract public class SpriteModel extends ModelBase implements ItemGeneraterBase.GeneraterNotify
{
	private static final String TAG = "SpriteModel";
	private static final float SCREEN_MERGIN = 100.0f;
	private RectF mTempRect;
	protected ModelGroup mGroup;
	protected int mIndexCount;
	protected int mIdOffset;
	protected boolean mIsScrollable;
	protected static Vec2 mOffsetVect;
	
	public void initialize(ReadersWriterLock lock, int offset, int num, ModelGroup mg, int p)
	{
		super.initialize(lock, p);
		mGroup = mg;
		mIdOffset = offset;
		mIsScrollable = false;
		mOffsetVect = new Vec2(0,0);
	}
	
	public void clear(){
		mTempRect = null;
		mIndexCount = 0;
		mIdOffset = 0;
		mOffsetVect = null;
		super.clear();
	}

	public SpriteModel() 
	{
		mTempRect = new RectF(0,0,0,0);
	}

	public boolean getScrollable(){
		return mIsScrollable;
	}
	
	public void changeMotion(MotionSequnce[] ms){
		
		try{
			mLock.writeLock();
			ItemBase itm = null;
			for(int i=0; i<mItemList.size();i++){
				itm = mItemList.get(i);
				//itm.setAnimationValid(false);
				itm.setMotionPattern(ms,null);
			}
		} catch(Exception e){
			Lg.e(TAG, e.toString());
		} finally {
			mLock.writeUnlock();
		}
	}
	
	
	@Override
	public int getIndexCount(){
		return mIndexCount;
	}

	
	@Override
	public ItemData getData(){
		ItemData id = null;
		try{
			mLock.readLock();
			id = new ItemData();
			int count = mItemList.size();
			id.mVert = new float[count*12];
			id.mColor = new float[count*16];
			id.mIndex = new short[count*6];
			id.mUv = new float[count*8];
			id.mVecsIndex =0;
			id.mColorIndex =0;
			id.mIndexIndex =0;
			id.mUvIndex = 0;

			for(int j=0, n=mItemList.size();j<n;j++){
				NonCollidableItem itm = (NonCollidableItem)mItemList.get(j);
				if(!itm.mIsDeleted){
					itm.updateVertix();
					
					// add offset to indices
					short[] src_s = addOffset((short)(id.mVecsIndex/3), itm.getIndices());
					System.arraycopy(
						src_s , 0,
						id.mIndex, id.mIndexIndex,
						src_s.length);

					id.mIndexIndex += src_s.length;
				
					float[] src_f = itm.getVertices();
					System.arraycopy(
						src_f, 0,
						id.mVert, id.mVecsIndex,
						src_f.length);

					id.mVecsIndex += src_f.length;

					src_f = itm.getColors();
					System.arraycopy(
						src_f , 0,
						id.mColor, id.mColorIndex,
						itm.getColors().length);

					id.mColorIndex += src_f.length;

					src_f = itm.getUvs();
					System.arraycopy(
						src_f, 0,
						id.mUv, id.mUvIndex,
						src_f.length);

					id.mUvIndex += src_f.length;
				}
			}
		} catch (Exception InterruptedException){
			Lg.e(TAG, "1 "+InterruptedException.toString());
			Lg.e(TAG, "  "+InterruptedException.getStackTrace()[0].getLineNumber());
		} finally {
			mLock.readUnlock();
		}

		return id;
	}
	
	/*
	public float[] getVert(int length){
		float[] vert = null;
		
		try {
			mLock.readLock();
			int i = 1;
		
			for(int j=0, n=mItemList.size();j<n;j++){
				CollidableItem itm = (CollidableItem)mItemList.get(j);
				if(!itm.mIsDeleted){
					if(vert == null) {
						vert = itm.getVertices();
					} else {
						vert = floatArrayConcat(vert, itm.getVertices());
					}
					
					if(i >= length){
						break;
					} else {
						i++;
					}
				}
			}

		} catch(Exception e){
			Lg.e(TAG, "2 "+e.toString());
			Lg.stack(TAG, e.getStackTrace());
			
		} finally{
			mLock.readUnlock();
			return vert;
		}
	}

	public short[] getIndex(int length){
		short[] index = null;
		
		try{
			mLock.readLock();
			
			int i = 0;
		
			for(int j=0,n=mItemList.size();j<n;j++){
				CollidableItem it =(CollidableItem)mItemList.get(j);
				if(!it.mIsDeleted){
					if(index == null) {
						index = it.getIndices();
					} else {
						index = shortArrayConcat(index, it.getIndices());
					}

					if(i+1 >= length){
						break;
					} else {
						i++;
					}
				}
			}

			mIndexCount = index.length;
				
		} catch(Exception e){
			Lg.e(TAG, "3 "+e.toString());
			Lg.stack(TAG, e.getStackTrace());
			
		} finally{
			mLock.readUnlock();
			return index;
		}
	}

	public float[] getUvs(){
		float[] ux = null;
		try {
			mLock.readLock();
		
			for(int i=0; i<mItemList.size();i++){
				CollidableItem it = (CollidableItem)mItemList.get(i);
				if(it.mIsDeleted){
					continue;
				}
				if(ux == null){
					ux = it.getUvs();
				} else {
					ux = floatArrayConcat(ux, it.getUvs());
				}
			}
		
		} catch(Exception e){
			Lg.e(TAG, "4 "+e.toString());
			Lg.stack(TAG, e.getStackTrace());
		} finally {

			mLock.readUnlock();
			return ux;
		}
	}
*/

	protected boolean isInScreen(ItemBase i){
		mTempRect.set(ScreenModel.getInstance().getScreenInfo());
		
		// add mergin
		mTempRect.left -= SCREEN_MERGIN;
		mTempRect.right += SCREEN_MERGIN;
		mTempRect.top -= SCREEN_MERGIN;
		mTempRect.bottom +=SCREEN_MERGIN;

		return mTempRect.contains(i.getRect());
	}
	
	
	/*
	protected float[] floatArrayConcat(float[] a, float[] b){
		float res[] = new float[a.length + b.length];
		System.arraycopy(a, 0, res, 0, a.length);
		System.arraycopy(b, 0, res, a.length, b.length);
		return res;
	}

	protected short[] shortArrayConcat(short[] a, short[] b){
		short res[] = new short[a.length + b.length];
		System.arraycopy(a, 0, res, 0, a.length);
		System.arraycopy(b, 0, res, a.length, b.length);
		return res;
	}
	*/
	/*
	 @Override
	 public ItemData getData(){
	 ItemData id = new ItemData();
	 try{
	 mLock.readLock();

	 int i = 0;
	 for(int j=0, n=mBlocks.size();j<n;j++){
	 Item itm = (Item)mBlocks.get(j);
	 if(!itm.mIsDeleted){
	 if(id.mVert == null) {
	 id.mVert = itm.getSprite().getTransformedVertices();
	 } else {
	 id.mVert = floatArrayConcat(id.mVert, itm.getSprite().getTransformedVertices());
	 }

	 if(id.mIndex == null) {
	 id.mIndex = itm.getSprite().getIndices(i);
	 } else {
	 id.mIndex = shortArrayConcat(id.mIndex, itm.getSprite().getIndices(i));
	 }

	 if(id.mUv == null){
	 id.mUv = itm.getSprite().getUvs();
	 } else {
	 id.mUv = floatArrayConcat(id.mUv, itm.getSprite().getUvs());
	 }

	 i++;
	 }
	 }
	 if(id.mIndex != null){
	 mIndexCount = id.mIndex.length;
	 } else {
	 mIndexCount = 0;
	 }
	 } catch (Exception InterruptedException){
	 Lg.e(TAG, "1 "+InterruptedException.toString());
	 Lg.e(TAG, "  "+InterruptedException.getStackTrace()[0].getLineNumber());
	 } finally {
	 mLock.readUnlock();
	 }


	 return id;
	 }
	 */
	
}
