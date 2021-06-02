package com.recover.autosavesample;

import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
	protected Boolean c = false;
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

    @NeedSave
    Bundle bundle;


//	@NeedSave
//	private SecondSExample secondSExample;

//	@NeedSave
//	SecondSExample2 secondSExample2;


//    CustomView customView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//        customView = findViewById(R.id.view);
		initData();
        Log.e("Sample", i + "");
        if(savedInstanceState != null){
            i = savedInstanceState.getInt("ttt");
            Log.e("Sample", "onCreate1:" + i);
        }
		SaveHelper.recover(this,savedInstanceState);
	}

	private void initData() {
		//TODO
	}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.e("Sample", "onSaveInstanceState");
        SaveHelper.save(this,outState);
        super.onSaveInstanceState(outState);
    }

    @NeedSave(isPersistable = true)
    PersistableBundle persistableBundle;

//    @NeedSave(isPersistable = true)
//    int i;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        SaveHelper.recover(this, savedInstanceState, persistentState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        SaveHelper.save(this, outState, outPersistentState);
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
