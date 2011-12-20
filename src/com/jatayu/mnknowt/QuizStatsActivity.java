package com.jatayu.mnknowt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * This class displays Quiz Statistics.
 * 
 * @author Neeraj Sharma
 * 
 */
public class QuizStatsActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quiz_stats_layout_v2);

		new QuizReaderTask(this).execute();
	}

	public void showAppHomePage(View view) {
		finish(); // invoke finish() will close this activity
	}

	private class QuizReaderTask extends AsyncTask<Void, Void, Boolean> {

		private Context		context;
		private ProgressDialog	progressDialog;
		private StringBuffer	buffer;
		private Cursor		cursor_totalQuestionsAnswered_table;
		private Cursor		cursor_correctIncorrect_table;
		private Cursor		cursor_score_table;
		private QuizDBManager	qdbm;

		public QuizReaderTask(QuizStatsActivity context) {
			this.context = context;
			progressDialog = new ProgressDialog(context);
			buffer = new StringBuffer();
		}

		protected void onPreExecute() {
			progressDialog.setMessage("Reading Quiz Stats...");
			progressDialog.show();
		}

		protected Boolean doInBackground(Void... arg0) {

			readFromDataBase();

			return true;
		}

		private void readFromDataBase() {
			Log.d("QuizStatsActivity QuizReaderTask",
					" >>> Reading Quiz Stats information from database");

			qdbm = new QuizDBManager(context);

			// 1. Get Cursor from totalquestionsanswered TABLE
			cursor_totalQuestionsAnswered_table = qdbm
					.getTotalQuestionsAnswered();

			// 2. get cursor from correctincorrect TABLE
			cursor_correctIncorrect_table = qdbm
					.getTotalCorrectIncorrectStats();

			// 3. Get Cursor from 'score' TABLE
			cursor_score_table = qdbm.getQuizScore();

		}

		protected void onPostExecute(Boolean result) {

			Log.d("QuizStatsActivity",
					" >> QuizReaderTask onPostExecute");

			if (progressDialog.isShowing())
				progressDialog.dismiss();

			// ------------------------------------------
			// Set value for total questions answered text view
			cursor_totalQuestionsAnswered_table.moveToFirst();

			int total_questions_answered = cursor_totalQuestionsAnswered_table
					.getInt(1);

			TextView totalQuestionsAnswered_Value_TV = (TextView) findViewById(R.id.quizStats_totalQuestionsAnsweredValue);

			TableRow graphBar_IV = (TableRow) findViewById(R.id.quizStats_graphBar_TableRow);
			ImageView quizStats_graph_greenBar = (ImageView) findViewById(R.id.quizStats_graph_greenBar);
			ImageView quizStats_graph_redBar = (ImageView) findViewById(R.id.quizStats_graph_redBar);

			TextView correctAns_Value_TV = (TextView) findViewById(R.id.quizStats_total_correct_answers_value);
			TextView incorrectAnswers_value_TV = (TextView) findViewById(R.id.quizStats_total_incorrect_answers_value);

			TextView bestScoreTV = (TextView) findViewById(R.id.quizStats_best_score_value);
			TextView worstScoreTV = (TextView) findViewById(R.id.quizStats_worst_score_value);

			// If the user has not yet taken any quiz
			if (!(total_questions_answered > 0)) {

				totalQuestionsAnswered_Value_TV
						.setText("Quiz Not Yet taken!");

				graphBar_IV.setVisibility(View.GONE);

				correctAns_Value_TV.setText("--");
				incorrectAnswers_value_TV.setText("--");

				bestScoreTV.setText("--/--");
				worstScoreTV.setText("--/--");

			} else {

				buffer.append(total_questions_answered);
				totalQuestionsAnswered_Value_TV.setText(buffer
						.toString());
				buffer.setLength(0);

				// -------------------------------------------
				// Set value for correct and incorrect text view

				// always remember to move the cursor so that it
				// points to the first row
				cursor_correctIncorrect_table.moveToFirst();

				// column index 1 is correct answer values
				int total_correct_answers = cursor_correctIncorrect_table
						.getInt(1);
				buffer.append(total_correct_answers);

				correctAns_Value_TV.setText(buffer.toString());

				buffer.setLength(0);

				// column index 2 is incorrect answer values
				int total_incorrect_answers = cursor_correctIncorrect_table
						.getInt(2);
				buffer.append(total_incorrect_answers);
				incorrectAnswers_value_TV.setText(buffer
						.toString());

				buffer.setLength(0);

				// ------------------------------------------
				cursor_score_table.moveToFirst();

				// get best score value
				int best_score = cursor_score_table.getInt(1);

				buffer.append(best_score + "/10");

				bestScoreTV.setText(buffer.toString());

				buffer.setLength(0);

				// get worst score value
				int worst_score = cursor_score_table.getInt(2);

				buffer.append(worst_score + "/10");

				worstScoreTV.setText(buffer.toString());

				buffer.setLength(0);

				// Compute the height for Green bar height and
				// Red bar
				// height for correct and incorrect answers

				// Now compute what percentage of default bar
				// height
				// will be green bar

				int correct_ans_percentage = 0;
				int incorrect_ans_percentage = 0;
				if (total_questions_answered > 0) {
					correct_ans_percentage = (100 * total_correct_answers)
							/ total_questions_answered;

					incorrect_ans_percentage = (100 * total_incorrect_answers)
							/ total_questions_answered;

				}

				Bitmap bitmapGreenBar = Bitmap.createBitmap(40,
						correct_ans_percentage,
						Config.ARGB_8888);
				Canvas c = new Canvas(bitmapGreenBar);
				c.drawColor(0xFF0B610B);

				quizStats_graph_greenBar
						.setImageBitmap(bitmapGreenBar);

				Bitmap bitmapRedBar = Bitmap.createBitmap(40,
						incorrect_ans_percentage,
						Config.ARGB_8888);
				Canvas c2 = new Canvas(bitmapRedBar);
				c2.drawColor(Color.RED);

				quizStats_graph_redBar
						.setImageBitmap(bitmapRedBar);

			}
			// close all the Cursors

			cursor_score_table.close();
			cursor_correctIncorrect_table.close();
			cursor_totalQuestionsAnswered_table.close();

			qdbm.closeDB();
		}
	}

}
