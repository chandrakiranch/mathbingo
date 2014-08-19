package com.joshisk.mathbingo.sprites;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.EntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import com.joshisk.mathbingo.ResourceManager;
import com.joshisk.mathbingo.SceneManager;

public class CoinSprite extends Sprite {

	private static final ResourceManager RM = ResourceManager.getInstance();
	private Sprite coin ;
	private final Text timeExtendText;
	private final Text coinCount;

	private long coins = 0;
	
	private SequenceEntityModifier modifier;
	public CoinSprite(float pX, float pY, boolean isBackgroundNeeded) {
		super(pX, pY, RM.scoreBackgroundTextureRegion, RM.activity.getVertexBufferObjectManager());
		coins = RM.gameDataManager.getGoldCoins();
		coin = new Sprite(RM.scoreBackgroundTextureRegion.getWidth()/4 - 15, 3 * RM.scoreBackgroundTextureRegion.getHeight()/4 - 15, RM.coinTextureRegion, RM.vbom);
		coin.setScale(0.5f);
		
		coinCount = new Text(RM.scoreBackgroundTextureRegion.getWidth()/4 + 25, 3 * RM.scoreBackgroundTextureRegion.getHeight()/4 - 15, 
				RM.scoreHeaderFont, String.valueOf(coins),
				"xxxxxx".length(), RM.activity.getVertexBufferObjectManager());
		
		timeExtendText = new Text(3 * RM.scoreBackgroundTextureRegion.getWidth()/4 , 3 * RM.scoreBackgroundTextureRegion.getHeight()/4 - 15, 
				RM.scoreFont, "+5s",
				"+5 sec".length(), RM.activity.getVertexBufferObjectManager());
		timeExtendText.setScale(0.85f);
		if(!isBackgroundNeeded) {
			this.setAlpha(0.0f);
		}
		attachChild(coin);
		attachChild(coinCount);
		coinCount.setScale(0.5f);
		timeExtendText.setScale(0.5f);
		attachChild(timeExtendText);
		
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
		coinCount.registerEntityModifier(modifier);
	}
	
	public long getCoins() {
		return coins;
	}
	
	public void setCoins(long coins) {
		if(coins < 0)
			coins = 0;
		this.coins = coins;
		coinCount.setText(String.valueOf(this.coins));
		
		modifier.reset();
		coinCount.registerEntityModifier(modifier);
	}
}
