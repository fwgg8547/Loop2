package com.fwgg8547.loop2.gamebase.modelbase;

import com.fwgg8547.loop2.gamebase.modelbase.*;
import com.fwgg8547.loop2.gamebase.util.*;
import com.fwgg8547.loop2.R;

import android.graphics.*;
import android.text.*;

public class TextModelBase extends ModelBase implements TextItemBase.NotifyUpdate
{
	private static final String TAG = TextModelBase.class.getSimpleName();
	private static final int MAX_BLOCK = 10;
	private int mTotalTextLength;
	
	public TextModelBase(){
		super();
	}

	@Override
	public void textUpdated()
	{
		updateLength();
	}

	@Override
	public int size()
	{
		return mItemList.size();
	}
	
	public void initialize(ReadersWriterLock lock, int num, int p){
		super.initialize(lock, p);
		Lg.d(TAG, "initialize");
		TextItemBase[] items = new TextItemBase[num];
		for(int i=0;i<num; i++){
			items[i] = new TextItemBase();
			items[i].mIsDeleted = true;
			items[i].mIndex = i;
		}
		mItemList.initialize(items);
		
	}
	
	public void clear() {
		mTotalTextLength = 0;
		super.clear();
	}
	
	@Override
	public int getIndexCount(){
		return 0;
	}
	
	public ItemData getData(){
		ItemData id = new ItemData();
		try{
			mLock.readLock();
			id.mVert = new float[mTotalTextLength*12];
			id.mColor = new float[mTotalTextLength*16];
			id.mIndex = new short[mTotalTextLength*6];
			id.mUv = new float[mTotalTextLength*8];
			id.mVecsIndex =0;
			id.mColorIndex =0;
			id.mIndexIndex =0;
			id.mUvIndex = 0;
			
			for(int j=0, n=mItemList.size();j<n;j++){
				TextItemBase itm = (TextItemBase)mItemList.get(j);
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

	public TextItemBase createItem(String s, PointF pos, TextTexUtil.FontSize size){
		TextItemBase i = null;
		try{
			mLock.writeLock();
			TextSprite sp = new TextSprite(0, s, pos, TextTexUtil.convSizeToScale(size));
			i = (TextItemBase)mItemList.getFreeItem();
			i.setSprite(sp);
			i.addCallback(this);
			i.mIsDeleted = false;
			mTotalTextLength+=s.length();
			
		}catch(Exception e){
			
		}finally{
			mLock.writeUnlock();
		}
		return i;
	}
	
	public void freeItem(CollidableItem i)
	{
		// TODO: Implement this method
	}
	
	public void freeItem(int idx){
		try{
			mLock.writeLock();
			mTotalTextLength -= ((TextItemBase)((mItemList).get(idx))).length();
			mItemList.freeItem(idx);
		} catch(Exception e){
			Lg.stack(TAG, e.getStackTrace());
		} finally {
			mLock.writeUnlock();
		}
	}
	
	@Override
	public void onUpdate()
	{
		// TODO: Implement this method
	}

	@Override
	public int getTextureId()
	{
		return R.drawable.font;
	}

	@Override
	public int getTextureCount()
	{
		return 1;
	}
	
	public int updateLength(){
	
		int total =0;
		try{
			mLock.writeLock();
			for(int i=0,n=mItemList.size(); i<n; i++){
				TextItemBase item = (TextItemBase)mItemList.get(i);
				total += item.length();
			}
		} catch (Exception e){
			
		}finally{
			mLock.writeUnlock();
		}
		
		mTotalTextLength = total;
		return total;
	}
}
