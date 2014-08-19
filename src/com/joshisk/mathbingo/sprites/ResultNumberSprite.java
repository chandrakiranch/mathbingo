package com.joshisk.mathbingo.sprites;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import com.joshisk.mathbingo.ResourceManager;

public class ResultNumberSprite extends Sprite {

	private static final ResourceManager RM = ResourceManager.getInstance();
	String value;
	public ResultNumberSprite(String value, float pX, float pY,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.value = value;
		
		Text valueText = new Text(this.getWidth()/2, this.getHeight()/2, RM.resultNumberFont, this.value, 
				this.value.length(), pVertexBufferObjectManager);
		attachChild(valueText);
	}
	
	public ResultNumberSprite(int value, float pX, float pY,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		this(String.valueOf(value), pX, pY, pTextureRegion, pVertexBufferObjectManager);
		
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {

		return true;
	}
}
