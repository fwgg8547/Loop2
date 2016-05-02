package com.fwgg8547.loop2.gamebase.modelbase;

import com.fwgg8547.loop2.gamebase.util.*;
import java.util.*;

public class ItemArray
{
	private final String TAG = "ItemArray";
	
	private ItemBase[] mItems;
	private int mFreeIndex;
	
	public ItemArray(){
	}
	
	public void initialize(ItemBase[] bs){
		mItems = bs;
		for(int i=0;i<bs.length; i++){
			mItems[i].mIsDeleted = true;
			mItems[i].mIndex = i;
		}
		mFreeIndex = 0;
		
		
	}
	
	public void clear(){
		mItems = null;
		mFreeIndex = 0;
	}
	
	public int size(){
		return (mFreeIndex);
	}
	
	public ItemBase get(int i){
		try{
			return mItems[i];
		} catch (Exception e){
			Lg.e(TAG, e.toString());
			Lg.e(TAG, "outof " +i);
		}
		return null;
	}
	
	public ItemBase getFreeItem(){
		try {
			if(mFreeIndex >= mItems.length){
				Lg.e(TAG, "no free item");
				return null;
			}
			
			return mItems[mFreeIndex++];
		}  catch(Exception e) {
			Lg.e(TAG, e.toString());
			Lg.w(TAG,"array overflow");
		}
		return null;
	}
	
	public void freeItem(int i){
		try {
			mItems[i].mIsDeleted = true;
			ItemBase tmp = mItems[i];
			
			mItems[i] = mItems[mFreeIndex-1];
			mItems[i].mIndex = i;
			mItems[mFreeIndex-1] = tmp;
			mItems[mFreeIndex-1].mIndex = mFreeIndex-1;
			mFreeIndex--;
		} catch (Exception e){
			Lg.e(TAG, e.toString());
			Lg.e(TAG, "out " +i +" | "+mFreeIndex);
		}
	}
}
