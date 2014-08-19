package com.joshisk.mathbingo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.joshisk.mathbingo.ResourceManager;

public class Utilities {

	private static final ResourceManager RM = ResourceManager.getInstance();
	
	private Utilities() {
		
	}

	public static void playButtonSound() {
		if(RM.gameDataManager.ismSoundRequired())
			RM.mButtonPressSound.play();
	}
	
	public static void playBingoSound() {
		if(RM.gameDataManager.ismSoundRequired())
			RM.mBingoSound.play();
	}
	
	public static boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) RM.activity.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
