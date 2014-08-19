package com.joshisk.mathbingo.sprites;

import java.text.SimpleDateFormat;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import com.joshisk.mathbingo.ResourceManager;

public class TimerSprite extends Sprite {

	private static final ResourceManager RM = ResourceManager.getInstance();
	private static final SimpleDateFormat df = new SimpleDateFormat("mm:ss");
	private final Text timeText;

	private int timeTotalInSeconds;
	private int timeRemaining;
	private TimerHandler timerHandler;

	public TimerSprite(int setTime, boolean isBackgroundNeeded) {
		this(10,10, setTime, isBackgroundNeeded);
	}
	public TimerSprite(int setTime) {
		this(setTime, true);
	}
	public TimerSprite(float pX, float pY, int setTime, boolean isBackgroundNeeded) {
		super(pX, pY, RM.timerBackgroundTextureRegion, RM.activity.getVertexBufferObjectManager());
		this.timeTotalInSeconds = setTime;
		this.timeRemaining = setTime * 1000;
		df.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
		timeText = new Text(RM.timerBackgroundTextureRegion.getWidth()/2, RM.timerBackgroundTextureRegion.getHeight()/2, 
				RM.cellValueFont, df.format(this.timeRemaining),
				("00:00").length(), RM.activity.getVertexBufferObjectManager());
		timeText.setScale(0.5f);
		timeText.setColor(Color.BLACK);
		if(!isBackgroundNeeded) {
			this.setAlpha(0.0f);
		}
		attachChild(timeText);

		timerHandler = new TimerHandler(1f, false, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				TimerSprite.this.timeRemaining -= 1000;

				IEntityModifier modifier = new LoopEntityModifier(
						new ParallelEntityModifier(new ScaleModifier(0.5f,0.5f,0.4f), new AlphaModifier(0.5f, 1f, 0.6f)), 5);

				TimerSprite.this.timeText.setText(df.format(TimerSprite.this.timeRemaining));
				
				if(TimerSprite.this.timeRemaining <= 10000) {
					TimerSprite.this.timeText.registerEntityModifier(modifier);
				} else {
					TimerSprite.this.timeText.unregisterEntityModifier(modifier);
					TimerSprite.this.timeText.setScale(0.5f);
					TimerSprite.this.timeText.setAlpha(1f);
				}
			}
		});
	}

	public void start() {
		registerUpdateHandler(timerHandler);
	}
	
	public void hide() {
		timeText.setVisible(false);
	}
	public void pause() {
		timerHandler.setAutoReset(false);
	}
	public void unpause() {
		timerHandler.setAutoReset(true);
	}
	public int getTimeTotalInSeconds() {
		return timeTotalInSeconds;
	}
	private void setTimeTotalInSeconds(int timeTotalInSeconds) {
		this.timeTotalInSeconds = timeTotalInSeconds;
	}

	public synchronized int getTimeRemaining() {
		return timeRemaining;
	}

	public synchronized void setTimeRemaining(int timeRemaining) {
		this.timeRemaining = timeRemaining;
		timeText.setText(df.format(this.timeRemaining));
	}
	
	public synchronized void incrementTime(int sec) {
		this.timeRemaining += sec * 1000;
		timeText.setText(df.format(this.timeRemaining));
	}

	public boolean isTimerExpried() {
		if(timeRemaining <= 0) {
			return true;
		}
		return false;
	}
}
