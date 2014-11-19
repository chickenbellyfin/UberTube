package com.chickenbellyfinn.ubertube;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector3;

public class Starfield {

	private static final String TAG = Starfield.class.getSimpleName();
	
	private static final float SIZE = 30f;
	private static final float DEPTH = 10f;
	public static float POINT_SIZE = 2f;

	private  FloatBuffer vertexBuffer;
	
	private int _count = 0;
	private float[] _stars;
	private float _angle;
	
	public Starfield(int count){
		_count = count;
		_stars = new float[_count*3];
		Vector3 tmp = new Vector3();
		for(int i = 0; i < _stars.length; i+=3){
			while(tmp.dst(0, 0, tmp.z) <= BlockRing.RADIUS){
				tmp.x = (float)Math.random()*SIZE - SIZE/2;
				tmp.y = (float)Math.random()*SIZE - SIZE/2;
				tmp.z = (float)Math.random()*DEPTH;				
			}

			_stars[i] = tmp.x;
			_stars[i+1] = tmp.y;
			_stars[i+2] = tmp.z;
			tmp.set(0,0,0);
		}
	
		ByteBuffer buf = ByteBuffer.allocateDirect(_stars.length*4);
		buf.order(ByteOrder.nativeOrder());
		vertexBuffer = buf.asFloatBuffer();
		vertexBuffer.put(_stars);
		vertexBuffer.position(0);
	}
	
	public void update(float delta, float dz, float angle){
		for(int i = 2; i < _stars.length; i += 3){
			_stars[i] -= dz;
			if(_stars[i] < 0){
				_stars[i] = DEPTH;
			}
		}
		_angle = angle;
		vertexBuffer.put(_stars);
		vertexBuffer.position(0);
	}
	
	public void render(Camera cam){
		GL10 gl = Gdx.gl10;
		gl.glPointSize(POINT_SIZE);
		cam.apply(gl);
		gl.glPushMatrix();
		gl.glRotatef(_angle, 0, 0, 1);
		gl.glColor4f(.7f, .7f, .7f, 1);
		gl.glEnable(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glDrawArrays(GL10.GL_POINTS, 0, _count);
		gl.glDisable(GL10.GL_VERTEX_ARRAY);
		gl.glPopMatrix();
	}

}
