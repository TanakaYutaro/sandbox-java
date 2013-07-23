package com.example.jp.co.yutaro.tanaka;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

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
			// TODO ログインしている場合、ゲームカテゴリ一覧を表示する
			FragmentManager manager = getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();

			CategoryFragment categoryFragment = CategoryFragment.newInstance();
	
			transaction.replace(android.R.id.content, categoryFragment, "categoryFragment");
			transaction.commit();
			
			/*
			Intent intent = new Intent(this, BattleActivity.class);
			startActivity(intent);
			finish();
			*/
		}
	}
}
