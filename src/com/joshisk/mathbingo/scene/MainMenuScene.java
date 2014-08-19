package com.joshisk.mathbingo.scene;

import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.google.android.gms.games.Games;
import com.joshisk.mathbingo.BaseScene;
import com.joshisk.mathbingo.ResourceManager;
import com.joshisk.mathbingo.SceneManager;
import com.joshisk.mathbingo.LevelManager.Level;
import com.joshisk.mathbingo.SceneManager.SceneType;
import com.joshisk.mathbingo.activity.MainGameActivity;
import com.joshisk.mathbingo.utils.Utilities;
import com.mobeyond.mathbingo.R;

public class MainMenuScene extends BaseScene implements 
IOnMenuItemClickListener {

	private static final ResourceManager RM = ResourceManager.getInstance();

	private static final int REQUEST_ACHIEVEMENTS = 10;
	
	private MenuScene menuChildScene;
	private final int MENU_PLAY = 0;
	private final int MENU_OPTIONS = 1;
	private final int MENU_QUIT = 2;
	TiledSprite soundSprite;
	
	private void createBackground()
	{
	    setBackground(new SpriteBackground(new Sprite(MainGameActivity.CAMERA_WIDTH/2, MainGameActivity.CAMERA_HEIGHT/2, 
	    		RM.menu_background_region, vbom)));
		
	    attachChild(new Sprite(MainGameActivity.CAMERA_WIDTH/2, 3 * MainGameActivity.CAMERA_HEIGHT/4 + 50, 
	    		RM.titleTextureRegion, vbom));
	}
	
	private void createMenuChildSceneOld()
	{
	    menuChildScene = new MenuScene(camera);
	    menuChildScene.setPosition(0, 0);
	    
	    final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, RM.play_region, vbom), 1f, 0.75f);
	    final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_OPTIONS, RM.options_region, vbom), 1f, 0.75f);
	    final IMenuItem quitMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_QUIT, RM.quit_region, vbom), 1f, 0.75f);
	    
	    /*playMenuItem.setScale(0.75f);
	    optionsMenuItem.setScale(0.75f);
	    quitMenuItem.setScale(0.75f);*/
	    menuChildScene.addMenuItem(playMenuItem);
	    menuChildScene.addMenuItem(optionsMenuItem);
	    menuChildScene.addMenuItem(quitMenuItem);
	    
	    menuChildScene.buildAnimations();
	    menuChildScene.setBackgroundEnabled(false);
	    
