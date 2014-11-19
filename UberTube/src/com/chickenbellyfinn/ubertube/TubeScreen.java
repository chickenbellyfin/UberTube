package com.chickenbellyfinn.ubertube;

import static com.chickenbellyfinn.ubertube.Util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.lights.Lights;
import com.badlogic.gdx.graphics.g3d.lights.PointLight;
import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

public class TubeScreen implements Screen{
	
	private static final String TAG = MainMenuScreen.class.getSimpleName();
	
	private final float FOV = 67f;

	private final float STARTING_DISTANCE = -20;
	private final float DISTANCE = 3f;
	private final float MENU_Z = 3.5f;
	private final float PLAY_Y = 4.0f;
	private final float SCORE_Y = 2.7f;
	private final float SPEED = 0.65f;
	private final float ACCEL = 1.01f;
	
	private UberTubeGame _game;
	
	private int W;
	private int H;
	
	private PerspectiveCamera _camera3;

	
	private Lights _lights;
	private DirectionalLight _dLight;
	private PointLight _pLight;
	

	
	
	private Starfield _stars;
	
	private ModelBatch _modelBatch;
	
	private Tube _tube;
	private float z = 0;
	
	private float elapsed = 0;
	
	private int rings = 10;

	
	
	public TubeScreen(UberTubeGame game){
		_game = game;
		
		W = Gdx.graphics.getWidth();
		H = Gdx.graphics.getHeight();
		
		_modelBatch = new ModelBatch();	

        
		_tube = new Tube(rings, true);
		
		_camera3 = new PerspectiveCamera(FOV, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		_camera3.position.set(0f , 7f, 4f);
		_camera3.lookAt(0, 0, 4.1f);
		//_pCamera.direction.scl(0, 0, -1);
		_camera3.near = 0.1f;
		_camera3.far = 300f;
		_camera3.update();

		

		_stars = new Starfield(0);

        
//        _play.transform.setToTranslation(MENU_X, PLAY_Y, MENU_Z);
//        _play.calculateBoundingBox(_playBox = new BoundingBox());
//        
//        _scores.transform.setToTranslation(MENU_X, SCORE_Y, MENU_Z);
//        _scores.calculateBoundingBox(_scoreBox = new BoundingBox());

        _dLight = new DirectionalLight().set(0.4f, 0.4f, 0.4f, 0f, 0f, 1f);
		_lights = new Lights();
       // _lights.ambientLight.set(0.0f, 0.0f, 0.0f, 1f);
        _lights.ambientLight.set(0.7f, 0.7f, 0.7f, 1f);
        _pLight = new PointLight().set(1, 1, 3,DISTANCE, DISTANCE, 0.5f, 20);
        _lights.add(_dLight);
        //_lights.add(_pLight);
  
       
	}

	private void update(float delta){
		elapsed += delta;
		float dz= SPEED*delta;
		z += dz;
		
//		if(_camera3.position.z < 0){
//			_camera3.position.x = expEaseIn(elapsed, -DISTANCE, DISTANCE);
//			_camera3.position.y = expEaseIn(elapsed, -DISTANCE, DISTANCE);
//			_camera3.position.z = expEaseIn(elapsed, STARTING_DISTANCE, 0);
//			if(Math.abs(_camera3.position.z) < 0.01){
//				_camera3.position.x = DISTANCE;
//				_camera3.position.y = DISTANCE;
//				_camera3.position.z = 0;
//			}
//		}
		if(Gdx.input.justTouched()){
			_tube = new Tube(rings, true);
		}


//			float aX = getGyroYSmooth();
//			float aY = getGyroXSmooth();
//			_camera3.lookAt(_camera3.position.x + aX/50f, _camera3.position.y + aY/50f,_camera3.position.z+1f);
//			_camera3.up.set(0, 1, 0);
		
		_camera3.update();		
		_tube.update(delta, elapsed, getGyroYSmooth()*18, true);
		_stars.update(delta, dz, elapsed);
	}
	
	@Override
	public void render(float delta) {
		update(delta);
		_camera3.apply(Gdx.gl10);
		_stars.render(_camera3);

		_modelBatch.begin(_camera3);
		_tube.render(_modelBatch, _lights);
		_modelBatch.end();
	

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {		
	}

	@Override
	public void dispose() {
				
	}



}
