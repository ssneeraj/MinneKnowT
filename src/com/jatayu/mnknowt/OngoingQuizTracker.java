package com.jatayu.mnknowt;

public class OngoingQuizTracker {

	private static OngoingQuizTracker	instance;
	private static int			total_correct_answers	= 0;
	private static int			total_incorrect_answers	= 0;

	public int getTotal_correct_answers() {
		return total_correct_answers;
	}

	public int getTotal_incorrect_answers() {
		return total_incorrect_answers;
	}

	private OngoingQuizTracker() {
	}

	public static OngoingQuizTracker getInstance() {
		if (instance == null)
			instance = new OngoingQuizTracker();
		return instance;
	}

	public void setCorrectAnswer() {
		total_correct_answers++;
	}

	public void setIncorrectAnswer() {
		total_incorrect_answers++;
	}

	public void resetValues() {
		total_correct_answers = 0;
		total_incorrect_answers = 0;
	}
}
