package com.chickenbellyfinn.ubertube;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class UberTubeGame extends Game{
	
	private static final String TAG = UberTubeGame.class.getSimpleName();
	
	private static final String PREFS = "ubertube";


	private PlatformLayer _platform;
	public HighscoreManager _highscores;
	private boolean isMuted;
	private Button _signInOutButton;
	private boolean isCreated = false;

	private boolean restart;
	
	public UberTubeGame(PlatformLayer l){
		_platform = l;	
		_highscores = new HighscoreManager(l);
		
	}
	
	@Override
	public void create() {
		Resources.load();
		isMuted = _platform.getPrefBool("is_muted");
		//setScreen(new TubeScreen(this));
		isCreated = true;
		setScreen(new MainMenuScreen(this));
	}
	
	public boolean isSignedIn(){
		return _platform.isSignedIn();
	}
	
	public void toggleMute(){
		isMuted = !isMuted;
		_platform.putPrefBool("is_muted", isMuted);
		Resources.titleMusic.stop();
		Resources.gameMusic.stop();
	}
	
	public boolean isMuted(){
		return isMuted;
	}
	

	public void doSignInAction(){
		if(isSignedIn()){
			_platform.logout();
			restart = true;
		} else {
			_platform.login();
		}
	}
	
	public void showLeaderboard(){
		_platform.showLeaderboards();
	}
	
	public void showAchievements(){
		_platform.showAchievements();
	}
	
	public void onSignInEvent(boolean success){
		Gdx.app.log(TAG, "onSignInEvent" + success);
		if(isSignedIn()){
			_highscores.submitGame(0, 0, 0, false);
		}
		if(isCreated){
			restart = true;
		}
	}

	@Override
	public void dispose() {
		try{
			getScreen().dispose();
			Resources.dispose();
		}catch(Exception e){}
	}

	@Override
	public void render() {		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		getScreen().render(Gdx.graphics.getDeltaTime());
		if(restart){
			setScreen(new MainMenuScreen(this));
			restart = false;
		}
	}

	@Override
	public void resize(int width, int height) {
		Gdx.app.log(TAG, "resize");
		getScreen().resize(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
