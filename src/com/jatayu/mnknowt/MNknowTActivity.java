package com.jatayu.mnknowt;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Main activity for the application. This is called when the application is
 * launched
 * 
 * @author Neeraj Sharma
 * 
 */
public class MNknowTActivity extends Activity {

	private static final String	TAG			= "MinneknowTactivity:";
	private QuizCache		quizcache;

	// temporary variables to store one single QandA set
	private String			tempQuestionId		= "";
	private String			tempQuestionText	= "";
	private String			tempCorrectAnswer	= "";
	private String[]		tempAnswers		= new String[4];
	private String			tempImageFileName	= "";

	private static final int	FLASH_CARDS_ACTIVITY	= 0;
	private static final int	QUIZ_ACTIVITY		= 1;
	private static final String	PREFS_NAME		= "mnknowtPrefs";
	private static int		appUsageCount;
	private boolean			appReviewDone;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// code for getting shared preferences
		SharedPreferences prefs = getSharedPreferences(PREFS_NAME,
				Activity.MODE_PRIVATE);
		appUsageCount = prefs.getInt("appUsageCount", 0);
		appReviewDone = prefs.getBoolean("appReviewStatus", false);
		if (appUsageCount % 5 == 0 && !appReviewDone) {
			showAppFeedbackDialog();
		}

		setContentView(R.layout.apphome_layout_v2);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menu_inflator = getMenuInflater();
		menu_inflator.inflate(R.menu.app_home_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.aboutMenuItem) {
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

		}

