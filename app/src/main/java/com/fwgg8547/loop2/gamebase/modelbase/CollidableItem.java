package com.fwgg8547.loop2.gamebase.modelbase;

import com.fwgg8547.loop2.gamebase.util.Vec2;
import com.fwgg8547.loop2.gamebase.modelbase.*;
import com.fwgg8547.loop2.gamebase.sequencerbase.*;

import android.graphics.PointF;
import android.graphics.RectF;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.dynamics.*;
import org.dyn4j.geometry.*;
import com.fwgg8547.loop2.gamebase.util.*;

public class CollidableItem extends ItemBase implements ScrollableItem
{
	private static final String TAG = CollidableItem.class.getSimpleName();
	protected Body mBody;
	protected BodyType mBodyType;
	private float mAngleDelta;
	private PointF mAngleCenter;
	protected static Vec2 mDirectOffset;
	
	private enum BodyType {
		Rectangle,
		Quadrilateral,
	}
	
	public CollidableItem() 
	{
		super();
		mIndex = 0;
		mIsDeleted = false;
		mAnimType = AnimType.NONE;
	}
	
	public static void setOffsetVect(Vec2 v){
		mDirectOffset = v;
	}
	
	public void initialize(Body b){
		mIsDeleted = false;
		mAnimType = AnimType.NONE;
		mAnimSequencer = new AnimationSequencer();
		mBody = b;
		UserData ud = new UserData();
		ud.setItem(this);
		b.setUserData(ud);
		mAngleDelta =0;
		mAngleCenter = new PointF(0,0);
	}
	
	public void clear(){
		if(mBody != null){
			mBody.removeAllFixtures();
			mBody = null;
		}
		if(mAnimSequencer != null){
			mAnimSequencer.clear();
			mAnimSequencer = null;
		}
		super.clear();
		mDirectOffset = null;
	}

	@Override
	public void setCenterOffset(Vec2 v){
		super.setCenterOffset(v);
		
		// translate fixure center
		Vector2 center = mBody.getFixture(0).getShape().getCenter();
		mBody.getFixture(0).getShape().translate(v.x - center.x, v.y-center.y);
	}
	
	public Vec2 getCenterOffset(){
		return mSprite.getCenterOffset();
	}
	
	@Override
	public void setSprite(SpriteBase s){
		super.setSprite(s);
		mBody.setMass(MassType.NORMAL);
	}
	
	public void setRectangleSprite(SpriteBase s){
		this.setSprite(s);
	}

	public void removeBody(){
		mBody.removeAllFixtures();
	}
	
	public Body getBody(){
		return mBody;
	}
	
	public boolean isCollision(RectF r){
		return RectF.intersects(mSprite.getOutline() , r);
	}
	
	public boolean isIncluded(PointF pos){
		Vector2 v = new Vector2(pos.x, pos.y);
		return mBody.contains(v);
	}

	@Override
	public void setPosition(float x, float y, float r, float deg){
		super.setPosition(x, y, r, deg);
		mBody.translateToOrigin();
		mBody.translate(x,y);
	}

	@Override
	public void setPositionDelta(float x, float y)
	{
		super.setPositionDelta(x, y);
		mBody.translate(x,y);
	}
	
	@Override
	public void setPositionY(float y){
		super.setPositionY(y);
		PointF p = getPosition();
		mBody.translate(p.x,p.y);
	}

	public void setAngleCenter(PointF pos){
		Lg.i(TAG, "setAngleCenter "+ "x="+pos.x+":y="+pos.y);
		mAngleCenter.x = pos.x;
		mAngleCenter.y = pos.y;
	}

	public void setAngleCenter(float x, float y){
		Lg.i(TAG, "setAngleCenter "+ "x="+x+":y="+y);
		mAngleCenter.x = x;
		mAngleCenter.y = y;
	}

	public PointF getAngleCenter(){
		return mAngleCenter;
	}
	
	@Override
	public float setAngle(float a){
		double rad2 = Math.PI * a / 180.0f;
		//if(mAngleCenter == null){
			mBody.rotateAboutCenter(rad2);
		//} else {
		//	mBody.rotate(rad2, new Vector2(mAngleCenter.x, mAngleCenter.y));
		//}	
		
		return super.setAngle(a);
	}
	
