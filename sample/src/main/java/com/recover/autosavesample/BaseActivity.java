package com.recover.autosavesample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.noober.savehelper.SaveHelper;

/**
 * Created by Administrator on 2017/12/26.
 */

public class BaseActivity extends AppCompatActivity {


	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		SaveHelper.recover(this, savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		SaveHelper.save(this, outState);
		super.onSaveInstanceState(outState);
	}
}
