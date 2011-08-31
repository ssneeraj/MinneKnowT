package com.jatayu;

import java.util.ArrayList;

public class QuizCache {

	private static ArrayList<QandA> quizArrayList;
	private static QuizCache instance;

	private QuizCache() {
		quizArrayList = new ArrayList<QandA>();
	}

	public ArrayList<QandA> getQuiz() {
		return quizArrayList;
	}

	public void addQandA(QandA qa) {
		QuizCache.quizArrayList.add(qa);
	}

	public static synchronized QuizCache getInstance() {
		if (instance == null) {
			instance = new QuizCache();
		}
		return instance;
	}

	public static int getQuizTotalNumberofQuestion() {
		if (quizArrayList != null)
			return quizArrayList.size();
		else
			return 0;
	}
}
