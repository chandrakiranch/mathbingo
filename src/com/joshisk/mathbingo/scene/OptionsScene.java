package com.joshisk.mathbingo.scene;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import com.joshisk.mathbingo.BaseScene;
import com.joshisk.mathbingo.LevelManager.Level;
import com.joshisk.mathbingo.ResourceManager;
import com.joshisk.mathbingo.SceneManager;
import com.joshisk.mathbingo.SceneManager.SceneType;
import com.joshisk.mathbingo.activity.MainGameActivity;
import com.joshisk.mathbingo.controls.OptionControl;
import com.joshisk.mathbingo.utils.Utilities;

public class OptionsScene extends BaseScene {

	private static final ResourceManager RM = ResourceManager.getInstance();

	private String soundLabel = "Sound:";
	private OptionControl soundRequired;

	private String leaderBoardLabel = "LeaderBoard:";
	private OptionControl leaderBoardRequired;
	
	private String helpRequiredLabel = "Help:";
	private OptionControl helpOptionControl;

	//	private String musicLabel = "Music:";
	//	private OptionControl musicRequired;

	private String levelLabel = "Level:";
	private OptionControl levelSelected;

	public OptionsScene() {
		soundLabel = "Sound:";

		//		musicLabel = "Music:";
		leaderBoardLabel = "LeaderBoard:";
		levelLabel = "Level:";

		setBackground(new SpriteBackground(new Sprite(MainGameActivity.CAMERA_WIDTH/2, MainGameActivity.CAMERA_HEIGHT/2, 
				RM.menu_background_region, vbom)));

		Rectangle rect = new Rectangle(MainGameActivity.CAMERA_WIDTH/2, MainGameActivity.CAMERA_HEIGHT/2,
				MainGameActivity.CAMERA_WIDTH, MainGameActivity.CAMERA_HEIGHT, RM.vbom);
		rect.setColor(Color.BLACK);
		rect.setAlpha(0.4f);
		attachChild(rect);

		/*Text soundLabelText = new Text(MainGameActivity.CAMERA_WIDTH/2, 550, RM.resultNumberFont, soundLabel, RM.vbom);
		soundLabelText.setScale(0.5f);
		//attachChild(soundLabelText);
		soundRequired = new OptionControl(MainGameActivity.CAMERA_WIDTH/2, 500, new String[]{"On", "Off"}, this);
		if(RM.gameDataManager.ismSoundRequired()) {
			soundRequired.setOption(0);
		} else {
			soundRequired.setOption(1);
		}

		//attachChild(soundRequired);*/

		/*Text musicLabelText = new Text(130, 350, RM.resultNumberFont, musicLabel, RM.vbom);
		attachChild(musicLabelText);
		musicRequired = new OptionControl(330, 350, new String[]{"On", "Off"}, this);
		if(RM.gameDataManager.ismMusicRequired()) {
			musicRequired.setOption(0);
		} else {
			musicRequired.setOption(1);
		}
		attachChild(musicRequired);
		 */

		Text levelText = new Text(MainGameActivity.CAMERA_WIDTH/2, 550, RM.resultNumberFont, levelLabel, RM.vbom);
		levelText.setScale(0.5f);
		attachChild(levelText);
		levelSelected = new OptionControl(MainGameActivity.CAMERA_WIDTH/2, 500, new String[]{"Easy", "Medium", "Hard"}, this);

		/*if(RM.gameDataManager.getmLevel() == Level.KIDS)
			levelSelected.setOption(0);
		else*/ if(RM.gameDataManager.getmLevel() == Level.EASY)
			levelSelected.setOption(0);
		else if(RM.gameDataManager.getmLevel() == Level.MEDIUM)
			levelSelected.setOption(1);
		else if(RM.gameDataManager.getmLevel() == Level.HARD)
			levelSelected.setOption(2);

		attachChild(levelSelected);

		Text leaderBoardText = new Text(MainGameActivity.CAMERA_WIDTH/2, 400, RM.resultNumberFont, leaderBoardLabel, RM.vbom);
		leaderBoardText.setScale(0.5f);
		attachChild(leaderBoardText);
		leaderBoardRequired = new OptionControl(MainGameActivity.CAMERA_WIDTH/2, 350, new String[]{"On", "Off"}, this);
		if(RM.gameDataManager.ismLeaderBoardRequired()) {
			leaderBoardRequired.setOption(0);
		} else {
			leaderBoardRequired.setOption(1);
		}

		attachChild(leaderBoardRequired);
		
		Text helpRequiredText = new Text(MainGameActivity.CAMERA_WIDTH/2, 250, RM.resultNumberFont, helpRequiredLabel, RM.vbom);
		helpRequiredText.setScale(0.5f);
		attachChild(helpRequiredText);
		helpOptionControl = new OptionControl(MainGameActivity.CAMERA_WIDTH/2, 200, new String[]{"On", "Off"}, this);
		if(RM.gameDataManager.ismHelpRequired()) {
			helpOptionControl.setOption(0);
		} else {
			helpOptionControl.setOption(1);
		}

		attachChild(helpOptionControl);
	}

