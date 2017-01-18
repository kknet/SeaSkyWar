package com.seasky.seaskywar;

import com.badlogic.gdx.Game;
import com.seasky.seaskywar.entities.SceneData;
import com.seasky.seaskywar.singletons.GM;
import com.seasky.seaskywar.utils.StaticValue;

public class WarGame extends Game {

	public WarGame(){
		GM.instance().registerGame(this);
	}
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		
		SceneData sceneData = new SceneData();
		//sceneData.sceneId = StaticValue.SCENE_LOADING;
		sceneData.sceneId = StaticValue.SCENE_GAME;
		GM.instance().changeScene(sceneData);
	}

}
