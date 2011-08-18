package com.jatayu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MinneknowTActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.apphomelayout);
	}

	/*
	 * this method is invoked when 'Take Quiz' button is selected
	 */
	public void showQuizHomeActivity(View view) {

		Intent intent = new Intent(MinneknowTActivity.this,
				QuizHomeActivity.class);

		startActivity(intent);

	}
}