package com.jatayu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class QuizDBManager {

	private static final String	TAG	= "QuizDBManager class: ";
	private MyOpenHelper		db_helper;
	private Context			context;

	public QuizDBManager(Context context) {
		this.context = context;
		db_helper = new MyOpenHelper(context);
	}

	/**
	 * ------------- Developer Notes ---------
	 * 
	 * Step 1: Open 'quiztracker' table and read all the rows under the
	 * column 'correctanswertracker'
	 * 
	 * Step 2: Update 'quiztracker table' using the data in
	 * OngoingQuizTracker
	 * 
	 * Step 3: Open 'attempts' table and read the value
	 * 
	 * Step 4: Update attempts table
	 */
	public void saveQuizResultToDB() {

		try {

			if (CommonProps.LOG_ENABLED)
				Log.v(TAG, ">>> In saveQuizResultToDB() method");

			SQLiteDatabase db = db_helper.getWritableDatabase();

			// Step 1: In 'quiztracker' table and read all the rows
			// under the
			// correctanswertracker column
			Cursor c = db.query(
					MyOpenHelper.QUIZ_TRACKER_TABLE_NAME,
					null, null, null, null, null, null);

			if (CommonProps.LOG_ENABLED) {
				Log.d(TAG,
						">>> Column count: "
								+ c.getColumnCount());

				Log.d(TAG,
						">>> Total number of Rows (records) in quiztracker table : "
								+ c.getCount());

			}

			String[] col_names = new String[c.getColumnCount()];
			System.arraycopy(c.getColumnNames(), 0, col_names, 0,
					col_names.length);

			if (CommonProps.LOG_ENABLED) {
				for (int i = 0; i < col_names.length; i++) {
					Log.d(TAG,
							">>> Column names: "
									+ col_names[i]
									+ " and its index is: "
									+ c.getColumnIndex(col_names[i]));

				}
			}

			int sum = 0;

			if (CommonProps.LOG_ENABLED)
				Log.d(TAG, ">>> Row reading done");

			OngoingQuizTracker quiztracker = OngoingQuizTracker
					.getInstance();

			int[] questionnumber_array = new int[CommonProps.TOTAL_QUIZ_QUESTIONS];
			int[] correct_answer_tracker_array = new int[CommonProps.TOTAL_QUIZ_QUESTIONS];

			System.arraycopy(quiztracker.getQuestion_number(), 0,
					questionnumber_array, 0,
					CommonProps.TOTAL_QUIZ_QUESTIONS);

			System.arraycopy(
					quiztracker.getCorrect_answer_tracker(),
					0, correct_answer_tracker_array, 0,
					CommonProps.TOTAL_QUIZ_QUESTIONS);

			if (CommonProps.LOG_ENABLED)
				Log.d(TAG, ">>> About to reading the rows");

			int index = 0;

			ContentValues new_values_for_correct_answer_tracker = new ContentValues();

			while (c.moveToNext()) {

				if (CommonProps.LOG_ENABLED) {
					Log.d(TAG,
							">>> Value at Column 1 (question number): "
									+ c.getInt(1));
					Log.d(TAG,
							">>> Value at Column 2 (correct answer tracker): "
									+ c.getInt(2));

				}

				// compare the 'questionnumber' from the table
				// with 'questionnumber_array'

				if (c.getInt(1) == questionnumber_array[index]) {

					// add the correct answer tracker read
					// from ongoing quiz to that read from
					// the database
					sum = correct_answer_tracker_array[index]
							+ c.getInt(2);

					if (CommonProps.LOG_ENABLED)
						Log.d(TAG, ">>> sum is: " + sum);

					new_values_for_correct_answer_tracker
							.put(MyOpenHelper.CORRECT_ANSWER_TRACKER_COLUMN,
									sum);

					if (CommonProps.LOG_ENABLED)
						Log.v(TAG,
								"from content values: "
										+ new_values_for_correct_answer_tracker
												.getAsString(MyOpenHelper.CORRECT_ANSWER_TRACKER_COLUMN));

					db.update(MyOpenHelper.QUIZ_TRACKER_TABLE_NAME,
							new_values_for_correct_answer_tracker,
							MyOpenHelper.QUESTION_NUMBER_COLUMN
									+ "="
									+ questionnumber_array[index],
							null);

				}

				sum = 0;
				index++;
			}

			// Read the first row from attempts column in attempts
			// table
			Cursor c2 = db.query(MyOpenHelper.ATTEMPTS_TABLE_NAME,
					null, null, null, null, null, null);
			Log.d(TAG, ">>> Attempts table Row reading done");

			Log.d(TAG,
					">>> Total number of Columns in attempts table : "
							+ c2.getColumnCount());

			Log.d(TAG,
					">>> Total number of Rows (records) in attempts table : "
							+ c2.getCount());

			String[] col_names_attemptsTable = new String[c2
					.getColumnCount()];

			// copy column names that were read from the attempts
			// table
			System.arraycopy(c2.getColumnNames(), 0,
					col_names_attemptsTable, 0,
					col_names_attemptsTable.length);

			for (int i = 0; i < col_names_attemptsTable.length; i++) {
				Log.d(TAG, ">>> Column names: "
						+ col_names_attemptsTable[i]);
			}

			c2.moveToFirst();

			// get the value at column 1 which is attempts column in
			// first row
			int attemptsFromDb = c2.getInt(1);

			if (CommonProps.LOG_ENABLED)
				Log.d(TAG,
						">>> Prior to this attempt Quiz has been attempted: "
								+ attemptsFromDb);

			attemptsFromDb = attemptsFromDb + 1;

			ContentValues attempt_content_values = new ContentValues();
			attempt_content_values.put(
					MyOpenHelper.ATTEMPTS_COLUMN,
					attemptsFromDb);

			db.update(MyOpenHelper.ATTEMPTS_TABLE_NAME,
					attempt_content_values, null, null);

			if (CommonProps.LOG_ENABLED)
				Log.d(TAG,
						">>> Update Attempts table successfully");

			if (db.isOpen())
				db.close();

			if (CommonProps.LOG_ENABLED)
				Log.v(TAG, " Database successfully closed");

		} catch (SQLiteException sqlite_error) {
			Log.v(TAG,
					"Error while writting into Quiz Tracker Table: Error message: "
							+ sqlite_error.toString());
		}

	}

	public Cursor getQuizHistory() {

		if (CommonProps.LOG_ENABLED)
			Log.d(TAG, ">>> Reading Quiz Tracker table from DB");

		SQLiteDatabase db = db_helper.getReadableDatabase();

		return db.query(MyOpenHelper.QUIZ_TRACKER_TABLE_NAME, null,
				null, null, null, null, null);

	}

	public Cursor getQuizAttemptsCursor() {

		if (CommonProps.LOG_ENABLED)
			Log.d(TAG, ">>> Reading Attempts table from DB");

		SQLiteDatabase db = db_helper.getReadableDatabase();

		return db.query(MyOpenHelper.ATTEMPTS_TABLE_NAME, null, null,
				null, null, null, null);
	}

	public void resetQuizDatabase() {
		new Thread(new Runnable() {

			public void run() {

				if (CommonProps.LOG_ENABLED)
					Log.d(TAG,
							">>> About to reset rows in DB");

				SQLiteDatabase db = db_helper
						.getWritableDatabase();

				db_helper.resetToDefaultValues(db);

				if (CommonProps.LOG_ENABLED)
					Log.d(TAG,
							">>> reset to default values done!");

			}
		}).start();

	}

}
