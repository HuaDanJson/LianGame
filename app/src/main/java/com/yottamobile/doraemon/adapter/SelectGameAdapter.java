package com.yottamobile.doraemon.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yottamobile.doraemon.PikachuActivity;
import com.yottamobile.doraemon.R;

public class SelectGameAdapter extends BaseAdapter {
	private Context mContext;

	public Integer[] mThumbIds = { R.drawable.game_mission, R.drawable.game_top, R.drawable.more_game };

	public SelectGameAdapter(Context c) {
		mContext = c;

	}

	public int getCount() {
		return mThumbIds.length;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		
		View vi = convertView;
//		if (convertView == null)
			vi = PikachuActivity.getPikachu().getLayoutInflater().inflate(R.layout.game_pikachu, parent, false);

		
		ImageView btn = (ImageView) vi.findViewById(R.id.imageView1);
		btn.setBackgroundColor(android.graphics.Color.TRANSPARENT);
//		btn.setBackgroundResource(R.drawable.hover);
		btn.setImageResource(mThumbIds[position]);
		// ImageViewLevel imageView = new ImageViewLevel(mContext);
		// imageView.setImageBitmap(mThumbIds[position]);
		// imageView.setScaleType(ScaleType.FIT_XY);
		// DisplayMetrics dm =
		// PikachuActivity.getPikachu().getResources().getDisplayMetrics();
		// float dpInPxW =
		// TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, dm);
		// float dpInPxH =
		// TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 63, dm);
		// imageView.setLayoutParams(new GridView.LayoutParams((int)dpInPxW,
		// (int)dpInPxH));

		return vi;
	}

	public Object getItem(int pos) {

		return pos;
	}

	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

}