package com.xqand.savehelper;


import android.app.Activity;
import android.os.Bundle;

public interface ISaveInstanceStateHelper {

	void save(Bundle outState, Activity activity);
	void recover(Bundle savedInstanceState, Activity activity);
}
