package com.fwgg8547.loop2.gamebase.sequencerbase;
import android.graphics.*;

public class TextureSequence
{
	public int frame;
	public float[] uv;
	
	public TextureSequence(int frame, float[] uv){
		this.frame = frame;
		this.uv = uv;
	}
}
