package com.fwgg8547.loop2.gamebase.modelbase;

import com.fwgg8547.loop2.gamebase.util.*;
import android.graphics.RectF;
import android.graphics.PointF;

import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

import java.security.*;

abstract public class SpriteBase
{
	protected int id;
	protected RectF base;
	protected PointF translation;
	protected Vec2 offset;
	protected float angle;
	protected float scalex;
	protected float scaley;
	
	protected Rectangle mRectangle;
	protected Quadrilateral mQuad;
	protected boolean mDirty;
	
	protected float[] color = new float[] {0f, 1f, 1f, 1f};
	protected float[] mVecs;
	protected float[] mColors;
	protected float[] mUvs;
	protected short[] mIndices;
	
	public SpriteBase(int i ,int length){
		id = i;
		base = new RectF(-1,-1, 1, 1);
		translation = new PointF(0f,0f);
		scalex = 1.0f;
		scaley = 1.0f;
		angle =0f;
		mRectangle = new Rectangle(base.width(), base.height()); // width:hight
		mDirty = true;
		offset = new Vec2(0,0);
		allocateBuffer(length);
	}
	
	public void clear() {
		base = null;
		translation = null;
		mRectangle = null;
		mQuad = null;
		color = null;
		mVecs = null;
		mColors = null;
		mIndices = null;
		mUvs = null;
		offset = null;
	}
	
	public int getId(){
		return id;
	}
	
	public RectF setRect(RectF r){
		base = r;
		mDirty = true;
		return base;
	}
	
	public void setCenterOffset(Vec2 v){
		offset = v;
	}
	
	public Vec2 getCenterOffset()
	{
		return new Vec2(offset.x, offset.y);
	}
	
	public void setQuad(Quadrilateral q){
		mQuad = q;
		mDirty = true;
	}
	
	public PointF translate(float x, float y)
	{
		// Update our location.
		translation.x = x;
		translation.y = y;
		mDirty = true;
		return new PointF(translation.x,translation.y);
	}

	public PointF translate(Vec2 v){
		translation.x = v.x;
		translation.y = v.y;
		mDirty = true;
		return new PointF(translation.x,translation.y);
	}

	public PointF translateDelta(Vec2 v){
		translation.x += v.x;
		translation.y += v.y;
		mDirty = true;
		return new PointF(translation.x,translation.y);
	}

	public PointF translateDelta(float deltax, float deltay)
	{
		// Update our location.
		translation.x += deltax;
		translation.y += deltay;
		mDirty = true;
		return new PointF(translation.x,translation.y);
	}
	
	public float translateDelta(float deltay)
	{
		// Update our location.
		translation.y += deltay;
		mDirty = true;
		return translation.y;
	}
	
	public PointF translate(float deltay)
	{
		// Update our location.
		translation.y = deltay;
		mDirty = true;
		return new PointF(translation.x,translation.y);
	}
	
	public double rotateDelta(float deltaa)
	{
		double rad = Math.PI * deltaa / 180.0f;
		angle += rad;
		angle %= 2*Math.PI;
		mDirty = true;
		return angle;
	}
	
	public double rotate(float deltaa)
	{
		double rad = Math.PI * deltaa / 180.0f;
		angle = (float)rad;
		mDirty = true;
		return angle;
	}
	
	
	public double getRotateDeg(){
		return Math.toDegrees(angle);
	}
	
	public float getHight(){
		return mQuad.getHight();
	}

	public PointF getPosition(){
		return translation;
	}

	public float getPositionY(){
		return translation.y;
	}
	
	//===
	public float[] getTransformedVertices(){
		return mVecs;
	}

	public short[] getIndices(){
		return mIndices;
	}

	public float[] getUvs(){
		return mUvs;
	}

	public float[] getColors(){
		return mColors;
	}
	
  public void convertTriangleInfo(){
		if(!mDirty){
			return;
		}
		doConvrrt();
		mDirty = false;
	}
	
	public void allocateBuffer(int length){
		//color = new float[] {0f, 1f, 1f, 1f};
		mVecs = new float[length * 12];
		mColors = new float[length * 16];
		mUvs = new float[length * 8];
		mIndices = new short[length * 6];
	}
	
	//
	abstract protected void doConvrrt();
	abstract public RectF getOutline();
	abstract public PointF[] getOutline2();
	abstract public void scale(float x, float y);
	abstract public void scaleDelta(float dx, float dy);
	abstract public void setTextureUv(float[] uv);
	abstract public void setColor(float[] clr);
}
