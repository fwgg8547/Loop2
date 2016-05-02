package com.fwgg8547.loop2.gamebase.modelbase;

public class UserData
{
	private CollidableItem mItem;
	private boolean mIsRect;

	public UserData(){
		mIsRect = false;
	}

	public void setIsRect(boolean is){
		mIsRect = is;
	}

	public boolean getIsRect(){
		return mIsRect;
	}

	public void setItem(CollidableItem item){
		mItem = item;
	}
	
	public CollidableItem getItem(){
		return mItem;
	}
	
}
