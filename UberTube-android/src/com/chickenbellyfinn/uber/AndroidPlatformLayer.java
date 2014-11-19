package com.chickenbellyfinn.uber;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.chickenbellyfinn.ubertube.PlatformLayer;
import com.google.example.games.basegameutils.GameHelper;

public class AndroidPlatformLayer implements PlatformLayer{
	
	private static final String TAG = AndroidPlatformLayer.class.getSimpleName();
	
	public static final int REQUEST_LEADERBOARD = 9001;
	public static final int REQUEST_ACHIEVEMENTS = 9002;
	
	private static final int[] ACHIEVEMENTS = {
		R.string.achievement_10_games,
		R.string.achievement_50_games,
		R.string.achievement_250_games,
		R.string.achievement_42_points,
		R.string.achievement_1337_points,
		R.string.achievement_5280_points,
		R.string.achievement_cube,
	};

	public static final String PREFS_SIGN_IN = "signed_in";
	private Activity mActivity;
	private GameHelper mHelper;
	
	public AndroidPlatformLayer(Activity c, GameHelper h){
		mActivity = c;
		mHelper = h;
	}

	@Override
	public int getPrefInt(String key) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
		return prefs.getInt(key, 0);
	}

	@Override
	public void putPrefInt(String key, int val) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
		prefs.edit().putInt(key, val).commit();		
	}
	
	@Override
	public boolean getPrefBool(String key){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
		return prefs.getBoolean(key, false);
	}
	
	@Override
	public void putPrefBool(String key, boolean val){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
		prefs.edit().putBoolean(key, val).commit();			
	}
	
	
	@Override 
	public void autoLogin(){
			mActivity.runOnUiThread(new Runnable(){
				@Override
				public void run(){
					try{
						mHelper.getGamesClient().connect();
					}catch(Exception e){}
				}
			});				
	}

	@Override
	public void login() {
		putPrefBool(PREFS_SIGN_IN, true);
			mActivity.runOnUiThread(new Runnable(){
				@Override
				public void run(){
					try{
					mHelper.beginUserInitiatedSignIn();
					}catch(Exception e){}
				}
			});
	}

	@Override
	public void logout() {
		putPrefBool(PREFS_SIGN_IN, false);
			mActivity.runOnUiThread(new Runnable(){
				@Override
				public void run(){
					try{
					mHelper.signOut();
					}catch(Exception e){}
				}
			});
		
	}

	@Override
	public boolean isSignedIn() {
		return mHelper.isSignedIn();
	}

	@Override
	public void submitHighscore(int score) {
		if(isSignedIn())
		mHelper.getGamesClient().submitScore(mActivity.getString(R.string.leaderboard_highscore), score);
	}
	
	@Override
	public void submitMaxCombo(int combo) {
		if(isSignedIn())
		mHelper.getGamesClient().submitScore(mActivity.getString(R.string.leaderboard_biggestcombo), combo);

		
	}
	

	@Override
	public void unlockAchievement(int which) {
		if(isSignedIn())
		mHelper.getGamesClient().unlockAchievement(mActivity.getString(ACHIEVEMENTS[which]));
		
	}

	@Override
	public void submitBestTime(int time) {
		if(isSignedIn()){
			Log.d(TAG, "showing achievements");
			mHelper.getGamesClient().submitScore(mActivity.getString(R.string.leaderboard_longestgame), time);		
		}
	}
	

	@Override
	public void showLeaderboards() {
		if(isSignedIn()){
			Log.d(TAG, "show leaderboards");
			
			mActivity.startActivityForResult(mHelper.getGamesClient().getAllLeaderboardsIntent(), REQUEST_LEADERBOARD);
		}
	}

	@Override
	public void showAchievements() {
		mActivity.startActivityForResult(mHelper.getGamesClient().getAchievementsIntent(), REQUEST_ACHIEVEMENTS);
		
	}


	
}
