package com.xqand.autosave;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.xqand.savehelper.ISaveInstanceStateHelper;


public class MainActivity_SaveStateHelper implements ISaveInstanceStateHelper {

	@Override
	public void save(Bundle outState, Object clazz) {
		MainActivity_Cache cache = (MainActivity_Cache)clazz;
		outState.putInt("I",cache.i);
	}

	@Override
	public void recover(Bundle savedInstanceState , Activity activity) {
		if(savedInstanceState != null){
			MainActivity recoverActivity = ((MainActivity)activity);
			recoverActivity.i = savedInstanceState.getInt("I");
		}

	}
}
