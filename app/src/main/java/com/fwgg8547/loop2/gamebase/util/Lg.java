package com.fwgg8547.loop2.gamebase.util;

import android.content.Context;
import android.util.Log;
import android.graphics.*;

public class Lg
{
		final private static boolean isOutput = true;
		final private static int level = 3;

		public static void a(String tag, String s){
			if (isOutput && level <= 1) Log.d(tag, s);   
		}
	
		public static void d(String tag, String s){
			if (isOutput && level <= 2) Log.d(tag, s);   
		}
	
		public static void i(String tag, String s){
			if (isOutput && level <= 3) Log.i(tag, s);   
		}

		public static void w(String tag, String s){
			if (isOutput && level <= 4) Log.w(tag, s);   
		}

		public static void e(String tag, String s){
			if (isOutput && level <= 5) Log.e(tag, s);   
		}
		
		public static void stack(String tag, StackTraceElement e[]){
			Log.e(tag, e[0].getClassName() +"|" + e[0].getMethodName() +"|"+e[0].getLineNumber());
			Log.e(tag, e[1].getClassName() +"|" + e[1].getMethodName() +"|"+e[1].getLineNumber());
		}
		
		public static void dump(String tag, String s, RectF rect){
			if (isOutput && level <= 3) {
				Log.i(tag, s+
				" top= " + rect.top +
				" lef= " + rect.left +
				" btm= " + rect.bottom +
				" rgt= " + rect.right);
			} 
		}
  
}
