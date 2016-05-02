package com.fwgg8547.loop2.generater;
import com.fwgg8547.loop2.model.*;
import com.fwgg8547.loop2.gamebase.util.*;
import java.util.*;

public class BlockCreatePattern
{
	private static final String TAG = BlockCreatePattern.class.getSimpleName();
	private BlockMap mBlockMap;
	private int mCreateStage;
	private BlockMap.Direction mCreateDirection;
	private Random mRand;
	
	private class MapStatus{
		//public int mHeight;
		//public int mWidth;
		//public int mInitW;
		int mTopLine;
		int mBottomLine;
		
	}
	
	public BlockCreatePattern(){
		
	}
	
	public void initialize(BlockMap map){
		mBlockMap = map;
		mRand = new Random();
	}
	
	public List<Integer> getNextCreatePosition(){
		
		MapStatus ms = new MapStatus();
		//ms.mHeight = BlockMap.MAPHEIGHT;
		//ms.mWidth = BlockMap.MAPWIDTH;
		//ms.mInitW = BlockMap.MAPINITIALW;
		ms.mTopLine = mBlockMap.getTopLine();
		ms.mBottomLine = mBlockMap.getBottomLine();
		
		List<Integer> poslist = null;
		if(mCreateStage == 0){
			// add upper
			int size = mRand.nextInt(2);
			poslist = addBlockAtTop(ms, size);
			if(poslist.size() > 0){
				mCreateStage=1;
			}
		} else {
			mCreateDirection = (mRand.nextInt(2) ==0)? BlockMap.Direction.Left:BlockMap.Direction.Right;
			
			// add horizontal
			poslist = addBlockAtTopWidth(ms);
			if(poslist.size() > 0){
				mCreateStage=0;
			}
		}
		
		return poslist;
	}
	
	private List<Integer> addBlockAtTop(MapStatus ms, int size){
		List<Integer> poslist = new ArrayList<Integer>();
		if(ms.mTopLine >= BlockMap.MAPHEIGHT-1){
			Lg.i(TAG, "top line is max");
			return poslist;
		}
		Lg.i(TAG, "current top="+ms.mTopLine);
		
		int ml = mBlockMap.getTopLine1stIndex() + BlockMap.MAPOFFSETW-1;
		int mr = ml+BlockMap.MAPINITIALW;
		
		int count = mRand.nextInt(BlockMap.MAPINITIALW-1);
		for(int i=0; i<BlockMap.MAPWIDTH;i++){
			int test = ml+count+i;
			if(mBlockMap.isExist(test)){
				poslist.add(mBlockMap.getOneLineAbove(test));
				poslist.add(mBlockMap.getOneLineAbove(test+1));
				
				Lg.i(TAG,"stage 0 " +mBlockMap.getOneLineAbove(test) );
				Lg.i(TAG,"added line  = "+mBlockMap.getLineIndex(test));
				
				break;
			}
		}
		
		return poslist;
	}
	
	private List<Integer> addBlockAtTopWidth(MapStatus ms){
		List<Integer> poslist = new ArrayList<Integer>();
		if(ms.mTopLine >= BlockMap.MAPHEIGHT-1){
			Lg.i(TAG, "top line is max");
			return poslist;
		}
		
		int ml = mBlockMap.getLine1stIndex(ms.mTopLine+1)+BlockMap.MAPOFFSETW;
		
		for(int i=0; i<BlockMap.MAPINITIALW; i++){
			poslist.add(ml+i);
		}
		
		return poslist;
	}
	
	
}
