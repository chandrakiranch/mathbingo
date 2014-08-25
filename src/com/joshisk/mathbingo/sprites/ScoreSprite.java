package com.joshisk.mathbingo.sprites;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import com.joshisk.mathbingo.ResourceManager;
import com.joshisk.mathbingo.SceneManager;
import com.joshisk.mathbingo.activity.MainGameActivity;

public class ScoreSprite extends Sprite {

	private static final ResourceManager RM = ResourceManager.getInstance();
	private static final String SCORE_HEADING = "Score:";
	private final Text scoreText;

	private int score = 0;
	private SequenceEntityModifier modifier;
	
	public ScoreSprite(float pX, float pY, int score, boolean isBackgroundNeeded) {
		super(pX, pY, RM.scoreBackgroundTextureRegion, RM.activity.getVertexBufferObjectManager());
		this.score = score;

		/*Text scoreHeader = new Text(RM.scoreBackgroundTextureRegion.getWidth()/2, 3 * RM.scoreBackgroundTextureRegion.getHeight()/4 - 15, 
				RM.scoreHeaderFont, SCORE_HEADING,
				new TextOptions(HorizontalAlign.CENTER), RM.activity.getVertexBufferObjectManager());*/

		scoreText = new Text(RM.scoreBackgroundTextureRegion.getWidth()/2, RM.scoreBackgroundTextureRegion.getHeight()/2, 
				RM.scoreFont, String.valueOf(score),
				"000000000000".length(), RM.activity.getVertexBufferObjectManager());
		scoreText.setScale(0.85f);
		if(!isBackgroundNeeded) {
			this.setAlpha(0.0f);
		}
		//attachChild(scoreHeader);
		attachChild(scoreText);
		
		modifier = new SequenceEntityModifier (
				new ScaleModifier(0.1f, 0.5f, 1f),
				new DelayModifier(0.2f),
				new ScaleModifier(0.1f, 1f, 0.5f) {
					@Override
					public void onModifierFinished(final IEntity pItem) {
						super.onModifierFinished(pItem);
						RM.activity.runOnUpdateThread(new Runnable() {
							@Override
							public void run() {
								pItem.unregisterEntityModifier(modifier);
							}
						});
					}
				}
				);
		
		scoreText.registerEntityModifier(modifier);
	}

	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
		if(this.score < 0) {
			this.score = 0;
			score = 0;
		}

		RM.gameDataManager.setmScore(this.score);
		
		scoreText.setText(String.valueOf(this.score));
		
		modifier.reset();
		scoreText.registerEntityModifier(modifier);
	}

	public void addScore(int value) {
		setScore((score + value));
	}

	public void addScore(final int value, CellSprite cellSpriteLastTouched) {
		Text scoreIncrease = new Text(RM.scoreBackgroundTextureRegion.getWidth()/2, RM.scoreBackgroundTextureRegion.getHeight()/2, 
				RM.scoreFont, "+"+String.valueOf(value),
				String.valueOf(value).length() + 1, RM.activity.getVertexBufferObjectManager());
		scoreIncrease.setScale(2f);
		if(scoreIncrease.getX() - scoreIncrease.getWidthScaled()/2 < 0) {
			scoreIncrease.setX(scoreIncrease.getX() + (scoreIncrease.getX() - scoreIncrease.getWidthScaled()/2));
		} else if(scoreIncrease.getX() + scoreIncrease.getWidthScaled()/2 > MainGameActivity.CAMERA_WIDTH) {
			scoreIncrease.setX(scoreIncrease.getX() - (scoreIncrease.getX() - scoreIncrease.getWidthScaled()/2));
		}
		scoreIncrease.registerEntityModifier(new ParallelEntityModifier(
				new AlphaModifier(1f, 1f, 0.2f),
				new MoveModifier(1f, cellSpriteLastTouched.getX(), cellSpriteLastTouched.getY(), getX(), getY()) {
					@Override
					public void onModifierFinished(final IEntity pItem) {
						super.onModifierFinished(pItem);
						setScore((score + value));
						RM.activity.runOnUpdateThread(new Runnable() {
							@Override
							public void run() {
								SceneManager.getInstance().getCurrentScene().detachChild(pItem);
							}
						});
					}
				}
				));
		SceneManager.getInstance().getCurrentScene().attachChild(scoreIncrease);
	}

}
