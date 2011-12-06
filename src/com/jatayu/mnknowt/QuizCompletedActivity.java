package com.jatayu.mnknowt;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class QuizCompletedActivity extends Activity {

	private static final String	TAG			= "QuizCompletedActivity";
	private OngoingQuizTracker	ongoing_quiz_tracker	= OngoingQuizTracker
										.getInstance();

	private StringBuffer		buffer;
	private QuizDBManager		qdbm;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.quiz_completed_layout_v2);

		buffer = new StringBuffer();

		TextView correct_ans_TV = (TextView) this
				.findViewById(R.id.quizCompleted_correct_answers_count);
		buffer.append(ongoing_quiz_tracker.getTotal_correct_answers());
		correct_ans_TV.setText(buffer.toString());

		buffer.setLength(0);

		TextView incorrect_ans_TV = (TextView) this
				.findViewById(R.id.quizCompleted_correct_answers_count);
		buffer.append(ongoing_quiz_tracker.getTotal_incorrect_answers());
		incorrect_ans_TV.setText(buffer.toString());

		new Thread(new Runnable() {
			public void run() {
				saveToDataBase();
			}
		}).start();

	}

	private void saveToDataBase() {
		// if (CommonProps.LOG_ENABLED)
		Log.d(TAG, " >>> Saving current Quiz information to database");

		qdbm = new QuizDBManager(QuizCompletedActivity.this);
		qdbm.saveQuizInformationToDB();

		OngoingQuizTracker.getInstance().resetValues();

		qdbm.closeDB();
	}

	public void showAppHomePage(View view) {
		finish(); // invoke finish() will close this activity
	}

	protected void onDestroy() {
		super.onDestroy();
	}
}
