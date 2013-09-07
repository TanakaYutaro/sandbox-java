package com.example.jp.co.yutaro.tanaka;

import java.util.List;

import twitter4j.Query;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jp.co.yutaro.tanaka.animation.RenderImage;
import com.example.jp.co.yutaro.tanaka.character.Enemy;
import com.example.jp.co.yutaro.tanaka.character.Player;
import com.example.jp.co.yutaro.tanaka.sound.GameSound;
import com.example.jp.co.yutaro.tanaka.twitter.TweetAdapter;
import com.example.jp.co.yutaro.tanaka.twitter.TwitterUtils;

/**
 * バトルのアクティビティ.
 * 
 * @author gain-glory-victory
 * 
 */
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

	private Player mPlayer;
	private Enemy enemy;
	private ProgressBar playerHpBar, enemyHpBar;

	private TextView msgView, mPlayerHpView, mPlayerPowerView,
			mPlayerDefendView;

	private ImageView mPlayerImg, mEnemyImg;
	private RenderImage mRenderImage;

	private Handler mHandler = new Handler(Looper.getMainLooper());
	private Runnable runEnemyAttack, udtTxtUsrTurn, udtTxtEnmTurn,
			udtTxtUsrDmg, udtTxtEnmDmg;

	private GameSound mGameSound;

	private int enemyDamage;
	private int nowEnemyHp;

	private int playerDamage;
	private int nowPlayerHp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_battle);

		reloadBtn = (ImageButton) findViewById(R.id.reloadBtn);
		tweetBtn = (ImageButton) findViewById(R.id.tweetBtn);
		defendBtn = (ImageButton) findViewById(R.id.defendBtn);
		reloadBtn.setOnClickListener(this);
		tweetBtn.setOnClickListener(this);
		defendBtn.setOnClickListener(this);

		mPlayerImg = (ImageView) findViewById(R.id.PlayerPicView);
		mEnemyImg = (ImageView) findViewById(R.id.dragonPicView);
		mRenderImage = new RenderImage();

		list = (ListView) findViewById(R.id.tweetTLView);
		mAdapter = new TweetAdapter(this);
		list.setAdapter(mAdapter);
		mTwitter = TwitterUtils.getTwitterInstance(this);
		reloadTimeLine();

		initRolls(mTwitter);

		mPlayerHpView = (TextView) findViewById(R.id.playerHp);
		mPlayerPowerView = (TextView) findViewById(R.id.playerPower);
		mPlayerDefendView = (TextView) findViewById(R.id.playerDefend);

		mGameSound = new GameSound(this);

		msgView = (TextView) findViewById(R.id.message);

		showToast(getString(R.string.game_start_toast));

		initRunnable();

		msgView.setText("Encount Dragon !!!");
		mHandler.postDelayed(udtTxtUsrTurn, 3000);

	}

	private void initRunnable() {
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
				msgView.setText(playerDamage + " damage !");
			}
		};
	}

	private void initRolls(Twitter twitter) {
		mPlayer = new Player(twitter);
		// player = new Player(PLAYER_DEFAULT_HP, PLAYER_DEFAULT_POWER,
		// PLAYER_DEFAULT_DEFEND);
		enemy = new Enemy(DRAGON_DEFAULT_HP, DRAGON_DEFAULT_POWER,
				DRAGON_DEFAULT_DEFEND);

		playerHpBar = (ProgressBar) findViewById(R.id.playerHpBar);
		enemyHpBar = (ProgressBar) findViewById(R.id.enemyHpBar);
		playerHpBar.setMax(PLAYER_DEFAULT_HP);
		enemyHpBar.setMax(DRAGON_DEFAULT_HP);
		playerHpBar.setBackgroundColor(Color.GREEN);
		enemyHpBar.setBackgroundColor(Color.GREEN);

		nowPlayerHp = enemy.getHp();
		playerHpBar.setProgress(nowPlayerHp);
		nowEnemyHp = enemy.getHp();
		enemyHpBar.setProgress(nowEnemyHp);
	}

	// TODO ステータスを画面に表示したいが実行すると NullPointerで落ちる
	private void reloadStatus() {
		mPlayerHpView.setText(mPlayer.getHp());
		mPlayerPowerView.setText(mPlayer.getPower());
		mPlayerDefendView.setText(mPlayer.getDefend());
	}

	@Override
	public void onStop() {
		super.onStop();
		mGameSound.bgm.stop();
		mGameSound.bgm.release();
	}

	@Override
	public void onResume() {
		super.onResume();
		mGameSound.soundsInit(this, R.raw.bgm);
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
						// TODO 入力チェック
						// TODO 文字数制限
						if (contentsTweet != null || contentsTweet.equals("")) {
							// Tweet にハッシュタグを付け加える
							String tweetBody = getString(R.string.Twi_Dra_Hash_Tag)
									+ " " + contentsTweet;
							tweet(tweetBody);
							playerAtttack();
							mHandler.postDelayed(udtTxtEnmTurn, 3000);
							mHandler.postDelayed(runEnemyAttack, 5000);
						} else {
							showToast("Empty Input !");
						}
					}
				});
		AlertDialog alertDialog = tweetDialog.create();
		// TODO ESCキー無効化
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
		alertDialog.show();
	}

	private void toTop() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	private void retryGame() {
		Intent intent = new Intent(this, BattleActivity.class);
		startActivity(intent);
	}

	private void playerAtttack() {
		mGameSound.playSoundAtk();
		mRenderImage.renderCharacterIcon(mEnemyImg);
		mGameSound.playSoundDef();

		enemyDamage = mPlayer.userAttack(enemy, contentsTweet);

		mHandler.postDelayed(udtTxtUsrDmg, 1500);

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
		playerDamage = enemy.dragonAttack(mPlayer);

		mGameSound.playSoundAtk();
		mRenderImage.renderCharacterIcon(mPlayerImg);
		mGameSound.playSoundDef();

		nowPlayerHp = enemy.getHp();
		playerHpBar.setProgress(nowPlayerHp);

		mHandler.postDelayed(udtTxtEnmDmg, 2000);
		// TODO ゲーム開始前に実行すると、NullPoで落ちる
		// reloadStatus();

		if (nowPlayerHp <= 0) {
			createFailureDialog();

			// break;
		} else if (PLAYER_DEFAULT_HP * 0.3 <= nowPlayerHp
				&& nowPlayerHp <= PLAYER_DEFAULT_HP * 0.6) {
			playerHpBar.setBackgroundColor(Color.YELLOW);
			// break;
		} else if (nowPlayerHp < PLAYER_DEFAULT_HP * 0.3) {
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

	private List<Status> searchTweet(Twitter twitter, String target)
			throws TwitterException {
		Query query = new Query();
		// 「自分のアカウント」 &「ハッシュタグのツイート」のみ表示
		// スペースで区切ると AND 検索になる
		query.setQuery(target + " " + "from:" + twitter.getScreenName());
		return twitter.search(query).getTweets();
	}

	private void reloadTimeLine() {
		AsyncTask<Void, Void, List<twitter4j.Status>> task = new AsyncTask<Void, Void, List<twitter4j.Status>>() {
			@Override
			protected List<twitter4j.Status> doInBackground(Void... params) {
				try {
					return searchTweet(mTwitter,
							getString(R.string.Twi_Dra_Hash_Tag));
					// return mTwitter.getHomeTimeline();
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

	/**
	 * オプショメニューのカスタマイズ.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	/**
	 * オプションメニュークリック時の挙動.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.bgm:
			if (mGameSound.getSoundOn()) {
				mGameSound.bgm.stop();
				mGameSound.bgm.release();
			} else {
				mGameSound.soundsInit(this, R.raw.bgm);
			}
			mGameSound.switchBgm();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
