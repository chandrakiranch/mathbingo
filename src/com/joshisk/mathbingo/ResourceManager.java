package com.joshisk.mathbingo;

import java.io.IOException;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;

import android.graphics.Typeface;

import com.google.android.gms.ads.InterstitialAd;
import com.joshisk.mathbingo.LevelManager.Level;
import com.joshisk.mathbingo.activity.MainGameActivity;
import com.joshisk.mathbingo.core.GameDataManager;



public class ResourceManager {

	private static final ResourceManager INSTANCE = new ResourceManager();

	public Engine engine;
	public MainGameActivity activity;
	public Camera camera;
	public VertexBufferObjectManager vbom;

	public Font cellValueFont;
	public int cellValueFontSize = 40;
	public Font scoreHeaderFont;
	public int scoreHeaderFontSize = 18;
	public Font scoreFont;
	public int scoreFontSize = 20;

	public Font resultNumberFont;
	public int resultNumberFontSize = 55;

	/* Splash Scene */
	public BitmapTextureAtlas splashScreenAtlas ;
	public ITextureRegion splashScreenTextureRegion ;
	public BitmapTextureAtlas splashBulletAtlas ;
	public ITextureRegion splashBulletTextureRegion ;
	
	public BitmapTextureAtlas backgroundAtlas ;
	public ITextureRegion backgroundTextureRegion ;

	/* Result Scene */
	public Font resultSceneFont;
	public int resultSceneFontSize = 30;

	/* MENU */
	public ITextureRegion menu_background_region;
	public BitmapTextureAtlas titleAtlas ;
	public ITextureRegion titleTextureRegion ;
	public BitmapTextureAtlas menu_play_atlas = null;
	public ITextureRegion play_region;
	public BitmapTextureAtlas menu_options_atlas = null;
	public ITextureRegion options_region;
	public BitmapTextureAtlas menu_quit_atlas = null;
	public ITextureRegion quit_region;
	public BitmapTextureAtlas menuTextureAtlas;
	public BitmapTextureAtlas soundRequiredAtlas;
	public TiledTextureRegion soundRequiredTextureRegion;
	

	public BitmapTextureAtlas optionsArrowAtlas = null; ;
	public ITextureRegion optionsArrowTextRegion = null ;
	
	public Sound mButtonPressSound;

	/* Game Objects */

	public int DEFAULT_MAXIMUM_GREENS_ALLOWED = 3;

	public BitmapTextureAtlas blueSphereAtlas ;
	public ITextureRegion blueSphereTextureRegion ;
	public BitmapTextureAtlas maroonSphereAtlas ;
	public ITextureRegion maroonSphereTextureRegion ;
	public BitmapTextureAtlas greenSphereAtlas ;
	public ITextureRegion greenSphereTextureRegion ;

	public BitmapTextureAtlas timerBackgroundAtlas ;
	public ITiledTextureRegion timerBackgroundTextureRegion ;
	public BitmapTextureAtlas hintBackgroundAtlas ;
	public ITextureRegion hintBackgroundTextureRegion ;
	public BitmapTextureAtlas scoreBackgroundAtlas ;
	public ITextureRegion scoreBackgroundTextureRegion ;
	public BitmapTextureAtlas coinAtlas ;
	public ITextureRegion coinTextureRegion ;
	public BitmapTextureAtlas playAtlas ;
	public ITextureRegion playTextureRegion ;
	public BitmapTextureAtlas pauseAtlas ;
	public ITiledTextureRegion pauseTextureRegion ;
	public BitmapTextureAtlas blackPatchAtlas ;
	public ITextureRegion blackPatchTextureRegion ;
	public BitmapTextureAtlas nextBoardAtlas ;
	public ITextureRegion nextBoardTextureRegion ;
	public BitmapTextureAtlas hintIconAtlas ;
	public ITextureRegion hintIconTextureRegion ;
	public BitmapTextureAtlas timeoutPopupAtlas ;
	public ITextureRegion timeoutPopupTextureRegion ;
	public BitmapTextureAtlas restartAtlas ;
	public ITextureRegion restartTextureRegion ;
	public BitmapTextureAtlas leaderBoardAtlas ;
	public ITextureRegion leaderBoardTextureRegion ;
	public BitmapTextureAtlas nextBoardPopupAtlas ;
	public ITextureRegion nextBoardPopupTextureRegion ;
	public BitmapTextureAtlas bingoAtlas ;
	public ITextureRegion bingoTextureRegion ;
	public BitmapTextureAtlas menuAtlas ;
	public ITextureRegion menuTextureRegion ;
	public BitmapTextureAtlas gPlusShareAtlas ;
	public ITextureRegion gPlusShareTextureRegion ;
	public BitmapTextureAtlas fbShareAtlas ;
	public ITextureRegion fbShareTextureRegion ;
	public BitmapTextureAtlas shareAtlas ;
	public ITextureRegion shareTextureRegion ;

