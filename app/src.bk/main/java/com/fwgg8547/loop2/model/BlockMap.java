package com.fwgg8547.loop2.model;
import java.util.*;
import android.text.style.*;
import android.graphics.PointF;
import com.fwgg8547.loop2.gamebase.util.Lg;

public class BlockMap
{
	private static final String TAG = BlockMap.class.getSimpleName();
	
	public static final int MAPHEIGHT=10;
	public static final int MAPOFFSETW=0;
	public static final int MAPINITIALW = 7;
	public static final int MAPWIDTH=MAPINITIALW + MAPOFFSETW*2;
	private Generater mGenerater;
	private BlockItem[] mBlockItems;
	private int mBattItemPosition;
	private float mBattRDirect;
	private int mBottomLine;
	private int mTopLine;
	private static BlockMap instance;
	private Random mRand;
	private int mBlockCreateStage;
	private Direction mBlockCreateDirection;
	
	public enum Direction {
		Left,
		Right
	}
	
	public interface Generater {
		public BlockItem getNextItem(int pattern);
		public BlockItem getNextItem(int pattern, float y);
	}
	
	public static BlockMap getInstance(){
		if(instance == null){
			instance = new BlockMap();
		}
		
		return instance;
	}
	
	private BlockMap(){
		
	}
	
	public void initialize(Generater g){
		mGenerater = g;
		mBlockItems = new BlockItem[BlockModel.MAX_BLOCK];
		for(int i=0; i<mBlockItems.length; i++){
			mBlockItems[i]=null;
		}
		mRand = new Random();
	}
	
	public void clear(){
		mBlockItems = null;
		mGenerater = null;
		mBottomLine = 0;
		mTopLine = 0;
		instance = null;
		mBattItemPosition = 0;
		mBattRDirect =0;
		mBlockCreateStage = 0;
	}
	
	public int getTopLine(){
		return mTopLine;
	}
	
	public int getBottomLine(){
		return mBottomLine;
	}
	
	public BlockItem getBlock(int index){
		return mBlockItems[index];
	}
	
	static public int getOneLineAbove(int index){
		return index+MAPWIDTH;
	}

	static public int getOneLineBlow(int index){
		return index-MAPWIDTH;
	}	

	static public int getIndex(int line, int pos){
		return getLine1stIndex(line)+pos;
	}

	static public int getLineIndex(int pos){
		return pos/(MAPWIDTH);
	}
	
	static public int getLine1stIndex(int line){
		return line*(MAPWIDTH);
	}

	static public int getLineEndIndex(int line){
		return line*(MAPWIDTH)+MAPWIDTH-1;
	}
	
	public boolean isExist(int index){
		return (mBlockItems[index] != null)? true:false;
	}
	
	public int getTopLine1stIndex(){
		return getLine1stIndex(mTopLine);
	}

	public int getTopLineEndIndex(){
		return getLineEndIndex(mTopLine);
	}
	
	
	public void setBattPosition(int line, int pos){
		mBattItemPosition = getLine1stIndex(line) + pos;
		Lg.i(TAG, "bat pos = " + mBattItemPosition);
	}
	
	public void setBattRotateDirect(float r){
		mBattRDirect = r;
	}
	
	public void battMoveUp(){
		int tmp = getOneLineAbove(mBattItemPosition);
		mBattItemPosition = (getLineIndex(tmp) > MAPHEIGHT-1)? mBattItemPosition:tmp;
		Lg.i(TAG, "bat pos = " + mBattItemPosition);
	}
	
	public void battMoveDown(){
		int tmp = getOneLineBlow(mBattItemPosition);
		mBattItemPosition = (tmp < 0)? mBattItemPosition:tmp;
		Lg.i(TAG, "bat pos = " + mBattItemPosition);
	}
	
	public void battMoveLeft(){
		int tmp = mBattItemPosition - 1;
		mBattItemPosition = (tmp < getLine1stIndex(getLineIndex(mBattItemPosition)))? mBattItemPosition:tmp;
		Lg.i(TAG, "bat pos = " + mBattItemPosition);
	}
	
	public void battMoveRight(){
		int tmp = mBattItemPosition +1;
		mBattItemPosition = (tmp > getLineEndIndex(getLineIndex(mBattItemPosition)))? mBattItemPosition:tmp;
		Lg.i(TAG, "bat pos = " + mBattItemPosition);
	}
	
	synchronized public BlockItem addMap(int pos, BlockItem i){
		if(mBlockItems[pos]!= null){
			return mBlockItems[pos];
		}
		mBlockItems[pos] = i;
		return null;
	}
	
