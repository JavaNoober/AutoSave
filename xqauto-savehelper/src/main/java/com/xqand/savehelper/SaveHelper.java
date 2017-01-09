package com.xqand.savehelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SaveHelper {
	private static Map<String,ISaveInstanceStateHelper> helperCache = new HashMap<>();
	private static Map<String,Object> objectCache = new HashMap<>();
	private final static String HELPER_END = "_SaveStateHelper";
	private final static String CACHE_END = "_Cache";

	public static void bind(Activity activity, Bundle savedInstanceState){
		if(savedInstanceState != null){
			ISaveInstanceStateHelper saveInstanceStateHelper = findSaveHelper(activity);
			saveInstanceStateHelper.recover(savedInstanceState, activity);
		}
	}

	public static void save(Activity activity, Bundle outState){
		ISaveInstanceStateHelper saveInstanceStateHelper = findSaveHelper(activity);
		saveInstanceStateHelper.save(outState,findSaveCache(activity));
	}

	@Nullable
	private static ISaveInstanceStateHelper findSaveHelper(Activity activity) {
		String clazz = activity.getClass().getName();
		ISaveInstanceStateHelper saveInstanceStateHelper = helperCache.get(clazz);
		if(saveInstanceStateHelper == null){
			try {
				Class<?> findClass = Class.forName(clazz + HELPER_END);
				saveInstanceStateHelper = (ISaveInstanceStateHelper)findClass.newInstance();
				helperCache.put(clazz,saveInstanceStateHelper);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException(String.format(" not find %s.class",clazz + HELPER_END));
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return saveInstanceStateHelper;
	}

	@Nullable
	private static Object findSaveCache(Activity activity) {
		String clazz = activity.getClass().getName();
		Object cacheObject = objectCache.get(clazz);
		if(cacheObject == null){
			try {
				Class<?> findClass = Class.forName(clazz + CACHE_END);
				cacheObject = findClass.newInstance();
				objectCache.put(clazz,cacheObject);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException(String.format(" not find %s.class",clazz + CACHE_END));
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return cacheObject;
	}
}
