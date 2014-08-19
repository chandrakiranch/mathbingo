package com.joshisk.mathbingo.sprites;

import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import com.joshisk.mathbingo.ResourceManager;
import com.joshisk.mathbingo.core.Board;

public class CellSprite extends Sprite {

	private static final ResourceManager RM = ResourceManager.getInstance();
	private String value;
	private String pauseString = "?";
	private Text valueText;
	
	private boolean isGreenSprite = false;
	private IEntityModifier modifier;
	private boolean highlighted = false;
	
	public CellSprite(String value, float pX, float pY,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.value = value;
		
		valueText = new Text(this.getWidth()/2, this.getHeight()/2, RM.cellValueFont, this.value, 
				5, pVertexBufferObjectManager);
		valueText.setColor(Color.WHITE);
		attachChild(valueText);
		if(value.length() > 2) {
			valueText.setScale(0.75f);
		} else {
			valueText.setScale(1f);
		}
		this.setScale(0.75f);
		
		modifier = new LoopEntityModifier(
						new SequenceEntityModifier(
								new MoveModifier(0.05f, getX() - 3, getY() - 3, getX() + 3, getY() + 3), 
								new MoveModifier(0.05f, getX() + 3, getY() + 3, getX() - 3, getY() - 3)));
	}

	
	public boolean isGreenSprite() {
		return isGreenSprite;
	}


	public void setGreenSprite(boolean isGreenSprite) {
		this.isGreenSprite = isGreenSprite;
	}

	public String getValue() {
		return value;
	}
	
	public int getValueInt() {
		int result = 0 ;
		try {
		result = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			result = Board.convertOperatorToInt(value);
		}
		
		return result;
	}
	
	public boolean isHighlighted() {
		return highlighted;
	}
	public void highlight() {
		//this.setAlpha(0.5f);
		this.setScale(0.85f);
		registerEntityModifier(modifier);
		highlighted = true;
	}
	
	public void removeHighLight() {
		//this.setAlpha(1f);
		this.setScale(0.75f);
		unregisterEntityModifier(modifier);
		highlighted = false;
	}
	
	public void pause() {
		valueText.setText(pauseString);
	}
	
	public void unpause() {
		valueText.setText(value);
	}
}
