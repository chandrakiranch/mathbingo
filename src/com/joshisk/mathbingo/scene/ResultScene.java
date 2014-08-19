package com.joshisk.mathbingo.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import com.joshisk.mathbingo.BaseScene;
import com.joshisk.mathbingo.ResourceManager;
import com.joshisk.mathbingo.SceneManager;
import com.joshisk.mathbingo.SceneManager.SceneType;
import com.joshisk.mathbingo.activity.MainGameActivity;

public class ResultScene extends BaseScene {

	private static final ResourceManager RM = ResourceManager.getInstance();

	long score = 5000;
	long highScore = 10000;
	Text scoreText;
	Text highScoreText;

	public ResultScene() {
		score = 5000;
		highScore = 10000;
		System.out.println("Result Scene Created");
	}

	@Override
	public void createScene() {
		Sprite backgroundSprite = new Sprite(MainGameActivity.CAMERA_WIDTH/2, MainGameActivity.CAMERA_HEIGHT/2, 
	    		RM.backgroundTextureRegion, vbom);
		//backgroundSprite.setAlpha(0.3f);
		attachChild(backgroundSprite);

		setBackgroundEnabled(true);

		score = RM.gameDataManager.getmScore();
		highScore = RM.gameDataManager.getmHighScore();;
		System.out.println("Result Scene Created");

		scoreText =  new Text(MainGameActivity.CAMERA_WIDTH/2, MainGameActivity.CAMERA_HEIGHT/2 + 50, 
				RM.cellValueFont, "Score: "+String.valueOf(this.score), 
				String.valueOf("Score: "+this.score).length(), RM.vbom);
		highScoreText =  new Text(MainGameActivity.CAMERA_WIDTH/2, MainGameActivity.CAMERA_HEIGHT/2 - 50, 
				RM.cellValueFont, "High Score: "+String.valueOf(this.highScore), 
				String.valueOf("High Score: "+this.highScore).length(), RM.vbom);

		attachChild(scoreText);
		attachChild(highScoreText);
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().createMenuScene();
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_RESULT;
	}

	@Override
	public void disposeScene() {

	}

	@Override
	public void loadScene() {
		// TODO Auto-generated method stub
		
	}

}
