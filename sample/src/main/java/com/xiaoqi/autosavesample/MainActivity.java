package com.xiaoqi.autosavesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.processor.NeedSave;
import com.xqand.savehelper.SaveHelper;

public class MainActivity extends AppCompatActivity {
	@NeedSave
	public int a;
	@NeedSave
	public String test;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SaveHelper.bind(this,savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		SaveHelper.save(this,outState);
		super.onSaveInstanceState(outState);
	}
}
