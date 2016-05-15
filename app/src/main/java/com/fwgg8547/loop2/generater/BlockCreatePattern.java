package com.fwgg8547.loop2.generater;
import com.fwgg8547.loop2.GameConfig;
import com.fwgg8547.loop2.model.*;
import com.fwgg8547.loop2.gamebase.util.*;
import java.util.*;
import com.fwgg8547.loop2.*;

public class BlockCreatePattern
{
	private static final String TAG = BlockCreatePattern.class.getSimpleName();
	private int mCreateStage;
	private int mPatternIndex;
	private int mPatternType;
	private int mPrevioursPos;
	private Random mRand;
	
	public class CreateInfo {
		int mPos;
		int mType;
		
		public CreateInfo(int p, int t){
			mPos = p;
			mType = t;
		}
	}
	private static final int[][] p1 = {
		{0},
		{0,1},
		{1,2},
		{2,3},
		{3,4},
		{4},
		{0,1,2,3,4}
	};
	
	private static final int[][] p2 = {
		{2},
		{2},
		{0,1,2,3,4}
	};
	
	private static final int[][] p3 = {
		{4},
		{3,4},
		{2,3},
		{1,2},
		{0,1},
		{0},
		{0,1,2,3,4}
	};
	
	private static final int[][] p4 = {
		{3},
		{3},
		{0,1,2,3,4}
	};
	
	public BlockCreatePattern(){}
	
	public void initialize(){
		mPrevioursPos = 0;
		mPatternIndex = 0;
		mPatternType = 0;
		mRand = new Random();
	}
	
	public List<CreateInfo> getNextCreatePosition(ScrollManager.Direct d){
		
		List<CreateInfo> poslist = null;
		if(mCreateStage == 0){
			Lg.i(TAG, "Create Stage 0");
			// add upper
			poslist = addBlockAt(d);
			/*
			if(poslist.size() > 0){
				mCreateStage=1;
			}
			*/
		} 
		
		return poslist;
	}
	
	private List<CreateInfo> addBlockAt(ScrollManager.Direct d){
		List<CreateInfo> poslist  = null;
		
		switch(d){
			case DOWN:
				//poslist = addBlockAtTop();
				poslist = addBlockByPattern();
				break;
			case RIGHT:
				poslist = addBlockAtRight();
				break;
		}
		
		return poslist;
	}
	
	private List<CreateInfo> addBlockAtRight(){
		List<CreateInfo> poslist = new ArrayList<CreateInfo>();

		BlockItem[] before = BlockMap2.INSTANCE.getRight();
		if (before == null) {
			return poslist;
		}

		// create block num
		int count = mRand.nextInt(2)+1;
		int created = 0;
		for(int i = 0; i< GameConfig.MAPHEIGHT; i++){
			if( mRand.nextBoolean() && before[i] != null && mPrevioursPos != i){
				CreateInfo c = new CreateInfo(i, 0);
				poslist.add(c);
				mPrevioursPos = i;
				created++;
			}
			if( created >= count) {
				break;
			}
		}

		Lg.i(TAG, "created block count is " + poslist.size());
		return poslist;
	}
	
	private List<CreateInfo> addBlockByPattern(){
		List<CreateInfo> poslist = new ArrayList<CreateInfo>();
		
		switch(mPatternType){
			case 0:
				for(int i: p1[mPatternIndex++]){
					CreateInfo c = new CreateInfo(i, 0);
					poslist.add(c);
				}

				if(mPatternIndex>=p1.length){
					mPatternIndex=0;
					mPatternType=1;
				}
				
				break;
				
			case 1:
				for(int i: p2[mPatternIndex++]){
					CreateInfo c = new CreateInfo(i, 0);
					poslist.add(c);
				}

				if(mPatternIndex>=p2.length){
					mPatternIndex=0;
					mPatternType = 2;
				}
				
				
				break;
				
				
			case 2:
				for(int i: p3[mPatternIndex++]){
					CreateInfo c = new CreateInfo(i, 0);
					poslist.add(c);
				}

				if(mPatternIndex>=p3.length){
					mPatternIndex=0;
					mPatternType=3;
				}
				break;
				
			case 3:
				for(int i: p4[mPatternIndex++]){
					CreateInfo c = new CreateInfo(i, 0);
					poslist.add(c);
				}

				if(mPatternIndex>=p4.length){
					mPatternIndex=0;
					mPatternType=0;
				}
				break;
		}
		
		return poslist;
	}
	
	/*
	private List<Integer> addBlockAtTop(){
		List<Integer> poslist = new ArrayList<Integer>();
		
		BlockItem[] before = BlockMap2.INSTANCE.getTop();
		if (before == null) {
			return poslist;
		}
		
		// create block num
		int count = mRand.nextInt(2)+1;
		int created = 0;
		for(int i = 0; i< GameConfig.MAPINITIALW; i++){
			if( mRand.nextBoolean() && before[i] != null && mPrevioursPos != i){
				poslist.add(i);
				mPrevioursPos = i;
				created++;
			}
			if( created >= count) {
				break;
			}
		}
		
		Lg.i(TAG, "created block count is " + poslist.size());
		return poslist;
	}
	
	private List<Integer> addBlockAtWidth(ScrollManager.Direct d){
		List<Integer> poslist = new ArrayList<Integer>();
		int size = 0;
		if(d == ScrollManager.Direct.DOWN || d == ScrollManager.Direct.UP){
			size = GameConfig.MAPINITIALW;
		} else {
			size = GameConfig.MAPHEIGHT;
		}
		for(int i=0; i<size; i++){
			poslist.add(i);
		}
		
		return poslist;
	}
	*/
	
}
