package com.seasky.seaskywar.views;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.seasky.seaskywar.entities.SceneData;
import com.seasky.seaskywar.utils.StaticValue;

public class MenuScene extends BaseScene {
	
	private SpriteBatch batch;
	private Stage stage;	
	
	public MenuScene(SceneData sceneData){
		super(sceneData);
		batch = new SpriteBatch();
		stage = new Stage(StaticValue.SCREEN_WIDTH, StaticValue.SCREEN_HEIGHT, StaticValue.SCREEN_STRETCH_MODE, batch);
		
		
	}
	
	

}
