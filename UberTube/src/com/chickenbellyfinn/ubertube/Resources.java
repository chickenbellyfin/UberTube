package com.chickenbellyfinn.ubertube;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;

public class Resources {
	private static final String TAG = Resources.class.getSimpleName();

	private static final float FONT_SIZE = 0.05f;
	private static final float BUTTON_SIZE = 0.25f;
	
	
	
	public static final Color[] COLORS = {
		Color.valueOf("33B5E5"),
		Color.valueOf("AA66CC"),
		Color.valueOf("99CC00"),
		Color.valueOf("FFBB33"),
		Color.valueOf("FF4444"),
	};
	
	public static final Color COLOR_GPLUS = Color.valueOf("DD4B39");
	
//	public static final Color[] COLORS = {
//		Color.RED,
//		Color.ORANGE,
//		Color.YELLOW,
//		Color.GREEN,
//		Color.BLUE,
//	};
	
	private static float W;
	private static float H;
	
	public static Model titleTextModel;
	public static Model gameoverTextModel;
	
	public static Texture buttonImage;
	public static TextureRegion buttonTexture;
	public static float buttonWidth;
	public static float buttonHeight;
	
	private static FreeTypeFontGenerator fontGen;
	public static BitmapFont font;
	public static float fontMargin;
	
	public static Texture playIconImage;
	public static TextureRegion controllerTexture;
	public static TextureRegion ribbonTexture;
	public static TextureRegion crownTexture;
	public static TextureRegion gplusTexture;
	public static TextureRegion speakerOff;
	public static TextureRegion speakerOn;
	//sounds
	public static Music titleMusic;
	public static Music gameMusic;
	
	
	
	public static void load(){
		W = Gdx.graphics.getWidth();
		H = Gdx.graphics.getHeight();
		
		long sTime = System.currentTimeMillis();
		//load font
		fontGen = new FreeTypeFontGenerator(Gdx.files.internal("data/nechao.ttf"));
		font = fontGen.generateFont((int) (FONT_SIZE*Gdx.graphics.getHeight()));
		fontMargin = font.getBounds("e").height;
		font.setUseIntegerPositions(true);
		
		//load models
		ModelLoader loader = new ObjLoader();
        titleTextModel = loader.loadModel(Gdx.files.internal("data/title.obj"));   
		gameoverTextModel = loader.loadModel(Gdx.files.internal("data/gameover.obj")); 
		
		//load image(s)
		buttonImage = new Texture(Gdx.files.internal("data/button.png"));
		buttonTexture = new TextureRegion(buttonImage, 0, 0, 1280, 256);
		buttonWidth = W*BUTTON_SIZE;
		buttonHeight = (buttonWidth/1280f)*256f;
		
		playIconImage =     new Texture(Gdx.files.internal("data/play_icons.png"));
		controllerTexture = new TextureRegion(playIconImage, 0, 0, 360, 256);
		ribbonTexture =     new TextureRegion(playIconImage, 361, 0, 173, 256);
		crownTexture =      new TextureRegion(playIconImage, 360+173, 0, 326, 256);
		gplusTexture =      new TextureRegion(playIconImage, 361+173+327, 0, 268, 256);
		speakerOff =        new TextureRegion(playIconImage, 361+173+327+269, 0, 256, 256);
		speakerOn =         new TextureRegion(playIconImage, 361+173+327+269+257, 0, 256, 256);
		

		titleMusic = Gdx.audio.newMusic(Gdx.files.internal("data/title.mp3"));
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("data/game.mp3"));
		
		long loadTime = System.currentTimeMillis()-sTime;
		Gdx.app.log(TAG, "Loaded resouces in "+loadTime+" ms");
		
	}

	
	public static void dispose(){
		font.dispose();
		titleTextModel.dispose();
		gameoverTextModel.dispose();
		buttonImage.dispose();
		playIconImage.dispose();
	}

}
