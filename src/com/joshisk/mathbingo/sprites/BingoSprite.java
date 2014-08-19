package com.joshisk.mathbingo.sprites;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;

import com.joshisk.mathbingo.ResourceManager;
import com.joshisk.mathbingo.SceneManager;
import com.joshisk.mathbingo.activity.MainGameActivity;
import com.joshisk.mathbingo.utils.Utilities;

public class BingoSprite extends Sprite {

	private static final ResourceManager RM = ResourceManager.getInstance();

	private IEntityModifier modifier ;
	public BingoSprite() {
		super(MainGameActivity.CAMERA_WIDTH/2, 200, RM.bingoTextureRegion, RM.activity.getVertexBufferObjectManager());

		modifier = new SequenceEntityModifier(
				new ParallelEntityModifier(
						new ScaleModifier(0.3f, 0, 1),
						new MoveModifier(0.3f, getX(), getY(), MainGameActivity.CAMERA_WIDTH/2, MainGameActivity.CAMERA_HEIGHT/2)
						),
				new DelayModifier(1f),
				new AlphaModifier(0.5f, 1, 0){
					public void onModifierFinished(final IEntity pItem) {
						super.onModifierFinished(pItem);
						RM.engine.runOnUpdateThread(new Runnable() {
							@Override
							public void run() {
								SceneManager.getInstance().getCurrentScene().detachChild(pItem);								
							}
						});

					}
				});

		registerEntityModifier(modifier);
		setZIndex(5);
		Utilities.playBingoSound();
	}

}
