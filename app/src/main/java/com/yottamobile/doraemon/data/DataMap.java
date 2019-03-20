package com.yottamobile.doraemon.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import com.yottamobile.doraemon.object.PikaMission;
import com.yottamobile.doraemon.scene.PikaPlayScene;

import android.content.Context;
import android.content.res.AssetManager;

public class DataMap {

	String TIME = "Time";
	String MOVE = "Move";
	String ADD_NO_MOVE_TIME = "AddNoMoveTime";
	String ADD_BOOM_TIME = "AddBoomTime";
	String BOOM_TIME = "BoomTime";
	String COUNT_BOOM = "CountBoom";
	String COUNT_NO_MOVE = "CountNoMove";
	String COUNT_FLIP = "CountFlip";
	String FLIP_TIME = "FlipTime";
	String POINT1 = "point1";
	String POINT2 = "point2";
	String POINT3 = "point3";

	public int row, col, move, count, level, time, addNoMoveTime, addBoomTime, boomTime, countBoom, countNoMove, countFlip, flipTime, point, point2,
			point3;
	public int[][] map;
	public Vector<PikaMissionData> vPikaMiss;

	public String getContent(String str, String key) {
		String value = "";
		String[] strSplit = str.split(" ");

		for (int i = 0; i < strSplit.length; i++) {
			if (strSplit[i].startsWith(key)) {
				String tValue = strSplit[i].substring(key.length() + 1);
				value = tValue;
				break;
			}
		}

		return value;
	}

	public int convertStringToInt(String str) {
		if (str.length() == 0)
			return 0;
		return Integer.parseInt(str);
	}

	public DataMap(Context context, int level) {
		this.level = level;
		InputStreamReader reader = null;
		try {
			AssetManager am = context.getAssets();
			reader = new InputStreamReader(am.open("lvl/" + level + ".lvl"));
			BufferedReader br = new BufferedReader(reader);
			String line;
			String effect = br.readLine();

			time = convertStringToInt(getContent(effect, TIME));
			move = convertStringToInt(getContent(effect, MOVE));
			addNoMoveTime = convertStringToInt(getContent(effect, ADD_NO_MOVE_TIME));
			addBoomTime = convertStringToInt(getContent(effect, ADD_BOOM_TIME));
			boomTime = convertStringToInt(getContent(effect, BOOM_TIME));
			countBoom = convertStringToInt(getContent(effect, COUNT_BOOM));
			countNoMove = convertStringToInt(getContent(effect, COUNT_NO_MOVE));
			countFlip = convertStringToInt(getContent(effect, COUNT_FLIP));
			flipTime = convertStringToInt(getContent(effect, FLIP_TIME));
			point = convertStringToInt(getContent(effect, POINT1));
			point2 = convertStringToInt(getContent(effect, POINT2));
			point3 = convertStringToInt(getContent(effect, POINT3));

			vPikaMiss = new Vector<PikaMissionData>();
			String mission = br.readLine();
			String[] tmpMiss = mission.split(";");
			for (int i = 0; i < tmpMiss.length; i++) {
				int count = convertStringToInt(getContent(tmpMiss[i], "count"));
				int type = convertStringToInt(getContent(tmpMiss[i], "type"));
				if (count > 0 && type > 0)
					vPikaMiss.add(new PikaMissionData(count, type));
			}

			row = Integer.parseInt(br.readLine());
			col = Integer.parseInt(br.readLine());
			count = Integer.parseInt(br.readLine());
			map = new int[row][col];
			int cntRow = 0;
			while ((line = br.readLine()) != null && !line.equals("")) {
				String[] data = line.split(",");
				for (int i = 0; i < data.length; i++) {
					map[cntRow][i] = Integer.parseInt(data[i]);
				}
				cntRow++;
			}
//			if(point == 0) {
				point = count / 2;
				if(level>40){
					point = (int) (((float)1.5f)*(float)point);
					point2 = 2*point;
					point3 = 3*point;
				} else {
					point2 = (int) (((float)1.5f)*(float)point);
					point3 = 2*point;
				}
//			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception ex) {

				}
			}
		}
	}

	public void randomMap() {
		this.level = 0;
		Random r = new Random();

		row = 8;
		col = 12;
		count = 60;
		map = new int[row][col];
		for (int i = 1; i < row - 1; i++)
			for (int j = 1; j < col - 1; j++)
				map[i][j] = 1;

		time = 10;
		move = r.nextInt(PikaPlayScene.RAND_MOVE + 1);
		int addNoMove = r.nextInt();
		if (addNoMove % 2 == 0)
			addNoMoveTime = r.nextInt(10) + 10;
		int addBoom = r.nextInt();
		if (addBoom % 2 == 0)
			addBoomTime = r.nextInt(10) + 10;
		boomTime = r.nextInt(PikaPlayScene.RAND_MOVE + 1) + 15;

		int addFlip = r.nextInt();
		if (addFlip % 2 == 0)
			flipTime = r.nextInt(10) + 10;

		int addCountFlip = r.nextInt();
		if (addCountFlip % 2 == 0) {
			countFlip = r.nextInt(4);
		}

		int addCountBoom = r.nextInt();
		if (addCountBoom % 2 == 0) {
			countBoom = r.nextInt(4);
			if (countBoom % 2 != 0)
				countBoom++;
		}

		int addCountNoMove = r.nextInt();
		if (addCountNoMove % 2 == 0) {
			countNoMove = r.nextInt(4);
			if (countNoMove % 2 != 0)
				countNoMove++;
		}
	}
}
