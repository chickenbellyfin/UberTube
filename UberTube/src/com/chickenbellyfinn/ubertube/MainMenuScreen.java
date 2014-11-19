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

public class MainMenuScreen implements Screen, InputProcessor{
	
	private static final String TAG = MainMenuScreen.class.getSimpleName();
	
	private final float FOV = 105f;

	private final float STARTING_DISTANCE = -20;
	private final float DISTANCE = 3f;
	private final float MENU_X = 10f;
	private final float MENU_Z = 3.5f;
	private final float PLAY_Y = 4.0f;
	private final float SCORE_Y = 2.7f;
	private final float SPEED = 5f;
	private final float ACCEL = 1.01f;
	private final float COLOR_FLIP_TIME = 0.43f;
	
	private UberTubeGame _game;
	
	private int W;
	private int H;
	
	private OrthographicCamera _camera2;
	private PerspectiveCamera _camera3;
	private float zoomSpeed = 5f;
	
	private Lights _menuLights;
	private PointLight _menuPointLight;
	
	private Lights _lights;
	private DirectionalLight _menuDLight;
	private PointLight _pLight;
	
	private ModelInstance _title;
	private float _lastFlip;
	
	//private ModelInstance _play;
	//private BoundingBox _playBox;
	
	//private ModelInstance _scores;
	//private BoundingBox _scoreBox;
	
	//private ModelInstance _selection = null;
	
	
	private Starfield _stars;
	
	private ModelBatch _modelBatch;
	
	private Tube _tube;
	private float z = 0;
	
	private float _elapsed = 0;
	private float endStartTime;
	
	private SpriteBatch _spriteBatch;
	
	private float _buttonX;
	private Button _playButton;
	private Button _scoresButton;
	private Button _signInOutButton;
	private Button _selection;
	
	private ToggleButton _muteButton;
	
