package com.jatayu;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MinneknowTActivity extends Activity {

	private boolean enable_log = false;
	private static final String TAG = "QuizHomeActivity:";
	private QuizCache quizcache;

	// temporary variable to store one single QandA set
	private String tempQuestionId = "";
	private String tempQuestionText = "";
	private String tempCorrectAnswer = "";
	private String[] tempAnswers = new String[4];

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.apphomelayout);
	}

	public void showQuizActivity(View view) {
		new SetupQuizTask(MinneknowTActivity.this).execute();
	}

	private class SetupQuizTask extends AsyncTask<Void, Void, Boolean> {

		private ProgressDialog progressDialog;
		private Context context;
		Boolean parserResult = false;
		private MinneknowTActivity activity;

		public SetupQuizTask(MinneknowTActivity minneknowTActivity) {
			this.activity = minneknowTActivity;
			this.context = activity;
			progressDialog = new ProgressDialog(context);

			quizcache = QuizCache.getInstance();
		}

		@Override
		protected void onPreExecute() {
			this.progressDialog.setMessage("Loading Quiz");
			this.progressDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			try {
				setupQuiz();
				parserResult = true;
			} catch (XmlPullParserException e) {
				parserResult = false;
				e.printStackTrace();
			} catch (IOException e) {
				parserResult = false;
				e.printStackTrace();
			}

			return parserResult;
		}

		@Override
		protected void onPostExecute(Boolean parserResult) {

			if (progressDialog.isShowing())
				progressDialog.dismiss();

			if (parserResult) { // If quiz was successfully parsed
				Intent intent = new Intent(MinneknowTActivity.this,
						QuizActivity.class);
				startActivity(intent);

			}
		}

	}

	/*
	 * In our project, we created a new directory named 'xml' under '/res'. Our
	 * entire Quiz is stored in custom XML format in '/res/xml/quiz.xml'. This
	 * method parses quiz.xml and uses Quiz and QandA classes to store the Quiz.
	 */
	private void setupQuiz() throws XmlPullParserException, IOException {

		int answerIndex = 0;

		XmlPullParser parser = getResources().getXml(R.xml.quiz);

		int eventType = parser.getEventType();

		if (eventType == XmlPullParser.START_DOCUMENT) {
			// when the current eventType is 'START_DOCUMENT' the
			// 'parser.getName()' returns 'null'
			if (enable_log) {
				Log.i(TAG, "Type of current event is: START_DOCUMENT");
				Log.i(TAG, "name of current element: " + parser.getName());
			}
		}

		// parser pointing to line 1
		if (enable_log) {
			Log.i(TAG, "parser.next(): " + parser.next());
			Log.i(TAG, "name of current element: " + parser.getName());

			// parser points to line 2 of
			Log.i(TAG, "parser.next(): " + parser.next());
			Log.i(TAG, "name of current element: " + parser.getName()); // returns
			// name
			// 'quiz'

		}
		// // parser points to line 3 of 'quiz.xml'
		// Log.i(TAG, "parser.next(): " + parser.next());
		// Log.i(TAG, "name of current element: " + parser.getName()); //
		// returns
		// // name
		// // 'question'

		eventType = parser.getEventType(); // this returns the type of current

		while (eventType != XmlPullParser.END_DOCUMENT) {

			// parser points to <question>
			if (eventType == XmlPullParser.START_TAG
					&& (parser.getName().equalsIgnoreCase("question"))) {

				if (enable_log)
					Log.i(TAG, " New question! Begin parsing ...");

				// when the parser finds a new <question>, clear the temporary
				// QandA variables
				// clearTemporaryQandA();

			}

			// parser points to <questionId>
			else if (eventType == XmlPullParser.START_TAG
					&& (parser.getName().equalsIgnoreCase("questionId"))) {

				if (enable_log)
					Log.i(TAG, "question Id: " + parser.nextText()); // returns
				// quiz
				// number
				// (1, 2, 3
				// so on)
				else
					tempQuestionId = parser.nextText();
			}

			// parser points to <questionText>
			else if (eventType == XmlPullParser.START_TAG
					&& (parser.getName().equalsIgnoreCase("questionText"))) {
				if (enable_log)
					Log.i(TAG, "question text: " + parser.nextText()); // returns
				// question
				// text
				tempQuestionText = parser.nextText();
			}

			// parser points to <correctAnswer>
			else if (eventType == XmlPullParser.START_TAG
					&& (parser.getName().equalsIgnoreCase("correctAnswer"))) {
				if (enable_log)
					Log.i(TAG, "corrected answer " + parser.nextText());
				else
					tempCorrectAnswer = parser.nextText();
			}

			// parser points to <answerText>
			else if (eventType == XmlPullParser.START_TAG
					&& (parser.getName().equalsIgnoreCase("answerText"))) {

				if (enable_log)
					Log.i(TAG, "answer " + parser.nextText());

				else {

					tempAnswers[answerIndex] = parser.nextText();
					answerIndex++;
					if (answerIndex > 3) // reset answer index to zero
						answerIndex = 0;

				}
			}

			// parser points to </question>
			else if (eventType == XmlPullParser.END_TAG
					&& (parser.getName().equalsIgnoreCase("question"))) {

				if (enable_log)
					Log.i(TAG, " Yay! One question parsed");
				else {
					Log.i(TAG, "saved QId: " + tempQuestionId);
					Log.i(TAG, "saved QText" + tempQuestionText);
					Log.i(TAG, "saved correctA: " + tempCorrectAnswer);
					Log.i(TAG, "saved A0: " + tempAnswers[0]);
					Log.i(TAG, "saved A1: " + tempAnswers[1]);
					Log.i(TAG, "saved A2: " + tempAnswers[2]);
					Log.i(TAG, "saved A3: " + tempAnswers[3]);

					// Create an instance of single QandA to store one QandA set
					QandA qa = new QandA();
					qa.setQuestionNumber(tempQuestionId);
					qa.setQuestionText(tempQuestionText);
					qa.setCorrectAnswer(tempCorrectAnswer);
					qa.setAnswers(tempAnswers);

					// add single QandA set into the quiz (which is an
					// ArrayList)
					quizcache.addQandA(qa);

				}

			}

			eventType = parser.next();

		}

	}

}