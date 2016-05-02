package com.fwgg8547.loop2.gamebase.layerbase;

import com.fwgg8547.loop2.gamebase.util.*;
import com.fwgg8547.loop2.gamebase.modelbase.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.List;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;
import java.util.ArrayList;
import java.util.Iterator;
import java.nio.IntBuffer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.MotionEvent;
import java.util.*;

public class GLRenderer implements Renderer
{
	private final static String TAG = "GLRenderer";
	private final float[] mtrxProjection = new float[16];
	private final float[] mtrxView = new float[16];
	private final float[] mtrxProjectionAndView = new float[16];
	
	// Geometric variables
	public int[] mTexturename;
	public TextManager tm;
	
	// Our screenresolution
	private float   mScreenWidth = 1280;
	private float   mScreenHeight = 768;

	// Misc
	Context mContext;

	// FPS
	long mLastTime;
	private FpsController mFpsCnt;
	
	//Layer
	private List<OpenglLayer> mLayers;

	public GLRenderer(Context c, ReadersWriterLock lock, List<? extends ModelBase> models, List<? extends CollisionModel> cmodels)
	{
		mContext = c;
		mLastTime = System.currentTimeMillis() + 100;
		mFpsCnt =new FpsController();
		mLayers = new ArrayList<OpenglLayer>();
		
		Iterator it = models.iterator();
		while(it.hasNext()){
			
			ModelBase m = (ModelBase)it.next();
			if(m instanceof SpriteModel){
				OpenglLayer l = new SpriteLayer(c, lock);
				l.initialize(m.getLayerPriority());
				l.setModels(m);
				mLayers.add(l);
			} else if(m instanceof TextModelBase){
				OpenglLayer l = new TextLayer(c, lock);
				l.initialize(m.getLayerPriority());
				l.setModels(m);
				mLayers.add(l);
			} else if(m instanceof BackgroundModel){
				OpenglLayer l = new BackgroundLayer(c, lock);
				l.initialize(m.getLayerPriority());
				l.setModels(m);
				mLayers.add(l);
			}
		}
		
		it = cmodels.iterator();
		while(it.hasNext()){

			CollisionModel m = (CollisionModel)it.next();
			if(m instanceof SpriteModel){
				OpenglLayer l = new SpriteLayer(c, lock);
				l.initialize(m.getLayerPriority());
				l.setModels(m);
				mLayers.add(l);
			} 
		}
		/*
		OpenglLayer l = new BackgroundLayer();
		l.initialize(0);
		mLayers.add(l);
		*/
		Collections.sort(mLayers, new LayerComparator());
	}

	
	public void onPause()
	{
		Lg.i(TAG, "onPause");
		/* Do stuff to pause the renderer */
	}

	public void onResume()
	{
		Lg.i(TAG, "onResume");
		/* Do stuff to resume the renderer */
		mLastTime = System.currentTimeMillis();
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		boolean isUpdated = mFpsCnt.onUpdate();
		
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		// Render our example
		Iterator it = mLayers.iterator();
		while(it.hasNext()){
			OpenglLayer l = (OpenglLayer)it.next();
			l.draw(mtrxProjectionAndView);
			l.update();
		}
		
		// Render deb7g
		if(tm!=null) {
			if (isUpdated){
				tm.removeText();
				float f = mFpsCnt.getFps();
				int t = FpsController.mTestCounrt;
				FpsController.mTestCounrt=0;
				TextObject to = new TextObject("f:"+f+"|"+t ,10,10);
				tm.addText(to);
				tm.PrepareDraw();
			}
			tm.Draw(mtrxProjectionAndView);
		}
		
	}

	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		Lg.i(TAG, "onSurfaceChanged");

		// We need to know the current width and height.
		mScreenWidth = width;
		mScreenHeight = height;
		
		ScreenModel sm = ScreenModel.getInstance();
		sm.setScreenInfo(width, height);

		// Redo the Viewport, making it fullscreen.
		GLES20.glViewport(0, 0, (int)mScreenWidth, (int)mScreenHeight);

