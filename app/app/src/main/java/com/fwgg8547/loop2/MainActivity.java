package com.fwgg8547.loop2;

import com.fwgg8547.loop2.gamebase.preferencebase.*;
import com.fwgg8547.loop2.gamebase.util.*;
import com.fwgg8547.loop2.gamebase.fragmentbase.*;
import com.fwgg8547.loop2.gamebase.dialogbase.*;

import android.view.Window;
import android.widget.RelativeLayout;
import android.app.*;
import android.os.*;
import android.nfc.*;
import android.view.*;
import android.content.*;

public class MainActivity extends Activity
{
	private GLEngine mEngine;
	private GameFragment mGameFragment;
	private MenuFragment mMenuFragment;
	
	private static final String TAG = MainActivity.class.getSimpleName();
	
	@Override
  protected void onCreate(Bundle savedInstanceState)
	{
		Lg.i(TAG, "onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
			
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		GameThreadMessageHandler.getInstance();
		mEngine = new GLEngine(this, new GLEngine.Callback(){
			public void notifyEvent(GLEngine.Callback.Event e){
				
				switch(e){
					case GameStart:
						Lg.i(TAG, "game start");
						break;
					case GameOver:
						Lg.i(TAG, "game over");
						GameThreadMessageHandler.getInstance().request(new Runnable(){
								public void run(){
									/*
									 FragmentManager fm = getFragmentManager();
									 fm.beginTransaction().replace(R.id.fragmentcontainer, mRankingFragment).commit();
									 mEngine.clear();
									 */

									mGameFragment.StopGameShowRanking();
								}
							});
						break;
				}
				
			}
		});

		mGameFragment = new GameFragment(mEngine, new BaseFragment.Callback(){
				public void notifyEvent(int e){
					FragmentManager fm = getFragmentManager();
					fm.beginTransaction().replace(R.id.fragmentcontainer, mGameFragment).commit();
				}
			});
			
		mMenuFragment = new MenuFragment(mEngine, new BaseFragment.Callback(){
			public void notifyEvent(int e){
				FragmentManager fm = getFragmentManager();
				fm.beginTransaction().replace(R.id.fragmentcontainer, mGameFragment).commit();
			}
		});		
			
		mEngine.initialize();
		PreferenceLoader.getInstance(this);
		FragmentManager fm = getFragmentManager();
		fm.beginTransaction().add(R.id.fragmentcontainer, mMenuFragment).commit();
		
	}
		
	@Override
		protected void onDestroy()
	{
		super.onDestroy();
		mEngine.clear();
	}

	@Override
	protected void onStart()
	{
		Lg.i(TAG, "onstart");
		super.onStart();
		//mEngine.start();
	}		
	
	@Override
		protected void onResume()
	{
		Lg.i(TAG, "onresume");
		super.onResume();
		//mEngine.resume();
	}

	@Override
		protected void onPause()
	{
		super.onPause();
		Lg.i(TAG, "onpause");
		//mEngine.pause();
	}

	@Override
		protected void onStop()
	{
		Lg.i(TAG, "onstop");
		super.onStop();
		//mEngine.stop();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event)
	{
		// TODO: Implement this method
		if(event.getAction() == KeyEvent.ACTION_DOWN){
			switch(event.getKeyCode()){
				case KeyEvent.KEYCODE_BACK:
					new CommonDialog.Builder(this)
					.message("exit ?")
					.positive("Yes")
					.negative("No")
					//.title("title")
					.cancelable(false)
					.callback(new CommonDialog.Callback(){
						public void onSuccess(int r, int w){
							Lg.i(TAG, "exit dialog" +w);
							switch(w){
								case DialogInterface.BUTTON1:
									// pos
									finish();
									break;
								case DialogInterface.BUTTON2:
									break;
							}
							
						}
						
						public void onCancel(int r, int w){
							Lg.i(TAG, "cancel "+w);
						}
					})
					.show();
					return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	
}
