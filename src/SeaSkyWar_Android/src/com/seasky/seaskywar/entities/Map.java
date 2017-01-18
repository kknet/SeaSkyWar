package com.seasky.seaskywar.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.AtlasTmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Map extends Actor{
	
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	public Map(OrthographicCamera camera){
		this.camera = camera;
		map = new AtlasTmxMapLoader().load("map01/map01.tmx");
		renderer = new OrthogonalTiledMapRenderer(map);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		super.draw(batch, parentAlpha);
		batch.end();
		renderer.setView(camera);
		renderer.render();	
		batch.begin();
	}
	
	
	
	
}
