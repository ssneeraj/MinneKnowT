package com.jatayu.mnknowt;

/**
 * This class track's each attempted question in the on-going or the current
 * quiz that the user is taking. After the entire quiz is completed the data in
 * this class is mapped to the corresponding column in quiztracker table in
 * minneknowt.db
 * 
 * @author sharman
 * 
 */
public class OngoingQuizTracker {

	private static OngoingQuizTracker	instance;

	private int[]				question_number;

	private int[]				correct_answer_tracker;

	private OngoingQuizTracker() {
		question_number = new int[CommonProps.TOTAL_QUIZ_QUESTIONS];
		correct_answer_tracker = new int[CommonProps.TOTAL_QUIZ_QUESTIONS];
	}

	public static OngoingQuizTracker getInstance() {
		if (instance == null)
			instance = new OngoingQuizTracker();
		return instance;
	}

	public void setCorrectAnswerTracker(int currentQuestionIndex,
			int answeredCorrectValue) {
		if (currentQuestionIndex < CommonProps.TOTAL_QUIZ_QUESTIONS) {
			this.question_number[currentQuestionIndex] = currentQuestionIndex + 1;
			this.correct_answer_tracker[currentQuestionIndex] = answeredCorrectValue;
		}
	}

	public int[] getQuestion_number() {
		return question_number;
	}

	public int[] getCorrect_answer_tracker() {
		return correct_answer_tracker;
	}

}
