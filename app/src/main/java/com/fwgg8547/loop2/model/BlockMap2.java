package com.fwgg8547.loop2.model;

import com.fwgg8547.loop2.GameConfig;
import com.fwgg8547.loop2.generater.BlockGenerater;

import java.security.*;

public class BlockMap2
{
	private static final String TAG = BlockMap2.class.getSimpleName();

	private BlockItem[] mTopItems;
	private BlockItem[] mBottomItems;
	private BlockItem[] mRightItems;
	private BlockItem[] mLeftItems;
	
	public static final BlockMap2 INSTANCE = new BlockMap2();
	
	private BlockMap2(){}
	
	public void initialize(){
		mTopItems = new BlockItem[GameConfig.MAPINITIALW];
		mBottomItems = new BlockItem[GameConfig.MAPINITIALW];
	}
	
	public void setTop(BlockItem[] top){
		System.arraycopy(top, 0, mTopItems, 0, mTopItems.length);
	}
	
	public BlockItem[] getTop(){
		return mTopItems;
	}
}