//	    playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY());
//	    optionsMenuItem.setPosition(optionsMenuItem.getX(), optionsMenuItem.getY());
//	    optionsMenuItem.setPosition(quitMenuItem.getX(), quitMenuItem.getY());
	    
	    menuChildScene.setOnMenuItemClickListener(this);
	    
	    setChildScene(menuChildScene);
	    
	}

	private void createMenuChildScene() {
		Sprite playSprite = new Sprite(MainGameActivity.CAMERA_WIDTH/2, MainGameActivity.CAMERA_HEIGHT/2 + 50, 
				RM.play_region, vbom) {
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					this.setScale(0.75f);
				} else if (pSceneTouchEvent.isActionUp()) {
					this.setScale(1f);
					Utilities.playButtonSound();
					SceneManager.getInstance().createGameScene();
				} else if(pSceneTouchEvent.isActionOutside()) {
					this.setScale(1f);
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		Sprite optionsSprite = new ButtonSprite(MainGameActivity.CAMERA_WIDTH/2, MainGameActivity.CAMERA_HEIGHT/2 - 50, 
				RM.options_region, vbom) {
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					this.setScale(0.75f);
				} else if (pSceneTouchEvent.isActionUp()) {
					this.setScale(1f);
					Utilities.playButtonSound();
					SceneManager.getInstance().createOptionsScene();
				} else if(pSceneTouchEvent.isActionOutside()) {
					this.setScale(1f);
				}

				return true;
			}
		};
		Sprite quitSprite = new Sprite(MainGameActivity.CAMERA_WIDTH/2, MainGameActivity.CAMERA_HEIGHT/2 - 150, 
				RM.quit_region, vbom) {
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					this.setScale(0.75f);
					Utilities.playButtonSound();
				} else if (pSceneTouchEvent.isActionUp()) {
					this.setScale(1f);
					RM.activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {

							AlertDialog.Builder alert = new AlertDialog.Builder(RM.activity);
							alert.setTitle("Quit");
							alert.setMessage("Do you really want to quit the game");
							alert.setPositiveButton("Exit", new OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									System.exit(0);
								}
							});

							alert.setNegativeButton("Stay Back", new OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
								}
							});
							alert.show();
						}
					});
				} else if(pSceneTouchEvent.isActionOutside()) {
					this.setScale(1f);
				}

				return true;
			}
		};
		soundSprite = new TiledSprite(50, MainGameActivity.CAMERA_HEIGHT/2 - 250, 
				RM.soundRequiredTextureRegion, RM.vbom) {
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					this.setScale(0.75f);
				} else if (pSceneTouchEvent.isActionUp()) {
					this.setScale(1f);
					
					RM.gameDataManager.setmSoundRequired(!RM.gameDataManager.ismSoundRequired());
					
					if(RM.gameDataManager.ismSoundRequired()) {
						setCurrentTileIndex(1);
					}  else {
						setCurrentTileIndex(0);
					}
					
					Utilities.playButtonSound();
				}
				return true;
			}
		};
		if(RM.gameDataManager.ismSoundRequired())
			soundSprite.setCurrentTileIndex(1);
		else 
			soundSprite.setCurrentTileIndex(0);
		
		Sprite leaderBoardSprite = new Sprite(MainGameActivity.CAMERA_WIDTH - 50, MainGameActivity.CAMERA_HEIGHT/2 - 250, 
				RM.leaderBoardTextureRegion, RM.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					this.setScale(0.75f);
				} else if (pSceneTouchEvent.isActionUp()) {
					this.setScale(1f);
					Utilities.playButtonSound();
					if(!RM.activity.isSignIn()) {
						RM.activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {

								AlertDialog.Builder alert = new AlertDialog.Builder(RM.activity);
								alert.setTitle("Not Signed In");
								alert.setMessage("Not Signed Into Google\nCheck your internet connection");
								alert.setPositiveButton("Try again", new OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										RM.activity.signInToGooglePlus();
									}
								});

								alert.setNegativeButton("Cancel", new OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										// Do Nothing
									}
								});
								alert.show();
							}
						});
					}

					if(RM.activity.isSignIn()) {
						if(!Utilities.isNetworkAvailable()) {
							RM.activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {

									AlertDialog.Builder alert = new AlertDialog.Builder(RM.activity);
									alert.setTitle("No Internet");
									alert.setMessage("No Internet connection available");
									alert.setPositiveButton("OK", new OnClickListener() {
										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											showLeaderBoard();
										}
									});

									alert.show();
								}
							});
						} else {
							showLeaderBoard();
						}
					}
				}
				return true;
			}
		};
		
		Sprite achivementSprite = new Sprite(MainGameActivity.CAMERA_WIDTH - 150, MainGameActivity.CAMERA_HEIGHT/2 - 250, 
				RM.leaderBoardTextureRegion, RM.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					this.setScale(0.75f);
				} else if (pSceneTouchEvent.isActionUp()) {
					this.setScale(1f);
					Utilities.playButtonSound();
					if(!RM.activity.isSignIn()) {
						RM.activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {

								AlertDialog.Builder alert = new AlertDialog.Builder(RM.activity);
								alert.setTitle("Not Signed In");
								alert.setMessage("Not Signed Into Google\nCheck your internet connection");
								alert.setPositiveButton("Try again", new OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										RM.activity.signInToGooglePlus();
									}
								});

								alert.setNegativeButton("Cancel", new OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										// Do Nothing
									}
								});
								alert.show();
							}
						});
					}

					if(RM.activity.isSignIn()) {
						if(!Utilities.isNetworkAvailable()) {
							RM.activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {

									AlertDialog.Builder alert = new AlertDialog.Builder(RM.activity);
									alert.setTitle("No Internet");
									alert.setMessage("No Internet connection available");
									alert.setPositiveButton("OK", new OnClickListener() {
										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											showAchivements();
										}
									});

									alert.show();
								}
							});
						} else {
							showAchivements();
						}
					}
				}
				return true;
			}
		};
		
		attachChild(playSprite);
		attachChild(optionsSprite);
		attachChild(quitSprite);
		attachChild(soundSprite);
		attachChild(leaderBoardSprite);
		attachChild(achivementSprite);
		
		registerTouchArea(playSprite);
		registerTouchArea(optionsSprite);
		registerTouchArea(quitSprite);
		registerTouchArea(soundSprite);
		registerTouchArea(leaderBoardSprite);
		registerTouchArea(achivementSprite);
	}
	
	@Override
	public void createScene() {
		createBackground();
		createMenuChildScene();
		RM.activity.signInToGooglePlus();
		RM.activity.setAdMobInVisibile();
	}

	@Override
	public void onBackKeyPressed() {
		Utilities.playButtonSound();
		RM.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {

				AlertDialog.Builder alert = new AlertDialog.Builder(RM.activity);
				alert.setTitle("Quit");
				alert.setMessage("Do you really want to quit the game");
				alert.setPositiveButton("Exit", new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						System.exit(0);
					}
				});

				alert.setNegativeButton("Stay Back", new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});
				alert.show();
			}
		});
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_MENU;
	}

	@Override
	public void disposeScene() {
		//ResourceManager.getInstance().unLoadMenuResources();
		//menuChildScene.dispose();
		//dispose();
	}
	
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
	{
	        switch(pMenuItem.getID())
	        {
	        case MENU_PLAY:
	        	SceneManager.getInstance().createGameScene();
	            return true;
	        case MENU_OPTIONS:
	        	SceneManager.getInstance().createOptionsScene();
	            return true;
	        case MENU_QUIT:
	            System.exit(0);
	        default:
	            return false;
	    }
	}

	@Override
	public void loadScene() {
		if(RM.gameDataManager.ismSoundRequired())
			soundSprite.setCurrentTileIndex(1);
		else 
			soundSprite.setCurrentTileIndex(0);
	}
	
	private void showAchivements() {
		RM.activity.startActivityForResult(Games.Achievements.getAchievementsIntent(RM.activity.getApiClient()), REQUEST_ACHIEVEMENTS);
	}
	
	private void showLeaderBoard() {
		if(RM.gameDataManager.getmLevel() == Level.EASY) {
			RM.activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
					RM.activity.getApiClient(), 
					RM.activity.getString(R.string.easy_leader_board)), 
					2);
		} else if(RM.gameDataManager.getmLevel() == Level.MEDIUM) {
			RM.activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
					RM.activity.getApiClient(), 
					RM.activity.getString(R.string.medium_leader_board)), 
					2);
		} else if(RM.gameDataManager.getmLevel() == Level.HARD) {
			RM.activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
					RM.activity.getApiClient(), 
					RM.activity.getString(R.string.hard_leader_board)), 
					2);
		}
	}
}
