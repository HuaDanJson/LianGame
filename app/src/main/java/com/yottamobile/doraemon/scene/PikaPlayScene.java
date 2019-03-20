package com.yottamobile.doraemon.scene;

import java.util.Random;
import java.util.Vector;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.text.TickerText;
import org.andengine.entity.text.TickerText.TickerTextOptions;
import org.andengine.entity.text.exception.OutOfCharactersException;
import org.andengine.entity.util.ScreenCapture;
import org.andengine.entity.util.ScreenCapture.IScreenCaptureCallback;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.FileUtils;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.ease.EaseBackInOut;
import org.andengine.util.modifier.ease.EaseQuadOut;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.widget.Toast;

import com.yottamobile.doraemon.Pikachu;
import com.yottamobile.doraemon.R;
import com.yottamobile.doraemon.SceneType;
import com.yottamobile.doraemon.data.DataMap;
import com.yottamobile.doraemon.data.PikaSaveGamer;
import com.yottamobile.doraemon.data.PikachuPos;
import com.yottamobile.doraemon.object.PikaMission;
import com.yottamobile.doraemon.ui.MButton;
import com.yottamobile.doraemon.ui.Pika;
import com.yottamobile.doraemon.ui.ProgressBar;


public class PikaPlayScene extends CustomScene implements IMyScene, OnClickListener {
	// private static int ROW = 8;
	// private static int COL = 12;
	public static int PIKA_LENGH = 20;
	private static int ROW = 8;
	private static int COL = 12;

	private static int NO_MOVE = 0;
	private static int LEFT = 1;
	private static int RIGHT = 2;
	private static int UP = 3;
	private static int DOWN = 4;
	public static int RAND_MOVE = 5;

	public static int PIKA = 17;
	public static int PIKA_NO_MOVE = 18;
	public static int PIKA_BOOM = 19;
	public static int PIKA_FLIP = 20;

	private float EXTRA_TIME = 4;
	float mTimeCombo = 1.5f;
	float mTime = 0;
	int boomTime, addNoMoveTime, addBoomTime, flipTime;
	float TIME_MISSION = 60;
	int EFFECT_COMBO = 3;
	private int fix_point = 1;

	int PIKA_SIZE = (Integer) (((6 * 89 / ROW) > 89) ? 89 : (6 * 89 / ROW));
	float perSize = 1;
	protected static final int TIP_TIME = 4;
	private static final int PIKA_TABLE_HEIGHT = 425;
	private static final int PIKA_TABLE_WIDTH = 675;
	private static final Object BTN_SHARE = null;

	float PADDING_TOP = 40 - PIKA_SIZE;
	float PADDING_LEFT = 115 - PIKA_SIZE;

