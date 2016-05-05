package com.fwgg8547.loop2.model;

import com.fwgg8547.loop2.generater.BlockGenerater;

import java.security.*;public class BlockMap2
{
	private static final String TAG = BlockMap2.class.getSimpleName();

	public static final int MAPHEIGHT=10;
	public static final int MAPOFFSETW=0;
	public static final int MAPINITIALW = 7;
	public static final int MAPWIDTH=MAPINITIALW + MAPOFFSETW*2;
	
	private BlockItem[] mTopItems;
	private BlockItem[] mBottomItems;
	private BlockItem[] mRightItems;
	private BlockItem[] mLeftItems;
	
	public static final BlockMap2 INSTANCE = new BlockMap2();
	
	private BlockMap2(){}
	
	public void initialize(){
		mTopItems = new BlockItem[MAPINITIALW];
		mBottomItems = new BlockItem[MAPINITIALW];
	}
	
	public void setTop(BlockItem[] top){
		System.arraycopy(top, 0, mTopItems, 0, mTopItems.length);
	}
	
	public BlockItem[] getTop(){
		return mTopItems;
	}
}
