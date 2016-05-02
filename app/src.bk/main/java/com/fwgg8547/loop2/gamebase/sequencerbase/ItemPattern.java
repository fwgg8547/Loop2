package com.fwgg8547.loop2.gamebase.sequencerbase;

import android.graphics.*;
import com.fwgg8547.loop2.gamebase.modelbase.*;

public class ItemPattern
{
	public PointF mInitPos;
	public RectF mRect;
	public Quadrilateral mQuad;
	public int mType;
	public MotionSequnce[] mMotionPattern;
	public RotateSequence[] mRotatePattern;
	public ScaleSequence[] mScalePattern;
	public TextureSequence[] mTexturePattern;
	public int mItemId;

	public ItemPattern(){}
	
	public ItemPattern(PointF p, RectF r,MotionSequnce[] mp, RotateSequence[] rs ,ScaleSequence[] ma, TextureSequence[] mt){
		mInitPos = p;
		mRect = r;
		mMotionPattern = mp;
		mRotatePattern = rs;
		mScalePattern = ma;
		mTexturePattern = mt;
	}
}
