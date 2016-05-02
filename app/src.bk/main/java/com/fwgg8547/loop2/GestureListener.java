package com.fwgg8547.loop2;

import com.fwgg8547.loop2.model.*;
import com.fwgg8547.loop2.gamebase.util.*;
import com.fwgg8547.loop2.gamebase.layerbase.*;
import com.fwgg8547.loop2.gamebase.sequencerbase.*;
import com.fwgg8547.loop2.gamebase.modelbase.*;
import com.fwgg8547.loop2.gamebase.controllerbase.*;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.graphics.RectF;
import android.graphics.*;
import java.util.*;

public class GestureListener extends GestureListenerBase
{
	private static final String TAG = GestureListener.class.getSimpleName();
	private boolean mIsReady;
	private TrigerPoint mTriger;
	
	private class TrigerPoint{
		PointF mStart;
		PointF mStop;
		
		public float getStartX(){
			return mStart.x;
		}
		
		public float getStartY(){
			return mStart.y;
		}
	}
	
	public GestureListener(ReadersWriterLock lock){
		super(lock);
		mTriger = new TrigerPoint();
	}

	public void setListener(notifyEventListener ne){
		mNE = ne;
	}
	
	@Override
	public boolean onDown(MotionEvent event)
	{
		super.onDown(event);
		RectF screen = ScreenModel.getInstance().getScreenInfo();
		float ty1 = screen.height() - event.getY();
		Lg.i(TAG,"onDown "+event.getX()+"|"+ty1);
				
		
		if(mNE != null){
			mNE.onDown(new PointF(event.getX(), event.getY()));
		}
		return false;
	}

	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2, float vx, float vy)
	{
		super.onFling(event1,event2,vx,vy);
		Lg.i(TAG,"onFling " + vy);

		RectF screen = ScreenModel.getInstance().getScreenInfo();
		final float ty1 = screen.height() - event1.getY();
		final float ty2 = screen.height() - event2.getY();
		final float tx2 = event2.getX();
		return false;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent p1)
	{
		RectF screen = ScreenModel.getInstance().getScreenInfo();
		float ty1 = screen.height() - p1.getY();
		Lg.d(TAG, "onSingleTapUp " +p1.getX() +"|"+p1.getY());
		return super.onSingleTapUp(p1);
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e)
	{
		Lg.d(TAG, "onSingleTapConfirm");
		return super.onSingleTapConfirmed(e);
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e)
	{
		Lg.d(TAG, "onDoubleTapEvent");
		return super.onDoubleTapEvent(e);
	}

	@Override
	public boolean onDoubleTap(MotionEvent e)
	{
		Lg.d(TAG, "onDouleTap");
		return super.onDoubleTap(e);
	}

	@Override
	public void onLongPress(MotionEvent p1)
	{
		Lg.d(TAG,"onLongPress");
		super.onLongPress(p1);
	}

	@Override
	public boolean onScroll(MotionEvent event1, MotionEvent event2, float dx, float dy)
	{
		return super.onScroll(event1, event2, dx, dy);
	}

	@Override
	public void onShowPress(MotionEvent p1)
	{
		Lg.d(TAG, "onShowPress");
		super.onShowPress(p1);
	}
}
