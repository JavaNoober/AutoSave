package com.noober.savehelper;


import android.os.Bundle;
import android.os.PersistableBundle;

public interface ISaveInstanceStateHelper<T> {

	void save(Bundle outState, PersistableBundle outPersistentState, T clazz);
	void recover(Bundle savedInstanceState, PersistableBundle persistentState, T clazz);
}
