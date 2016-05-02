package com.fwgg8547.loop2.model;
import com.fwgg8547.loop2.gamebase.modelbase.*;
import com.fwgg8547.loop2.gamebase.util.Vec2;

import android.graphics.*;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.dynamics.*;
import org.dyn4j.geometry.*;

public class WallItem2 extends CollidableItem
{
	private static final String TAG = WallItem2.class.getSimpleName();

	public WallItem2(){
		super();
	}
	
	public void setTrapezoid(PointF tl, PointF tr, PointF br, PointF bl)
	{
		Quadrilateral q = new Quadrilateral(
			tl, tr,  br, bl
		);
		super.setQuadrilateral(q);
	}
}
