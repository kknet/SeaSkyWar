package com.seasky.seaskywar.views;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.seasky.seaskywar.entities.EnemyPlane;
import com.seasky.seaskywar.entities.Map;
import com.seasky.seaskywar.entities.Plane;
import com.seasky.seaskywar.entities.PlayerBullet;
import com.seasky.seaskywar.entities.SceneData;
import com.seasky.seaskywar.singletons.RM;
import com.seasky.seaskywar.utils.StaticValue;

public class GameScene extends BaseScene {
	private SpriteBatch batch;
	private Stage stage;
	
	private Image bg;
	private Map gameMap;
	private Image aircraftCarrier;
	private Group bulletGroup;
	private Plane plane;
	private EnemyPlane enemyPlane;
	
	private float time = 0;
	private float physicFrameTime = 0.02f;
	
	public GameScene(SceneData sceneData){
		super(sceneData);
		
		batch = new SpriteBatch();
		stage = new Stage(StaticValue.SCREEN_WIDTH, StaticValue.SCREEN_HEIGHT, StaticValue.SCREEN_STRETCH_MODE, batch);		
		
		
		TextureAtlas atlas = RM.instance().getTextureAltasByName("game");
		
		
//		bg = new Image(atlas.createSprite("game_bg"));
//		bg.setPosition(-bg.getWidth()/2+400, -bg.getHeight()/2+240);
//		stage.addActor(bg);
		
		gameMap = new Map((OrthographicCamera) stage.getCamera());
		stage.addActor(gameMap);
		
		aircraftCarrier = new Image(atlas.createSprite("aircraft_carrier"));
		stage.addActor(aircraftCarrier);
		aircraftCarrier.setScale(2);
		aircraftCarrier.setPosition(320, 240);
		aircraftCarrier.addAction(Actions.sequence(Actions.delay(0.1f), Actions.scaleTo(0.7f, 0.7f, 3f)));
		
		bulletGroup = new Group();
		bulletGroup.setVisible(true);
		stage.addActor(bulletGroup);
		
		PlayerBullet.setTextureAtlas(atlas);
		
		plane = new Plane(atlas.createSprite("plane"), stage.getCamera(), bulletGroup);
		stage.addActor(plane);
		plane.setPosition(400, 240);
		plane.setDegree(100);
		
		enemyPlane = new EnemyPlane(atlas.createSprite("plane"), plane, bulletGroup);
		enemyPlane.setPosition(0, 0);
		stage.addActor(enemyPlane);
		List<Vector2> path = enemyPlane.randomPatrolPath(5, 0, 0, 800, 480);
		enemyPlane.patrol(path);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		Gdx.input.setInputProcessor(stage);
	}
	
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		super.render(delta);
		
		if(Gdx.input.isKeyPressed(Keys.A)){
			plane.goLeft();
		}else{
			plane.stopLeft();
		}
		
		if(Gdx.input.isKeyPressed(Keys.D)){
			plane.goRight();
		}else{
			plane.stopRight();
		}
		
		if(Gdx.input.isKeyPressed(Keys.W)){
			plane.forward();
		}else{
			plane.stopForward();
		}
		
		if(Gdx.input.isKeyPressed(Keys.SPACE)){
			plane.fireForward();
		}else{
			plane.stopFire();
		}
		
		
		time += delta;
		if(time > physicFrameTime){
			stage.act(physicFrameTime);
			time -= physicFrameTime;
		}
		
		
		
		stage.draw();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		if(stage != null){
			stage.dispose();
		}
		
		if(batch != null){
			batch.dispose();
		}
	}


	
	
	
	
}
