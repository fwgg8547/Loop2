package com.fwgg8547.loop2.gamebase.modelbase;

import com.fwgg8547.loop2.gamebase.util.*;
import android.graphics.RectF;
import android.graphics.PointF;

public class Sprite extends SpriteBase
{
	private static final String TAG = "Sprite";
	
	private PointF tempPos;
	private Vec2[] vector;	
	private final static short[] indices = new short[] {0, 1, 2, 0, 2, 3};
	
	public Sprite(int i)
	{
		super(i, 1);
		tempPos = new PointF(0f,0f);
		angle = 0f;
	}
	
	public Sprite(int i, RectF b)
	{
		super(i, 1);
		base = new RectF(b);
		tempPos = new PointF(0f,0f);
		angle = 0f;

	}
	
	public void clear(){
		tempPos = null;
		vector = null;
		super.clear();
	}

	public void translateTemp(float dx, float dy){
		tempPos.x = dx;
		tempPos.y = dy;
		mDirty = true;
	}
	
	public void setTextureUv(float[] uv){
		mUvs = uv;
		mDirty = true;
	}

	@Override
	public void setColor(float[] clr)
	{
		color = clr;
	}
	
	public void scale(float x, float y)
	{
		scalex = x;
		scaley = y;
		
		float dw = (base.width()*scalex-base.width())/2f;
		float dh = (base.height()*scaley-base.height())/2f;
		float x1 = base.left - dw; 
		float x2 = base.right +dw;
		float y1 = base.bottom +dh;
		float y2 = base.top -dh;
		
		base.set(x1,  y2, x2, y1);
		
		Vec2 v = getCenterOffset();
		v.x *=scalex; v.y *= scaley;
		setCenterOffset(v);
		
		mDirty = true;
	}
	
	public void scaleDelta(float dx, float dy)
	{
		scalex += dx;
		scaley += dy;
		mDirty = true;
	}
	
	public RectF getOutline(){
		RectF rect = new RectF();
		float[] pos = getTransformedVertices();
		int i;

		float r = pos[0],t =pos[1],l=pos[0],b=pos[2] ;
	  for(i=0;i<4;i++){
			l = (pos[i*3] < l)? pos[i*3]:l;
			r = (pos[i*3] > r)? pos[i*3]:r;
			t = (pos[i*3+1] < t)? pos[i*3+1]:t;
			b = (pos[i*3+1] > b)? pos[i*3+1]:b;
		}
		rect.left = l;
		rect.top = t;
		rect.right = r;
		rect.bottom = b;
		
		return rect;
	}

