package com.joshisk.mathbingo.sprites;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.util.adt.align.HorizontalAlign;

import com.joshisk.mathbingo.ResourceManager;

public class NextBoardSprite extends Sprite {

	private static final ResourceManager RM = ResourceManager.getInstance();
	//private static final String NEXT_BOARD = "Next->";

	public NextBoardSprite(float pX, float pY, boolean isBackgroundNeeded) {
		super(pX, pY, RM.nextBoardTextureRegion, RM.activity.getVertexBufferObjectManager());

		
		/*Text scoreHeader = new Text(RM.timerBackgroundTextureRegion.getWidth()/2, RM.timerBackgroundTextureRegion.getHeight()/2, 
				RM.scoreHeaderFont, NEXT_BOARD,
				new TextOptions(HorizontalAlign.CENTER), RM.activity.getVertexBufferObjectManager());*/
		
		setScale(0.8f);
		if(!isBackgroundNeeded) {
			this.setAlpha(0.0f);
		}
		//attachChild(scoreHeader);
	}
}
