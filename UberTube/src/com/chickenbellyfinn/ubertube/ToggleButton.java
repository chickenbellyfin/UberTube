package com.chickenbellyfinn.ubertube;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static com.chickenbellyfinn.ubertube.Util.*;


public class ToggleButton {
	
	private static final String TAG = ToggleButton.class.getSimpleName();
	
	private static final String SPACE = "  ";
	
	public static float minWidth;
	public static float minHeight;
	
	public float width;
	public float height;
	
	private Sprite spriteA;
	private Sprite spriteB;
	private Sprite activeSprite;
	
	public float x;
	public float y;
	
	private float textX;
	private float textY;
	private float iconX;
	private float iconY;
	
	private Color defaultColor = Color.WHITE;
	private Color color = Color.WHITE;
	
	public boolean isTouched = false;
	
	public ToggleButton(TextureRegion a, TextureRegion b, float x, float y, float width, float height){		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		spriteA = new Sprite(a);
		spriteB = new Sprite(b);

		spriteA.setPosition(x, y);
		spriteA.setSize(width, height);
		spriteB.setPosition(x, y);
		spriteB.setSize(width, height);
		activeSprite = spriteA;
	}

	public void setDefaultColor(Color c){
		defaultColor = c;
		touch(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	
	public void setPosition(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void toggle(){
		activeSprite = (activeSprite == spriteA)?spriteB:spriteA;
	}
		
	public boolean touch(int screenX, int screenY) {
		boolean prev = isTouched;
		screenY = Gdx.graphics.getHeight()-screenY;
		isTouched = (screenX >= x && screenX <= x+width && screenY >= y && screenY <= y+height);
		if(isTouched && !prev){
			color = randColor();
		} else if(!isTouched){
			color = defaultColor;
		}
		return isTouched;
	}
	
	public void touchUp(){
		isTouched = false;
		color = defaultColor;
	}

	public void draw(SpriteBatch batch){
		activeSprite.setColor(color);
		activeSprite.setPosition(x, y);
		activeSprite.draw(batch);
	}
	
	
	
	

}
