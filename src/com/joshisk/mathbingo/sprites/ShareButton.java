package com.joshisk.mathbingo.sprites;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import com.joshisk.mathbingo.ResourceManager;
import com.joshisk.mathbingo.SceneManager;

public class ShareButton extends Entity {

	private static final ResourceManager RM = ResourceManager.getInstance();
	
	public ShareButton(float pX, float pY) {
		super(pX, pY);
		
		Rectangle rect = new Rectangle(pX, pY, 50, 20, RM.vbom);
		rect.setColor(Color.BLACK);
		
		Text text = new Text(pX, pY, RM.cellValueFont, "SHARE", RM.vbom);
		text.setColor(Color.WHITE);
		
		attachChild(rect);
		attachChild(text);
		
		SceneManager.getInstance().getCurrentScene().registerTouchArea(this);
	}
	
	public ShareButton(float pX, float pY, float pWidth, float pHeight) {
		super(pX, pY, pWidth, pHeight);
	}

}
