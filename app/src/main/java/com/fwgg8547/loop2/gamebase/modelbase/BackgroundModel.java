package com.fwgg8547.loop2.gamebase.modelbase;

import com.fwgg8547.loop2.gamebase.modelbase.ModelBase;
import com.fwgg8547.loop2.gamebase.util.*;
import android.util.*;

public class BackgroundModel extends ModelBase
{
	private final static String TAG = BackgroundModel.class.getSimpleName();
	protected int mIndexCount;
	
	public BackgroundModel(){
		
	}
	
	public void initialize(ReadersWriterLock lock, int num, int p){
		super.initialize(lock, p);
		Lg.d(TAG, "initialize");
	}
	
	@Override
	public int size(){
		// todo
		return 1;
	}
	
	// Draw methods
	public int getIndexCount(){
		return mIndexCount;
	}
	
	public ItemData getData(){
		ItemData id = null;
		final int WIDTH=180;
		final int NUMW=1080/WIDTH;
		final int NUMH=1920/WIDTH;
		
		id = new ItemData();
		id.mVert = new float[NUMW*2*3+NUMH*2*3];
		id.mColor = null;
		id.mIndex = new short[NUMW*2+NUMH*2];
		id.mUv = null;
		id.mVecsIndex =0;
		id.mColorIndex =0;
		id.mIndexIndex =0;
		id.mUvIndex = 0;
		
		float[] a = id.mVert;
		int index=0;
		float dw = 180, dh=180;
		for(int i=0; i<NUMW; i++){
			a[index++] = i*dw;
			a[index++] = 0f;
			a[index++] = 0f;
			a[index++] = i*dw;
			a[index++] = 1920f;
			a[index++] = 0f;
		}

		for(int j=0; j<NUMH; j++){
			a[index++] = 0f;
			a[index++] = dh*j;
			a[index++] = 0f;
			a[index++] = 1080f;
			a[index++] = dh*j;
			a[index++] = 0f;
		}

		// index
		short[] b = id.mIndex;
		index=0;
		for(int i=0; i<(NUMW*2+NUMH*2); i++){
			b[i] = (short)i;
		}
		
		return id;
	}
	
	public int getTextureId(){
		return 0;
	}
	
	public int getTextureCount(){
		return 0;
	}

	// Update methods
	public void onUpdate(){}
	
}
