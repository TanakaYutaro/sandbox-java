package com.example.jp.co.yutaro.tanaka;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.jp.co.yutaro.tanaka.twitter.TwitterOAuthActivity;
import com.example.jp.co.yutaro.tanaka.twitter.TwitterUtils;

/**
 * Twitter認証のアクティビティ.
 * 
 * @author gain-glory-victory
 * 
 */
public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (!TwitterUtils.hasAccessToken(this)) {
			Intent intent = new Intent(this, TwitterOAuthActivity.class);
			startActivity(intent);
			finish();
		} else {
			FragmentManager manager = getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();

			CategoryFragment categoryFragment = CategoryFragment.newInstance();

			transaction.replace(android.R.id.content, categoryFragment,
					"categoryFragment");
			transaction.commit();
		}
	}
}
