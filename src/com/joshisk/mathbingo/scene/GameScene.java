package com.joshisk.mathbingo.scene;

import java.util.ArrayList;
import java.util.Iterator;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.color.Color;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.PlusShare;
import com.joshisk.mathbingo.BaseScene;
import com.joshisk.mathbingo.LevelManager.Level;
import com.joshisk.mathbingo.ResourceManager;
import com.joshisk.mathbingo.SceneManager;
import com.joshisk.mathbingo.SceneManager.SceneType;
import com.joshisk.mathbingo.activity.MainGameActivity;
import com.joshisk.mathbingo.core.Board;
import com.joshisk.mathbingo.core.FacebookManager;
import com.joshisk.mathbingo.core.Operation;
import com.joshisk.mathbingo.sprites.BingoSprite;
import com.joshisk.mathbingo.sprites.CellSprite;
import com.joshisk.mathbingo.sprites.CoinSprite;
import com.joshisk.mathbingo.sprites.HintSprite;
import com.joshisk.mathbingo.sprites.NextBoardSprite;
import com.joshisk.mathbingo.sprites.ScoreSprite;
import com.joshisk.mathbingo.sprites.ShareButton;
import com.joshisk.mathbingo.sprites.TimeoutSprite;
import com.joshisk.mathbingo.sprites.TimerSprite;
import com.joshisk.mathbingo.utils.Utilities;
import com.mobeyond.mathbingo.R;

public class GameScene extends BaseScene {

	private static final int BOARD_CLEAR_SCORE_BONUS = 200;
	private static final int BOARD_CLEAR_COINS = 1;
	private static final int COINS_TO_SPEND_FOR_NEXT_BOARD = 5;
	private static final int TIME_INCREASE_ON_BOARD_CLEAR = 45;
	private static final int SCORE_INCREASE = 100;
	private static final int GREEN_BUBBLE_SCORE_INCREASE = 150;
	private static final int TIME_EXTENSION_REQUIRE_COINS = 2;
	private static final ResourceManager RM = ResourceManager.getInstance();
	private static final GoogleApiClient API_CLIENT = RM.activity.getApiClient();

	private static final int GAME_OVER_TIME = 120;
	private static final int BONUS_FOR_ATLEAST_3_BOARDS_CLEAR = 2;
	private static final int BONUS_FOR_ATLEAST_1_BOARD_CLEAR = 1;
	private static final int TIME_EXTENSIONS_ALLOWED = 2;

	int startPositionX = 0;
	int startPositionY = 220;

	int marginBetweenCells = 70;

	public HUD mHUD;
	Board board;

	private int operand1;
	private int operand2;
	private int operator;
	private int currentCellTouches = 0;
	private int boardsCleared = 0;
	private int timeExtensionCount = 0;

	private ArrayList<CellSprite> allCellSpritesInBoard; 
	private ArrayList<CellSprite> unmodifiedAllCellSpritesInBoard;
	private ArrayList<Boolean> unmodifiedAllCellSpritesIsOnBoard;
	private CellSprite selectedCellSprite[] = new CellSprite[3];
	private TimerSprite timerSprite;
	private ScoreSprite scoreSprite;
	private CoinSprite coinSprite;
	//Sprite playSprite = new Sprite(70, 70, RM.play_region, RM.vbom);
	TiledSprite pauseSprite;
	HintSprite hintSprite;
	NextBoardSprite nextBoardSprite;
	TiledSprite soundSprite;

	private Text resultNumberText;
	private ArrayList<Integer> playedResultNumbers;

	private boolean isGameOver = false;
	private boolean isGamePaused = false;
	private boolean isSoundRequired = true;

	private boolean isGameStarted = false;

