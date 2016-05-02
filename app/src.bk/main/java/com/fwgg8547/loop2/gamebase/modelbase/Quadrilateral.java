package com.fwgg8547.loop2.gamebase.modelbase;

import android.graphics.*;

public class Quadrilateral
{
	public PointF topleft;
	public PointF topright;
	public PointF bottomleft;
	public PointF bottomright;
	
	public Quadrilateral(
	PointF tl, 
	PointF tr,
	PointF br,
	PointF bl 
	){
		topleft = tl;
		topright = tr;
		bottomleft = bl;
		bottomright = br;
	}
	
	public PointF[] getVertices() {
		return new PointF[]{
			topleft, topright, bottomleft, bottomright
		};
	}
	
	public void setVertices(PointF tl, 
													PointF tr, 
													PointF br,
													PointF bl){
		topleft = tl;
		topright = tr;
		bottomleft = bl;
		bottomright = br;
	}
	
	public float getHight(){
		float top, bottom;
		
		top = (topleft.y > topright.y)? topleft.y:topright.y;
		bottom = (bottomleft.y > bottomright.y)? bottomleft.y:bottomright.y;
		
		return top-bottom;
	}
}
