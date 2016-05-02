package com.fwgg8547.loop2;

import com.fwgg8547.loop2.gamebase.scorebase.*;
import com.fwgg8547.loop2.gamebase.modelbase.*;
import com.fwgg8547.loop2.gamebase.util.*;
import android.content.Context;
import android.graphics.*;

public class Score extends ScoreBase
{
	private final static String TAG = Score.class.getSimpleName();
	private TextItemBase mCount;

	public Score(Context ctx, NotifyScore cb){
		super(ctx, cb);
	}

	@Override
	public void initialize()
	{
		super.initialize();
		mScore = 0;
		// score
		mCount = mTextModel.createItem("00", new PointF(850,1550), TextTexUtil.FontSize.SMALL);
	}

	public void add(int s){
		super.add(s);
		mCount.setText("" + mScore);
	}
	
	
}
