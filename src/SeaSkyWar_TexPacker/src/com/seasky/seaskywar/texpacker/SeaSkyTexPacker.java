package com.seasky.seaskywar.texpacker;

import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class SeaSkyTexPacker {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String [] inputs = {
			"game",
		};
		
		String input = "E:\\SeaSkyWar\\SeaSkyWar\\images\\";
		String output = "E:\\SeaSkyWar\\SeaSkyWar\\src\\SeaSkyWar_Android\\assets\\images\\";
		Settings settings = new Settings();
		settings.maxHeight = 4096;
		settings.maxWidth = 4096;
		settings.alias = true;
		settings.duplicatePadding = true;
		settings.jpegQuality = 1f;
		
		for(int i = 0; i < inputs.length; i++){
			String tmp = inputs[i];
			TexturePacker2.process(settings, input+tmp, output + tmp, tmp);
		}
	}

}
