package com.jatayu;

public class QandA {

	private String questionNumber;
	private String questionText;
	private String correctAnswer;
	private String[] answers;

	public QandA() {
		answers = new String[4];
	}

	public String getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(String questionNumber) {
		this.questionNumber = questionNumber;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public String[] getAnswers() {
		return answers;
	}

	public void setAnswers(String[] answers) {
		System.arraycopy(answers, 0, this.answers, 0, answers.length);
	}
}
