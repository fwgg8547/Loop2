package com.fwgg8547.loop2;

import com.fwgg8547.loop2.gamebase.fragmentbase.*;
import com.fwgg8547.loop2.gamebase.util.Lg;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.view.View.*;

public class MenuFragment extends BaseFragment
{
	private final static String TAG = "MenuFrag";
	
	public MenuFragment(GLEngine eg, Callback cb){
		super(eg, cb);
	}

	@Override
	public void reload()
	{
		// TODO: Implement this method
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Lg.i(TAG, "onCreateCiew");
		View v = inflater.inflate(R.layout.menufragment, container, false);
		Button sb = (Button) v.findViewById(R.id.btn_menu_start);
		sb.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				mCallback.notifyEvent(1);
			}
		});
		
		return v;
	}

	@Override
	public void onPause()
	{
		super.onPause();
	}

	@Override
	public void onResume()
	{
		super.onResume();
	}
	
}
