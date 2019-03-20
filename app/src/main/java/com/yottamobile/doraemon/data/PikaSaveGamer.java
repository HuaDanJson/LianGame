package com.yottamobile.doraemon.data;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.yottamobile.doraemon.Pikachu;

public class PikaSaveGamer {

	public static final String GAME = "pikachu";
	public static final String KEY_LEVEL = "level";
	public static final String KEY_STATE = "star";
	public static final String KEY_SOUND_BG = "sound_bg";
	public static final String KEY_SOUND_GAME = "sound_game";
	public static final String KEY_HIGHSCORE = "highscore";
	public static final String KEY_TUTORIAL = "tutorial";

	public static final int LV_LOCK = 0;
	public static final int LV_UNLOCK = 1;
	public static final int LV_1_STAR = 2;
	public static final int LV_2_STAR = 3;
	public static final int LV_3_STAR = 4;

	public static boolean getSound(String key) {
		SharedPreferences prefs = Pikachu.getPikachu().getSharedPreferences(GAME, Context.MODE_PRIVATE);
		boolean data = prefs.getBoolean(key, true);
		return data;
	}

	public static void saveSoundGame(String key, boolean data) {
		SharedPreferences prefs = Pikachu.getPikachu().getSharedPreferences(GAME, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putBoolean(key, data);
		editor.commit();
	}

	public static void saveHighScore(int score) {
		SharedPreferences prefs = Pikachu.getPikachu().getSharedPreferences(GAME, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putInt(KEY_HIGHSCORE, score);
		editor.commit();
	}

	public static String defaultLevel() {
		JSONArray arr = new JSONArray();
		JSONObject objLv1 = new JSONObject();
		try {
			objLv1.put(KEY_LEVEL, 0);
			objLv1.put(KEY_STATE, 1);
			arr.put(objLv1);

			for (int i = 1; i < Pikachu.LVL_MAX; i++) {
				JSONObject objLv = new JSONObject();

				objLv.put(KEY_LEVEL, i);
				objLv.put(KEY_STATE, 0);
				arr.put(objLv);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return arr.toString();
	}

	public static Vector<PikaLevelData> getDataLevel() {
		Vector<PikaLevelData> vData = new Vector<PikaLevelData>();
		SharedPreferences prefs = Pikachu.getPikachu().getSharedPreferences(GAME, Context.MODE_PRIVATE);
		String data = prefs.getString(KEY_LEVEL, defaultLevel());
		try {
			JSONArray arrLv = new JSONArray(data);
			for (int i = 0; i < arrLv.length(); i++) {
				JSONObject objLv = arrLv.getJSONObject(i);
				int level = objLv.getInt(KEY_LEVEL);
				int state = objLv.getInt(KEY_STATE);
				vData.add(new PikaLevelData(level, state));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return vData;
	}

	public static void saveDataLevel(Vector<PikaLevelData> vData) {
		SharedPreferences prefs = Pikachu.getPikachu().getSharedPreferences(GAME, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		String data = "";

		JSONArray arrLv = new JSONArray();
		try {
			for (int i = 0; i < vData.size(); i++) {
				JSONObject objLv = new JSONObject();
				objLv.put(KEY_LEVEL, vData.get(i).level);
				objLv.put(KEY_STATE, vData.get(i).state);
				arrLv.put(objLv);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data = arrLv.toString();
		editor.putString(KEY_LEVEL, data);
		editor.commit();
	}

	public static void unlockAllMap() {
		JSONArray arr = new JSONArray();
		try {
			for (int i = 0; i < Pikachu.LVL_MAX; i++) {
				JSONObject objLv = new JSONObject();

				objLv.put(KEY_LEVEL, i);
				objLv.put(KEY_STATE, 1);
				arr.put(objLv);
			}

			SharedPreferences prefs = Pikachu.getPikachu().getSharedPreferences(GAME, Context.MODE_PRIVATE);
			Editor editor = prefs.edit();
			String data = "";
			data = arr.toString();
			editor.putString(KEY_LEVEL, data);
			editor.commit();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void lockAllMap() {
		SharedPreferences prefs = Pikachu.getPikachu().getSharedPreferences(GAME, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		String data = "";
		data = defaultLevel();
		editor.putString(KEY_LEVEL, data);
		editor.commit();
	}

	public static int getHighScore() {
		SharedPreferences prefs = Pikachu.getPikachu().getSharedPreferences(GAME, Context.MODE_PRIVATE);
		int score = prefs.getInt(KEY_HIGHSCORE, 0);
		return score;
	}

}
