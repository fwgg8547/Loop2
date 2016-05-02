package com.fwgg8547.loop2.gamebase.controllerbase;

import com.fwgg8547.loop2.gamebase.modelbase.ModelBase;
import com.fwgg8547.loop2.gamebase.modelbase.ItemBase;
import com.fwgg8547.loop2.gamebase.modelbase.CollisionModel;
import com.fwgg8547.loop2.gamebase.modelbase.*;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.CollisionAdapter;
import org.dyn4j.dynamics.*;
import org.dyn4j.dynamics.contact.*;
import org.dyn4j.collision.narrowphase.*;
import org.dyn4j.collision.manifold.*;
import org.dyn4j.collision.broadphase.*;
import org.dyn4j.geometry.Geometry;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import android.graphics.RectF;

public class CollisionManager extends CollisionAdapter implements CollisionModel.NotifyChangeItem
{
	public interface NotifyCollision {
		public boolean broadCollision(ItemBase i1, ItemBase i2);
		public boolean narrowCollision(ItemBase i1, ItemBase i2);
	}
	
	private final static String TAG = CollisionManager.class.getSimpleName();
	private World mWorld;
	private List<CollisionModel> mModelList;
	private NotifyCollision mCallback;
	
	public CollisionManager(World wd) {
		mWorld = wd;
		mModelList = new ArrayList<CollisionModel>();
	}
	
	public void clear(){
		if(mModelList != null){
			mModelList.clear();
			mModelList = null;
		}
		
		mWorld = null;
		mCallback = null;
	}
	
	public void addModel(CollisionModel m){
		mModelList.add(m);
	}
	
	public void addCallback(NotifyCollision cb){
		mCallback = cb;
	}
	
	// collidable.NotifyChangeItem
	@Override
	public void addItem(CollidableItem i)
	{
		Body b = i.getBody();
		mWorld.addBody(b);
	}

	@Override
	public void removeItem(CollidableItem i)
	{
		mWorld.removeBody(i.getBody());
	}

	public List<CollidableItem> getCollisionItem(RectF rect){
		List<CollidableItem> cl = new ArrayList<CollidableItem>();

		Body rectBody = new Body();
		rectBody.addFixture(new BodyFixture(Geometry.createRectangle(rect.width(), rect.height())));
		rectBody.translate(rect.centerX(), rect.centerY());
		UserData ud = new UserData();
		ud.setIsRect(true);
		rectBody.setUserData(ud);

		mWorld.addBody(rectBody);
		mWorld.setUpdateRequired(true);

		BroadphaseDetector<Body, BodyFixture> bd = mWorld.getBroadphaseDetector();
		List<BroadphasePair<Body, BodyFixture>> lbp = bd.detect();

		Iterator ite = lbp.iterator();
		while (ite.hasNext()){
			BroadphasePair<Body, BodyFixture> bp =
					(BroadphasePair<Body, BodyFixture>)ite.next();

			UserData ud1 = (UserData)bp.getCollidable1().getUserData();
			UserData ud2 = (UserData)bp.getCollidable2().getUserData();

			if(ud1.getIsRect()){
				cl.add(ud2.getItem());
			} else if(ud2.getIsRect()){
				cl.add(ud1.getItem());
			}
		}

		mWorld.removeBody(rectBody);
		return cl;

	}

	public List<CollidableItem> getCollisionItem(CollidableItem src){
		
		List<CollidableItem> cl = new ArrayList<CollidableItem>();
		BroadphaseDetector<Body, BodyFixture> bd = mWorld.getBroadphaseDetector();
		List<BroadphasePair<Body, BodyFixture>> lbp = bd.detect();

		Iterator ite = lbp.iterator();
		while (ite.hasNext()){
			BroadphasePair<Body, BodyFixture> bp = 
				(BroadphasePair<Body, BodyFixture>)ite.next();
				
			UserData ud1 = (UserData)bp.getCollidable1().getUserData();
			UserData ud2 = (UserData)bp.getCollidable2().getUserData();
			
			if(ud1.getItem().getType() == src.getType() &&
			ud1.getItem().mIndex == src.mIndex){
				
				cl.add(ud2.getItem());
		 } else if(ud2.getItem().getType() == src.getType() &&
								ud2.getItem().mIndex == src.mIndex){
				cl.add(ud1.getItem());
			}
		}
	return cl;
	}
	
	// CollisionAdapter
	@Override
	public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2)
	{
		// find item by body
		ItemBase i1 = null, i2 = null;
		Iterator it = mModelList.iterator();
		while(it.hasNext()){
			CollisionModel cm = (CollisionModel)it.next();
			ItemBase ib1 = cm.getItemFromBody(body1);
			ItemBase ib2 = cm.getItemFromBody(body2);
			if(ib1 != null){
				if(i1 == null){
					i1 = ib1;
				} else {
					i2 = ib1;
					break;
				}
			}
			if(ib2 != null){
				if(i1 == null) {
					i1 = ib2;
				} else {
					i2 = ib2;
					break;
				}
			}
		}

		return mCallback.broadCollision(i1, i2);
	}


	@Override
	public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Manifold manifold)
	{
		// TODO: Implement this method
		return super.collision(body1, fixture1, body2, fixture2, manifold);
	}

	@Override
	public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Penetration penetration)
	{
		// find item by body
		ItemBase i1 = null, i2 = null;
		Iterator it = mModelList.iterator();
		while(it.hasNext()){
			CollisionModel cm = (CollisionModel)it.next();
			ItemBase ib1 = cm.getItemFromBody(body1);
			ItemBase ib2 = cm.getItemFromBody(body2);
			if(ib1 != null){
				if(i1 == null){
					i1 = ib1;
				} else {
					i2 = ib1;
					break;
				}
			}
			if(ib2 != null){
				if(i1 == null) {
					i1 = ib2;
				} else {
					i2 = ib2;
					break;
				}
			}
		}

		// stop collision process
		return mCallback.narrowCollision(i1, i2);
	}

	@Override
	public boolean collision(ContactConstraint contactConstraint)
	{
		// TODO: Implement this method
		return super.collision(contactConstraint);
	}


	/*
	@Override
	protected boolean doCollisonCheckBroad(ItemBase item1, ItemBase item2)
	{
		if(item1.getType() != GLEngine.BALLMODELIDX ||
			 item2.getType() != GLEngine.BALLMODELIDX){
			return false;
		}
		return true;
	}

	@Override
	protected boolean doCollisonCheckNarrow(ItemBase item1, ItemBase item2)
	{
		Lg.i(TAG, "HIT");
		return false;
	}
	*/
}
