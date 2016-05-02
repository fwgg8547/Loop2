package com.fwgg8547.loop2;

import com.fwgg8547.loop2.gamebase.fragmentbase.BaseFragment;
import com.fwgg8547.loop2.gamebase.util.*;
import com.fwgg8547.loop2.gamebase.layerbase.*;
import com.fwgg8547.loop2.gamebase.sequencerbase.*;
import com.fwgg8547.loop2.gamebase.modelbase.*;
import com.fwgg8547.loop2.gamebase.preferencebase.*;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.opengl.GLSurfaceView;
import android.widget.*;
import android.view.View.*;

public class GameFragment extends BaseFragment
{
	private final String TAG = "GAMEFRAG";
	private GLSurfaceView mGlSurfaceView;
	private View mRootView;
	private View mRankView;
	
	public GameFragment(GLEngine e, Callback cb){
		super(e, cb);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Lg.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Lg.i(TAG, "onCreateView");
		mRootView = inflater.inflate(R.layout.gamefragment, container, false);
		if(mGlSurfaceView == null){
			mGlSurfaceView = mEngine.getSurfaceView();
			RelativeLayout layout = (RelativeLayout) mRootView.findViewById(R.id.gamelayout);
			RelativeLayout.LayoutParams glParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			layout.addView(mGlSurfaceView, glParams);
		}
		
		mRankView = mRootView.findViewById(R.id.resultlayout);
		Button retry = (Button)mRankView.findViewById(R.id.gamefragmentRetryBtn);
		retry.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				RelativeLayout layout =  (RelativeLayout)mRootView.findViewById(R.id.gamelayout);
				layout.removeView(mGlSurfaceView);
				mEngine.clear();
				mEngine.initialize();
				mGlSurfaceView = mEngine.getSurfaceView();
				RelativeLayout.LayoutParams glParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
				layout.addView(mGlSurfaceView, glParams);
				mRankView.setVisibility(View.GONE);
				//onStart();
				//onResume();
				mEngine.start();
			}
		});
		
		int hi = PreferenceLoader.getHiscore();
		int curr = PreferenceLoader.getCurrentScore();
		TextView tv = (TextView)mRankView.findViewById(R.id.gamefragmentTextView1);
		tv.setText("Hi " + hi + " : "+curr);
		return mRootView;
	}
	
	public void StopGameShowRanking(){
		onPause();
		int hi = PreferenceLoader.getHiscore();
		int curr = PreferenceLoader.getCurrentScore();
		TextView tv = (TextView)mRankView.findViewById(R.id.gamefragmentTextView1);
		tv.setText("Hi " + hi + " : "+curr);
		mRankView.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onStart()
	{
		Lg.i(TAG, "onstart");
		super.onStart();
		mEngine.start();
		mGlSurfaceView = mEngine.getSurfaceView();
	}		
	
	@Override
	public void onResume()
	{
		// TODO: Implement this method
		super.onResume();
		Lg.i(TAG, "onResume");
		//mGlSurfaceView.onResume();
		mEngine.resume();
		if(mGlSurfaceView != null){
			mGlSurfaceView.onResume();
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();
		Lg.i(TAG, "onpause");
		if(mGlSurfaceView != null){
			mGlSurfaceView.onPause();
		}
		mEngine.pause();
		
	}

	@Override
	public void onStop()
	{
		Lg.i(TAG, "onstop");
		super.onStop();
		mEngine.stop();
		mGlSurfaceView = null;
	}
	
	@Override
	public void reload()
	{
		// TODO: Implement this method
	}
	
}
