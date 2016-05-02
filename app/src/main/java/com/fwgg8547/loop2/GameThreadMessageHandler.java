package com.fwgg8547.loop2;

import android.os.Message;
import android.os.Handler;
import android.content.Context;
import android.app.Activity;
import com.fwgg8547.loop2.gamebase.enginebase.GLEngineBase;
import com.fwgg8547.loop2.gamebase.util.Lg;
import java.util.*;

public class GameThreadMessageHandler extends Handler
{
		private final static int CALLBACK = 1;		
		private final static String TAG = GameThreadMessageHandler.class.getSimpleName();
		private static GameThreadMessageHandler instance;
		private static Activity mAct;
		private static boolean mIsPaused;
		private static LinkedList<MsgObject> mQueue = new LinkedList<MsgObject>();
		
		public class MsgObject {
				public int mId;
				public Runnable mCallback;
		}
  
		public static GameThreadMessageHandler getInstance() {
				if(instance == null){
						instance = new GameThreadMessageHandler();
				}
				return instance;
		}
  
		private GameThreadMessageHandler(){
			super();
		}

		public void resume(){
			mIsPaused = false;
			while(mQueue.size() > 0){
				MsgObject msgObj = mQueue.pollFirst();
				Message msg = Message.obtain();
        msg.obj = msgObj;
        sendMessage(msg);
			}
		}
		
		public void pause(){
			mIsPaused = true;
		}
		
		public void setActivity(Activity act){
			mAct = act;
		}
		
		@Override
		public void handleMessage(Message msg)
    {
			if(mIsPaused){
				Message copy = Message.obtain();
				copy.copyFrom(msg);
				mQueue.addFirst((MsgObject)(msg.obj));
			} else {
				processMessage(msg);
			}
    }
  
		public void processMessage(Message msg){
			MsgObject o = (MsgObject)(msg.obj);
			switch (o.mId) {
        case CALLBACK:
					Lg.i(TAG, "recive createitem  " + o.mId);
					o.mCallback.run();

					break;

        default:
					Lg.e(TAG, "unknown event");
			}
		}
		
		public  void request(Runnable cb){
        MsgObject o = new MsgObject();
        o.mId = CALLBACK;
				o.mCallback = cb;
				Message msg = Message.obtain();
				msg.obj = o;
				sendMessage(msg);
    }
		
}
