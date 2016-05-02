package com.fwgg8547.loop2.gamebase.util;

import android.graphics.*;

public class Vec2Rect
{
	public Vec2[] vec;
	public PointF[] point;
	
	public Vec2Rect(PointF one, PointF two, PointF three, PointF four){
		
		vec = new Vec2[] {
			new Vec2(one.x - two.x, one.y - two.y),
			new Vec2(two.x - three.x, two.y - three.y),
			new Vec2(three.x - four.x, three.y - four.y),
			new Vec2(four.x - one.x, four.y - one.y)
			};
			
		point = new PointF[] {
			one, two, three, four
		};
	}
	
	public boolean conflict(float x, float y){
		boolean isConflict = true;

		int i = 0;
		for(Vec2 v: vec){
			Vec2 p = new Vec2(x - point[i].x, y- point[i].y);
			if(Vec2.cross(v,p) < 0){
				//Lg.i(TAG, "cross < 0");
			} else{
				isConflict = false;
				//Lg.i(TAG, "out of sprote");
				break;
			}
			i++;
		}

		return isConflict;
	}
}
