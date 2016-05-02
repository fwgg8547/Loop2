package com.fwgg8547.loop2.model;

import com.fwgg8547.loop2.gamebase.modelbase.CollidableItem;

public class BlockItem extends CollidableItem
{
	private static final String TAG = BlockItem.class.getSimpleName();
	private Type mType;
	
	public enum Type{
		BLACK,
		WHITE,
	}
	
	public BlockItem(){
		super();
	}
	
	public void setBlockType(int t){
		switch(t){
			case 0:
				mType = Type.BLACK;
				mSprite.setColor(new float[]{1,1,1,1 });
				break;
			default :
				mType = Type.WHITE;
				mSprite.setColor(new float[]{1,0,0,1 });
		}
	}
	
	public int getBlockType(){
		switch(mType){
			case BLACK:
				return 0;
			default:
				return 1;
		}
	}
}
