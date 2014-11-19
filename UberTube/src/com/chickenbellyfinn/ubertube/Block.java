package com.chickenbellyfinn.ubertube;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import static com.chickenbellyfinn.ubertube.Util.*;
public class Block extends ModelInstance {
	
	
	
	public static final float WIDTH = 1f;
	public static final float HEIGHT = 0.125f;
	public static final float DEPTH = 1.3f;
	
	//"google colors"
//	private static final Color[] COLORS = {
//		Color.RED,
//		Color.ORANGE,
//		Color.YELLOW,
//		Color.GREEN,
//		Color.BLUE,
//	};
	
	
	public float startingAngle;
	public float x,y,z;
	public boolean blocking = false;
	
	//private static HashMap<Integer, Model> blockModels = new HashMap<Integer, Model>();
	
	static Model getModel(int n, float w, float h, float d){
		ModelBuilder builder = new ModelBuilder();
		//builder.begin();
		//return builder.crea
		return builder.createBox(w, h, d, new Material("null-material"),Usage.Position | Usage.Normal);
	}
	
//	static Model getBlockingModel(int n, float w, float h, float d){
//		ModelBuilder builder = new ModelBuilder();
//		builder.part(MeshPart, material);
//		// builder.createBox(w, h, d, new Material("null-material"),Usage.Position | Usage.Normal)
//	}
	
	public Color color;
	public Block(Model m, float x, float y, float z, float startingAngle/*, boolean isBlock*/) {
		super(m, x, y, z);
		this.startingAngle = startingAngle;
		this.x = x;
		this.y = y;
		this.z = z; 
		//this.blocking = isBlock;
		color = randColor();
		materials.first().set(ColorAttribute.createDiffuse(color));
	}
	
	public void setPosition(float x, float y, float z){
		transform.translate(x-this.x, y-this.y, z-this.z);
		this.x = x;
		this.y = y;
		this.z = z;
	}

}
