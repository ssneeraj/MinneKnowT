package com.jatayu.mnknowt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class QuizCompletedActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quiz_completed_layout_v2);

		StringBuffer buffer = new StringBuffer();

		buffer.append(OngoingQuizTracker.getInstance()
				.getTotal_correct_answers());

		TextView correct_ans_TV = (TextView) this
				.findViewById(R.id.quizCompleted_correct_answers_count);
		correct_ans_TV.setText(buffer);

		buffer.setLength(0);
		buffer.append(OngoingQuizTracker.getInstance()
				.getTotal_incorrect_answers());

		TextView incorrect_ans_TV = (TextView) this
				.findViewById(R.id.quizCompleted_incorrect_answers_count);
		incorrect_ans_TV.setText(buffer.toString());

	}

	public void showAppHomePage(View view) {
		finish();
	}

	public void takeQuizAgain(View view) {
		finish();

		Intent intent = new Intent(this, QuizActivity.class);
		startActivity(intent);
	}

}
