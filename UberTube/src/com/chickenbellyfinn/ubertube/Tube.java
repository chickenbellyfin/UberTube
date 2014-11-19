  package com.chickenbellyfinn.ubertube;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.lights.Lights;
 
public class Tube {
	
	private static final String TAG = Tube.class.getSimpleName();
	
	private final int MIN_BLOCKS = 8;
	private final int MAX_BLOCKS = 20;
	private final float HOLES = 0.45f;
	
	private final float RING_DISTANCE = 1.1f;
	
	private ArrayList<BlockRing> _rings;
	
	private float z = 0;
	public float length = 0;
	
	private int ringCount = 0;
	
	public Tube(int limit, boolean startFull){
		_rings = new ArrayList<BlockRing>();
		
		for(int i = 0; i < limit; i++){
			int blocks = (int)(Math.random()*(MAX_BLOCKS-MIN_BLOCKS))+MIN_BLOCKS;
			_rings.add(new BlockRing(blocks, ringCount++*(Block.DEPTH*RING_DISTANCE), startFull?0f:HOLES));
		}		
		
		length = limit * Block.DEPTH*RING_DISTANCE;
	}
	
	public void update(float delta, float position, float angle, boolean addRings){
		z = position;
		Iterator<BlockRing> i = _rings.iterator();
		int add = 0;
		while(i.hasNext()){
			BlockRing ring = i.next();
			ring.update(delta, angle, -z);
			if(ring.Z + Block.DEPTH < position){
				i.remove();
				add++;
			}
		}	
		
		while(addRings && add > 0){
			int blocks = (int)(Math.random()*(MAX_BLOCKS-MIN_BLOCKS))+MIN_BLOCKS;			
			BlockRing newRing = new BlockRing(blocks, ringCount++*(Block.DEPTH*RING_DISTANCE), HOLES);
			_rings.add(newRing);
			newRing.update(delta, angle, -z);
			
			add--;
		}
		
		
	}

	
	public Block bounce(float y, float z, float a){
		//z += Block.DEPTH;
		for(BlockRing r:_rings){
			if(r.Z - (Block.DEPTH*RING_DISTANCE)/2 <= z && r.Z + (Block.DEPTH*RING_DISTANCE)/2 >= z){
				return r.getBounce(a);
			}
		}
		return null;
	}
	
	public void render(ModelBatch batch, Lights lights){
		GL10 gl = Gdx.gl10;
		for(BlockRing br:_rings){
			br.render(batch, lights);
		}
	}

}
