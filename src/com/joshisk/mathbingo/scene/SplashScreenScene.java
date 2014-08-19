package com.joshisk.mathbingo.scene;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;

import com.joshisk.mathbingo.BaseScene;
import com.joshisk.mathbingo.ResourceManager;
import com.joshisk.mathbingo.SceneManager;
import com.joshisk.mathbingo.SceneManager.SceneType;
import com.joshisk.mathbingo.activity.MainGameActivity;

public class SplashScreenScene extends BaseScene {

	private static final ResourceManager RM = ResourceManager.getInstance();

	private IEntityModifier modifier ;
	@Override
	public void createScene() {
		Sprite backgroundSprite = new Sprite(MainGameActivity.CAMERA_WIDTH/2, MainGameActivity.CAMERA_HEIGHT/2, 
				RM.splashScreenTextureRegion, vbom);
		attachChild(backgroundSprite);

		setBackgroundEnabled(true);
		
		modifier = new SequenceEntityModifier(
				new MoveModifier(0.1f, getX(), getY(), getX() + 3, getY() + 3),
				new MoveModifier(0.1f, getX(), getY(), getX() - 6, getY() - 6),
				new MoveModifier(0.1f, getX(), getY(), getX() + 3, getY() + 3)
				);

		registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				RM.activity.setAdMobVisibile();
				SceneManager.getInstance().createMenuScene();
			}
		}));

		registerUpdateHandler(new TimerHandler(0.5f, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				attachChild(new Bullet(MainGameActivity.CAMERA_WIDTH/2 - 150, MainGameActivity.CAMERA_HEIGHT/2 - 70));
			}
		}));

		registerUpdateHandler(new TimerHandler(0.9f, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				attachChild(new Bullet(MainGameActivity.CAMERA_WIDTH/2 + 150, MainGameActivity.CAMERA_HEIGHT/2 - 70));
			}
		}));

		registerUpdateHandler(new TimerHandler(1.3f, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				attachChild(new Bullet(MainGameActivity.CAMERA_WIDTH/2 - 150, MainGameActivity.CAMERA_HEIGHT/2 + 100));
			}
		}));

		registerUpdateHandler(new TimerHandler(1.7f, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				attachChild(new Bullet(MainGameActivity.CAMERA_WIDTH/2 + 150, MainGameActivity.CAMERA_HEIGHT/2 + 100));
			}
		}));
	}

	public void shake() {
		registerEntityModifier(modifier);
		modifier.reset();
	}
	
	@Override
	public void onBackKeyPressed() {
		System.exit(0);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_SPLASH;
	}

	@Override
	public void disposeScene() {

	}

	@Override
	public void loadScene() {
		// TODO Auto-generated method stub

	}

	class Bullet extends Sprite {

		IEntityModifier modifier = null;

		/* Final position in splash screen */
		public Bullet(int pX, int pY) {
			super(MainGameActivity.CAMERA_WIDTH/2, MainGameActivity.CAMERA_HEIGHT/2, 
					RM.splashBulletTextureRegion, vbom);

			modifier =  new ParallelEntityModifier(
					new ScaleModifier(0.2f, 1f, 0.5f),
					new MoveModifier(0.2f, getX(), getY(), pX, pY)) {
				@Override
				public void onModifierFinished(IEntity pItem) {
					//shake();
				}
			};

			registerEntityModifier(modifier);
		}

	}

}
