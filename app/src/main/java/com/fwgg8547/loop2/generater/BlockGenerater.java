package com.fwgg8547.loop2.generater;

import com.fwgg8547.loop2.gamebase.util.Lg;
import com.fwgg8547.loop2.gamebase.sequencerbase.ItemGeneraterBase;
import com.fwgg8547.loop2.gamebase.modelbase.SpriteModel;
import com.fwgg8547.loop2.model.BlockModel;
import com.fwgg8547.loop2.R;

import android.content.Context;

import java.util.*;
import com.fwgg8547.loop2.model.*;
import android.text.style.*;
import android.text.*;
import com.fwgg8547.loop2.*;
import android.graphics.PointF;

public class BlockGenerater 
extends ItemGeneraterBase
implements BlockMap.Generater
{
	private final static String TAG = BlockGenerater.class.getSimpleName();
	private final static int GENERATEPRIOD = 60;
	private boolean mIsReady=false;
	private boolean mPending=false;
	private BlockModel mBlockModel;
	BlockMap2 mBlockMap2;
	BlockCreatePattern mCreatePattern;
	List<BlockItem> mDeletedItem;
	Random mRand;
	
	public BlockGenerater(BlockModel m){
		super(m);
		mBlockModel = m;
		mDeletedItem = new ArrayList<BlockItem>();
		mBlockMap2 = BlockMap2.INSTANCE;
		mBlockMap2.initialize();
		mCreatePattern = new BlockCreatePattern();
		mCreatePattern.initialize();
		mSequenceEnd = false;
		mPending = false;
		mRand = new Random();
	}

	@Override
	public void clear()
	{
		super.clear();
		if(mDeletedItem != null){
			mDeletedItem.clear();
		}
		mBlockMap2=null;
		mDeletedItem = null;
		mBlockModel = null;
		mIsReady = false;
	}
	
	@Override
	public void loadSequence(Context ctx, int level){
		mCounter = 0;
		mSqIndex = 0;
		mSq = null;
		mRepeat = 0;
		mSequenceEnd = false;
		
	}
	
	@Override
	public void tick(){
		if(mSequenceEnd || !mIsAutoMode){
			return;
		}
		mCounter++;
		
		if(mCounter >= GENERATEPRIOD){
			mCounter=0;
			addNewLines();
		}
		
	}
	
	private void createBoard(){
		int indx=0;
		for(int i=0; i<BlockMap.MAPHEIGHT-1; i++){
			indx+=BlockMap.MAPOFFSETW;
			for(int j=0; j<BlockMap.MAPINITIALW; j++){
				BlockItem itm = (BlockItem)mBlockModel.createItem(indx);
				indx++;
			}
			indx+=BlockMap.MAPOFFSETW;
		}
		
		// top line
		BlockItem[] tmp = new BlockItem[BlockMap2.MAPINITIALW];
		indx+=BlockMap.MAPOFFSETW;
		for(int j=0; j<BlockMap2.MAPINITIALW; j++){
			tmp[j] = (BlockItem)mBlockModel.createItem(indx);
			indx++;
		}
		mBlockMap2.setTop(tmp);
	}

	@Override
	public void createInitialItem()
	{
		createBoard();
		mIsReady=true;
	}

	@Override
	public BlockItem getNextItem(int pattern)
	{
		return (BlockItem)mBlockModel.createItem(pattern);
	}
	
	@Override
	public BlockItem getNextItem(int pattern, float y){
		return (BlockItem)mBlockModel.createItem(pattern, y);
	}
	
	private void addNewLines(){
		Lg.i(TAG, "add new lines current");
	
		List<Integer> poslist = mCreatePattern.getNextCreatePosition();
		Lg.i(TAG,"new line size is " + poslist.size());
		
		// get current y position of block
		PointF beforePos = null;
		BlockItem[] tmp = mBlockMap2.getTop();
		for (BlockItem bi : tmp){
			if (bi != null) {
				beforePos = bi.getPosition();
				Lg.i(TAG, "current block Y position is " + beforePos.y);				break;
			}
		}
		
		// create new blocks
		tmp = new BlockItem[BlockMap2.MAPINITIALW];
		if(beforePos != null && poslist != null && poslist.size() > 0){
			Iterator ite = poslist.iterator();
			while(ite.hasNext()){
				int pos = (Integer) ite.next();
				BlockItem b = getNextItem(pos, beforePos.y+BatModel.WIDTH*2);
				tmp[pos] = b;
			}
			mBlockMap2.setTop(tmp);
		}
	}
	
	@Override
	public void loadLevel(Context ctx){
		mLevel = ResourceFileReader.getLevelThreash();
	}
	
	public void free(){	
	}
	
}
