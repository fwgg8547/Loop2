package com.fwgg8547.loop2.gamebase.controllerbase;

import com.fwgg8547.loop2.gamebase.util.*;
import com.fwgg8547.loop2.gamebase.layerbase.*;
import com.fwgg8547.loop2.gamebase.sequencerbase.*;
import com.fwgg8547.loop2.gamebase.modelbase.*;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.graphics.RectF;
import android.graphics.*;

public class GestureListenerBase extends GestureDetector.SimpleOnGestureListener
{
	public interface notifyEventListener {
		public void onDown(PointF pos);
	}
	
	protected notifyEventListener mNE;
	protected ReadersWriterLock mLock;
	
	public GestureListenerBase(ReadersWriterLock lock){
		mLock = lock;
	}
	
	public void clear(){
		mLock = null;
		mNE = null;
	}
	
	public void setListener(notifyEventListener ne){
		mNE = ne;
	}
	
}
