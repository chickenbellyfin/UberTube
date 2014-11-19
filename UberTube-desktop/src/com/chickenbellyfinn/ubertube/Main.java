package com.chickenbellyfinn.ubertube;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "UberTube";
		cfg.useGL20 = false;
		cfg.fullscreen = false;
		cfg.width = 1280;
		cfg.height = 720;
		cfg.foregroundFPS = 60;
		cfg.samples = 8;		
		new LwjglApplication(new UberTubeGame(new DefaultPlatformLayer()), cfg);

	}
}
