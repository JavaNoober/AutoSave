package com.xqand.autosave;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.xqand.savehelper.SaveHelper;


public class BaseActivity extends AppCompatActivity {

	Activity childActivity;

	@Override
	public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
		super.onCreate(savedInstanceState, persistentState);
		if(savedInstanceState != null){
			Log.i("SaveHelper,","BaseActivity not null");
		}
		SaveHelper.bind(childActivity,savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		SaveHelper.save(childActivity,outState);
		super.onSaveInstanceState(outState);
	}


	public void setChildActivity(Activity childActivity){
		this.childActivity = childActivity;
	}
}
