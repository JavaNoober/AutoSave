package com.recover.autosavesample;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.noober.api.NeedSave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class TestActivity extends BaseActivity {

	private TextView textView;
	private Button button;
	private final static String testContent = "This is a test code:";

//	@NeedSave
	String testString = "11111111";

	@NeedSave
	String[] stringArray;

	@NeedSave
	EnumTest enumTest;

	SerializableExample[] ssss;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		textView = findViewById(R.id.tv_content);
		button = findViewById(R.id.button);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				testString = "222222222";
				textView.setText(testContent + testString);
                Intent intent = new Intent(TestActivity.this, SampleActivity.class);
                intent.putExtra("test", 1);
                startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		textView.setText(testContent + testString);
	}
}
