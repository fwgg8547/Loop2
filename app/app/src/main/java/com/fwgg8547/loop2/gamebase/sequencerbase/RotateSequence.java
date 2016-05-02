package com.fwgg8547.loop2.gamebase.sequencerbase;

import com.fwgg8547.loop2.gamebase.util.*;
import com.fwgg8547.loop2.gamebase.modelbase.*;
import android.graphics.*;

public class RotateSequence
{
	public int frame;
	public float dpf; // degree per frame

	public RotateSequence(){}

	public RotateSequence(int frame, float dpf){
		this.frame = frame;
		this.dpf = dpf;
	}
}
