package com.chickenbellyfinn.ubertube;

import static com.chickenbellyfinn.ubertube.Util.expEaseIn;
import static com.chickenbellyfinn.ubertube.Util.getGyroXSmooth;
import static com.chickenbellyfinn.ubertube.Util.getGyroYSmooth;
import static com.chickenbellyfinn.ubertube.Util.randColor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.lights.Lights;
import com.badlogic.gdx.graphics.g3d.lights.PointLight;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
public class GameScreen implements Screen, InputProcessor {
	
	private static final String TAG = GameScreen.class.getSimpleName();
	
	private final float FOV = 105;//105
	private final float MAX_FOV = 120;//120
	private final float STARTING_DISTANCE = -20;
	private final float DEADIMATION_TIME = 2;
	private final float SPEED = Block.DEPTH;
	private final float ANGLE_ACCEL = 3f;
	private final float ANGLE_FRICTION = .05f;
	private final float MAX_ANGLE_SPEED = 90f;
	private final float MIN_ANGLE_SPEED = 0.05f;
	private final float BOOST_ACCEL = 3f;
	
	private final float BASE_MULTIPLIER_PER_SECOND = 1/120f;//increase by 1 every 2 minutes
	
	private final float MAX_ENERGY = 4f;//in seconds
	private final float MIN_ENERGY = 0.1f;
	private final float ENERGY_PER_POINT = 1f/100f;	
	private final float ENERGYBLOCK_WIDTH = 0.8f;
	private final float ENERGYBLOCK_SIDE = 0.01f;
	private final float ENERGYBLOCK_Y = 0.23f;
	private final float ENERGYBLOCK_Z = 0.2f;
	
	private UberTubeGame _game;
	
	private int W, H;
	
	private ModelBatch _modelBatch;
	
	private SpriteBatch _spriteBatch;
	
	private ModelInstance _gameOver;	
	private ModelInstance energyBlock;
	
	private Tube _tube;
	private Starfield _starField;
	
	private OrthographicCamera _camera2;
	private PerspectiveCamera _camera3;
	
	private Lights _lights;
	private DirectionalLight _dLight;
	
	private Lights _titleLights;
	private PointLight _titlePointLight;	
	private DirectionalLight _titleDLight;
	
	private FPSLogger _fps;
	
	private float _y,_z;
	private float _angleVeloAccel = 0;
	private float _angleVelo, _angle;
	private float _speedBoostMultiplier;
	private boolean _boostable;
	private float _elapsed = 0;
	
	private float _scoreMultiplier;
	private float _score;
	private int _maxCombo = 1;
	private Color _lastBounceColor;
	private float _baseMultiplier;
	
	private float _deathTime;
	private float _deathEnergy;
	private float _deathZ;
	private String _scoreString = "";
	private float _scoreStringX;
	private float _scoreStringY;
	private float _scoreStringFinalY;
	private boolean _deadComplete = false;
	private boolean _isDead = false;
	private float _dRotX;
	private float _energy;
	
	private boolean _isCountDown;
	private float _countDownStartTime;
	private String _countDownText = "";
	
	private float lastDY = 1;
	
	private Button _boostButton;
	
	private Button _menuButton;
	private Button _playButton;
	private Button _signInButton;
	private boolean _isSignedIn;
	private boolean _signInAttempted;
	