		// Clear our matrices
		for(int i=0;i<16;i++)
		{
			mtrxProjection[i] = 0.0f;
			mtrxView[i] = 0.0f;
			mtrxProjectionAndView[i] = 0.0f;
		}

		// Setup our screen width and height for normal sprite translation.
		Matrix.orthoM(mtrxProjection, 0, 
		0.0f, mScreenWidth, 
		0.0f, mScreenHeight, 
		0.0f, 50.0f);

		// Set the camera position (View matrix)
		Matrix.setLookAtM(mtrxView, 0, 
		0f, 0f, 10f, // position of camera
		0f, 0.0f, 0f,  // position of view
		0f, 1.0f, 0.0f
		);

		// Calculate the projection and view transformation
		Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		Lg.i(TAG,"onSurfaceCreated");
		// Create our texts
		SetupText();
		Iterator it = mLayers.iterator();
		while(it.hasNext()){
			OpenglLayer l = (OpenglLayer)it.next();
			l.setupPoints();
		}
		setupTexture();
		
		// Set the clear color to black
		GLES20.glClearColor(184f/255f, 194f/255f, 159f/255f, 1);

		// Create sold shader
		int vertexShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER, riGraphicTools.vs_SolidColor);
		int fragmentShader = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER, riGraphicTools.fs_SolidColor);
		riGraphicTools.sp_SolidColor = GLES20.glCreateProgram();     
		GLES20.glAttachShader(riGraphicTools.sp_SolidColor, vertexShader);   
		GLES20.glAttachShader(riGraphicTools.sp_SolidColor, fragmentShader);
		GLES20.glLinkProgram(riGraphicTools.sp_SolidColor);
		
		// Create images shader
		vertexShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER, riGraphicTools.vs_Image);
		fragmentShader = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER, riGraphicTools.fs_Image);
		riGraphicTools.sp_Image = GLES20.glCreateProgram();     
		GLES20.glAttachShader(riGraphicTools.sp_Image, vertexShader);   
		GLES20.glAttachShader(riGraphicTools.sp_Image, fragmentShader);
		GLES20.glLinkProgram(riGraphicTools.sp_Image);

		// Text shader
		int vshadert = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER, riGraphicTools.vs_Text);
		int fshadert = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER, riGraphicTools.fs_Text);
		riGraphicTools.sp_Text = GLES20.glCreateProgram();
		GLES20.glAttachShader(riGraphicTools.sp_Text, vshadert);
		GLES20.glAttachShader(riGraphicTools.sp_Text, fshadert); 
		GLES20.glLinkProgram(riGraphicTools.sp_Text);

		// Set our shader programm
		GLES20.glUseProgram(riGraphicTools.sp_Image);
		
	}

	public void processTouchEvent(MotionEvent event)
	{
	}
	
	public void UpdateSprite()
	{
		Iterator it = mLayers.iterator();
		while(it.hasNext()){
			OpenglLayer l = (OpenglLayer) it.next();
			l.update();
		}
		
	}
	
	public void setupTexture(){
		// Generate Textures, if more needed, alter these numbers.
		// decide tex buff num
		int i = 0;
		int t = 0;
		IntBuffer tex;
		Iterator it = mLayers.iterator();
		while(it.hasNext()){
			OpenglLayer l = (OpenglLayer)it.next();
			t = l.getTextureCount();
			if(t > 0){
				tex = IntBuffer.allocate(t);
				GLES20.glGenTextures(t, tex);
				l.setupTexture(i, tex.array()[0], GLES20.GL_TEXTURE0+i);
				i++;
			}
		}
		
		// text layer
		tex = IntBuffer.allocate(1);
		GLES20.glGenTextures(t, tex);
		tm.setupTexture(i, tex.array()[0],GLES20.GL_TEXTURE0+ i);
	}
	
	public void SetupText()
	{
    // Create our text manager
    tm = new TextManager(mContext);
    // Pass the uniform scale
    tm.setUniformscale(3.0f);
	}
	
}
