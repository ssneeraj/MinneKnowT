package com.jatayu;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyOpenHelper extends SQLiteOpenHelper {

	// Database information
	private static final String	DATABASE_NAME			= "mnknowt.db";
	private static final int	DATABASE_VERSION		= 1;

	// Table information
	public static final String	QUIZ_TRACKER_TABLE_NAME		= "quiztracker";
	public static final String	ATTEMPTS_TABLE_NAME		= "attempts";

	// Column information
	public static final String	QUESTION_NUMBER_COLUMN		= "questionnumber";
	public static final String	CORRECT_ANSWER_TRACKER_COLUMN	= "correctanswertracker";
	public static final String	ATTEMPTS_COLUMN			= "attempts";

	// CREATE TABLE SQL statements
	private static final String	CREATE_QUIZ_TRACKER_TABLE	= "CREATE TABLE "
											+ QUIZ_TRACKER_TABLE_NAME
											+ " ("
											+ "id"
											+ " INTEGER PRIMARY KEY, "
											+ QUESTION_NUMBER_COLUMN
											+ " integer, "
											+ CORRECT_ANSWER_TRACKER_COLUMN
											+ " integer);";

	private static final String	CREATE_QUIZ_ATTEMPTS_TABLE	= "CREATE TABLE "
											+ ATTEMPTS_TABLE_NAME
											+ " ("
											+ "id"
											+ " INTEGER PRIMARY KEY, "
											+ ATTEMPTS_COLUMN
											+ " integer);";

	// Tag For Debugging (Logging)
	private String			TAG				= "MyOpenHelper class: ";

	public MyOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.d(TAG, "constructor");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		if (CommonProps.LOG_ENABLED)
			Log.d(TAG,
					"onCreate method: Creating two tables in mnknowt.db");

		try {

			// table creation
			db.execSQL(CREATE_QUIZ_TRACKER_TABLE);

			if (CommonProps.LOG_ENABLED)
				Log.d(TAG,
						" quiztracker table Successfully Created!");

			db.execSQL(CREATE_QUIZ_ATTEMPTS_TABLE);

			if (CommonProps.LOG_ENABLED)
				Log.d(TAG,
						" attempts table Successfully Created!");

			// default values into quiztracker table
			ContentValues content_values = new ContentValues();
			for (int i = 1; i < CommonProps.TOTAL_QUIZ_QUESTIONS; i++) {
				content_values.put(
						MyOpenHelper.QUESTION_NUMBER_COLUMN,
						i);
				content_values.put(
						MyOpenHelper.CORRECT_ANSWER_TRACKER_COLUMN,
						0);
				db.insert(MyOpenHelper.QUIZ_TRACKER_TABLE_NAME,
						null, content_values);
			}

			if (CommonProps.LOG_ENABLED)
				Log.d(TAG,
						" quiztracker Table Initialized with default rows!");

			// default values into attempts table
			ContentValues content_values_attemptsTable = new ContentValues();
			content_values_attemptsTable.put(
					MyOpenHelper.ATTEMPTS_COLUMN, 0);
			db.insert(ATTEMPTS_TABLE_NAME, null,
					content_values_attemptsTable);

		} catch (SQLiteException sqllite_error) {
			Log.v(TAG,
					"Error creating tables for first time: error message: "
							+ sqllite_error.toString());
		}
	}

	public boolean resetToDefaultValues(SQLiteDatabase db) {

		try {

			// delete all the rows in quiz tracker table
			db.delete(QUIZ_TRACKER_TABLE_NAME, null, null);
			db.delete(ATTEMPTS_TABLE_NAME, null, null);

			// default values into quiztracker table
			ContentValues content_values = new ContentValues();
			for (int i = 1; i < CommonProps.TOTAL_QUIZ_QUESTIONS; i++) {
				content_values.put(
						MyOpenHelper.QUESTION_NUMBER_COLUMN,
						i);
				content_values.put(
						MyOpenHelper.CORRECT_ANSWER_TRACKER_COLUMN,
						0);
				db.insert(MyOpenHelper.QUIZ_TRACKER_TABLE_NAME,
						null, content_values);
			}

			if (CommonProps.LOG_ENABLED)
				Log.d(TAG,
						" quiztracker Table Initialized with default rows!");

			// default values into attempts table
			ContentValues content_values_attemptsTable = new ContentValues();
			content_values_attemptsTable.put(
					MyOpenHelper.ATTEMPTS_COLUMN, 0);
			db.insert(ATTEMPTS_TABLE_NAME, null,
					content_values_attemptsTable);

			return true;

		} catch (SQLiteException message) {
			Log.v(TAG, "Error resetting DB to default values");
			return false;
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
