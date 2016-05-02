package com.fwgg8547.loop2.gamebase.layerbase;

import com.fwgg8547.loop2.gamebase.util.*;
import com.fwgg8547.loop2.gamebase.layerbase.*;
import com.fwgg8547.loop2.gamebase.sequencerbase.*;
import com.fwgg8547.loop2.gamebase.modelbase.*;

import android.opengl.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.*;

public class BackgroundLayer implements OpenglLayer
{
	private static final String TAG = "BackgroundLayer";
	private ModelBase mModel;
	private int mIndexCount;
	private Context mContext;
	private int mTextureUnit;
	private int mPriority;
	private ReadersWriterLock mLock;
	
	public FloatBuffer mVertexBuffer;
	public ShortBuffer mDrawListBuffer;

	public BackgroundLayer(Context c, ReadersWriterLock loc){
		mContext = c;
		mLock = loc;
	}
	
	@Override
	public void initialize(int p)
	{
		mPriority = p;
	}
	
	@Override
	public int getLayerPriority(){
		return mPriority;
	}
	
	@Override
	public void setModels(ModelBase model)
	{
		mModel = model;
	}
	
	
	//public FloatBuffer mUvBuffer;
	public void setupPoints(){
		
		if(mModel.size() <= 0){
			return;
		}
		SpriteModel.ItemData id = null;
		Lg.a(TAG, "setupPoints 1");
		id = mModel.getData();
		Lg.a(TAG, "setupPoints 2");

		float[] a = id.mVert;
		short[] b = id.mIndex;
		//float[] uvs = id.mUv;
		//float[] colors = id.mColor;

		if(a == null || b == null/* || uvs == null || colors == null*/){
			a = new float[0];
			b = new short[0];
			//uvs = new float[0];
			//colors = new float[0];
		}
		
		// The vertex buffer.
		ByteBuffer bb = ByteBuffer.allocateDirect(a.length * 4);
		bb.order(ByteOrder.nativeOrder());
		mVertexBuffer = bb.asFloatBuffer();
		mVertexBuffer.put(a);
		mVertexBuffer.position(0);

		// index list ; initialize byte buffer for the draw list
		ByteBuffer dlb = ByteBuffer.allocateDirect(b.length * 2);
		dlb.order(ByteOrder.nativeOrder());
		mDrawListBuffer = dlb.asShortBuffer();
		mDrawListBuffer.put(b);
		mDrawListBuffer.position(0);
	  mIndexCount = b.length;

		// The texture buffer
		/*
		ByteBuffer bbuv = ByteBuffer.allocateDirect(uvs.length * 4);
		bbuv.order(ByteOrder.nativeOrder());
		mUvBuffer = bbuv.asFloatBuffer();
		mUvBuffer.put(uvs);
		mUvBuffer.position(0);
		*/
	}
	
	public void setupTexture(int id, int name, int uint){
		//
	}
	
	public void update(){
		setupPoints();
	}
	
	public void draw(float[] projectionAndview){
		if(mVertexBuffer == null){
			//skip
			return;
		}
		// Set our shaderprogram to image shader
		GLES20.glUseProgram(riGraphicTools.sp_SolidColor);

		// get handle to vertex shader's vPosition member
		int mPositionHandle = 
			GLES20.glGetAttribLocation(riGraphicTools.sp_SolidColor, "vPosition");

		// Enable generic vertex attribute array
		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// Prepare the triangle coordinate data
		GLES20.glVertexAttribPointer(mPositionHandle, 3,
                                 GLES20.GL_FLOAT, false,
                                 0, mVertexBuffer);

		// Get handle to shape's transformation matrix
		int mtrxhandle = GLES20.glGetUniformLocation(riGraphicTools.sp_SolidColor, 
																								 "uMVPMatrix");

		// Apply the projection and view transformation
		GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, projectionAndview, 0);

		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glEnable(GLES20.GL_BLEND);

		// Draw the triangle
		GLES20.glDrawElements(GLES20.GL_LINES, mIndexCount,
													GLES20.GL_UNSIGNED_SHORT, mDrawListBuffer);

		// Disable vertex array
		GLES20.glDisableVertexAttribArray(mPositionHandle);
		
	}

	@Override
	public int getTextureCount()
	{
		// TODO: Implement this method
		return 0;
	}
}
