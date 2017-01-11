package com.xqand.savehelper;


import android.app.Activity;
import android.os.Bundle;

public interface ISaveInstanceStateHelper<T> {

	void save(Bundle outState, T clazz);
	void recover(Bundle savedInstanceState, T clazz);
}
