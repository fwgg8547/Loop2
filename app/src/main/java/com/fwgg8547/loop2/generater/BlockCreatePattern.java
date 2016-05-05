package com.fwgg8547.loop2.generater;
import com.fwgg8547.loop2.model.*;
import com.fwgg8547.loop2.gamebase.util.*;
import java.util.*;

public class BlockCreatePattern
{
	private static final String TAG = BlockCreatePattern.class.getSimpleName();
	private int mCreateStage;
	private BlockMap.Direction mCreateDirection;
	private Random mRand;
	
	public BlockCreatePattern(){}
	
	public void initialize(){
		mRand = new Random();
	}
	
	public List<Integer> getNextCreatePosition(){
		
		List<Integer> poslist = null;
		if(mCreateStage == 0){
			Lg.i(TAG, "Create Stage 0");
			// add upper
			poslist = addBlockAtTop();
			if(poslist.size() > 0){
				mCreateStage=1;
			}
		} else {
			Lg.i(TAG, "Create Stage 1");
			mCreateDirection = (mRand.nextInt(2) ==0)? BlockMap.Direction.Left:BlockMap.Direction.Right;
			
			// add horizontal
			poslist = addBlockAtTopWidth();
			if(poslist.size() > 0){
				mCreateStage=0;
			}
		}
		
		return poslist;
	}
	
	private List<Integer> addBlockAtTop(){
		List<Integer> poslist = new ArrayList<Integer>();
		
		BlockItem[] before = BlockMap2.INSTANCE.getTop();
		if (before == null) {
			return poslist;
		}
		
		// create block num
		int count = mRand.nextInt(2)+1;
		int created = 0;
		for(int i=0; i<BlockMap.MAPINITIALW;i++){
			if( mRand.nextBoolean() && before[i] != null){
				poslist.add(i);
				created++;
			}
			if( created >= count) {
				break;
			}
		}
		
		Lg.i(TAG, "created block count is " + poslist.size());
		return poslist;
	}
	
	private List<Integer> addBlockAtTopWidth(){
		List<Integer> poslist = new ArrayList<Integer>();
		
		for(int i=0; i<BlockMap.MAPINITIALW; i++){
			poslist.add(i);
		}
		
		return poslist;
	}
	
	
}
