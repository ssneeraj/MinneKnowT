package com.jatayu.mnknowt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * This class displays a question and 4 answer choices in ListView. It gets an
 * individual question and answer set at a time and displays it.
 * 
 * @author sharman
 * 
 */

public class QuizActivity extends ListActivity {

	// this points to the question number that the user is currently
	// answering
	private int				currentQuestionIndex	= 0;

	private String[]			answerData		= new String[4];
	private ArrayList<QandA>		quizArrayList		= QuizCache.getInstance()
											.getQuiz();
	private String				correct_answer;
	// private QuizStat quiz_stat = QuizStat.getInstance();

	// If the user selects an incorrect answer or does not attempt to
	// answer,
	// store a '0' in integer array 'answer_selected_stat' and store a '1'
	// for
	// correct answer choice
	// private int[] answer_selected_stat = new
	// int[CommonProps.TOTAL_QUIZ_QUESTIONS];
	private OngoingQuizTracker		quiz_tracker;
	// private boolean questionAttempted = false;
	private StringBuffer			quizQuestionNumberText;
	private AnswerCustomAdapter		custom_adapter;

	private boolean				hasAnswered		= false;

	private HashMap<String, Integer>	roadSignMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.real_quiz_layout_v2);

		initializeRoadSignHashMap();

		// Shuffle the quiz
		Collections.shuffle(quizArrayList);

		quizQuestionNumberText = new StringBuffer();

		getListView().setCacheColorHint(0);

		// quiz_stat.clearQuizStat();

		quiz_tracker = OngoingQuizTracker.getInstance();
		quiz_tracker.resetValues();

		showSingleQandA();

	}

	private void initializeRoadSignHashMap() {
		roadSignMap = new HashMap<String, Integer>();
		roadSignMap.put("w1", R.drawable.w1);
		roadSignMap.put("w2", R.drawable.w2);
		roadSignMap.put("w3", R.drawable.w3);
		roadSignMap.put("w4", R.drawable.w4);
		roadSignMap.put("w5", R.drawable.w5);
		roadSignMap.put("w6", R.drawable.w6);
		roadSignMap.put("w7", R.drawable.w7);
		roadSignMap.put("w8", R.drawable.w8);
		roadSignMap.put("w9", R.drawable.w9);
		roadSignMap.put("w10", R.drawable.w10);
		roadSignMap.put("w11", R.drawable.w11);
		roadSignMap.put("w12", R.drawable.w12);
		roadSignMap.put("w13", R.drawable.w13);
		roadSignMap.put("w14", R.drawable.w14);
		roadSignMap.put("w15", R.drawable.w15);
		roadSignMap.put("w16", R.drawable.w16);
		roadSignMap.put("w17", R.drawable.w17);
		roadSignMap.put("w18", R.drawable.w18);
		roadSignMap.put("w19", R.drawable.w19);
		roadSignMap.put("w20", R.drawable.w20);
		roadSignMap.put("w21", R.drawable.w21);
		roadSignMap.put("w22", R.drawable.w22);
		roadSignMap.put("w23", R.drawable.w23);
		roadSignMap.put("w24", R.drawable.w24);
		roadSignMap.put("w25", R.drawable.w25);
		roadSignMap.put("w26", R.drawable.w26);
		roadSignMap.put("w27", R.drawable.w27);
		roadSignMap.put("w28", R.drawable.w28);
		roadSignMap.put("w29", R.drawable.w29);
	}

	/**
	 * This method populates the Quiz UI (that is displays a single question
	 * and (4 answer choices) with Quiz data which is stored in a
	 */
	private void showSingleQandA() {

		currentQuestionIndex = 0;

		QandA singleQandA = quizArrayList.get(currentQuestionIndex);

		ImageView quiz_roadSign_IV = (ImageView) findViewById(R.id.realQuiz_roadSignIV);

		// Check if it is a road sign questions if so, then show the
		// ImageView which will display the road sign
		if (!singleQandA.getImageFileName().equalsIgnoreCase("na")) {
			quiz_roadSign_IV.setVisibility(View.VISIBLE);
			quiz_roadSign_IV.setImageResource(roadSignMap.get(
					singleQandA.getImageFileName())
					.intValue());
		} else {
			quiz_roadSign_IV.setVisibility(View.GONE);
		}

		quizQuestionNumberText.setLength(0);
		quizQuestionNumberText.append(currentQuestionIndex + 1);
		quizQuestionNumberText.append(" of ");
		quizQuestionNumberText.append(CommonProps.TOTAL_QUIZ_QUESTIONS);

		correct_answer = singleQandA.getCorrectAnswer();

		System.arraycopy(singleQandA.getAnswers(), 0, answerData, 0, 4);

		// We are using ArrayAdapter to bind underlying date (an array
		// of strings) to ListView defined in quizlayout.xml
		// if (array_adapter == null) {
		// array_adapter = new ArrayAdapter<String>(this,
		// R.layout.list_item, answerData);
		// }

		custom_adapter = new AnswerCustomAdapter(this, answerData);

		TextView realQuiz_questionText = (TextView) this
				.findViewById(R.id.realQuiz_questionText);
		realQuiz_questionText.setText(singleQandA.getQuestionText());

		TextView realQuiz_questionNumberTextView = (TextView) this
				.findViewById(R.id.quiz_questionNumberValue);
		realQuiz_questionNumberTextView.setText(quizQuestionNumberText);

		// This binds the array adapter to our QuizActvity
		// setListAdapter(array_adapter);
		setListAdapter(custom_adapter);
		// array_adapter.setNotifyOnChange(true);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		if (!hasAnswered) {
			v.setBackgroundColor(Color.GRAY);

			TextView realQuiz_answer_result_title = (TextView) this
					.findViewById(R.id.realQuiz_answer_result_title);
			realQuiz_answer_result_title
					.setVisibility(TextView.VISIBLE);

			TextView realQuiz_answer_result_description = (TextView) this
					.findViewById(R.id.realQuiz_answer_result_description);

			// If onListItemClick is invoked it means the user has
			// selected
			// an answer choice
			// questionAttempted = true;

			// String toastText = null;

			Integer selectedListItemPosition = position;

			// compare if the user select answer choice matches the
			// actual
			// correct answer
			if (correct_answer
					.equalsIgnoreCase(selectedListItemPosition
							.toString())) {
				realQuiz_answer_result_title
						.setTextColor(getResources()
								.getColor(R.color.green));
				realQuiz_answer_result_title.setText("Correct");
				// toastText = "correct answer";
				// v.setBackgroundColor(Color.GREEN);

				// answer_selected_stat[currentQuestionIndex] =
				// CommonProps.ANSWERED_CORRECT;

				// If correct answer, quiz tracker sets correct
				// answer
				// tracker to 1
				// quiz_tracker.setCorrectAnswerTracker(
				// currentQuestionIndex, 1);

				// If the user selects the correct answer
				quiz_tracker.setCorrectAnswer();

			} else {

				realQuiz_answer_result_title
						.setTextColor(Color.RED);
				realQuiz_answer_result_title
						.setText("Incorrect");
				realQuiz_answer_result_description
						.setVisibility(TextView.VISIBLE);
				realQuiz_answer_result_description
						.setTextColor(Color.RED);
				realQuiz_answer_result_description
						.setText(getCorrectAnswerText());

				View child = l.getChildAt(getCorrectAnswerPosition());

				TextView tv = (TextView) child
						.findViewById(R.id.answerListItemTV1);
				tv.setTextColor(getResources().getColor(
						R.color.green));

				TextView tv2 = (TextView) child
						.findViewById(R.id.answerListItemTV2);
				tv2.setTextColor(getResources().getColor(
						R.color.green));

				// toastText = "wrong answer";
				// v.setBackgroundColor(Color.RED);

				// answer_selected_stat[currentQuestionIndex] =
				// CommonProps.ANSWERED_INCORRECT;

				// If incorrect answer quiz tracker sets correct
				// answer
				// tracker to 0
				// quiz_tracker.setCorrectAnswerTracker(
				// currentQuestionIndex, 0);

				// If the user selects the incorrect answer
				quiz_tracker.setIncorrectAnswer();

			}

			v.setFocusable(false);
			v.setClickable(false);

			// Toast toast = Toast.makeText(QuizActivity.this,
			// toastText,
			// Toast.LENGTH_SHORT);
			// toast.show();

			l.setFocusable(false);

			ImageButton realQuiz_nextButton = (ImageButton) findViewById(R.id.realQuiz_nextButton);
			realQuiz_nextButton.setVisibility(ImageButton.VISIBLE);
			// showNextQuestion();

			hasAnswered = true;
		}

	}

	public void showNextQuestion(View view) {

		hasAnswered = false;

		ImageButton realQuiz_nextButton = (ImageButton) findViewById(R.id.realQuiz_nextButton);
		realQuiz_nextButton.setVisibility(ImageButton.INVISIBLE);

		TextView realQuiz_answer_result_title = (TextView) this
				.findViewById(R.id.realQuiz_answer_result_title);
		realQuiz_answer_result_title.setVisibility(TextView.INVISIBLE);

		TextView realQuiz_answer_result_description = (TextView) this
				.findViewById(R.id.realQuiz_answer_result_description);
		realQuiz_answer_result_description
				.setVisibility(TextView.INVISIBLE);

		// When the user selects 'Next' button, first check if the
		// questions was attempted or not!
		// if (questionAttempted) {
		// questionAttempted = false;
		// } else {
		// // if the question was not attempted we assume that the
		// // answer tracker is 0
		// quiz_tracker.setCorrectAnswerTracker(
		// currentQuestionIndex, 0);
		// }

		// first increment the currentQuestionIndex so that we advance
		// to the next question
		currentQuestionIndex++;

		// check if the quiz index is more than 10. If it is more than
		// 10 it indicates that the quis has completed
		if (currentQuestionIndex > (CommonProps.TOTAL_QUIZ_QUESTIONS - 1)) {
			// Intent intent = new Intent(this,
			// QuizCompletedActivity.class);

			new QuizDBSaverTask(QuizActivity.this).execute();
			// startActivity(intent);

			// Since Quiz is over we want to finish QuizActivity
			// finish();

			return;
		}

		// If Quiz is over and 'Next' is pressed, then show a Toast
		// if (currentQuestionIndex > QuizCache
		// .getQuizTotalNumberofQuestion() - 1) {
		//
		// // save the answer selected stat into quiz stat
		// quiz_stat.saveQandAInfo(answer_selected_stat);
		//
		// // invoke QuizResultActivity
		// Intent intent = new Intent(this,
		// QuizResultActivity.class);
		//
		// startActivity(intent);
		//
		// // Since Quiz is over we want to finish QuizActivity
		// finish();
		//
		// return;
		// }

		QandA singleQandA = quizArrayList.get(currentQuestionIndex);

		ImageView quiz_roadSign_IV = (ImageView) findViewById(R.id.realQuiz_roadSignIV);

		// Check if it is a road sign questions if so, then show the
		// ImageView which will display the road sign

		if (singleQandA.getImageFileName().length() > 2) {
			quiz_roadSign_IV.setVisibility(View.VISIBLE);
			quiz_roadSign_IV.setImageResource(roadSignMap.get(
					singleQandA.getImageFileName())
					.intValue());
		} else {
			quiz_roadSign_IV.setVisibility(View.GONE);
		}

		// TextView questionNumber = (TextView) this
		// .findViewById(R.id.questionNumber);
		// questionNumber.setText("Question: "
		// + singleQandA.getQuestionNumber() + " of "
		// + QuizCache.getQuizTotalNumberofQuestion());

		// Setup quiz question number text
		quizQuestionNumberText.setLength(0);
		quizQuestionNumberText.append(currentQuestionIndex + 1);
		quizQuestionNumberText.append(" of ");
		quizQuestionNumberText.append(CommonProps.TOTAL_QUIZ_QUESTIONS);

		TextView question = (TextView) this
				.findViewById(R.id.realQuiz_questionText);
		// question.setText(singleQandA.getQuestionText());
		question.setText(singleQandA.getQuestionText());

		correct_answer = singleQandA.getCorrectAnswer();

		System.arraycopy(singleQandA.getAnswers(), 0, answerData, 0, 4);

		// We are using ArrayAdapter to bind underlying date (an array
		// of strings) to ListView in quizlayout
		// if (array_adapter == null) {
		// array_adapter = new ArrayAdapter<String>(this,
		// R.layout.list_item, answerData);
		// }

		// This binds the array adapter to our QuizActvity
		// setListAdapter(array_adapter);
		if (custom_adapter == null) {
			custom_adapter = new AnswerCustomAdapter(this,
					answerData);
		} else {
			custom_adapter.setAnswerChoiceText(singleQandA
					.getAnswers());
		}

		TextView quiz_questionNumberTextView = (TextView) this
				.findViewById(R.id.quiz_questionNumberValue);

		quiz_questionNumberTextView.setText(quizQuestionNumberText);

		setListAdapter(custom_adapter);
	}

	public void showAppHomePage(View view) {
		finish(); // invoke finish() will close this (i.e.
		// QuizActivity)
	}

	public void shuffleQuiz(View view) {
		currentQuestionIndex = 0;
		QuizCache.getInstance().shuffleQuizCache();
	}

	public String getCorrectAnswerText() {

		QandA singleQandA = quizArrayList.get(currentQuestionIndex);

		String ans = singleQandA.getCorrectAnswer();
		String ansLetter = null;

		if (ans.equalsIgnoreCase("0")) {
			ansLetter = "Correct answer is A";
		} else if (ans.equalsIgnoreCase("1")) {
			ansLetter = "Correct answer is B";
		} else if (ans.equalsIgnoreCase("2")) {
			ansLetter = "Correct answer is C";
		} else if (ans.equalsIgnoreCase("3")) {
			ansLetter = "Correct answer is D";
		}

		return ansLetter;
	}

	public int getCorrectAnswerPosition() {

		QandA singleQandA = quizArrayList.get(currentQuestionIndex);

		String ans = singleQandA.getCorrectAnswer();

		if (ans.equalsIgnoreCase("0")) {
			return 0;
		} else if (ans.equalsIgnoreCase("1")) {
			return 1;
		} else if (ans.equalsIgnoreCase("2")) {
			return 2;
		} else if (ans.equalsIgnoreCase("3")) {
			return 3;
		}
		return 4;
	}

	private class QuizDBSaverTask extends AsyncTask<Void, Void, Boolean> {

		private boolean		isQuizSavedToDB	= false;
		private Context		context;
		private ProgressDialog	progressDialog;

		public QuizDBSaverTask(QuizActivity context) {
			this.context = context;
			progressDialog = new ProgressDialog(context);
		}

		protected void onPreExecute() {
			progressDialog.setMessage("Saving Quiz Stats...");
			progressDialog.show();
		}

		protected Boolean doInBackground(Void... arg0) {

			isQuizSavedToDB = true;
			saveToDataBase();
			return isQuizSavedToDB;
		}

		private void saveToDataBase() {
			// if (CommonProps.LOG_ENABLED)
			Log.d("QuizActivity",
					" >>> Saving current Quiz information to database");

			QuizDBManager qdbm = new QuizDBManager(context);
			qdbm.saveQuizInformationToDB();
		}

		protected void onPostExecute(Boolean result) {

			if (progressDialog.isShowing())
				progressDialog.dismiss();

			if (result) {
				Intent intent = new Intent(QuizActivity.this,
						QuizCompletedActivity.class);
				startActivity(intent);

				finish();
			}
		}

	}
}
