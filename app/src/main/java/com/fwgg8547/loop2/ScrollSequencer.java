package com.fwgg8547.loop2;
import java.util.*;
import com.fwgg8547.loop2.gamebase.modelbase.*;
import com.fwgg8547.loop2.gamebase.util.*;
import com.fwgg8547.loop2.gamebase.sequencerbase.*;
import com.fwgg8547.loop2.gamebase.scorebase.*;

public class ScrollSequencer
{
	public static final Vec2 DOWN  = new Vec2(0, GameConfig.SCROLLVELOCITY);
	public static final Vec2 RIGHT = new Vec2(GameConfig.SCROLLVELOCITY, 0);
	
	public static final Vec2 LEFT  = new Vec2(-1*GameConfig.SCROLLVELOCITY, 0);
	public static final Vec2 UP    = new Vec2(0, -1*GameConfig.SCROLLVELOCITY);
	public static final Vec2 NONE    = new Vec2(0, 0);
	
	private List<ScrollableModel> mScrollModels;
	private float mScrollDelta;
	private boolean mIsOneWidth;
	private static ScrollManager.Direct mCurrent = ScrollManager.Direct.DOWN;
	private static ScrollManager.Direct mPrev = ScrollManager.Direct.DOWN;
	private int mBreak;
	private boolean mChangeDirection;
	
	public class ScrollSequence{
		private int frame;
		private Vec2 direct;
		
		public ScrollSequence(int frame, Vec2 direct){
			this.frame = frame;
			this.direct = direct;
		}
	}
	
	public ScrollSequencer(){}
	
	public void initilaize(){
		mScrollModels = new ArrayList<ScrollableModel>();
		mScrollDelta = 0;
		mBreak = 0;
		mIsOneWidth = false;
		mChangeDirection = false;
		mCurrent = ScrollManager.Direct.DOWN;
		mPrev = ScrollManager.Direct.DOWN;
		CollidableItem.setOffsetVect(DOWN);
	}
	
	public void addModel(ScrollableModel m){
		mScrollModels.add(m);
	}
	
	static public ScrollManager.Direct getDirection(){
		return mCurrent;
	}
	
	public boolean isBreak(){
		return (mBreak >0)? true: false;
	}

	public void setDirection(ScrollManager.Direct d){
		mCurrent = d;
	}
	
	public void changeDirectRequest(){
		//mChangeDirection = true;
	}
	
	public boolean onUpdate(){
		return doScrollAnimation();
	}
	
	private boolean doScrollAnimation(){
		boolean isBreakEnd=false;
		Iterator ite = mScrollModels.iterator();
		while(ite.hasNext()){
			ScrollableModel m = (ScrollableModel)ite.next();
			m.moveScroll();
		}
		
		if(mCurrent != ScrollManager.Direct.NONE){
			mScrollDelta += Math.abs( GameConfig.SCROLLVELOCITY);
		}
		
		if(mScrollDelta >= GameConfig.WIDTH*2){
			mIsOneWidth = true;
			CollidableItem.setOffsetVect(NONE);
			mPrev = mCurrent;
			mCurrent = ScrollManager.Direct.NONE;
			mScrollDelta = 0;
		}
		
		if(mIsOneWidth){
			mBreak++;
			if(mBreak > GameConfig.BREAKSCROLL){
				getNextDirection();
				mBreak = 0;
				mIsOneWidth = false;
				isBreakEnd=true;
			}
		}
		
		return isBreakEnd;
	}
	
	public boolean isChangeTiming(){
		return mIsOneWidth;
	}
	
	public void getNextDirection() {

		//CollidableItem.setOffsetVect(DOWN);
		
		
		if(mChangeDirection){
			if(mPrev == ScrollManager.Direct.DOWN){
				CollidableItem.setOffsetVect(RIGHT);
				mCurrent = ScrollManager.Direct.RIGHT;
			}
			mChangeDirection = false;
		} else {
			CollidableItem.setOffsetVect(getScrollVect(mPrev));
			mCurrent = mPrev;
			return;
		}
		
		return;
	}
	
	
	private Vec2 getScrollVect(ScrollManager.Direct d) {
		Vec2 v = null;
		switch(d){
			case DOWN:
				v = ScrollSequencer.DOWN;
				break;
			case RIGHT:
				v = ScrollSequencer.RIGHT;
				break;
			case LEFT:
				v = ScrollSequencer.LEFT;
				break;
			case UP:
				v = ScrollSequencer.UP;
				break;
		}
		return v;
	}
	
}
