package com.recover.autosavesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;


import com.noober.api.NeedSave;
import com.noober.savehelper.SaveHelper;

import java.util.ArrayList;

public class SampleActivity extends AppCompatActivity {

	@NeedSave
	String test;
	@NeedSave
	public boolean b;
	@NeedSave
	protected Boolean c;
	@NeedSave
	ArrayList<String> t;
	@NeedSave
	Integer i;
	@NeedSave
	ParcelableExample parcelableExample;
	@NeedSave
	Float f1;
	@NeedSave
	float f2;
	@NeedSave
	char achar;
	@NeedSave
	char achars[];
	@NeedSave
	int sssss[];
	@NeedSave
	int[] sasa;
	@NeedSave
	Bundle bundle;
	@NeedSave
	int a;
	@NeedSave
	SerializableExample serializableExample;

	@NeedSave
	ArrayList<SerializableExample> serializableExamples;
	@NeedSave
	ArrayList<ParcelableExample> parcelableExamples;
	@NeedSave
	ParcelableExample[] parcelableArray;
	@NeedSave
	SparseArray<ParcelableExample> sparseArray;
	@NeedSave
	byte[] bytes;
	@NeedSave
	String[] stringArray;
	@NeedSave
	long[] longArray;
	@NeedSave
	Size size;
	@NeedSave
	SizeF sizeFS;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData();
		SaveHelper.recover(this,savedInstanceState);
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
