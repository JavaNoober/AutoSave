package com.xiaoqi.autosavesample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xqand.api.NeedSave;
import com.xqand.savehelper.SaveHelper;

import java.util.ArrayList;

public class MainFragment extends BaseFragment {
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
	@NeedSave(isParcelable = true)
	public Example example;
	@NeedSave
	public Float f1;
	@NeedSave
	public float f2;
	@NeedSave
	public char achar;
	@NeedSave
	private char achars[];


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
			savedInstanceState) {
		SaveHelper.bind(this,savedInstanceState);
		return super.onCreateView(inflater, container, savedInstanceState);
	}


	@Override
	public void onSaveInstanceState(Bundle outState) {
		SaveHelper.save(this,outState);
		super.onSaveInstanceState(outState);
	}
}
