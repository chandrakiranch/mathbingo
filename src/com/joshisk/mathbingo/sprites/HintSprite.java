package com.joshisk.mathbingo.sprites;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.util.adt.align.HorizontalAlign;

import com.joshisk.mathbingo.ResourceManager;

public class HintSprite extends Sprite {

	private static final ResourceManager RM = ResourceManager.getInstance();
	private static final String TEXT = "HINT";
	private boolean enabled = true;
	private int reEnableTimer = 30;
	private boolean isPause = false;
	private TimerHandler enableTimer = new TimerHandler(reEnableTimer, new ITimerCallback() {

		@Override
		public void onTimePassed(TimerHandler pTimerHandler) {
			enable();
		}
	});
	private IEntityModifier modifier = new LoopEntityModifier(
			new AlphaModifier(1f, 0.75f, 0.5f), reEnableTimer);

	public HintSprite(float pX, float pY, boolean isBackgroundNeeded) {
		super(pX, pY, RM.hintBackgroundTextureRegion, RM.activity.getVertexBufferObjectManager());

		
		/*Text hintText = new Text(RM.timerBackgroundTextureRegion.getWidth()/2, RM.timerBackgroundTextureRegion.getHeight()/2, 
				RM.scoreHeaderFont, TEXT,
				new TextOptions(HorizontalAlign.CENTER), RM.activity.getVertexBufferObjectManager());
		*/
		if(!isBackgroundNeeded) {
			this.setAlpha(0.0f);
		}
		
		//attachChild(hintText);
		/*Sprite hintIcon = new Sprite(this.getWidth()/2 + 3, this.getHeight()/2, RM.hintIconTextureRegion, RM.vbom);
		hintIcon.setScale(0.65f);
		attachChild(hintIcon);*/
		registerUpdateHandler(getEnableTimer());
		
		setScale(0.85f);
	}

	public void pause() {
		isPause = true;
		unregisterUpdateHandler(getEnableTimer());
	}
	
	public void unpause() {
		isPause = false;
		registerUpdateHandler(getEnableTimer());
	}
	
	public void disable() {
		if(!enabled)
			return;
		System.out.println("Disabled hint sprite");
		setAlpha(0.3f);
		enableTimer.reset();
		enabled = false;
		registerEntityModifier(modifier);
		modifier.reset();
	}
	
	public TimerHandler getEnableTimer() {
		return enableTimer;
	}

	public void setEnableTimer(TimerHandler enableTimer) {
		this.enableTimer = enableTimer;
	}

	public void enable() {
		System.out.println("Enabled hint sprite");
		setAlpha(1f);
		setScale(0.85f);
		enabled = true;
		unregisterEntityModifier(modifier);
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
}
