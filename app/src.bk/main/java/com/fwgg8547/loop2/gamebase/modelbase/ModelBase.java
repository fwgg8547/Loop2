package com.fwgg8547.loop2.gamebase.modelbase;

import com.fwgg8547.loop2.gamebase.modelbase.CollisionModel;
import com.fwgg8547.loop2.gamebase.scorebase.ScoreBase;
import com.fwgg8547.loop2.gamebase.util.ReadersWriterLock;
import com.fwgg8547.loop2.gamebase.util.Lg;
import com.fwgg8547.loop2.gamebase.util.InnerEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

abstract public class ModelBase
{
	
	private final static String TAG = ModelBase.class.getSimpleName();
	public class ItemData {
		public float[] mVert = null;
		public float[] mColor = null;
		public short[] mIndex = null;
		public float[] mUv = null;
		
		public int mVecsIndex;
		public int mColorIndex;
		public int mUvIndex;
		public int mIndexIndex;
	}
	
	protected ReadersWriterLock mLock;
	protected ItemArray mItemList;
	protected int mLayerPriority;
	protected InnerEvent mInnerEvent;
	protected ScoreBase mScore;
	
	// Draw methods
	abstract public int getIndexCount();
	abstract public ItemData getData();
	abstract public int getTextureId();
	abstract public int getTextureCount();
	
	// Update methods
	abstract public void onUpdate();
	
	// Initialize
	public void initialize(ReadersWriterLock lock, int p){
		mLock = lock;
		mLayerPriority = p;
		mItemList = new ItemArray();
		mInnerEvent = new InnerEvent();
		//mIdItemMap = new HashMap<Integer, ItemBase>();
	}

	public void setScore(ScoreBase s){
		mScore = s;
	}
	
	public void clear()
	{
		if(mInnerEvent != null){
			mInnerEvent.clear();
			mInnerEvent = null;
		}
		if(mItemList != null){
			mItemList.clear();
			mItemList = null;
		}
		mLayerPriority = 0;
		mLock = null;
		mScore = null;
	}
	
	public int getLayerPriority(){
		return mLayerPriority;
	}
	
	public ItemArray getItemArray()
	{
		return mItemList;
	}
	
	public int size()
	{
		return mItemList.size();
	}
	
	public ItemBase createItem()
	{
		ItemBase ib = mItemList.getFreeItem();
		return ib;
	}
	
	public void freeItem(int index)
	{
		try{
			//mLock.writeLock();
			mItemList.freeItem(index);
		} catch(Exception e){
			Lg.stack(TAG, e.getStackTrace());
		} finally {
			//mLock.writeUnlock();
		}
	}
	
	// util
	protected short[] addOffset(short offset, short[] sl){
		short[] tmp = new short[sl.length];
		for(int i=0; i<sl.length; i++){
			tmp[i] = (short)(sl[i] + offset);
		}
		return tmp;
	}
	
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
	
}
