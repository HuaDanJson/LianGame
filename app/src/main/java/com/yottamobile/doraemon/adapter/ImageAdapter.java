package com.yottamobile.doraemon.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yottamobile.doraemon.PikachuActivity;
import com.yottamobile.doraemon.R;
import com.yottamobile.doraemon.data.PikaLevelData;
import com.yottamobile.doraemon.data.PikaSaveGamer;

import java.util.Vector;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    public Vector<PikaLevelData> vData;

    public ImageAdapter(Context c) {
        mContext = c;

    }

    @Override
    public int getCount() {
        return PikachuActivity.LVL_MAX;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
//		if (convertView == null)
        vi = PikachuActivity.getPikachu().getLayoutInflater().inflate(R.layout.item_lv_detail, parent, false);

        PikaLevelData data = vData.get(position);

        ImageView number1 = (ImageView) vi.findViewById(R.id.number1);
        ImageView number2 = (ImageView) vi.findViewById(R.id.number2);
        ImageView number3 = (ImageView) vi.findViewById(R.id.number3);
        RelativeLayout lv_lock = (RelativeLayout) vi.findViewById(R.id.layoutNumber);
        ImageView star1 = (ImageView) vi.findViewById(R.id.itm_star_1);
        ImageView star2 = (ImageView) vi.findViewById(R.id.itm_star_2);
        ImageView star3 = (ImageView) vi.findViewById(R.id.itm_star_3);

        int level = data.level + 1;
        int n3 = level % 10;
        int n2 = ((level - n3) / 10) % 10;
        int n1 = ((level - n3 - n2 * 10) / 100) % 10;

        if (data.state == PikaSaveGamer.LV_LOCK) {
            lv_lock.setBackgroundResource(R.drawable.locked);
            star1.setVisibility(View.GONE);
            star2.setVisibility(View.GONE);
            star3.setVisibility(View.GONE);
        } else {
//			lv_lock.setVisibility(View.GONE);
            if (level < 10) {
                number1.setVisibility(View.GONE);
                number2.setVisibility(View.GONE);
                number3.setVisibility(View.VISIBLE);
                number3.setImageResource(getIDbyNumber(n3));
            } else if (level < 100) {
                number1.setVisibility(View.GONE);
                number2.setVisibility(View.VISIBLE);
                number3.setVisibility(View.VISIBLE);
                number2.setImageResource(getIDbyNumber(n2));
                number3.setImageResource(getIDbyNumber(n3));
            } else {
                number1.setVisibility(View.VISIBLE);
                number2.setVisibility(View.VISIBLE);
                number3.setVisibility(View.VISIBLE);
                number1.setImageResource(getIDbyNumber(n1));
                number2.setImageResource(getIDbyNumber(n2));
                number3.setImageResource(getIDbyNumber(n3));
            }

            if (data.state >= PikaSaveGamer.LV_1_STAR) {
                star1.setImageResource(R.drawable.star_1);
            }

            if (data.state >= PikaSaveGamer.LV_2_STAR) {
                star2.setImageResource(R.drawable.star_1);
            }

            if (data.state >= PikaSaveGamer.LV_3_STAR) {
                star3.setImageResource(R.drawable.star_1);
            }
        }
        return vi;
    }

    @Override
    public Object getItem(int pos) {
        return pos;
    }

    @Override
    public long getItemId(int pos) {
        // TODO Auto-generated method stub
        return pos;
    }

    public int getIDbyNumber(int number) {
        switch (number) {
            case 0:
                return R.drawable.number_0;
            case 1:
                return R.drawable.number_1;
            case 2:
                return R.drawable.number_2;
            case 3:
                return R.drawable.number_3;
            case 4:
                return R.drawable.number_4;
            case 5:
                return R.drawable.number_5;
            case 6:
                return R.drawable.number_6;
            case 7:
                return R.drawable.number_7;
            case 8:
                return R.drawable.number_8;
            case 9:
                return R.drawable.number_9;
            default:
                return R.drawable.number_0;
        }
    }
}