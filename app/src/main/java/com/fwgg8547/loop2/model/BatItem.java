package com.fwgg8547.loop2.model;
import com.fwgg8547.loop2.gamebase.modelbase.*;

public class BatItem extends CollidableItem
{
	private static final String TAG = BatItem.class.getSimpleName();
	
	public enum Type {
		RIGHT,
		LEFT
	}
	
	private Type mType;
	
	public BatItem(){}
	
	public void setBatType(Type t){
		mType = t;
	}
	
	public Type getBatType(){
		return mType;
	}
}
