package com.fwgg8547.loop2;

import com.fwgg8547.loop2.gamebase.enginebase.GLEngineBase;
import com.fwgg8547.loop2.gamebase.controllerbase.GLControllerBase;
import com.fwgg8547.loop2.gamebase.controllerbase.GestureListenerBase;
import com.fwgg8547.loop2.gamebase.controllerbase.CollisionManager;
import com.fwgg8547.loop2.gamebase.util.ReadersWriterLock;
import com.fwgg8547.loop2.gamebase.layerbase.GlSurf;
import com.fwgg8547.loop2.gamebase.sequencerbase.ItemGeneraterBase;
import com.fwgg8547.loop2.gamebase.sequencerbase.ItemPattern;
import com.fwgg8547.loop2.gamebase.modelbase.TextModelBase;
import com.fwgg8547.loop2.gamebase.modelbase.CollisionModel;
import com.fwgg8547.loop2.gamebase.modelbase.ScreenModel;
import com.fwgg8547.loop2.gamebase.modelbase.ModelBase;
import com.fwgg8547.loop2.gamebase.modelbase.ModelGroup;
import com.fwgg8547.loop2.gamebase.scorebase.ScoreBase;
import com.fwgg8547.loop2.gamebase.modelbase.BackgroundModel;
import com.fwgg8547.loop2.model.*;
import com.fwgg8547.loop2.generater.*;
import com.fwgg8547.loop2.generater.BlockGenerater;
import com.fwgg8547.loop2.generater.ResourceFileReader;

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
import android.os.*;
import com.fwgg8547.loop2.gamebase.util.*;

public class GLEngine extends GLEngineBase
{
	private static final String TAG = GLEngine.class.getSimpleName();	
	
	public static final int PRIBACK = 0;
	public static final int PRIWALL = PRIBACK + 1;
	public static final int PRIBLOCK = PRIWALL + 1;
	public static final int PRIBATT = PRIBLOCK + 1;
	public static final int PRITEXT = PRIBATT  +1;
	public static final int PRIDEBUG = PRITEXT +1;
	// non collidable
	public static final int BACKMODELINDX = 0;
	public static final int TEXTMODELINDX = BACKMODELINDX +1;
	public static final int DEBUGMODELINDX = TEXTMODELINDX +1;
	// collidable
	public static final int WALLMODELINDX = 0;
	public static final int BATTMODELINDX = WALLMODELINDX + 1;
	public static final int BLOCKMODELINDX = BATTMODELINDX + 1;
	private GLController mController;	
	private BlockGenerater mBlockGenerater;
	private WallGenerater mWallGenerater;
	private Score mScore;
	
	public GLEngine(Context ctx,Callback cb) 
	{
		super(ctx, cb);
	}

	@Override
	public void initialize()
	{
		Lg.i(TAG, "initialize");
		ResourceFileReader rf = new ResourceFileReader(mContext);
		rf.loadPattern();
		rf.loadSequence();
		GameConfig.INSTANCE.initialize();
		super.initialize();
	}
	
	public void clear(){
		Lg.i(TAG, "clear");
		ResourceFileReader.clear();
		super.clear();
	}
	
	protected void createModels(List<ModelBase> ml, List<ItemGeneraterBase> gl, ReadersWriterLock lock)
	{
		Lg.i(TAG,"createModels");
		// 0;index 150 : text
		BackgroundModel bm = new BackgroundModel();
		bm.initialize(lock, 250, PRIBACK);
		ml.add(bm);
		
		TextModelBase txm = new TextModelBase();
		txm.initialize(lock, 10, PRITEXT);
		ml.add(txm);
		
		DebugText dt = new DebugText();
		dt.initialize(lock, 10, PRIDEBUG);
		ml.add(dt);
		
	}
	
	@Override
	protected void createCollisionModels(List<CollisionModel> ml, List<ItemGeneraterBase> gl, ReadersWriterLock lock)
	{
		Lg.i(TAG,"createCollisionModels");
		// 1;index 10: enemy
		WallModel2 wm = new WallModel2();
		wm.initialize(mLock, 10, null, PRIWALL);
		ml.add(wm);
		mWallGenerater = new WallGenerater(wm);
		mWallGenerater.setAutoMode(true);
		mWallGenerater.loadSequence(mContext, 1);
		mWallGenerater.loadLevel(mContext);
		gl.add(mWallGenerater);
		
		BatModel bm = new BatModel();
		bm.initialize(mLock, 0, null, PRIBATT);
		ml.add(bm);
		
		BlockModel blm = new BlockModel();
		blm.initialize(mLock, 100, null, PRIBLOCK);
		ml.add(blm);
		mBlockGenerater = new BlockGenerater(blm);
		mBlockGenerater.setAutoMode(true);
		mBlockGenerater.loadSequence(mContext, 1);
		mBlockGenerater.loadLevel(mContext);
		gl.add(mBlockGenerater);
	}
	
	@Override
	protected GLControllerBase createController(List<ModelBase>ml, List<CollisionModel>cl, CollisionManager cmg, List<ItemGeneraterBase>gen)
	{
		Lg.i(TAG,"createController");
		mController = new GLController(mContext, 
		createGestureListener(),
		cmg
		);
		mController.initialize();
		
		// collision model
		mController.addCollisionModel((WallModel2)cl.get(WALLMODELINDX));
		mController.addCollisionModel((BatModel)cl.get(BATTMODELINDX));
		mController.addCollisionModel((BlockModel)cl.get(BLOCKMODELINDX));
		mController.addGenerater(mBlockGenerater);
		return mController;
	}

	@Override
	protected ScoreBase createScore(List<ModelBase>ml){
		Lg.i(TAG,"createScore");
		mScore = new Score(mContext, mController);
		mScore.addTextModel((TextModelBase)ml.get(TEXTMODELINDX));
		mScore.initialize();
		return mScore;
	}
	
	@Override
	protected GlSurf createSurfaceView(List<ModelBase>ml, List<CollisionModel>cl ,GLControllerBase cnt)
	{
		Lg.i(TAG,"createSurfaceView");
		GlSurf gs = new GlSurf(mContext, mLock, ml, cl);
		gs.setCallback(cnt);
		return gs;
	}

	private GestureListener createGestureListener()
	{
		Lg.i(TAG,"createGestureListener");
		 return new GestureListener(mLock);
	}

}
