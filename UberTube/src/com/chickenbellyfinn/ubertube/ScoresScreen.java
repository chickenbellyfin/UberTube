package com.chickenbellyfinn.ubertube;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import static com.chickenbellyfinn.ubertube.Util.*;

public class ScoresScreen implements Screen, InputProcessor{
	
	private static final String TAG = ScoresScreen.class.getSimpleName();
	
	private static final float ANIM_TIME = .8f;
	private static final float ZOOM_SPEED = 5f;
	
	private class ColoredText {
		
		public String text;
		public Color color;
		
		public ColoredText(String t, Color c){
			text = t;
			color = c;
		}		
	}
	
	private int W, H;
	
	private UberTubeGame _game;
	private SpriteBatch _batch;
	
	private PerspectiveCamera _camera3;
	private Starfield _starfield;
	
	private String _highScore;
	private String _maxCombo;
	private String _bestTime;
	private String _gameCount;
	
	private String _labelText;
	private String _scoreText;
	
	private ColoredText[] labels;
	private ColoredText[] scores;
	
	private float _lineHeight;
	private float _textY;
	
	private float _labelX;
	private float _scoreX;
	
	private float _initialLabelX;
	private float _initialScoreX;
	private float _finalLabelX;
	private float _finalScoreX;
	
	private float _elapsed;
	
	private boolean _isStarting;
	private boolean _isEnding;
	private float _endTime;
	
	private float margin;
	
	private Button _selection;
	private Button _menuButton;
	private Button _achievementButton;
	private Button _leaderboardButton;
	private Button _signInButton;
	private boolean _signedIn;
	private float _playServiceX;
	
