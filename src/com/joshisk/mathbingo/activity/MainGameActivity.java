package com.joshisk.mathbingo.activity;

import java.io.IOException;
import java.util.Random;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.view.RenderSurfaceView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.example.games.basegameutils.GBaseGameActivity;
import com.joshisk.mathbingo.ResourceManager;
import com.joshisk.mathbingo.SceneManager;
import com.mobeyond.mathbingo.R;

public class MainGameActivity extends GBaseGameActivity {

	public static int CAMERA_WIDTH = 480;
	public static int CAMERA_HEIGHT = 800;
	
	private Camera camera;

	FrameLayout frameLayout;
	AdView adView;
	InterstitialAd interstitial;
	//Tracker tracker;
	
	@Override protected void onSetContentView() {
		// CREATING the parent FrameLayout //
		frameLayout = new FrameLayout(this);

		// CREATING the layout parameters, fill the screen //
		final FrameLayout.LayoutParams frameLayoutLayoutParams =
				new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
						FrameLayout.LayoutParams.MATCH_PARENT);

		// CREATING a Smart Banner View //
		this.adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(getString(R.string.game_scene_ads));
		adView.setBackgroundColor(Color.TRANSPARENT);
		// Doing something I'm not 100% sure on, but guessing by the name //
		adView.refreshDrawableState();
		adView.setVisibility(AdView.INVISIBLE);

		// ADVIEW layout, show at the bottom of the screen //
		final FrameLayout.LayoutParams adViewLayoutParams =
				new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
						FrameLayout.LayoutParams.WRAP_CONTENT,
						Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);

		adViewLayoutParams.topMargin = 0;
		
		// REQUEST an ad (Test ad) //
		AdRequest adRequest = new AdRequest.Builder()
		.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		.addTestDevice("66F1E1AEAA58A62AB08FD1E439111D8A")
		.build();
		adView.loadAd(adRequest);

		interstitial = new InterstitialAd(getApplicationContext());
		interstitial.setAdUnitId(getString(R.string.game_scene_full_screen_ads));
		interstitial.loadAd(adRequest);
		
		// RENDER the add on top of the scene //
		this.mRenderSurfaceView = new RenderSurfaceView(this);
		mRenderSurfaceView.setRenderer(mEngine, this);

		// SURFACE layout ? //
		final android.widget.FrameLayout.LayoutParams surfaceViewLayoutParams =
				new FrameLayout.LayoutParams(super.createSurfaceViewLayoutParams());

		// ADD the surface view and adView to the frame //
		frameLayout.addView(this.mRenderSurfaceView, surfaceViewLayoutParams);
		frameLayout.addView(adView, adViewLayoutParams);

		// SHOW AD //
		this.setContentView(frameLayout, frameLayoutLayoutParams);
		//this.setContentView(this.mRenderSurfaceView, surfaceViewLayoutParams);
		//setAdMobInVisibile();
	}
	
	@Override
    protected void onCreate(Bundle b) {
		super.onCreate(b);
		mHelper.setMaxAutoSignInAttempts(0);
		//tracker = GoogleAnalytics.getInstance(getApplicationContext()).newTracker(R.string.analytics_usage_count_tracker_id);
	}
	
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) 
	{
		return new LimitedFPSEngine(pEngineOptions, 60);
	}
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, 1500, 1500, 2f);
		/*EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, 
				new RatioResolutionPolicy(MainGameActivity.CAMERA_WIDTH, MainGameActivity.CAMERA_HEIGHT) , camera);*/
	
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, 
				new FillResolutionPolicy() , camera);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		engineOptions.getAudioOptions().setNeedsSound(true);
		engineOptions.getRenderOptions().setDithering(true);
		return engineOptions;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws IOException {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		ResourceManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
		//resourcesManager = ResourceManager.getInstance();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException {
		//SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
		//SceneManager.getInstance().createGameScene();
		//SceneManager.getInstance().createMenuScene();
		SceneManager.getInstance().createSplashScene();
		pOnCreateSceneCallback.onCreateSceneFinished(ResourceManager.getInstance().engine.getScene());
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws IOException {
		/*mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() 
		{
			public void onTimePassed(final TimerHandler pTimerHandler) 
			{
				mEngine.unregisterUpdateHandler(pTimerHandler);
				SceneManager.getInstance().createMenuScene();
			}
		}));*/
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{  
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
		}
		return false; 
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		System.exit(0);	
	}
	

	public void setAdMobInterstetialVisiable() {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				
				/* To show ad have only 1 out of 10 chances 
				 * This can be increased in next update
				 */
				Random rand = new Random();
				int random = rand.nextInt(5);
				if(random != 3) {
					//System.out.println("DEBUG: Interstitial not showing "+ random);
					return;
				}
				
				AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("66F1E1AEAA58A62AB08FD1E439111D8A")
				.build();
				interstitial.loadAd(adRequest);
				
				if (interstitial.isLoaded()) {
					interstitial.show();
				} else {
					System.out.println("DEBUG: Interstitial not loaded");
				}
			}
		});
	}
	
	public void setAdMobInVisibile() {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adView.setVisibility(AdView.INVISIBLE);
				//frameLayout.removeView(adView);
			}
		});
	}

	public void setAdMobVisibile() {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adView.setVisibility(AdView.VISIBLE);
				//frameLayout.addView(adView);
			}
		});
	}

	@Override
	public void onSignInFailed() {
		//toastOnUiThread("Sign in failed",3);
	}

	@Override
	public void onSignInSucceeded() {
		//toastOnUiThread("Sign in Success",3);
	}

	public void signInToGooglePlus() {
		beginUserInitiatedSignIn();
	}
	
	public void signOutOfGooglePlus() {
		signOut();
	}
	
	public boolean isSignIn() {
		return isSignedIn();
	}
	
	@Override
	public GoogleApiClient getApiClient() {
		return super.getApiClient();
	}
	
	
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            //Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }
	
}
