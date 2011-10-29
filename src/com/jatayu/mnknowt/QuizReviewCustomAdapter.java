package com.jatayu.mnknowt;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * Custom adapter used by QuizReviewActvity
 */
public class QuizReviewCustomAdapter extends BaseAdapter {

	private Activity	context;
	private int[]		correctAnswer_array;
	private int[]		question_number_array;
	private int		questionNumber	= 0;

	public QuizReviewCustomAdapter(Activity context) {
		this.context = context;

		correctAnswer_array = new int[CommonProps.TOTAL_QUIZ_QUESTIONS];
		question_number_array = new int[CommonProps.TOTAL_QUIZ_QUESTIONS];
		System.arraycopy(OngoingQuizTracker.getInstance()
				.getCorrect_answer_tracker(), 0,
				correctAnswer_array, 0,
				question_number_array.length);
	}

	public int getCount() {
		return question_number_array.length;
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		LayoutInflater inflator = context.getLayoutInflater();
		questionNumber = position + 1;

		if (convertView == null) {
			convertView = inflator.inflate(
					R.layout.quiz_review_row_list_item,
					null);

			holder = new ViewHolder();

			holder.iconView = (ImageView) convertView
					.findViewById(R.id.reviewActivityListItem_ImageView);

			holder.questionTextView = (TextView) convertView
					.findViewById(R.id.reviewActivityListItemTV1);

			// Set icon for correct answer
			if (correctAnswer_array[position] == 1) {
				holder.iconView.setImageResource(R.drawable.correct_icon);
			} else {// Set icon for incorrect answer choice
				holder.iconView.setImageResource(R.drawable.incorrect_icon);
			}

			holder.answerTextView = (TextView) convertView
					.findViewById(R.id.reviewActivityListItemTV2);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.questionTextView.setText("Question " + questionNumber
				+ ": "
				+ QuizQuestionsList.QUESTION_TEXT[position]);

		holder.answerTextView
				.setText(QuizCorrectAnswerList.CORRECT_ANSWER_TEXT[position]);

		return convertView;
	}

	private class ViewHolder {
		ImageView	iconView;		// displays correct or
							// incorrect icon

		TextView	questionTextView;	// displaysthe question
							// text

		TextView	answerTextView;	// displays correct
							// answer text
	}

}