		return true;
	}

	public void showFlashCardsActivity(View view) {

		// Initiate setup quiz task only if QuizCache is empty
		if (QuizCache.isQuizcacheEmpty()) {
			new SetupQuizTask(MNknowTActivity.this,
					FLASH_CARDS_ACTIVITY).execute();
		} else {
			showFlashCardsChoicesDialog();
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
			this.progressDialog.setMessage("Loading...");
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
					showFlashCardsChoicesDialog();

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
					// Log.i(TAG, "saved QId: "
					// + tempQuestionId);
					// Log.i(TAG, "saved QText"
					// + tempQuestionText);
					// Log.i(TAG, "saved correctA: "
					// + tempCorrectAnswer);
					// Log.i(TAG, "saved A0: "
					// + tempAnswers[0]);
					// Log.i(TAG, "saved A1: "
					// + tempAnswers[1]);
					// Log.i(TAG, "saved A2: "
					// + tempAnswers[2]);
					// Log.i(TAG, "saved A3: "
					// + tempAnswers[3]);
					// Log.i(TAG, "saved ImageFileName: "
					// + tempImageFileName);

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
		if (CommonProps.LOG_ENABLED)
			Log.d(TAG,
					" >>> In showQuizStatsActivity(View view) method");
		new Thread(new Runnable() {
			public void run() {

				Intent intent = new Intent(
						MNknowTActivity.this,
						QuizStatsActivity.class);
				startActivity(intent);
			}
		}).start();

	}

	public void showQBookmarkTest() {

	}

	private ListView	lv;
	private Dialog		dialog;

	public void showFlashCardsChoicesDialog() {

		// Set up dialog
		dialog = new Dialog(this);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select category");

		Context mContext = getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(
				R.layout.flashcards_choice_dialog_layout,
				(ViewGroup) findViewById(R.id.layout_root));

		lv = (ListView) layout.findViewById(R.id.listview);

		String[] stringArray = new String[] { "All", "Accident",
				"Children", "Commercial vehicle",
				"Construction & Workzone", "Crosswalk",
				"Driving under influence", "Emergency vehicle",
				"Headlights", "Intersection", "Lane markings",
				"Merge & Exit", "Parking", "Passing",
				"Pedestrian & Byclist", "Rail road crossing",
				"school bus", "Seat belt and Air bags",
				"Severe weather", "Signaling & Lane change",
				"Speed", "Traffic Signal", "Turns", "Other",
				"Road Signs" };

		ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_1,
				android.R.id.text1, stringArray);

		lv.setAdapter(modeAdapter);
		lv.setCacheColorHint(0);
		builder.setView(layout);
		dialog = builder.create();

		dialog.show();

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {

				Log.i("MNKnowT setOnItemClickListener get item at position",
						new Integer(position)
								.toString());

				Intent intent = new Intent(
						MNknowTActivity.this,
						FlashCardsActivity.class);

				Bundle bundle = new Bundle();

				bundle.putIntArray("questionRange",
						getQuestionRange(position));
				intent.putExtras(bundle);
				startActivity(intent);

				dismissDialog();

			}
		});
	}

	public void dismissDialog() {
		dialog.dismiss();
	}

	public int[] getQuestionRange(int position) {

		int[] questionRange = new int[2];

		switch (position) {

		// All
			case 0:
				questionRange[0] = 0;
				questionRange[1] = 162;
				break;

			// Accidents
			case 1:
				questionRange[0] = 0;
				questionRange[1] = 1;
				break;

			// Children
			case 2:
				questionRange[0] = 2;
				questionRange[1] = 5;
				break;

			// Commercial vehicle
			case 3:
				questionRange[0] = 6;
				questionRange[1] = 9;
				break;

			// Construction & Work zone
			case 4:
				questionRange[0] = 10;
				questionRange[1] = 13;
				break;

			// Crosswalk
			case 5:
				questionRange[0] = 14;
				questionRange[1] = 15;
				break;

			// Driving under influence
			case 6:
				questionRange[0] = 16;
				questionRange[1] = 21;
				break;

			// Emergency vehicle
			case 7:
				questionRange[0] = 22;
				questionRange[1] = 23;
				break;

			// Headlights
			case 8:
				questionRange[0] = 24;
				questionRange[1] = 29;
				break;

			// Intersection
			case 9:
				questionRange[0] = 30;
				questionRange[1] = 38;
				break;

			// Lane markings
			case 10:
				questionRange[0] = 39;
				questionRange[1] = 43;
				break;

			// Merge and Exit
			case 11:
				questionRange[0] = 44;
				questionRange[1] = 47;
				break;

			// Parking
			case 12:
				questionRange[0] = 48;
				questionRange[1] = 57;
				break;

			// Passing
			case 13:
				questionRange[0] = 58;
				questionRange[1] = 63;
				break;

			// Pedestrian & Bicylist
			case 14:
				questionRange[0] = 64;
				questionRange[1] = 66;
				break;

			// Rail road crossing
			case 15:
				questionRange[0] = 67;
				questionRange[1] = 70;
				break;

			// School bus
			case 16:
				questionRange[0] = 71;
				questionRange[1] = 72;
				break;

			// Seat belts & Airbags
			case 17:
				questionRange[0] = 73;
				questionRange[1] = 79;
				break;

			// Severe Weather
			case 18:
				questionRange[0] = 80;
				questionRange[1] = 84;
				break;

			// Signaling & Lane change
			case 19:
				questionRange[0] = 85;
				questionRange[1] = 88;
				break;

			// Speed
			case 20:
				questionRange[0] = 89;
				questionRange[1] = 94;
				break;

			// Traffic light
			case 21:
				questionRange[0] = 95;
				questionRange[1] = 102;
				break;

			// Turns
			case 22:
				questionRange[0] = 103;
				questionRange[1] = 107;
				break;

			// Other
			case 23:
				questionRange[0] = 108;
				questionRange[1] = 140;
				break;

			// Road Signs
			case 24:
				questionRange[0] = 141;
				questionRange[1] = 162;
				break;

		}

		return questionRange;
	}

	public void showAppFeedbackDialog() {
		// set up dialog
		final Dialog dialog2 = new Dialog(MNknowTActivity.this);
		dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog2.setContentView(R.layout.app_feedback);

		Button later = (Button) dialog2.findViewById(R.id.laterButton);
		later.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				dialog2.dismiss();
			}
		});

		Button feedback = (Button) dialog2
				.findViewById(R.id.feedbackButton);
		feedback.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				appReviewDone = true;

				setSharedPrefs();

				Intent feedbackIntent = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("market://details?id="
								+ "com.jatayu.mnknowt"));
				startActivity(feedbackIntent);
				dialog2.dismiss();
			}
		});

		Button doNotShowButton = (Button) dialog2
				.findViewById(R.id.doNotShowButton);
		doNotShowButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				appReviewDone = true;

				setSharedPrefs();
			}
		});

		dialog2.show();

	}

	@Override
	protected void onStop() {
		super.onStop();

		setSharedPrefs();
	}

	private void setSharedPrefs() {
		// code for setting shared preferences
		SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("appUsageCount", appUsageCount + 1);
		editor.putBoolean("appReviewStatus", appReviewDone);
		editor.commit();
	}
}