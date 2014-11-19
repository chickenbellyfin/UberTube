package com.chickenbellyfinn.ubertube;

public class HighscoreManager {

	private static final String PREFS_HIGHSCORE = "highscore";
	private static final String PREFS_MAXCOMBO = "combo";
	private static final String PREFS_BESTTIME = "time";
	private static final String PREFS_GAMECOUNT = "games_played";

	private static final int ACHIEVEMENT_10_GAMES = 0;
	private static final int ACHIEVEMENT_50_GAMES = 1;
	private static final int ACHIEVEMENT_250_GAMES = 2;
	private static final int ACHIEVEMENT_42_POINTS = 3;
	private static final int ACHIEVEMENT_1337_POINTS = 4;
	private static final int ACHIEVEMENT_5280_POINTS = 5;
	private static final int ACHIEVEMENT_CUBE = 6;
	
	private PlatformLayer _platform;
	
	public HighscoreManager(PlatformLayer pl){
		_platform = pl;
		
	}
	
	//TODO: auto submit scores when logged in
	
	public void submitGame(int score, int combo, int time, boolean game){
		int highScore =_platform.getPrefInt(PREFS_HIGHSCORE);
		int maxCombo =_platform.getPrefInt(PREFS_MAXCOMBO);	
		int bestTime =  _platform.getPrefInt(PREFS_BESTTIME);
		int gameCount = _platform.getPrefInt(PREFS_GAMECOUNT);
		
		highScore = Math.max(highScore, score);
		maxCombo = Math.max(maxCombo, combo);
		bestTime = Math.max(bestTime, time);
		if(game){
			gameCount++;
		}
		
		_platform.submitHighscore(highScore);
		_platform.submitMaxCombo(maxCombo);
		_platform.submitBestTime(bestTime*1000);

		_platform.putPrefInt(PREFS_BESTTIME, bestTime);
		_platform.putPrefInt(PREFS_MAXCOMBO, maxCombo);
		_platform.putPrefInt(PREFS_HIGHSCORE, highScore);
		
		_platform.putPrefInt(PREFS_GAMECOUNT, gameCount);	
		checkAchievements(score, combo, time, gameCount);
	}
	
	private void checkAchievements(int score, int combo, int time, int gameCount){
		if(gameCount >= 10)_platform.unlockAchievement(ACHIEVEMENT_10_GAMES);
		if(gameCount >= 50)_platform.unlockAchievement(ACHIEVEMENT_50_GAMES);
		if(gameCount >= 250)_platform.unlockAchievement(ACHIEVEMENT_250_GAMES);

		if(score >= 42)_platform.unlockAchievement(ACHIEVEMENT_42_POINTS);
		if(score >= 1337)_platform.unlockAchievement(ACHIEVEMENT_1337_POINTS);
		if(score >= 5280)_platform.unlockAchievement(ACHIEVEMENT_5280_POINTS);
		
		if(score > 1 && Math.cbrt(score) == Math.round(Math.cbrt(score)))_platform.unlockAchievement(ACHIEVEMENT_CUBE);
	}
	
	public int getHighscore(){
		return _platform.getPrefInt(PREFS_HIGHSCORE);
	}

	public int getMaxCombo(){
		return _platform.getPrefInt(PREFS_MAXCOMBO);
	}
	
	public int getBestTime(){
		return _platform.getPrefInt(PREFS_BESTTIME);
	}
	
	public int getGameCount(){
		return _platform.getPrefInt(PREFS_GAMECOUNT);
	}
}
