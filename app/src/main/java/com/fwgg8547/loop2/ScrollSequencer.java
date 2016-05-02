package com.fwgg8547.loop2;
import java.util.*;
import com.fwgg8547.loop2.gamebase.modelbase.*;
import com.fwgg8547.loop2.gamebase.util.*;
import com.fwgg8547.loop2.gamebase.sequencerbase.*;

public class ScrollSequencer
{
	private List<ScrollableModel> mScrollModels;
	
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
	}
	
	public void addModel(ScrollableModel m){
		mScrollModels.add(m);
	}
	
	public void onUpdate(){
		doScrollAnimation();
	}
	
	private void doScrollAnimation(){
		Iterator ite = mScrollModels.iterator();
		while(ite.hasNext()){
			ScrollableModel m = (ScrollableModel)ite.next();
			m.moveScroll();
		}
	}
	
	private void changeScrollDirection(){
		//todo
	}
	
}
