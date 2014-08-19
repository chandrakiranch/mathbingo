package com.joshisk.mathbingo.sprites;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.joshisk.mathbingo.ResourceManager;
import com.joshisk.mathbingo.SceneManager;
import com.joshisk.mathbingo.activity.MainGameActivity;

public class TimeoutSprite extends Sprite {

	private static final ResourceManager RM = ResourceManager.getInstance();

	public TimeoutSprite() {
		super(MainGameActivity.CAMERA_WIDTH/2, MainGameActivity.CAMERA_HEIGHT/2,
				RM.timeoutPopupTextureRegion, RM.vbom);
		//setZIndex(5);
		long score = RM.gameDataManager.getmScore();
		long highScore = RM.gameDataManager.getmHighScore();
		
		Text scoreText = new Text(getWidth()/2 + 60, 3 * getHeight()/4 + 45, RM.cellValueFont, String.valueOf(score), RM.vbom);
		Text highScoreText = new Text(getWidth()/2 + 60, 3 * getHeight()/4 + 5, RM.cellValueFont, String.valueOf(highScore), RM.vbom);
		
		scoreText.setScale(0.8f);
		highScoreText.setScale(0.8f);
		
		attachChild(scoreText);
		attachChild(highScoreText);
	}

}
