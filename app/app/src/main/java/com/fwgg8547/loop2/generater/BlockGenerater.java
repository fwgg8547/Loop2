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
	BlockMap mBlockMap;
	BlockCreatePattern mCreatePattern;
	List<BlockItem> mDeletedItem;
	Random mRand;
	
	public BlockGenerater(BlockModel m){
		super(m);
		mBlockModel = m;
		mDeletedItem = new ArrayList<BlockItem>();
		mBlockMap = BlockMap.getInstance();
		mCreatePattern = new BlockCreatePattern();
		mBlockMap.initialize(this);
		mCreatePattern.initialize(mBlockMap);
		mSequenceEnd = false;
		mPending = false;
		mRand = new Random();
	}

	@Override
	public void clear()
	{
		super.clear();
		if(mBlockMap != null){
			mBlockMap.clear();
		}
		mBlockMap=null;
		if(mDeletedItem != null){
			mDeletedItem.clear();
		}
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
		if(mDeletedItem.size() > 0){
			mBlockModel.deleteItem(mDeletedItem);
			mDeletedItem.clear();
			addNewLines();
		}
		
		if(mPending){
			if(mBlockModel.isMoving()){
				Lg.d(TAG,"create pend");
				mPending = true;
			} else {
				//addTopLine();
				Lg.i(TAG, "create !");
				addNewLines();
				mPending = false;
			}
		}
		
		if(mCounter >= GENERATEPRIOD){
			mCounter=0;
			
			if(GameConfig.BLOCKDELETE != 0 && mBlockModel.size() > 0){
				deleteBottomLine();
			}	
			
			
		}
	}
	
	private void createBoard(){
		int indx=0;
		for(int i=0; i<BlockMap.MAPHEIGHT; i++){
			indx+=BlockMap.MAPOFFSETW;
			for(int j=0; j<BlockMap.MAPINITIALW; j++){
				BlockItem itm = (BlockItem)mBlockModel.createItem(indx);
				mBlockMap.addMap(indx, itm);
				indx++;
			}
			indx+=BlockMap.MAPOFFSETW;
		}
		mBlockMap.refesh();
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
	
	public boolean isBlockAboveBatt(){
		return mBlockMap.isBlockAboveBatt();
	}
	
	public boolean isBlockBelowBatt(){
		return mBlockMap.isBlockBelowBatt();
	}
	
	public boolean isBlockRightBatt(){
		return mBlockMap.isBlockRightBatt();
	}
	
	public boolean isBlockLeftBatt(){
		return mBlockMap.isBlockLeftBatt();
	}
	
	public void setBattPosition(int x, int y){
		mBlockMap.setBattPosition(y, x);
	}
	
	public void setBattRotateDirect(float r){
		mBlockMap.setBattRotateDirect(r);
	}
	
	public List<BlockItem> shiftDown(){
		if(GameConfig.BLOCKSCROLL != 0){
			mDeletedItem.addAll( mBlockMap.moveDown());
			mPending = true;
		} else{
			mBlockMap.moveDown();
		}
		
		return null;
	}
	
	public List<BlockItem> shiftleft(){
		if(GameConfig.HORIZSCROLL !=0){
			mDeletedItem.addAll( mBlockMap.moveright());
			mPending = true;
		} else{
			mBlockMap.battMoveLeft();
		}
		return null;
	}
	
	public List<BlockItem> shiftRight(){
		if(GameConfig.HORIZSCROLL != 0){
			mDeletedItem.addAll( mBlockMap.moveleft());
			mPending = true;
		} else {
			mBlockMap.battMoveRight();
		}
		return null;
	}
	
	public List<BlockItem> shiftUp(){
		// todo
		return null;
	}
	
	public void addTopLine(){
		Lg.i(TAG, "add top line");
		mBlockMap.addTopLine();
	}

	private void addNewLines(){
		Lg.i(TAG, "add new lines current");
	
		List<Integer> poslist = mCreatePattern.getNextCreatePosition();
		Lg.i(TAG,"new " + poslist.size());
		
		if(poslist != null && poslist.size() > 0){
			Iterator ite = poslist.iterator();
			while(ite.hasNext()){
				int pos = (Integer) ite.next();
				int belowLine = mBlockMap.getLineIndex(pos) -1;
				if (mBlockMap.getBlockNumOfLine(belowLine) >0){
					for(int i=mBlockMap.getLine1stIndex(belowLine); i<mBlockMap.getLineEndIndex(belowLine); i++){
						if(mBlockMap.isExist(i)){
							PointF position = mBlockMap.getBlock(i).getPosition();
							Lg.i(TAG,"addmap2 y position " +position.y+BatModel.WIDTH*2);
							BlockItem b = getNextItem(pos, position.y+BatModel.WIDTH*2);
							mBlockMap.addMap(pos, b);
							break;
						}
					}
				} else {
					BlockItem b = getNextItem(pos);		
					mBlockMap.addMap(pos,b);
				}
			}
			mBlockMap.refesh();
		}
	}
	
	
	private void deleteBottomLine(){
		List<BlockItem> deleted = mBlockMap.deleteBottomLine();
		if(deleted.size() >0){
			Lg.i(TAG, "mcounter " +mCounter);
			mCounter = 0;
			mBlockModel.deleteItem(deleted);
		}
	}
	
	@Override
	public void loadLevel(Context ctx){
		mLevel = ResourceFileReader.getLevelThreash();
	}
	
	public void free(){	
	}
	
	
}
