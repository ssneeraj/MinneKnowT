package com.jatayu.mnknowt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class QuizDBManager {

	private static final String	TAG	= "QuizDBManager class: ";
	private MyOpenHelper		db_helper;
	private SQLiteDatabase		db;

	public QuizDBManager(Context context) {
		db_helper = new MyOpenHelper(context);
	}

	public void closeDB() {
		if (db.isOpen()) {
			db.close();
		}
	}

	public void saveQuizInformationToDB() {
		updateScoreTable();
		updateCorrectIncorrectTable();
		updateTotalQuestionsAnsweredTable();

		closeDB();
	}

	public void resetQuizDatabase() {
		// new Thread(new Runnable() {
		//
		// public void run() {
		//
		// if (CommonProps.LOG_ENABLED)
		// Log.d(TAG,
		// ">>> About to reset rows in DB");
		//
		// SQLiteDatabase db = db_helper
		// .getWritableDatabase();
		//
		// db_helper.resetToDefaultValues(db);
		//
		// if (CommonProps.LOG_ENABLED)
		// Log.d(TAG,
		// ">>> reset to default values done!");
		//
		// }
		// }).start();

	}

	private void updateTotalQuestionsAnsweredTable() {
		db = db_helper.getWritableDatabase();
		ContentValues cv = new ContentValues();

		// read all the rows from 'totalquestionsanswered' TABLE into
		// the Cursor
		Cursor cursor = getTotalQuestionsAnswered();

		// move the cursor to the first row
		cursor.moveToFirst();

		int total_questions_answered = cursor.getInt(1);

		cv.put(MyOpenHelper.TOTAL_QUESTIONS_ANSWERED_COLUMN,
				total_questions_answered + 10);

		// update the DB with ContentValues
		db.update(MyOpenHelper.TOTAL_QUESTIONS_ANSWERED_TABLE_NAME, cv,
				null, null);

		cursor.close();

		if (CommonProps.LOG_ENABLED)
			Log.d(TAG,
					">>> Updated 'totalquestionsanswered' TABLE successfully");

	}

	private void updateCorrectIncorrectTable() {
		db = db_helper.getWritableDatabase();
		OngoingQuizTracker quiz_tracker = OngoingQuizTracker
				.getInstance();

		// ContentValues that will be written into 'correctincorrect'
		// TABLE
		ContentValues cv = new ContentValues();

		// read all the rows from 'correctincorrect TABLE into the
		// Cursor
		Cursor correct_incorrect_cursor = getTotalCorrectIncorrectStats();

		// move the cursor to the first row
		correct_incorrect_cursor.moveToFirst();

		// read value from 'correct' column at column index 1
		int total_correct = correct_incorrect_cursor.getInt(1);

		// read value from 'incorrect' column at column index 2
		int total_incorrect = correct_incorrect_cursor.getInt(2);

		// Compare the total correct and incorrect answers from recently
		// completed quiz and those from the database

		cv.put(MyOpenHelper.CORRECT_COLUMN,
				quiz_tracker.getTotal_correct_answers()
						+ total_correct);
		cv.put(MyOpenHelper.INCORRECT_COLUMN,
				quiz_tracker.getTotal_incorrect_answers()
						+ total_incorrect);

		// update the DB with ContentValues
		db.update(MyOpenHelper.CORRECT_INCORRECT_TABLE_NAME, cv, null,
				null);

		correct_incorrect_cursor.close();

		if (CommonProps.LOG_ENABLED)
			Log.d(TAG,
					">>> Updated 'correctincorrect TABLE successfully");

	}

	private void updateScoreTable() {
		db = db_helper.getWritableDatabase();
		OngoingQuizTracker quiz_tracker = OngoingQuizTracker
				.getInstance();

		// ContentValues that will be written into score table
		ContentValues content_values_score_table = new ContentValues();

		// read all the rows from 'score' table into the Cursor
		Cursor score_cursor = getQuizScore();

		// move the cursor to the first row
		score_cursor.moveToFirst();

		// read value from 'best' score column at column index 1
		int best_score = score_cursor.getInt(1);

		// read value from 'worst' score column at column index 2
		int worst_score = score_cursor.getInt(2);

		// Compare the scores from recently completed quiz and those
		// from the database

		// first check if the best_score = 11 and worst_score = 11
		// this means the score table has default values and is being
		// read for the first time
		// we simple write the ongoing quiz tracker values
		if (best_score == 11 && worst_score == 11) {
			content_values_score_table
					.put(MyOpenHelper.BEST_SCORE_COLUMN,
							quiz_tracker.getTotal_correct_answers());

			content_values_score_table
					.put(MyOpenHelper.WORST_SCORE_COLUMN,
							quiz_tracker.getTotal_incorrect_answers());
		} else {

			// if the correct answers in recently completed quiz is
			// greated
			// than the best_score in the database then replace the
			// best
			// score in the DB with the current total correct answer
			// value
			// that is in the OngoingQuizTracker
			if (quiz_tracker.getTotal_correct_answers() > best_score) {
				content_values_score_table
						.put(MyOpenHelper.BEST_SCORE_COLUMN,
								quiz_tracker.getTotal_correct_answers());
			} else {
				content_values_score_table.put(
						MyOpenHelper.BEST_SCORE_COLUMN,
						best_score);
			}

			if (quiz_tracker.getTotal_incorrect_answers() > worst_score) {
				content_values_score_table
						.put(MyOpenHelper.WORST_SCORE_COLUMN,
								quiz_tracker.getTotal_incorrect_answers());
			} else {
				content_values_score_table
						.put(MyOpenHelper.WORST_SCORE_COLUMN,
								worst_score);
			}
		}
		// update the DB with ContentValues
		db.update(MyOpenHelper.SCORE_TABLE_NAME,
				content_values_score_table, null, null);

		score_cursor.close();

		if (CommonProps.LOG_ENABLED)
			Log.d(TAG, ">>> Updated score table successfully");

	}

	public Cursor getQuizScore() {
		// if (CommonProps.LOG_ENABLED)
		Log.d(TAG, ">>> Reading 'score' table from DB");

		// SQLiteDatabase db = db_helper.getReadableDatabase();

		return db.query(MyOpenHelper.SCORE_TABLE_NAME, null, null,
				null, null, null, null);
	}

	public Cursor getTotalQuestionsAnswered() {
		// if (CommonProps.LOG_ENABLED)

		Log.d(TAG, ">>> Reading 'totalquestionsanswered' table from DB");

		db = db_helper.getReadableDatabase();

		return db.query(MyOpenHelper.TOTAL_QUESTIONS_ANSWERED_TABLE_NAME,
				null, null, null, null, null, null);
	}

	public Cursor getTotalCorrectIncorrectStats() {

		// if (CommonProps.LOG_ENABLED)
		Log.d(TAG, ">>> Reading 'correctincorrect' table from DB");

		db = db_helper.getReadableDatabase();

		return db.query(MyOpenHelper.CORRECT_INCORRECT_TABLE_NAME,
				null, null, null, null, null, null);
	}

}
