package com.xqand.autosave;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.processor.NeedSave;
import com.xqand.savehelper.SaveHelper;

public class MainActivity extends AppCompatActivity {
	@NeedSave
	int i = 0;

	@NeedSave
	String text = "2222";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		setChildActivity(this);
		super.onCreate(savedInstanceState);
		SaveHelper.bind(this,savedInstanceState);
		if(savedInstanceState != null){
			Log.i("SaveHelper,","MainActivity not null");
		}
		setContentView(R.layout.activity_main);
		Log.i("SaveHelper",i+"");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i("SaveHelper","onPause");
		i = 2;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.i("SaveHelper","onSaveInstanceState");
		SaveHelper.save(this,outState);
		super.onSaveInstanceState(outState);
	}
}