	@Override
	public void createScene() {

	}

	@Override
	public void onBackKeyPressed() {
		storeValues();
		Utilities.playButtonSound();
		SceneManager.getInstance().createMenuScene();
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_OPTIONS;
	}

	// Implement on game data manager
	private void storeValues() {
		//		if(musicRequired.getSelectedOption().equalsIgnoreCase("On")) {
		//			RM.gameDataManager.setmMusicRequired(true);
		//		} else {
		//			RM.gameDataManager.setmMusicRequired(false);
		//		}

		/*if(soundRequired.getSelectedOption().equalsIgnoreCase("On")) {
			RM.gameDataManager.setmSoundRequired(true);
		} else {
			RM.gameDataManager.setmSoundRequired(false);
		}*/

		/*if(levelSelected.getSelectedOption().equals("Kids")) {
			RM.gameDataManager.setmLevel(Level.KIDS);
		} else*/ if(levelSelected.getSelectedOption().equals("Easy")) {
			RM.gameDataManager.setmLevel(Level.EASY);
		}
		else if(levelSelected.getSelectedOption().equals("Medium")) {
			RM.gameDataManager.setmLevel(Level.MEDIUM);
		}
		else if(levelSelected.getSelectedOption().equals("Hard")) {
			RM.gameDataManager.setmLevel(Level.HARD);
		}
		
		if(leaderBoardRequired.getSelectedOption().equalsIgnoreCase("On")) {
			RM.gameDataManager.setmLeaderBoardRequired(true);
		} else {
			RM.gameDataManager.setmLeaderBoardRequired(false);
		}
		
		if(helpOptionControl.getSelectedOption().equalsIgnoreCase("On")) {
			RM.gameDataManager.setmHelpRequired(true);
		} else {
			RM.gameDataManager.setmHelpRequired(false);
		}
	}

	@Override
	public void disposeScene() {
		storeValues();
		/*rm.engine.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				detachChild(soundRequired);
				detachChild(musicRequired);
				soundRequired = null;
				musicRequired = null;			
			}
		});*/ // Not deleting resources, as we will use the same instance always for OptionsScene from scene manager
	}

	@Override
	public void loadScene() {
		/*if(RM.gameDataManager.ismSoundRequired()) {
			soundRequired.setOption(0);
		} else {
			soundRequired.setOption(1);
		}*/

		/*if(RM.gameDataManager.getmLevel() == Level.KIDS)
			levelSelected.setOption(0);
		else */if(RM.gameDataManager.getmLevel() == Level.EASY)
			levelSelected.setOption(0);
		else if(RM.gameDataManager.getmLevel() == Level.MEDIUM)
			levelSelected.setOption(1);
		else if(RM.gameDataManager.getmLevel() == Level.HARD)
			levelSelected.setOption(2);

		if(RM.gameDataManager.ismLeaderBoardRequired()) {
			leaderBoardRequired.setOption(0);
		} else {
			leaderBoardRequired.setOption(1);
		}
		
		if(RM.gameDataManager.ismHelpRequired()) {
			helpOptionControl.setOption(0);
		} else {
			helpOptionControl.setOption(1);
		}
	}

}
