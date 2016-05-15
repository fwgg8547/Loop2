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
		mRightItems = new BlockItem[GameConfig.MAPHEIGHT];
		mLeftItems = new BlockItem[GameConfig.MAPHEIGHT];
		mBottomItems = new BlockItem[GameConfig.MAPINITIALW];
	}
	
	public void setTop(BlockItem[] top){
		System.arraycopy(top, 0, mTopItems, 0, mTopItems.length);
	
		//
		for(int i=1; i < GameConfig.MAPHEIGHT; i++){
			mRightItems[i-1] = mRightItems[i];
		}
		mRightItems[GameConfig.MAPHEIGHT-1] = mTopItems[GameConfig.MAPINITIALW-1];
	}
	
	public BlockItem[] getTop(){
		return mTopItems;
	}
	
	public void setRight(BlockItem[] right){
		System.arraycopy(right, 0, mRightItems, 0, mRightItems.length);
		
	}

	public BlockItem[] getRight(){
		return mRightItems;
	}
	
	
}
