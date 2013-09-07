package com.example.jp.co.yutaro.tanaka;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * ゲームカテゴリ選択のフラグメント.
 * 
 * @author gain-glory-victory
 * 
 */
public class CategoryFragment extends Fragment implements OnClickListener {

	/**
	 * インスタンスを生成する.
	 * 
	 * @return フラグメントのインスタンス.
	 */
	public static CategoryFragment newInstance() {
		CategoryFragment categoryFragment = new CategoryFragment();
		return categoryFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_category, container,
				false);

		root.findViewById(R.id.button_start_dragon).setOnClickListener(this);

		return root;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_start_dragon:
			Intent intent = new Intent(getActivity(), BattleActivity.class);
			startActivity(intent);
			break;
		case R.id.button_start_english:

		}
	}

}
