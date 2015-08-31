package com.example.jp.co.yutaro.tanaka.sound;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.example.jp.co.yutaro.tanaka.R;

/**
 * ゲーム内音声クラス.
 * 
 * @author gain-glory-victory
 * 
 */
public class GameSound {

	private SoundPool mSoundPool;
	private int mSoundId_atk;
	private int mSoundId_def;
	private int mSoundId_tame;
	private boolean mSoundOn, mAttackActive, mDefendActive, mGameBgmAcctive;

	/** ゲーム内音楽. */
	public MediaPlayer bgm;

	/**
	 * コンストラクタ.
	 * 
	 * @param context
	 */
	public GameSound(Context context) {
		// Sound
		mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		mSoundId_atk = mSoundPool.load(context, R.raw.kirare03, 1);
		mSoundId_def = mSoundPool.load(context, R.raw.kick2, 1);
		mSoundId_tame = mSoundPool.load(context, R.raw.tame, 1);

		mSoundOn = true;
		mAttackActive = true;
		mDefendActive = true;
		mGameBgmAcctive = true;

		bgm = MediaPlayer.create(context, R.raw.bgm);
		bgm.setLooping(true);
		try {
			bgm.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		bgm.start();
	}

	/**
	 * 攻撃時音声.
	 */
	public void playSoundAtk() {
		if (mAttackActive) {
			mSoundPool.play(mSoundId_atk, 1.0F, 1.0F, 0, 0, 1.0F);
		}
	}

	/**
	 * 防御時音声.
	 */
	public void playSoundDef() {
		if (mDefendActive) {
			mSoundPool.play(mSoundId_def, 1.0F, 1.0F, 0, 0, 1.0F);
		}
	}

	/**
	 * ゲーム全体の音楽.
	 */
	public void playSoundTame() {
		if (mGameBgmAcctive) {
			mSoundPool.play(mSoundId_tame, 1.0F, 1.0F, 0, 0, 1.0F);
		}
	}

	/**
	 * BGMの再生開始.
	 * 
	 * @param context
	 * @param bgmId
	 */
	public void soundsInit(Context context, int bgmId) {
		bgm = MediaPlayer.create(context, bgmId);
		bgm.setLooping(true);
		try {
			bgm.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		bgm.start();
	}

	/**
	 * BGM関連フラグのセッター.
	 */
	public void switchBgm() {
		mSoundOn = !mSoundOn;
		mAttackActive = !mAttackActive;
		mDefendActive = !mDefendActive;
		mGameBgmAcctive = !mGameBgmAcctive;
	}

	/**
	 * ゲッター.
	 * 
	 * @return mSoundOn BGM再生中フラグ
	 */
	public boolean getSoundOn() {
		return mSoundOn;
	}
}
