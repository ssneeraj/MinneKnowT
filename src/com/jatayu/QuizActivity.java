package com.jatayu;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * This class displays a question and 4 answer choices in ListView.
 * It gets an individual question and answer set at a time and displays it.
 * 
 */
public class QuizActivity extends ListActivity {

	// this points to the question number that the user is currently answering
	private int currentQuestionIndex = 0;

	// adapter to manage the list view (that displays our answers
	private ArrayAdapter<String> array_adapter;
	private String[] answerData = new String[4];
	private ArrayList<QandA> quizArrayList = QuizCache.getInstance().getQuiz();

	private String correct_answer;

	private QuizStat quiz_stat = QuizStat.getInstance();

	// If the user selects an incorrect answer or does not attempt to answer,
	// store a '0' in integer array 'answer_selected_stat' and store a '1' for
	// correct answer choice
	private int[] answer_selected_stat = new int[19];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quizlayout);

		getListView().setCacheColorHint(0);

		showSingleQandA();

		quiz_stat.clearQuizStat();
	}

	/*
	 * This method populates the Quiz UI (that is displays a single question and
	 * (4 answer choices) with Quiz data which is stored in a
	 */
	private void showSingleQandA() {

		QandA singleQandA = quizArrayList.get(currentQuestionIndex);

		TextView questionNumber = (TextView) this
				.findViewById(R.id.questionNumber);
		questionNumber.setText("Question: " + singleQandA.getQuestionNumber()
				+ " of " + QuizCache.getQuizTotalNumberofQuestion());

		TextView question = (TextView) this.findViewById(R.id.questionText);
		question.setText(singleQandA.getQuestionText());

		correct_answer = singleQandA.getCorrectAnswer();

		System.arraycopy(singleQandA.getAnswers(), 0, answerData, 0, 4);

		// We are using ArrayAdapter to bind underlying date (an array of
		// strings) to ListView in quizlayout
		if (array_adapter == null) {
			array_adapter = new ArrayAdapter<String>(this, R.layout.list_item,
					answerData);
		}

		// This binds the array adapter to our QuizActvity
		setListAdapter(array_adapter);

		array_adapter.setNotifyOnChange(true);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		String toastText = null;

		Integer selectedListItemPosition = position;

		// compare if the user select answer choice matches the actual correct
		// answer
		if (correct_answer
				.equalsIgnoreCase(selectedListItemPosition.toString())) {
			toastText = "correct answer";
			v.setBackgroundColor(Color.GREEN);
			answer_selected_stat[currentQuestionIndex] = CommonProps.ANSWERED_CORRECT;
		} else {
			toastText = "wrong answer";
			v.setBackgroundColor(Color.RED);
			answer_selected_stat[currentQuestionIndex] = CommonProps.ANSWERED_INCORRECT;
		}

		v.setFocusable(false);
		v.setClickable(false);

		Toast toast = Toast.makeText(QuizActivity.this, toastText,
				Toast.LENGTH_SHORT);
		toast.show();

		l.setFocusable(false);

	}

	public void showNextQuestion(View view) {

		// first increment the currentQuestionIndex so that we advance to the
		// next question
		currentQuestionIndex++;

		// If Quiz is over and 'Next' is pressed, then show a Toast
		if (currentQuestionIndex > QuizCache.getQuizTotalNumberofQuestion() - 1) {
			Toast toast = Toast.makeText(QuizActivity.this, "Quiz Over!",
					Toast.LENGTH_LONG);
			toast.show();

			// save the answer selected stat into quiz stat
			quiz_stat.saveQandAInfo(answer_selected_stat);

			// invoke QuizResultActivity
			Intent intent = new Intent(this, QuizResultActivity.class);
			startActivity(intent);

			return;
		}

		QandA singleQandA = quizArrayList.get(currentQuestionIndex);

		TextView questionNumber = (TextView) this
				.findViewById(R.id.questionNumber);
		questionNumber.setText("Question: " + singleQandA.getQuestionNumber()
				+ " of " + QuizCache.getQuizTotalNumberofQuestion());

		TextView question = (TextView) this.findViewById(R.id.questionText);
		question.setText(singleQandA.getQuestionText());

		correct_answer = singleQandA.getCorrectAnswer();

		System.arraycopy(singleQandA.getAnswers(), 0, answerData, 0, 4);

		// We are using ArrayAdapter to bind underlying date (an array of
		// strings) to ListView in quizlayout
		if (array_adapter == null) {
			array_adapter = new ArrayAdapter<String>(this, R.layout.list_item,
					answerData);
		}

		// This binds the array adapter to our QuizActvity
		setListAdapter(array_adapter);
	}
}