	synchronized public BlockItem addMap(int pos){
		Lg.i(TAG,"addmap pos " +pos);
		if(pos > MAPWIDTH*MAPHEIGHT-1){
			Lg.i(TAG, "requested pos is over");
			return null;
		}
		if(mBlockItems[pos]!= null){
			return mBlockItems[pos];
		}
		mBlockItems[pos] = mGenerater.getNextItem(pos);
		return null;
	}
	
	synchronized public BlockItem addMap2(int pos){
		Lg.i(TAG,"addmap2 pos " +pos);
		if(pos > MAPWIDTH*MAPHEIGHT-1){
			Lg.i(TAG, "requested pos is over");
			return null;
		}
		if(mBlockItems[pos]!= null){
			return mBlockItems[pos];
		}
		int belowLine = getLineIndex(pos) -1;
		if (getBlockNumOfLine(belowLine) >0){
			for(int i=getLine1stIndex(belowLine); i<getLineEndIndex(belowLine); i++){
				if(mBlockItems[i] != null){
					PointF position = mBlockItems[i].getPosition();
					Lg.i(TAG,"addmap2 y position " +position.y+BatModel.WIDTH*2);
					mBlockItems[pos] = mGenerater.getNextItem(pos, position.y+BatModel.WIDTH*2);
					break;
				}
			}
		} else {
			mBlockItems[pos] = mGenerater.getNextItem(pos);			
		}
		return mBlockItems[pos];
	}
	
	synchronized public BlockItem deleteMap(int pos){
		BlockItem i = mBlockItems[pos];
		mBlockItems[pos]=null;
		return i;
	}
	
	synchronized public void addTopLine(){
		if(mTopLine >= MAPHEIGHT-1){
			return;
		}
		for(int i=getLine1stIndex(mTopLine+1); i<= getLineEndIndex(mTopLine+1); i++){
			if(mBlockItems[i] == null){
				mBlockItems[i] = mGenerater.getNextItem(i);
				Lg.i(TAG, "add line " +i);
			}
		}
	}
	synchronized public void addOneLine(int line){
		for(int i=getLine1stIndex(line); i<= getLineEndIndex(line); i++){
			if(mBlockItems[i] == null){
				mBlockItems[i] = mGenerater.getNextItem(i);
				Lg.i(TAG, "add line " +i);
			}
		}
	}
	
	synchronized public List<BlockItem> moveleft(){
		List<BlockItem> deleted = new ArrayList<BlockItem>();
		BlockItem tmp=null;
		Lg.i(TAG, "moveright");
		
		for(int i=0; i< MAPHEIGHT; i++){
			tmp=mBlockItems[getLine1stIndex(i)];
			if(tmp != null){
				deleted.add(tmp);
				mBlockItems[i] = null;
			}
		}

		for(int j=0; j<MAPHEIGHT; j++){
			for(int i=0; i<MAPWIDTH-1; i++){
				mBlockItems[getLine1stIndex(j)+i]=
					mBlockItems[getLine1stIndex(j)+i+1];
			}
		}

		for(int i=0; i<MAPHEIGHT; i++){
			mBlockItems[getLineEndIndex(i)]=null;
			
		}

		refesh();
		Lg.i(TAG, "new top "+mTopLine);
		return deleted;
	}
	
	synchronized public List<BlockItem> moveright(){
		List<BlockItem> deleted = new ArrayList<BlockItem>();
		BlockItem tmp=null;
		Lg.i(TAG, "moveleft");

		for(int i=0; i< MAPHEIGHT; i++){
			tmp=mBlockItems[getLineEndIndex(i)];
			if(tmp != null){
				deleted.add(tmp);
				mBlockItems[i] = null;
			}
		}

		for(int j=0; j<MAPHEIGHT; j++){
			for(int i=MAPWIDTH-1; i>0; i--){
				mBlockItems[getLine1stIndex(j)+i]=
					mBlockItems[getLine1stIndex(j)+i-1];
			}
		}

		for(int i=0; i<MAPHEIGHT; i++){
			mBlockItems[getLine1stIndex(i)]=null;

		}

		refesh();
		Lg.i(TAG, "new top "+mTopLine);
		return deleted;
	}
	synchronized public List<BlockItem> moveDown(){
		
		List<BlockItem> deleted = new ArrayList<BlockItem>();
		BlockItem tmp=null;
		Lg.d(TAG, "movedown");
		for(int i=0; i<=getLineEndIndex(0); i++){
			tmp=mBlockItems[i];
			if(tmp != null){
				deleted.add(tmp);
				mBlockItems[i] = null;
			}
		}
		
		for(int j=0; j<MAPHEIGHT-1; j++){
			for(int i=0; i<MAPWIDTH; i++){
				mBlockItems[getLine1stIndex(j)+i]=
					mBlockItems[getLine1stIndex(j+1)+i];
			}
		}
		
		for(int i=0; i<MAPWIDTH; i++){
			mBlockItems[getLine1stIndex(MAPHEIGHT-1)+i]=null;
			Lg.d(TAG, "movedown " + (getLine1stIndex(MAPHEIGHT-1)+i));
		}
		
		refesh();
		Lg.i(TAG, "new top "+mTopLine);
		return deleted;
	}
	
