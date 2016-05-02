package com.fwgg8547.loop2.gamebase.sequencerbase;

import android.graphics.PointF;

public class BlockPatternBase
{
	public class Pat{
		public PointF mInitPos;
		public MotionSequnce[] mMotionPattern;
		public ScaleSequence[] mScalePattern;
		public TextureSequence[] mTexturePattern;
		public int mTextureIndex;

		public Pat(PointF p, MotionSequnce[] mp, ScaleSequence[] ma, TextureSequence[] mt){
			mInitPos = p;
			mMotionPattern = mp;
			mScalePattern = ma;
			mTexturePattern = mt;
		}
	}
}