	public GameScreen(UberTubeGame game){
		_game = game;
		
		W = Gdx.graphics.getWidth();
		H = Gdx.graphics.getHeight();

		_gameOver = new ModelInstance(Resources.gameoverTextModel);
		_gameOver.transform.translate(0, 0, -21);
		
		energyBlock = new Block(Block.getModel(-1, ENERGYBLOCK_WIDTH, ENERGYBLOCK_SIDE, ENERGYBLOCK_SIDE), 0, ENERGYBLOCK_Y, ENERGYBLOCK_Z, 0);
		
		_fps = new FPSLogger();

		_modelBatch = new ModelBatch();
		_spriteBatch = new SpriteBatch();
		
		
		_camera3 = new PerspectiveCamera(FOV, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		_camera3.position.set(0f, _y, _z);
		_camera3.lookAt(0,0,1);
		_camera3.near = 0.1f;
		_camera3.far = 300f;
		_camera3.update();
		
		_camera2 = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		_camera2.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
		_camera2.translate(W/2, H/2);
		_camera2.update();
		
		_lights = new Lights();
        _lights.ambientLight.set(0.6f, 0.6f, 0.6f, 1f);
        _dLight = new DirectionalLight().set(0.4f, 0.4f, 0.4f, 0f, 0f, 1f);
        _lights.add(_dLight);
        
        _titleLights = new Lights();
        _titleLights.ambientLight.set(0.0f, 0.0f, 0.0f, 1f);
       // _titleLights.ambientLight.set(0.4f, 0.4f, 0.4f, 1f);
        Color base = randColor();
        Color highlight;
        while((highlight = randColor()) == base);
         _titlePointLight = new PointLight().set(highlight.r, highlight.g, highlight.b, _camera3.position.x, _camera3.position.y, _camera3.position.z+2.5f, 5);
        // _menuDLight = new DirectionalLight().set(0.1f, 0.85f, 0.1f, 0f, 0f, 1f);
        _titleDLight =  new DirectionalLight().set(base.r*0.7f, base.g*0.7f, base.b*0.7f, 0f, 0f, 1f);
        //_titlePointLight = new PointLight().set(1, 0, 1, _camera3.position.x, _camera3.position.y, _camera3.position.z+2.5f, 5);
        _titleLights.add(_titlePointLight);  
        _titleLights.add(_titleDLight);      
        
        _boostButton = new Button("BOOST",null, W, Resources.fontMargin);
        
        _menuButton = new Button("MENU",null, 0, Resources.fontMargin);
        _menuButton.x = -_menuButton.x;
        _playButton = new Button("PLAY AGAIN", null, W, Resources.fontMargin);
        _isSignedIn = _game.isSignedIn();
        _signInAttempted = false;
        _signInButton = new Button("SIGN IN", Resources.gplusTexture, 0,0);
        _signInButton.x = (W-_signInButton.width)/2;
        _signInButton.setDefaultColor(Resources.COLOR_GPLUS);
        Gdx.input.setInputProcessor(this);
        
        if(!_game.isMuted()){
	        Resources.titleMusic.stop();
	        Resources.gameMusic.stop();
	        //Resources.gameMusic.setVolume(0.5f);
	        Resources.gameMusic.setLooping(true);
        }
        
        resetGame();
        
	}

	
	public void update(float delta){
		
		//_fps.log();
		_elapsed  += delta;
		//Gdx.app.log(TAG, "d = "+delta);
		float dz = SPEED*delta;
		if(!_game.isMuted() && !Resources.gameMusic.isPlaying()){
			Resources.gameMusic.play();
		}
		
		if(_z < 0){		
			_z = expEaseIn(_elapsed, STARTING_DISTANCE, 0);
			_energy = expEaseIn(_elapsed, 0, MAX_ENERGY);
			_boostButton.x = expEaseIn(_elapsed, W, W-(_boostButton.width+Resources.fontMargin));
			//_z /= 1.1;
			//_energy = (1-(_z/STARTING_DISTANCE))*MAX_ENERGY;
			if(Math.abs(_z) < 0.01){
				_z = 0;
				_isCountDown = true;
				_countDownStartTime = _elapsed;
				_energy = MAX_ENERGY;
			}
			_camera3.position.z = _z;
		} else if(_isCountDown){
			int c = 3-((int)(_elapsed-_countDownStartTime));
			_countDownText = (c == 0)?"GO!":""+c;
			if(c == -1){
				_isCountDown = false;
				_elapsed = 0;
			}
		}else if(!_isDead){

			if(Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)){
				_angleVelo = Gdx.input.getAccelerometerY()*36;
			} else {	

				_angleVelo = (((float)Gdx.input.getX()/Gdx.graphics.getWidth())*20f-10f)*36;
				if(Gdx.input.isKeyPressed(Keys.LEFT)){
					_angleVelo = -MAX_ANGLE_SPEED;
				} else if (Gdx.input.isKeyPressed(Keys.RIGHT)){
					_angleVelo = MAX_ANGLE_SPEED;
				}
			}
			
			if(!_boostable && _energy >= MIN_ENERGY){
				_boostable = true;
				setEnergyColor(_lastBounceColor);				
			}
			
			if((_boostButton.isTouched || Gdx.input.isKeyPressed(Keys.SPACE)) && _energy > 0 && _boostable){
				_speedBoostMultiplier += BOOST_ACCEL*delta;
				_speedBoostMultiplier = Math.min(_speedBoostMultiplier, 3f);
				_energy = Math.max(_energy-delta, 0);
				if(_energy <= 0){
					_boostable = false;
					setEnergyColor(Color.GRAY);
				}
			} else {
				_speedBoostMultiplier -= BOOST_ACCEL*delta;
				_speedBoostMultiplier = Math.max(_speedBoostMultiplier, 1f);
				_energy = Math.min(_energy+dz*_scoreMultiplier*ENERGY_PER_POINT, MAX_ENERGY);
			}
			_baseMultiplier += BASE_MULTIPLIER_PER_SECOND*delta;
			dz *= _baseMultiplier;
			dz *= _speedBoostMultiplier;
			
			_angleVelo = Math.min(Math.abs(_angleVelo),MAX_ANGLE_SPEED)* Math.signum(_angleVelo);
			
			_angle += _angleVelo*delta;
			_angleVelo -= (_angleVelo-(_angleVelo*ANGLE_FRICTION))*delta;
			
			if(Math.abs(_angleVelo) < MIN_ANGLE_SPEED){
				_angleVelo = 0;
			}
					
			
			float dy =  ((float)Math.abs(Math.cos(_elapsed)*BlockRing.RADIUS) - (BlockRing.RADIUS-Block.HEIGHT)) - _y;
			
			if(lastDY < 0 && dy > 0){
				Block bounce = _tube.bounce(_y, _z, _angle);
				if(bounce != null){
					if(bounce.color == _lastBounceColor){
						_scoreMultiplier++;
					} else {
						_lastBounceColor = bounce.color;
						if(_scoreMultiplier > _maxCombo){
							_maxCombo = (int) _scoreMultiplier;
						}
						_scoreMultiplier = 1;
						if(_boostable){
							setEnergyColor(_lastBounceColor);
						}
					}
				} else {
					_deathZ = _z;
					_deathTime = _elapsed;
					_deathEnergy = _energy;
					_isDead = true;
					//do dead things
					int oldHighscore = _game._highscores.getHighscore();
					if((int)_score > oldHighscore){
						_scoreString = "NEW HIGHSCORE : "+(int)_score;
					} else {
						_scoreString = "SCORE : "+(int)_score;
					}
					_scoreStringX = (W-Resources.font.getBounds(_scoreString).width)/2;
					_scoreStringFinalY = (H-Resources.font.getLineHeight())/2;
					_scoreStringY = -Resources.font.getLineHeight();
					_game._highscores.submitGame((int)_score, (int)_maxCombo, (int)_elapsed, true);
				}
			}
			
			lastDY = dy;
			_y += dy;
			_z += dz;
			if(!_isDead){
				_score += dz*_scoreMultiplier;
			}
		} else if(_isDead){//dead
			float aX = getGyroYSmooth();
			float aY = getGyroXSmooth();
			
			float pDeath = Math.min(1, (_elapsed-_deathTime)/DEADIMATION_TIME); //percent of time of deadness
			_camera3.lookAt(_camera3.position.x + aX/50f, _camera3.position.y + aY/50f,_camera3.position.z+1f);
			_camera3.up.set(0, 1, 0);
			_z = expEaseIn(pDeath, _deathZ, _deathZ + _tube.length);	
			_boostButton.x = expEaseIn(pDeath, W-(_boostButton.width+Resources.fontMargin), W);
			_menuButton.x = expEaseIn(pDeath, -_menuButton.width, Resources.fontMargin);
			_playButton.x = expEaseIn(pDeath, W, W-(_playButton.width+Resources.fontMargin));
			if( _elapsed-_deathTime < DEADIMATION_TIME){	
				_y -= dz;	
			} else {
				_deadComplete = true;
				_dRotX = 0;
			}
			_scoreStringY = expEaseIn(pDeath, -Resources.font.getLineHeight(), _scoreStringFinalY);
			_signInButton.y = expEaseIn(pDeath-0.1f, -Button.minHeight, _scoreStringFinalY-(Resources.font.getLineHeight()+Button.minHeight+Resources.fontMargin));
			_energy = expEaseIn(pDeath, _deathEnergy, 0);
			_titlePointLight.position.set(aX/5f, _y+aY/5f,expEaseIn(pDeath, 0, 3f));
			_gameOver.transform.idt();
			_gameOver.transform
				.translate(0, _y, expEaseIn(pDeath, 0, 3.5f))
				.rotate(1, 0, 0, 0)
				.translate(0, expEaseIn(pDeath, 0, 2f), 0);
			
		}

		_starField.update(delta, dz, _angle);
	    _tube.update(delta,_z, _angle, !_isDead);
		energyBlock.transform
			.idt()
			.translate(0, _y+0.23f, _camera3.position.z+0.2f)
			.rotate(1, 0, 0, _elapsed*90)
			.scale(_energy/MAX_ENERGY, 1, 1);
		_camera3.position.y = _y;
		_camera3.rotate(_dRotX, 1, 0, 0);
		_camera3.fieldOfView = FOV + ((_speedBoostMultiplier-1)*(MAX_FOV-FOV));
		_camera3.update();
	}
	
