package com.fwgg8547.loop2.gamebase.modelbase;

import com.fwgg8547.loop2.gamebase.util.Lg;
import com.fwgg8547.loop2.gamebase.modelbase.*;
import android.graphics.RectF;
import android.graphics.PointF;

public class TextItemBase extends ItemBase
{	
	private final static String TAG = TextModelBase.class.getSimpleName();
	private NotifyUpdate mCallbak;
	
	public interface NotifyUpdate{
		public void textUpdated();
	}
	
	public TextItemBase(){
		
	}
	
	public void clear(){
		mCallbak = null;
		super.clear();
	}
	
	public void addCallback(NotifyUpdate cb){
		mCallbak = cb;
	}
	
	public int length(){
		return ((TextSprite)mSprite).getText().length();
	}
	
	public void setText(String s){
		((TextSprite)mSprite).setText(s);
		mCallbak.textUpdated();
	}
	
	@Override
	protected PointF doMoveMotion(){
		Lg.w(TAG, "no implementation");
		return null;
	}
	
	/*
	@Override
	public float[] getVertices(){
		return mSprite.getTransformedVertices();
	}

	@Override
	public float[] getColors(){
		return mSprite.getColors();
	}

	@Override
	public float[] getUvs(){
		return mSprite.getUvs();
	}

	@Override
	public short[] getIndices(){
		return mSprite.getIndices();
	}
	*/
}
