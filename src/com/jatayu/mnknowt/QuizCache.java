package com.jatayu.mnknowt;

import java.util.ArrayList;
import java.util.Collections;

public class QuizCache {

	private static ArrayList<QandA>	quizArrayList;
	private static QuizCache	instance;
	private static boolean		quizcacheEmpty	= true;

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
			quizcacheEmpty = true; // set quiz cache is empty
		}
		return instance;
	}

	public static int getQuizTotalNumberofQuestion() {
		if (quizArrayList != null)
			return quizArrayList.size();
		else
			return 0;
	}

	public static boolean isQuizcacheEmpty() {
		return quizcacheEmpty;
	}

	public static void setQuizcacheEmpty(boolean quizcacheEmpty) {
		QuizCache.quizcacheEmpty = quizcacheEmpty;
	}

	public void shuffleQuizCache() {
		Collections.shuffle(quizArrayList);
	}
}