	synchronized public List<BlockItem> deleteBottomLine(){
		List<BlockItem> deleted = new ArrayList<BlockItem>();
		for(int i=getLine1stIndex(mBottomLine);
		i<=getLineEndIndex(mBottomLine); i++){
			BlockItem tmp = mBlockItems[i];
			if(tmp != null){
				deleted.add(tmp);
				mBlockItems[i] = null;
			}
		}
		Lg.i(TAG,"dekete bottom " +mBottomLine+"|"+
		getLine1stIndex(mBottomLine) +"|"+ 
		getLineEndIndex(mBottomLine));
		mBottomLine+=1;
		return deleted;
	}
	
	synchronized public List<BlockItem> deleteTopLine(){
		List<BlockItem> deleted = new ArrayList<BlockItem>();
		for(int i=getLine1stIndex(mTopLine);
		i<=getLineEndIndex(mTopLine); i++){
			BlockItem tmp = mBlockItems[i];
			if(tmp != null){
				deleted.add(tmp);
				mBlockItems[i] = null;
			}
		}
		mTopLine-=1;
		return deleted;
	}
	
	synchronized public void addBlockAtTopByRand(){
		int b = getBlockNumOfLine(mTopLine);
		int index = 0;
		if(b > 0 && mTopLine < MAPHEIGHT-1){
			if(b==1){
				index =0;
			} else{
				index = mRand.nextInt(b);
				Lg.i(TAG, "rand " +index +"| "+b);
			}
			BlockItem[] top = getLine(mTopLine);
			int found = 0;
			for(int i=0; i<MAPWIDTH; i++){
				if(top[i] != null){
					if(found == index){
						Lg.i(TAG, "add " +(mTopLine+1)+"|"+i);
						addMap(getIndex(mTopLine+1, i));
						if(i>0){
							Lg.i(TAG, "addmap -1 " + (i-1));
							addMap(getIndex(mTopLine+1, i-1));
						}
						if(i<MAPWIDTH-1){
							Lg.i(TAG, "addmap +1 " + (i+1));
							addMap(getIndex(mTopLine+1, i+1));
						}
						updateTopAndBottom(mTopLine+1);
						break;
					}
					found++;
				}
			}
		} else {
			Lg.i(TAG, "add fail " +b +"|"+mTopLine);
			return;
		}
		
	}
	
	synchronized public void addBlockAtTopByZigZag(){
		//  *   // *    //  **
		// **   // **   //  *
		// * 　 //  *   // **
		if(mTopLine >= MAPHEIGHT-1){
			Lg.i(TAG, "top line is max");
			return;
		}
		Lg.i(TAG, "current top="+mTopLine+"| batt="+getLineIndex( mBattItemPosition));
		 // 0 is left
		
		if(mBlockCreateStage == 0){
			
			mBlockCreateDirection = (mRand.nextInt(2) == 0)? Direction.Left: Direction.Right;
			int index = blockEdgeOfLine(mTopLine, mBlockCreateDirection);
			if(addMap2(getOneLineAbove(index)) == null){
				Lg.i(TAG, "fail create ");
			} else {
				mBlockCreateStage++;
				Lg.i(TAG,"stage 0 " +getOneLineAbove(index) );
				Lg.i(TAG,"direction = "+mBlockCreateDirection);
			}
			
		} else if (mBlockCreateStage == 1){
			int index = blockEdgeOfLine(mTopLine, mBlockCreateDirection);
			int a = getOneLineAbove(index);
			int length = mRand.nextInt(MAPWIDTH-2) + 2;
			Lg.i(TAG, "create len ="+length);
			boolean canGoNext = true;
			for(int i=0; i<length; i++){
				if(mBlockCreateDirection == Direction.Right){
					if(a+i <= getLineEndIndex(mTopLine+1)){
						if(addMap2(a+i) == null){
							Lg.i(TAG, "fail create");
							if(i==0){
								canGoNext = false;
							}
						}
						Lg.i(TAG,"stage1 " + (a+i));
					} else {
						break;
					}
				} else{
					if(a-i >= getLine1stIndex( mTopLine+1)){
						if(addMap2(a-i) == null){
							Lg.i(TAG, "fail create");
							if(i==0){
								canGoNext = false;
							}
						}
						Lg.i(TAG,"stage1 " + (a-i));
					} else {
						break;
					}
				}
			}
			
			if(canGoNext) {
				mBlockCreateStage++;
			}
		} else {
			if(mBlockCreateDirection == Direction.Left){
				mBlockCreateDirection = Direction.Right;
			}
			int a = blockEdgeOfLine(mTopLine, mBlockCreateDirection);
			if(addMap2(getOneLineAbove(a)) == null){
				Lg.i(TAG, "fail create");
			}else {
				mBlockCreateStage=0;
				Lg.i(TAG,"stage2 " + getOneLineAbove(a));
			}
		}
		updateTopAndBottom(mTopLine+1);
		
		
	}
	