	private void rateTheApp() {
		if(RM.gameDataManager.ismDontAskToRate())
			return;
		if(RM.gameDataManager.getmNoOfGamesPlayed() > 1 &&
				RM.gameDataManager.getmNoOfGamesPlayed() % 3 == 0) {
			RM.activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {

					AlertDialog.Builder alert = new AlertDialog.Builder(RM.activity);
					alert.setTitle("Rate Us");
					alert.setMessage("If you like the game,\nPlease rate us and Get 5 Coins");
					alert.setPositiveButton("Rate Us", new OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							final Uri uri = Uri.parse("market://details?id=" + RM.activity.getApplicationContext().getPackageName());
							final Intent rateAppIntent = new Intent(Intent.ACTION_VIEW, uri);

							if (RM.activity.getPackageManager().queryIntentActivities(rateAppIntent, 0).size() > 0)
							{
								RM.activity.startActivity(rateAppIntent);
								RM.gameDataManager.setmDontAskToRate(true);
								coinSprite.setCoins(coinSprite.getCoins() + 5);
								RM.gameDataManager.addGoldCoins(5);
							} else {
								/* handle your error case: the device has no way to handle market urls */
							}	
						}
					});

					alert.setNeutralButton("Not Now", new OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// Do Nothing
						}
					});
					alert.setNegativeButton("Don't ask", new OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							RM.gameDataManager.setmDontAskToRate(true);
						}
					});
					alert.show();
				}
			});
		}
	}

	private void tapToStart() {
		final Rectangle rect = new Rectangle(MainGameActivity.CAMERA_WIDTH/2, MainGameActivity.CAMERA_HEIGHT/2,
				MainGameActivity.CAMERA_WIDTH, MainGameActivity.CAMERA_HEIGHT, RM.vbom);
		rect.setColor(Color.BLACK);
		rect.setAlpha(0.4f);

		final Text text = new Text(MainGameActivity.CAMERA_WIDTH/2, MainGameActivity.CAMERA_HEIGHT/2,
				RM.cellValueFont, "Tap Here to", RM.vbom);

		text.setColor(Color.YELLOW);

		final Text text1 = new Text(MainGameActivity.CAMERA_WIDTH/2, MainGameActivity.CAMERA_HEIGHT/2 - 50,
				RM.cellValueFont, "Start", RM.vbom);

		text1.setColor(Color.YELLOW);

		attachChild(rect);
		attachChild(text);
		attachChild(text1);

		setOnSceneTouchListener(new IOnSceneTouchListener() {

			@Override
			public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {

				if (pSceneTouchEvent.isActionUp()) {
					isGameStarted = true;
					rm.activity.runOnUpdateThread(new Runnable() {

						@Override
						public void run() {
							detachChild(rect);
							detachChild(text);
							detachChild(text1);
							unpauseGame();
							registerTouchArea(pauseSprite);
						}
					});
					timerSprite.start();
					resultNumberText.setVisible(true);
					pScene.setOnSceneTouchListener(null);
				}
				return true;
			}
		});
	}
	@Override
	public void createScene() {
		/* Data Initialisation */
		RM.activity.setAdMobInterstetialVisiable();
		rateTheApp();
		RM.gameDataManager.setmNoOfGamesPlayed(RM.gameDataManager.getmNoOfGamesPlayed() + 1);

		allCellSpritesInBoard = new ArrayList<CellSprite>();
		unmodifiedAllCellSpritesInBoard = new ArrayList<CellSprite>();
		unmodifiedAllCellSpritesIsOnBoard = new ArrayList<Boolean>();
		playedResultNumbers = new ArrayList<Integer>();

		// TODO: This positions should be calculated dynamically depending on size of cells
		startPositionX = 0;
		startPositionY = 220;

		marginBetweenCells = 70;

		RM.levelManager.setLevelParameters();

		/* Scene */
		//setBackground(new Background(0,0,0,100));
		SpriteBackground backgroundSprite = new SpriteBackground(new Sprite(MainGameActivity.CAMERA_WIDTH/2, MainGameActivity.CAMERA_HEIGHT/2, 
				RM.backgroundTextureRegion, vbom));
		//backgroundSprite.setAlpha(0.3f);
		//attachChild(backgroundSprite);
		setBackground(backgroundSprite);
		//setBackgroundEnabled(true);


		createHUD();

		// Bottom line after result number
		/*Line line = new Line(0, startPositionY - 100, 
				MainGameActivity.CAMERA_WIDTH, startPositionY - 100, RM.vbom);
		line.setColor(Color.YELLOW);
		attachChild(line);*/

		//createBoard();
		createNewBoard();
		nextBoardSprite = new NextBoardSprite(400, 200, true) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					this.setScale(0.65f);
				} else if (pSceneTouchEvent.isActionUp()) {
					this.setScale(0.8f);
					Utilities.playButtonSound();

					//createNewBoard();
					//attachChild(new NextBoardPopupSprite());
					RM.activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {

							AlertDialog.Builder alert = new AlertDialog.Builder(RM.activity);
							//alert.setTitle("Not cleared");
							alert.setMessage("Board Not cleared\nUse "+COINS_TO_SPEND_FOR_NEXT_BOARD+" coins to go to next board");
							alert.setPositiveButton("Use "+COINS_TO_SPEND_FOR_NEXT_BOARD+" coins", new OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									if(coinSprite.getCoins() < COINS_TO_SPEND_FOR_NEXT_BOARD) {
										AlertDialog.Builder alert1 = new AlertDialog.Builder(RM.activity);
										alert1.setMessage("Not enough coins");
										alert1.setPositiveButton("OK", new OnClickListener() {
											@Override
											public void onClick(DialogInterface arg0, int arg1) {
												//Do nothing
											}
										});
										alert1.show();
										return;
									}
									coinSprite.setCoins(coinSprite.getCoins() - COINS_TO_SPEND_FOR_NEXT_BOARD);
									RM.gameDataManager.addGoldCoins(-COINS_TO_SPEND_FOR_NEXT_BOARD);
									timerSprite.incrementTime(TIME_INCREASE_ON_BOARD_CLEAR);
									RM.activity.runOnUpdateThread(new Runnable() {
										@Override
										public void run() {
											createNewBoard();
										}
									});
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
				} else if(pSceneTouchEvent.isActionOutside()) {
					this.setScale(0.8f);
				}

				return true;
			}
		};

		hintSprite = new HintSprite(130, 200, true) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(!isEnabled())
					return true;

				if(pSceneTouchEvent.isActionDown()) {
					this.setScale(0.6f);
				} else if (pSceneTouchEvent.isActionUp()) {
					this.setScale(0.80f);
					Utilities.playButtonSound();
					unregisterTouchArea(this);

					int hint[];
					int spritesHighlighted = 0;

					CellSprite operand1CellSprite = null;
					CellSprite operatorCellSprite = null;
					CellSprite operand2CellSprite = null;
					for (int i = 0; i < board.allOperations.size(); i++) {
						Operation operation = board.allOperations.get(i);
						operand1CellSprite = null;
						operatorCellSprite = null;
						operand2CellSprite = null;

						System.out.println("DEBUG: "+operation.toString());
						for (int j=0; j < unmodifiedAllCellSpritesInBoard.size(); j++) {
							if(!((Boolean)unmodifiedAllCellSpritesIsOnBoard.get(j)).booleanValue())
								continue;

							CellSprite cellSprite = (CellSprite) unmodifiedAllCellSpritesInBoard.get(j);
							System.out.println("DEBUG: "+cellSprite.getValue());
							if(operand1CellSprite == null && 
									cellSprite.getValueInt() == operation.getOperand1()) {
								operand1CellSprite = cellSprite;
								System.out.println("DEBUG: Matching operand1 "+cellSprite.getValue());
							} else if(operand2CellSprite == null && 
									cellSprite.getValueInt() == operation.getOperand2()) {
								operand2CellSprite = cellSprite;
								System.out.println("DEBUG: Matching operand2 "+cellSprite.getValue());
							} else if(operatorCellSprite == null && 
									cellSprite.getValueInt() == operation.getOperator()) {
								operatorCellSprite = cellSprite;
								System.out.println("DEBUG: Matching operator "+cellSprite.getValue());
							}

							if(operand1CellSprite != null 
									&& operand2CellSprite != null
									&& operatorCellSprite != null ) {
								break;
							}
						}
						if(operand1CellSprite != null 
								&& operand2CellSprite != null
								&& operatorCellSprite != null ) {
							break;
						}
					}


					if(operand1CellSprite == null 
							|| operand2CellSprite == null
							|| operatorCellSprite == null ) {
						//Looks like all pre-decided operations are over
						//  we need to guess the hint from the available sprites
						System.out.println("DEBUG: Didn't match any thing");

					}

					if(operand1CellSprite != null 
							&& operand2CellSprite != null
							&& operatorCellSprite != null ) {

						this.disable();
						final CellSprite hintTemp[] = new CellSprite[] { operand1CellSprite, operatorCellSprite ,operand2CellSprite };
						/*do {
						hint = board.getHint();*/
						for (int i = 0; i < hintTemp.length; i++) {
							/* TODO: This has defect, as cell sprites be removed from 
							allCellSpritesInBoard while the user is playing, using the
							hint values my occur ArrayIndexOutOfBounds exception or cause infinite loop
							 */
							CellSprite cellSprite = hintTemp[i];
							if(cellSprite == null)
								break;
							cellSprite.highlight();
							spritesHighlighted ++;
						}

						//final int hintTemp[] = hint;

						registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() {

							@Override
							public void onTimePassed(TimerHandler pTimerHandler) {
								/*for (int i = 0; i < hintTemp.length; i++) {
								CellSprite cellSprite = unmodifiedAllCellSpritesInBoard.get(hintTemp[i]);
								cellSprite.removeHighLight();
							} */
								for (int i = 0; i < hintTemp.length; i++) {
									CellSprite cellSprite = hintTemp[i];
									if(cellSprite != null)
										cellSprite.removeHighLight();
								}
							}
						}));
					}

				} else if(pSceneTouchEvent.isActionOutside()) {
					this.setScale(0.85f);
				}

				return true;
			}
		};
		/* Game Monitor Thread */
		registerUpdateHandler(new TimerHandler(1f, true, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				if(!isGameStarted) {
					return;
				}

				if(isGameOver) {
					pTimerHandler.setAutoReset(false);
					return;
				}

				if(boardsCleared > 1 && boardsCleared % 3 == 0) {
					coinSprite.setCoins(coinSprite.getCoins() + BONUS_FOR_ATLEAST_3_BOARDS_CLEAR);
					RM.gameDataManager.addGoldCoins(BONUS_FOR_ATLEAST_3_BOARDS_CLEAR);
					boardsCleared = 0;
				}

				if(timerSprite.isTimerExpried()) { /* Timer is expired, so game is over */
					RM.gameDataManager.setmScore(scoreSprite.getScore());


					if(boardsCleared == 0 && 
							allCellSpritesInBoard.size() <= (board.getSize() * board.getSize())/2) {
						coinSprite.setCoins(coinSprite.getCoins() + BONUS_FOR_ATLEAST_1_BOARD_CLEAR);
						RM.gameDataManager.addGoldCoins(BONUS_FOR_ATLEAST_1_BOARD_CLEAR);
					}

					isGameOver = true;
					//SceneManager.getInstance().createResultScene();
					gameOver();
					return;
				}

				/* User finished the board. So load the 
				 * new board as the time is not yet over */
				if(allCellSpritesInBoard.size() == 0) {

					incrementBoardClear();

					Text boardClearBonus = new Text(RM.scoreBackgroundTextureRegion.getWidth()/2, RM.scoreBackgroundTextureRegion.getHeight()/2, 
							RM.scoreFont, String.valueOf(BOARD_CLEAR_SCORE_BONUS),
							String.valueOf(BOARD_CLEAR_SCORE_BONUS).length(), RM.activity.getVertexBufferObjectManager());
					boardClearBonus.setScale(1f);

					boardClearBonus.registerEntityModifier(new ParallelEntityModifier(
							new AlphaModifier(2f, 1f, 0.2f),
							new MoveModifier(2f, MainGameActivity.CAMERA_WIDTH/2, MainGameActivity.CAMERA_HEIGHT/2, 
									timerSprite.getX(), timerSprite.getY()) 
							){
						@Override
						public void onModifierFinished(final IEntity pItem) {
							super.onModifierFinished(pItem);
							scoreSprite.addScore(BOARD_CLEAR_SCORE_BONUS);
							RM.activity.runOnUpdateThread(new Runnable() {
								@Override
								public void run() {
									SceneManager.getInstance().getCurrentScene().detachChild(pItem);
								}
							});
						}
					});
					attachChild(boardClearBonus);
					attachChild(new BingoSprite());
					timerSprite.incrementTime(TIME_INCREASE_ON_BOARD_CLEAR);
					coinSprite.setCoins(coinSprite.getCoins() + BOARD_CLEAR_COINS);
					RM.gameDataManager.addGoldCoins(BOARD_CLEAR_COINS);
					createNewBoard();
					sortChildren();
				}
				if(hintSprite.isEnabled()) {
					if(!isPause)
						registerTouchArea(hintSprite);
					else
						unregisterTouchArea(hintSprite);
				}
				else {
					unregisterTouchArea(hintSprite);
				}
			}

		}));

		pauseSprite = new TiledSprite(MainGameActivity.CAMERA_WIDTH/2, MainGameActivity.CAMERA_HEIGHT/2 - 285, RM.pauseTextureRegion, RM.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					this.setScale(0.6f);
				} else if (pSceneTouchEvent.isActionUp()) {
					this.setScale(0.85f);
					Utilities.playButtonSound();
					if(!isGamePaused){
						pauseGame();
						pauseSprite.setCurrentTileIndex(1);
					} else {
						unpauseGame();
						pauseSprite.setCurrentTileIndex(0);
					}
				}
				return true;
			}
		};

		pauseSprite.setScale(0.85f);

		soundSprite = new TiledSprite(50, 198, 
				RM.soundRequiredTextureRegion, RM.vbom) {
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					this.setScale(0.6f);
				} else if (pSceneTouchEvent.isActionUp()) {
					this.setScale(0.85f);

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

		hintSprite.setScale(0.80f);
		soundSprite.setScale(0.85f);
		attachChild(new Sprite(MainGameActivity.CAMERA_WIDTH/2, hintSprite.getY(), RM.blackPatchTextureRegion, vbom));
		attachChild(nextBoardSprite);
		registerTouchArea(nextBoardSprite);
		attachChild(hintSprite);
		registerTouchArea(hintSprite);
		//registerTouchArea(pauseSprite);

		attachChild(pauseSprite);
		registerTouchArea(soundSprite);
		attachChild(soundSprite);
		createResultNumber();
		if (!isGameStarted) {
			timerSprite.pause();
			resultNumberText.setVisible(false);
		}
		pauseGame();
		tapToStart();
		RM.activity.setAdMobVisibile();
	}

	private void incrementBoardClear() {
		boardsCleared++;
		
		if(!API_CLIENT.isConnected())
			return;
		
		Games.Achievements.unlock(API_CLIENT, RM.activity.getString(R.string.achievement_1_board_clear));
		Games.Achievements.unlock(API_CLIENT, RM.activity.getString(R.string.achievement_beginner));
		
		Games.Achievements.increment(API_CLIENT,RM.activity.getString(R.string.achievement_3_boards_cleared), 1);
		Games.Achievements.increment(API_CLIENT,RM.activity.getString(R.string.achievement_5_boards_cleared), 1);
		Games.Achievements.increment(API_CLIENT,RM.activity.getString(R.string.achievement_10_boards_cleared), 1);
		Games.Achievements.increment(API_CLIENT,RM.activity.getString(R.string.achievement_30_boards_cleared), 1);
		Games.Achievements.increment(API_CLIENT,RM.activity.getString(R.string.achievement_100_boards_cleared), 1);
		
		switch(boardsCleared) {
		case 3:
			Games.Achievements.unlock(API_CLIENT, RM.activity.getString(R.string.achievement_3_boards_in_single_game));
			break;
		case 5:
			Games.Achievements.unlock(API_CLIENT, RM.activity.getString(R.string.achievement_5_boards_in_single_game));
			Games.Achievements.unlock(API_CLIENT, RM.activity.getString(R.string.achievement_amateur));
			
			break;
		case 10:
			Games.Achievements.unlock(API_CLIENT, RM.activity.getString(R.string.achievement_10_boards_in_single_game));
			Games.Achievements.unlock(API_CLIENT, RM.activity.getString(R.string.achievement_novice));
			
			break;
		case 20:
			Games.Achievements.unlock(API_CLIENT, RM.activity.getString(R.string.achievement_mathematician));
			
			break;
		case 30:
			Games.Achievements.unlock(API_CLIENT, RM.activity.getString(R.string.achievement_grand_master));
			
			break;
		}
	}

	private void gameOver() {
		for (Iterator iterator = allCellSpritesInBoard.iterator(); iterator.hasNext();) {
			CellSprite sprite = (CellSprite) iterator.next();
			sprite.pause();
			unregisterTouchArea(sprite);
		}
		unregisterTouchArea(hintSprite);
		detachChild(hintSprite);
		unregisterTouchArea(pauseSprite);
		detachChild(pauseSprite);
		unregisterTouchArea(nextBoardSprite);
		detachChild(nextBoardSprite);
		unregisterTouchArea(coinSprite);
		timerSprite.pause();
		timerSprite.hide();

		mHUD.setVisible(false);

		final Rectangle rect = new Rectangle(MainGameActivity.CAMERA_WIDTH/2, MainGameActivity.CAMERA_HEIGHT/2,
				MainGameActivity.CAMERA_WIDTH, MainGameActivity.CAMERA_HEIGHT, RM.vbom);
		rect.setColor(Color.BLACK);
		rect.setAlpha(0.4f);
		attachChild(rect);

		Sprite timeoutSprite = new TimeoutSprite();
		attachChild(timeoutSprite);

		//TiledSprite restartSprite = new TiledSprite(timeoutSprite.getWidth()/2 + 30, timeoutSprite.getHeight()/2 + 30, 
		TiledSprite restartSprite = new TiledSprite(timeoutSprite.getWidth()/2 + 30, timeoutSprite.getHeight()/2 + 30, 
				RM.pauseTextureRegion, RM.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					this.setScale(0.5f);
				} else if (pSceneTouchEvent.isActionUp()) {
					this.setScale(0.75f);
					Utilities.playButtonSound();
					SceneManager.getInstance().createGameScene();
				}
				return true;
			}
		};

		restartSprite.setCurrentTileIndex(1);

		//Sprite leaderBoardSprite = new Sprite(timeoutSprite.getWidth()/2 - 40, timeoutSprite.getHeight()/2 - 80, RM.leaderBoardTextureRegion, RM.vbom)
		Sprite leaderBoardSprite = new Sprite(timeoutSprite.getWidth()/2 - 60, timeoutSprite.getHeight()/2 + 30, RM.leaderBoardTextureRegion, RM.vbom){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					this.setScale(0.5f);
				} else if (pSceneTouchEvent.isActionUp()) {
					this.setScale(0.75f);
					Utilities.playButtonSound();
					showLeaderBoard();
				}
				return true;
			}
		};

		Sprite mainMenuSprite = new Sprite(timeoutSprite.getWidth()/2 + 120, timeoutSprite.getHeight()/2 + 30, RM.menuTextureRegion, RM.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					this.setScale(0.5f);
				} else if (pSceneTouchEvent.isActionUp()) {
					this.setScale(0.75f);
					Utilities.playButtonSound();
					SceneManager.getInstance().createMenuScene();
				}
				return true;
			}
		};

		Sprite gPlusShareSprite = new Sprite(timeoutSprite.getWidth()/2 + 30, timeoutSprite.getHeight()/2 - 70, 
				RM.gPlusShareTextureRegion, RM.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					this.setScale(0.75f);
				} else if (pSceneTouchEvent.isActionUp()) {
					//this.setScale(0.75f);
					Utilities.playButtonSound();
					//SceneManager.getInstance().createMenuScene();
					
					
					Intent shareIntent = new PlusShare.Builder(RM.activity)
					.setType("text/plain")
					.setText("I just scored "+scoreSprite.getScore()+"\n\nLoved playing MathBingo. You will love it too." +
							"\n\nBring your dead minds back alive.\nPlay Math Bingo. Compete with your friends in Leaderboard")
							//.setContentUrl(Uri.parse("market://details?id=" + RM.activity.getApplicationContext().getPackageName()))
							.setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.mobeyond.mathbingo"))
							.getIntent();

					RM.activity.startActivityForResult(shareIntent, 0);
				}
				return true;
			}
		};

		Sprite fbShareSprite = new Sprite(timeoutSprite.getWidth()/2 + 40, timeoutSprite.getHeight()/2 - 70, 
				RM.fbShareTextureRegion, RM.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					this.setScale(0.65f);
				} else if (pSceneTouchEvent.isActionUp()) {
					this.setScale(0.85f);
					Utilities.playButtonSound();
					//FacebookManager.checkUserLoggedIn();
					FacebookManager.postScore(scoreSprite.getScore() +"", RM.gameDataManager.getmLevel().toString());
				}
				return true;
			}
		};

		Sprite shareButton = new Sprite(timeoutSprite.getWidth()/2 - 40, timeoutSprite.getHeight()/2 - 70,
				RM.shareTextureRegion, RM.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					this.setScale(0.5f);
				} else if (pSceneTouchEvent.isActionUp()) {
					this.setScale(0.75f);
					Utilities.playButtonSound();
					
					Intent sendIntent = new Intent();
					sendIntent.setAction(Intent.ACTION_SEND);
					sendIntent.putExtra(Intent.EXTRA_TEXT, "I just scored "+scoreSprite.getScore() + " in MathBingo" +
							"\nLoved Playing it. You too can try it at\nhttps://play.google.com/store/apps/details?id=com.mobeyond.mathbingo");
					sendIntent.setType("text/plain");
					RM.activity.startActivity(Intent.createChooser(sendIntent, "Share"));
				}
				return true;
			}
		};
		
		restartSprite.setScale(0.75f);
		mainMenuSprite.setScale(0.75f);
		leaderBoardSprite.setScale(0.75f);
		shareButton.setScale(0.75f);
		//gPlusShareSprite.setScale(0.75f);
		//fbShareSprite.setScale(0.85f);

		registerTouchArea(restartSprite);
		registerTouchArea(leaderBoardSprite);
		registerTouchArea(mainMenuSprite);
		registerTouchArea(gPlusShareSprite);
		//registerTouchArea(fbShareSprite);
		registerTouchArea(shareButton);
		
		attachChild(restartSprite);
		attachChild(leaderBoardSprite);
		attachChild(mainMenuSprite);
		attachChild(gPlusShareSprite);
		//attachChild(fbShareSprite);
		attachChild(shareButton);
		
		postScore();
		if(RM.gameDataManager.ismLeaderBoardRequired()) {
			showLeaderBoard();
		}
	}

	private void pauseGame() {
		isGamePaused = true;

		for (Iterator iterator = allCellSpritesInBoard.iterator(); iterator.hasNext();) {
			CellSprite sprite = (CellSprite) iterator.next();
			sprite.pause();
			unregisterTouchArea(sprite);
		}
		unregisterTouchArea(hintSprite);
		unregisterTouchArea(nextBoardSprite);
		unregisterTouchArea(coinSprite);
		unregisterTouchArea(soundSprite);
		timerSprite.pause();

		hintSprite.pause();
	}

	private void unpauseGame() {
		isGamePaused = false;
		for (Iterator iterator = allCellSpritesInBoard.iterator(); iterator.hasNext();) {
			CellSprite sprite = (CellSprite) iterator.next();
			sprite.unpause();
			registerTouchArea(sprite);
		}
		registerTouchArea(hintSprite);
		registerTouchArea(nextBoardSprite);
		registerTouchArea(coinSprite);
		registerTouchArea(soundSprite);

		timerSprite.unpause();
		hintSprite.unpause();
	}

	public void createHUD() {
		mHUD = new HUD();
		mHUD.attachChild(new Sprite(MainGameActivity.CAMERA_WIDTH/2, MainGameActivity.CAMERA_HEIGHT-70, RM.blackPatchTextureRegion, vbom));
		mHUD.attachChild(timerSprite = new TimerSprite(MainGameActivity.CAMERA_WIDTH/2, MainGameActivity.CAMERA_HEIGHT-70, GAME_OVER_TIME, true));
		mHUD.attachChild(scoreSprite = new ScoreSprite(MainGameActivity.CAMERA_WIDTH/4 - 25, MainGameActivity.CAMERA_HEIGHT-70, 0, true));
		mHUD.attachChild(coinSprite = new CoinSprite(3 * MainGameActivity.CAMERA_WIDTH/4 + 30, MainGameActivity.CAMERA_HEIGHT-70, true) {
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(timeExtensionCount >= TIME_EXTENSIONS_ALLOWED ||
						coinSprite.getCoins() < TIME_EXTENSION_REQUIRE_COINS)
					return true;

				if(pSceneTouchEvent.isActionDown()) {
					this.setScale(0.75f);
				} else if (pSceneTouchEvent.isActionUp()) {
					this.setScale(0.9f);
					Utilities.playButtonSound();
					Text timeIncrease = new Text(RM.scoreBackgroundTextureRegion.getWidth()/2, RM.scoreBackgroundTextureRegion.getHeight()/2, 
							RM.scoreFont, "+5s",
							"+5s".length(), RM.activity.getVertexBufferObjectManager());
					timeIncrease.setScale(0.6f);

					timeIncrease.registerEntityModifier(new ParallelEntityModifier(
							new AlphaModifier(1f, 1f, 0.2f),
							new MoveModifier(1f, 3 * MainGameActivity.CAMERA_WIDTH/4 + 30, MainGameActivity.CAMERA_HEIGHT-70, 
									timerSprite.getX(), timerSprite.getY()) {
								@Override
								public void onModifierFinished(final IEntity pItem) {
									super.onModifierFinished(pItem);
									timerSprite.incrementTime(5);
									coinSprite.setCoins(coinSprite.getCoins() - TIME_EXTENSION_REQUIRE_COINS);
									RM.gameDataManager.addGoldCoins(-TIME_EXTENSION_REQUIRE_COINS);
									timeExtensionCount++;
									RM.activity.runOnUpdateThread(new Runnable() {
										@Override
										public void run() {
											mHUD.detachChild(pItem);
										}
									});
								}
							}
							));
					mHUD.attachChild(timeIncrease);
				}
				return true;
			}
		});
		registerTouchArea(coinSprite);

		scoreSprite.setScale(0.9f);
		coinSprite.setScale(0.9f);
		RM.camera.setHUD(mHUD);
	}

	public void createResultNumber() {
		if(resultNumberText != null) {
			detachChild(resultNumberText);
			resultNumberText.detachSelf();
		}
		resultNumberText = new Text(MainGameActivity.CAMERA_WIDTH/2, 200, RM.resultNumberFont, String.valueOf(board.getResultNumber()), RM.vbom);
		attachChild(resultNumberText);
	}

	public void createNewBoard() {
		//board = new Board();

		clearBoard();
		createBoard();
		createResultNumber();
	}

	public boolean isResultNumberAlreadyPlayed(int number) {
		if(playedResultNumbers.size() >= rm.DEFAULT_MAXIMUM_RESULT_NUMBER/2)
			playedResultNumbers.clear();

		for (Iterator<Integer> iterator = playedResultNumbers.iterator(); iterator.hasNext();) {
			Integer playedResultNumber = (Integer) iterator.next();
			if(playedResultNumber.intValue() == number)
				return true;
		}
		return false;
	}
	public void createBoard() {

		// To make sure we are getting the same result number in the next board too.

		do {
			board = new Board();
		} while (isResultNumberAlreadyPlayed(board.getResultNumber()));

		playedResultNumbers.add(Integer.valueOf(board.getResultNumber()));

		/*Line line1 = new Line(0, startPositionY, MainGameActivity.CAMERA_WIDTH, startPositionY, RM.vbom);
		Line line2 = new Line(0, startPositionY + ((board.getSize()+1) * (marginBetweenCells)), 
				MainGameActivity.CAMERA_WIDTH, startPositionY + ((board.getSize()+1) * (marginBetweenCells)), RM.vbom);
		line1.setColor(Color.YELLOW);
		line2.setColor(Color.YELLOW);
		attachChild(line1);
		attachChild(line2);*/

		int noOfGreens = 0;
		for (int i = 0; i < board.getMatrix().length; i++) {
			for (int j = 0; j < board.getMatrix()[i].length; j++) {
				int pX = marginBetweenCells * (j+1) + startPositionX;
				int pY = marginBetweenCells * (i+1) + startPositionY;
				ITextureRegion textureRegion = RM.blueSphereTextureRegion;
				if(board.isSignAt(i, j)) {
					textureRegion = RM.maroonSphereTextureRegion;
				} else if(noOfGreens < RM.DEFAULT_MAXIMUM_GREENS_ALLOWED 
						&& board.getValueAtInt(i, j) > (RM.DEFAULT_MAXIMUM_GRID_NUMBER * 60 / 100)) {
					noOfGreens++;
					textureRegion = RM.greenSphereTextureRegion;
				}
				CellSprite sprite = new CellSprite(board.getValueAt(i, j), pX, pY, textureRegion, RM.vbom) {
					@Override
					public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {

						if(pSceneTouchEvent.isActionUp()) {
							System.out.println("Sprite Touched "+this.getValue());
							selectedCellSprite[currentCellTouches] = this;
							this.highlight();
							cellSpriteTouched(this);

							return true;
						}
						return false;
					}
				};
				if(textureRegion == RM.greenSphereTextureRegion)
					sprite.setGreenSprite(true);
				allCellSpritesInBoard.add(sprite);
				unmodifiedAllCellSpritesInBoard.add(sprite);
				unmodifiedAllCellSpritesIsOnBoard.add(Boolean.valueOf(true));
				attachChild(sprite);
				registerTouchArea(sprite);
			}
		}
	}

	public void clearBoard() {
		if(allCellSpritesInBoard.size() <= 0)
			return;
		for (Iterator iterator = allCellSpritesInBoard.iterator(); iterator.hasNext();) {
			CellSprite cellSprite = (CellSprite) iterator.next();
			unregisterTouchArea(cellSprite);
			detachChild(cellSprite);
			cellSprite.detachSelf();
			cellSprite = null;
		}
		allCellSpritesInBoard.clear();
		unmodifiedAllCellSpritesInBoard.clear();
		unmodifiedAllCellSpritesIsOnBoard.clear();
	}
	@Override
	public void onBackKeyPressed() {
		Utilities.playButtonSound();
		if(isGameOver) {
			SceneManager.getInstance().createMenuScene();
			return;
		}

		pauseGame();
		RM.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {

				AlertDialog.Builder alert = new AlertDialog.Builder(RM.activity);
				alert.setTitle("Stop Game");
				alert.setMessage("Do you really want to stop the game");
				alert.setPositiveButton("Stop", new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						isGameOver = true;
						SceneManager.getInstance().createMenuScene();
					}
				});

				alert.setNegativeButton("Keep Playing", new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						unpauseGame();
					}
				});
				alert.show();
			}
		});
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene() {
		rm.engine.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				clearBoard();				
			}
		});

		camera.setHUD(null);
	}

	private void cellSpriteTouched(CellSprite cellSprite) {
		String value = cellSprite.getValue();

		switch(currentCellTouches) {
		case 0:
			try {
				operand1 = Integer.parseInt(value);
				currentCellTouches++;
			} catch (NumberFormatException e) {
				currentCellTouches = 0;
				removeHighlightOfSprites();
				return;
			}
			break;
		case 1:
			switch(value.charAt(0)) {
			case '\u00F7':
				operator = Board.DIVIDED_BY_SIGN;
				break;
			case '\u00D7':
				operator = Board.MULTIPLY_SIGN;
				break;
			case '+':
				operator = Board.PLUS_SIGN;
				break;
			case '-':
				operator = Board.MINUS_SIGN;
				break;
			default:
				currentCellTouches = 0;
				removeHighlightOfSprites();
				return;
			}
			currentCellTouches++;
			break;
		case 2:
			try{
				/*if(cellSprite.isHighlighted()) {
					removeHighlightOfSprites();
					currentCellTouches = 0;
					return;
				}*/
				if(selectedCellSprite[0] == cellSprite) {
					removeHighlightOfSprites();
					currentCellTouches = 0;
					return;
				}

				operand2 = Integer.parseInt(value);
				currentCellTouches++;
			} catch(NumberFormatException e) {
				currentCellTouches = 0;
				removeHighlightOfSprites();
				if(RM.gameDataManager.ismSoundRequired()) {
					RM.mWrongOperationSound.play();
				}
				return;
			}

			Operation operation = new Operation(operand1, operand2, operator);
			System.out.println(operation);
			if(operation.result() == board.getResultNumber()) {
				System.out.println("Sucess: "+board.getResultNumber() + ", result: "+operation.result());

				boolean greenBubbleSelected = false;

				for (int i = 0; i < selectedCellSprite.length; i++) {
					CellSprite tempCellSprite = selectedCellSprite[i];
					if(tempCellSprite.isGreenSprite())
						greenBubbleSelected = true;
					unmodifiedAllCellSpritesIsOnBoard.set(unmodifiedAllCellSpritesInBoard.indexOf(tempCellSprite), Boolean.valueOf(false));
					allCellSpritesInBoard.remove(tempCellSprite);
					//unmodifiedAllCellSpritesInBoard.remove(tempCellSprite);
					unregisterTouchArea(tempCellSprite);
					detachChild(tempCellSprite);
					tempCellSprite.detachSelf();
					tempCellSprite = null;
				}
				increaseScore(greenBubbleSelected?GREEN_BUBBLE_SCORE_INCREASE:SCORE_INCREASE, operation, cellSprite);
				if(RM.gameDataManager.ismSoundRequired()) {
					RM.mBubbleBlastSound.play();
				}
			} else {
				removeHighlightOfSprites();
				if(RM.gameDataManager.ismSoundRequired()) {
					RM.mWrongOperationSound.play();
				}
				//decreaseScore(operation);
			}
			currentCellTouches = 0;
			break;
		default:
			break;
		}
	}

	private void postScore() {
		if(RM.activity.isSignIn()) {
			if(!Utilities.isNetworkAvailable()) {
				if(RM.gameDataManager.getmLevel() == Level.EASY) {
					Games.Leaderboards.submitScore(API_CLIENT, 
							RM.activity.getString(R.string.easy_leader_board), 
							scoreSprite.getScore());
				} else if(RM.gameDataManager.getmLevel() == Level.MEDIUM) {
					Games.Leaderboards.submitScore(API_CLIENT, 
							RM.activity.getString(R.string.medium_leader_board), 
							scoreSprite.getScore());
				} else if(RM.gameDataManager.getmLevel() == Level.HARD) {
					Games.Leaderboards.submitScore(API_CLIENT, 
							RM.activity.getString(R.string.hard_leader_board), 
							scoreSprite.getScore());
				}
			}
		}
	}

	private void showLeaderBoard() {
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
		} else {

			if(RM.activity.isSignIn()) {
				if(!Utilities.isNetworkAvailable()) {
					RM.activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {

							AlertDialog.Builder alert = new AlertDialog.Builder(RM.activity);
							alert.setTitle("No Internet");
							alert.setMessage("No Internet connection available\n" +
									"Your score may not be posted to leaderboard");
							alert.setPositiveButton("OK", new OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									if(RM.gameDataManager.getmLevel() == Level.EASY) {
										Games.Leaderboards.submitScore(API_CLIENT, 
												RM.activity.getString(R.string.easy_leader_board), 
												scoreSprite.getScore());
										RM.activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
												API_CLIENT, 
												RM.activity.getString(R.string.easy_leader_board)), 
												2);
									} else if(RM.gameDataManager.getmLevel() == Level.MEDIUM) {
										Games.Leaderboards.submitScore(API_CLIENT, 
												RM.activity.getString(R.string.medium_leader_board), 
												scoreSprite.getScore());
										RM.activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
												API_CLIENT, 
												RM.activity.getString(R.string.medium_leader_board)), 
												2);
									} else if(RM.gameDataManager.getmLevel() == Level.HARD) {
										Games.Leaderboards.submitScore(API_CLIENT, 
												RM.activity.getString(R.string.hard_leader_board), 
												scoreSprite.getScore());
										RM.activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
												API_CLIENT, 
												RM.activity.getString(R.string.hard_leader_board)), 
												2);
									}
								}
							});

							alert.show();
						}
					});
				} else {
					if(RM.gameDataManager.getmLevel() == Level.EASY) {
						Games.Leaderboards.submitScore(API_CLIENT, 
								RM.activity.getString(R.string.easy_leader_board), 
								scoreSprite.getScore());
						RM.activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
								API_CLIENT, 
								RM.activity.getString(R.string.easy_leader_board)), 
								2);
					} else if(RM.gameDataManager.getmLevel() == Level.MEDIUM) {
						Games.Leaderboards.submitScore(API_CLIENT, 
								RM.activity.getString(R.string.medium_leader_board), 
								scoreSprite.getScore());
						RM.activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
								API_CLIENT, 
								RM.activity.getString(R.string.medium_leader_board)), 
								2);
					} else if(RM.gameDataManager.getmLevel() == Level.HARD) {
						Games.Leaderboards.submitScore(API_CLIENT, 
								RM.activity.getString(R.string.hard_leader_board), 
								scoreSprite.getScore());
						RM.activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
								API_CLIENT, 
								RM.activity.getString(R.string.hard_leader_board)), 
								2);
					}
				}
			}
		}
	}

	private void removeHighlightOfSprites() {
		for (int i = 0; i < selectedCellSprite.length; i++) {
			CellSprite tempCellSprite = selectedCellSprite[i];
			if(tempCellSprite == null)
				continue;
			tempCellSprite.removeHighLight();
		}
	}

	private void increaseScore(int score, Operation oper, CellSprite cellSprite) {
		scoreSprite.addScore(score, cellSprite);
	}

	private void decreaseScore(Operation oper) {
		scoreSprite.addScore(-1000);
	}

	@Override
	public void loadScene() {
		if(RM.gameDataManager.ismSoundRequired())
			soundSprite.setCurrentTileIndex(1);
		else 
			soundSprite.setCurrentTileIndex(0);
	}
}
