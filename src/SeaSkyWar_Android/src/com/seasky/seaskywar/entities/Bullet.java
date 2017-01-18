package com.seasky.seaskywar.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Bullet extends Image {
	protected int hitMask = 0;
	protected float demage = 0;
	protected boolean isInvalid = false;
	
	
	public Bullet(Sprite tex) {
		// TODO Auto-generated constructor stub
		super(tex);
	}

	// 子弹初始化
	public void init(){
		hitMask = 0;
		demage = 0;
		isInvalid = false;
	}
	
	// 子弹重用
	public void reuse(){
		this.init();
		this.setPosition(0, 0);
		this.setRotation(0);
	}
	
	// 子弹失效
	public void invalid(){
		this.isInvalid = true;
		this.remove();
	}
	
	// 子弹是否失效
	public boolean isInvalid(){
		return this.isInvalid;
	}
	
	// 设置子弹伤害
	public void setDemage(float demage){
		this.demage = demage;
	}
	
	// 返回子弹伤害
	public float getDemage(){
		return this.demage;
	}
	
	// 返回子弹碰撞掩码
	public int getHitMask(){
		return this.hitMask;
	}
	
	// 设置子弹碰撞掩码
	public void setHitMask(int hitMask){
		this.hitMask = hitMask;
	}
	
}
