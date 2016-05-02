package com.fwgg8547.loop2.gamebase.enginebase;

import com.fwgg8547.loop2.*;
import com.fwgg8547.loop2.gamebase.controllerbase.GLControllerBase.Callback.*;
import com.fwgg8547.loop2.gamebase.preferencebase.*;
import com.fwgg8547.loop2.gamebase.controllerbase.GLControllerBase;
import com.fwgg8547.loop2.gamebase.controllerbase.CollisionManager;
import com.fwgg8547.loop2.gamebase.modelbase.CollisionModel;
import com.fwgg8547.loop2.gamebase.modelbase.ScreenModel;
import com.fwgg8547.loop2.gamebase.modelbase.ModelBase;
import com.fwgg8547.loop2.gamebase.modelbase.ModelGroup;
import com.fwgg8547.loop2.gamebase.modelbase.ItemArray;
import com.fwgg8547.loop2.gamebase.modelbase.CollidableItem;
import com.fwgg8547.loop2.gamebase.layerbase.GlSurf;
import com.fwgg8547.loop2.gamebase.sequencerbase.ItemGeneraterBase;
import com.fwgg8547.loop2.gamebase.scorebase.ScoreBase;
import com.fwgg8547.loop2.gamebase.util.Lg;
import com.fwgg8547.loop2.gamebase.util.ReadersWriterLock;

import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledFuture;
import java.util.Iterator;

import android.content.Context;
import android.opengl.GLSurfaceView;

import org.dyn4j.dynamics.World;
import android.os.*;

