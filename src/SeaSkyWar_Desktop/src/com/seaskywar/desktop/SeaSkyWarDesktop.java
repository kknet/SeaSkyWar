package com.seaskywar.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.seasky.seaskywar.WarGame;
import com.seasky.seaskywar.utils.StaticValue;

public class SeaSkyWarDesktop {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) StaticValue.SCREEN_WIDTH;
		config.height = (int) StaticValue.SCREEN_HEIGHT;
		config.useGL20 = true;
		new LwjglApplication(new WarGame(), config);
	}

}
