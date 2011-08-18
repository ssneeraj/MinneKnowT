package com.jatayu;

import java.util.ArrayList;

public class QuizCache {

	private ArrayList<QandA> quizArrayList;
	private static QuizCache instance;

	private QuizCache() {
		quizArrayList = new ArrayList<QandA>();
	}

	public ArrayList<QandA> getQuiz() {
		return quizArrayList;
	}

	public void addQandA(QandA qa) {
		this.quizArrayList.add(qa);
	}

	public static synchronized QuizCache getInstance() {
		if (instance == null) {
			instance = new QuizCache();
		}
		return instance;
	}
}
