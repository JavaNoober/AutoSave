package com.xiaoqi.autosavesample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.processor.NeedSave;
import com.xqand.savehelper.SaveHelper;

import java.util.ArrayList;

public class BaseFragment extends Fragment {


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
			savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}


	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
}
