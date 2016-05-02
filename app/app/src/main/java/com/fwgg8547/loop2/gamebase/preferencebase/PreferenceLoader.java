package com.fwgg8547.loop2.gamebase.preferencebase;

import android.content.SharedPreferences;
import android.content.Context;

public class PreferenceLoader
{
	private final static String PREFNAME = "gameprefdata";
	private final static String HISCOREKEY = "hiscore";
	private final static String CURRENTKEY = "score";
	private static PreferenceLoader instance;
	private static Context mContext;
	
	public static PreferenceLoader getInstance(Context ctx){
		if(instance == null){
			instance = new PreferenceLoader(ctx);
		}
		
		return instance;
	}
	
	private PreferenceLoader(Context ctx){
		mContext = ctx;
	}
	
	public static int getHiscore(){
		SharedPreferences sp = mContext.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
		return sp.getInt(HISCOREKEY, 0);
	}
	
	public static void setHiscore(int s){
		SharedPreferences sp = mContext.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor ed = sp.edit();
		ed.putInt(HISCOREKEY, s);
		ed.apply();
	}
	
	public static int getCurrentScore(){
		SharedPreferences sp = mContext.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
		return sp.getInt(CURRENTKEY, 0);
	}
	
	public static void setScore(int s){
		SharedPreferences sp = mContext.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor ed = sp.edit();
		ed.putInt(CURRENTKEY, s);
		ed.apply();
	}
}
