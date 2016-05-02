package com.fwgg8547.loop2.gamebase.layerbase;

import com.fwgg8547.loop2.gamebase.util.*;
import com.fwgg8547.loop2.gamebase.modelbase.*;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.*;
import android.util.*;
import java.util.*;
import android.nfc.*;

public class GlSurf extends GLSurfaceView
{
		public interface Callback 
		{
				abstract public boolean onTouchEvent(MotionEvent event);
		}

		private final GLRenderer mRenderer;
		private Callback mCallback;
		private final static String TAG = "GLSUF";
		private ReadersWriterLock mLock;

		public GlSurf(Context context, ReadersWriterLock lock, List<? extends ModelBase> models, List<CollisionModel> cmodels) {
				super(context);
				mLock = lock;
				
				// Create an OpenGL ES 2.0 context.
				setEGLContextClientVersion(2);

				// Set the Renderer for drawing on the GLSurfaceView
				mRenderer = new GLRenderer(context, lock, models, cmodels);
				setRenderer(mRenderer);

				// Render the view only when there is a change in the drawing data
				setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		}

		public void setCallback(Callback cb)
		{
				mCallback = cb;
		}

		@Override
				public boolean onTouchEvent(MotionEvent event)
		{
				if(mCallback != null) {
						mCallback.onTouchEvent(event);
				}
				return true;
		}

		@Override
				public void onPause() {
					Lg.i(TAG, "onPause");
				// TODO Auto-generated method stub
				queueEvent(new Runnable(){
					public void run() {
						mRenderer.onPause();
					}
				});
				
				super.onPause();
				/*
				mRenderer.onPause();
				Lg.i(TAG, "onPause");
				*/
		}

		@Override
				public void onResume() {
				// TODO Auto-generated method stub
				
				super.onResume();
				mRenderer.onResume();
				Lg.i(TAG, "onResume");
		}
		
		
	
}
