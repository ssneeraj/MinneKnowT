package com.jatayu;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyOpenHelper extends SQLiteOpenHelper {

	// Database information
	private static final String DATABASE_NAME = "mnknowt.db";
	private static final int DATABASE_VERSION = 1;

	// Table information
	public static final String QUIZ_TRACKER_TABLE_NAME = "quiztracker";
	public static final String ATTEMPTS_TABLE_NAME = "attempts";

	// Column information
	public static final String QUESTION_NUMBER_COLUMN = "questionnumber";
	public static final String CORRECT_ANSWER_TRACKER_COLUMN = "correctanswertracker";
	public static final String ATTEMPTS_COLUMN = "attempts";

	// CREATE TABLE SQL statements
	private static final String CREATE_QUIZ_TRACKER_TABLE = "CREATE TABLE "
			+ QUIZ_TRACKER_TABLE_NAME + " (" + "id" + " INTEGER PRIMARY KEY, "
			+ QUESTION_NUMBER_COLUMN + " integer, "
			+ CORRECT_ANSWER_TRACKER_COLUMN + " integer);";

	private static final String CREATE_QUIZ_ATTEMPTS_TABLE = "CREATE TABLE "
			+ ATTEMPTS_TABLE_NAME + " (" + "id" + " INTEGER PRIMARY KEY, "
			+ ATTEMPTS_COLUMN + " integer);";

	// For Debugging
	private String TAG = "MyOpenHelper class: ";

	public MyOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.d(TAG, "constructor");
	}

	// private boolean checkifTableExists() {
	//
	// try {
	// SQLiteDatabase sdb = null;
	//
	// SQLiteDatabase.openDatabase(
	// "/data/data/com.jatayu/databases/mnknowt.db", null,
	// SQLiteDatabase.OPEN_READONLY);
	// return true;
	// } catch (SQLiteException message) {
	// Log.v(TAG,
	// "error while checking if table exists: "
	// + message.toString());
	// return false;
	// }
	//
	// }

	@Override
	public void onCreate(SQLiteDatabase db) {

		Log.d(TAG, "onCreate method: Creating two tables in mnknowt.db");
		try {

			// table creation
			db.execSQL(CREATE_QUIZ_TRACKER_TABLE);
			Log.d(TAG, " quiztracker table Successfully Created!");

			db.execSQL(CREATE_QUIZ_ATTEMPTS_TABLE);
			Log.d(TAG, " attempts table Successfully Created!");

			// default values into quiztracker table
			ContentValues content_values = new ContentValues();
			for (int i = 1; i < CommonProps.TOTAL_QUIZ_QUESTIONS; i++) {
				content_values.put(MyOpenHelper.QUESTION_NUMBER_COLUMN, i);
				content_values.put(MyOpenHelper.CORRECT_ANSWER_TRACKER_COLUMN,
						0);
				db.insert(MyOpenHelper.QUIZ_TRACKER_TABLE_NAME, null,
						content_values);
			}
			Log.d(TAG, " quiztracker Table Initialized with default rows!");

			// default values into attempts table
			ContentValues content_values_attemptsTable = new ContentValues();
			content_values_attemptsTable.put(MyOpenHelper.ATTEMPTS_COLUMN, 0);
			db.insert(ATTEMPTS_TABLE_NAME, null, content_values_attemptsTable);

			// if (db.isOpen())
			// db.close();
			// Log.v(TAG,
			// " Database closed after successful creationa and initialization");

		} catch (SQLiteException sqllite_error) {
			Log.v(TAG, "Error creating tables for first time: error message: "
					+ sqllite_error.toString());
		}
		// insert default values into the tables when the database is created
		// for the first time
		// initializeDB(db);
	}

	/*
	 * The onCreate() method of MyOpenHelper is invoked only once. Since
	 * initializeDB() is invoked by onCreate(), even initializeDB() will be
	 * invoked ONLY once when the database is created! This method inserts
	 * default values in all the tables (i.e. 'quiztracker' table and 'attempts'
	 * table in mnknowt.db)
	 */
	private void initializeDB(SQLiteDatabase db) {

		// Now we want to add some default values to the quiztracker table
		ContentValues values = new ContentValues();
		for (int i = 0; i < CommonProps.TOTAL_QUIZ_QUESTIONS; i++) {
			values.put(MyOpenHelper.QUESTION_NUMBER_COLUMN, i + 1);
			values.put(MyOpenHelper.CORRECT_ANSWER_TRACKER_COLUMN, 0);
		}

		// Insert default values for 'questionnumber' column and
		// 'correctanswertracker' column in 'quiztracker' table
		db.insert(MyOpenHelper.QUIZ_TRACKER_TABLE_NAME, null, values);

		// since we want to reuse 'values', clear it
		values.clear();

		// Now to set default value in 'attempts' table in 'attempts' column
		values.put(ATTEMPTS_COLUMN, 0);
		// Again insert this into the 'attempts' table
		db.insert(ATTEMPTS_TABLE_NAME, null, values);

		// close the database
		if (db.isOpen())
			db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}
