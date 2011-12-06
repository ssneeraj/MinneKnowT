package com.jatayu.mnknowt;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Main activity for the application. This is called when the app is launched
 * 
 * @author sharman
 * 
 */
public class MNknowTActivity extends Activity {

	private static final String	TAG			= "MinneknowTactivity:";
	private QuizCache		quizcache;

	// temporary variable to store one single QandA set
	private String			tempQuestionId		= "";
	private String			tempQuestionText	= "";
	private String			tempCorrectAnswer	= "";
	private String[]		tempAnswers		= new String[4];
	private String			tempImageFileName	= "";

	private static final int	FLASH_CARDS_ACTIVITY	= 0;
	private static final int	QUIZ_ACTIVITY		= 1;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.apphome_layout_v2);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menu_inflator = getMenuInflater();
		menu_inflator.inflate(R.menu.app_home_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.resetMenuItem) {
			Log.d(TAG, "Option Menu item clicked to clear DB");
			QuizDBManager qdm = new QuizDBManager(
					MNknowTActivity.this);
			qdm.resetQuizDatabase();
		} else if (item.getItemId() == R.id.aboutMenuItem) {
			// set up dialog
			final Dialog dialog = new Dialog(MNknowTActivity.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

			dialog.setContentView(R.layout.custom_dialog_layout);

			// set up image view
			ImageView img = (ImageView) dialog
					.findViewById(R.id.customDialogIcon);
			img.setImageResource(R.drawable.icon);

			// message TextView in Custom Dialog
			TextView text = (TextView) dialog
					.findViewById(R.id.customDialogMessage);
			text.setText("\nMNknowT will help you to prepare for Minnesota Driver's License Written Knowledge test.\n\n"
					+ CommonProps.COPYRIGHT_SYMBOL
					+ CommonProps.COMPANY_NAME
					+ CommonProps.APP_RELEASE_YEAR
					+ "\n\n"
					+ "Contact Us: mnknowt@gmail.com"
					+ "\n");

			// Close button
			Button closeButton = (Button) dialog
					.findViewById(R.id.customDialogButton);
			closeButton.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			// now that the dialog is set up, it's time to show it
			dialog.show();

		} else if (item.getItemId() == R.id.helpMenuItem) {
			final Dialog dialog = new Dialog(MNknowTActivity.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

			dialog.setContentView(R.layout.custom_dialog_layout);

			// set up image view
			ImageView img = (ImageView) dialog
					.findViewById(R.id.customDialogIcon);
			img.setImageResource(R.drawable.icon);

			// set up text
			TextView text = (TextView) dialog
					.findViewById(R.id.customDialogMessage);
			String message = "\nMNknowT app features\n\n"
					+ "\t\tQuiz is un-timed and comprises of 75 questions. You can ONLY review the quiz result once you have answered all the 75 questions.\n\n"
					+ "\t\tRoad Signs includes flash cards for popular road signs.\n\n"
					+ "\t\tStats stores history of all the previous quiz attempts. To reset the quiz history go to the app home page, then select the 'menu' button that displays menu options, and select 'Reset History'.\n\n";
			text.setText(message);

			// set up button
			Button closeButton = (Button) dialog
					.findViewById(R.id.customDialogButton);
			closeButton.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					dialog.dismiss();

				}
			});

			// now that the dialog is set up, it's time to show it
			dialog.show();
		}

		return true;
	}

	public void showFlashCardsActivity(View view) {

		// Initiate setup quiz task only if QuizCache is empty
		if (QuizCache.isQuizcacheEmpty()) {
			new SetupQuizTask(MNknowTActivity.this,
					FLASH_CARDS_ACTIVITY).execute();
		} else {
			// since QuizCache is not empty directly show the Quiz
			Intent intent = new Intent(MNknowTActivity.this,
					FlashCardsActivity.class);
			startActivity(intent);
		}
	}

	public void showQuizActivity(View view) {

		// Initiate setup quiz task only if QuizCache is empty
		if (QuizCache.isQuizcacheEmpty()) {
			new SetupQuizTask(MNknowTActivity.this, QUIZ_ACTIVITY)
					.execute();
		} else {
			// since QuizCache is not empty directly show the Quiz
			Intent intent = new Intent(MNknowTActivity.this,
					QuizActivity.class);
			startActivity(intent);
		}
	}

	private class SetupQuizTask extends AsyncTask<Void, Void, Boolean> {

		private ProgressDialog	progressDialog;
		private Context		context;
		Boolean			parserResult	= false;
		private MNknowTActivity	activity;
		private int		activityType;

		public SetupQuizTask(MNknowTActivity minneknowTActivity,
				int activityType) {
			this.activity = minneknowTActivity;
			this.context = activity;
			this.activityType = activityType;
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

				// set that the quiz cache is not empty now
				QuizCache.setQuizcacheEmpty(false);

				if (this.activityType == FLASH_CARDS_ACTIVITY) {
					Intent intent = new Intent(
							MNknowTActivity.this,
							FlashCardsActivity.class);
					startActivity(intent);
				} else if (this.activityType == QUIZ_ACTIVITY) {
					Intent intent = new Intent(
							MNknowTActivity.this,
							QuizActivity.class);
					startActivity(intent);
				}
			}
		}
	}

	/**
	 * In our project, we created a new directory named 'xml' under '/res'.
	 * Our entire Quiz is stored in custom XML format in
	 * '/res/xml/quiz.xml'. This method parses quiz.xml and uses Quiz and
	 * QandA classes to store the Quiz.
	 * 
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void setupQuiz() throws XmlPullParserException, IOException {

		int answerIndex = 0;

		XmlPullParser parser = getResources().getXml(R.xml.quiz);

		int eventType = parser.getEventType();

		if (eventType == XmlPullParser.START_DOCUMENT) {
			// when the current eventType is 'START_DOCUMENT' the
			// 'parser.getName()' returns 'null'
			if (CommonProps.LOG_ENABLED) {
				Log.i(TAG,
						"Type of current event is: START_DOCUMENT");
				Log.i(TAG,
						"name of current element: "
								+ parser.getName());
			}
		}

		// parser pointing to line 1
		if (CommonProps.LOG_ENABLED) {
			Log.i(TAG, "parser.next(): " + parser.next());
			Log.i(TAG,
					"name of current element: "
							+ parser.getName());

			// parser points to line 2 of
			Log.i(TAG, "parser.next(): " + parser.next());
			Log.i(TAG,
					"name of current element: "
							+ parser.getName()); // returns
										// name
										// 'quiz'

		}
		// // parser points to line 3 of 'quiz.xml'
		// Log.i(TAG, "parser.next(): " + parser.next());
		// Log.i(TAG, "name of current element: " + parser.getName());
		// //
		// returns
		// // name
		// // 'question'

		eventType = parser.getEventType(); // this returns the type of
							// current

		while (eventType != XmlPullParser.END_DOCUMENT) {

			// parser points to <question>
			if (eventType == XmlPullParser.START_TAG
					&& (parser.getName()
							.equalsIgnoreCase("question"))) {

				if (CommonProps.LOG_ENABLED)
					Log.i(TAG,
							" New question! Begin parsing ...");

			}

			// parser points to <questionId>
			else if (eventType == XmlPullParser.START_TAG
					&& (parser.getName()
							.equalsIgnoreCase("questionId"))) {

				if (CommonProps.LOG_ENABLED)
					Log.i(TAG,
							"question Id: "
									+ parser.nextText()); // returns
												// quiz
												// number
												// (1,
												// 2,
												// 3
												// so
												// on)
				else
					tempQuestionId = parser.nextText();
			}

			// parser points to <questionText>
			else if (eventType == XmlPullParser.START_TAG
					&& (parser.getName()
							.equalsIgnoreCase("questionText"))) {
				if (CommonProps.LOG_ENABLED)
					Log.i(TAG,
							"question text: "
									+ parser.nextText()); // returns
				// question
				// text
				tempQuestionText = parser.nextText();
			}

			// parser points to <correctAnswer>
			else if (eventType == XmlPullParser.START_TAG
					&& (parser.getName()
							.equalsIgnoreCase("correctAnswer"))) {
				if (CommonProps.LOG_ENABLED)
					Log.i(TAG,
							"corrected answer "
									+ parser.nextText());
				else
					tempCorrectAnswer = parser.nextText();
			}

			// parser points to <answerText>
			else if (eventType == XmlPullParser.START_TAG
					&& (parser.getName()
							.equalsIgnoreCase("answerText"))) {

				if (CommonProps.LOG_ENABLED)
					Log.i(TAG,
							"answer "
									+ parser.nextText());

				else {

					tempAnswers[answerIndex] = parser
							.nextText();
					answerIndex++;
					if (answerIndex > 3) // reset answer
								// index to zero
						answerIndex = 0;

				}
			}
			// parser points to <imageFileName>
			else if (eventType == XmlPullParser.START_TAG
					&& (parser.getName()
							.equalsIgnoreCase("imageFileName"))) {
				tempImageFileName = parser.nextText();

			}

			// parser points to </question>
			else if (eventType == XmlPullParser.END_TAG
					&& (parser.getName()
							.equalsIgnoreCase("question"))) {

				if (CommonProps.LOG_ENABLED)
					Log.i(TAG, " Yay! One question parsed");
				else {
					Log.i(TAG, "saved QId: "
							+ tempQuestionId);
					Log.i(TAG, "saved QText"
							+ tempQuestionText);
					Log.i(TAG, "saved correctA: "
							+ tempCorrectAnswer);
					Log.i(TAG, "saved A0: "
							+ tempAnswers[0]);
					Log.i(TAG, "saved A1: "
							+ tempAnswers[1]);
					Log.i(TAG, "saved A2: "
							+ tempAnswers[2]);
					Log.i(TAG, "saved A3: "
							+ tempAnswers[3]);
					Log.i(TAG, "saved ImageFileName: "
							+ tempImageFileName);

					// Create an instance of single QandA to
					// store one QandA set
					QandA qa = new QandA();
					qa.setQuestionNumber(tempQuestionId);
					qa.setQuestionText(tempQuestionText);
					qa.setCorrectAnswer(tempCorrectAnswer);
					qa.setAnswers(tempAnswers);
					qa.setImageFileName(tempImageFileName);

					// add single QandA set into the quiz
					// (which is an
					// ArrayList)
					quizcache.addQandA(qa);

				}

			}

			eventType = parser.next();

		}

	}

	public void showQuizStatsActivity(View view) {
		Log.d(TAG, " >>> In showQuizStatsActivity(View view) method");
		new Thread(new Runnable() {
			public void run() {

				Intent intent = new Intent(
						MNknowTActivity.this,
						QuizStatsActivity.class);
				startActivity(intent);
			}
		}).start();

	}

}