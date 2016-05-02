package com.fwgg8547.loop2.gamebase.util;

import java.util.List;
import java.util.LinkedList;

public class InnerEvent
{
	//private static LinkedList<InnerMessage> mQueue = new LinkedList<InnerMessage>();
	private static Callback mCallback;
	
	public static class InnerMessage {
		public enum Event {
			GameOver,
		}
		
		private Event mEvent;
		
		public InnerMessage(Event e){
			mEvent = e;
		}
		
		public Event getMessage(){
			return mEvent;
		}
	}
	
	public interface Callback {
		public void onReceive(InnerMessage msg);
	}
	
	public InnerEvent(){
		
		
	}
	
	public void clear(){
		mCallback = null;
	}
	
	public static void register(Callback cb){
		mCallback = cb;
	}
	
	public void notifyEvent(InnerMessage.Event e){
		InnerMessage msg = new InnerMessage(e);
		//mQueue.addLast(msg);
		if(mCallback != null){
			mCallback.onReceive(msg);
		}
	}
}
