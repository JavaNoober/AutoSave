package com.noober.savehelper;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SaveHelper {
	private static Map<String,ISaveInstanceStateHelper> helperCache = new HashMap<>();
	private final static String HELPER_END = "_SaveStateHelper";

	public static <T> void bind(T recover, Bundle savedInstanceState){
		if(savedInstanceState != null){
			ISaveInstanceStateHelper<T> saveInstanceStateHelper = findSaveHelper(recover);
			if(saveInstanceStateHelper != null){
				saveInstanceStateHelper.recover(savedInstanceState, recover);
			}
		}
	}

	public static <T> void save(T save, Bundle outState){
		ISaveInstanceStateHelper<T> saveInstanceStateHelper = findSaveHelper(save);
		if(saveInstanceStateHelper != null){
			saveInstanceStateHelper.save(outState, save);
		}
	}


	@Nullable
	private static <T> ISaveInstanceStateHelper<T> findSaveHelper(T cl) {
		String clazz = cl.getClass().getName();
		ISaveInstanceStateHelper<T> saveInstanceStateHelper = helperCache.get(clazz);
		if(saveInstanceStateHelper == null){
			try {
				Class<?> findClass = Class.forName(clazz + HELPER_END);
				saveInstanceStateHelper = (ISaveInstanceStateHelper<T>)findClass.newInstance();
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

}
