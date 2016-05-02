package com.fwgg8547.loop2.gamebase.modelbase;

import com.fwgg8547.loop2.gamebase.util.ReadersWriterLock;
import com.fwgg8547.loop2.gamebase.util.Lg;
import com.fwgg8547.loop2.gamebase.util.Vec2;
import com.fwgg8547.loop2.gamebase.sequencerbase.*;
import com.fwgg8547.loop2.gamebase.controllerbase.GameOverPatternBase;

import android.graphics.PointF;
import java.util.List;
import java.util.Map;
import org.dyn4j.dynamics.Body;
import java.util.*;

abstract public class CollisionModel extends SpriteModel implements ScrollableModel
{
	private final String TAG ="";// CollisionModel.class.getSimpleName();
	protected Map<Body, ItemBase> mItem2Body;
	protected List<NotifyChangeItem> mCallbackList;
	protected GameOverPatternBase mGameOverChecker;
	
	public CollisionModel(){
	}
	
	public interface NotifyChangeItem {
		public void addItem(CollidableItem i);
		public void removeItem(CollidableItem i);
	}
	
	public void initialize(ReadersWriterLock lock, int offset, int num, ModelGroup mg, int p)
	{
		super.initialize(lock, offset, num, mg, p);
		CollidableItem[] items = new CollidableItem[num];
		for(int i=0;i<num; i++){
			items[i] = new CollidableItem();
			items[i].mIsDeleted = true;
			items[i].mIndex = i;
		}
		mItemList.initialize(items);
		mItem2Body = new HashMap<Body,ItemBase>();
		mCallbackList = new ArrayList<NotifyChangeItem>();
	}
	
	public void clear(){
		if(mCallbackList != null){
			mCallbackList.clear();
			mCallbackList = null;
		}
		if(mItem2Body != null){
			mItem2Body.clear();
			mItem2Body = null;
		}
		mGameOverChecker = null;
		super.clear();
	}
	
	public void addChecker(GameOverPatternBase gop){
		mGameOverChecker = gop;
	}
	
	public void addCallback(NotifyChangeItem cb){
		mCallbackList.add(cb);
	}
	
	protected void setBody2ItemMap(Body b, ItemBase i){
		mItem2Body.put(b, i);
	}
	
	public ItemBase getItemFromBody(Body b){
		return mItem2Body.get(b);
	}

	public void removeBody2ItemMap(Body b){
		mItem2Body.remove(b);
	}
	
	public PointF getAngleCenter(int index){
		PointF pos = null;
		try{
			mLock.readLock();
			pos = ((CollidableItem)mItemList.get(index)).getAngleCenter();
		} catch (Exception e) {
			Lg.e(TAG, e.toString());
		} finally {
			mLock.readUnlock();
		}
		return pos;
	}

	public float getAngleDelta(int index){
		float angle = 0;
		try{
			mLock.readLock();
			angle = ((CollidableItem)mItemList.get(index)).getAngleDelta();
		} catch (Exception e) {
			Lg.e(TAG, e.toString());
		} finally {
			mLock.readUnlock();
		}
		return angle;
	}
	
	public void setOffsetVect(Vec2 v){
		try{
			mLock.writeLock();
			CollidableItem.setOffsetVect(v);
		} catch(Exception e){

		} finally {
			mLock.writeUnlock();
		}
	}

	@Override
	public ItemBase createItem()
	{
		CollidableItem it = (CollidableItem)super.createItem();
		if(it == null){
			return null;
		}
		Body b = new Body();
		it.initialize(b);
		
		setBody2ItemMap(b, it);
		
		Iterator t = mCallbackList.iterator();
		while(t.hasNext()){
			NotifyChangeItem cb = (NotifyChangeItem)t.next();
			cb.addItem(it);
		}
		return it;
	}

	@Override
	public void freeItem(int index)
	{
		Iterator it = mCallbackList.iterator();
		while(it.hasNext()){
			NotifyChangeItem cb = (NotifyChangeItem)it.next();
			CollidableItem i = (CollidableItem)mItemList.get(index);
			removeBody2ItemMap(i.getBody());
			cb.removeItem(i);
		}		
		super.freeItem(index);
	}
	
	@Override
	public ItemData getData(){
		ItemData id = new ItemData();
		try{
			mLock.readLock();
			int count = mItemList.size();
			id.mVert = new float[count*12];
			id.mColor = new float[count*16];
			id.mIndex = new short[count*6];
			id.mUv = new float[count*8];
			id.mVecsIndex =0;
			id.mColorIndex =0;
			id.mIndexIndex =0;
			id.mUvIndex = 0;

			for(int j=0, n=mItemList.size();j<n;j++){
				CollidableItem itm = (CollidableItem)mItemList.get(j);
				if(!itm.mIsDeleted){
					itm.updateVertix();

					// add offset to indices
					short[] src_s = addOffset((short)(id.mVecsIndex/3), itm.getIndices());
					System.arraycopy(
						src_s , 0,
						id.mIndex, id.mIndexIndex,
						src_s.length);

					id.mIndexIndex += src_s.length;

					float[] src_f = itm.getVertices();
					System.arraycopy(
						src_f, 0,
						id.mVert, id.mVecsIndex,
						src_f.length);

					id.mVecsIndex += src_f.length;

					src_f = itm.getColors();
					System.arraycopy(
						src_f , 0,
						id.mColor, id.mColorIndex,
						itm.getColors().length);

					id.mColorIndex += src_f.length;

					src_f = itm.getUvs();
					System.arraycopy(
						src_f, 0,
						id.mUv, id.mUvIndex,
						src_f.length);

					id.mUvIndex += src_f.length;
				}
			}
		} catch (Exception InterruptedException){
			Lg.e(TAG, "1 "+InterruptedException.toString());
			Lg.e(TAG, "  "+InterruptedException.getStackTrace()[0].getLineNumber());
		} finally {
			mLock.readUnlock();
		}

		return id;
	}
		
	// o4iginal collision
	/*
	public List<CollidableItem> getCollisionList(int level, float x, float y) {
		return mCollisionTree.getCollisionList(level, x, y);
	}

	public List<CollidableItem> getCollisionList(int level, int mnum){
		return mCollisionTree.getCollisionList(level, mnum);
	}	

	public List<CollidableItem> getCollisionList(CollidableItem itm){
		return mCollisionTree.getCollisionList(itm);
	}	

	public List<CollidableItem> getCollisionList(ModelBase m){
		List<CollidableItem> cl = null;

		try{
			mLock.writeLock();
			ItemArray ia = m.getItemArray();
			for(int i=0, n=ia.size(); i<n; i++) {
				CollidableItem itm = (CollidableItem)ia.get(i);
				List<CollidableItem> tmp = mCollisionTree.getCollisionList(itm);

				if(tmp != null && tmp.size() >0){
					itm.mIsDeleted = true; // delete collision ball
				}

				if(tmp !=null){
					if(cl == null){
						cl = tmp;
					} else {
						cl.addAll(tmp);
					}
				}
			}

		} catch(Exception e){
			Lg.stack(TAG, e.getStackTrace());
		} finally{
			mLock.writeUnlock();
		}

		return cl;
	}	

	public void refreshTree(ItemArray cl) {
		mCollisionTree.refreshTree(cl);
	}

	public void refreshTree(CollidableItem i)
	{
		mCollisionTree.refreshTree(i);
	}

	public void refreshTree()
	{
		refreshTree(mItemList);
	}
	*/
}
