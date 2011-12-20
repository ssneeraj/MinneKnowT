package com.jatayu.mnknowt;

import android.util.Log;

/**
 * Tracks the total correct answer and incorrect answers in the current quiz.
 * 
 * @author Neeraj Sharma
 * 
 */
public class OngoingQuizTracker {
	private int				total_correct_answers;
	private int				total_incorrect_answers;
	private final String			TAG	= "OngoingQuizTracker";
	private static OngoingQuizTracker	instance;

	private OngoingQuizTracker() {
		resetOngoingQuizTracker();
	}

	public static synchronized OngoingQuizTracker getInstance() {
		if (null == instance) {
			instance = new OngoingQuizTracker();
		}
		return instance;
	}

	public int getTotal_correct_answers() {
		Log.d(TAG, "Getting total corect answers: "
				+ total_correct_answers);
		return this.total_correct_answers;
	}

	public int getTotal_incorrect_answers() {
		Log.d(TAG, "Getting total incorect answers: "
				+ total_incorrect_answers);
		return this.total_incorrect_answers;
	}

	public void setCorrectAnswer() {
		this.total_correct_answers++;
		Log.d(TAG, "total corect answers: " + total_correct_answers);
	}

	public void setIncorrectAnswer() {
		this.total_incorrect_answers++;
		Log.d(TAG, "total incorect answers: " + total_incorrect_answers);
	}

	public void resetOngoingQuizTracker() {
		this.total_correct_answers = 0;
		this.total_incorrect_answers = 0;
	}
}
