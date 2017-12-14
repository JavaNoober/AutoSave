package com.recover.autosavesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.noober.api.NeedSave;
import com.noober.savehelper.SaveHelper;

import java.util.ArrayList;

public class SampleActivity extends AppCompatActivity {

	@NeedSave
	public String test;
	@NeedSave
	private boolean b;
	@NeedSave
	public Boolean c;
	@NeedSave
	public ArrayList<String> t;
	@NeedSave
	public Integer i;
	@NeedSave(isParcelable = true)
	public Example example;
	@NeedSave
	public Float f1;
	@NeedSave
	public float f2;
	@NeedSave
	public char achar;
	@NeedSave
	public char achars[];
	@NeedSave
	public int sssss[];
	@NeedSave
	public int[] sasa;
	@NeedSave
	public Bundle bundle;
	@NeedSave
	public int a;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData();
		SaveHelper.bind(this,savedInstanceState);
	}

	private void initData() {
		//TODO
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		SaveHelper.save(this,outState);
		super.onSaveInstanceState(outState);
	}
}
