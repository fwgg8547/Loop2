package com.fwgg8547.loop2.gamebase.modelbase;

import com.fwgg8547.loop2.gamebase.util.*;
import com.fwgg8547.loop2.gamebase.modelbase.*;

import android.graphics.*;
import android.text.*;

public class TextSprite extends SpriteBase
{
	private static final String TAG = TextSprite.class.getSimpleName();
	
	private float fontSize;
	private String text;
	private int mVecsIndex;
	private int mColorIndex;
	private int mUvIndex;
	private int mIndexIndex;
	
	public TextSprite(int i, String t, PointF p, float sz)
	{
		super(i, t.length());
		text = t;
		fontSize = sz;
		translation = new PointF(p.x,p.y);
	}
	
	public void clear(){
		text = null;
		super.clear();
	}
	
	public void setText(String s){
		if(text.length() != s.length()){
			allocateBuffer(s.length());
		}
		text = s;
		mDirty = true;
	}

	@Override
	public void setColor(float[] clr)
	{
		// TODO: Implement this method
	}

	public String getText(){
		return text;
	}

	@Override
	protected void doConvrrt()
  {
    // Get attributes from text object
		mVecsIndex =0; mColorIndex=0; mUvIndex=0;mIndexIndex=0;
    float x = base.left + translation.x;
    float y = base.top + translation.y;
		float uniformscale = fontSize;

    // Create 
    for(int j=0; j<text.length(); j++)
    {
      // get ascii value
      char c = text.charAt(j);
      int c_val = (int)c;

      int indx = TextTexUtil.convertCharToIndex(c_val);

      if(indx==-1) {
        // unknown character, we will add a space for it to be save.
        x += ((TextTexUtil.RI_TEXT_SPACESIZE) * uniformscale);
        continue;
      }

      // Calculate the uv parts
      int row = indx / 8;
      int col = indx % 8;

      float v = row * TextTexUtil.RI_TEXT_UV_BOX_WIDTH;
      float v2 = v + TextTexUtil.RI_TEXT_UV_BOX_WIDTH;
      float u = col * TextTexUtil.RI_TEXT_UV_BOX_WIDTH;
      float u2 = u + TextTexUtil.RI_TEXT_UV_BOX_WIDTH;

      // Creating the triangle information
      float[] vec = new float[12];
      float[] uv = new float[8];
      float[] colors = new float[16];

      vec[0] = x;
      vec[1] = y + (TextTexUtil.RI_TEXT_WIDTH * uniformscale);
      vec[2] = 0.99f;
      vec[3] = x;
      vec[4] = y;
      vec[5] = 0.99f;
      vec[6] = x + (TextTexUtil.RI_TEXT_WIDTH * uniformscale);
      vec[7] = y;
      vec[8] = 0.99f;
      vec[9] = x + (TextTexUtil.RI_TEXT_WIDTH * uniformscale);
      vec[10] = y + (TextTexUtil.RI_TEXT_WIDTH * uniformscale);
      vec[11] = 0.99f;

      colors = new float[]
			{color[0], color[1], color[2], color[3],
				color[0], color[1], color[2], color[3],
				color[0], color[1], color[2], color[3],
				color[0], color[1], color[2], color[3]
			};
      // 0.001f = texture bleeding hack/fix
      uv[0] = u+0.001f;
      uv[1] = v+0.001f;
      uv[2] = u+0.001f;
      uv[3] = v2-0.001f;
      uv[4] = u2-0.001f;
      uv[5] = v2-0.001f;
      uv[6] = u2-0.001f;
      uv[7] = v+0.001f;

      short[] inds = {0, 1, 2, 0, 2, 3};

			addVectorInfo(vec, uv, inds, colors);

      // Calculate the new position
      x += ((TextTexUtil.l_size[indx]/2)  * uniformscale);
    }
		
		mDirty = false;
  }
	
	public void addVectorInfo(float[] v, float[] u, short[] i,float[] c){

		short base = (short) ((mVecsIndex / 3));

		// We should add the vec, translating the indices to our saved vector
		for(int j=0;j<v.length;j++)
		{
			mVecs[mVecsIndex] = v[j];
			mVecsIndex++;
		}

		// We should add the colors.
		for(int j=0;j<c.length;j++)
		{
			mColors[mColorIndex] = c[j];
			mColorIndex++;
		}

		// We should add the uvs
		for(int j=0;j<u.length;j++)
		{
			mUvs[mUvIndex] = u[j];
			mUvIndex++;
		}

		// We handle the indices
		for(int j=0;j<i.length;j++)
		{
			mIndices[mIndexIndex] = (short) (base+i[j]);
			mIndexIndex++;
		}
	}
	
	@Override
	public RectF getOutline() {
		return null;
	}

	@Override
	public PointF[] getOutline2()
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public void scaleDelta(float dx, float dy){

	}

@Override
public void scale(float x, float y)
{

 // TODO: Implement this method
}


	
	@Override
	public void setTextureUv(float[] uv){

	}

}
