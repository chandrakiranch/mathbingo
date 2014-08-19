package com.joshisk.mathbingo;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;

import com.joshisk.mathbingo.SceneManager.SceneType;

public abstract class BaseScene extends Scene
{
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    protected Engine engine;
    protected BaseGameActivity activity;
    protected ResourceManager rm;
    protected VertexBufferObjectManager vbom;
    protected Camera camera;
    protected boolean isPause = false;
    //---------------------------------------------
    // CONSTRUCTOR
    //---------------------------------------------
    
    public boolean isPause() {
		return isPause;
	}

	public void setPause(boolean isPause) {
		this.isPause = isPause;
	}

	public BaseScene()
    {
        this.rm = ResourceManager.getInstance();
        this.engine = rm.engine;
        this.activity = rm.activity;
        this.vbom = rm.vbom;
        this.camera = rm.camera;
        createScene();
    }
    
    //---------------------------------------------
    // ABSTRACTION
    //---------------------------------------------
    
    public abstract void createScene();
    
    public abstract void onBackKeyPressed();
    
    public abstract SceneType getSceneType();
    
    public abstract void disposeScene();
    
    public abstract void loadScene();
    
    @Override
    public void onManagedUpdate(float pSecondsElapsed) {
    	if(isPause) {
    		super.onManagedUpdate(0);
    	} else {
    		super.onManagedUpdate(pSecondsElapsed);
    	}
    }
}