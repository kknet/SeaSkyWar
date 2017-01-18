package com.seasky.seaskywar.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class PlayerBullet extends Bullet {
	
	public static List<PlayerBullet> bullets = new ArrayList<PlayerBullet>();
	public static List<PlayerBullet> invalidBullets = new ArrayList<PlayerBullet>();
	
	private static TextureAtlas atlas;
	
	public static void setTextureAtlas(TextureAtlas atlas){
		PlayerBullet.atlas = atlas;
	}
	
	public static PlayerBullet createPlayerBullet(float x, float y, float speed, float degree){
		PlayerBullet rs = null;
		if(!invalidBullets.isEmpty()){
			rs = invalidBullets.get(0);
			invalidBullets.remove(0);
			rs.reuse();
			
		}else{
			rs = new PlayerBullet(PlayerBullet.atlas);
			rs.init();
		}
		bullets.add(rs);
		rs.setPosition(x - rs.getWidth()/2, y - rs.getHeight()/2);
		rs.setRotation(degree-90);
		rs.setDy((float) (speed * Math.sin(degree / 180 * Math.PI)));
		rs.setDx((float) (speed * Math.cos(degree / 180 * Math.PI)));
		return rs;
	}
	
	
	public static void clearAllPlayerBullet(){
		bullets.clear();
		invalidBullets.clear();
	}
	
	
	private Sprite tex;
	private float dx = 0;
	private float dy = 0;
	
	private PlayerBullet(TextureAtlas atlas){
		super(atlas.createSprite("player_bullet"));
		this.setOrigin(this.getWidth()/2, this.getHeight()/2);
	}

	float time = 0;
	
	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
		this.translate(dx * delta, dy * delta);
		
		System.out.println(this.getX() + "   "+this.getY());
		time += delta;
		this.setColor(1, 1, 1, (1-time/0.3f));
		if(time > 0.3f){
			this.invalid();
		}
	}

	public float getDx() {
		return dx;
	}

	public void setDx(float dx) {

		this.dx = dx;
	}

	public float getDy() {
		
		return dy;
	}

	public void setDy(float dy) {
		this.dy = dy;
	}
	
	
	

}