abstract public class GLEngineBase 
	implements ScreenModel.Callback, GLControllerBase.Callback
{
	private static final String TAG = GLEngineBase.class.getSimpleName();
	public static final long FRAME_RATE = 60;
	public static final long FRATE_PRIOD_MS = 1000/FRAME_RATE;

	protected State mState;
	protected Context mContext;
	
	protected Callback mCallback;
	protected ReadersWriterLock mLock;

	private GLControllerBase mController;
	private ScoreBase mScore;
	private List<ItemGeneraterBase> mGenerater;
	private List<ModelBase> mModelList;
	private List<CollisionModel> mCollisionModelList;
	private CollisionManager mCollisionManager;
	private ModelGroup mModelGroup;
	private GlSurf mSurfaceView;
	private PriodWork mPriodWork;
	private ScheduledExecutorService mSchedular;
	private ScheduledFuture<?> mFuture;
	
	// World
	private long lastTime;
	private World mWorld;
	
	protected enum State {
		MENU,
		PLAYING,
		PAUSE,
		STOP,
		SCORE,
	}
	
	public interface Callback {
		public enum Event{
			GameStart,
			GameOver,
		};
		
		public void notifyEvent(Event event);
	}
	
	private class PriodWork implements Runnable
	{
		private boolean mIsFirst = true;

		@Override
		public void run()
		{
			onUpdate(mIsFirst);
			mIsFirst = false;
		}
	}
	
	public GLEngineBase(Context ctx,Callback cb) 
	{
		mContext = ctx;
		mCallback = cb;
		mState = State.MENU;
		
	}
	
	public void initialize()
	{
		Lg.i(TAG, "engine init");
		mState = State.MENU;
		mLock = new ReadersWriterLock();
		mModelList = new ArrayList<ModelBase>();
		mCollisionModelList = new ArrayList<CollisionModel>();
		mGenerater = new ArrayList<ItemGeneraterBase>();
		mModelGroup = new ModelGroup();
		
		createModels(mModelList, mGenerater, mLock);
		createCollisionModels(mCollisionModelList, mGenerater, mLock);
		mWorld = new World();
		mCollisionManager = new CollisionManager(mWorld);
		
		mController = createController(mModelList, mCollisionModelList, mCollisionManager, mGenerater);
		mController.setWorld(mWorld);
		mCollisionManager.addCallback(mController);
		mScore = createScore(mModelList);
		
		ScreenModel.getInstance().setCallback(this);
		
		//world initialize
		
		Iterator it = mCollisionModelList.iterator();
		while(it.hasNext()){
			CollisionModel cm = (CollisionModel)it.next();
			cm.addCallback(mCollisionManager);
			cm.setScore(mScore);
			mCollisionManager.addModel(cm);
			ItemArray ia = cm.getItemArray();
			for(int i=0, n=ia.size();i<n;i++){
				CollidableItem itm = (CollidableItem)ia.get(i);
				mWorld.addBody(itm.getBody());
			}
		}
		mWorld.addListener(mCollisionManager);
		mWorld.setGravity(World.ZERO_GRAVITY);
		
		mController.register(this);
		mController.register(mScore);
		// do update before surface view start
		onUpdate(true);
		
		mSurfaceView = createSurfaceView(mModelList, mCollisionModelList, mController);
		
		mSchedular = Executors.newSingleThreadScheduledExecutor();
		mPriodWork = new PriodWork();
		Lg.i(TAG, "engine init end");
	}
	
	public void clear()
	{
		Lg.i(TAG, "eng finalize start");
		if(mSurfaceView!=null){
			//mSurfaceView.onPause();
		}
		if(mSchedular != null){
			mSchedular.shutdownNow();
		}
		mSchedular = null;
		mPriodWork = null;
		if(mWorld != null){
			mWorld.removeAllBodiesAndJoints();
			mWorld.removeAllListeners();
		}
		mWorld = null;
		
		if(mCollisionModelList != null){
			Iterator it = mCollisionModelList.iterator();
			while(it.hasNext()){
				CollisionModel cm = (CollisionModel)it.next();
				cm.clear();
			}
			mCollisionModelList.clear();
		}
		mCollisionModelList = null;
		
		if(mModelList != null){
			Iterator it = mModelList.iterator();
			while(it.hasNext()){
				ModelBase cm = (ModelBase)it.next();
				cm.clear();
			}
			mModelList.clear();
		}
		mModelList = null;
		
		//mSurfaceView.finalize();
		mSurfaceView = null;
		
		if(mScore != null){
			mScore.clear();
		}
		mScore = null;
		
		if(mController != null){
			mController.clear();
		}
		mController = null;
		if(mCollisionManager != null){
			mCollisionManager.clear();
		}
		mCollisionManager = null;
		mModelGroup = null;
		
		if(mGenerater != null){
			Iterator it = mGenerater.iterator();
			while(it.hasNext()){
				ItemGeneraterBase cm = (ItemGeneraterBase)it.next();
				cm.clear();
			}
			mGenerater.clear();
		}
		mGenerater = null;
		mLock = null;
	}

	public void gameStart(){
		
	}
	
	public void gameStop(){
		PreferenceLoader.setScore(mScore.getCurrentScore());
		mCallback.notifyEvent(Callback.Event.GameOver);
	}
	
	public GLSurfaceView getSurfaceView() 
	{
		return mSurfaceView;
	}

	public void start() {
		Lg.i(TAG, "start");
		mCallback.notifyEvent(Callback.Event.GameStart);
		
		
		mState = State.PLAYING;
		mFuture = mSchedular.scheduleAtFixedRate(mPriodWork, 0, FRATE_PRIOD_MS, TimeUnit.MILLISECONDS);

	}

	public void restart(){
		Lg.i(TAG, "restart");
		if(mState == State.PLAYING) {
			mFuture = mSchedular.scheduleAtFixedRate(mPriodWork, 0, FRATE_PRIOD_MS, TimeUnit.MILLISECONDS);
		} else {
			mState = State.PLAYING;
		}
	}

	public void pause(){
		Lg.i(TAG, "pause");
		mState = State.PAUSE;
		if(mFuture != null){
			mFuture.cancel(true);
			mFuture = null;
		}
	}

	public void resume(){
		Lg.i(TAG, "resume");
		if(mFuture == null && mSchedular != null &&
			 mState == State.PLAYING){
			mFuture = mSchedular.scheduleAtFixedRate(mPriodWork, 0, FRATE_PRIOD_MS, TimeUnit.MILLISECONDS);
		} else{
			mState = State.PLAYING;
		}
	}

	public void stop() {
		Lg.i(TAG, "stop");
		mState =State.STOP;
		if(mFuture != null){
			mFuture.cancel(true);
			mFuture = null;
		}
	}

	@Override
	public void onScreenChange()
	{
		Lg.i(TAG, "onScreenChange");
		//mCollistionTree.initialize(ScreenModel.getInstance().getScreenInfo());
	
		Iterator it = mModelList.iterator();
		while(it.hasNext()){
			ModelBase s = (ModelBase)it.next();
			if(s instanceof CollisionModel){
				//mCollistionTree.registModel((CollisionModel)s);
			}
		}
	}

	@Override
	public void notifyEvent(GLControllerBase.Callback.Event e)
	{
		switch(e){
			case GameOver:
				//mSurfaceView.onPause();
				
				mCallback.notifyEvent(Callback.Event.GameOver);
				
				break;
			default:
		}
	}
	
	private void onUpdate(boolean isFirst)
	{
		if(mState != State.PLAYING){
			return;
		}
		if(isFirst){}
		
		long current = System.nanoTime();
		long diff = current - lastTime;
		lastTime = current;
		double elapsed = (double)diff/1000000000.0;
		
		if(updateControl()){
			updateGenerater();
			updateModel();
			updateWorld(elapsed);
			updateScore();			
		}
	}
	
	private void updateModel()
	{
		Iterator it = mModelList.iterator();
		while(it.hasNext()){
			ModelBase m = (ModelBase)it.next();
			m.onUpdate();
		}
		
		it = mCollisionModelList.iterator();
		while(it.hasNext()){
			CollisionModel m = (CollisionModel)it.next();
			m.onUpdate();
		}
	}

	private void updateWorld(double elapsed){
		mWorld.setUpdateRequired(true);
		mWorld.update(elapsed);
	}
	
	private boolean updateControl()
	{
		if(mController != null){
			return mController.onUpdate();
		}
		return false;
	}

	private void updateGenerater(){
		boolean isReady = true;
		Iterator it = mGenerater.iterator();
		while(it.hasNext()){
			ItemGeneraterBase m = (ItemGeneraterBase)it.next();
			m.tick();
		}
		return;
	}
	
	private void updateScore(){
		if(mScore != null){
			mScore.onUpdate();
		}
	}
	
	abstract protected void createModels(List<ModelBase>ml, List<ItemGeneraterBase> gl, ReadersWriterLock lock);
	abstract protected void createCollisionModels(List<CollisionModel>ml, List<ItemGeneraterBase> gl, ReadersWriterLock lock);
	abstract protected GLControllerBase createController(List<ModelBase>ml, List<CollisionModel>cl, CollisionManager cmg, List<ItemGeneraterBase> gen);
	abstract protected ScoreBase createScore(List<ModelBase>ml);
	abstract protected GlSurf createSurfaceView(List<ModelBase>ml, List<CollisionModel> cl,GLControllerBase cnt);
	
}
