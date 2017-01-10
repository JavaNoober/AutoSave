package com.xiaoqi.autosavesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.processor.NeedSave;
import com.xqand.savehelper.SaveHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
	@NeedSave
	public int a;
	@NeedSave
	public String test;
	@NeedSave
	public boolean b;
	@NeedSave
	public Boolean c;
	@NeedSave
	public ArrayList<String> t;
	@NeedSave
	public Integer i;
	@NeedSave
	public Example example;
	@NeedSave
	public Float f1;
	@NeedSave
	public float f2;
	@NeedSave
	public char achar;
	@NeedSave
	public char achars[];
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
