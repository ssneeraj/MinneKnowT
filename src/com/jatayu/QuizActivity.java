package com.jatayu;

import java.util.ArrayList;

import android.app.ListActivity;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quizlayout);

		showSingleQandA();
	}

	/*
	 * This method populates the Quiz UI (that is displays a single question and
	 * (4 answer choices) with Quiz data which is stored in a
	 */
	private void showSingleQandA() {

		QandA singleQandA = quizArrayList.get(currentQuestionIndex);

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

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		String toastText = null;

		Integer selectedListItemPosition = position;

		// compare if the user select answer choice matches the actual correct
		// answer
		if (correct_answer
				.equalsIgnoreCase(selectedListItemPosition.toString())) {
			toastText = "correct answer";
		} else {
			toastText = "wrong answer";
			v.setBackgroundColor(Color.RED);
		}

		Toast toast = Toast.makeText(QuizActivity.this, toastText,
				Toast.LENGTH_SHORT);
		toast.show();

	}

}
