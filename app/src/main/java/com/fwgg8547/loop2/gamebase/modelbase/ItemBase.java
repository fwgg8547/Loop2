package com.fwgg8547.loop2.gamebase.modelbase;

import com.fwgg8547.loop2.gamebase.util.*;
import com.fwgg8547.loop2.gamebase.modelbase.*;
import com.fwgg8547.loop2.gamebase.sequencerbase.*;
import android.graphics.PointF;
import android.graphics.RectF;
import com.fwgg8547.loop2.gamebase.scorebase.*;

abstract public class ItemBase {
	
	public boolean mIsDeleted;
	public int mIndex;
	
	protected int mId;
	protected int mItemType;
	protected SpriteBase mSprite;
	protected int mTextureResourceId;
	protected AnimationSequencer mAnimSequencer;
	protected AnimType mAnimType;
	
	public enum AnimType {
		NONE,
		ROLL,
		STRIGHT,
		MOTIONP,
	}
	
	public ItemBase(){
		
	}
	
	public void clear(){
		if(mSprite != null){
			mSprite.clear();
			mSprite = null;
		}
		if(mAnimSequencer != null){
			mAnimSequencer.clear();
			mAnimSequencer = null;
		}
	}
	
	public int getId(){
		return mId;
	}
	
	public int getIndex(){
		return mIndex;
	}
	
	public void setId(int i){
		mId = i;
	}
	
	public void setType(int t){
		mItemType = t;
	}
	
	public int getType(){
		return mItemType;
	}
	
	public RectF setRect(RectF r){
		return mSprite.setRect(r);
	}
	
	public void setCenterOffset(Vec2 v){
		mSprite.setCenterOffset(v);
	}
	
	public void setQuadrilateral(Quadrilateral q){
		mSprite.setQuad(q);
	}
	
	public RectF getRect() {
		return mSprite.getOutline();
	}
	
	public void setSprite(SpriteBase s){
		mSprite = s;
		mSprite.convertTriangleInfo();
	}
	
	public int getSpriteId(){
		return mSprite.getId();
	}

	public void setTexureId(int id){
		mTextureResourceId = id;
	}

	public int getTextureId(){
		return mTextureResourceId;
	}

	public void setPosition(float x, float y, float r, float deg){
		mSprite.translate(
			(float)(x + r*Math.cos(deg*Math.PI/180.0f)),
			(float)(y + r*Math.sin(deg*Math.PI/180.0f)));
	}
	
	public void setPositionDelta(float x, float y){
		mSprite.translateDelta(x,y);
	}
	
	public void setPositionY(float y){
		mSprite.translate(y);
	}
	
	public void setScale(float x, float y){
		mSprite.scale(x, y);
	}

	public float setAngleDelta(float a){
		return (float)mSprite.rotateDelta(a);
	}
	
	public float setAngle(float a){
		return (float)mSprite.rotate(a);
	}
	
	public float getAngle(){
		return (float)mSprite.getRotateDeg();
	}
	
	public void setColor(float[] c){
		mSprite.setColor(c);
	}
	
	public void setMotionPattern(MotionSequnce[] mp, AnimationSequencer.Callback cb){

		if(mp != null){
			mAnimType = AnimType.MOTIONP;
			mAnimSequencer.Initialize(mp, this, cb);
		} else {
			mAnimSequencer.stopMotion();
		}
	}

	public void setRotatePattern(RotateSequence[] rs, AnimationSequencer.Callback cb){

		if(rs != null){
			mAnimType = AnimType.MOTIONP;
			mAnimSequencer.Initialize(rs, this, cb);
		} else {
			mAnimSequencer.stopRotate();
		}
	}
	
	public void setScalePattern(ScaleSequence[] mp, AnimationSequencer.Callback cb){
		if(mp != null){
			mAnimType = AnimType.MOTIONP;
			mAnimSequencer.Initialize(mp, this, cb);
		} else {
			mAnimSequencer.stopScale();
		}
	}

	public void setTexturePattern(TextureSequence[] mp, AnimationSequencer.Callback cb){
		if(mp != null){
			mAnimType = AnimType.MOTIONP;
			mAnimSequencer.Initialize(mp, this, cb);
		} else {
			mAnimSequencer.stopTexture();
		}
	}

	public void setPattern(MotionSequnce[] mp ,RotateSequence[] rs ,ScaleSequence[] ma, TextureSequence[] mt,AnimationSequencer.Callback cb){
		mAnimType = AnimType.MOTIONP;
		mAnimSequencer.Initialize(mp, rs, ma, mt, this, cb);
	}
	
	public void setAnimationValid(boolean v){
		mAnimSequencer.valid(v);
	}
	
	public void updateVertix(){
		mSprite.convertTriangleInfo();
	}
	
	public PointF getPosition() {
		return mSprite.getPosition();	
	}
	
	public float getHight(){
		return mSprite.getHight();
	}
	
	public float[] getVertices(){
		return mSprite.getTransformedVertices();
	}

	public float[] getColors(){
		return mSprite.getColors();
	}

	public float[] getUvs(){
		return mSprite.getUvs();
	}

	public short[] getIndices(){
		return mSprite.getIndices();
	}
	
	public PointF moveAnimation(){
		if(!mAnimSequencer.getValid()) {
			return null;
		}

		PointF pos = null;		
		switch(mAnimType){
			case ROLL:
				//pos = moveAnimation1();
				//mAnim.mDuration--;
				break;
			case STRIGHT:
				//pos = moveAnimation2();
				//mAnim.mDuration--;
				break;
			case MOTIONP:
				pos = doMoveMotion();
				break;
			default:
				break;
		}
		return pos;
	}
	
	abstract protected PointF doMoveMotion();
}

