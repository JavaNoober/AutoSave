package com.noober.savehelper;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

public class SaveHelper {
	private static Map<String,ISaveInstanceStateHelper> helperCache = new HashMap<>();

	private final static String HELPER_END = "_SaveStateHelper";

	/**
	 * Equate to{@link SaveHelper#recover}, it's just renamed to make it easier to understand
	 */
	@Deprecated
	public static <T> void bind(T recover, Bundle savedInstanceState){
		recover(recover, savedInstanceState);
	}

	/**
	 * added where need to recover data
	 *
	 * @param recover current Activity or Fragment
	 * @param savedInstanceState Bundle
	 */
	public static <T> void recover(T recover, Bundle savedInstanceState){
		if(savedInstanceState != null){
			ISaveInstanceStateHelper<T> saveInstanceStateHelper = findSaveHelper(recover);
			if(saveInstanceStateHelper != null){
				saveInstanceStateHelper.recover(savedInstanceState, recover);
			}
		}
	}

	public static <T> void save(T save, Bundle outState){
		if(outState != null){
			ISaveInstanceStateHelper<T> saveInstanceStateHelper = findSaveHelper(save);
			if(saveInstanceStateHelper != null){
				saveInstanceStateHelper.save(outState, save);
			}
		}
	}


	private static <T> ISaveInstanceStateHelper<T> findSaveHelper(T cl) {
		String clazz = cl.getClass().getName();
		ISaveInstanceStateHelper<T> saveInstanceStateHelper = helperCache.get(clazz);
		if(saveInstanceStateHelper == null){
			try {
				Class<?> findClass = Class.forName(clazz + HELPER_END);
				saveInstanceStateHelper = (ISaveInstanceStateHelper<T>)findClass.newInstance();
				helperCache.put(clazz,saveInstanceStateHelper);
			} catch (ClassNotFoundException e) {
				// ignore
				//e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return saveInstanceStateHelper;
	}

}
