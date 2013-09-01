package com.example.jp.co.yutaro.tanaka.animation;

import android.view.animation.AlphaAnimation;
import android.view.animation.CycleInterpolator;
import android.widget.ImageView;

public class RenderImage {

	private AlphaAnimation alpha;

	public RenderImage() {
		alpha = new AlphaAnimation(1, 0);
		alpha.setDuration(1000);
		alpha.setInterpolator(new CycleInterpolator(3));
	}

	public void renderCharacterIcon(ImageView image) {
		image.startAnimation(alpha);
	}
}