	@Override
	public float setAngleDelta(float a)
	{
		if(a < 0){
			a+=360;
			a%=360;
		}
		mAngleDelta = a;
		float rad  = super.setAngleDelta(a);
		double rad2 = Math.PI * a / 180.0f;
		
		mBody.rotateAboutCenter(rad2);
		return rad;
	}
	
	public float getAngleDelta(){
		return mAngleDelta;	
	}
	
	@Override
	public RectF setRect(RectF r)
	{
		RectF nr = super.setRect(r);
		BodyFixture bf = new BodyFixture(Geometry.createRectangle(
		nr.width(), nr.height()));
		mBody.removeAllFixtures();
		mBody.addFixture(bf);
		return nr;
	}
	
	@Override
	public void setQuadrilateral(Quadrilateral q)
	{
		super.setQuadrilateral(q);
		
		mBodyType = BodyType.Quadrilateral;
		mBody.removeAllFixtures();
		mBody.addFixture(Geometry.createPolygon(
											 new Vector2(q.topleft.x, q.topleft.y),
											 new Vector2(q.topright.x, q.topright.y),
											 new Vector2(q.bottomright.x, q.bottomright.y),
											 new Vector2(q.bottomleft.x, q.bottomleft.y)
										 ));		
		mBody.setMass(MassType.NORMAL);
		
	}

	@Override
	public PointF doScroll()
	{
		PointF pos = null;
		if(mDirectOffset != null){
			pos = mSprite.translateDelta(mDirectOffset);
			mBody.translate(mDirectOffset.x, mDirectOffset.y);
		}
		return pos;
	}

	@Override
	protected PointF doMoveMotion(){
		mAnimSequencer.tick();
		
		Vec2 tmp = mAnimSequencer.getVector();
		PointF pos = null;
		
		if(tmp != null){
			pos = mSprite.translateDelta(tmp);
			mBody.translate(tmp.x, tmp.y);
		}
		
		float angle = mAnimSequencer.getRotate();
		if(angle != 0){
			setAngleDelta(angle);
		}
		
		Vec2 tmp2 = mAnimSequencer.getScale();
		if(tmp2 != null){
			//mSprite.scaleDelta(tmp2.x, tmp2.y);
			mSprite.scale(tmp2.x, tmp2.y);
		}
		
		float[] tmp3 = mAnimSequencer.getTextureUv();
		if(tmp3 != null){
			mSprite.setTextureUv(tmp3);
		}
		
		return pos;
	}
	
	public void delete(ItemPattern delanim){
		if(delanim != null){
			setAnimationValid(false);
			PointF pos = delanim.mInitPos;
			if(pos!=null){
			setPosition(pos.x, pos.y, 0.0f, 0.0f);
			}
			setMotionPattern(delanim.mMotionPattern,new AnimationSequencer.Callback(){
					public void notify(ItemBase itm, int i){
						switch(i){
							case 0:
								Lg.i(TAG, "motion end callback");
								itm.mIsDeleted = true;
								break;
							default:
								Lg.i(TAG, "other end callback");
						}
					}
				});
			setScale(1 ,1);
			setScalePattern(delanim.mScalePattern, null
			/*
			new AnimationSequencer.Callback(){
					public void notify(ItemBase itm, int i){
						switch(i){
							case 1:
								Lg.i(TAG,"scale end callback");
								itm.mIsDeleted = true;
						}
					}
				}
				*/
				);
		} else {
			mIsDeleted = true;
		}
		removeBody();
	}
	
