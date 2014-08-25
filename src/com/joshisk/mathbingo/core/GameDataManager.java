package com.joshisk.mathbingo.core;

import android.content.Context;
import android.content.SharedPreferences;

import com.joshisk.mathbingo.LevelManager;
import com.joshisk.mathbingo.LevelManager.Level;
import com.joshisk.mathbingo.ResourceManager;

/**
 * Will be used to store/load the data to/from disk
 * @author joshisk
 *
 */
public class GameDataManager {
	
	private static ResourceManager RM = ResourceManager.getInstance();
	//public static GameDataManager INSTANCE = new GameDataManager(RM.activity.getApplicationContext());
	
	private static final String PREFS_NAME = "GAME_USERDATA";
	private static final String HIGH_SCORE_KEY[] = new String[]{"HIGHSCORE_EASY", "HIGHSCORE_MEDIUM", "HIGHSCORE_HARD"};
	private static final String HIGH_SCORE_MEDIUM_KEY = "HIGHSCORE_MEDIUM";
	private static final String HIGH_SCORE_HARD_KEY = "HIGHSCORE_HARD";
	private static final String SCORE_KEY = "SCORE"; //Current game's score
	private static final String GOLD_COINS = "GOLDCOINS";
	private static final String LEVEL = "LEVEL";
	private static final String SOUND_REQUIRED = "SOUND_REQUIRED";
	private static final String LEADERBOARD = "LEADERBOARD";
	private static final String MUSIC_REQUIRED = "MUSIC_REQUIRED";
	private static final String NO_OF_GAMES_PLAYED = "NO_OF_GAMES_PLAYED";
	private static final String DONT_ASK_TO_RATE = "DONT_ASK_TO_RATE";
	private static final String HELP_REQUIRED = "HELP_REQUIRED";

	private static final int DEFAULT_COINS = 5;

	private SharedPreferences mSettings;
	private SharedPreferences.Editor mEditor;

	private int mNoOfGamesPlayed;
	private int mLevel;
	private long mScore;
	private long mHighScore[] = new long[LevelManager.Level.values().length];
	private long mGoldCoins;
	private boolean mSoundRequired;
	private boolean mLeaderBoardRequired;
	private boolean mMusicRequired;
	private boolean mDontAskToRate;
	private boolean mHelpRequired;
	

	public GameDataManager(Context pContext) {
		init(pContext);
	}
	
	public synchronized void init(Context pContext) {
		if (mSettings == null) {
			mSettings = pContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
			mEditor = mSettings.edit();

			mNoOfGamesPlayed = mSettings.getInt(NO_OF_GAMES_PLAYED, 0);
			mSoundRequired = mSettings.getBoolean(SOUND_REQUIRED, true);
			mDontAskToRate = mSettings.getBoolean(DONT_ASK_TO_RATE, false);
			mHelpRequired = mSettings.getBoolean(HELP_REQUIRED, true);
			mLeaderBoardRequired = mSettings.getBoolean(LEADERBOARD, true);
			mLevel = mSettings.getInt(LEVEL, LevelManager.Level.EASY.ordinal());
			for (int i = 0; i < Level.values().length; i++) {
				mHighScore[i] = mSettings.getLong(HIGH_SCORE_KEY[i], 0);
			}

			mScore = mSettings.getLong(SCORE_KEY, 0);
			mGoldCoins = mSettings.getLong(GOLD_COINS, DEFAULT_COINS);
		}
	}

//	public static GameDataManager getInstance() {
//		return INSTANCE;
//	}
	
	public synchronized Level getmLevel() {
		return LevelManager.Level.values()[mLevel];
	}

	public boolean ismDontAskToRate() {
		return mDontAskToRate;
	}

	public void setmDontAskToRate(boolean mDontAskToRate) {
		this.mDontAskToRate = mDontAskToRate;
		mEditor.putBoolean(DONT_ASK_TO_RATE, mDontAskToRate);
		mEditor.commit();
	}
	
	public boolean ismHelpRequired() {
		return mHelpRequired;
	}

	public void setmHelpRequired(boolean mHelpRequired) {
		this.mHelpRequired = mHelpRequired;
		mEditor.putBoolean(HELP_REQUIRED, mHelpRequired);
		mEditor.commit();
		
		System.out.println("DEBUG: HELP REQD "+this.mHelpRequired);
	}

	public int getmNoOfGamesPlayed() {
		return mNoOfGamesPlayed;
	}

	public void setmNoOfGamesPlayed(int mNoOfGamesPlayed) {
		this.mNoOfGamesPlayed = mNoOfGamesPlayed;
		mEditor.putInt(NO_OF_GAMES_PLAYED, mNoOfGamesPlayed);
		mEditor.commit();
	}

	public synchronized void setmLevel(Level level) {
		mLevel = level.ordinal();
		mEditor.putInt(LEVEL, mLevel);
		mEditor.commit();
	}

	public synchronized long getmScore() {
		return mScore;
	}

	public synchronized void setmScore(long mScore) {
		this.mScore = mScore;

		if(mScore > getmHighScore()) {
			setmHighScore(mScore);
		} else {
			mEditor.putLong(SCORE_KEY, this.mScore);
			mEditor.commit();
		}
	}

	public synchronized long getmHighScore() {
		return mHighScore[mLevel];
	}

	public synchronized void setmHighScore(long mHighScore) {
		System.out.println("DEBUG: Storing high score Level:"+mLevel+" Highscore:"+mHighScore);
		this.mHighScore[mLevel] = mHighScore;
		mEditor.putLong(HIGH_SCORE_KEY[mLevel], this.mHighScore[mLevel]);
		mEditor.commit();
	}

	public long getGoldCoins() {
		return mGoldCoins;
	}

	public void setGoldCoins(long mGoldCoins) {
		this.mGoldCoins = mGoldCoins;
		mEditor.putLong(GOLD_COINS, this.mGoldCoins);
		mEditor.commit();
	}

	public void addGoldCoins(long mGoldCoins) {
		this.mGoldCoins += mGoldCoins;
		setGoldCoins(this.mGoldCoins);
	}

	public boolean ismLeaderBoardRequired() {
		return mLeaderBoardRequired;
	}
	public void setmLeaderBoardRequired(boolean pLeaderBoardRequired) {
		this.mLeaderBoardRequired = pLeaderBoardRequired;
		mEditor.putBoolean(LEADERBOARD, mLeaderBoardRequired);
		mEditor.commit();
	}

	public boolean ismSoundRequired() {
		return mSoundRequired;
	}
	public void setmSoundRequired(boolean pSoundRequired) {
		this.mSoundRequired = pSoundRequired;
		mEditor.putBoolean(SOUND_REQUIRED, pSoundRequired);
		mEditor.commit();
	}

	public boolean ismMusicRequired() {
		return mMusicRequired;
	}

	public void setmMusicRequired(boolean pMusicRequired) {
		this.mMusicRequired = pMusicRequired;
		mEditor.putBoolean(MUSIC_REQUIRED, pMusicRequired);
		mEditor.commit();
	}
}
