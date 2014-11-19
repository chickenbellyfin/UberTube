package com.chickenbellyfinn.ubertube;

import com.badlogic.gdx.Gdx;

public class DefaultPlatformLayer implements PlatformLayer {

	@Override
	public int getPrefInt(String key) {
		return Gdx.app.getPreferences(PREFS).getInteger(key);
	}

	@Override
	public void putPrefInt(String key, int val) {
		Gdx.app.getPreferences(PREFS).putInteger(key, val);
		Gdx.app.getPreferences(PREFS).flush();
		
	}

	@Override 
	public void autoLogin(){
		
	}
	
	@Override
	public void login() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSignedIn() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void showLeaderboards() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void submitHighscore(int score) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getPrefBool(String key) {
		return Gdx.app.getPreferences(PREFS).getBoolean(key);
	}

	@Override
	public void putPrefBool(String key, boolean val) {
		Gdx.app.getPreferences(PREFS).putBoolean(key, val);
		Gdx.app.getPreferences(PREFS).flush();		
	}

	@Override
	public void submitMaxCombo(int combo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void submitBestTime(int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unlockAchievement(int which) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showAchievements() {
		// TODO Auto-generated method stub
		
	}

}
