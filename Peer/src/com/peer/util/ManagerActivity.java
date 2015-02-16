package com.peer.util;

import java.util.Stack;

import com.peer.activity.LoginActivity;
import com.peer.constant.Constant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * application Activity managet :be used for manage Activity and exit application
 * 
 * @author Concoon-break
 */
public class ManagerActivity {

	private static Stack<Activity> activityStack;

	private ManagerActivity() {
		
	}

	/**
	 * single instance
	 */
	public static ManagerActivity getAppManager() {

		return SingletonCreator.instance;
	}

	private static class SingletonCreator {
		private final static ManagerActivity instance = new ManagerActivity();
	}

	/**
	 * add activity to stack
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	public void removeActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);

			activity = null;
		}
	}

	/**
	 * get current activity (in the top of stack)
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * finish current Activity
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * The end of the specified Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * The end of the specified class name
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}
	/**
	 * finsh All Top Activities 
	 */
	public void finishAllTopActivities(Activity activity) {
		if (activity != null) {
			boolean flag = false;

			for (Activity a : activityStack) {
				if (flag) {
					a.finish();
					activityStack.remove(a);
				} else {
					if (a == activity) {
						flag = true;
					}
				}
			}
		}
	}

	/**
	 * finsh all Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * exit application
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			// ActivityManager activityMgr = (ActivityManager) context
			// .getSystemService(Context.ACTIVITY_SERVICE);
			// activityMgr.restartPackage(context.getPackageName());
			// System.exit(0);
		} catch (Exception e) {
		}
	}
	/**
	 * restart entrance activity
	 * @param context
	 */
	public void restart(Context context) {

		finishAllActivity();

		// Intent i = context.getPackageManager().getLaunchIntentForPackage(
		// context.getPackageName());
		Intent i = new Intent(context, LoginActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra(Constant.RELOGIN, Constant.RELOGIN);
		context.startActivity(i);

	}
}
