package com.jatayu;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class QuizResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		QuizStat stat = QuizStat.getInstance();
		setContentView(R.layout.quiz_result_layout);

		TextView total_questions_value = (TextView) this
				.findViewById(R.id.totalQuestionsValue);
		total_questions_value.setText(Integer
				.toString(CommonProps.TOTAL_QUIZ_QUESTIONS));

		TextView correct_answer_value = (TextView) this
				.findViewById(R.id.correctValue);
		correct_answer_value.setText(Integer.toString(stat
				.getNumberOfCorrectAnswer()));

		TextView percentage_correct_answer_value = (TextView) this
				.findViewById(R.id.percentageValue);

		int answer_percentage_accuracy = stat.getPercentageOfCorrectAnswers();
		percentage_correct_answer_value.setText(Integer
				.toString(answer_percentage_accuracy));

		TextView quiz_outcome = (TextView) this.findViewById(R.id.quizOutcome);
		if (answer_percentage_accuracy > 84)
			quiz_outcome.setText("PASS");
		else
			quiz_outcome.setText("FAIL!");

	}
}