	// Changeable
	Sprite fire;
	Vector<IUpdateHandler> sessionThread = new Vector<IUpdateHandler>();
	Pika tmpSprite;
	AnimatedSprite aniCombo;
	Text mScore;
	int score = 0;
	int point = 0;
	int cntCombo = 0;
	int[][] table = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 1, 1, 1, 1, 1, 1, 1, 0 }, { 0, 2, 1, 1, 1, 1, 1, 1, 0 },
			{ 0, 3, 1, 1, 1, 1, 1, 1, 0 }, { 0, 4, 1, 1, 1, 1, 1, 1, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

	private Vector<PointF> vPos;
	int cntDone = 0;
	private boolean useBoom = false;
	private boolean useLockTime = false;
	private boolean waiting = false;
	private Font mFont;
	private BaseGameActivity game;
	private Camera mCamera;
	private boolean useItemBoom = false;
	private boolean useItemRandom = false;
	private boolean useItemClock = false;

	private int countPikachu, countBoom, countNoMove, countFlip;
	private DataMap dataMap;
	private ProgressBar proTimeCombo;
	private ProgressBar proTime;
	private TextureRegion mParticleTextureRegion;
	private Vector<PikaMission> vPikaMiss;
	private Text mLevel;
	private MButton btnBoom;
	private MButton btnRandom;
	private MButton btnLockTime;
	private int currentStep = 1;
	private int totalStep = 5;

	public PikaPlayScene() {

	}

	public void initLevel() {
		
		
		cntCombo = 0;
		if (Pikachu.getPikachu().game_mode == Pikachu.GAME_CLASSIC) {
			dataMap = new DataMap(game.getApplicationContext(), Pikachu.LVL);
		} else {
			Pikachu.getPikachu().detectView(Pikachu.VISIBLE_ADS);
			Pikachu.LVL = 1;
			dataMap = new DataMap(game.getApplicationContext(), Pikachu.LVL);
			vPikaMiss = null;
			dataMap.randomMap();
		}
		ROW = dataMap.row;
		COL = dataMap.col;
		TIME_MISSION = dataMap.time;

		addNoMoveTime = dataMap.addNoMoveTime;
		addBoomTime = dataMap.addBoomTime;
		boomTime = dataMap.boomTime;
		countBoom = dataMap.countBoom;
		countNoMove = dataMap.countNoMove;
		flipTime = dataMap.flipTime;
		countFlip = dataMap.countFlip;
		point = dataMap.point;

		if (vPikaMiss == null)
			vPikaMiss = new Vector<PikaMission>();

		float y = 6;
		float x = 349;

		if (vPikaMiss.size() == 0) {
			int typeMiss = 5;
			int countMiss = 1;
			Random r = new Random();
			typeMiss = r.nextInt(PIKA - 1) + 1;
			for (int i = 0; i < dataMap.vPikaMiss.size(); i++) {
				typeMiss = dataMap.vPikaMiss.elementAt(i).type;
				countMiss = dataMap.vPikaMiss.elementAt(i).count;
				if (typeMiss == 0 || countMiss == 0)
					continue;
				PikaMission miss1 = new PikaMission(x, y, 33, 33, Pikachu.getPikachu().texturePikachu[typeMiss - 1],
						game.getVertexBufferObjectManager());
				final Text time = new Text(13, -15, Pikachu.getPikachu().fontTektonProRed, countMiss + "", 2, new TextOptions(HorizontalAlign.LEFT),
						game.getVertexBufferObjectManager());
				time.setScaleCenter(0, 0);
				time.setScale(0.4f);
				miss1.setMission(countMiss, typeMiss, time);
				vPikaMiss.add(miss1);
				attachChild(miss1);
				miss1.setCount(miss1.count);
				x -= 72;
			}
		}

		countPikachu = dataMap.count - countBoom - countNoMove;
		table = dataMap.map;
		tmpSprite = null;
		cntDone = 0;
		useBoom = false;
		useLockTime = false;

		if (useItemBoom)
			btnBoom.detachChild(99);
		if (useItemClock)
			btnLockTime.detachChild(99);
		if (useItemRandom)
			btnRandom.detachChild(99);
		useItemBoom = useItemClock = useItemRandom = false;
		aniCombo.setVisible(false);
		proTime.clearUpdateHandlers();
		mTime = TIME_MISSION;
		if (Pikachu.getPikachu().game_mode == Pikachu.GAME_CLASSIC)
			score = 0;
		PIKA_SIZE = (Integer) (((6 * 89 / ROW) > 89) ? 89 : (6 * 89 / ROW));
		PADDING_TOP = 40 - PIKA_SIZE;
		PADDING_LEFT = 115 - PIKA_SIZE;
		if ((ROW - 2) * PIKA_SIZE < PIKA_TABLE_HEIGHT)
			PADDING_TOP += (PIKA_TABLE_HEIGHT - ((ROW - 2) * PIKA_SIZE)) / 2;
		if ((COL - 2) * PIKA_SIZE < PIKA_TABLE_WIDTH)
			PADDING_LEFT += (PIKA_TABLE_WIDTH - ((COL - 2) * PIKA_SIZE)) / 2;

		mLevel.setText((dataMap.level) + "");
		mScore.setText("" + score);
		perSize = (float) PIKA_SIZE / (float) 89;

		final float rateP = ((float) (mTime / (float) 100)) / 100;

		proTime.clearUpdateHandlers();
		proTime.setProgress(100);

		final IUpdateHandler timeH1 = new TimerHandler(rateP, true, new ITimerCallback() {
			float percent = 100;
			boolean done = false;
			float per = 1;

			public void onTimePassed(TimerHandler pTimerHandler) {

				if (!done) {
					percent = proTime.getPercent();
					percent -= (per / 100);
					// System.out.println(percent);

					if (percent < 0) {
						percent = 0;
						done = true;
						proTime.clearUpdateHandlers();
						new Thread() {
							public void run() {
								youLose();
							};
						}.start();

					}
					;
					proTime.setProgress(percent);
				}
			}
		});

		proTime.registerUpdateHandler(timeH1);

		proTimeCombo.clearUpdateHandlers();
		proTimeCombo.setProgress(0);
		final float rateP1 = ((float) (mTimeCombo / (float) 100)) / 100;

		proTimeCombo.registerUpdateHandler(new TimerHandler(rateP1, true, new ITimerCallback() {

			private int cnt;
			private float countIdie;

			public void onTimePassed(TimerHandler pTimerHandler) {

				if (cnt == cntDone) {
					countIdie += rateP1;
					if (countIdie >= mTimeCombo) {
						proTimeCombo.setProgress(0);
						cntDone = 0;
						cntCombo = 0;
						aniCombo.setVisible(false);
					}
				} else {
					if (cntDone != 0)
						proTimeCombo.setProgress(100);
					cnt = cntDone;
					countIdie = 0;
				}

				float per = 1;
				float percent = proTimeCombo.getPercent();
				percent -= (per / 100);
				if (percent < 0) {
					percent = 0;
				}

				proTimeCombo.setProgress(percent);
			}

		}));
		// clearUpdateHandlers();

		while (sessionThread.size() > 0) {
			unregisterUpdateHandler(sessionThread.get(0));
			sessionThread.removeElementAt(0);
		}

		if (addBoomTime > 0) {
			IUpdateHandler thread = genThreadAddPika(addBoomTime, PIKA_BOOM);
			if (thread != null) {
				sessionThread.add(thread);
				registerUpdateHandler(thread);
			}
		}
		if (addNoMoveTime > 0) {
			IUpdateHandler thread = genThreadAddPika(addNoMoveTime, PIKA_NO_MOVE);
			if (thread != null) {
				sessionThread.add(thread);
				registerUpdateHandler(thread);
			}
		}

		if (flipTime > 0) {
			IUpdateHandler thread = genThreadFlipPika(flipTime, PIKA_FLIP);
			if (thread != null) {
				sessionThread.add(thread);
				registerUpdateHandler(thread);
			}
		}
		if (Pikachu.getPikachu().game_mode == Pikachu.GAME_CLASSIC)
			showMission();
	}

	private boolean clearMission() {
		boolean check = true;
		for (int j = 0; j < vPikaMiss.size(); j++) {
			PikaMission miss = vPikaMiss.get(j);
			if (miss.count >= 0) {
				check = false;
				break;
			}
		}
		return check;
	}

	private void fillMission(Vector<Pika> vPika) {
		for (int i = 0; i < vPika.size(); i++) {
			Pika p = vPika.get(i);
			PikachuPos pos = (PikachuPos) p.getUserData();
			for (int j = 0; j < vPikaMiss.size(); j++) {
				PikaMission miss = vPikaMiss.get(j);
				if (miss.type == pos.type)
					if (miss.count > 0) {
						miss.count--;
						miss.setCount(miss.count);
						// miss.mText.setText(miss.count+"");
					}
			}
		}

		for (int j = 0; j < vPikaMiss.size(); j++) {
			PikaMission miss = vPikaMiss.get(j);
			if (miss.count == 0) {
				vPikaMiss.remove(j);
				detachChild(miss);
				if (j > 0)
					j--;
			}
		}

		// if (dataMap.vPikaMiss.size() > 0 && vPikaMiss.size() == 0) {
		// Pikachu.LVL++;
		// if (Pikachu.LVL > Pikachu.LVL_MAX)
		// Pikachu.LVL = 1;
		// genMap();
		// }
	}

	public IUpdateHandler genThreadAddPika(final int time, final int type) {
		if (time > 0) {

			TimerHandler thread = new TimerHandler(1, true, new ITimerCallback() {
				int cntTime = 0;

				public void onTimePassed(TimerHandler pTimerHandler) {
					cntTime++;
					if (cntTime > time) {
						cntTime = 0;
						Pika mPika = getRandomPika();
						if (mPika != null) {
							PikachuPos pos = (PikachuPos) mPika.getUserData();
							Vector<Pika> vPika = getPikasByType(pos.type);
							if (type == PIKA_BOOM) {
								if (vPika.size() >= 2) {
									Pika pika1 = vPika.get(0);
									Pika pika2 = vPika.get(1);
									transformPika(pika1, type);
									transformPika(pika2, type);
								}
							} else {
								if (vPika.size() > 2 || getAllPika().size() > 4) {
									Pika pika1 = vPika.get(0);
									Pika pika2 = vPika.get(1);
									transformPika(pika1, type);
									transformPika(pika2, type);
								}
							}
							if (deadLock())
								reGenMap();
						}
					}
				}
			});
			return thread;
		}
		return null;
	}

	public IUpdateHandler genThreadFlipPika(final int time, final int type) {
		if (time > 0) {

			TimerHandler thread = new TimerHandler(1, true, new ITimerCallback() {
				int cntTime = 0;

				public void onTimePassed(TimerHandler pTimerHandler) {
					cntTime++;
					if (cntTime > time) {
						cntTime = 0;
						Pika mPika = getAllNotHiddenRandomPika();
						if (mPika != null) {
							PikachuPos pos = (PikachuPos) mPika.getUserData();
							Vector<Pika> vPika = getPikasByType(pos.type);
							if (vPika.size() > 0) {
								final Pika pika1 = vPika.get(0);
								pika1.isHidden = true;
								ScaleModifier toMin = new ScaleModifier(0.1f, 1, 0, 1, 1) {
									protected void onModifierFinished(IEntity pItem) {
										pika1.setHidden(new Sprite(0, 0, PIKA_SIZE, PIKA_SIZE, Pikachu.getPikachu().texturePikachu[type - 1], game
												.getVertexBufferObjectManager()));
										super.onModifierFinished(pItem);
									};
								};
								ScaleModifier toMax = new ScaleModifier(0.1f, 0, 1, 1, 1);
								SequenceEntityModifier sequen = new SequenceEntityModifier(toMin, toMax);
								pika1.registerEntityModifier(sequen);

							}
						}
					}
				}
			});
			return thread;
		}
		return null;
	}

	public void transformPika(final Pika pika, final int type) {
		final PikachuPos pos1 = (PikachuPos) pika.getUserData();
		table[pos1.row][pos1.col] = type;

		if (type == PIKA_NO_MOVE) {
			pos1.type = type;
			final ScaleModifier mScale = new ScaleModifier(0.2f, 2, 1);

			AnimatedSprite aniIce = new AnimatedSprite(0, 0, PIKA_SIZE, PIKA_SIZE, Pikachu.getPikachu().tiledEffectIce,
					PikaPlayScene.this.game.getVertexBufferObjectManager());

			aniIce.animate(100);
			aniIce.registerEntityModifier(mScale);
			pika.attachChild(aniIce);

		} else {
			final ScaleModifier mScaleMax = new ScaleModifier(0.2f, 0, 1);
			ScaleModifier mScaleMin = new ScaleModifier(0.2f, 1, 0) {
				@Override
				protected void onModifierFinished(IEntity pItem) {
					// texturePikachu
					Pika newPika = genPika(pika.getX(), pika.getY(), new PikachuPos(pos1.row, pos1.col, type, pos1.move));

					newPika.setScale(0);

					newPika.registerEntityModifier(mScaleMax);

					pika.setVisible(false);

					PikaPlayScene.this.unregisterTouchArea(pika);

					registerUpdateHandler(new TimerHandler(0.2f, new ITimerCallback() {

						public void onTimePassed(TimerHandler pTimerHandler) {
							PikaPlayScene.this.detachChild(pika);

						}
					}));
					super.onModifierFinished(pItem);
				}
			};

			pika.registerEntityModifier(mScaleMin);
		}
	}

	public Pika getRandomPika() {
		Vector<Pika> vPikas = getAllPika();
		Vector<Pika> list = new Vector<Pika>();

		for (int i = 0; i < vPikas.size(); i++) {
			Pika p = vPikas.get(i);
			PikachuPos pos = (PikachuPos) p.getUserData();
			if (pos.type < PIKA) {
				list.add(p);
			}
		}
		Random r = new Random();
		int location = r.nextInt(list.size());
		return list.elementAt(location);
	}

	public Pika getAllNotHiddenRandomPika() {
		Vector<Pika> vPikas = getAllPika();
		Vector<Pika> list = new Vector<Pika>();

		for (int i = 0; i < vPikas.size(); i++) {
			Pika p = vPikas.get(i);
			if (!p.isHidden)
				list.add(p);

		}
		Random r = new Random();
		int location = r.nextInt(list.size());
		return list.elementAt(location);
	}

	public Vector<Pika> getPikasByType(int type) {
		Vector<Pika> listPika = new Vector<Pika>();
		Vector<Pika> vPikas = getAllPika();
		for (int i = 0; i < vPikas.size(); i++) {
			Pika p = vPikas.get(i);
			PikachuPos pos = (PikachuPos) p.getUserData();
			if (pos.type == type) {
				listPika.add(p);
			}
		}
		return listPika;
	}

	public boolean deadLock() {

		boolean check = true;
		Vector<Pika> vPika = getAllPika();
		int length = vPika.size();
		Pika pika1, pika2;
		for (int i = 0; i < length; i++) {
			pika1 = vPika.elementAt(i);
			for (int j = i + 1; j < length; j++) {
				pika2 = vPika.elementAt(j);
				if (pikachuConnect(pika1, pika2)) {
					check = false;
					break;
				}
			}
			if (!check)
				break;
		}
		return check;
	}

	public void tip() {
		boolean check = true;
		Vector<Pika> vPika = getAllPika();
		int length = vPika.size();
		Pika pika1, pika2;
		for (int i = 0; i < length; i++) {
			pika1 = vPika.elementAt(i);
			if (pika1.isHidden)
				continue;
			for (int j = i + 1; j < length; j++) {
				pika2 = vPika.elementAt(j);
				if (pika2.isHidden)
					continue;
				if (pikachuConnect(pika1, pika2)) {
					ScaleModifier mScale1 = new ScaleModifier(0.25f, 1, 1.2f);
					ScaleModifier mScale2 = new ScaleModifier(0.25f, 1.2f, 1);
					SequenceEntityModifier sequen = new SequenceEntityModifier(mScale1, mScale2);

					LoopEntityModifier loopScale = new LoopEntityModifier(sequen, 3);
					pika1.registerEntityModifier(loopScale);
					pika2.registerEntityModifier(loopScale);
					check = false;
					break;
				}
			}
			if (!check)
				break;
		}

	}

	public Pika getPikaByTable(int row, int col) {
		Pika pika = null;
		Vector<Pika> vPika = getAllPika();

		int length = vPika.size();
		// System.out.println("search "+row +" - "+col);
		Pika pika1;
		for (int i = 0; i < length; i++) {
			pika1 = vPika.elementAt(i);
			PikachuPos pos = (PikachuPos) pika1.getUserData();
			if (pos.row == row && pos.col == col) {
				pika = pika1;
				break;
			}
		}
		return pika;
	}

	public void randomPos() {
		Vector<Pika> vPika = getAllPika();
		Vector<Pika> vPikaTmp = new Vector<Pika>();
		Vector<Pika> vPikaTmp1 = new Vector<Pika>();
		for (int i = 0; i < vPika.size(); i++) {
			vPikaTmp.add(vPika.elementAt(i));
			// System.out.println("vPikaTmp "+((PikachuPos)vPika.elementAt(i).getUserData()).col+" - "+((PikachuPos)vPika.elementAt(i).getUserData()).row);
		}
		Random r = new Random();

		while (vPikaTmp.size() > 0) {
			int location = r.nextInt(vPikaTmp.size());
			vPikaTmp1.add(vPikaTmp.elementAt(location));
			vPikaTmp.removeElementAt(location);

		}
		int[] col = new int[vPikaTmp1.size()];
		int[] row = new int[vPikaTmp1.size()];
		for (int i = 0; i < vPikaTmp1.size(); i++) {
			Pika pika1 = vPikaTmp1.elementAt(i);
			col[i] = ((PikachuPos) pika1.getUserData()).col;
			row[i] = ((PikachuPos) pika1.getUserData()).row;
		}
		for (int i = 0; i < vPika.size(); i++) {
			Pika pika1 = vPika.elementAt(i);
			((PikachuPos) pika1.getUserData()).col = col[i];
			((PikachuPos) pika1.getUserData()).row = row[i];
		}
	}

	public void reGenMap() {
		waiting = true;
		randomPos();

		for (int i = 1; i < ROW - 1; i++) {
			for (int j = 1; j < COL - 1; j++) {
				if (table[i][j] != 0) {

					final Pika pika = getPikaByTable(i, j);
					pika.setScale(1);
					pika.clearEntityModifiers();

					PikachuPos pos = (PikachuPos) pika.getUserData();
					table[i][j] = pos.type;
					MoveModifier move1 = new MoveModifier(0.2f, pika.getX(), Pikachu.CAMERA_WIDTH / 2, pika.getY(), Pikachu.CAMERA_HEIGHT / 2);
					MoveModifier move2 = new MoveModifier(0.5f, Pikachu.CAMERA_WIDTH / 2, pos.col * PIKA_SIZE + PADDING_LEFT,
							Pikachu.CAMERA_HEIGHT / 2, pos.row * PIKA_SIZE + PADDING_TOP, EaseBackInOut.getInstance());
					SequenceEntityModifier sequen = new SequenceEntityModifier(move1, move2) {
						@Override
						protected void onModifierFinished(IEntity pItem) {
							waiting = false;
							super.onModifierFinished(pItem);
						}
					};
					pika.registerEntityModifier(sequen);
				}
			}
		}
		if (deadLock())
			reGenMap();
	}

	public void clearMap() {

		Vector<Pika> vPika = getAllPika();
		int length = vPika.size();
		// Pika pika;
		for (int i = 0; i < length; i++) {
			final Pika pika = vPika.elementAt(i);
			pika.setVisible(false);
			detachChild(pika);
			unregisterTouchArea(pika);
			effectBoom(pika);
		}

	}

	public Pika genPika(float x, float y, PikachuPos pos) {

		TextureRegion pikaRegion = Pikachu.getPikachu().texturePikachu[table[pos.row][pos.col] - 1];

		if (table[pos.row][pos.col] == PIKA_NO_MOVE) {
			pikaRegion = Pikachu.getPikachu().texturePikachu[0];
		}

		final Pika pika = new Pika(x, y, PIKA_SIZE, PIKA_SIZE, pikaRegion, game.getVertexBufferObjectManager()) {

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				PikachuPos pos = (PikachuPos) getUserData();
				if (table[pos.row][pos.col] == 0 || waiting) {
					// System.out.println("ô rỗng");
				} else {
					if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {

						if (this.isHidden) {
							flipUp();
						}

						if (useBoom) {
							System.out.println("use boom");
							if (table[pos.row][pos.col] == PIKA_NO_MOVE) {
								table[pos.row][pos.col] = 0;
								Vector<Pika> vPikas = new Vector<Pika>();
								PikaPlayScene.this.unregisterTouchArea(this);
								PikaPlayScene.this.detachChild(this);
								effectBoom(this);
								vPikas.add(this);

								moveEffect(vPikas);
								fillMission(vPikas);
								aniCombo.setVisible(false);
								useBoom = false;
								isClear();

								tmpSprite = null;
							} else {
								Vector<Pika> vPika = boomPika(table[pos.row][pos.col]);
								moveEffect(vPika);
								aniCombo.setVisible(false);
								useBoom = false;
								fillMission(vPika);
								isClear();

								tmpSprite = null;
							}
							return true;
						} else {
							if (table[pos.row][pos.col] == PIKA_NO_MOVE)
								return true;
							if (tmpSprite == null) {
								tmpSprite = this;

								ScaleModifier mScale1 = new ScaleModifier(0.25f, 1, 1.2f);
								ScaleModifier mScale2 = new ScaleModifier(0.25f, 1.2f, 1);
								SequenceEntityModifier sequen = new SequenceEntityModifier(mScale1, mScale2);

								LoopEntityModifier loopScale = new LoopEntityModifier(sequen);
								registerEntityModifier(loopScale);
								setZIndex(1);

								setHighestDepth(this);
								System.out.println("Lấy pikachu " + pos.type);
								if (cntCombo > EFFECT_COMBO) {
									aniCombo.setVisible(true);
									aniCombo.setX(getX() - 15);
									aniCombo.setY(getY() - 15);
									setHighestDepth(aniCombo);
								}
							} else {

								tmpSprite.clearEntityModifiers();
								tmpSprite.setScale(1);
								final Pika pika1 = (Pika) tmpSprite;
								tmpSprite = this;

								if (tmpSprite.isMark() || pika1.isMark()) {
									System.out.println("kiểm tra pikachu isMark " + tmpSprite.isMark + " " + pika1.isMark);
									ScaleModifier mScale1 = new ScaleModifier(0.25f, 1, 1.2f);
									ScaleModifier mScale2 = new ScaleModifier(0.25f, 1.2f, 1);
									SequenceEntityModifier sequen = new SequenceEntityModifier(mScale1, mScale2);

									LoopEntityModifier loopScale = new LoopEntityModifier(sequen);
									registerEntityModifier(loopScale);
									setZIndex(1);
									setHighestDepth(this);
									tmpSprite = this;
									if (cntCombo > EFFECT_COMBO) {
										aniCombo.setVisible(true);
										aniCombo.setX(getX() - 15);
										aniCombo.setY(getY() - 15);
										setHighestDepth(aniCombo);
									}
									cntCombo = 0;
									proTimeCombo.setProgress(0);
									// tmpSprite.setMark(false);
								} else if (pikachuConnect(pika1, this)) {
									System.out.println("ăn điểm");
									tmpSprite = null;

									Pikachu.getPikachu().playSound(Pikachu.SOUND_POINT);

									cntCombo++;
									score += fix_point * cntCombo;

									mScore.setText("" + score);
									// mScore.setText(pText);

									// tmpSprite.setMark(true);
									pika1.pikaMark = this;
									pika1.pikaMark.setMark(true);
									pika1.setMark(true);

									table[pos.row][pos.col] = 0;
									pos = (PikachuPos) pika1.getUserData();
									table[pos.row][pos.col] = 0;

									if (vPos.size() > 1) {
										fire.setX(vPos.get(0).x);
										fire.setY(vPos.get(0).y);
										fire.setVisible(true);
										aniCombo.setVisible(false);

										float[] posX = new float[vPos.size()];
										float[] posY = new float[vPos.size()];

										float length = 0;

										for (int i = 0; i < vPos.size(); i++) {
											posX[i] = vPos.get(i).x;
											posY[i] = vPos.get(i).y;
										}
										for (int i = 0; i < vPos.size() - 1; i++) {
											length += Math.sqrt(Math.pow(vPos.get(i + 1).x - vPos.get(i).x, 2)
													+ Math.pow(vPos.get(i + 1).y - vPos.get(i).y, 2));
										}

										float time = (length * 0.5f) / 850;
										if (time > 0.5f)
											time = 0.5f;

										Vector<PointF> listPF = new Vector<PointF>();
										PointF curr = null, nextP;
										while (vPos.size() > 0) {
											if (curr == null) {
												curr = vPos.elementAt(0);
												vPos.removeElementAt(0);
											}
											nextP = vPos.elementAt(0);
											vPos.removeElementAt(0);
											int padding = (PIKA_SIZE);
											if (nextP.x != curr.x) {
												float blinkX = nextP.x - curr.x;
												// System.out.println("blink X "
												// + blinkX);

												if (blinkX > 0) {
													for (int j = 0; j < blinkX / padding; j++) {
														listPF.add(new PointF(curr.x + j * padding, curr.y));
													}
												} else {
													for (int j = 0; j < -blinkX / padding; j++) {
														listPF.add(new PointF(curr.x - j * padding, curr.y));
													}
												}
											} else if (nextP.y != curr.y) {
												float blinkY = nextP.y - curr.y;
												// System.out.println("blink Y "
												// + blinkY);
												if (blinkY > 0) {
													for (int j = 0; j < blinkY / padding; j++) {
														listPF.add(new PointF(curr.x, curr.y + j * padding));
													}
												} else {
													for (int j = 0; j < -blinkY / padding; j++) {
														listPF.add(new PointF(curr.x, curr.y - j * padding));
													}
												}
											}
											curr = nextP;
											if (vPos.size() == 0)
												listPF.add(curr);
										}

										MoveModifier[] move = new MoveModifier[listPF.size() - 1];
										for (int i = 0; i < move.length; i++) {
											PointF p = listPF.elementAt(i);
											PointF p1 = listPF.elementAt(i + 1);

											move[i] = new MoveModifier(time / move.length, p.x, p1.x, p.y, p1.y) {
												protected void onModifierFinished(IEntity pItem) {
													addFire(getToValueA() - 30, getToValueB() - 30);
													super.onModifierFinished(pItem);
												};
											};
										}

										SequenceEntityModifier sequen = new SequenceEntityModifier(move) {

											protected void onModifierFinished(IEntity pItem) {
												unregisterTouchArea(pika1);
												unregisterTouchArea(pika1.pikaMark);
												PikaPlayScene.this.detachChild(pika1);
												PikaPlayScene.this.detachChild(pika1.pikaMark);
												// tmpSprite = null;
												// sceneGame.registerTouchArea(pTouchArea);
												fire.setVisible(false);

												cntDone++;
												addEffectTextCombo(pika1.pikaMark.getX() - pika1.pikaMark.getWidth(), pika1.pikaMark.getY(), cntCombo);

												Vector<Pika> vPika = new Vector<Pika>();
												vPika.add(pika1);
												vPika.add(pika1.pikaMark);
												moveEffect(vPika);

												effectBoom(pika1);
												effectBoom(pika1.pikaMark);

												fillMission(vPika);

												float percent = proTime.getPercent();
												percent += (EXTRA_TIME / mTime) * 100;
												if (percent > 100)
													percent = 100;
												proTime.setProgress(percent);

												if (isClear())
													System.out.println("done");
												else {
													if (deadLock())
														reGenMap();
												}
											}

										};

										setHighestDepth(fire);
										fire.registerEntityModifier(sequen);

									}

								} else {

									cntCombo = 0;
									proTimeCombo.setProgress(0);
									System.out.println("Lấy con khác " + ((PikachuPos) pika1.getUserData()).type + " - "
											+ ((PikachuPos) getUserData()).type);
									ScaleModifier mScale1 = new ScaleModifier(0.25f, 1, 1.2f);
									ScaleModifier mScale2 = new ScaleModifier(0.25f, 1.2f, 1);
									SequenceEntityModifier sequen = new SequenceEntityModifier(mScale1, mScale2);

									LoopEntityModifier loopScale = new LoopEntityModifier(sequen);
									registerEntityModifier(loopScale);
									setZIndex(1);
									setHighestDepth(this);

									tmpSprite = this;
									if (cntCombo > EFFECT_COMBO) {
										aniCombo.setVisible(true);
										aniCombo.setX(getX() - 15);
										aniCombo.setY(getY() - 15);
										setHighestDepth(aniCombo);
									}
								}

								return true;
							}
						}
						return false;
					}
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}

		};

		pika.setUserData(pos);

		if (table[pos.row][pos.col] == PIKA_BOOM) {
			final Text time = new Text(9, 9, Pikachu.getPikachu().fontTektonProRedBoom, "" + boomTime, new TextOptions(HorizontalAlign.CENTER),
					game.getVertexBufferObjectManager());
			time.setScale(0.5f);
			time.registerUpdateHandler(new TimerHandler(1, true, new ITimerCallback() {
				int tmpBoom = boomTime;

				public void onTimePassed(TimerHandler pTimerHandler) {
					if (!pika.addIce) {
						if (tmpBoom > 0) {
							tmpBoom--;
							if (tmpBoom >= 10)
								time.setText(tmpBoom + "");
							if (tmpBoom < 10)
								time.setText("0" + tmpBoom);
						}

						if (tmpBoom == 0) {
							youLose();
						}
					} else {

					}
				}
			}));
			pika.attachChild(time);
		} else if (table[pos.row][pos.col] == PIKA_NO_MOVE) {
			AnimatedSprite aniIce = new AnimatedSprite(0, 0, PIKA_SIZE, PIKA_SIZE, Pikachu.getPikachu().tiledEffectIce,
					PikaPlayScene.this.game.getVertexBufferObjectManager());

			aniIce.animate(100);
			pika.attachChild(aniIce);
		}
		registerTouchArea(pika);

		attachChild(pika);
		return pika;
	}

	public void genMap() {

		initLevel();

		clearMap();

		Random r = new Random();
		Vector<Integer> vMap = new Vector<Integer>();

		for (int i = 0; i < countBoom / 2; i++) {
			vMap.add(PIKA_BOOM);
			vMap.add(PIKA_BOOM);
		}

		for (int i = 0; i < countNoMove / 2; i++) {
			vMap.add(PIKA_NO_MOVE);
			vMap.add(PIKA_NO_MOVE);
		}

		for (int i = 0; i < countPikachu / 2; i++) {
			int id = i + 1;
			if (id > PIKA)
				id = r.nextInt(PIKA) + 1;
			vMap.add(id);
			vMap.add(id);
		}

		System.out.println("=========================level =" + Pikachu.LVL);
		for (int i = 1; i < ROW - 1; i++) {
			for (int j = 1; j < COL - 1; j++) {
				if (table[i][j] != 0) {
					int location = r.nextInt(vMap.size());

					table[i][j] = vMap.elementAt(location);
					// System.out.println(vMap.elementAt(location));
					vMap.removeElementAt(location);
				}
			}
		}

		for (int row = 0; row < ROW; row++) {

			for (int col = 0; col < COL; col++) {
				if (table[row][col] == 0)
					continue;

				Pika pika = genPika(Pikachu.CAMERA_WIDTH / 2, Pikachu.CAMERA_HEIGHT + 20, new PikachuPos(row, col, table[row][col], 0));

				DelayModifier delay = new DelayModifier(0);

				MoveModifier move = new MoveModifier(0.9f, pika.getX(), col * PIKA_SIZE + PADDING_LEFT, pika.getY(), row * PIKA_SIZE + PADDING_TOP,
						EaseBackInOut.getInstance());

				SequenceEntityModifier sequen = new SequenceEntityModifier(delay, move);

				pika.registerEntityModifier(sequen);

			}

		}

		for (int i = 0; i < countFlip; i++) {
			Pika pika1 = getAllNotHiddenRandomPika();
			if (pika1 != null)
				pika1.setHidden(new Sprite(0, 0, PIKA_SIZE, PIKA_SIZE, Pikachu.getPikachu().texturePikachu[PIKA_FLIP - 1], game
						.getVertexBufferObjectManager()));
		}

		boolean slideRow = r.nextBoolean();
		int move = dataMap.move;

		if (move == RAND_MOVE) {

			if (slideRow) {
				for (int i = 0; i < ROW; i++) {
					int type = r.nextInt(3);
					if (type == 1)
						setEffectRow(i, LEFT);
					if (type == 2)
						setEffectRow(i, RIGHT);
					if (type == 0)
						setEffectRow(i, NO_MOVE);
				}
			} else {
				for (int i = 0; i < COL; i++) {
					int type = r.nextInt(3);
					if (type == 1)
						setEffectCol(i, UP);
					if (type == 2)
						setEffectCol(i, DOWN);
					if (type == 0)
						setEffectCol(i, NO_MOVE);
				}
			}
		} else {
			if (move == 0 || move == 1 || move == 2)
				slideRow = true;
			else
				slideRow = false;

			if (slideRow) {
				for (int i = 0; i < ROW; i++) {
					setEffectRow(i, move);
				}
			} else {
				for (int i = 0; i < COL; i++) {
					setEffectRow(i, move);
				}
			}

		}
		if (deadLock())
			reGenMap();
	}

	public void moveEffect(Vector<Pika> vPika) {

		int length = vPika.size();
		Pika pika, pikaTmp;
		PikachuPos pos, posTmp;

		for (int i = 0; i < length; i++) {
			pika = vPika.get(i);
			pos = (PikachuPos) pika.getUserData();
			if (pos.move == LEFT) {
				for (int col = 1; col < COL - 1; col++) {
					pikaTmp = getPikaByTable(pos.row, col);

					if (pikaTmp != null && col - 1 >= 1 && table[pos.row][col - 1] == 0) {
						posTmp = (PikachuPos) pikaTmp.getUserData();

						table[pos.row][col - 1] = posTmp.type;
						table[pos.row][col] = 0;
						pikaTmp.addModifiertoSequen(new MoveModifier(0.05f, col * PIKA_SIZE + PADDING_LEFT, (col - 1) * PIKA_SIZE + PADDING_LEFT,
								pikaTmp.getY(), pikaTmp.getY()));
						posTmp.col--;
					}
				}
			} else if (pos.move == RIGHT) {
				for (int col = COL - 2; col >= 1; col--) {
					pikaTmp = getPikaByTable(pos.row, col);

					if (pikaTmp != null && col + 1 <= COL - 2 && table[pos.row][col + 1] == 0) {
						posTmp = (PikachuPos) pikaTmp.getUserData();

						table[pos.row][col + 1] = posTmp.type;
						table[pos.row][col] = 0;
						pikaTmp.addModifiertoSequen(new MoveModifier(0.05f, col * PIKA_SIZE + PADDING_LEFT, (col + 1) * PIKA_SIZE + PADDING_LEFT,
								pikaTmp.getY(), pikaTmp.getY()));
						posTmp.col++;
					}
				}
			} else if (pos.move == DOWN) {
				for (int row = ROW - 2; row >= 1; row--) {
					pikaTmp = getPikaByTable(row, pos.col);

					if (pikaTmp != null && row + 1 <= ROW - 2 && table[row + 1][pos.col] == 0) {
						posTmp = (PikachuPos) pikaTmp.getUserData();

						table[row + 1][pos.col] = posTmp.type;
						table[row][posTmp.col] = 0;
						pikaTmp.addModifiertoSequen(new MoveModifier(0.05f, pikaTmp.getX(), pikaTmp.getX(), row * PIKA_SIZE + PADDING_TOP, (row + 1)
								* PIKA_SIZE + PADDING_TOP));
						posTmp.row++;
					}
				}
			} else if (pos.move == UP) {
				for (int row = 1; row < ROW - 1; row++) {
					pikaTmp = getPikaByTable(row, pos.col);
					if (pikaTmp != null && row - 1 >= 1 && table[row - 1][pos.col] == 0) {
						posTmp = (PikachuPos) pikaTmp.getUserData();

						table[row - 1][pos.col] = posTmp.type;
						table[row][posTmp.col] = 0;
						pikaTmp.addModifiertoSequen(new MoveModifier(0.1f, pikaTmp.getX(), pikaTmp.getX(), row * PIKA_SIZE + PADDING_TOP, (row - 1)
								* PIKA_SIZE + PADDING_TOP));
						posTmp.row--;

					}
				}
			}
		}

		Vector<Pika> vP = getAllPika();
		for (int i = 0; i < vP.size(); i++) {
			vP.get(i).startSequen();
		}
	}

	public void setEffectCol(int col, int move) {
		Vector<Pika> vPika = getAllPika();
		int length = vPika.size();
		Pika pika;
		PikachuPos pos;

		for (int i = 0; i < length; i++) {
			pika = vPika.get(i);
			pos = (PikachuPos) pika.getUserData();
			if (pos.col == col)
				pos.move = move;
		}
	}

	public void setEffectRow(int row, int move) {
		Vector<Pika> vPika = getAllPika();
		int length = vPika.size();
		Pika pika;
		PikachuPos pos;

		for (int i = 0; i < length; i++) {
			pika = vPika.get(i);
			pos = (PikachuPos) pika.getUserData();
			if (pos.row == row)
				pos.move = move;
		}
	}

	public void addItemToScene(final BaseGameActivity game) {

		Sprite board = new Sprite(7, 92, Pikachu.getPikachu().textureItemBoard, game.getVertexBufferObjectManager());

		attachChild(board);

		btnRandom = new MButton(11, 25, Pikachu.getPikachu().random[1], game.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
					if (!useItemRandom) {
						useItemRandom = true;
						Sprite tmp = new Sprite(0, 0, Pikachu.getPikachu().random[0], PikaPlayScene.this.game.getVertexBufferObjectManager());
						tmp.setTag(99);
						attachChild(tmp);
						reGenMap();
						aniCombo.setVisible(false);

					}
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};

		registerTouchArea(btnRandom);
		btnRandom.setZIndex(9);
		board.attachChild(btnRandom);

		btnBoom = new MButton(11, 110, Pikachu.getPikachu().boom[1], game.getVertexBufferObjectManager()) {

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
					if (!useItemBoom) {
						useItemBoom = true;
						Sprite tmp = new Sprite(0, 0, Pikachu.getPikachu().boom[0], PikaPlayScene.this.game.getVertexBufferObjectManager());
						tmp.setTag(99);
						attachChild(tmp);
						useBoom = true;
					}
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};

		registerTouchArea(btnBoom);
		board.attachChild(btnBoom);

		btnLockTime = new MButton(11, 195, Pikachu.getPikachu().lockTime[1], game.getVertexBufferObjectManager()) {

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
					if (!useItemClock) {

						final Sprite wave = new Sprite(0, Pikachu.CAMERA_HEIGHT, Pikachu.getPikachu().wave, game.getVertexBufferObjectManager());
						MoveYModifier move = new MoveYModifier(1f, Pikachu.CAMERA_HEIGHT, -300) {
							@Override
							protected void onModifierFinished(IEntity pItem) {
								wave.setVisible(false);
								PikaPlayScene.this.detachChild(wave);

								Vector<Pika> vPikas = getPikasByType(PIKA_BOOM);

								if (vPikas.size() > 0) {
									for (int i = 0; i < vPikas.size(); i++) {
										Pika mPika = vPikas.get(i);
										if (!mPika.addIce) {
											mPika.addIce = true;
											final ScaleModifier mScale = new ScaleModifier(0.2f, 2, 1);

											AnimatedSprite aniIce = new AnimatedSprite(0, 0, PIKA_SIZE, PIKA_SIZE,
													Pikachu.getPikachu().tiledEffectWater, PikaPlayScene.this.game.getVertexBufferObjectManager());
											aniIce.registerEntityModifier(mScale);
											aniIce.animate(100);
											mPika.attachChild(aniIce);
										}
									}
								} else {
									final Text mLevel = new Text(Pikachu.CAMERA_WIDTH / 2 - 100, Pikachu.CAMERA_HEIGHT / 2,
											Pikachu.getPikachu().mStrokeFont, "NO BOOM", 10, game.getVertexBufferObjectManager());
									MoveYModifier move = new MoveYModifier(0.5f, mLevel.getY(), mLevel.getY() - 30);
									ScaleModifier scale = new ScaleModifier(0.5f, 1, 1.2f);
									AlphaModifier alpha = new AlphaModifier(0.5f, 1, 0.7f){
										@Override
										protected void onModifierFinished(IEntity pItem) {
											mLevel.setVisible(false);
											mLevel.detachSelf();
											super.onModifierFinished(pItem);
										}
									};
									mLevel.registerEntityModifier(move);
									mLevel.registerEntityModifier(scale);
									mLevel.registerEntityModifier(alpha);
									PikaPlayScene.this.attachChild(mLevel);

								}
								super.onModifierFinished(pItem);
							}
						};
						wave.registerEntityModifier(move);
						PikaPlayScene.this.attachChild(wave);

						useItemClock = true;
						Sprite tmp = new Sprite(0, 0, Pikachu.getPikachu().lockTime[0], PikaPlayScene.this.game.getVertexBufferObjectManager());
						tmp.setTag(99);
						attachChild(tmp);
						useLockTime = true;

					}
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};

		registerTouchArea(btnLockTime);
		board.attachChild(btnLockTime);

		MButton btnStop = new MButton(11, 296, Pikachu.getPikachu().textureStop, game.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {

				if (pSceneTouchEvent.isActionDown()) {
					showPopUpPause();
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		registerTouchArea(btnStop);
		board.attachChild(btnStop);
	}

	public int getStringWidth(String mStr, IFont font) {
		int w = 0;
		for (int i = 0; i < mStr.length(); i++) {
			w += font.getLetter(mStr.charAt(i)).mWidth;
		}
		return w;
	}

	public Scene onCreateScene(BaseGameActivity game, Camera mCamera) {
		// CustomScene sceneGame = new CustomScene();

		this.game = game;
		this.mCamera = mCamera;
		setBackground(new Background(Color.GREEN));

		Sprite bg = new Sprite(0, 0, Pikachu.getPikachu().background, game.getVertexBufferObjectManager());
		attachChild(bg);

		Sprite bgTop = new Sprite(115, 0, Pikachu.getPikachu().regionTopGameBG, game.getVertexBufferObjectManager());
		attachChild(bgTop);

		mLevel = new Text(43 - getStringWidth("00", Pikachu.getPikachu().mStrokeFont) / 2,
				40 - Pikachu.getPikachu().mStrokeFont.getLineHeight() / 2 + 15, Pikachu.getPikachu().mStrokeFont, Pikachu.LVL + "", 10,
				game.getVertexBufferObjectManager()) {
			@Override
			public void setText(CharSequence pText) throws OutOfCharactersException {
				if (pText.toString().length() < 2)
					pText = "0" + pText;
				super.setText(pText);
			}
		};
		// mLevel.setText(Pikachu.LVL + "");
		mLevel.setScale(0.5f);
		// mLevel.setTextOptions(new )
		// TextOptions t = new TextOptions(HorizontalAlign.CENTER);

		Sprite levelIcon = new Sprite(16, 6, Pikachu.getPikachu().textureLevelIcon, game.getVertexBufferObjectManager());
		levelIcon.attachChild(mLevel);
		attachChild(levelIcon);

		mScore = new Text(130, 12, Pikachu.getPikachu().fontTektonProScore20, "" + score, 1000, new TextOptions(HorizontalAlign.LEFT),
				game.getVertexBufferObjectManager()) {
			// @Override
			// public void setText(CharSequence pText) throws
			// OutOfCharactersException {
			// setX(115- getStringWidth(pText.toString(),
			// fontTektonProScore80));
			// super.setText(pText);
			// }
		};
		attachChild(mScore);

		registerUpdateHandler(new TimerHandler(1, true, new ITimerCallback() {

			int cnt = 0;
			int countIdie = 0;

			public void onTimePassed(TimerHandler pTimerHandler) {
				if (cnt == cntDone) {
					countIdie++;
					if (countIdie == TIP_TIME) {
						tip();
						countIdie = TIP_TIME - 2;
					}
				} else {
					cnt = cntDone;
					countIdie = 0;
				}
			}
		}));

		addItemToScene(game);

		fire = new Sprite(0, 0, Pikachu.getPikachu().textFire, game.getVertexBufferObjectManager());
		fire.setVisible(false);
		attachChild(fire);
		System.out.println(Pikachu.getPikachu().tiledEffectBom.getWidth(0) + " - " + Pikachu.getPikachu().tiledEffectBom.getWidth(0) * perSize);
		aniCombo = new AnimatedSprite(0, 0, Pikachu.getPikachu().tiledEffectCombo, game.getVertexBufferObjectManager());
		aniCombo.setVisible(false);
		aniCombo.animate(70);
		attachChild(aniCombo);

		setTouchAreaBindingOnActionMoveEnabled(true);

		proTime = new ProgressBar(mCamera, 435, 12, Pikachu.getPikachu().bgBarTime, Pikachu.getPikachu().bgBarTimeProcess,
				game.getVertexBufferObjectManager());

		attachChild(proTime);

		proTimeCombo = new ProgressBar(mCamera, 671, 12, Pikachu.getPikachu().bgBarCombo, Pikachu.getPikachu().bgBarComboProcess,
				game.getVertexBufferObjectManager());

		attachChild(proTimeCombo);

		Sprite sCombo = new Sprite(696, 12, Pikachu.getPikachu().textCombo, game.getVertexBufferObjectManager());
		attachChild(sCombo);

		if (Pikachu.showTutorial) {
			showTutorial();
		} else {
			genMap();
		}
		return this;
	}

	private void showTutorial() {
		final PopupScene pop = new PopupScene(PikaPlayScene.this.mCamera, PikaPlayScene.this, 0);

		MoveModifier move = new MoveModifier(0.3f, 0, 0, -Pikachu.getPikachu().regionBangDiem.getHeight(), 0, EaseQuadOut.getInstance());
		Sprite bg = new Sprite(0, 0, Pikachu.getPikachu().regionBangDiem, PikaPlayScene.this.game.getVertexBufferObjectManager());
		pop.registerEntityModifier(move);
		pop.attachChild(bg);
		final Text mStep = new Text(145, 56, Pikachu.getPikachu().fontTektonProWhite35, currentStep + "/" + totalStep,
				game.getVertexBufferObjectManager());
		pop.attachChild(mStep);

		final Entity content = new Entity(0, 0);
		pop.attachChild(content);
		setContent(content, pop);

		Text mText = new Text(500, 56, Pikachu.getPikachu().fontTektonProWhite35, game.getString(R.string.info1), game.getVertexBufferObjectManager());
		pop.attachChild(mText);
		MButton btnBack = new MButton(145, 200, Pikachu.getPikachu().textureBack, game.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					currentStep--;
					if (currentStep < 1)
						currentStep = totalStep;
					mStep.setText(currentStep + "/" + totalStep);
					setContent(content, pop);
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}

		};
		pop.attachChild(btnBack);
		pop.registerTouchArea(btnBack);

		MButton btnRetry = new MButton(375, 338, Pikachu.getPikachu().textureBack, game.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				MoveYModifier moveY = new MoveYModifier(0.3f, pop.getY(), -Pikachu.getPikachu().regionBangDiem.getHeight(),
						EaseBackInOut.getInstance()) {
					@Override
					protected void onModifierFinished(IEntity pItem) {
						PikaPlayScene.this.clearChildScene();
						PikaSaveGamer.saveSoundGame(PikaSaveGamer.KEY_TUTORIAL, false);
						Pikachu.showTutorial = PikaSaveGamer.getSound(PikaSaveGamer.KEY_TUTORIAL);
						genMap();
						super.onModifierFinished(pItem);
					}
				};
				pop.registerEntityModifier(moveY);
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		pop.attachChild(btnRetry);
		pop.registerTouchArea(btnRetry);

		MButton btnNext = new MButton(600, 200, Pikachu.getPikachu().textureNext, game.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					currentStep++;
					if (currentStep > totalStep)
						currentStep = 1;
					mStep.setText(currentStep + "/" + totalStep);
					setContent(content, pop);
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		pop.attachChild(btnNext);
		pop.registerTouchArea(btnNext);
	}

	private void setContent(final Entity content, final PopupScene pop) {
		content.detachChildren();
		int pX = 254;
		int pY = 230;
		final int size = 90;
		if (currentStep == 1) {
			Pika p = new Pika(pX, pY, Pikachu.getPikachu().texturePikachu[0], game.getVertexBufferObjectManager());
			content.attachChild(p);
			Pika p1 = new Pika(pX + size, pY, Pikachu.getPikachu().texturePikachu[1], game.getVertexBufferObjectManager());
			content.attachChild(p1);
			Pika p2 = new Pika(pX + size, pY - size, Pikachu.getPikachu().texturePikachu[2], game.getVertexBufferObjectManager());
			content.attachChild(p2);
			Pika p3 = new Pika(pX + size * 2, pY, Pikachu.getPikachu().texturePikachu[0], game.getVertexBufferObjectManager());
			content.attachChild(p3);

			MoveModifier move1 = new MoveModifier(1, pX + size / 2, pX + size / 2, pY - 10, pY - size - 10) {
				@Override
				protected void onSetValues(IEntity pEntity, float pPercentageDone, float pX, float pY) {
					super.onSetValues(pEntity, pPercentageDone, pX, pY);
					Line line1 = new Line(getFromValueA(), getFromValueB(), pX, pY, 5, game.getVertexBufferObjectManager());
					content.attachChild(line1);
				}

				@Override
				protected void onModifierFinished(IEntity pItem) {
					Text mT = new Text(getFromValueA() - 30, getFromValueB() - size / 2, Pikachu.getPikachu().fontTektonProScore20, "1",
							game.getVertexBufferObjectManager());
					content.attachChild(mT);
					super.onModifierFinished(pItem);
				}
			};

			MoveModifier move2 = new MoveModifier(1, pX + size / 2, pX + size * 2 + size / 2, pY - size - 10, pY - size - 10) {
				@Override
				protected void onSetValues(IEntity pEntity, float pPercentageDone, float pX, float pY) {
					super.onSetValues(pEntity, pPercentageDone, pX, pY);
					Line line1 = new Line(getFromValueA(), getFromValueB(), pX, pY, 5, game.getVertexBufferObjectManager());
					content.attachChild(line1);
				}

				@Override
				protected void onModifierFinished(IEntity pItem) {
					Text mT = new Text(getFromValueA() + size, getFromValueB() - 33, Pikachu.getPikachu().fontTektonProScore20, "2",
							game.getVertexBufferObjectManager());
					content.attachChild(mT);
					super.onModifierFinished(pItem);
				}
			};

			MoveModifier move3 = new MoveModifier(1, pX + size * 2 + size / 2, pX + size * 2 + size / 2, pY - size - 10, pY - 10) {
				@Override
				protected void onSetValues(IEntity pEntity, float pPercentageDone, float pX, float pY) {
					super.onSetValues(pEntity, pPercentageDone, pX, pY);
					Line line1 = new Line(getFromValueA(), getFromValueB(), pX, pY, 5, game.getVertexBufferObjectManager());
					content.attachChild(line1);
				}

				@Override
				protected void onModifierFinished(IEntity pItem) {
					Text mT = new Text(getFromValueA() + 5, getFromValueB() + size / 2 - 5, Pikachu.getPikachu().fontTektonProScore20, "3",
							game.getVertexBufferObjectManager());
					content.attachChild(mT);
					super.onModifierFinished(pItem);
				}
			};

			SequenceEntityModifier squen = new SequenceEntityModifier(move1, move2, move3);
			Entity tmp = new Entity();
			tmp.registerEntityModifier(squen);
			content.attachChild(tmp);

		} else if (currentStep == 2) {

			Pika p2 = new Pika(pX + size, pY - size, Pikachu.getPikachu().texturePikachu[0], game.getVertexBufferObjectManager());
			final ScaleModifier mScale = new ScaleModifier(0.2f, 2, 1);

			AnimatedSprite aniIce = new AnimatedSprite(0, 0, Pikachu.getPikachu().tiledEffectIce,
					PikaPlayScene.this.game.getVertexBufferObjectManager());

			aniIce.animate(100);
			aniIce.registerEntityModifier(mScale);
			p2.attachChild(aniIce);
			
			content.attachChild(p2);
			Text mT = new TickerText(pX, pY + 30, Pikachu.getPikachu().fontTektonProWhite35, game.getString(R.string.info2), new TickerTextOptions(30),
					game.getVertexBufferObjectManager());
			content.attachChild(mT);
		} else if (currentStep == 3) {
			final Pika pika2 = new Pika(pX + size, pY - size, Pikachu.getPikachu().texturePikachu[0], game.getVertexBufferObjectManager()) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
					if (pSceneTouchEvent.isActionDown()) {
						flipUp();
					}
					return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
				}
			};
			content.attachChild(pika2);
			Text mT = new TickerText(pX, pY + 30, Pikachu.getPikachu().fontTektonProWhite35, game.getString(R.string.info3), new TickerTextOptions(
					30), game.getVertexBufferObjectManager());
			content.attachChild(mT);
			pika2.setHidden(new Sprite(0, 0, Pikachu.getPikachu().texturePikachu[19], game.getVertexBufferObjectManager()));
			pop.registerTouchArea(pika2);
		} else if (currentStep == 4) {
			final Pika pika2 = new Pika(pX + size, pY - size, Pikachu.getPikachu().texturePikachu[18], game.getVertexBufferObjectManager());
			content.attachChild(pika2);

			final Text time = new Text(17, 17, Pikachu.getPikachu().fontTektonProRed, "10", new TextOptions(HorizontalAlign.CENTER),
					game.getVertexBufferObjectManager());
			time.setScale(0.5f);
			pika2.attachChild(time);

			Text mT = new TickerText(pX, pY + 30, Pikachu.getPikachu().fontTektonProWhite35, game.getString(R.string.info4),
					new TickerTextOptions(30), game.getVertexBufferObjectManager());
			content.attachChild(mT);

		} else if (currentStep == 5) {
			Sprite board = new Sprite(pX, 80, Pikachu.getPikachu().textureItemBoard, game.getVertexBufferObjectManager());
			content.attachChild(board);

			MButton btnRandom = new MButton(17, 26, Pikachu.getPikachu().random[1], game.getVertexBufferObjectManager());
			board.attachChild(btnRandom);

			MButton btnBoom = new MButton(17, 105, Pikachu.getPikachu().boom[1], game.getVertexBufferObjectManager());
			board.attachChild(btnBoom);

			MButton btnLockTime = new MButton(17, 185, Pikachu.getPikachu().lockTime[1], game.getVertexBufferObjectManager());
			board.attachChild(btnLockTime);

			Text mText = new Text(90, 30, Pikachu.getPikachu().fontTektonProWhite35, game.getString(R.string.info5),
					game.getVertexBufferObjectManager());
			board.attachChild(mText);
		}
	}

	public void showPopUpPause() {
		Pikachu.getPikachu().detectView(Pikachu.SHOW_ADS);
		final PopupScene pop = new PopupScene(PikaPlayScene.this.mCamera, PikaPlayScene.this, 0);
		Sprite spr = new Sprite(300, 10, Pikachu.getPikachu().regionMission, game.getVertexBufferObjectManager());
		MoveModifier move = new MoveModifier(0.3f, 0, 0, -Pikachu.getPikachu().regionBangDiem.getHeight(), 0, EaseQuadOut.getInstance());
		Sprite bg = new Sprite(0, 0, Pikachu.getPikachu().regionBangDiem, PikaPlayScene.this.game.getVertexBufferObjectManager());
		pop.registerEntityModifier(move);
		pop.attachChild(bg);
		pop.attachChild(spr);
		if (Pikachu.getPikachu().game_mode == Pikachu.GAME_CLASSIC) {
			Text myLevel = new Text(168, 111, Pikachu.getPikachu().mStrokeFontYellow, "Level " + Pikachu.LVL, game.getVertexBufferObjectManager());
			Text mMission = new TickerText(168, 168, Pikachu.getPikachu().mStrokeFont, "Score : " + dataMap.point, new TickerTextOptions(30),
					game.getVertexBufferObjectManager());

			pop.attachChild(myLevel);
			pop.attachChild(mMission);
		} else {

			Text myLevel = new Text(168, 111, Pikachu.getPikachu().mStrokeFontYellow, game.getString(R.string.crazy_mode), game.getVertexBufferObjectManager());

			Text mMission = new TickerText(168, 168, Pikachu.getPikachu().mStrokeFont, "Pausing ...\nHigh score : "
					+ Pikachu.HIGH_SCORE, new TickerTextOptions(30), game.getVertexBufferObjectManager());

			pop.attachChild(myLevel);
			pop.attachChild(mMission);
		}
		float x = 206, y = 235;
		int typeMiss = 5;
		int countMiss = 1;
		Random r = new Random();
		typeMiss = r.nextInt(PIKA - 1) + 1;
		for (int i = 0; i < vPikaMiss.size(); i++) {
			typeMiss = vPikaMiss.elementAt(i).type;
			countMiss = vPikaMiss.elementAt(i).count;
			if (typeMiss == 0 || countMiss == 0)
				continue;
			PikaMission miss1 = new PikaMission(x, y, Pikachu.getPikachu().texturePikachu[typeMiss - 1], game.getVertexBufferObjectManager());
			final Text time = new Text(103, 32, Pikachu.getPikachu().fontTektonProWhite35, "x" + countMiss, new TextOptions(HorizontalAlign.LEFT),
					game.getVertexBufferObjectManager());
			miss1.setMission(countMiss, typeMiss, time);
			pop.attachChild(miss1);
			x += 158;
		}

		MButton btnBack = new MButton(278, 338, Pikachu.getPikachu().textureBack, game.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				MoveYModifier moveY = new MoveYModifier(0.3f, pop.getY(), -Pikachu.getPikachu().regionBangDiem.getHeight(),
						EaseBackInOut.getInstance()) {
					@Override
					protected void onModifierFinished(IEntity pItem) {
						if (Pikachu.getPikachu().game_mode == Pikachu.GAME_CLASSIC) {
							Pikachu.getPikachu().changeScene(SceneType.LEVEL);
						} else {
							Pikachu.getPikachu().changeScene(SceneType.SELECT_GAME);
						}
						super.onModifierFinished(pItem);
					}
				};
				pop.registerEntityModifier(moveY);
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		pop.attachChild(btnBack);
		pop.registerTouchArea(btnBack);

		MButton btnRetry = new MButton(471, 338, Pikachu.getPikachu().textureRetry, game.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				MoveYModifier moveY = new MoveYModifier(0.3f, pop.getY(), -Pikachu.getPikachu().regionBangDiem.getHeight(),
						EaseBackInOut.getInstance()) {
					@Override
					protected void onModifierFinished(IEntity pItem) {
						PikaPlayScene.this.clearChildScene();
						Pikachu.getPikachu().detectView(Pikachu.VISIBLE_ADS);
						super.onModifierFinished(pItem);
					}
				};
				pop.registerEntityModifier(moveY);
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		pop.attachChild(btnRetry);
		pop.registerTouchArea(btnRetry);

	}

	public void showMission() {
		Pikachu.getPikachu().detectView(Pikachu.SHOW_ADS);
		final PopupScene pop = new PopupScene(PikaPlayScene.this.mCamera, PikaPlayScene.this, 0);

		Sprite spr = new Sprite(300, 10, Pikachu.getPikachu().regionMission, game.getVertexBufferObjectManager());

		pop.registerUpdateHandler(new TimerHandler(2, new ITimerCallback() {

			public void onTimePassed(final TimerHandler pTimerHandler) {
				pop.unregisterUpdateHandler(pTimerHandler);
				MoveYModifier moveY = new MoveYModifier(0.3f, pop.getY(), -Pikachu.getPikachu().regionBangDiem.getHeight(), EaseBackInOut
						.getInstance()) {
					@Override
					protected void onModifierFinished(IEntity pItem) {
						PikaPlayScene.this.clearChildScene();
						Pikachu.getPikachu().detectView(Pikachu.VISIBLE_ADS);
						super.onModifierFinished(pItem);
					}
				};

				pop.registerEntityModifier(moveY);
			}
		}));

		MoveModifier move = new MoveModifier(0.3f, 0, 0, -Pikachu.getPikachu().regionBangDiem.getHeight(), 0, EaseQuadOut.getInstance());
		Sprite bg = new Sprite(0, 0, Pikachu.getPikachu().regionBangDiem, PikaPlayScene.this.game.getVertexBufferObjectManager());
		bg.registerEntityModifier(move);
		pop.attachChild(bg);
		pop.attachChild(spr);

		Text myLevel = new Text(168, 111, Pikachu.getPikachu().mStrokeFontYellow, "Level " + Pikachu.LVL, game.getVertexBufferObjectManager());
		Text mMission = new TickerText(168, 168, Pikachu.getPikachu().mStrokeFont, "Score : " + dataMap.point + "\nTime : " + dataMap.time,
				new TickerTextOptions(30), game.getVertexBufferObjectManager());

		pop.attachChild(myLevel);
		pop.attachChild(mMission);

		float x = 206, y = 300;
		int typeMiss = 5;
		int countMiss = 1;
		Random r = new Random();
		typeMiss = r.nextInt(PIKA - 1) + 1;
		for (int i = 0; i < vPikaMiss.size(); i++) {
			typeMiss = vPikaMiss.elementAt(i).type;
			countMiss = vPikaMiss.elementAt(i).count;
			if (typeMiss == 0 || countMiss == 0)
				continue;
			PikaMission miss1 = new PikaMission(x, y, Pikachu.getPikachu().texturePikachu[typeMiss - 1], game.getVertexBufferObjectManager());
			final Text time = new Text(103, 32, Pikachu.getPikachu().fontTektonProWhite35, "x" + countMiss, new TextOptions(HorizontalAlign.LEFT),
					game.getVertexBufferObjectManager());
			miss1.setMission(countMiss, typeMiss, time);
			pop.attachChild(miss1);
			miss1.setCount(miss1.count);
			x += 158;
		}

	}

	public void addEffectTextCombo(float x, float y, int combo) {
		if (combo > 1) {
			float xText = x + 60;
			final Sprite text = new Sprite(xText, y, Pikachu.getPikachu().regionComboText, game.getVertexBufferObjectManager());
			// text.registerEntityModifier(new AlphaModifier(0.5f, 1, 0));
			text.registerEntityModifier(new ScaleModifier(0.3f, 2f, 1f));
			text.registerEntityModifier(new MoveModifier(0.5f, xText, xText, y + 10, y) {
				@Override
				protected void onModifierFinished(IEntity pItem) {
					text.setVisible(false);
					registerUpdateHandler(new TimerHandler(0.5f, new ITimerCallback() {

						public void onTimePassed(TimerHandler pTimerHandler) {
							PikaPlayScene.this.detachChild(text);
						}
					}));
					super.onModifierFinished(pItem);
				}
			});

			final Text t = new Text(x, y, Pikachu.getPikachu().font, "" + combo, new TextOptions(AutoWrap.WORDS, 300, HorizontalAlign.CENTER),
					game.getVertexBufferObjectManager());
			t.registerEntityModifier(new MoveModifier(0.5f, x, x, y, y - 10) {
				@Override
				protected void onModifierFinished(IEntity pItem) {
					t.setVisible(false);
					registerUpdateHandler(new TimerHandler(0.5f, new ITimerCallback() {

						public void onTimePassed(TimerHandler pTimerHandler) {
							PikaPlayScene.this.detachChild(t);
						}
					}));
					super.onModifierFinished(pItem);
				}
			});
			t.registerEntityModifier(new ScaleModifier(0.3f, 2f, 1f));
			// t.registerEntityModifier(new AlphaModifier(0.5f, 1, 0));
			attachChild(t);
			attachChild(text);
		}
	}

	public void addFire(float x, float y) {
		final AnimatedSprite s = new AnimatedSprite(x, y, Pikachu.getPikachu().tiledEffectFire.getWidth(0) * perSize,
				Pikachu.getPikachu().tiledEffectFire.getHeight(0) * perSize, Pikachu.getPikachu().tiledEffectFire,
				game.getVertexBufferObjectManager());

		s.animate(100, false, new IAnimationListener() {

			public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount) {
				// TODO
				// Auto-generated
				// method
				// stub

			}

			public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount) {
				// TODO
				// Auto-generated
				// method
				// stub

			}

			public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex, int pNewFrameIndex) {
				// TODO
				// Auto-generated
				// method
				// stub

			}

			public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
				s.setVisible(false);
				registerUpdateHandler(new TimerHandler(0.5f, new ITimerCallback() {

					public void onTimePassed(TimerHandler pTimerHandler) {
						PikaPlayScene.this.detachChild(s);
					}
				}));
			}
		});
		PikaPlayScene.this.attachChild(s);
		setHighestDepth(s);
	}

	private void youLose() {
		Pikachu.getPikachu().detectView(Pikachu.SHOW_ADS);
		clearMap();
		Pikachu.getPikachu().playSound(Pikachu.SOUND_LOSE);
		final PopupScene pop = new PopupScene(PikaPlayScene.this.mCamera, PikaPlayScene.this, 0);

		Sprite spr = new Sprite(236, 8, Pikachu.getPikachu().regionGameOver, game.getVertexBufferObjectManager());

		MoveModifier move = new MoveModifier(0.3f, 0, 0, -Pikachu.getPikachu().regionBangDiem.getHeight(), 0, EaseQuadOut.getInstance());
		Sprite bg = new Sprite(0, 0, Pikachu.getPikachu().regionBangDiem, PikaPlayScene.this.game.getVertexBufferObjectManager());
		pop.registerEntityModifier(move);
		pop.attachChild(bg);
		pop.attachChild(spr);

		if (Pikachu.getPikachu().game_mode == Pikachu.GAME_CLASSIC) {
			Text myLevel = new Text(168, 111, Pikachu.getPikachu().mStrokeFontYellow, "Level " + Pikachu.LVL, game.getVertexBufferObjectManager());
			Text mMission = new TickerText(168, 168, Pikachu.getPikachu().mStrokeFont, "Score: " + score + "/" + dataMap.point,
					new TickerTextOptions(30), game.getVertexBufferObjectManager());

			pop.attachChild(myLevel);
			pop.attachChild(mMission);
		} else {
			Pikachu.getPikachu().saveHighScore(score);
			Text myLevel = new Text(168, 111, Pikachu.getPikachu().mStrokeFontYellow, "Level " + Pikachu.LVL, game.getVertexBufferObjectManager());
			Text mMission = new TickerText(168, 168, Pikachu.getPikachu().mStrokeFont, "Score: " + score + "\nHigh score : " + Pikachu.HIGH_SCORE,
					new TickerTextOptions(30), game.getVertexBufferObjectManager());
			pop.attachChild(myLevel);
			pop.attachChild(mMission);

		}
		float x = 206, y = 235;
		int typeMiss = 5;
		int countMiss = 1;
		Random r = new Random();
		typeMiss = r.nextInt(PIKA - 1) + 1;
		for (int i = 0; i < vPikaMiss.size(); i++) {
			typeMiss = vPikaMiss.elementAt(i).type;
			countMiss = vPikaMiss.elementAt(i).count;
			if (typeMiss == 0 || countMiss == 0)
				continue;
			PikaMission miss1 = new PikaMission(x, y, Pikachu.getPikachu().texturePikachu[typeMiss - 1], game.getVertexBufferObjectManager());
			final Text time = new Text(103, 32, Pikachu.getPikachu().fontTektonProWhite35, "x" + countMiss, new TextOptions(HorizontalAlign.LEFT),
					game.getVertexBufferObjectManager());
			miss1.setMission(countMiss, typeMiss, time);

			pop.attachChild(miss1);
			x += 158;
		}

		MButton btnBack = new MButton(278, 338, Pikachu.getPikachu().textureBack, game.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				MoveYModifier moveY = new MoveYModifier(0.3f, pop.getY(), -Pikachu.getPikachu().regionBangDiem.getHeight(),
						EaseBackInOut.getInstance()) {
					@Override
					protected void onModifierFinished(IEntity pItem) {
						if (Pikachu.getPikachu().game_mode == Pikachu.GAME_CLASSIC) {
							Pikachu.getPikachu().changeScene(SceneType.LEVEL);
						} else
							Pikachu.getPikachu().changeScene(SceneType.SELECT_GAME);
						super.onModifierFinished(pItem);
					}
				};
				pop.registerEntityModifier(moveY);
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		pop.attachChild(btnBack);
		pop.registerTouchArea(btnBack);

		MButton btnRetry = new MButton(471, 338, Pikachu.getPikachu().textureRetry, game.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				MoveYModifier moveY = new MoveYModifier(0.3f, pop.getY(), -Pikachu.getPikachu().regionBangDiem.getHeight(),
						EaseBackInOut.getInstance()) {
					@Override
					protected void onModifierFinished(IEntity pItem) {
						Pikachu.getPikachu().detectView(Pikachu.VISIBLE_ADS);
						PikaPlayScene.this.clearChildScene();
						score = 0;

						for (int j = 0; j < vPikaMiss.size(); j++) {
							PikaMission miss = vPikaMiss.get(j);
							vPikaMiss.remove(j);
							detachChild(miss);
							if (j > 0)
								j--;
						}
						vPikaMiss = null;

						genMap();

						super.onModifierFinished(pItem);
					}
				};
				pop.registerEntityModifier(moveY);
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		pop.attachChild(btnRetry);
		pop.registerTouchArea(btnRetry);

	}

	@Override
	public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		try {
			return super.onSceneTouchEvent(pSceneTouchEvent);
		} catch (Exception e) {
			return false;
		}
	}

	public Vector<Pika> boomPika(int type) {
		Vector<Pika> vPika = getAllPika();
		Vector<Pika> vPikas = new Vector<Pika>();
		int length = vPika.size();
		// Pika pika;
		for (int i = 0; i < length; i++) {
			final Pika pika = vPika.elementAt(i);
			PikachuPos pos = (PikachuPos) pika.getUserData();
			if (pos.type == type) {
				table[pos.row][pos.col] = 0;

				unregisterTouchArea(pika);
				detachChild(pika);
				effectBoom(pika);
				vPikas.add(pika);
			}
		}
		return vPikas;
	}

	public boolean isClear() {
		boolean check = true;
		Vector<Pika> vPika = getAllPika();
		int length = vPika.size();
		for (int i = 0; i < length; i++) {
			Pika pikachu = vPika.get(i);
			PikachuPos pos = (PikachuPos) pikachu.getUserData();
			if (pos.type != PIKA_NO_MOVE) {
				check = false;
				break;
			}
		}
		if (check) {
			if (clearMission() && score >= dataMap.point) {

				registerUpdateHandler(new TimerHandler(0.8f, new ITimerCallback() {

					public void onTimePassed(TimerHandler pTimerHandler) {
						youWin();
					}
				}));

			} else if (!clearMission()) {
				int tmpScore = score;
				genMap();
				clearChildScene();
				score = tmpScore;
				mScore.setText(score + "");
			} else {
				registerUpdateHandler(new TimerHandler(0.8f, new ITimerCallback() {

					public void onTimePassed(TimerHandler pTimerHandler) {
						// TODO Auto-generated method stub
						youLose();
					}
				}));
			}
		}

		return check;
	}

	private void youWin() {
		clearMap();
		Pikachu.getPikachu().detectView(Pikachu.SHOW_INT);
		if (Pikachu.getPikachu().game_mode == Pikachu.GAME_CLASSIC) {
			final ScreenCapture screenCapture = new ScreenCapture();
			// Pikachu.getPikachu().playSound(Pikachu.SOUND_WIN);
			final PopupScene pop = new PopupScene(PikaPlayScene.this.mCamera, PikaPlayScene.this, 0);
			Sprite spr = new Sprite(285, 10, Pikachu.getPikachu().regionFinish, game.getVertexBufferObjectManager());

			MoveModifier move = new MoveModifier(0.3f, 0, 0, -Pikachu.getPikachu().regionBangDiem.getHeight(), 0, EaseQuadOut.getInstance());
			Sprite bg = new Sprite(0, 0, Pikachu.getPikachu().regionBangDiem, PikaPlayScene.this.game.getVertexBufferObjectManager());
			pop.registerEntityModifier(move);
			pop.attachChild(bg);
			pop.attachChild(spr);
			final MButton btnShare = new MButton(591, 47, Pikachu.getPikachu().regionShareFace, game.getVertexBufferObjectManager()) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
					if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
						setVisible(false);
						final int viewWidth = Pikachu.getPikachu().getRenderSurfaceView().getWidth();
						final int viewHeight = Pikachu.getPikachu().getRenderSurfaceView().getHeight();

						screenCapture.capture(viewWidth, viewHeight,
								FileUtils.getAbsolutePathOnExternalStorage(Pikachu.getPikachu(), "Screen_" + System.currentTimeMillis() + ".png"),
								new IScreenCaptureCallback() {

									public void onScreenCaptured(final String pFilePath) {
										Pikachu.getPikachu().runOnUiThread(new Runnable() {

											public void run() {
												// Toast.makeText(Pikachu.getPikachu(),
												// "Screenshot: " + pFilePath +
												// " taken!",
												// Toast.LENGTH_SHORT).show();
											}
										});
									}

									public void onScreenCaptureFailed(final String pFilePath, final Exception pException) {
										Pikachu.getPikachu().runOnUiThread(new Runnable() {

											public void run() {
												// pException.printStackTrace();
												// Toast.makeText(Pikachu.getPikachu(),
												// pException.toString() +
												// "FAILED capturing Screenshot: "
												// + pFilePath + " !",
												// Toast.LENGTH_SHORT).show();
											}
										});
									}

									public void onScreenCaptured(Bitmap pBitmap) {
										// System.out.println(pBitmap);
										// screenCapture.dispose();
										Pikachu.getPikachu().captureImage(pBitmap);
									}
								});

					}
					return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
				}
			};

			Text myLevel = new Text(168, 111, Pikachu.getPikachu().mStrokeFontYellow, "Level " + Pikachu.LVL, game.getVertexBufferObjectManager());
			Text mMission = new TickerText(168, 168, Pikachu.getPikachu().mStrokeFont, "Score : " + score, new TickerTextOptions(30),
					game.getVertexBufferObjectManager());

			pop.attachChild(myLevel);
			pop.attachChild(mMission);

			float x = 306, y = 245;

			for (int i = 0; i < 3; i++) {
				Sprite star0 = new Sprite(x, y, Pikachu.getPikachu().regionStar0, game.getVertexBufferObjectManager());
				pop.attachChild(star0);
				float point = dataMap.point;
				if (i == 1) {
					point = dataMap.point2;
				}

				if (i == 2) {
					point = dataMap.point3;
				}

				if (score >= point) {

					Pikachu.getPikachu().editLevelData(Pikachu.LVL - 1, i + PikaSaveGamer.LV_1_STAR);

					final Sprite star1 = new Sprite(x - 6, y - 5, Pikachu.getPikachu().regionStar1, game.getVertexBufferObjectManager());
					pop.registerUpdateHandler(new TimerHandler(0.3f + (i * 0.2f), new ITimerCallback() {

						public void onTimePassed(TimerHandler pTimerHandler) {
							Pikachu.getPikachu().playSound(Pikachu.SOUND_STAR);
							ScaleModifier scale = new ScaleModifier(0.2f, 1.5f, 1);
							star1.registerEntityModifier(scale);
							pop.attachChild(star1);
						}
					}));
				}

				x += 66;
			}

			Pikachu.getPikachu().editLevelData(Pikachu.LVL, PikaSaveGamer.LV_UNLOCK);

			MButton btnBack = new MButton(278, 338, Pikachu.getPikachu().textureBack, game.getVertexBufferObjectManager()) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
					MoveYModifier moveY = new MoveYModifier(0.3f, pop.getY(), -Pikachu.getPikachu().regionBangDiem.getHeight(),
							EaseBackInOut.getInstance()) {
						@Override
						protected void onModifierFinished(IEntity pItem) {
							Pikachu.getPikachu().changeScene(SceneType.LEVEL);
							super.onModifierFinished(pItem);
						}
					};
					pop.registerEntityModifier(moveY);
					return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
				}
			};
			pop.attachChild(btnBack);
			pop.registerTouchArea(btnBack);

			MButton btnRetry = new MButton(375, 338, Pikachu.getPikachu().textureRetry, game.getVertexBufferObjectManager()) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
					MoveYModifier moveY = new MoveYModifier(0.3f, pop.getY(), -Pikachu.getPikachu().regionBangDiem.getHeight(),
							EaseBackInOut.getInstance()) {
						@Override
						protected void onModifierFinished(IEntity pItem) {
							Pikachu.getPikachu().detectView(Pikachu.VISIBLE_ADS);
							PikaPlayScene.this.clearChildScene();
							genMap();
							super.onModifierFinished(pItem);
						}
					};
					pop.registerEntityModifier(moveY);
					return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
				}
			};
			pop.attachChild(btnRetry);
			pop.registerTouchArea(btnRetry);

			MButton btnNext = new MButton(471, 338, Pikachu.getPikachu().textureNext, game.getVertexBufferObjectManager()) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
					MoveYModifier moveY = new MoveYModifier(0.3f, pop.getY(), -Pikachu.getPikachu().regionBangDiem.getHeight(),
							EaseBackInOut.getInstance()) {
						@Override
						protected void onModifierFinished(IEntity pItem) {
							Pikachu.LVL++;
							if (Pikachu.LVL > Pikachu.LVL_MAX)
								Pikachu.LVL = 1;
							Pikachu.getPikachu().detectView(Pikachu.VISIBLE_ADS);
							PikaPlayScene.this.clearChildScene();
							genMap();
							super.onModifierFinished(pItem);
						}
					};
					pop.registerEntityModifier(moveY);
					return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
				}
			};
			pop.attachChild(btnNext);
			pop.registerTouchArea(btnNext);
			pop.registerUpdateHandler(new TimerHandler(1f, new ITimerCallback() {

				public void onTimePassed(TimerHandler pTimerHandler) {
					pop.attachChild(btnShare);
					btnShare.setUserData(BTN_SHARE);
					pop.registerTouchArea(btnShare);
					pop.attachChild(screenCapture);
					ScaleModifier mScale1 = new ScaleModifier(0.25f, 1, 1.1f);
					ScaleModifier mScale2 = new ScaleModifier(0.25f, 1.1f, 1f);
					SequenceEntityModifier sequen = new SequenceEntityModifier(mScale1, mScale2);

					LoopEntityModifier loopScale = new LoopEntityModifier(sequen);
					btnShare.registerEntityModifier(loopScale);

				}
			}));
			btnShare.setOnClickListener(this);
		} else
			genMap();

	}

	public void addPoint(PointF p) {

		// if (p.y > CAMERA_HEIGHT)
		// p.y = ROW * PIKA_SIZE;
		boolean check = false;
		for (int i = 0; i < vPos.size(); i++) {

			if (vPos.get(i).x == p.x && vPos.get(i).y == p.y)
				check = true;
		}
		if (!check) {
			// System.out.println("new point " + p.x + " - " + p.y);
			vPos.add(p);
		}

	}

	@Override
	protected void onManagedDraw(GLState pGLState, Camera pCamera) {
		try {
			super.onManagedDraw(pGLState, pCamera);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		try {
			super.onManagedUpdate(pSecondsElapsed);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public boolean pikachuConnect(Sprite pika1, Sprite pika2) {
		boolean check = false;
		PikachuPos pos1 = (PikachuPos) pika1.getUserData();
		PikachuPos pos2 = (PikachuPos) pika2.getUserData();

		vPos = new Vector<PointF>();

		if ((pos1.col == pos2.col && pos1.row == pos2.row) || (pos2.type == PIKA_NO_MOVE || pos1.type == PIKA_NO_MOVE))
			return false;
		if (pos1.type == pos2.type) {
			// System.out.println("pos1 " + pos1.row + " - " + pos1.col);
			// System.out.println("pos2 " + pos2.row + " - " + pos2.col);
			int startRow = pos1.row;
			int endRow = pos2.row;
			int startCol = pos1.col;
			int endCol = pos2.col;
			if (startRow > endRow) {
				startRow = pos2.row;
				endRow = pos1.row;
			}

			if (startCol > endCol) {
				startCol = pos2.col;
				endCol = pos1.col;
			}
			// System.out.println("start " + startRow + " - " + startCol);
			// System.out.println("end " + endRow + " - " + endCol);
			// d�?c
			// tìm từ đầu - > cuối
			// System.out.println("doc ====================================");
			int maxLeng = 1000;
			int colFix = -1;
			for (int col = 0; col < COL; col++) {
				boolean checkCol = false;
				boolean checkRow = false;
				// System.out.println("----------------->");

				for (int row = startRow + 1; row < endRow; row++) {
					if (table[row][col] != 0) {
						checkCol = true;
					}
					// System.out.println(table[row][col]);
				}
				// System.out.println("----------------->");
				if (!checkCol) {

					int tmpLeng = Math.abs(col - pos1.col) + Math.abs(col - pos2.col);

					if (col > pos1.col) {
						for (int i = pos1.col + 1; i <= col; i++) {
							if (table[pos1.row][i] != 0)
								checkRow = true;
						}
					} else {
						for (int i = col; i < pos1.col; i++) {
							if (table[pos1.row][i] != 0)
								checkRow = true;
						}
					}

					if (col > pos2.col) {
						for (int i = pos2.col + 1; i <= col; i++) {
							if (table[pos2.row][i] != 0)
								checkRow = true;
						}
					} else {
						for (int i = col; i < pos2.col; i++) {
							if (table[pos2.row][i] != 0)
								checkRow = true;
						}
					}

					if (!checkRow && tmpLeng < maxLeng) {
						maxLeng = tmpLeng;
						colFix = col;
					}
				}

			}

			if (colFix != -1 && pos1.row != pos2.row) {
				check = true;

				PikachuPos line1 = new PikachuPos(startRow, colFix, pos1.type, pos1.move);
				PikachuPos line2 = new PikachuPos(endRow, colFix, pos1.type, pos1.move);

				addPoint(new PointF(pika1.getX() + (PIKA_SIZE / 2), pika1.getY() + (PIKA_SIZE / 2)));
				if (startRow == pos1.row) {
					addPoint(new PointF(line1.col * PIKA_SIZE + (PIKA_SIZE / 2) + PADDING_LEFT, line1.row * PIKA_SIZE + (PIKA_SIZE / 2) + PADDING_TOP));
					addPoint(new PointF(line1.col * PIKA_SIZE + (PIKA_SIZE / 2) + PADDING_LEFT, line2.row * PIKA_SIZE + (PIKA_SIZE / 2) + PADDING_TOP));
				} else {
					addPoint(new PointF(line1.col * PIKA_SIZE + (PIKA_SIZE / 2) + PADDING_LEFT, line2.row * PIKA_SIZE + (PIKA_SIZE / 2) + PADDING_TOP));
					addPoint(new PointF(line1.col * PIKA_SIZE + (PIKA_SIZE / 2) + PADDING_LEFT, line1.row * PIKA_SIZE + (PIKA_SIZE / 2) + PADDING_TOP));
				}
				addPoint(new PointF(pika2.getX() + (PIKA_SIZE / 2), pika2.getY() + (PIKA_SIZE / 2)));

			}
			// System.out.println("===============>");
			// ngang

			if (check)
				return check;
			// System.out.println("ngang ====================================");
			maxLeng = 1000;
			int rowFix = -1;
			for (int row = 0; row < ROW; row++) {
				boolean checkCol = false;
				boolean checkRow = false;
				// System.out.println("----------------->");
				for (int col = startCol + 1; col < endCol; col++) {
					if (table[row][col] != 0) {
						checkRow = true;
					}
					// System.out.println(table[row][col]);
				}
				// System.out.println("----------------->");
				if (!checkRow) {

					int tmpLeng = Math.abs(row - pos1.row) + Math.abs(row - pos2.row);

					if (row > pos1.row) {
						for (int i = pos1.row + 1; i <= row; i++) {
							if (table[i][pos1.col] != 0)
								checkCol = true;
						}
					} else {
						for (int i = row; i < pos1.row; i++) {
							if (table[i][pos1.col] != 0)
								checkCol = true;
						}
					}

					if (row > pos2.row) {
						for (int i = pos2.row + 1; i <= row; i++) {
							if (table[i][pos2.col] != 0)
								checkCol = true;
						}
					} else {
						for (int i = row; i < pos2.row; i++) {
							if (table[i][pos2.col] != 0)
								checkCol = true;
						}
					}

					if (!checkCol && tmpLeng < maxLeng) {
						maxLeng = tmpLeng;
						rowFix = row;
					}
				}

			}

			if (rowFix != -1) {
				check = true;

				PikachuPos line1 = new PikachuPos(rowFix, startCol, pos1.type, pos1.move);
				PikachuPos line2 = new PikachuPos(rowFix, endCol, pos1.type, pos1.move);

				addPoint(new PointF(pika1.getX() + (PIKA_SIZE / 2), pika1.getY() + (PIKA_SIZE / 2)));
				if (startCol == pos1.col) {
					addPoint(new PointF(line1.col * PIKA_SIZE + (PIKA_SIZE / 2) + PADDING_LEFT, line1.row * PIKA_SIZE + (PIKA_SIZE / 2) + PADDING_TOP));
					addPoint(new PointF(line2.col * PIKA_SIZE + (PIKA_SIZE / 2) + PADDING_LEFT, line1.row * PIKA_SIZE + (PIKA_SIZE / 2) + PADDING_TOP));
				} else {
					addPoint(new PointF(line2.col * PIKA_SIZE + (PIKA_SIZE / 2) + PADDING_LEFT, line1.row * PIKA_SIZE + (PIKA_SIZE / 2) + PADDING_TOP));
					addPoint(new PointF(line1.col * PIKA_SIZE + (PIKA_SIZE / 2) + PADDING_LEFT, line1.row * PIKA_SIZE + (PIKA_SIZE / 2) + PADDING_TOP));
				}
				addPoint(new PointF(pika2.getX() + (PIKA_SIZE / 2), pika2.getY() + (PIKA_SIZE / 2)));

			}
			// System.out.println("===============>");
		} else {
			// System.out.println("Không giống nhau " + pos1.type + " - "
			// + pos2.type);

		}
		if ((pos1.col == pos2.col && pos1.row == pos2.row))
			check = true;
		return check;
	}

	private void effectBoom(final Pika pika1) {
		final AnimatedSprite bom = new AnimatedSprite(pika1.getX(), pika1.getY(), Pikachu.getPikachu().tiledEffectBom,
				game.getVertexBufferObjectManager()) {

		};

		bom.animate(150, false, new IAnimationListener() {

			public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount) {
				// TODO
				// Auto-generated
				// method stub

			}

			public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount) {
				// TODO
				// Auto-generated
				// method stub

			}

			public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex, int pNewFrameIndex) {
				// TODO
				// Auto-generated
				// method stub

			}

			public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
				bom.setVisible(false);
				registerUpdateHandler(new TimerHandler(0.5f, new ITimerCallback() {

					public void onTimePassed(TimerHandler pTimerHandler) {
						PikaPlayScene.this.detachChild(bom);
					}
				}));

			}
		});

		attachChild(bom);
	};

	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {

		switch (pSceneTouchEvent.getAction()) {
		case TouchEvent.ACTION_DOWN:

			// this.mEngine.setScene(sceneSplash);
			// currentScene = SceneType.SPLASH;
			break;
		case TouchEvent.ACTION_UP:
			// this.mEngine.setScene(sceneGame);
			// sceneGame.attachChild(mySprite);
			// currentScene = SceneType.GAME;
			// sound.play();
			break;
		default:
			break;
		}
		return false;
	}

	public void endScene() {
		// TODO Auto-generated method stub

	}

	public void destroyed() {
		clearChildScene();
		clearEntityModifiers();
		clearTouchAreas();
		clearUpdateHandlers();
	}

	@Override
	public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if (pButtonSprite.getUserData().equals(BTN_SHARE)) {

		}
	}

}
