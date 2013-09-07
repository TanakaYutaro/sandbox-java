package com.example.jp.co.yutaro.tanaka.animation;

import android.view.animation.AlphaAnimation;
import android.view.animation.CycleInterpolator;
import android.widget.ImageView;

/**
 * キャラクターアイコンのアニメーション.
 * 
 * @author gain-glory-victory
 * 
 */
public class RenderImage {

	private AlphaAnimation alpha;

	/**
	 * コンストラクタ.
	 */
	public RenderImage() {
		alpha = new AlphaAnimation(1, 0);
		alpha.setDuration(1000);
		alpha.setInterpolator(new CycleInterpolator(3));
	}

	/**
	 * 画像のレンダリング.
	 */
	public void renderCharacterIcon(ImageView image) {
		image.startAnimation(alpha);
	}
}
