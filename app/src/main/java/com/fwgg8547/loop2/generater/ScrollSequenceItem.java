package com.fwgg8547.loop2.generater;

import com.fwgg8547.loop2.gamebase.sequencerbase.*;
import com.fwgg8547.loop2.gamebase.util.Vec2;
import com.fwgg8547.loop2.*;

public class ScrollSequenceItem extends SequenceItemBase
{
	public static final int FRAME = 12;
	public static final int PPF = 15;
	public static final int DIST = FRAME*PPF;
	
	public static final ScrollSequenceItem UP = new ScrollSequenceItem(
		ScrollManager.Direct.UP, FRAME, PPF, new Vec2(0,-1)
	);
	public static final ScrollSequenceItem DOWN = new ScrollSequenceItem(
		ScrollManager.Direct.DOWN, 12, 15, new Vec2(0, 1)
	);
	public static final ScrollSequenceItem LEFT = new ScrollSequenceItem(
		ScrollManager.Direct.LEFT, 12, 15, new Vec2(1, 0)
	);
	public static final ScrollSequenceItem RIGHT = new ScrollSequenceItem(
		ScrollManager.Direct.RIGHT, 12, 15, new Vec2(-1,0)
	);
	public static final ScrollSequenceItem NONE = new ScrollSequenceItem(
		ScrollManager.Direct.NONE, 12, 15, new Vec2(0, 0)
	);
	
	public ScrollManager.Direct mDirectName;
	public Vec2 mDirect;
	public float mPpf;
	
	public ScrollSequenceItem(ScrollManager.Direct d, int frame, float ppf, Vec2 v){
		super(frame);
		mDirectName = d;
		mDirect = v;
		mPpf = ppf;
	}
}
