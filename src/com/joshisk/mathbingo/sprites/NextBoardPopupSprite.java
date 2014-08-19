package com.joshisk.mathbingo.sprites;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import com.joshisk.mathbingo.ResourceManager;
import com.joshisk.mathbingo.activity.MainGameActivity;

public class NextBoardPopupSprite extends Sprite {

	private static final ResourceManager RM = ResourceManager.getInstance();

	public NextBoardPopupSprite() {
		super(MainGameActivity.CAMERA_WIDTH/2 - 350, MainGameActivity.CAMERA_HEIGHT/2 - 50, 
				RM.nextBoardPopupTextureRegion, RM.vbom);

		Text text = new Text(MainGameActivity.CAMERA_WIDTH/2 - 70, 3 * getHeight()/4 - 10, RM.cellValueFont, "Board Not cleared!!!", RM.vbom);
		text.setScale(0.75f);
		attachChild(text);
		
		text = new Text(MainGameActivity.CAMERA_WIDTH/2 - 70, getHeight()/2 + 10, RM.cellValueFont, "Wait for 10s", RM.vbom);
		text.setScale(0.65f);
		attachChild(text);
		
		text = new Text(MainGameActivity.CAMERA_WIDTH/2 - 70, getHeight()/4 + 20, RM.cellValueFont, "Skip with 10 coins", RM.vbom);
		text.setScale(0.65f);
		attachChild(text);
		
		System.out.println("NextBoardPopUP "+getX() +"  "+getY());
	}
}
