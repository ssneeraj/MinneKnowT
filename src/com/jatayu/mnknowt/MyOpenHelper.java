package com.jatayu.mnknowt;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * A Helper class that manages all the database related stuff in the application
 * 
 * @author Neeraj Sharma
 * 
 */
public class MyOpenHelper extends SQLiteOpenHelper {

	// Database information
	private static final String	DATABASE_NAME				= "mnknowt.db";
	private static final int	DATABASE_VERSION			= 1;

	public static final String	SCORE_TABLE_NAME			= "score";
	public static final String	BEST_SCORE_COLUMN			= "bestscore";
	public static final String	WORST_SCORE_COLUMN			= "worstscore";
	private static final String	CREATE_SCORE_TABLE			= "CREATE TABLE "
												+ SCORE_TABLE_NAME
												+ " ("
												+ "id"
												+ " INTEGER PRIMARY KEY, "
												+ BEST_SCORE_COLUMN
												+ " integer, "
												+ WORST_SCORE_COLUMN
												+ " integer);";

	public static final String	TOTAL_QUESTIONS_ANSWERED_TABLE_NAME	= "totalquestionsanswered";
	public static final String	TOTAL_QUESTIONS_ANSWERED_COLUMN		= "totalquestions";
	private static final String	CREATE_TOTAL_QUESTIONS_ANSWERED_TABLE	= "CREATE TABLE "
												+ TOTAL_QUESTIONS_ANSWERED_TABLE_NAME
												+ " ("
												+ "id"
												+ " INTEGER PRIMARY KEY, "
												+ TOTAL_QUESTIONS_ANSWERED_COLUMN
												+ " integer);";

	public static final String	CORRECT_INCORRECT_TABLE_NAME		= "correctincorrect";
	public static final String	CORRECT_COLUMN				= "correct";
	public static final String	INCORRECT_COLUMN			= "incorrect";
	private static final String	CREATE_CORRECT_INCORRECT_TABLE		= "CREATE TABLE "
												+ CORRECT_INCORRECT_TABLE_NAME
												+ " ("
												+ "id"
												+ " INTEGER PRIMARY KEY, "
												+ CORRECT_COLUMN
												+ " integer, "
												+ INCORRECT_COLUMN
												+ " integer);";

	// Tag For Debugging (Logging)
	private String			TAG					= "MyOpenHelper class: ";

	public MyOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		if (CommonProps.LOG_ENABLED)
			Log.d(TAG, "constructor");
	}

	public void onCreate(SQLiteDatabase db) {

		try {

			// ------------------ CREATE 'score' TABLE
			db.execSQL(CREATE_SCORE_TABLE);

			if (CommonProps.LOG_ENABLED)
				Log.d(TAG, " score table Successfully Created!");

			// ----------- CREATE 'totalquestionsanswered' TABLE
			db.execSQL(CREATE_TOTAL_QUESTIONS_ANSWERED_TABLE);

			if (CommonProps.LOG_ENABLED)
				Log.d(TAG,
						" total questions answered table Successfully Created!");

			// ------------------ CREATE 'correctincorrect TABLE
			db.execSQL(CREATE_CORRECT_INCORRECT_TABLE);

			if (CommonProps.LOG_ENABLED)
				Log.d(TAG,
						" correct incorrect table Successfully Created!");

			// --------------- PUT DEFAULT VALUES IN 'score' TABLE
			ContentValues content_values_scoreTable = new ContentValues();
			content_values_scoreTable.put(
					MyOpenHelper.BEST_SCORE_COLUMN, 11);
			content_values_scoreTable.put(
					MyOpenHelper.WORST_SCORE_COLUMN, 11);
			db.insert(MyOpenHelper.SCORE_TABLE_NAME, null,
					content_values_scoreTable);

			if (CommonProps.LOG_ENABLED)
				Log.d(TAG,
						" score table Initialized with default values!");

			// PUT DEFAULT VALUES IN 'totalquestionsanswered' TABLE
			ContentValues content_values_totalquestions = new ContentValues();
			content_values_totalquestions
					.put(MyOpenHelper.TOTAL_QUESTIONS_ANSWERED_COLUMN,
							0);
			db.insert(MyOpenHelper.TOTAL_QUESTIONS_ANSWERED_TABLE_NAME,
					null, content_values_totalquestions);

			if (CommonProps.LOG_ENABLED)
				Log.d(TAG,
						" totalquestionsanswered table Initialized with default value!");

			// ------- PUT DEFAULT VALUES IN 'correctincorrect'
			// TABLE
			ContentValues content_values_correctincorrect = new ContentValues();
			content_values_correctincorrect.put(
					MyOpenHelper.CORRECT_COLUMN, 0);
			content_values_correctincorrect.put(
					MyOpenHelper.INCORRECT_COLUMN, 0);

			db.insert(MyOpenHelper.CORRECT_INCORRECT_TABLE_NAME,
					null, content_values_correctincorrect);
			if (CommonProps.LOG_ENABLED)
				Log.d(TAG,
						" correctincorrect table Initialized with default value!");

		} catch (SQLiteException sqllite_error) {
			Log.v(TAG,
					"Error creating tables for first time: error message: "
							+ sqllite_error.toString());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
