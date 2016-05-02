package com.fwgg8547.loop2.gamebase.dialogbase;

import android.app.*;
import android.os.*;
import android.view.*;
import javax.xml.parsers.*;
import android.widget.*;
import android.view.InputQueue.*;
import android.content.*;
import android.view.View.*;

public class CommonDialog extends DialogFragment
{
	private static final String TAG = CommonDialog.class.getSimpleName();
	public CommonDialog(){
		
	}
	
	public interface Callback {
		void onSuccess(int r, int w);
		void onCancel(int r, int w);
	}
	
	public static class Builder {
		final Fragment mParent;
		final Activity mActivity;
		
		String mTitle;
		String mMessage;
		int mRequestCode = -1;
		String mPositiveLabel;
		String mNegativeLabel;
		boolean mCancelable;
		
		CommonDialog.Callback mCallback;
		
		public <F extends Fragment> Builder(final Fragment p){
			mParent = p;
			mActivity = null;
		}
		
		public <F extends Activity> Builder(final Activity p){
			mActivity = p;
			mParent = null;
		}
		
		public Builder callback(CommonDialog.Callback cb){
			mCallback = cb;
			return this;
		}
		
		public Builder title(final String t){
			mTitle = t;
			return this;
		}
		
		public Builder message(final String m){
			mMessage = m;
			return this;
		}
		
		public Builder requestCode(final int req){
			mRequestCode = req;
			return this;
		}
		
		public Builder positive(final String m)
		{
			mPositiveLabel = m;
			return this;
		}
		
		public Builder negative(final String m){
			mNegativeLabel = m;
			return this;
		}
		
		public Builder cancelable(final boolean cancelable) {
			mCancelable = cancelable;
			return this;
		}
		
		public void show(){
			Bundle b = new Bundle();
			b.putString("TITLE", mTitle);
			b.putString("MESSAGE", mMessage);
			b.putString("POSITIVE", mPositiveLabel);
			b.putString("NEGATIVE", mNegativeLabel);
			b.putInt("REQUESTCODE", mRequestCode);
			b.putBoolean("CANCELABLE",mCancelable);
			final CommonDialog cd = new CommonDialog();
			if(mParent != null){
				cd.setTargetFragment(mParent, 1);
			}
			cd.setArguments(b);
			cd.mCallback = this.mCallback;
			if(mParent != null){
				cd.show(mParent.getChildFragmentManager(), "tag");
			} else {
				cd.show(mActivity.getFragmentManager(), "");
			}
		}
	}
	
	private Callback mCallback;

	@Override
	public void onAttach(Activity activity)
	{
		// TODO: Implement this method	
		super.onAttach(activity);
		
	}

	@Override
	public void onDetach()
	{
		// TODO: Implement this method
		super.onDetach();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface i, int w){
				if(mCallback != null){
					mCallback.onSuccess(getRequestCode(), w);
				}
			}
		};
		// TODO: Implement this method
		String title = getArguments().getString("TITLE");
		String message = getArguments().getString("MESSAGE");
		String pos = getArguments().getString("POSITIVE");
		String neg = getArguments().getString("NEGATIVE");
		boolean cancelable = getArguments().getBoolean("CANCELABLE");
		setCancelable(cancelable);
		
		AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(pos, listener);
		builder.setNegativeButton(neg, listener);
		
		
		return builder.create();
	}
	
	private int getRequestCode() {
		return getArguments().containsKey("REQUESTCODE") ? getArguments().getInt("request_code") : getTargetRequestCode();
	}
	
}
