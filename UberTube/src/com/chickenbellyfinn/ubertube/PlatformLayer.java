package com.chickenbellyfinn.ubertube;

public interface PlatformLayer{
	
	public String PREFS = "ubertube";
	
	public int getPrefInt(String key);
	public void putPrefInt(String key, int val);
	public boolean getPrefBool(String key);
	public void putPrefBool(String key, boolean val);
	
	//Google Services
	public void autoLogin();
	public void login();
	public void logout();
	public boolean isSignedIn();
	public void submitHighscore(int score);
	public void submitMaxCombo(int combo);
	public void submitBestTime(int time);
	public void unlockAchievement(int which);
	public void showAchievements();
	public void showLeaderboards();

}
