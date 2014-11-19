package com.chickenbellyfinn.ubertube;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static com.chickenbellyfinn.ubertube.Util.*;


public class Button {
	
	private static final String TAG = Button.class.getSimpleName();
	
	private static final String SPACE = "  ";
	
	public static float minWidth;
	public static float minHeight;
	
	public float width;
	public float height;
	
	private Sprite _top;
	private Sprite _bottom;
	private Sprite _icon;
	private float _bottomOffset;
	private String text;
	
	public float x;
	public float y;
	
	private float textX;
	private float textY;
	private float iconX;
	private float iconY;
	
	private Color defaultColor = Color.WHITE;
	private Color color = Color.WHITE;
	
	public boolean isTouched = false;
	
	public Button(String s, TextureRegion t, float x, float y){
		
		minWidth = Resources.buttonWidth;
		minHeight = Resources.buttonHeight;
		
		this.x = x;
		this.y = y;
		
		_top = new Sprite(Resources.buttonTexture);
		_top.setSize(minWidth, minHeight);
		_bottom = new Sprite(Resources.buttonTexture);
		_bottom.setSize(minWidth, minHeight);
		_bottom.setOrigin(minWidth/2, minHeight/2);
		_bottom.rotate(180);
		

		
		text = s;	
		TextBounds bounds = Resources.font.getBounds(text);
		float iconWidth = 0;	
	    iconX = 0;
		//vertical alignment
		height = minHeight;
		textY = (minHeight+bounds.height)/2;
		if(t != null){

			iconX = Resources.fontMargin;
			iconWidth = (float)(bounds.height/t.getRegionHeight())*t.getRegionWidth();
			_icon = new Sprite(t);
			_icon.setSize(iconWidth*1.5f, bounds.height*1.5f);
			iconY = (textY-bounds.height)-(_icon.getHeight()-bounds.height)/2;
		}

		float textWidth = bounds.width + 2*Resources.fontMargin;
		width =  Math.max(minWidth, iconX + iconWidth + textWidth);
		//Gdx.app.log(TAG, "width = "+width);
		_bottomOffset = Math.max(0,width-minWidth);
		//Gdx.app.log(TAG, "_botOff = "+_bottomOffset);
		if(t != null){
			textX = ((minWidth+_bottomOffset)-textWidth)/2 + iconX + iconWidth;
		} else {
			textX = (width-bounds.width)/2;
		}
		
	}

	public void setDefaultColor(Color c){
		defaultColor = c;
		touch(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	
	public void setPosition(float x, float y){
		this.x = x;
		this.y = y;
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
		_top.setColor(color);
		_top.setPosition(x, y);
		_bottom.setColor(color);
		_bottom.setPosition(x+_bottomOffset, y);
		if(_icon != null){
			_icon.setPosition(x+iconX, y+iconY);
			_icon.setColor(color);
			_icon.draw(batch);
		}
		
		_top.draw(batch);		
		_bottom.draw(batch);
		Resources.font.setColor(color);
		Resources.font.draw(batch, text, x+textX, y+textY);
	}
	
	
	
	

}