	@Override
	public void render(float delta) {
//		float target = (1f/30f)-delta;
//		if(target > 0){
//			try{
//				Thread.sleep((long)(target*1000));
//			} catch(Exception e){}
//		}
		
		//Gdx.app.log(TAG, "delta u"+delta);
		update(delta);

		_starField.render(_camera3);
		_modelBatch.begin(_camera3);
		if(!_deadComplete){
			_tube.render(_modelBatch, _lights);	
		}

		_modelBatch.end();	
		
		_camera3.fieldOfView = FOV;
		_camera3.update();
		_modelBatch.begin(_camera3);
		if(_isDead){
			_modelBatch.render(_gameOver, _titleLights);
		}
		if(!_deadComplete){
			_modelBatch.render(energyBlock, _lights);
		}
		_modelBatch.end();
		
		_spriteBatch.begin();
		int iscore = (int)_score;
		BitmapFont font = Resources.font;
		float margin = Resources.fontMargin;
		font.setColor(Color.WHITE);
		
		if(_isCountDown){
			font.draw(_spriteBatch, _countDownText, (W-font.getBounds(_countDownText).width)/2, (H+font.getLineHeight())/2);
		}
		
		font.draw(_spriteBatch, iscore+"", W-(font.getBounds(iscore+"").width+margin), H-margin);
		if(!_isDead && _scoreMultiplier > 1){
			font.setColor(_lastBounceColor);
			int imult = (int)_scoreMultiplier;
			font.draw(_spriteBatch, "x"+imult, W-(font.getBounds("x"+imult).width+margin), H-(font.getLineHeight()+margin));
		}
		
		if(_isDead){
			font.setColor(Color.WHITE);
			font.draw(_spriteBatch, _scoreString,_scoreStringX, _scoreStringY);
			_menuButton.draw(_spriteBatch);
			_playButton.draw(_spriteBatch);
			if(!_isSignedIn && !_signInAttempted){
				_signInButton.draw(_spriteBatch);
			}
		}
		_boostButton.draw(_spriteBatch);
		_spriteBatch.end();
	}
	
