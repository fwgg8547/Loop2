package com.fwgg8547.loop2;
import java.util.*;

public class ScrollManager
{
	public enum Direct {
		UP,
		DOWN,
		LEFT,
		RIGHT,
		NONE
	}
	
	private	Random mRand;
	
	public ScrollManager(){
		mRand = new Random();
		
	}
	
	
	public Direct getNextDirection() {
		int i = mRand.nextInt(2);
		Direct d = Direct.DOWN;
		switch(i){
			case 0:
				d = Direct.DOWN;
				break;
			
			case 1:
				d = Direct.RIGHT;
				break;
		}
		return d;
	}
/*
	
	*/
}
