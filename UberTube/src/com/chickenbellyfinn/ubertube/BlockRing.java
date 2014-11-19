package com.chickenbellyfinn.ubertube;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.lights.Lights;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.math.Vector3;

import static com.chickenbellyfinn.ubertube.Util.*;


public class BlockRing {
	
	private static final String TAG = BlockRing.class.getSimpleName();
	
	public static final float MIN_SPEED = 10;
	public static final float MAX_SPEED = 70;
	
	public static final float RADIUS = 2f;
	private static final float RADIUS_FACTOR_TIME = 1f;

	private ArrayList<Block> _blocks;
	

	private float dAngle;
	private float rot;
	public float Z;
	private int n;
	private float radiusFactor = 3f;
	private float time = 0f;
	public BlockRing(int size, float z, float holes){
		//long sTime = System.currentTimeMillis();
		_blocks = new ArrayList<Block>();
		Z = z;
		n = size;
		float side = 2*RADIUS*(float)Math.sin(Math.toRadians(180f/size))*0.9f;
		Model m = Block.getModel(size, side, Block.HEIGHT, Block.DEPTH);
		for(int i = 0; i < size; i++){
			if(Math.random() > holes){
				float angle = (float) (i*(2*Math.PI)/size);
				Block b = new Block(m, (float)Math.cos(angle)*RADIUS, (float)Math.sin(angle)*RADIUS, z, (float)Math.toDegrees(angle));
				_blocks.add(b);
			}
		}				

		dAngle = ((float)Math.random()*(MAX_SPEED-MIN_SPEED) + MIN_SPEED)* ((Math.random() < 0.5)?1f:-1f);

		//Gdx.app.log(TAG, "BlockRing "+(System.currentTimeMillis()-sTime));
	}
	
	
	public void update(float delta, float angle, float z){
		float dA = dAngle*delta;
		rot += dA;
		time = Math.min(time+delta, RADIUS_FACTOR_TIME);
		float alpha = time/RADIUS_FACTOR_TIME;
		radiusFactor = expEaseIn(alpha,3f, 1f);	
		float tAngle = expEaseIn(alpha, 180f, 0f);
		//Math.atan2(Block.DEPTH, x+xt)
		//float hyp = (float) ((dA/360f)*(2*Math.PI*RADIUS));
		for(Block b:_blocks){
						
			float xt = (b.z+z)/5;
			float a = b.startingAngle + rot + angle;
			float x = (float)Math.cos(Math.toRadians(a))*RADIUS*radiusFactor;
			float y = (float)Math.sin(Math.toRadians(a))*RADIUS*radiusFactor;
			b.transform.idt();	
		//	b.transform.rotate(0, 1, 0, (b.z+z)*5);
		//	b.transform.rotate(0, 1, 0, (b.z+z)*5);
			b.transform.translate(x, y, b.z + z);
			b.transform.rotate(0, 0, 1, a-90+tAngle);
			//b.transform.rotate(1, 0, 0, angle);
			//b.transform.rotate(new Vector3(0, 0, 0), new Vector3(0, 0, 0));

			
		}
	}
	
	public Block getBounce(float a){
		float arc = 360/n;
		Block best = null;
		float dist = 360;
		for(Block b:_blocks){
			float t = b.startingAngle + rot + a;
			while(t < 0) t += 360f;
			while(t > 360) t -= 360f;
			if(t >= 270-arc/2 && t <= 270+arc/2){
				b.materials.first().set(ColorAttribute.createDiffuse(Color.WHITE));
				if(Math.abs(t-270) < dist){
					best = b;
					dist = Math.abs(t-270);
				}
			}
		}		
		return best;
	}
	
	public void render(ModelBatch batch, Lights lights){
		for(Block b:_blocks){
			//Gdx.gl10.glColor4f(b.color.r, b.color.g, b.color.b, 1);
			batch.render(b, lights);
		}
		
	}

}