	@Override
	public PointF[] getOutline2()
	{
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
	
	public Vec2Rect getTransformedVector()
	{
		float offsetX = 0, offsetY = 0;
		if(offset != null){
			offsetX = offset.x;
			offsetY = offset.y;
		}
		// Start with scaling
		float dw = (base.right - base.left)*scalex/2;
		float dh =(base.top - base.bottom)*scaley/2;
		float x1 = base.left - dw + offsetX; 
		float x2 = base.right +dw + offsetX;
		float y1 = base.bottom -dh + offsetY;
		float y2 = base.top +dh + offsetY;
		
		// We now detach from our Rect because when rotating, 
		// we need the seperate points, so we do so in opengl order
		PointF one = new PointF(x1, y2);
		PointF two = new PointF(x1, y1);
		PointF three = new PointF(x2, y1);
		PointF four = new PointF(x2, y2);

		// We create the sin and cos function once, 
		// so we do not have calculate them each time.
		float s = (float) Math.sin(angle);
		float c = (float) Math.cos(angle);

		// Then we rotate each point
		one.x = x1 * c - y2 * s;
		one.y = x1 * s + y2 * c;
		two.x = x1 * c - y1 * s;
		two.y = x1 * s + y1 * c;
		three.x = x2 * c - y1 * s;
		three.y = x2 * s + y1 * c;
		four.x = x2 * c - y2 * s;
		four.y = x2 * s + y2 * c;

		// Finally we translate the sprite to its correct position.
		one.x += translation.x;
		one.y += translation.y;
		two.x += translation.x;
		two.y += translation.y;
		three.x += translation.x;
		three.y += translation.y;
		four.x += translation.x;
		four.y += translation.y;

		one.x += tempPos.x;
		one.y += tempPos.y;
		two.x += tempPos.x;
		two.y += tempPos.y;
		three.x += tempPos.x;
		three.y += tempPos.y;
		four.x += tempPos.x;
		four.y += tempPos.y;

		return new Vec2Rect(one, two, three, four);
	}
	
	@Override
	protected void doConvrrt()
	{
		float offsetX = 0, offsetY = 0;
		
		if(offset != null){
			offsetX = offset.x;
			offsetY = offset.y;
		}
		
		float x1 = base.left + offsetX;
		float x2 = base.right + offsetX;
		float y1 = base.bottom + offsetY;
		float y2 = base.top + offsetY;
		// Start with scaling
		
		/*
		dw = (base.right - base.left)*scalex/2;
		dh = (base.top   - base.bottom)*scaley/2;
		float x1 = base.left - dw + offsetX;
		float x2 = base.right +dw + offsetX;
		float y1 = base.bottom -dh + offsetY;
		float y2 = base.top +dh + offsetY;
		*/
		
		// We now detach from our Rect because when rotating, 
		// we need the seperate points, so we do so in opengl order
		PointF one = new PointF(x1, y2); // tl
		PointF two = new PointF(x1, y1); // bl
		PointF three = new PointF(x2, y1);
		PointF four = new PointF(x2, y2);

		// We create the sin and cos function once, 
		// so we do not have calculate them each time.
		float s = (float) Math.sin(angle);
		float c = (float) Math.cos(angle);

		// Then we rotate each point
		one.x = x1 * c - y2 * s;
		one.y = x1 * s + y2 * c;
		two.x = x1 * c - y1 * s;
		two.y = x1 * s + y1 * c;
		three.x = x2 * c - y1 * s;
		three.y = x2 * s + y1 * c;
		four.x = x2 * c - y2 * s;
		four.y = x2 * s + y2 * c;

		// Finally we translate the sprite to its correct position.
		one.x += translation.x;
		one.y += translation.y ;//+ cv.y;
		two.x += translation.x;// + cv.x;
		two.y += translation.y;// + cv.y;
		three.x += translation.x;// + cv.x;
		three.y += translation.y;// + cv.y;
		four.x += translation.x;// + cv.x;
		four.y += translation.y;// + cv.y;

		one.x += tempPos.x;
		one.y += tempPos.y;
		two.x += tempPos.x;
		two.y += tempPos.y;
		three.x += tempPos.x;
		three.y += tempPos.y;
		four.x += tempPos.x;
		four.y += tempPos.y;

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
	
	
	/*
	
	 public boolean conflict2(float x, float y){
	 boolean isConflict = false;
	 float[] pos = getTransformedVertices();
	 float left = pos[0];
	 float top = pos[1];
	 float right = pos[6];
	 float bottom = pos[4];

	 //Lg.w(TAG, "l t r b " + left+" "+top+" "+right+" "+bottom);
	 if(left <= x && x <= right){
	 if(bottom <= y && y <= top){
	 isConflict = true;
	 }
	 }
	 return isConflict;
	 }

	 public boolean conflict(float x, float y){
	 boolean isConflict = true;
	 Vec2Rect vr = getTransformedVector();

	 int i = 0;
	 for(Vec2 v: vr.vec){
	 Vec2 p = new Vec2(x -vr.point[i].x, y- vr.point[i].y);
	 if(Vec2.cross(v,p) < 0){
	 //Lg.i(TAG, "cross < 0");
	 } else{
	 isConflict = false;
	 //Lg.i(TAG, "out of sprote");
	 break;
	 }
	 i++;
	 }

	 return isConflict;
	 }
	 
	*/
}
