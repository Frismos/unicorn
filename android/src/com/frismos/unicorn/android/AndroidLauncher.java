package com.frismos.unicorn.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.flurry.android.FlurryAgent;
import com.frismos.unicorn.UnicornGame;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;

		FlurryAgent.init(this, "PXZ568PN8HZJRFPSF22V");

		initialize(new UnicornGame(null), config);
	}

	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, "PXZ568PN8HZJRFPSF22V");
		FlurryAgent.setCaptureUncaughtExceptions(true);
	}

	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}
}
