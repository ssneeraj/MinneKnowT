package com.jatayu.mnknowt;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Displays Road Sign bitmap with a short description below the bitmap. Also
 * displays road sign number at the top (example 1 of 8). Browing through the
 * road signs can be done using forward and back arror button position at the
 * top right and top left corner respectively.
 * 
 * @author sharman
 * 
 */
public class RoadSignActivity extends Activity {

	private ImageView	roadSignIV;			// displays Road
								// Sign
								// bitmap

	private TextView	roadSignNumberTV;		// displays road
								// sign
								// number for
								// example 1 of
								// 30

	private TextView	roadSignDescription;		// displays a
								// short
								// description
								// for the
								// corresponding
								// road
								// sign

	private int[]		road_sign_bitmap_ids	= new int[] {
			R.drawable.w1, R.drawable.w2, R.drawable.w3,
			R.drawable.w4, R.drawable.w5, R.drawable.w6,
			R.drawable.w7, R.drawable.w8, R.drawable.w9,
			R.drawable.w10, R.drawable.w11, R.drawable.w12,
			R.drawable.w13, R.drawable.w14, R.drawable.w15,
			R.drawable.w16, R.drawable.w17, R.drawable.w18,
			R.drawable.w19, R.drawable.w20, R.drawable.w21,
			R.drawable.w22, R.drawable.w23, R.drawable.w24,
			R.drawable.w25			};

	private int		index			= 0;
	private StringBuffer	road_sign_number_buff;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		road_sign_number_buff = new StringBuffer();
		road_sign_number_buff.append(index + 1);
		road_sign_number_buff.append(" of ");
		road_sign_number_buff.append(road_sign_bitmap_ids.length);

		setContentView(R.layout.road_sign_layout);

		roadSignIV = (ImageView) this.findViewById(R.id.roadsignIV);
		Resources res = this.getResources();
		Drawable drawable = res
				.getDrawable(road_sign_bitmap_ids[index]);
		roadSignIV.setImageDrawable(drawable);

		roadSignNumberTV = (TextView) this
				.findViewById(R.id.roadSignNumber);
		roadSignNumberTV.setText(road_sign_number_buff);

		roadSignDescription = (TextView) this
				.findViewById(R.id.roadsignDescTextView);
		roadSignDescription
				.setText(RoadSignInfoText.ROAD_SIGN_INFO_TEXT[index]);

	}

	/**
	 * Invoked when forward button is pressed. This increments the index by
	 * 1.
	 * 
	 * @param view
	 */
	public void showNext(View view) {
		index++;

		if (index >= road_sign_bitmap_ids.length)
			index = 0;

		invalidateActivity();
	}

	/**
	 * Redraw the RoadSignActivity
	 */
	private void invalidateActivity() {
		roadSignIV.setImageDrawable(getResources().getDrawable(
				road_sign_bitmap_ids[index]));

		road_sign_number_buff.setLength(0);
		road_sign_number_buff.append(index + 1);
		road_sign_number_buff.append(" of ");
		road_sign_number_buff.append(road_sign_bitmap_ids.length);
		roadSignNumberTV.setText(road_sign_number_buff);

		roadSignDescription
				.setText(RoadSignInfoText.ROAD_SIGN_INFO_TEXT[index]);

		roadSignIV.invalidate();
	}

	/**
	 * Invoked when back image button is pressed. The index is decremented
	 * by 1
	 * 
	 * @param view
	 */
	public void showPrevious(View view) {
		index--;

		if (index < 0)
			index = 0;

		invalidateActivity();
	}
}
