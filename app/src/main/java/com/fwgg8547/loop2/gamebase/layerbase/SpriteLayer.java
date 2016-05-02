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

public class SpriteLayer implements OpenglLayer
{
	private static final String TAG = "SpriteLayer";
	private ModelBase mModel;
	private int mIndexCount;
	private Context mContext;
	private int mTextureUnitIndex;
	private ReadersWriterLock mLock;
	private int mPriority;
	public FloatBuffer mVertexBuffer;
	public ShortBuffer mDrawListBuffer;
	public FloatBuffer mUvBuffer;
	public FloatBuffer mColorBuffer;
	
	public SpriteLayer(Context ctx, ReadersWriterLock lock){
		mIndexCount = 0;
		mTextureUnitIndex = 0;
		mContext = ctx;
		mLock = lock;
	}

	
	@Override
	public void initialize(int p)
	{
		mPriority = p;
	}
	
	public void clear(){
		if(mModel != null){
			mModel.clear();
			mModel = null;
		}
		
		mContext = null;
		mLock = null;
		mVertexBuffer = null;
		mDrawListBuffer = null;
		mUvBuffer = null;
		mColorBuffer = null;
	}

	@Override
	public int getLayerPriority()
	{
		return mPriority;
	}

	@Override
	public void setModels(ModelBase model)
	{
		mModel = model;
	}
	
	@Override
	public void setupPoints()
	{
		if(mModel.size() <= 0){
			return;
		}
		SpriteModel.ItemData id = null;
		Lg.a(TAG, "setupPoints 1");
		id = mModel.getData();
		Lg.a(TAG, "setupPoints 2");
		
		float[] a = id.mVert;
		short[] b = id.mIndex;
		float[] uvs = id.mUv;
		float[] colors = id.mColor;
		
		if(a == null || b == null || uvs == null || colors == null){
			a = new float[0];
			b = new short[0];
			uvs = new float[0];
			colors = new float[0];
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
	  mIndexCount = mModel.getIndexCount();
		
		// The texture buffer
		ByteBuffer bbuv = ByteBuffer.allocateDirect(uvs.length * 4);
		bbuv.order(ByteOrder.nativeOrder());
		mUvBuffer = bbuv.asFloatBuffer();
		mUvBuffer.put(uvs);
		mUvBuffer.position(0);
		
		// The color buffer.
		ByteBuffer bb3 = ByteBuffer.allocateDirect(colors.length * 4);
		bb3.order(ByteOrder.nativeOrder());
		mColorBuffer = bb3.asFloatBuffer();
		mColorBuffer.put(colors);
		mColorBuffer.position(0);
	}
	
/*
	public void setupTexture(){
		TextureManager tm = TextureManager.getInstance(mContext);
		ItemArray ia = mModel.getItemArray();
		for(int i=0, n=ia.size();i<n;i++){
			Item itm = (Item)ia.get(i);
			int resource = itm.getTextureId();
			tm.bindTexture(resource);
		}
	}
*/
	@Override
	public void setupTexture(int id, int name, int unit)
	{

		mTextureUnitIndex = id;
		Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), mModel.getTextureId());//id);

		// Bind texture to texturename
		GLES20.glActiveTexture(unit);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, name);

		// Set filtering
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, 
													 GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, 
													 GLES20.GL_LINEAR);

		// Set wrapping mode
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, 
													 GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, 
													 GLES20.GL_CLAMP_TO_EDGE);

		// Load the bitmap into the bound texture.
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

		// We are done using the bitmap so we should recycle it.
		bmp.recycle();
		
	}

	@Override
	public void update()
	{
		setupPoints();
	}

	@Override
	public void draw(float[] projectionAndview)
	{
	try{
		if(mModel==null ||mModel.size() <=0 || mUvBuffer == null || mVertexBuffer == null || mColorBuffer == null){
			//skip
			return;
		}
		} catch(Exception e){
			Lg.e(TAG,"");
		}
		// Set our shaderprogram to image shader
		GLES20.glUseProgram(riGraphicTools.sp_Text);

		// clear Screen and Depth Buffer, 
		// we have set the clear color as black.
		//GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		// get handle to vertex shader's vPosition member
		int mPositionHandle = 
			GLES20.glGetAttribLocation(riGraphicTools.sp_Text, "vPosition");

		// Enable generic vertex attribute array
		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// Prepare the triangle coordinate data
		GLES20.glVertexAttribPointer(mPositionHandle, 3,
                                 GLES20.GL_FLOAT, false,
                                 0, mVertexBuffer);

		// Get handle to texture coordinates location
		int mTexCoordLoc = GLES20.glGetAttribLocation(riGraphicTools.sp_Text, 
																									"a_texCoord" );

		// Enable generic vertex attribute array
		GLES20.glEnableVertexAttribArray ( mTexCoordLoc );

		
		// Prepare the texturecoordinates
		GLES20.glVertexAttribPointer ( mTexCoordLoc, 2, GLES20.GL_FLOAT,
																	false,
																	0, mUvBuffer);

		int mColorHandle = GLES20.glGetAttribLocation(riGraphicTools.sp_Text, 
																									"a_Color");

		// Enable a handle to the triangle vertices
		GLES20.glEnableVertexAttribArray(mColorHandle);

		// Prepare the background coordinate data
		GLES20.glVertexAttribPointer(mColorHandle, 4,
																 GLES20.GL_FLOAT, false,
																 0, mColorBuffer);
		
		// Get handle to shape's transformation matrix
		int mtrxhandle = GLES20.glGetUniformLocation(riGraphicTools.sp_Text, 
																								 "uMVPMatrix");

		// Apply the projection and view transformation
		GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, projectionAndview, 0);

		// Get handle to textures locations
		int mSamplerLoc = GLES20.glGetUniformLocation (riGraphicTools.sp_Text, 
																									 "s_texture" );

		// Set the sampler texture unit to 0, where we have saved the texture.
		GLES20.glUniform1i ( mSamplerLoc, mTextureUnitIndex);

		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glEnable(GLES20.GL_BLEND);

		// Draw the triangle

		GLES20.glDrawElements(GLES20.GL_TRIANGLES, mDrawListBuffer.capacity(),
													GLES20.GL_UNSIGNED_SHORT, mDrawListBuffer);

		// Disable vertex array
		GLES20.glDisableVertexAttribArray(mPositionHandle);
		GLES20.glDisableVertexAttribArray(mTexCoordLoc);
		
	}

	@Override
	public int getTextureCount()
	{
		return mModel.getTextureCount();
	}
}
