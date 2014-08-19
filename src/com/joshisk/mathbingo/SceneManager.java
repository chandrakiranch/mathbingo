package com.joshisk.mathbingo;

import org.andengine.engine.Engine;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import com.joshisk.mathbingo.scene.GameScene;
import com.joshisk.mathbingo.scene.MainMenuScene;
import com.joshisk.mathbingo.scene.OptionsScene;
import com.joshisk.mathbingo.scene.ResultScene;
import com.joshisk.mathbingo.scene.SplashScreenScene;

public class SceneManager
{
    //---------------------------------------------
    // SCENES
    //---------------------------------------------
    
    private BaseScene splashScene;
    private BaseScene menuScene;
    private BaseScene gameScene;
    private BaseScene loadingScene;
    private BaseScene optionsScene;
    private BaseScene levelScene;
    private BaseScene resultScene;
    
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    private static final SceneManager INSTANCE = new SceneManager();
    
    private SceneType currentSceneType = SceneType.SCENE_SPLASH;
    
    private BaseScene currentScene;
    
    private Engine engine = ResourceManager.getInstance().engine;
    
    public enum SceneType
    {
        SCENE_SPLASH,
        SCENE_MENU,
        SCENE_GAME,
        SCENE_LOADING,
        SCENE_OPTIONS,
        SCENE_RESULT
    }
    
    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------
    
    public void setScene(BaseScene scene)
    {
        engine.setScene(scene);
        currentScene = scene;
        currentSceneType = scene.getSceneType();
    }
    
    public void setScene(SceneType sceneType)
    {
        switch (sceneType)
        {
            case SCENE_MENU:
                setScene(menuScene);
                break;
            case SCENE_GAME:
                setScene(gameScene);
                break;
            case SCENE_SPLASH:
                setScene(splashScene);
                break;
            case SCENE_LOADING:
                setScene(loadingScene);
                break;
            case SCENE_OPTIONS:
                setScene(optionsScene);
                break;
            case SCENE_RESULT:
                setScene(resultScene);
                break;
            default:
                break;
        }
    }
    
    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------
    
    public static SceneManager getInstance()
    {
        return INSTANCE;
    }
    
    public SceneType getCurrentSceneType()
    {
        return currentSceneType;
    }
    
    public BaseScene getCurrentScene()
    {
        return currentScene;
    }
    
    public void createSplashScene()
    {
    	if(splashScene == null) {
    		ResourceManager.getInstance().loadSplashScreen();
    		splashScene = new SplashScreenScene();
    	}
    	setScene(splashScene);
    }
    private void disposeSplashScene()
    {
    	if(splashScene != null) {
    		ResourceManager.getInstance().unloadSplashScreen();
    	}
    }
    
    public void createMenuScene()
    {
    	if(menuScene == null) {
    		ResourceManager.getInstance().loadMenuResources();
    		menuScene = new MainMenuScene();
    	} else {
    		menuScene.loadScene();
    	}
        
        setScene(menuScene);
        
        if(splashScene != null) {
        	splashScene.disposeScene();
        	ResourceManager.getInstance().unloadSplashScreen();
    		//ResourceManager.getInstance().unLoadGameResources();
    		//gameScene = null;
    	}
        
        if(gameScene != null) {
    		gameScene.disposeScene();
    		//ResourceManager.getInstance().unLoadGameResources();
    		//gameScene = null;
    	}
        if(resultScene != null) {
    		resultScene.disposeScene();
    		ResourceManager.getInstance().unloadResultSceneResources();
    		resultScene = null;
    	}
        if(optionsScene != null) {
        	optionsScene.disposeScene();
        	//optionsScene = null;
        }
    }
    
    public void createGameScene() {
    	
    	if(gameScene != null) {
    		gameScene.disposeScene();
    	} else {
//    		ResourceManager.getInstance().loadGameResources();
    	}
    	
    	gameScene = new GameScene();
    	setScene(gameScene);
    	if(menuScene != null) {
    		menuScene.disposeScene();
    		//ResourceManager.getInstance().unLoadMenuResources();
    	}
    }
    
    public void createOptionsScene() {
    	ResourceManager.getInstance().loadOptionsResources();
    	
    	if(optionsScene == null) {
    		optionsScene = new OptionsScene();
    		ResourceManager.getInstance().loadOptionsResources();
    	} else {
    		optionsScene.loadScene();
    	}
    	
    	setScene(optionsScene);
    	
    	if(menuScene != null) {
    		menuScene.disposeScene();
    		// Always menuScene we are not unloading the resources, because this is a small scene and we can keep the info in memory
  		  	//  to make it fast
    		//ResourceManager.getInstance().unLoadMenuResources();
    	} 
    }
    
    public void createResultScene()
    {
    	System.out.println("In Scenemanager ResultScene Creation");
    	ResourceManager.getInstance().loadResultSceneResources();
    	resultScene = new ResultScene();
    	setScene(resultScene);
    	if(gameScene != null) {
    		gameScene.disposeScene();
    		ResourceManager.getInstance().unLoadGameResources();
    	}
    }
}