package com.fwgg8547.loop2.model;
import com.fwgg8547.loop2.gamebase.modelbase.*;
import com.fwgg8547.loop2.gamebase.util.Vec2;

import android.graphics.*;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.dynamics.*;
import org.dyn4j.geometry.*;

public class WallItem extends CollidableItem
{
	private static final String TAG = CollidableItem.class.getSimpleName();
	
	public WallItem(){
		super();
	}

	@Override
	protected PointF doMoveMotion()
	{
		mAnimSequencer.tick();
		PointF pos = null;
		
		if(mDirectOffset != null){
			pos = mSprite.translateDelta(mDirectOffset);
			mBody.translate(mDirectOffset.x, mDirectOffset.y);
		}
		
		Vec2 tmp = mAnimSequencer.getVector();
		
		if(tmp != null){
			pos = mSprite.translateDelta(tmp);
			mBody.translate(tmp.x, tmp.y);
		}

		Vec2 tmp2 = mAnimSequencer.getScale();
		if(tmp2 != null){
			mSprite.scale(tmp2.x, tmp2.y);
			//mSprite.scaleDelta(tmp2.x, tmp2.y);
		}

		float[] tmp3 = mAnimSequencer.getTextureUv();
		if(tmp3 != null){
			mSprite.setTextureUv(tmp3);
		}
		return super.doMoveMotion();
	}
	
	
	
}
