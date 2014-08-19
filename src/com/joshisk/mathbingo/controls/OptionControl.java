package com.joshisk.mathbingo.controls;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;

import com.joshisk.mathbingo.ResourceManager;
import com.joshisk.mathbingo.utils.Utilities;

public class OptionControl extends Entity {
	
	private ResourceManager RM = ResourceManager.getInstance();

	private Scene mParentScene;
	private String[] mOptions = null;
	private int mCurrentSelectedOption = 0;
	private Sprite mLeftArrowSprite = null;
	private Sprite mRightArrowSprite = null;
	private Text mSelectedOptionText = null;
	
	private boolean mRestartAfterLast = true;
	
	public OptionControl(float pX, float pY,
			String[] options, Scene parentScene) {
		mOptions = options;
		mCurrentSelectedOption = 0;
		mSelectedOptionText = new Text(pX, pY, RM.resultNumberFont, "xxxxx", 50, RM.vbom);
		mSelectedOptionText.setScale(0.5f);
		
		mParentScene = parentScene;
		
		
		mLeftArrowSprite = new Sprite(pX - mSelectedOptionText.getWidthScaled()/2 - 30, pY, RM.optionsArrowTextRegion, RM.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					Utilities.playButtonSound();
					previousOption();
				}
				return true;
			}
		};
		mRightArrowSprite = new Sprite(pX + mSelectedOptionText.getWidthScaled()/2 + 30, pY, RM.optionsArrowTextRegion, RM.vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					Utilities.playButtonSound();
					nextOption();
				}
				return true;
			}
		};
		
		mRightArrowSprite.setRotation(180);
		
		attachChild(mSelectedOptionText);
		attachChild(mLeftArrowSprite);
		attachChild(mRightArrowSprite);
		
		mParentScene.registerTouchArea(mLeftArrowSprite);
		mParentScene.registerTouchArea(mRightArrowSprite);
		
		setOption(mCurrentSelectedOption);
		mParentScene.registerTouchArea(this);
	}
	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if(pSceneTouchEvent.isActionDown()) {
			nextOption();
		}
		return true;
	}
	
	public String getSelectedOption() {
		return mOptions[mCurrentSelectedOption];
	}
	
	public void setOption(int index) {
		
		if(index < 0) {
			mCurrentSelectedOption = 0;
		} else if(index > mOptions.length - 1) {
			mCurrentSelectedOption = mOptions.length - 1;
		}
		
		mCurrentSelectedOption = index;
		mSelectedOptionText.setText(mOptions[mCurrentSelectedOption]);
		
		if(mRestartAfterLast)
			return;
		
		if(mCurrentSelectedOption == 0) {
			mLeftArrowSprite.setAlpha(0.2f);
			mRightArrowSprite.setAlpha(1f);
		}
		else if (mCurrentSelectedOption == mOptions.length - 1) {
			mRightArrowSprite.setAlpha(0.2f);
			mLeftArrowSprite.setAlpha(1f);
		}
		else {
			mRightArrowSprite.setAlpha(1f);
			mLeftArrowSprite.setAlpha(1f);
		}
	}

	public void nextOption() {
		if(mCurrentSelectedOption == mOptions.length - 1) {
			if(mRestartAfterLast) {
				mCurrentSelectedOption = 0;
			} else {
				return;
			}
		} else {
			mCurrentSelectedOption++;
		}
		setOption(mCurrentSelectedOption);
	}
	
	public void previousOption() {
		if(mCurrentSelectedOption == 0) {
			if(mRestartAfterLast) {
				mCurrentSelectedOption = mOptions.length - 1;
			} else {
				return;
			}
		} else {
			mCurrentSelectedOption--;
		}
		setOption(mCurrentSelectedOption);
	}
	
	public void setTextColor(Color pColor) {
		mSelectedOptionText.setColor(pColor);
	}

	public boolean isRestartAfterLast() {
		return mRestartAfterLast;
	}

	public void setRestartAfterLast(boolean mRestartAfterLast) {
		this.mRestartAfterLast = mRestartAfterLast;
		if(mRestartAfterLast) {
			mRightArrowSprite.setAlpha(1f);
			mLeftArrowSprite.setAlpha(1f);
		}
	}
}