	private boolean _isSignedIn;
	
	
	public MainMenuScreen(UberTubeGame game){
		_game = game;
		W = Gdx.graphics.getWidth();
		H = Gdx.graphics.getHeight();
		
		Starfield.POINT_SIZE = Math.round(W/1000f)+1;
		Gdx.app.log(TAG, "pointsize "+Starfield.POINT_SIZE);
		
		_modelBatch = new ModelBatch();	
		
        _title = new ModelInstance(Resources.titleTextModel);
        //_play = new ModelInstance(Resources.playTextModel);
        //_scores = new ModelInstance(Resources.scoresTextModel);
//        _title.transform.rotate(1, 0, 0, 90);
//        _title.transform.rotate(0,0,1,180);
//        _title.transform.translate(-TUBE_DISTANCE*0.75f, -5, -TUBE_DISTANCE*1.5f);
        
		_tube = new Tube(10, false);
		
		_camera3 = new PerspectiveCamera(FOV, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		_camera3.position.set(DISTANCE, DISTANCE, -20);
		_camera3.lookAt(DISTANCE, DISTANCE, 2);
		//_pCamera.direction.scl(0, 0, -1);
		_camera3.near = 0.1f;
		_camera3.far = 300f;
		_camera3.update();
		
		_camera2 = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		_camera2.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
		_camera2.translate(W/2, H/2);
		_camera2.update();
		
		_spriteBatch = new SpriteBatch();

		_stars = new Starfield(250);
		
        _title.transform.setToTranslation(DISTANCE, DISTANCE, MENU_Z);
        _title.transform.translate(0, 2.5f, 0);
        
//        _play.transform.setToTranslation(MENU_X, PLAY_Y, MENU_Z);
//        _play.calculateBoundingBox(_playBox = new BoundingBox());
//        
//        _scores.transform.setToTranslation(MENU_X, SCORE_Y, MENU_Z);
//        _scores.calculateBoundingBox(_scoreBox = new BoundingBox());
		
		_lights = new Lights();
        _lights.ambientLight.set(0.0f, 0.0f, 0.0f, 1f);
        //_lights.ambientLight.set(0.6f, 0.6f, 0.6f, 1f);
        _pLight = new PointLight().set(1, 1, 1,DISTANCE, DISTANCE, 0.5f, 20);
        //_lights.add(_dLight);
        _lights.add(_pLight);
        
        _menuLights = new Lights();

        _menuLights.ambientLight.set(0.0f, 0.0f, 0.0f, 1f);
       // _titleLights.ambientLight.set(0.4f, 0.4f, 0.4f, 1f);

        _menuPointLight = new PointLight().set(1, 1, 1, _camera3.position.x, _camera3.position.y, _camera3.position.z+2.5f, MENU_Z*2);
       // _menuDLight = new DirectionalLight().set(0.1f, 0.85f, 0.1f, 0f, 0f, 1f);
        _menuDLight = new DirectionalLight().set(1,1,1, 0f, 0f, 1f);
        _menuLights.add(_menuPointLight);  
        _menuLights.add(_menuDLight);  
        
        titleColorFlip();
        
        _isSignedIn = _game.isSignedIn();
        _buttonX = Resources.fontMargin*4;
        _playButton = new Button("PLAY",null, _buttonX,H*0.55f);
        TextureRegion scoresIcon = _isSignedIn?Resources.controllerTexture:null;
        _scoresButton = new Button("SCORES",scoresIcon, _buttonX, _playButton.y-Button.minHeight*1.6f);
        if(_game.isSignedIn()){
        	_signInOutButton = new Button("SIGN OUT", Resources.gplusTexture, _buttonX, Resources.fontMargin);
        	_signInOutButton.setDefaultColor(Color.DARK_GRAY);
        } else {
        	_signInOutButton = new Button("SIGN IN ", Resources.gplusTexture, _buttonX,  _scoresButton.y-Button.minHeight*1.6f);
            _signInOutButton.setDefaultColor(Resources.COLOR_GPLUS);
        }
        float muteSize = Resources.font.getLineHeight()*1f;
        _muteButton = new ToggleButton(Resources.speakerOn, Resources.speakerOff, 0, H-(muteSize+Resources.fontMargin),muteSize, muteSize);
        
        if(!_game.isMuted()){
        Resources.gameMusic.stop();
	        if(!Resources.titleMusic.isPlaying()){
		        //Resources.titleMusic.setVolume(0.5f);
		        Resources.titleMusic.setLooping(true);
		        Resources.titleMusic.play();
	        }
        } else {
        	_muteButton.toggle();
        }
        
        
        Gdx.input.setInputProcessor(this);
        
       
	}

	private void update(float delta){
		_elapsed += delta;
		float dz= SPEED*delta;
		z += dz;
		
		if(!_game.isMuted() &&_elapsed-_lastFlip > COLOR_FLIP_TIME)titleColorFlip();
		
		if(_camera3.position.z < 0){
			_camera3.position.x = expEaseIn(_elapsed, -DISTANCE, DISTANCE);
			_camera3.position.y = expEaseIn(_elapsed, -DISTANCE, DISTANCE);
			_camera3.position.z = expEaseIn(_elapsed, STARTING_DISTANCE, 0);
			_playButton.x = expEaseIn(_elapsed, -_playButton.width, _buttonX);
			_scoresButton.x = expEaseIn(_elapsed-0.1f, -_scoresButton.width, _buttonX);
			_signInOutButton.x = expEaseIn(_elapsed-0.2f, -_signInOutButton.width, _buttonX);
			_muteButton.x = expEaseIn(_elapsed, -_muteButton.width, Resources.fontMargin);
			if(Math.abs(_camera3.position.z) < 0.01){
				_camera3.position.x = DISTANCE;
				_camera3.position.y = DISTANCE;
				_camera3.position.z = 0;
			}
		}

		if(_selection == null){

			float aX = getGyroYSmooth();
			float aY = getGyroXSmooth();
			_camera3.lookAt(_camera3.position.x + aX/50f, _camera3.position.y + aY/50f,_camera3.position.z+1f);
			_menuPointLight.position.set(_camera3.position.x + aX/5f, _camera3.position.y+ 1f +  aY/5f, _camera3.position.z+2.5f);
			_camera3.up.set(0, 1, 0);
		}else {
			_camera3.fieldOfView *= ACCEL;
			zoomSpeed *= ACCEL;
			_camera3.fieldOfView = Math.min(_camera3.fieldOfView, 170);
			//_selection.transform.translate(0, 0,zoomSpeed*dz/4);
			_camera3.position.z += dz*zoomSpeed;
			float x = _elapsed-endStartTime;
			_playButton.x = expEaseIn(x,_buttonX, -_playButton.width);
			_scoresButton.x = expEaseIn(x, _buttonX, -_scoresButton.width);
			_signInOutButton.x = expEaseIn(x, _buttonX, -_signInOutButton.width);
			_muteButton.x = expEaseIn(x, Resources.fontMargin, -_muteButton.width);
			if(!_game.isMuted() && _selection == _playButton){
				float vol = expEaseIn(x, 0.5f, 0f);
				Resources.titleMusic.setVolume(vol);
			}
			if(_selection != null){
				_selection.x = expEaseIn(x, _buttonX, W);
			}
			if(_elapsed-endStartTime > 1.7){
				if(_selection == _playButton){
					_game.setScreen(new GameScreen(_game));
				} else if (_selection == _scoresButton){
					_game.setScreen(new ScoresScreen(_game));
				}
			}
		}
		_camera3.update();		
		_tube.update(delta, z, 0, true);
		_stars.update(delta, dz/SPEED, _elapsed*SPEED);
	}
	
	@Override
	public void render(float delta) {
		update(delta);
		_camera3.apply(Gdx.gl10);
		_stars.render(_camera3);
		_modelBatch.begin(_camera3);
		_modelBatch.render(_title, _menuLights);
		//_modelBatch.render(_play, _menuLights);
		//_modelBatch.render(_scores, _menuLights);
		_tube.render(_modelBatch, _lights);
		_modelBatch.end();
	
		_spriteBatch.begin();
		BitmapFont font = Resources.font;
		float margin = Resources.fontMargin;
		font.setColor(Color.WHITE);
		_playButton.draw(_spriteBatch);
		_scoresButton.draw(_spriteBatch);
		_signInOutButton.draw(_spriteBatch);
		_muteButton.draw(_spriteBatch);
		Resources.font.setColor(Color.WHITE);
		Resources.font.draw(_spriteBatch, "Inspired by Dan Church's UberTube", margin,margin*1.5f);
		_spriteBatch.end();
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
		Gdx.input.setInputProcessor(this);		
	}

	@Override
	public void dispose() {
				
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		touchDragged(screenX, screenY, 0);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {		
//		Ray pick = _camera3.getPickRay(screenX, screenY);
//		//Gdx.app.log(TAG, pick.toString()+"");
//		if(Intersector.intersectRayBounds(pick, getPlayBox(), null)){
//			Gdx.app.log(TAG, "PLAY!");
//			_selection = _play;
//			endStartTime = elapsed;
//		} else if(Intersector.intersectRayBounds(pick, getScoreBox(), null)){
//			Gdx.app.log(TAG, "SCORES!");
//			_selection = _scores;
//			endStartTime = elapsed;
//		}
		touchDragged(screenX, screenY, 0);
		if(_playButton.isTouched){
			_selection = _playButton;
			endStartTime = _elapsed;
		} else if (_scoresButton.isTouched){
			_selection = _scoresButton;
			endStartTime = _elapsed;
		} else if(_signInOutButton.isTouched){
			_game.doSignInAction();
			_signInOutButton.touchUp();
		}
		
		if(_muteButton.isTouched){
			_game.toggleMute();
			_muteButton.toggle();
			_muteButton.touchUp();
			if(!_game.isMuted()){
				_lastFlip = _elapsed;
				Resources.titleMusic.setVolume(0.5f);
				Resources.titleMusic.play();
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		_playButton.touch(screenX, screenY);
		_scoresButton.touch(screenX, screenY);
		_signInOutButton.touch(screenX, screenY);
		_muteButton.touch(screenX, screenY);
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return touchDragged(screenX, screenY, 0);
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void titleColorFlip(){
       Color base = randColor();
       Color highlight;
       while((highlight = randColor()) == base);
       
       _menuPointLight.set(highlight, _menuPointLight.position, _menuPointLight.intensity);
       _menuDLight.set(base.r*0.7f, base.g*0.7f, base.b*0.7f, _menuDLight.direction);
       _lastFlip = _elapsed;
	       
	}

//	private BoundingBox getPlayBox(){
//		Vector3 min = _playBox.getMin().cpy().add(MENU_X, PLAY_Y, MENU_Z);
//		Vector3 max = _playBox.getMax().cpy().add(MENU_X, PLAY_Y, MENU_Z);
//		BoundingBox b = new BoundingBox().set(min, max);
//		return b;
//	}
//	
//	private BoundingBox getScoreBox(){
//		Vector3 min = _scoreBox.getMin().cpy().add(MENU_X, SCORE_Y, MENU_Z);
//		Vector3 max = _scoreBox.getMax().cpy().add(MENU_X, SCORE_Y, MENU_Z);
//		BoundingBox b = new BoundingBox().set(min, max);
//		return b;
//	}
}
