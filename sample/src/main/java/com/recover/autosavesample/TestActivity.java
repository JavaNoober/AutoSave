package com.recover.autosavesample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.noober.api.NeedSave;

public class TestActivity extends BaseActivity {

	private TextView textView;
	private Button button;
	private final static String testContent = "This is a test code:";

	@NeedSave
	String testString = "11111111";
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
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		textView.setText(testContent + testString);
	}
}
