package com.Ichif1205.anrakutei;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Path;
import android.util.FloatMath;
import android.util.Log;

public class Invader {
	
	private final String TAG = FieldSurfaceView.class.getSimpleName();
	private float posX;
	private float posY;
	private int width;
	private int height;
	private boolean existFlag;
	private double mv_pattern;
	private int type;
	private InvarderListener mIl;
	private int mTerm = 1000;
	private Timer mShootTimer;
		private Player player;
	// 敵の動き関係
	private int speedX;
	private int speedY;
	private float theta;
	private int radius;
	private float centerX;
	private float centerY;
	private float alpha = 0;
	// 敵の種類定義
	public static final int INV_PURPLE = 0;
	public static final int INV_YELLOW = 1;
	public static final int INV_LIGHTBLUE = 2;
	public static final int INV_ORANGE = 3;
	public static final int INV_GREEN = 4;
	public static final int INV_BOSS = 5;
	// 敵の画像の大きさ
	public static final int INV_IMAGE_WIDTH = 36;
	public static final int INV_IMAGE_HEIGHT = 36;
	public static final int INVBOSS_IMAGE_WIDTH = 72;
	public static final int INVBOSS_IMAGE_HEIGHT = 72;

	private float HPX;
	private float HPY;
	private static final int BOSSHPMAX = 10; // BossのHPのMax
	private int BossHP; // BossのHP

	Invader(float x, float y, int invtype, InvarderListener li) {
		posX = getRandomPosition(x);
		posY = getRandomPosition(y);
		width = INV_IMAGE_WIDTH;
		height = INV_IMAGE_HEIGHT;
		Random speed_rand = new Random();
		int spd = speed_rand.nextInt(3) + 3;
		speedX = spd;
		speedY = spd;
		existFlag = true;
		type = invtype;

		mv_pattern = Math.random();
		mIl = li;
		// 青インベーダーの円の中心と表示位置を求める
		if (type == INV_LIGHTBLUE) {
			centerX = posX;
			centerY = posY;
			Random r_rand = new Random();
			int r_times = r_rand.nextInt(3);
			radius = 50 * (2 + r_times);
			// centerの位置座標によってthetaの初期値を決定
			// 例えば画面の右下が初期位置ならthetaの開始は円の左上
			if (centerX <= x / 2 && centerY > y / 2) {
				theta = (float) (7 * Math.PI / 4);
			} else if (centerX > x / 2 && centerY > y / 2) {
				theta = (float) (5 * Math.PI / 4);
			} else if (centerX > x / 2 && centerY <= y / 2) {
				theta = (float) (3 * Math.PI / 4);
			} else {
				theta = (float) (Math.PI / 4);
			}
		}
		// ボスの設定
		if (type == INV_BOSS) {
			width = INVBOSS_IMAGE_WIDTH;
			height = INVBOSS_IMAGE_HEIGHT;
			BossHP = BOSSHPMAX;
		}

		mShootTimer = new Timer();
		mShootTimer.schedule(new ShootTask(), mTerm);
	}

	public float getInvaderPosX() {
		return posX;
	}

	public float getInvaderPosY() {
		return posY;
	}

	public int getInvType() {
		return type;
	}

	public boolean isExisted() {
		return existFlag;
	}

	public float getRandomPosition(float length) {
		float rate;
		while (true) {
			rate = (float) Math.random();
			if (rate > 0.20 && rate < 0.80) {
				break;
			}
		}
		float pos = rate * length;
		return pos;
	}

	public boolean isShooted(float shotX, float shotY) {
		if ((shotX >= posX) && (shotX <= posX + width) && (shotY >= posY)
				&& (shotY <= posY + height)) {
			return true;
		}
		return false;
	}

	public boolean isOverBoundaryWidth(int w) {
		if (posX > w - width || posX < 0) {
			return true;
		}
		return false;
	}

	public boolean isOverBoundaryHeight(int h) {
		if (posY > h - height || posY < 0) {
			return true;
		}
		return false;
	}

	public void reverseSpeedXDirection() {
		speedX = -speedX;
	}

	public void reverseSpeedYDirection() {
		if (type == INV_LIGHTBLUE) {
			speedX = -speedX;
		} else {
			speedY = -speedY;
		}
	}

	public void updatePosition(float playerPosX) {
		// インベーダーの種類によって動き異なる
		if (type == INV_PURPLE) {
			moveLR();
		} else if (type == INV_YELLOW) {
			moveSkew();
		} else if (type == INV_LIGHTBLUE) {
			moveCircle();
		} else if (type == INV_ORANGE) {
			moveUD();
		} else if (type == INV_GREEN) {
			move30Skew();
		} else {
			moveVibration(playerPosX);
		}
	}

	private void moveLR() {
		if (mv_pattern < 0.5) {
			posX += speedX;
		} else {
			posX -= speedX;
		}
	}

