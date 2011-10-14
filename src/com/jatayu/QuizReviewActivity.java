package com.jatayu;

import android.app.ListActivity;
import android.os.Bundle;

/**
 * Displays Quiz Review ONLY after the user has answered all the questions. This
 * makes uses of a custom adapter
 * 
 * @author sharman
 * 
 */
public class QuizReviewActivity extends ListActivity {

	private QuizReviewCustomAdapter	custom_adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quiz_review_layout);

		getListView().setCacheColorHint(0);

		populateQuizHistoryUI();
	}

	private void populateQuizHistoryUI() {
		custom_adapter = new QuizReviewCustomAdapter(this);

		// custom adapter binds the underlying data and sets it for the
		// List View
		setListAdapter(custom_adapter);
	}
}