	public Sound mBubbleBlastSound;
	public Sound mWrongOperationSound;
	public Sound mBingoSound;

	/* Game Data Manager */

	public GameDataManager gameDataManager ;

	/* Level Manager */
	public byte DEFAULT_MINIMUM_RESULT_NUMBER = 5;
	public byte DEFAULT_MAXIMUM_RESULT_NUMBER = 30;
	public byte DEFAULT_MINIMUM_GRID_NUMBER = 1;
	public byte DEFAULT_MAXIMUM_GRID_NUMBER = 60;

	public byte DEFAULT_DIVIDE_PERCENT = 10;
	public byte DEFAULT_MULTIPLY_PERCENT = 15; // How many number of this signs allowed in the board.
	public byte DEFAULT_PLUS_PERCENT = 35;			// This can be different depending on complexity
	public byte DEFAULT_MINUS_PERCENT = 40;
	public Level gameLevel = Level.EASY;

	public LevelManager levelManager;


	public static ResourceManager getInstance() {
		return INSTANCE;
	}

	public void loadMenuResources()
	{
		loadMenuGraphics();
		loadMenuAudio();
		loadGameResources();
	}

	public void unLoadMenuResources()
	{
		//menuTextureAtlas.unload();
		//menu_background_region = null;
		play_region = null;
		options_region = null;
		quit_region = null;
	}

	public void loadGameResources()
	{
		loadGameFonts();
		loadGameGraphics();
		loadGameAudio();
	}

	public void unLoadGameResources() {
		unLoadGameGraphics();
		unLoadGameFonts();
	}

