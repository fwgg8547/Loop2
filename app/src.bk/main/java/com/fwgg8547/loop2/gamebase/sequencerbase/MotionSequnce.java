package com.fwgg8547.loop2.gamebase.sequencerbase;

import com.fwgg8547.loop2.gamebase.util.*;
import com.fwgg8547.loop2.gamebase.modelbase.*;
import android.graphics.*;

public class MotionSequnce
{
	public int frame;
	public float ppf;
	public Vec2 direct;

	public MotionSequnce(){}
	
	public MotionSequnce(int frame, float ppf, Vec2 d){
		this.frame = frame;
		this.ppf = ppf;
		this.direct = d;
	}
}
