package com.seasky.seaskywar.singletons;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.seasky.seaskywar.entities.SceneData;
import com.seasky.seaskywar.utils.StaticValue;
import com.seasky.seaskywar.views.BaseScene;
import com.seasky.seaskywar.views.EvaluateScene;
import com.seasky.seaskywar.views.GameScene;
import com.seasky.seaskywar.views.LoadingScene;
import com.seasky.seaskywar.views.MenuScene;

public class GM {
	private static GM instance = null;
	
	private GM(){
		
	}
	
	
	public static GM instance(){
		if(instance == null){
			instance = new GM();
		}
		
		return instance;		
	}
	
	
	private Game m_Game = null;
	public void registerGame(Game game){
		if(game != null){
			m_Game = game;
		}	
	}
	
	public void changeScene(SceneData sceneData){
		BaseScene tmpScene = null;
		switch(sceneData.sceneId){
		case StaticValue.SCENE_LOADING:
			tmpScene = new LoadingScene(sceneData);
			break;
		case StaticValue.SCENE_MENU:
			tmpScene = new MenuScene(sceneData);
			break;
		case StaticValue.SCENE_GAME:
			tmpScene = new GameScene(sceneData);
			break;
		case StaticValue.SCENE_EVALUATE:
			tmpScene = new EvaluateScene(sceneData);
			break;
		default:
			break;
		}
		if(m_Game!= null && tmpScene != null){
			if(m_Game.getScreen() != null){
				m_Game.getScreen().dispose();
			}
			m_Game.setScreen(tmpScene);
		}
	}
	
	
	private String m_LogTag = "Default Tag";
	public void setLogTag(String tag){
		if( tag != null){
			m_LogTag = tag;
		}
	}
	
	public void logi(String msg){
		Gdx.app.log(m_LogTag, msg);
	}
	
	public void logd(String msg){
		Gdx.app.debug(m_LogTag, msg);
	}
	
	public void loge(String msg){
		Gdx.app.error(m_LogTag, msg);
	}
	
	

}
