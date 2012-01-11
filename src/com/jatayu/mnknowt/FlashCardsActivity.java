package com.jatayu.mnknowt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.app.ListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * This class displays a Flash Card UI comprising of a Question text with it's
 * corresponding answer choices. Select the 'Peek' button at the bottom to peek
 * at the correct answer choice. Swipe across the screen or touch the right
 * arrow at the bottom to navigate to the next flash card.
 * 
 * @author Neeraj Sharma
 * 
 */
public class FlashCardsActivity extends ListActivity {

	private int				currentQuestionIndex	= 0;

	private String[]			answerData		= new String[4];
	private ArrayList<QandA>		quizArrayList		= QuizCache.getInstance()
											.getQuiz();
	private StringBuffer			quizQuestionNumberText;
	private AnswerCustomAdapter		custom_adapter;

	private HashMap<String, Integer>	roadSignMap;

	private float				touchX;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flashcards_layout_v2);

		QuestionBookmark.getInstance();

		initializeRoadSignHashMap();

		// Shuffle the questions
		Collections.shuffle(quizArrayList);

		quizQuestionNumberText = new StringBuffer();

		getListView().setCacheColorHint(0);

		showIndividualFlashCard();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflator = getMenuInflater();
		inflator.inflate(R.menu.flashcards_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.flashcards_bookmark_menu_item) {
			CheckBox checkbox = (CheckBox) findViewById(R.id.flashcards_checkbox);
			checkbox.setChecked(true);
			checkbox.setVisibility(View.VISIBLE);

		}
		return true;
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
		roadSignMap.put("w30", R.drawable.w30);
		roadSignMap.put("w31", R.drawable.w31);
	}

	private void showIndividualFlashCard() {

		currentQuestionIndex = 0;

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
		quizQuestionNumberText.append(currentQuestionIndex + 1);
		quizQuestionNumberText.append(" of ");
		quizQuestionNumberText.append(QuizCache
				.getQuizTotalNumberofQuestion());

		System.arraycopy(singleQandA.getAnswers(), 0, answerData, 0, 4);

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
	}

	private void manageCheckBox(boolean checkedStatus,
			int checkBoxVivibility) {
		CheckBox checkbox = (CheckBox) findViewById(R.id.flashcards_checkbox);
		checkbox.setChecked(checkedStatus);
		checkbox.setVisibility(checkBoxVivibility);
	}

	public void showNextQuestion(View view) {

		manageCheckBox(false, View.GONE);

		// first increment the currentQuestionIndex so that we advance
		// to the next question
		currentQuestionIndex++;

		// If all the flash cards have been read, the go back to the
		// first flash card
		if (currentQuestionIndex > QuizCache
				.getQuizTotalNumberofQuestion() - 1) {

			showIndividualFlashCard();

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

		// Setup quiz question number text
		quizQuestionNumberText.setLength(0);
		quizQuestionNumberText.append(currentQuestionIndex + 1);
		quizQuestionNumberText.append(" of ");
		quizQuestionNumberText.append(QuizCache
				.getQuizTotalNumberofQuestion());

		TextView question = (TextView) this
				.findViewById(R.id.flashcards_questionText);
		question.setText(singleQandA.getQuestionText());

		System.arraycopy(singleQandA.getAnswers(), 0, answerData, 0, 4);

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
		finish(); // finish the activity
	}

	public void shuffleQuiz(View view) {
		currentQuestionIndex = -1;
		QuizCache.getInstance().shuffleQuizCache();
		showNextQuestion(view);
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
		tv.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.flashcard_peek_stroke));

		TextView tv2 = (TextView) child
				.findViewById(R.id.answerListItemTV2);
		tv2.setTextColor(getResources().getColor(R.color.teal));

		TextView peekBar = (TextView) this
				.findViewById(R.id.flashCards_peekBar);

		peekBar.setBackgroundColor(Color.GRAY);
		peekBar.setText(ansLetter);
		peekBar.setTextColor(getResources().getColor(R.color.white));
		peekBar.setGravity(Gravity.CENTER);
	}

	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:
				touchX = event.getX();
				break;

			case MotionEvent.ACTION_UP:
				if (event.getX() < touchX)
					showNextQuestion(null);
				touchX = 0;
				break;
		}

		return true;
	}
}
