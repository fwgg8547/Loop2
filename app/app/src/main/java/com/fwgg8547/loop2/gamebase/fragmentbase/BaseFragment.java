package com.fwgg8547.loop2.gamebase.fragmentbase;

import com.fwgg8547.loop2.gamebase.enginebase.*;
import android.app.*;

abstract public class BaseFragment extends Fragment
{
	protected GLEngineBase mEngine;
	protected Callback mCallback;
	
	public interface Callback {
		public void notifyEvent(int e);
	}
	
	public BaseFragment(GLEngineBase eg, Callback cb){
		mEngine = eg;
		mCallback = cb;
	}
	
	abstract public void reload();
}
