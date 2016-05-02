package com.fwgg8547.loop2.model;
import com.fwgg8547.loop2.gamebase.modelbase.*;
import org.dyn4j.geometry.*;
import com.fwgg8547.loop2.gamebase.util.*;
import android.graphics.*;

public class WallSprite extends SpriteBase
{		
	private Vec2[] vector;
	private final static short[] indices = new short[] {0, 1, 2, 0, 2, 3};
	
	public WallSprite(int i){
		super(i, 1);
	}
		
	protected void doConvrrt(){	
		float r1, r2, l1, l2 , t, b;
		if(mQuad == null){
			return;
		}
		r1 = mQuad.topright.x;
		r2 = mQuad.bottomright.x;
		l1 = mQuad.topleft.x;
		l2 = mQuad.bottomleft.x;
		t = mQuad.topleft.y;
		b = mQuad.bottomleft.y;
		// Start with scaling
		
		float dw = (r1 - l1)*scalex/2;
		float dw2 = (r2 - l2)*scalex/2;
		float dh =(t - b)*scaley/2;
		/*
		float x1 = left - dw;
		float x2 = right +dw;
		float y1 = bottom -dh;
		float y2 = top +dh;
		*/
		// We now detach from our Rect because when rotating, 
		// we need the seperate points, so we do so in opengl order
		PointF one = new PointF(l1, t); //lt l1xt
		PointF two = new PointF(l2, b); //lb l2xb
		PointF three = new PointF(r2, b); //rb rxb
		PointF four = new PointF(r1, t); //rt rxt

		// We create the sin and cos function once, 
		// so we do not have calculate them each time.
		// todo rotation
		
		float s = (float) Math.sin(angle);
		float c = (float) Math.cos(angle);

		// Then we rotate each point
		one.x = one.x * c - one.y * s;
		one.y = one.x * s + one.y * c;
		two.x = two.x * c - two.y * s;
		two.y = two.x * s + two.y * c;
		three.x = three.x * c - three.y * s;
		three.y = three.x * s + three.y * c;
		four.x = four.x * c - four.y* s;
		four.y = four.x * s + four.y * c;
		
		
		// Finally we translate the sprite to its correct position.
		one.x += translation.x;
		one.y += translation.y;
		two.x += translation.x;
		two.y += translation.y;
		three.x += translation.x;
		three.y += translation.y;
		four.x += translation.x;
		four.y += translation.y;

		vector = new Vec2[] {
			new Vec2(one.x - two.x, one.y - two.y),
			new Vec2(two.x - three.x, two.y - three.y),
			new Vec2(three.x - four.x, three.y - four.y),
			new Vec2(four.x - one.x, four.y - one.y)
		};

		// We now return our float array of vertices.
		mVecs = new float[]
		{
			one.x, one.y, 0.0f,
			two.x, two.y, 0.0f,
			three.x, three.y, 0.0f,
			four.x, four.y, 0.0f,
		};

		mColors = new float[]
		{color[0], color[1], color[2], color[3],
			color[0], color[1], color[2], color[3],
			color[0], color[1], color[2], color[3],
			color[0], color[1], color[2], color[3]
		};

		mIndices = indices;
	}
	
	public RectF getOutline()
	{
		return null;
	}
	
	public PointF[] getOutline2() {
		float[] pos = getTransformedVertices();
		/*
		 one.x, one.y, 0.0f,
		 two.x, two.y, 0.0f,
		 three.x, three.y, 0.0f,
		 four.x, four.y, 0.0f,
		 
		*/
		PointF[] p = new PointF[]{
			new PointF(pos[0], pos[1]), //tl1
			new PointF(pos[3], pos[4]), //bl2
			new PointF(pos[6], pos[7]), //br
			new PointF(pos[9], pos[10]), //tr
		};
		
		return p;
	}
	
	public void scale(float x, float y){
		scalex = x;
		scaley = y;
		mDirty = true;
	}
	
	public void scaleDelta(float dx, float dy){
		scalex += dx;
		scaley += dy;
		mDirty = true;
	}
	
	public void setTextureUv(float[] uv){
		mUvs = uv;
		mDirty = true;
	}
	
	public void setColor(float[] clr){
		color = clr;
	}
}
