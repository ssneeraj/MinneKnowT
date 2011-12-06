package com.jatayu.mnknowt;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class displays a question and 4 answer choices in ListView. It gets an
 * individual question and answer set at a time and displays it.
 * 
 * @author sharman
 * 
 */

public class FlashCardsActivity extends ListActivity {

	// this points to the question number that the user is currently
	// answering
	private int				currentQuestionIndex	= 0;

	private String[]			answerData		= new String[4];
	private ArrayList<QandA>		quizArrayList		= QuizCache.getInstance()
											.getQuiz();
	private boolean				questionAttempted	= false;
	private StringBuffer			quizQuestionNumberText;
	private AnswerCustomAdapter		custom_adapter;

	private HashMap<String, Integer>	roadSignMap;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flashcards_layout_v2);

		initializeRoadSignHashMap();

		quizQuestionNumberText = new StringBuffer();

		getListView().setCacheColorHint(0);

		showIndividualFlashCard();

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
	private void showIndividualFlashCard() {

		QandA singleQandA = quizArrayList.get(currentQuestionIndex);
		ImageView flashCards_roadSign_IV = (ImageView) findViewById(R.id.flashcards_roadSignIV);

		// Check if it is a road sign questions if so, then show the
		// ImageView which will display the road sign
		if (!singleQandA.getImageFileName().equalsIgnoreCase("na")) {
			flashCards_roadSign_IV.setVisibility(View.VISIBLE);
			flashCards_roadSign_IV.setImageResource(roadSignMap
					.get(singleQandA.getImageFileName())
					.intValue());
		} else {
			flashCards_roadSign_IV.setVisibility(View.GONE);
		}

		quizQuestionNumberText.setLength(0);
		quizQuestionNumberText.append(singleQandA.getQuestionNumber());
		quizQuestionNumberText.append(" of ");
		quizQuestionNumberText.append(QuizCache
				.getQuizTotalNumberofQuestion());

		// correct_answer = singleQandA.getCorrectAnswer();

		System.arraycopy(singleQandA.getAnswers(), 0, answerData, 0, 4);

		// We are using ArrayAdapter to bind underlying date (an array
		// of strings) to ListView defined in quizlayout.xml
		// if (array_adapter == null) {
		// array_adapter = new ArrayAdapter<String>(this,
		// R.layout.list_item, answerData);
		// }

		custom_adapter = new AnswerCustomAdapter(this, answerData);

		TextView flashcards_questionText = (TextView) this
				.findViewById(R.id.flashcards_questionText);
		flashcards_questionText.setText(singleQandA.getQuestionText());

		TextView flashcards_questionNumberTextView = (TextView) this
				.findViewById(R.id.flashcards_questionNumberText);
		flashcards_questionNumberTextView
				.setText(quizQuestionNumberText);

		// This binds the array adapter to our QuizActvity
		// setListAdapter(array_adapter);
		setListAdapter(custom_adapter);
		// array_adapter.setNotifyOnChange(true);
	}

	// @Override
	// protected void onListItemClick(ListView l, View v, int position, long
	// id) {
	//
	// // If onListItemClick is invoked it means the user has selected
	// // an answer choice
	// questionAttempted = true;
	//
	// // String toastText = null;
	//
	// Integer selectedListItemPosition = position;
	//
	// // compare if the user select answer choice matches the actual
	// // correct answer
	// if (correct_answer.equalsIgnoreCase(selectedListItemPosition
	// .toString())) {
	//
	// // toastText = "correct answer";
	// // v.setBackgroundColor(Color.GREEN);
	//
	// answer_selected_stat[currentQuestionIndex] =
	// CommonProps.ANSWERED_CORRECT;
	//
	// // If correct answer, quiz tracker sets correct answer
	// // tracker to 1
	// // quiz_tracker.setCorrectAnswerTracker(
	// // currentQuestionIndex, 1);
	// } else {
	//
	// // toastText = "wrong answer";
	// // v.setBackgroundColor(Color.RED);
	//
	// answer_selected_stat[currentQuestionIndex] =
	// CommonProps.ANSWERED_INCORRECT;
	//
	// // If incorrect answer quiz tracker sets correct answer
	// // tracker to 0
	// // quiz_tracker.setCorrectAnswerTracker(
	// // currentQuestionIndex, 0);
	//
	// }
	//
	// v.setFocusable(false);
	// v.setClickable(false);
	//
	// // Toast toast = Toast.makeText(QuizActivity.this, toastText,
	// // Toast.LENGTH_SHORT);
	// // toast.show();
	//
	// l.setFocusable(false);
	//
	// // showNextQuestion();
	//
	// }

	public void showNextQuestion(View view) {

		// When the user selects 'Next' button, first check if the
		// questions was attempted or not!
		if (questionAttempted) {
			questionAttempted = false;
		} else {
			// if the question was not attempted we assume that the
			// answer tracker is 0
			// quiz_tracker.setCorrectAnswerTracker(
			// currentQuestionIndex, 0);
		}

		// first increment the currentQuestionIndex so that we advance
		// to the next question
		currentQuestionIndex++;

		// If Quiz is over and 'Next' is pressed, then show a Toast
		if (currentQuestionIndex > QuizCache
				.getQuizTotalNumberofQuestion() - 1) {

			Toast toast = Toast.makeText(FlashCardsActivity.this,
					"Quiz Over!", Toast.LENGTH_LONG);
			toast.show();

			// save the answer selected stat into quiz stat
			// quiz_stat.saveQandAInfo(answer_selected_stat);

			// invoke QuizResultActivity
			Intent intent = new Intent(this,
					QuizCompletedActivity.class);

			startActivity(intent);

			// Since Quiz is over we want to finish QuizActivity
			finish();

			return;
		}

		QandA singleQandA = quizArrayList.get(currentQuestionIndex);
		ImageView flashCards_roadSign_IV = (ImageView) findViewById(R.id.flashcards_roadSignIV);

		// Check if it is a road sign questions if so, then show the
		// ImageView which will display the road sign
		if (!singleQandA.getImageFileName().equalsIgnoreCase("na")) {
			flashCards_roadSign_IV.setVisibility(View.VISIBLE);
			flashCards_roadSign_IV.setImageResource(roadSignMap
					.get(singleQandA.getImageFileName())
					.intValue());
		} else {
			flashCards_roadSign_IV.setVisibility(View.GONE);
		}

		// TextView questionNumber = (TextView) this
		// .findViewById(R.id.questionNumber);
		// questionNumber.setText("Question: "
		// + singleQandA.getQuestionNumber() + " of "
		// + QuizCache.getQuizTotalNumberofQuestion());

		// Setup quiz question number text
		quizQuestionNumberText.setLength(0);
		quizQuestionNumberText.append(singleQandA.getQuestionNumber());
		quizQuestionNumberText.append(" of ");
		quizQuestionNumberText.append(QuizCache
				.getQuizTotalNumberofQuestion());

		TextView question = (TextView) this
				.findViewById(R.id.flashcards_questionText);
		// question.setText(singleQandA.getQuestionText());
		question.setText(singleQandA.getQuestionText());

		// correct_answer = singleQandA.getCorrectAnswer();

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

		TextView flashcards_questionNumberTextView = (TextView) this
				.findViewById(R.id.flashcards_questionNumberText);

		flashcards_questionNumberTextView
				.setText(quizQuestionNumberText);

		TextView peekBar = (TextView) this
				.findViewById(R.id.flashCards_peekBar);

		peekBar.setBackgroundColor(Color.BLACK);
		peekBar.setText("Peek");
		peekBar.setTextColor(getResources().getColor(R.color.white));
		peekBar.setGravity(Gravity.RIGHT);

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

	public void peekCorrectAnswer(View view) {

		QandA singleQandA = quizArrayList.get(currentQuestionIndex);

		String ans = singleQandA.getCorrectAnswer();
		String ansLetter = null;

		int position = 0;

		if (ans.equalsIgnoreCase("0")) {
			position = 0;
			ansLetter = "A";
		} else if (ans.equalsIgnoreCase("1")) {
			position = 1;
			ansLetter = "B";
		} else if (ans.equalsIgnoreCase("2")) {
			position = 2;
			ansLetter = "C";
		} else if (ans.equalsIgnoreCase("3")) {
			position = 3;
			ansLetter = "D";
		}
		ListView lv = getListView();

		View child = lv.getChildAt(position);

		TextView tv = (TextView) child
				.findViewById(R.id.answerListItemTV1);
		tv.setTextColor(getResources().getColor(R.color.green));

		TextView tv2 = (TextView) child
				.findViewById(R.id.answerListItemTV2);
		tv2.setTextColor(getResources().getColor(R.color.green));

		TextView peekBar = (TextView) this
				.findViewById(R.id.flashCards_peekBar);

		peekBar.setBackgroundColor(Color.GRAY);
		peekBar.setText(ansLetter);
		peekBar.setTextColor(getResources().getColor(R.color.white));
		peekBar.setGravity(Gravity.CENTER);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		event.
		return super.onTouchEvent(event);
	}
}
