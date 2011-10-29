package com.jatayu.mnknowt;

/**
 * Stores an integer array that either contains a 1 for correct answer and 2 for
 * incorrect answer
 * 
 * @author sharman
 * 
 */
public class QuizStat {

	private static QuizStat	instance;

	private int[]		resultType	= new int[CommonProps.TOTAL_QUIZ_QUESTIONS];

	private int		number_of_correct_ans;

	/**
	 * Private constructor, this prevents QuizStat being instantiated from
	 * outside classes
	 */
	private QuizStat() {

	}

	public static QuizStat getInstance() {
		if (instance == null)
			instance = new QuizStat();
		return instance;
	}

	public void saveQandAInfo(int[] answer_selected_stat) {
		System.arraycopy(answer_selected_stat, 0, resultType, 0,
				answer_selected_stat.length);
	}

	public int getNumberOfCorrectAnswer() {
		for (int i = 0; i < resultType.length; i++) {
			if (resultType[i] == CommonProps.ANSWERED_CORRECT) {
				number_of_correct_ans++;
			}
		}

		return number_of_correct_ans;
	}

	public void clearQuizStat() {
		number_of_correct_ans = 0;
	}

	public int getPercentageOfCorrectAnswers() {
		return ((100 * number_of_correct_ans) / CommonProps.TOTAL_QUIZ_QUESTIONS);
	}

}