	private void moveSkew() {
		if (mv_pattern < 0.25) {
			posX += speedX;
			posY += speedY;
		} else if (mv_pattern < 0.5) {
			posX -= speedX;
			posY += speedY;
		} else if (mv_pattern < 0.75) {
			posX += speedX;
			posY -= speedY;
		} else {
			posX -= speedX;
			posY -= speedY;
		}
	}

	private void move30Skew() {
		if (mv_pattern < 0.25) {
			posX += speedX;
			posY += speedY / 2;
		} else if (mv_pattern < 0.5) {
			posX -= speedX;
			posY += speedY / 2;
		} else if (mv_pattern < 0.75) {
			posX += speedX;
			posY -= speedY / 2;
		} else {
			posX -= speedX;
			posY -= speedY / 2;
		}
	}

	private void moveCircle() {
		if (mv_pattern < 0.5) {
			alpha += speedX * 1.0 / radius;
		} else {
			alpha -= speedX * 1.0 / radius;
		}
		posX = radius * FloatMath.cos(theta + alpha) + centerX;
		posY = radius * FloatMath.sin(theta + alpha) + centerY;
	}

	private void moveUD() {
		if (mv_pattern < 0.5) {
			posY += speedY;
		} else {
			posY -= speedY;
		}
	}

	private void moveVibration(float pposx) {
		double mvvi = Math.random();
		// 1/8 の確率で移動
		// そのうち各方向に移動する確率は 上: 2/8, 下: 2/8,
		// 自機の方向: 3/8, 自機と逆方向: 1/8
		if (mvvi < 0.03125) {		
			posY += speedY + 4;
		} else if (mvvi < 0.0625) {
			posY -= speedY + 4;
		} else if (mvvi < 0.09375) {
			if (posX > pposx) {
				posX -= speedX + 4;
			} else {
				posX += speedX + 4;
			}
		} else if (mvvi < 0.0625) {
			if (posX > pposx) {
				posX += speedX + 4;
			} else {
				posX -= speedX + 4;
			}
		}
	}

	public void remove() {
		posY = -1000;
		existFlag = false;
		mShootTimer.cancel();
	}

	public interface InvarderListener {
		public void shootBeamEvent(float shotX, float shotY, int type);

		public void Item(float shotX, float shotY);
	}

	class ShootTask extends TimerTask {
		public void run() {
			mIl.shootBeamEvent(posX, posY, type);
			Random binterval_rand = new Random();
			int randBeam;
			if (type == INV_BOSS) {
				randBeam = 1000 + (100 * binterval_rand.nextInt(5));
			} else {
				randBeam = 1500 + (500 * binterval_rand.nextInt(5));
			}
			mShootTimer.schedule(new ShootTask(), randBeam);
		}
	}

	public void ItemAdd() {
		mIl.Item(posX, posY);
	}

	/**
	 * インベーダーの種類に応じて点数を返す
	 * 
	 * @return
	 */
	public int getPoint() {
		final int point;
		switch (type) {
		case INV_PURPLE:
		case INV_YELLOW:
		case INV_LIGHTBLUE:
		case INV_ORANGE:
		case INV_GREEN:
			point = 1000;
			break;

		case INV_BOSS:
			point = 3000;
			break;

		default:
			point = 0;
			break;
		}
		return point;
	}

	// ボスHP描画のパス
	public Path drawBossHPMeter(Path path, float x, float y) {
		int HPwidth = 50 / BOSSHPMAX * BossHP;
		int HPheight = 8;
		HPX = x + 36; // width/2
		HPY = y - 20; // 適当
		path.moveTo(HPX, HPY);
		// 左下から反時計回りに描画
		path.lineTo(HPX - HPwidth, HPY + HPheight);
		path.lineTo(HPX + HPwidth, HPY + HPheight);
		path.lineTo(HPX + HPwidth, HPY - HPheight);
		path.lineTo(HPX + HPwidth, HPY - HPheight);
		path.lineTo(HPX + HPwidth, HPY - HPheight);
		path.lineTo(HPX - HPwidth, HPY - HPheight);
		path.lineTo(HPX - HPwidth, HPY - HPheight);
		path.lineTo(HPX - HPwidth, HPY - HPheight);
		path.lineTo(HPX - HPwidth, HPY + HPheight);

		return path;
	}

	public int BossCheck() {
		int continueflg = 0;
		int invType = getInvType();
		Log.d("itemPattern", "itemnum" + invType + INV_BOSS);
		if (invType == INV_BOSS) {
			BossHP--;
			if (BossHP != 0) {
				Log.d("itemPattern", "bhp" + BossHP);
				continueflg = 1;
			}
		} else {
			continueflg = 0;
		}
		return continueflg;
	}
}
