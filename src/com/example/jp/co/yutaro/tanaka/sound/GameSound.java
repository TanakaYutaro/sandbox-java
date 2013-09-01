package com.example.jp.co.yutaro.tanaka.sound;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.example.jp.co.yutaro.tanaka.R;

public class GameSound {

	private SoundPool mSoundPool;
	private int mSoundId_atk;
	private int mSoundId_def;
	private int mSoundId_tame;
	public MediaPlayer bgm;

	public GameSound(Context context) {
		// Sound
		mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		mSoundId_atk = mSoundPool.load(context, R.raw.kirare03, 1);
		mSoundId_def = mSoundPool.load(context, R.raw.kick2, 1);
		mSoundId_tame = mSoundPool.load(context, R.raw.tame, 1);

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

	public void playSoundAtk() {
		mSoundPool.play(mSoundId_atk, 1.0F, 1.0F, 0, 0, 1.0F);
	}

	public void playSoundDef() {
		mSoundPool.play(mSoundId_def, 1.0F, 1.0F, 0, 0, 1.0F);
	}

	public void playSoundTame() {
		mSoundPool.play(mSoundId_tame, 1.0F, 1.0F, 0, 0, 1.0F);
	}

}
