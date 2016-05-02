package com.fwgg8547.loop2.gamebase.modelbase;

import com.fwgg8547.loop2.gamebase.util.Vec2;
import com.fwgg8547.loop2.gamebase.sequencerbase.AnimationSequencer;
import android.graphics.PointF;

public class NonCollidableItem extends ItemBase
{
	public NonCollidableItem(){}
	
	public void initialize(){
		mIndex =0;
		mIsDeleted = false;
		mAnimType = AnimType.NONE;
		mAnimSequencer = new AnimationSequencer();
	}
	
	public void clear(){
		if(mAnimSequencer != null){
			mAnimSequencer.clear();
			mAnimSequencer = null;
		}
		super.clear();
	}
	
	@Override
	protected PointF doMoveMotion(){
		mAnimSequencer.tick();

		Vec2 tmp = mAnimSequencer.getVector();
		PointF pos = null;
		if(tmp != null){
			pos = mSprite.translateDelta(tmp);
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

		return pos;		
	}	
}
