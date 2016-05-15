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
{
	private final static String TAG = BlockGenerater.class.getSimpleName();
	private final static int GENERATEPRIOD = 50;
	private boolean mIsReady=false;
	private boolean mPending=false;
	private BlockModel mBlockModel;
	BlockMap2 mBlockMap2;
	BlockCreatePattern mCreatePattern;
	List<BlockItem> mDeletedItem;
	Random mRand;
	
	private PointF mInitRightPos[];
	private PointF mInitLeftPos[];
	
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
		//mCounter++;
		
		if(mPending){
			mPending=false;
			addNewLines(ScrollSequencer.getDirection());
		}
		
	}
	
	public void createRequest(){
		mPending = true;
	}
	
	private void createBoard(){
		mInitLeftPos = new PointF[GameConfig.MAPHEIGHT];
		mInitRightPos = new PointF[GameConfig.MAPHEIGHT];
		BlockItem[] tmpR = new BlockItem[GameConfig.MAPHEIGHT];
		
		int indx=0;
		for(int i=0; i<GameConfig.MAPHEIGHT-1; i++){
			indx+=GameConfig.MAPOFFSETW;
			for(int j=0; j<GameConfig.MAPINITIALW; j++){
				BlockItem itm = (BlockItem)mBlockModel.createItem(indx);
				if(j==0){
					mInitLeftPos[i] = new PointF(itm.getPosition().x, itm.getPosition().y);
				} else if (j==GameConfig.MAPINITIALW-1){
					mInitRightPos[i] = new PointF(itm.getPosition().x, itm.getPosition().y);
					tmpR[i] = itm;
				}
				indx++;
			}
			indx+=GameConfig.MAPOFFSETW;
		}
		
		// top line
		BlockItem[] tmp = new BlockItem[GameConfig.MAPINITIALW];
		
		
		indx+=GameConfig.MAPOFFSETW;
		for(int j=0; j<GameConfig.MAPINITIALW; j++){
			tmp[j] = (BlockItem)mBlockModel.createItem(indx);
			if(j==0){
				mInitLeftPos[GameConfig.MAPHEIGHT-1] = 
					new PointF(tmp[j].getPosition().x, tmp[j].getPosition().y);
			} else if (j==GameConfig.MAPINITIALW-1){
				mInitRightPos[GameConfig.MAPHEIGHT-1] = 
					new PointF(tmp[j].getPosition().x, tmp[j].getPosition().y);
				tmpR[GameConfig.MAPHEIGHT-1] = tmp[j];
			}
			indx++;
		}
		mBlockMap2.setTop(tmp);
		mBlockMap2.setRight(tmpR);
	}

	@Override
	public void createInitialItem()
	{
		createBoard();
		mIsReady=true;
	}
	
	private void addNewLines(ScrollManager.Direct d){
		Lg.i(TAG, "add new lines current");

		List<BlockCreatePattern.CreateInfo> poslist = mCreatePattern.getNextCreatePosition(d);
		Lg.i(TAG,"new line size is " + poslist.size());

		// get current y position of block
		PointF beforePos = null;
		BlockItem[] tmp = null;
		
		switch(d){
			case DOWN:
				tmp = mBlockMap2.getTop();
				for (BlockItem bi : tmp){
					if (bi != null) {
						beforePos = bi.getPosition();
						Lg.i(TAG, "current block Y position is " + beforePos.y);				
						break;
					}
				}
				// create new blocks
				tmp = new BlockItem[GameConfig.MAPINITIALW];
				if(beforePos != null && poslist != null && poslist.size() > 0){
					Iterator ite = poslist.iterator();
					while(ite.hasNext()){
						int pos = ((BlockCreatePattern.CreateInfo) ite.next()).mPos;
						BlockItem b = (BlockItem)mBlockModel.createItem(pos, beforePos.y+GameConfig.WIDTH*2);
						tmp[pos] = b;
					}
					mBlockMap2.setTop(tmp);
				}
				break;
			
			case RIGHT:
				PointF offset = new PointF(0,0);
				tmp = mBlockMap2.getRight();
				// calc offset position
				/*
				for (BlockItem bi : tmp){
					if (bi != null) {
						beforePos = bi.getPosition();
						Lg.i(TAG, "current block X position is " + beforePos.x);				
						break;
					}
				}
				
				*/
				for (int i=0; i < tmp.length; i++){
					if(tmp[i] != null) {
						beforePos =tmp[i].getPosition();
						offset = new PointF(
							0,
							mInitRightPos[i].y - tmp[i].getPosition().y
						);
						Lg.i(TAG, "current block i X position is " + i + "|"+ tmp[i].getPosition().x);
						break;
					}
				}
				
				// create new blocks
				tmp = new BlockItem[GameConfig.MAPHEIGHT];
				if(poslist != null && poslist.size() > 0){
					Iterator ite = poslist.iterator();
					while(ite.hasNext()){
						int pos = ((BlockCreatePattern.CreateInfo) ite.next()).mPos;
						BlockItem b = (BlockItem)mBlockModel.createItem(
							mInitRightPos[pos].x + GameConfig.WIDTH*2, 
							mInitRightPos[pos].y);
						tmp[pos] = b;
					}
					mBlockMap2.setRight(tmp);
				}
				break;
		}
	}

	private void addNewLines(){
		Lg.i(TAG, "add new lines current");
	
		List<BlockCreatePattern.CreateInfo> poslist = mCreatePattern.getNextCreatePosition(ScrollManager.Direct.RIGHT);
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
		tmp = new BlockItem[GameConfig.MAPINITIALW];
		if(beforePos != null && poslist != null && poslist.size() > 0){
			Iterator ite = poslist.iterator();
			while(ite.hasNext()){
				int pos = ((BlockCreatePattern.CreateInfo) ite.next()).mPos;
				
				BlockItem b = (BlockItem)mBlockModel.createItem(pos, beforePos.y+GameConfig.WIDTH*2);
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
