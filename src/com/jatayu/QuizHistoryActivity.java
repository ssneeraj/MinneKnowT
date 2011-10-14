package com.jatayu;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

/**
 * This class queries the DataBase and fetches the previous quiz history and
 * display the result.
 * 
 * @author sharman
 * 
 */
public class QuizHistoryActivity extends ListActivity {

	private QuizDBManager		qdbm;
	private ArrayAdapter<String>	array_adapter;
	private String[]		questionHistoryArray;
	private static final String	TAG	= "QuizHistoryActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quiz_history_layout);

		getListView().setCacheColorHint(0);

		qdbm = new QuizDBManager(this);

		Cursor cursor_for_quiztracker_table = qdbm.getQuizHistory();

		if (cursor_for_quiztracker_table.getCount() > 0) {

			Cursor cursor_for_attempts_table = qdbm
					.getQuizAttemptsCursor();
			cursor_for_attempts_table.moveToFirst();
			int total_attempts = cursor_for_attempts_table
					.getInt(1);

			cursor_for_quiztracker_table.moveToFirst();

			if (CommonProps.LOG_ENABLED) {
				Log.d(TAG,
						">>> Number of rows in Cursor that read from quiztracker table : "
								+ cursor_for_quiztracker_table
										.getCount());

				Log.d(TAG,
						">>> Number of Columns in Cursor that read from quiztracker table : "
								+ cursor_for_quiztracker_table
										.getColumnCount());
			}
			// create the question history array according to the
			// number of
			// rows in
			// quiz tracker table
			questionHistoryArray = new String[cursor_for_quiztracker_table
					.getCount()];

			StringBuffer buff = new StringBuffer();
			int index = 0;
			int accuracyPercent = 0;

			cursor_for_quiztracker_table.moveToFirst();

			while (!cursor_for_quiztracker_table.isAfterLast()) {
				buff.append("Question "
						+ cursor_for_quiztracker_table
								.getInt(1));
				buff.append(": ");
				buff.append(QuizQuestionsList.QUESTION_TEXT[index]);

				buff.append("\n");

				// Total attempts
				buff.append("\nTotal attempts: "
						+ total_attempts);
				buff.append("\nTotal Correct: "
						+ cursor_for_quiztracker_table
								.getInt(2));

				// Accuracy
				// check or else it may cause divide by zero
				// error
				if (total_attempts > 0) {
					accuracyPercent = (cursor_for_quiztracker_table
							.getInt(2) * 100)
							/ total_attempts;
				} else {
					accuracyPercent = 0;
				}
				buff.append("\nAccuracy: " + accuracyPercent
						+ " %");

				questionHistoryArray[index] = buff.toString();
				buff.setLength(0);
				accuracyPercent = 0;
				index++;
				cursor_for_quiztracker_table.moveToNext();
			}

			populateQuizHistoryUI();

			cursor_for_quiztracker_table.close();

		}

	}

	private void populateQuizHistoryUI() {

		if (array_adapter == null) {
			array_adapter = new ArrayAdapter<String>(this,
					R.layout.quiz_history_list_item,
					questionHistoryArray);
		}

		// This binds the array adapter to our QuizActvity
		setListAdapter(array_adapter);

		array_adapter.setNotifyOnChange(true);
	}

}
