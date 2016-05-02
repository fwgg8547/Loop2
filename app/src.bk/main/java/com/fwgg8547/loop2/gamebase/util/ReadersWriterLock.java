package com.fwgg8547.loop2.gamebase.util;

public final class ReadersWriterLock
{
	private final static String TAG ="LOCK";
	private static int mReadingReaders = 0;
	private static int mWaitingWriters = 0;
	private static int mWritingWriter = 0;
	private static boolean mPreferWriter = true;
	
	public synchronized void readLock() throws InterruptedException
	{
		Lg.a(TAG, "read lock");
		if(mWritingWriter > 0 || (mPreferWriter && mWaitingWriters > 0)){
			Lg.a(TAG, "wait read lock");
			wait();
			Lg.a(TAG, "after wait read lock");
		}
		mReadingReaders++;
	}
	
	public synchronized void readUnlock(){
		mReadingReaders--;
		mPreferWriter = true;
		Lg.a(TAG, "read unlock");
		notifyAll();
	}
	
	public synchronized void writeLock() throws InterruptedException
	{
		Lg.a(TAG, "writw lock");
		mWaitingWriters++;
		try {
			if(mReadingReaders > 0 || mWritingWriter > 0){
				Lg.a(TAG, "wait write lock");
				wait();
				Lg.a(TAG, "after wait write lock");
			}
		} finally {
			mWaitingWriters--;
		}
		mWritingWriter++;
	}
	
	public synchronized void writeUnlock()
	{
		mWritingWriter--;
		mPreferWriter = false;
		Lg.a(TAG, "write unlock");
		notifyAll();
	}
}
