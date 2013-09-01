package com.example.jp.co.yutaro.tanaka;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.jp.co.yutaro.tanaka.twitter.TweetAdapter;
import com.example.jp.co.yutaro.tanaka.twitter.TwitterUtils;
import com.loopj.android.image.SmartImageView;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.CycleInterpolator;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class BattleActivity extends FragmentActivity implements OnClickListener {

	private String contentsTweet;
	private ImageButton reloadBtn, tweetBtn, defendBtn;

	private TweetAdapter mAdapter;
	private Twitter mTwitter;
	private ListView list;

	private static final int PLAYER_DEFAULT_HP = 100;
	private static final int PLAYER_DEFAULT_POWER = 10;
	private static final int PLAYER_DEFAULT_DEFEND = 10;

	private static final int DRAGON_DEFAULT_HP = 200;
	private static final int DRAGON_DEFAULT_POWER = 40;
	private static final int DRAGON_DEFAULT_DEFEND = 40;

	private Roll player, enemy;
	private ProgressBar playerHpBar, enemyHpBar;

	// Sound
	private SoundPool mSoundPool;
	private int mSoundId_atk;
	private int mSoundId_def;
	private int mSoundId_tame;
	private MediaPlayer bgm;

	private TextView msgView;

	private Handler mHandler = new Handler(Looper.getMainLooper());
	private Runnable runEnemyAttack, udtTxtUsrTurn, udtTxtEnmTurn,
			udtTxtUsrDmg, udtTxtEnmDmg;

	int enemyDamage;
	int nowEnemyHp;

	int userDamage;
	int nowUserHp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_battle);

		// Sound
		mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		mSoundId_atk = mSoundPool.load(this, R.raw.kirare03, 1);
		mSoundId_def = mSoundPool.load(this, R.raw.kick2, 1);
		mSoundId_tame = mSoundPool.load(this, R.raw.tame, 1);

		bgm = MediaPlayer.create(this, R.raw.bgm);
		bgm.setLooping(true);
		try {
			bgm.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		bgm.start();

		// �{�^������
		reloadBtn = (ImageButton) findViewById(R.id.reloadBtn);
		tweetBtn = (ImageButton) findViewById(R.id.tweetBtn);
		defendBtn = (ImageButton) findViewById(R.id.defendBtn);
		reloadBtn.setOnClickListener(this);
		tweetBtn.setOnClickListener(this);
		defendBtn.setOnClickListener(this);

		// extends ListActivity ��������
		list = (ListView) findViewById(R.id.tweetTLView);
		mAdapter = new TweetAdapter(this);
		list.setAdapter(mAdapter);
		// Tweet ����
		mTwitter = TwitterUtils.getTwitterInstance(this);
		reloadTimeLine();

		// Player Dragon
		player = new Roll(PLAYER_DEFAULT_HP, PLAYER_DEFAULT_POWER,
				PLAYER_DEFAULT_DEFEND);
		enemy = new Roll(DRAGON_DEFAULT_HP, DRAGON_DEFAULT_POWER,
				DRAGON_DEFAULT_DEFEND);

		// HP
		playerHpBar = (ProgressBar) findViewById(R.id.playerHpBar);
		enemyHpBar = (ProgressBar) findViewById(R.id.enemyHpBar);
		playerHpBar.setMax(PLAYER_DEFAULT_HP);
		enemyHpBar.setMax(DRAGON_DEFAULT_HP);
		playerHpBar.setBackgroundColor(Color.GREEN);
		enemyHpBar.setBackgroundColor(Color.GREEN);

		nowUserHp = enemy.getHp();
		playerHpBar.setProgress(nowUserHp);
		nowEnemyHp = enemy.getHp();
		enemyHpBar.setProgress(nowEnemyHp);

		// ���b�Z�[�W�r���[��������
		msgView = (TextView) findViewById(R.id.message);

		// �o�g���X�^�[�g���A���[�g
		showToast(getString(R.string.game_start_toast));

		runEnemyAttack = new Runnable() {
			public void run() {
				enemyAttack();
			}
		};
		udtTxtUsrTurn = new Runnable() {
			public void run() {
				msgView.setText("Your Turn Now");
			}
		};
		udtTxtUsrDmg = new Runnable() {
			public void run() {
				msgView.setText("Success ! " + enemyDamage + " damage !");
			}
		};
		udtTxtEnmTurn = new Runnable() {
			public void run() {
				msgView.setText("Enemy Turn Now");
			}
		};
		udtTxtEnmDmg = new Runnable() {
			public void run() {
				msgView.setText(userDamage + " damage !");
			}
		};

		msgView.setText("Encount Dragon !!!");
		mHandler.postDelayed(udtTxtUsrTurn, 3000);

	}

	@Override
	public void onStop() {
		super.onStop();
		bgm.stop();
		bgm.release();
	}

	@Override
	public void onResume() {
		super.onResume();
		// BGM�����~
		bgm = MediaPlayer.create(this, R.raw.bgm);
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

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reloadBtn:
			reloadTimeLine();
			showToast(getString(R.string.reload_toast_msg));
			break;
		case R.id.tweetBtn:
			tweetDialog();
			break;
		case R.id.defendBtn:
			break;
		}
	}

	// animation
	public void renderPlayerIcon() {
		ImageView playerImg = (ImageView) findViewById(R.id.PlayerPicView);
		AlphaAnimation alpha = new AlphaAnimation(1, 0);
		alpha.setDuration(1000);
		alpha.setInterpolator(new CycleInterpolator(3));
		playerImg.startAnimation(alpha);
	}

	public void renderEnemyIcon() {
		ImageView enemyImg = (ImageView) findViewById(R.id.dragonPicView);
		AlphaAnimation alpha = new AlphaAnimation(1, 0);
		alpha.setDuration(1000);
		alpha.setInterpolator(new CycleInterpolator(3));
		enemyImg.startAnimation(alpha);
	}

	// Sound Control
	public void playSoundAtk() {
		mSoundPool.play(mSoundId_atk, 1.0F, 1.0F, 0, 0, 1.0F);
	}

	public void playSoundDef() {
		mSoundPool.play(mSoundId_def, 1.0F, 1.0F, 0, 0, 1.0F);
	}

	public void playSoundTame() {
		mSoundPool.play(mSoundId_tame, 1.0F, 1.0F, 0, 0, 1.0F);
	}

	private void tweetDialog() {
		AlertDialog.Builder tweetDialog = new AlertDialog.Builder(this);
		final EditText editView = new EditText(BattleActivity.this);
		editView.setInputType(InputType.TYPE_CLASS_TEXT);
		tweetDialog.setView(editView);
		tweetDialog.setTitle(getString(R.string.tweet_dialog_title));
		tweetDialog.setMessage(getString(R.string.tweet_dialog_message));
		tweetDialog.setPositiveButton(getString(R.string.tweet_dialog_btn_ok),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						contentsTweet = editView.getText().toString();
						// Tweet
						if (contentsTweet != null) {
							tweet(contentsTweet);
							// ���[�U���U��
							userAtttack();

							// �^�[���\�����X
							mHandler.postDelayed(udtTxtEnmTurn, 3000);

							// �G���U��
							mHandler.postDelayed(runEnemyAttack, 5000);
						} else {
							showToast("Empty Input !");
						}
					}
				});
		AlertDialog alertDialog = tweetDialog.create();
		alertDialog.show();
	}

	private void createFailureDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Failure .......");
		alertDialogBuilder.setPositiveButton(
				getString(R.string.retry_btn_label),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						retryGame();
					}
				});
		alertDialogBuilder.setNegativeButton(
				getString(R.string.totop_btn_label),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						toTop();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		// �A���[�g�_�C�A���O���\��
		alertDialog.show();
	}

	private void createEndDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("You Win !!!!!!!!!!!!!!!!.");
		alertDialogBuilder.setPositiveButton(
				getString(R.string.retry_btn_label),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						retryGame();
					}
				});
		alertDialogBuilder.setNegativeButton(
				getString(R.string.totop_btn_label),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						toTop();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		// �A���[�g�_�C�A���O���\��
		alertDialog.show();
	}

	/**
	 * TOP��������������.
	 * 
	 * @author tanaka_yut
	 */
	public void toTop() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	/**
	 * �Q�[�������g���C������.
	 * 
	 * @author tanaka_yut
	 */
	public void retryGame() {
		Intent intent = new Intent(this, BattleActivity.class);
		startActivity(intent);
	}

	private void userAtttack() {

		// ������������
		playSoundAtk();
		renderEnemyIcon();
		playSoundDef();

		// �_���[�W����
		enemyDamage = player.userAttack(enemy, contentsTweet);

		mHandler.postDelayed(udtTxtUsrDmg, 1500);

		// HP �o�[�X�V
		nowEnemyHp = enemy.getHp();
		enemyHpBar.setProgress(nowEnemyHp);

		if (nowEnemyHp <= 0) {
			createEndDialog();
			// break;
		} else if (DRAGON_DEFAULT_HP * 0.3 <= nowEnemyHp
				&& nowEnemyHp <= DRAGON_DEFAULT_HP * 0.6) {
			enemyHpBar.setBackgroundColor(Color.YELLOW);
			// break;
		} else if (nowEnemyHp < DRAGON_DEFAULT_HP * 0.3) {
			enemyHpBar.setBackgroundColor(Color.RED);
			// break;
		}
	}

	private void enemyAttack() {
		/****************
		 * �h���S�����U��
		 */

		// �_���[�W����
		userDamage = enemy.dragonAttack(player);

		// ������������
		playSoundAtk();
		renderPlayerIcon();
		playSoundDef();

		// HP �o�[�X�V
		nowUserHp = enemy.getHp();
		playerHpBar.setProgress(nowUserHp);

		// �_���[�W�e�L�X�g�\��
		mHandler.postDelayed(udtTxtEnmDmg, 2000);

		// ������ HP ������������
		if (nowUserHp <= 0) {
			createFailureDialog();

			// break;
		} else if (PLAYER_DEFAULT_HP * 0.3 <= nowUserHp
				&& nowUserHp <= PLAYER_DEFAULT_HP * 0.6) {
			playerHpBar.setBackgroundColor(Color.YELLOW);
			// break;
		} else if (nowUserHp < PLAYER_DEFAULT_HP * 0.3) {
			playerHpBar.setBackgroundColor(Color.RED);
			// break;
		}

		mHandler.postDelayed(udtTxtUsrTurn, 4000);

	}

	private void tweet(String tweetMsg) {
		AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(String... params) {
				try {
					mTwitter.updateStatus(params[0]);
					return true;
				} catch (TwitterException e) {
					e.printStackTrace();
					return false;
				}
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					showToast(getString(R.string.tweeted_toast_msg));
				} else {
					showToast(getString(R.string.tweet_failure_msg));
				}
			}
		};
		task.execute(tweetMsg);
	}

	private void reloadTimeLine() {
		AsyncTask<Void, Void, List<twitter4j.Status>> task = new AsyncTask<Void, Void, List<twitter4j.Status>>() {
			@Override
			protected List<twitter4j.Status> doInBackground(Void... params) {
				try {
					return mTwitter.getHomeTimeline();
				} catch (TwitterException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(List<twitter4j.Status> result) {
				if (result != null) {
					mAdapter.clear();
					for (twitter4j.Status status : result) {
						mAdapter.add(status);
					}
					list.setAdapter(mAdapter);
				} else {
					showToast("�^�C�����C�������������s���������B�B�B");
				}
			}
		};
		task.execute();
	}

	private void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	
}
