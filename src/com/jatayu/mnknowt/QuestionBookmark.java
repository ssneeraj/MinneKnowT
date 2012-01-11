package com.jatayu.mnknowt;

public class QuestionBookmark {

	private static QuestionBookmark	instance;
	private int[]			question_bookmarks;

	private QuestionBookmark() {
		question_bookmarks = new int[CommonProps.TOTAL_QUIZ_QUESTIONS];
	}

	public static QuestionBookmark getInstance() {
		if (instance == null)
			instance = new QuestionBookmark();
		return instance;
	}

	public void setQuestion_bookmarks(int index, int value) {
		this.question_bookmarks[index] = value;
	}

	public int[] getQuestion_bookmarks() {
		return question_bookmarks;
	}
}
