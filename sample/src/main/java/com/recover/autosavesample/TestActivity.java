package com.recover.autosavesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.noober.api.NeedSave;
import com.noober.savehelper.SaveHelper;

public class TestActivity extends AppCompatActivity {

	private TextView textView;
	private Button button;
	private final static String testContent = "This is a test code:";

	@NeedSave
	public String testString = "11111111";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		SaveHelper.bind(this, savedInstanceState);
		textView = findViewById(R.id.tv_content);
		button = findViewById(R.id.button);
		textView.setText(testContent + testString);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				testString = "222222222";
				textView.setText(testContent + testString);
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		SaveHelper.save(this, outState);
		super.onSaveInstanceState(outState);
	}
}