	private void loadMenuGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		menuTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 500, 800, TextureOptions.REPEATING_BILINEAR);
		menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_background.png",0,0);
		menuTextureAtlas.load();

		titleAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 312, 173, TextureOptions.BILINEAR);
		titleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(titleAtlas, activity, "mathbingo_title.png",0,0);
		titleAtlas.load();

		menu_play_atlas = new BitmapTextureAtlas(activity.getTextureManager(), 247, 114, TextureOptions.BILINEAR);
		play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menu_play_atlas, activity, "play.png",0,0);
		menu_options_atlas = new BitmapTextureAtlas(activity.getTextureManager(), 247, 114, TextureOptions.BILINEAR);
		options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menu_options_atlas, activity, "options.png",0,0);
		menu_quit_atlas = new BitmapTextureAtlas(activity.getTextureManager(), 247, 114, TextureOptions.BILINEAR);
		quit_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menu_quit_atlas, activity, "quit.png",0,0);

		menu_play_atlas.load();
		menu_options_atlas.load();
		menu_quit_atlas.load();
		
		soundRequiredAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 185, 94, TextureOptions.BILINEAR);
		soundRequiredTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(soundRequiredAtlas, activity, 
				"sound.png",0, 0, 2, 1);
		soundRequiredAtlas.load();
		/*try 
		{
			this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.menuTextureAtlas.load();
		} 
		catch (final TextureAtlasBuilderException e)
		{
			Debug.e(e);
		}*/

	}

	private void loadMenuAudio() {
		SoundFactory.setAssetBasePath("sounds/");

		try{
			this.mButtonPressSound = SoundFactory.createSoundFromAsset(this.engine.getSoundManager(), 
					this.activity.getApplicationContext(), "button_press.ogg");
		} catch (final IOException e) {
			Debug.e(e);
		}
	}

	public void loadOptionsResources() {
		if(optionsArrowTextRegion != null) { //Which means we already loaded the region. We don't implement unload resources as this is small object
			return;
		}
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		optionsArrowAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 30, 34, TextureOptions.BILINEAR);
		optionsArrowTextRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsArrowAtlas, activity, "options_left_arrow.png",0,0);
		optionsArrowAtlas.load();

		FontFactory.setAssetBasePath("fonts/");

		this.resultNumberFont = FontFactory.createFromAsset(activity.getFontManager(), activity.getTextureManager(), 
				512, 512, TextureOptions.BILINEAR, activity.getAssets(), "Plok.ttf", resultNumberFontSize, true, android.graphics.Color.WHITE);
		this.resultNumberFont.load();
	}

	private void loadGameGraphics() {
		/* Create Graphics */
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		backgroundAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 800, 1280, TextureOptions.BILINEAR);
		backgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backgroundAtlas, activity, "playscreen_bg.png",0,0);
		backgroundAtlas.load();

		blueSphereAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 80, 83, TextureOptions.BILINEAR);
		blueSphereTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(blueSphereAtlas, activity, "bubble_1.png", 0, 0);
		blueSphereAtlas.load();

		maroonSphereAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 80, 83, TextureOptions.BILINEAR);
		maroonSphereTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(maroonSphereAtlas, activity, "bubble_3.png", 0, 0);
		maroonSphereAtlas.load();

		greenSphereAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 80, 83, TextureOptions.BILINEAR);
		greenSphereTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(greenSphereAtlas, activity, "bubble_4.png", 0, 0);
		greenSphereAtlas.load();

		timerBackgroundAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 287, 94, TextureOptions.BILINEAR);
		timerBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(timerBackgroundAtlas, activity,"timer_background.png", 0, 0, 3, 1);
		timerBackgroundAtlas.load();

		hintBackgroundAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 93, 94, TextureOptions.BILINEAR);
		hintBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(hintBackgroundAtlas, activity,"hint.png", 0, 0);
		hintBackgroundAtlas.load();

		scoreBackgroundAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 200, 67, TextureOptions.BILINEAR);
		scoreBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(scoreBackgroundAtlas, activity, "scorebar.png", 0, 0);
		scoreBackgroundAtlas.load();

		nextBoardAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 164, 70, TextureOptions.BILINEAR);
		nextBoardTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(nextBoardAtlas, activity, "nextboard.png", 0, 0);
		nextBoardAtlas.load();

		coinAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 44, 48, TextureOptions.BILINEAR);
		coinTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(coinAtlas, activity, "gold_coins.png", 0, 0);
		coinAtlas.load();

		/*playAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 50, 49, TextureOptions.BILINEAR);
		playTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(playAtlas, activity, "play.png", 0, 0);
		playAtlas.load();*/

		pauseAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 180, 91, TextureOptions.BILINEAR);
		pauseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(pauseAtlas, activity, "pauseplay.png", 0, 0, 2, 1);
		pauseAtlas.load();

		blackPatchAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 480, 84, TextureOptions.BILINEAR);
		blackPatchTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(blackPatchAtlas, activity, "blackpatch.png", 0, 0);
		blackPatchAtlas.load();

		/*hintIconAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 49, 85, TextureOptions.BILINEAR);
		hintIconTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(hintIconAtlas, activity, "hint_icon.png", 0, 0);
		hintIconAtlas.load();*/

		timeoutPopupAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 400, 640, TextureOptions.BILINEAR);
		timeoutPopupTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(timeoutPopupAtlas, activity, "timeoutpopup.png", 0, 0);
		timeoutPopupAtlas.load();

		/*nextBoardPopupAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 400, 419, TextureOptions.BILINEAR);
		nextBoardPopupTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(nextBoardPopupAtlas, activity, "next_board_popup.png", 0, 0);
		nextBoardPopupAtlas.load();*/

		/*restartAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 247, 114, TextureOptions.BILINEAR);
		restartTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(restartAtlas, activity, "restart.png", 0, 0);
		restartAtlas.load();*/

		leaderBoardAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 90, 94, TextureOptions.BILINEAR);
		leaderBoardTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(leaderBoardAtlas, activity, "leaderboard.png", 0, 0);
		leaderBoardAtlas.load();

		bingoAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 400, 404, TextureOptions.BILINEAR);
		bingoTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bingoAtlas, activity, "bingo.png", 0, 0);
		bingoAtlas.load();
		
		menuAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 90, 94, TextureOptions.BILINEAR);
		menuTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuAtlas, activity, "menu.png", 0, 0);
		menuAtlas.load();
		
		gPlusShareAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 64, 64, TextureOptions.BILINEAR);
		gPlusShareTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gPlusShareAtlas, activity, "gplus_share.png", 0, 0);
		gPlusShareAtlas.load();
		
		fbShareAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 64, 64, TextureOptions.BILINEAR);
		fbShareTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(fbShareAtlas, activity, "fb_icon.png", 0, 0);
		fbShareAtlas.load();
		
		shareAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 90, 90, TextureOptions.BILINEAR);
		shareTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(shareAtlas, activity, "android_share.png", 0, 0);
		shareAtlas.load();
	}

	private void unLoadGameGraphics() {
		blueSphereAtlas.unload();
		maroonSphereAtlas.unload();
		greenSphereAtlas.unload();
		timerBackgroundAtlas.unload();
		scoreBackgroundAtlas.unload();
		coinAtlas.unload();
		playAtlas.unload();
		pauseAtlas.unload();
	}

	private void loadGameFonts() {
		FontFactory.setAssetBasePath("fonts/");
		this.cellValueFont = FontFactory.createFromAsset(activity.getFontManager(), activity.getTextureManager(), 
				512, 512, TextureOptions.BILINEAR, activity.getAssets(), "ANJA_ELIANE.ttf", cellValueFontSize, true, android.graphics.Color.WHITE);
		this.cellValueFont.load();

		this.scoreHeaderFont = FontFactory.createFromAsset(activity.getFontManager(), activity.getTextureManager(), 
				512, 512, TextureOptions.BILINEAR, activity.getAssets(), "ANJA_ELIANE.ttf", cellValueFontSize, true, android.graphics.Color.WHITE);
		this.scoreHeaderFont.load();

		this.scoreFont = FontFactory.createFromAsset(activity.getFontManager(), activity.getTextureManager(), 
				512, 512, TextureOptions.BILINEAR, activity.getAssets(), "ANJA_ELIANE.ttf", cellValueFontSize, true, android.graphics.Color.WHITE);
		this.scoreFont.load();

		this.resultNumberFont = FontFactory.createFromAsset(activity.getFontManager(), activity.getTextureManager(), 
				512, 512, TextureOptions.BILINEAR, activity.getAssets(), "Plok.ttf", resultNumberFontSize, true, android.graphics.Color.WHITE);
		this.resultNumberFont.load();
	}

	private void unLoadGameFonts() {
		//this.cellValueFont.unload();
		this.scoreHeaderFont.unload();
		this.scoreFont.unload();
		this.resultNumberFont.unload();
	}

	private void loadGameAudio() {
		SoundFactory.setAssetBasePath("sounds/");

		try{
			this.mBubbleBlastSound = SoundFactory.createSoundFromAsset(this.engine.getSoundManager(), 
					this.activity.getApplicationContext(), "bubble-blast-1.ogg");
			this.mWrongOperationSound = SoundFactory.createSoundFromAsset(this.engine.getSoundManager(), 
					this.activity.getApplicationContext(), "wrong-operation.ogg");
			this.mBingoSound = SoundFactory.createSoundFromAsset(this.engine.getSoundManager(), 
					this.activity.getApplicationContext(), "bingo.ogg");
		} catch (final IOException e) {
			Debug.e(e);
		}
	}

	public void loadSplashScreen() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashScreenAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 500, 800, TextureOptions.BILINEAR);
		splashScreenTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashScreenAtlas, activity, "splash.png",0,0);
		splashScreenAtlas.load();
		splashBulletAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 93, 85, TextureOptions.BILINEAR);
		splashBulletTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashBulletAtlas, activity, "bulletin.png",0,0);
		splashBulletAtlas.load();
	}

	public void unloadSplashScreen() {
		splashScreenAtlas.unload();
	}

	public void loadLevelSceneResources() {

	}

	public void unLoadLevelSceneResources() {

	}

	public void loadResultSceneResources() {
		loadResultSceneFonts();
	}
	public void unloadResultSceneResources() {
		unloadResultSceneFonts();
	}

	public void loadResultSceneFonts() {
		FontFactory.setAssetBasePath("fonts/");
		this.resultSceneFont = FontFactory.create(activity.getFontManager(), activity.getTextureManager(), 1024, 50, 
				TextureOptions.BILINEAR, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), resultSceneFontSize, Color.WHITE_ABGR_PACKED_INT);
		this.resultSceneFont.load();
	}

	public void unloadResultSceneFonts() {
		this.resultSceneFont.unload();
	}


	/**
	 * @param engine
	 * @param activity
	 * @param camera
	 * @param vbom
	 * <br><br>
	 * We use this method at beginning of game loading, to prepare Resources Manager properly,
	 * setting all needed parameters, so we can latter access them from different classes (eg. scenes)
	 */
	public static void prepareManager(Engine engine, MainGameActivity activity, Camera camera, VertexBufferObjectManager vbom)
	{
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
		getInstance().gameDataManager = new GameDataManager(activity.getApplicationContext());
		//getInstance().gameDataManager = GameDataManager.getInstance();
		getInstance().gameLevel = getInstance().gameDataManager.getmLevel();
		getInstance().levelManager = new LevelManager();

		getInstance().levelManager.setLevelParameters();
		
	}
}
