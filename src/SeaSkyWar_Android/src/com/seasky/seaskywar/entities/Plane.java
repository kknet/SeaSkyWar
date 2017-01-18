package com.seasky.seaskywar.entities;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Plane extends Group{
	private int hitMask = 0x00000000;
	
	private Image planeImg;//飞机贴图
	private float maxforwardSpeed = 400;//最大前进速度
	private float minForwardSpeed = 200;
	private float forwardSpeed = 0; //实时前进速度
	private float acceSpeed = 200; //前进加速度
	
	
	private float maxSteerSpeed = 90;
	private float steerSpeed = 0;//转向速度
	private int steerDir;//转向方向，-1表示左转，1表示右转
	
	private boolean isFiring = false;
	private float fireInterval = 0.05f;
	private float fireTime = 0;
	
	private boolean isForward = false;
	private Camera camera;
	private Group bulletGroup;
	
	public Plane(Sprite tex, Camera cam, Group bulletGroup){
		this.camera = cam;
		this.bulletGroup = bulletGroup;
		
		
		
		planeImg = new Image(tex);
		planeImg.setPosition(-planeImg.getWidth()/2, -planeImg.getHeight()/2);
		planeImg.setOrigin(planeImg.getWidth()/2, planeImg.getHeight()/2);
		this.addActor(planeImg);		
		this.forwardSpeed = this.minForwardSpeed;
	}
	
	
	public void setHitMask(int hitMask){
		this.hitMask = hitMask;
	}
	
	public int getHitMask(){
		return this.hitMask;
	}
	
	public void forward(){
		isForward = true;
	}
	
	public void stopForward(){
		isForward = false;
	}
	

	public void goLeft(){
		steerDir = -1;
		steerSpeed = maxSteerSpeed;
	}
	
	public void goRight(){
		steerDir = 1;
		steerSpeed = maxSteerSpeed;
	}
	
	public void stopLeft(){
		if(steerDir == -1){
			steerDir = 0;
		}
	}
	
	public void stopRight(){
		if(steerDir == 1){
			steerDir = 0;
		}
	}
	
	public float getDegree(){
		return planeImg.getRotation()+90;
	}
	
	public void setDegree(float degree){
		this.planeImg.setRotation(degree-90);
	}
	
	public float getForwardSpeed(){
		return forwardSpeed;
	}
	
	private int lastDir = 0;
	
	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
		if(isForward){
			this.forwardSpeed += this.acceSpeed * delta;
			this.forwardSpeed = Math.min(maxforwardSpeed, this.forwardSpeed);
		}else{
			if(this.forwardSpeed > 0.1f){
				this.forwardSpeed -= this.acceSpeed * delta;
				this.forwardSpeed = Math.max(minForwardSpeed, this.forwardSpeed);
			}
			
//			this.forwardSpeed -= this.acceSpeed * delta;
//			this.forwardSpeed = Math.max(0, this.forwardSpeed);
		}
		
		if(steerDir != 0){
			lastDir = -steerDir;
			planeImg.rotate(-steerDir * steerSpeed * delta);
		}else{
			steerSpeed *= 0.98f;
			steerSpeed = Math.max(steerSpeed, 0);
			planeImg.rotate(lastDir * steerSpeed * delta);
		}		
		
		float degree = planeImg.getRotation() + 90;
		double radian = degree / 180f * Math.PI;
//		System.out.println("Degree="+degree + "   ForwardSpeed="+this.forwardSpeed + "   Camera Pos:x="+camera.position.x+ " y="+camera.position.y);
		
		double distance = this.forwardSpeed * delta;
		if(distance > 0.01f){
			float dx = (float)(distance * Math.cos(radian));
			float dy = (float)(distance * Math.sin(radian));
			this.translate(dx, dy);
			float cameraX = (float) (this.getX() + (this.forwardSpeed - this.minForwardSpeed) * 0.3f* Math.cos(radian));
			float cameraY = (float) (this.getY() + (this.forwardSpeed - this.minForwardSpeed) * 0.3f* Math.sin(radian));
			camera.position.x = cameraX;
			camera.position.y = cameraY;
			camera.update();
		}
		
		if(this.isFiring){
			fireTime += delta;
			if(fireTime >= fireInterval){
				fireTime -= fireInterval;
				float tmpDegree = this.getDegree()-90;
				Vector2 vec2 = calcPos(7, tmpDegree);
				if(flag){
					flag = false;
					this.bulletGroup.addActor(PlayerBullet.createPlayerBullet(this.getX()+vec2.x, this.getY()+vec2.y, this.forwardSpeed + 1200, this.getDegree()));	
				}else{
					flag = true;
					this.bulletGroup.addActor(PlayerBullet.createPlayerBullet(this.getX()-vec2.x, this.getY()-vec2.y , this.forwardSpeed + 1200, this.getDegree()));	
				}
				
				
			}
		}
		
	}
	
	boolean flag = true;
	
	private Vector2 calcPos(float dis2center, float degree){
		Vector2 vec = new Vector2();
		double rd = degree / 180f * Math.PI;
		vec.y = (float) (dis2center * Math.sin(rd));
		vec.x = (float) (dis2center * Math.cos(rd));
		return vec;
	}
	
	public void fireForward(){
		if(this.isFiring == false){
			this.isFiring = true;
			this.fireTime = 0;
		}		
	}
	
	public void stopFire(){
		if(this.isFiring == true){
			this.isFiring = false;
		}
		
	}
	
}
