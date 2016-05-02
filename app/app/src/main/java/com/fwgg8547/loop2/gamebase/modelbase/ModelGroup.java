package com.fwgg8547.loop2.gamebase.modelbase;

import java.util.*;

public class ModelGroup
{
	public int mId;
	public List<ItemBase> mGroup;
	
	public ModelGroup(){
		mGroup = new ArrayList<ItemBase>();
	}
	
	public Iterator iterator()
	{
		return mGroup.iterator();
	}
	
	public void add(ItemBase i){
		mGroup.add(i);
	}
	
	public int size() {
		return mGroup.size();	
	}
}