	private void resetGame(){
		_energy = 0;
		_scoreMultiplier = 1;
		_score = 0;
		_lastBounceColor = Color.WHITE;
		_elapsed = 0;
		_z = STARTING_DISTANCE;
		_y = 0;
		_angleVelo = 0;
		_angle = 0;
		_tube = new Tube(10, true);
		_starField = new Starfield(100);
		_speedBoostMultiplier = 1;
		_baseMultiplier = 1;
		setEnergyColor(Color.WHITE);
		
	}
	
	private void setEnergyColor(Color c){
		energyBlock.materials.get(0).set(ColorAttribute.createDiffuse(c));
	}

	@Override
	public void resize(int width, int height) {
		
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
		// TODO Auto-generated method stub
		
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
		return touchDragged(screenX, screenY, 0);
		
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		_boostButton.touchUp();
		if(_deadComplete){
			if(_menuButton.touch(screenX, screenY)){
				_game.setScreen(new MainMenuScreen(_game));
			} else if(_playButton.touch(screenX, screenY)){
				_game.setScreen(new GameScreen(_game));
			} else if(_signInButton.touch(screenX, screenY) && !_game.isSignedIn() && !_signInAttempted){
				_game.doSignInAction();
				_signInAttempted = true;
			}
		}
		return false;
	}


	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		_boostButton.touch(screenX, screenY);
		_menuButton.touch(screenX, screenY);
		_playButton.touch(screenX, screenY);
		_signInButton.touch(screenX, screenY);
		return false;
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

}
