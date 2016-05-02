package com.fwgg8547.loop2.gamebase.sequencerbase;

import com.fwgg8547.loop2.gamebase.modelbase.ItemBase;
import java.util.List;
import java.util.*;

public class SequencerBase<T extends SequenceItemBase>
{
	private final int MAX_TICK_COUNT = 60000;
	private int mTick;
	private int mIndex;
	private boolean mStart;
	private T[] mSequence;
	private List<CallbackItem> mCallbacks;
	
	public interface Callback{
		abstract public void notifyNext();
		abstract public void notifyFinish(ItemBase i, int type);
	}
	
	public class CallbackItem{
		public Callback mCallback;
		public ItemBase mItemBase;
	}
	
	public SequencerBase(){
		mTick = 0;
		mIndex = 0;
		mStart = false;
		mSequence = null;
		mCallbacks = new ArrayList<CallbackItem>();
	}
	
	public void setSequenceItem(T[] items){
		mSequence = items;
	}
	
	public void setCallback(Callback cb, ItemBase i){
		if(cb != null){
			CallbackItem cbi = new CallbackItem();
			cbi.mCallback = cb;
			cbi.mItemBase = i;
			mCallbacks.add(cbi);
		}
	}
	
	public void initialize(){
		
	}
	
	public void start(){
		mStart = true;
	}
	
	public void stop(){
		mStart = false;
	}
	
	public T getCurrentSequenceItem(){
		return mSequence[mIndex];
	}
	
	public void onUpdate(){
		if(!mStart){
			return;
		}
		mTick++;
		
		if (mSequence != null 
				&& mTick != 0
				&& mSequence[mIndex].mTick != 0
				&&(mTick % mSequence[mIndex].mTick) == 0){
			mIndex++;
			if (mIndex >= mSequence.length) {
				mIndex = 0;
			}

			for(int i=0, n=mCallbacks.size();i<n;i++){
				CallbackItem cbi = mCallbacks.get(i);
				cbi.mCallback.notifyNext();
			}
			
			if (mSequence[mIndex].mTick < 0){
				mStart = false;
				for(int i=0, n=mCallbacks.size();i<n;i++){
					CallbackItem cbi = mCallbacks.get(i);
					cbi.mCallback.notifyFinish(cbi.mItemBase, 0);
				}
			}
		}
		
		if(mTick >= MAX_TICK_COUNT){
			mTick = 0;
		}
	}
}
