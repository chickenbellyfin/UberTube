package com.chickenbellyfinn.uber;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.chickenbellyfinn.ubertube.PlatformLayer;
import com.chickenbellyfinn.ubertube.UberTubeGame;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;

public class MainActivity extends AndroidApplication implements GameHelperListener {
	//A3:1B:15:33:60:FB:C2:E7:A5:8F:7B:47:8A:00:03:C2:55:F8:9A:2B

	
	private GameHelper mHelper;
	private PlatformLayer mLayer;
	private UberTubeGame _game;
	
	public MainActivity(){
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;

        mHelper = new GameHelper(this);
        mLayer = new AndroidPlatformLayer(this, mHelper);
        mHelper.setup(this);
        initialize(_game = new UberTubeGame(mLayer), cfg);
    }
    
    @Override
    public void onStart(){
    	super.onStart();
    	mHelper.onStart(this);
    	if(mLayer.getPrefBool(AndroidPlatformLayer.PREFS_SIGN_IN)){
    		mLayer.autoLogin();
    	}
    }
    
    @Override
    public void onStop(){
    	super.onStop();
    	mHelper.onStop();
    }
    
    @Override
    public void onActivityResult(int request, int response, Intent data) {
        super.onActivityResult(request, response, data);
        mHelper.onActivityResult(request, response, data);
    }

	@Override
	public void onSignInFailed() {
		_game.onSignInEvent(false);
		
	}

	@Override
	public void onSignInSucceeded() {
		mLayer.putPrefBool(AndroidPlatformLayer.PREFS_SIGN_IN, true);
		_game.onSignInEvent(true);
	}
}