	/*
	 public void setAnimParam(
	 AnimType t,
	 float ex, float ey, float er, float edeg,
	 float v,
	 int d,
	 Vec2 vd){
	 mAnim.mType = t;
	 mAnim.setEndParam(ex,ey,er,edeg);
	 mAnim.setVerocity(v);
	 mAnim.setDuration(d);
	 mAnim.setDirect(vd);
	 }
	 
	 public PointF moveAnimation2(){
		if(mAnim.mDuration < 0) {
			mAnim.mValid = false;
			mAnim.mDuration = 0;
			return null;
		}
		
		float ppf = mAnim.mVerocity;
		Vec2 v = new Vec2(mAnim.mDirect.x, mAnim.mDirect.y);
		v.multiply(ppf);
		
		mPosition.movePosition(v.x, v.y, 0f, 0f);
		PointF pos = mPosition.getPosition();
		return mSprite.translate(pos.x, pos.y);
	}
	
	 public class MoveParam2{
	 public float mex;
	 public float mey;
	 public float mer;
	 public float medeg;
	 public float mdtv;
	 public float mdtdeg;
	 public float mVerocity;
	 public int mDuration;
	 public Vec2 mDirect;
	 public AnimType mType;

	 public boolean mValid;

	 public MoveParam2(){
	 mex = 0.0f;
	 mey = 0.0f;
	 mer = 0.0f;
	 medeg = 0.0f;
	 mDirect = null;
	 mVerocity = 0.0f;
	 mDuration = 0;
	 mValid = false;
	 }

	 public void setEndParam(float x, float y, float r, float deg){
	 mex = x;
	 mey = y;
	 mer = r;
	 medeg = deg;
	 }

	 public void setDirect(Vec2 d){
	 mDirect = d;
	 }

	 public void setVerocity(float v){
	 mVerocity = v;
	 mdtv = v;
	 mdtdeg = v;
	 }

	 public void setDuration(int d){
	 mDuration = d;
	 }

	 public void setValid(boolean v){
	 mValid = v;
	 }

	 public void setType(AnimType t){
	 mType = t;
	 }
	 }

	 public class MoveParam{
		public float mx;
		public float my;
		public float mr;
	 	public float mdeg;

		public MoveParam(float x, float y, float r, float deg){
	 		mx = x;
	 		my = y;
	 		mr = r;
	 		mdeg = deg;
	 	}

	 	public void setParam(float x, float y, float r, float deg){
	 		mx = x;
	 		my = y;
	 		mr = r;
	 		mdeg = deg;			
		}
	}
	 
	public PointF moveAnimation1(){
		if(mAnim.mDuration < 0) {
			mAnim.mValid = false;
			mAnim.mDuration = 0;
			return null;
		}

		// roll scroll
		if(mAnim.mdtdeg > 0) {
			if(mDeg < mAnim.medeg){
				mDeg += mAnim.mdtdeg;
				mDeg = mDeg % 360;
				mAnim.mdtdeg -= mAnim.mdtdeg*0.1f;
				if (mAnim.mdtdeg <= 0) {
					// animation end by veromty is zero
					mAnim.mValid = false;
				}
			} else {
				// animation end by arrive end point
				//mAnim.mValid = false;
			}
		}	else {
			if(mDeg > -1.0f*mAnim.medeg){
				mDeg += mAnim.mdtdeg;
				mDeg = mDeg % 360;
				mAnim.mdtdeg -= mAnim.mdtdeg*0.1f;				
				if (mAnim.mdtdeg >= 0) {
					// animation end by verocity is zero
					mAnim.mValid = false;
				}		
			} else {
				// animation end by arrive end point
				// mAnim.mValid = false;				
			}
		}
		return mSprite.translate((float)(mPosition.x +mR*Math.cos(mDeg*Math.PI/180.0)),
														 (float)(mPosition.y +mR*Math.sin(mDeg*Math.PI/180.0)));
	}
	
	public void moveRoll(float x, float y){
		mAnim.mValid = false;
		Vec2 v = new Vec2(x-mPosition.x, y-mPosition.y);
		float r = mR / Vec2.size(v);
		v.multiply(r);
		v.x += mPosition.x;
		v.y += mPosition.y;
		mSprite.translate(v);	
	}

	public void moveRoll(float dtdeg){
		mAnim.mValid = false;
		mDeg += dtdeg;		
		mSprite.translate((float)(mPosition.x +mR*Math.cos(mDeg*Math.PI/180.0)),
											(float)(mPosition.y + mR*Math.sin(mDeg*Math.PI/180.0)));		
	}

	*/
}
