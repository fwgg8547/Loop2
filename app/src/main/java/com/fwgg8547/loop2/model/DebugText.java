package com.fwgg8547.loop2.model;
import com.fwgg8547.loop2.gamebase.modelbase.*;
import com.fwgg8547.loop2.gamebase.util.*;
import android.graphics.PointF;

public class DebugText extends TextModelBase
{

	static private TextItemBase mCount = null;
	
	@Override
	public void initialize(ReadersWriterLock lock, int num, int p)
	{
		// TODO: Implement this method
		super.initialize(lock, num, p);
		mCount = createItem("--", new PointF(100,300), TextTexUtil.FontSize.SMALL);
		
	}
	
	static public void set(String text){
		mCount.setText(text);
	}	
	
}
