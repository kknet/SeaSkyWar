package com.seasky.seaskywar.singletons;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class RM extends AssetManager{
	private static RM instance = null;
	
	private RM(){
		
	}
	
	
	public static RM instance(){
		if(instance == null){
			instance = new RM();
		}
		
		return instance;		
	}
	
	private String dir = "images/";
	private String suffix = ".atlas";
	public TextureAtlas getTextureAltasByName(String name){
		if(!this.isLoaded(dir + name + "/"+ name+ suffix)){
			this.load(dir + name + "/"+ name+ suffix, TextureAtlas.class);
			this.finishLoading();
		}
		return this.get(dir + name + "/"+ name+ suffix, TextureAtlas.class);
	}
}
