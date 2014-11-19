package com.chickenbellyfinn.ubertube;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.graphics.Color;

public class Util {
	
	private static final int SAMPLE_SIZE = 10;

	private static float[] gyroX = new float[SAMPLE_SIZE];
	private static int gyroXIdx = 0;
	
	private static float[] gyroY = new float[SAMPLE_SIZE];
	private static int gyroYIdx = 0;
	
	public static float expEaseIn(float x, float start, float end){
		return end + (float)Math.pow(2, -10*x)*(start-end);
	}
	
	public static float expEaseOut(float x, float start, float end){
		return start + (float)Math.pow(2, 10*(x-1))*(end-start);
	}
	
	public static Color randColor(){
		return Resources.COLORS[(int)(Math.random()*Resources.COLORS.length)];
	}
	
	public static float getGyroXSmooth(){
		if(Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)){
			gyroX[gyroXIdx++] = Gdx.input.getAccelerometerX()/SAMPLE_SIZE;
		} else {
			gyroX[gyroXIdx++] = -(((float)Gdx.input.getY()/Gdx.graphics.getHeight())*20f-10f)/SAMPLE_SIZE;
		}
		gyroXIdx %= SAMPLE_SIZE;
		float g = 0;
		for(float i:gyroX)g += i;
		return g;
	}	
	
	public static float getGyroYSmooth(){
		if(Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)){
			gyroY[gyroYIdx++] = Gdx.input.getAccelerometerY()/SAMPLE_SIZE;
		} else {
			gyroY[gyroYIdx++] = -(((float)Gdx.input.getX()/Gdx.graphics.getWidth())*20f-10f)/SAMPLE_SIZE;
		}
		gyroYIdx %= SAMPLE_SIZE;
		float g = 0;
		for(float i:gyroY)g += i;
		return g;
	}

}