	synchronized public void addBlockAtTopByZigZag2(){
		//  *   // *    //  **
		// **   // **   //  *
		// * 　 //  *   // **
		if(mTopLine >= MAPHEIGHT-1){
			Lg.i(TAG, "top line is max");
			return;
		}
		Lg.i(TAG, "current top="+mTopLine+"| batt="+getLineIndex( mBattItemPosition));
		Direction edge = (mRand.nextInt(2) == 0)? Direction.Left:Direction.Right;
		int index = blockEdgeOfLine(mTopLine, edge);

		addMap2(getOneLineAbove(index));
		if(edge==Direction.Left){
			//left
			if(index == getLine1stIndex(mTopLine)){

			} else {
				addMap2(getOneLineAbove(index-1));
			}
		} else {
			if(index == getLineEndIndex(mTopLine)){

			} else {
				addMap2(getOneLineAbove(index+1));
			}
		}
		updateTopAndBottom(mTopLine+1);
	}
	
	public int blockEdgeOfTopLine(Direction rightLeft){
		return blockEdgeOfLine(mTopLine, rightLeft);
	}
	
	public int blockEdgeOfLine(int line, Direction rightLeft){
		int num = -1;
		
		if(rightLeft == Direction.Right){
			//right
			for(int i = getLineEndIndex(line);i >=getLine1stIndex(line); i--){
				if(mBlockItems[i]!=null){
					num=i;
				}
			}
		} else {
			//left
			for(int i = getLine1stIndex(line);i <=getLineEndIndex(line); i++){
				if(mBlockItems[i]!=null){
					num=i;
				}
			}
		}
		
		return num;
	}
	
	synchronized public boolean isBlockAboveBatt(){
		int i = getOneLineAbove(mBattItemPosition);
		if(i> getLine1stIndex(mTopLine)){
			return false;
		}
		return (mBlockItems[i]!=null)? true:false;
	}
	
	synchronized public boolean isBlockBelowBatt(){
		int i = getOneLineBlow(mBattItemPosition);
		if(i<getLineEndIndex(mBottomLine)){
			return false;
		}
		return (mBlockItems[i]!=null)? true:false;
	}
	
	synchronized public boolean isBlockRightBatt(){
		int i = mBattItemPosition +1;
		
		if(mBattItemPosition +1 > getLineEndIndex(getLineIndex(mBattItemPosition))){
			return false;
		}
		
		return (mBlockItems[i]!=null)? true:false;
	}
	
	synchronized public boolean isBlockLeftBatt(){
		int i = mBattItemPosition -1;

		if(mBattItemPosition -1 < getLine1stIndex(getLineIndex(mBattItemPosition))){
			return false;
		}

		return (mBlockItems[i]!=null)? true:false;
	}
	
	synchronized public int getBlockNumOfLine(int line){
		int num = 0;
		for(int i = getLine1stIndex(line);i <=getLineEndIndex(line); i++){
			if(mBlockItems[i]!=null){
				num++;
			}
		}
		return num;
	}
	
	private BlockItem[] getLine(int line){
		BlockItem[] tmp = new BlockItem[MAPWIDTH];
		int j=0;
		for(int i = getLine1stIndex(line); i<=getLineEndIndex(line);i++){
			tmp[j++] = mBlockItems[i];
		}
		
		return tmp;
	}
	
	
	public  void updateTopAndBottom(int line){
		mTopLine = (line>mTopLine)? line:mTopLine;
		mBottomLine = (line<mBottomLine)? line:mBottomLine;
		Lg.i(TAG,"update line " + mTopLine +"|"+mBottomLine);
	}
	
	public void refesh(){
		
		for(int i=0; i<mBlockItems.length;i++){
			if(mBlockItems[i] != null){
				mBottomLine = getLineIndex(i);
				Lg.i(TAG,"refresh " + i+"|"+mBottomLine);
				break;
			}
		}
		
		for(int i=mBlockItems.length-1;i>=0;i--){
			if(mBlockItems[i] != null){
				mTopLine = getLineIndex(i);
				Lg.i(TAG,"refresh " + mTopLine+"|"+i);
				break;
			}
		}
		
	}
}
