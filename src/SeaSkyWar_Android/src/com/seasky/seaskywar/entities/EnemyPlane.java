package com.seasky.seaskywar.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class EnemyPlane extends Group {
	
	public static final int IDLE = 0;
	public static final int ATTACK = 1;
	public static final int PATROL = 2;
	public static final int GOAWAY = 3;
	
	private int hitMask = 0x00000000;
	
	private Image planeImg;//飞机贴图
	private float maxforwardSpeed = 250;//最大前进速度
	private float forwardSpeed = 0; //实时前进速度
	private float acceSpeed = 250; //前进加速度
	
	
	private float maxSteerSpeed = 90;
	private float steerSpeed = 0;//转向速度
	private int steerDir;//转向方向，-1表示左转，1表示右转
	
	private boolean isFiring = false;
	private float fireInterval = 0.2f;
	private float fireTime = 0;
	
	private boolean isForward = false;
	private Group bulletGroup;
	
	private int lastState = IDLE;
	private int state = IDLE;
	
	private Plane playerPlane;
	private float rangeOfVisibility = 200;
	
	private List<Vector2> patrolPath = new ArrayList<Vector2>();
	private int patrolPathNodeCount = 0;
	
	public EnemyPlane(Sprite tex, Plane playerPlane, Group bulletGroup){
		this.bulletGroup = bulletGroup;
		this.playerPlane = playerPlane;
		
		planeImg = new Image(tex);
		planeImg.setPosition(-planeImg.getWidth()/2, -planeImg.getHeight()/2);
		planeImg.setOrigin(planeImg.getWidth()/2, planeImg.getHeight()/2);
		this.addActor(planeImg);		
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
	
	public float getForwardSpeed(){
		return forwardSpeed;
	}
	
	private int lastDir = 0;
	
	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		super.act(delta);
		aiRun();		
	}
	
	private Vector2 calcPos(float dis2center, float degree){
		Vector2 vec = new Vector2();
		double rd = degree / 180f * Math.PI;
		vec.y = (float) (dis2center * Math.sin(rd));
		vec.x = (float) (dis2center * Math.cos(rd));
		return vec;
	}
	
	
	public void patrol(List<Vector2> path){
		this.patrolPath = path;
		this.lastState = this.state;
		this.state = PATROL;	
		patrolNext();
	}
	
	public void attack(){
	}
	
	public List<Vector2> randomPatrolPath(int num, float minx, float miny, float maxx, float maxy){
		List<Vector2> rs = new ArrayList<Vector2>();
		for(int i = 0; i < num; i++){
			Vector2 tmp = new Vector2();
			tmp.x = (float) (Math.random() * (maxx - minx) + minx);
			tmp.y = (float) (Math.random() * (maxy - miny) + miny);
			rs.add(tmp);
		}
		return rs;
	}
	
	

	public void aiRun(){
		switch(this.state){
		case IDLE:
			break;
		case ATTACK:
			if(calcDistance(this.getX(), this.getY(), playerPlane.getX(), playerPlane.getY()) > rangeOfVisibility + 100){
				this.state = this.lastState;
				if(this.state == PATROL){
					patrolPathNodeCount--;
					patrolNext();
				}
				System.out.println("attack");
			}else{
				fireAt(playerPlane.getX(), playerPlane.getY());
			}
			break;
		case PATROL:
			if(calcDistance(this.getX(), this.getY(), playerPlane.getX(), playerPlane.getY()) <= rangeOfVisibility){
				this.lastState = this.state;
				this.state = ATTACK;
				isFiring = false;
				this.clearActions();
				this.planeImg.clearActions();
				System.out.println("attack");
			}
			break;
		case GOAWAY:
			if(calcDistance(this.getX(), this.getY(), playerPlane.getX(), playerPlane.getY()) > rangeOfVisibility){
				this.lastState = this.state;
				this.state = IDLE;
			}
			break;
		default:
			break;		
		}
	}
	
	public void fireForward(){
		planeImg.addAction(new Action() {
			float time = 0;
			@Override
			public boolean act(float delta) {
				// TODO Auto-generated method stub
				time += delta;
				if(time > 0.2f){
					isFiring = false;
					planeImg.removeAction(this);
				}
				return false;
			}
		});
		float tmpDegree = this.getDegree()-90;
		bulletGroup.addActor(PlayerBullet.createPlayerBullet(this.getX(), this.getY(), this.forwardSpeed + 900, this.getDegree()));
	}
	
	public void fireAt(float x, float y){
		if(isFiring == false){	
			isFiring = true;
			float degree = (float) ((float) Math.atan((y- this.getY())/(x - this.getX())) / Math.PI * 180);
			if(x < this.getX()){
				degree = -(180 - degree);
			}
			float rdg = degree -  this.getDegree();
			RotateByAction rba = new RotateByAction(){

				@Override
				protected void end() {
					// TODO Auto-generated method stub
					super.end();
					fireForward();
				}			
			};
			rba.setAmount(rdg);
			System.out.println(rdg);
			rba.setDuration(rdg / 180);
			planeImg.addAction(rba);
		}
	}
	
	
	public void patrolNext(){
		if(this.state == PATROL && this.patrolPath.size() > 0){
			Vector2 targetPos = this.patrolPath.get(this.patrolPathNodeCount%this.patrolPath.size());
			this.patrolPathNodeCount++;
			rotateTo(targetPos.x, targetPos.y);
		}
		
	}
	
	public void moveTo(float x, float y){
		float dis = calcDistance(this.getX(), this.getY(), x, y);
		MoveByAction mba = new MoveByAction(){

			@Override
			protected void end() {
				// TODO Auto-generated method stub
				super.end();
				patrolNext();
			}			
		};
		mba.setAmount(x - this.getX(), y - this.getY());
		mba.setDuration(dis/300);
		this.addAction(mba);
	}
	
	public void rotateTo(final float x, final float y){
		
		float degree = (float) ((float) Math.atan((y- this.getY())/(x - this.getX())) / Math.PI * 180);
		if(x < this.getX()){
			degree = -(180 - degree);
		}
		float rdg = degree -  this.getDegree();
		RotateByAction rba = new RotateByAction(){

			@Override
			protected void end() {
				// TODO Auto-generated method stub
				super.end();
				moveTo(x, y);
			}			
		};
		rba.setAmount(rdg);
		System.out.println(rdg);
		rba.setDuration(rdg / 180);
		planeImg.addAction(rba);
	}
	
	
	public float calcDistance(float x1, float y1, float x2, float y2){
		float dx = x2 - x1;
		float dy = y2 - y1;
		
		return (float) Math.sqrt(dx * dx + dy * dy);
	}
	
	
	public void goAway(){
		rotateTo(this.getX() + (this.getX() - playerPlane.getX()), this.getY() + (this.getY() - playerPlane.getY()));
	}
	
}
