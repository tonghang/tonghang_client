package com.peer.localDB;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {
	private static SharedPreferences getSharedPreferences(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"userInfo", Context.MODE_WORLD_READABLE);
		return sharedPreferences;
	}
	public static void saveString(Context context, String key, String value) {
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		sharedPreferences.edit().putString(key, value).commit();
	}

	public static String getString(Context context, String key) {
		return getSharedPreferences(context).getString(key, "");
	}
	
	public static void saveBoolean(Context context, String key, Boolean value){
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		sharedPreferences.edit().putBoolean(key, value).commit();
	}
	public static boolean getBoolean(Context context, String key){		
		return getSharedPreferences(context).getBoolean(key, true);
	}
	public static void saveInt(Context context, String key, int value){
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		sharedPreferences.edit().putInt(key, value).commit();
	}
	public static int getInt(Context context, String key,int defaultvalue){
		return getSharedPreferences(context).getInt(key, defaultvalue);
	}
}
