package com.jatayu.mnknowt;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AnswerCustomAdapter extends BaseAdapter {

	private Activity	context;
	private String[]	answerChoiceText	= new String[4];

	public void setAnswerChoiceText(String[] answerData) {
		System.arraycopy(answerData, 0, answerChoiceText, 0, 4);
	}

	public AnswerCustomAdapter(Activity context, String[] answerData) {
		this.context = context;

		System.arraycopy(answerData, 0, answerChoiceText, 0, 4);
	}

	public int getCount() {
		return 4;
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		String answerChoiceLetter = null;

		ViewHolder holder;
		LayoutInflater inflator = context.getLayoutInflater();

		if (convertView == null) {
			convertView = inflator.inflate(R.layout.list_item_v2,
					null);

			holder = new ViewHolder();

			holder.answerChoiceLabel = (TextView) convertView
					.findViewById(R.id.answerListItemTV1);

			holder.answerChoiceText = (TextView) convertView
					.findViewById(R.id.answerListItemTV2);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		switch (position) {
			case 0:
				answerChoiceLetter = "A.";
				break;

			case 1:
				answerChoiceLetter = "B.";
				break;

			case 2:
				answerChoiceLetter = "C.";
				break;

			case 3:
				answerChoiceLetter = "D.";
				break;
		}

		if (answerChoiceText[position].length() > 1) {
			holder.answerChoiceLabel.setVisibility(View.VISIBLE);
			holder.answerChoiceText.setVisibility(View.VISIBLE);

			holder.answerChoiceLabel.setText(answerChoiceLetter);

			holder.answerChoiceText
					.setText(answerChoiceText[position]);
		} else {
			holder.answerChoiceLabel.setVisibility(View.GONE);
			holder.answerChoiceText.setVisibility(View.GONE);
		}

		return convertView;
	}

	private class ViewHolder {
		TextView	answerChoiceLabel;

		TextView	answerChoiceText;
	}

}
