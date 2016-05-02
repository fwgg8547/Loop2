package com.fwgg8547.loop2.gamebase.controllerbase;

import com.fwgg8547.loop2.gamebase.layerbase.GlSurf;
import com.fwgg8547.loop2.gamebase.modelbase.ModelBase;
import com.fwgg8547.loop2.gamebase.modelbase.*;
import com.fwgg8547.loop2.gamebase.util.Lg;
import com.fwgg8547.loop2.gamebase.util.InnerEvent;
import com.fwgg8547.loop2.gamebase.util.*;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import android.view.MotionEvent;
import android.graphics.RectF;
import android.graphics.PointF;
import android.content.Context;
import android.view.GestureDetector;

import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionListener;
import org.dyn4j.collision.broadphase.*;
import org.dyn4j.collision.Collidable;
import com.fwgg8547.loop2.gamebase.sequencerbase.*;

abstract public class GLControllerBase 
implements GlSurf.Callback, 
CollisionManager.NotifyCollision, 
InnerEvent.Callback
{
	private final static String TAG = "GLControllerBase";

	public interface Callback {
		public enum Event {
			GameOver,
		}
		public void notifyEvent(Event e);
	}
	
	// ---- Base ----
	protected Context mContext;
	protected CollisionManager mCollisitionManager;
	private GestureDetector mGestureDetector;
	private List<ModelBase> mModels;
	protected List<CollisionModel> mCollisionModels;
	protected List<Callback> mCallbacks;
	protected List<ItemGeneraterBase> mGenrerater;
	
	private World mWorld;
	
	public GLControllerBase(Context ctx, 
	GestureDetector.SimpleOnGestureListener listener,
	CollisionManager cmg)
	{
		mContext = ctx;
		mModels = new ArrayList<ModelBase>();
		mCollisionModels = new ArrayList<CollisionModel>();
		mCollisitionManager = cmg;
		mGestureDetector = new GestureDetector(ctx, listener);
		mCallbacks = new ArrayList<Callback>();
		InnerEvent.register(this);
		mGenrerater = new ArrayList<ItemGeneraterBase>();
	}
	
	public void register(Callback cb){
		mCallbacks.add(cb);
	}

	public void setWorld(World w){
		mWorld = w;
	}
	
	public boolean onTouchEvent(MotionEvent event){
		if(mGestureDetector != null){
			mGestureDetector.onTouchEvent(event);
		}
		return true;
	}
	
	public boolean onUpdate() {
		return true;
	}
	
	public void addModel(ModelBase m){
		mModels.add(m);
	}
	
	public void addCollisionModel(CollisionModel m){
		mCollisionModels.add(m);
	}

	public void addGenerater(ItemGeneraterBase g){
		mGenrerater.add(g);
		
	}
	/*
	public void getCollisionItem(CollidableItem src){
		BroadphaseDetector<Body, BodyFixture> bd = mWorld.getBroadphaseDetector();
		List<BroadphasePair<Body, BodyFixture>> lbp = bd.detect();
		
		Iterator ite = lbp.iterator();
		while (ite.hasNext()){
			BroadphasePair<Body, BodyFixture> bp = 
				(BroadphasePair<Body, BodyFixture>)ite.next();
			CollidableItem i1 = (CollidableItem)bp.getCollidable1().getUserData();
			Lg.i(TAG, "collision i1 = " + i1.getType());
			
		}
		
	}
	*/
	@Override
	public void onReceive(InnerEvent.InnerMessage msg)
	{
		switch(msg.getMessage()){
			case GameOver:
				Iterator it = mCallbacks.iterator();
				while(it.hasNext()){
					Callback cb = (Callback)it.next();
					cb.notifyEvent(Callback.Event.GameOver);
				}
				break;
			default:
		}
	}

	abstract public void initialize();
	
	public void clear(){
		mContext = null;
		if(mModels != null){
			mModels.clear();;
			mModels = null;
		}
		if(mCollisionModels != null){
			mCollisionModels.clear();
			mCollisionModels = null;
		}
		mGestureDetector = null;
		if(mCallbacks != null){
			mCallbacks.clear();
		}
		mCallbacks = null;
		
		if(mGenrerater != null){
			mGenrerater.clear();
			mGenrerater = null;
		}
	}
}