	public ScoresScreen(UberTubeGame game){
		_game = game;
		
		W = Gdx.graphics.getWidth();
		H = Gdx.graphics.getHeight();
		
		_camera3 = new PerspectiveCamera(105, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		_camera3.position.set(0, 0, -20);
		_camera3.lookAt(0, 0, 2);
		//_pCamera.direction.scl(0, 0, -1);
		_camera3.near = 0.1f;
		_camera3.far = 300f;
		_camera3.update();
		
		_starfield = new Starfield(250);
		
		_batch = new SpriteBatch();		
		
		int time = _game._highscores.getBestTime();
		int h = (time/3600);
		time %= 3600;
		int m = (time/60);
		time %= 60;
		int s = time;

		_highScore =  _game._highscores.getHighscore()+"";
		_maxCombo  =  _game._highscores.getMaxCombo()+"";
		_bestTime  = String.format("%02d:%02d:%02d", h, m, s);
		_gameCount = _game._highscores.getGameCount()+"";
		
		_labelText = " HIGHSCORE \n BEST COMBO \n LONGEST GAME \n GAMES PLAYED ";
		_scoreText = " : "+_highScore + "\n : " + _maxCombo + "\n : " + _bestTime + "\n : " + _gameCount;
		BitmapFont font = Resources.font;
		_lineHeight = font.getLineHeight();
		
		TextBounds labelBounds = Resources.font.getMultiLineBounds(_labelText);
		float labelW = labelBounds.width;
		float labelH = labelBounds.height;

		TextBounds scoreBounds = Resources.font.getMultiLineBounds(_scoreText);
		float scoreW = scoreBounds.width;
		
		_textY = (H/2+labelH);
		
		_initialLabelX = -labelW;
		_initialScoreX = W;
		_finalLabelX = (W-(labelW+scoreW))/2;
		_finalScoreX = _finalLabelX + labelW;
		
		labels = new ColoredText[]{
				new ColoredText(" HIGHSCORE ", randColor()),
				new ColoredText(" BEST COMBO ", randColor()),
				new ColoredText(" LONGEST GAME ", randColor()),
				new ColoredText(" GAMES PLAYED ", randColor()),
			};
		scores = new ColoredText[]{
				new ColoredText(" : " + _highScore, randColor()),
				new ColoredText(" : " + _maxCombo, randColor()),
				new ColoredText(" : " + _bestTime, randColor()),
				new ColoredText(" : " + _gameCount, randColor()),
			};
		
		_isStarting = true;
		
		margin = Resources.fontMargin;
		_menuButton = new Button("MENU",null, margin, margin);
		_leaderboardButton = new Button("LEADERBOARDS",Resources.crownTexture, 0, margin);
		_achievementButton = new Button("ACHIEVEMENTS", Resources.ribbonTexture, 0, margin+Button.minHeight*1.6f);
		_signInButton = new Button("SIGN IN", Resources.gplusTexture, 0, margin);
        _signInButton.setDefaultColor(Resources.COLOR_GPLUS);
		_signedIn = _game.isSignedIn();
		_playServiceX = Math.min(W-(_achievementButton.width+margin), W-(_leaderboardButton.width+margin));
		Gdx.input.setInputProcessor(this);
	}

	private void update(float delta){
		_elapsed += delta;
		
		if(_camera3.position.z < 0){
			_camera3.position.z  /= 1.1;
			
			if(Math.abs(_camera3.position.z) < 0.01){
				_camera3.position.z = 0;
			}
		}
		_starfield.update(delta, delta/2, _elapsed*5);
		
		if(_isStarting){
			float x = Math.min(_elapsed/ANIM_TIME, 1f);
			_labelX = expEaseIn(x, _initialLabelX, _finalLabelX);
			_scoreX = expEaseIn(x, _initialScoreX, _finalScoreX);
			_menuButton.x = Util.expEaseIn(x, -_menuButton.width, margin);
			if(_signedIn){
				_achievementButton.x = expEaseIn(x, W,_playServiceX);
				_leaderboardButton.x = expEaseIn(x, W,_playServiceX);
			} else {
				_signInButton.x = expEaseIn(x, W, W-(_signInButton.width+margin));
			}
			_isStarting = x < 1;
		} else if (_isEnding){
			float x = Math.min((_elapsed-_endTime)/ANIM_TIME, 1f);
			_labelX = expEaseOut(x, _finalLabelX,_initialLabelX);
			_scoreX = expEaseOut(x, _finalScoreX, _initialScoreX);
			_menuButton.x = expEaseOut(x, margin, -_menuButton.width);
			if(_signedIn){
				_achievementButton.x = expEaseOut(x, _playServiceX, W);
				_leaderboardButton.x = expEaseOut(x, _playServiceX, W);
			} else {
				_signInButton.x = expEaseOut(x, W-(_signInButton.width+margin), W);
			}
			_camera3.position.z += delta*ZOOM_SPEED;
			if(x >= 1f){
				if(_selection == _menuButton){
					_game.setScreen(new MainMenuScreen(_game));
				} else if(_selection == _leaderboardButton){
					_game.showLeaderboard();
					_game.setScreen(new ScoresScreen(_game));
					//start leaderboard view
				} else if(_selection == _achievementButton){
					_game.showAchievements();
					_game.setScreen(new ScoresScreen(_game));
					//start achievements view
				}
				_selection = null;
			}
		}
	}
	
	@Override
	public void render(float delta) {
		update(delta);
		
		_camera3.update();
		_starfield.render(_camera3);	
		
		_batch.begin();		
		BitmapFont font = Resources.font;
		for(int i = 0; i < 4; i++){
			font.setColor(labels[i].color);
			font.draw(_batch, labels[i].text, _labelX, _textY - _lineHeight*i);
			font.setColor(scores[i].color);
			font.draw(_batch, scores[i].text, _scoreX, _textY - _lineHeight*i);
		}
		
//		font.drawMultiLine(_batch, _labelText, _labelX, _textY);		
//		font.drawMultiLine(_batch, _scoreText, _scoreX, _textY);
		_menuButton.draw(_batch);
		if(_signedIn){
			_achievementButton.draw(_batch);
			_leaderboardButton.draw(_batch);
		} else {
			_signInButton.draw(_batch);
		}
		_batch.end();
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
		// TODO A
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
		return touchDragged(screenX, screenY, button);
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(_menuButton.touch(screenX, screenY)){
			_isEnding = true;
			_endTime = _elapsed;
			_selection = _menuButton;
		} else if(_leaderboardButton.touch(screenX, screenY)){
			_isEnding = true;
			_endTime = _elapsed;
			_selection = _leaderboardButton;			
		} else if (_achievementButton.touch(screenX, screenY)){
			_isEnding = true;
			_endTime = _elapsed;
			_selection = _achievementButton;
		} else if(_signInButton.touch(screenX, screenY)){
			_game.doSignInAction();
			_signInButton.touchUp();
		}
		if(_selection != null){
			_selection.touchUp();
		}
//		if(!_isStarting){
//			_isEnding = true;
//			_endTime = _elapsed;
//		}
		return false;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		_menuButton.touch(screenX, screenY);
		_achievementButton.touch(screenX, screenY);
		_leaderboardButton.touch(screenX, screenY);
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
