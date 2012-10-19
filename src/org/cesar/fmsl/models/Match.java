package org.cesar.fmsl.models;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class Match {

	private long _id;
	private long homeTeamId;
	private long awayTeamId;
	private int homeTeamScore;
	private int awayTeamScore;
	private String matchDate;
	public static String[] columns = new String[] { Matches._ID, Matches.HOME_TEAM_ID, 
		Matches.AWAY_TEAM_ID, Matches.HOME_TEAM_SCORE, Matches.AWAY_TEAM_SCORE, 
		Matches.MATCH_DATE};
	public static final String AUTHORITY = "org.cesar.fmsl.provider.match";
	
	public Match() {		
	}
	
	public Match(long homeTeamId, long awayTeamId, int homeTeamScore, 
			int awayTeamScore, String matchDate) {
		this.homeTeamId = homeTeamId;
		this.awayTeamId = awayTeamId;
		this.homeTeamScore = homeTeamScore;
		this.awayTeamScore = awayTeamScore;
		this.matchDate = matchDate;
	}
	
	public Match(long _id, long homeTeamId, long awayTeamId, int homeTeamScore, 
			int awayTeamScore, String matchDate) {
		this._id = _id;
		this.homeTeamId = homeTeamId;
		this.awayTeamId = awayTeamId;
		this.homeTeamScore = homeTeamScore;
		this.awayTeamScore = awayTeamScore;
		this.matchDate = matchDate;
	}
	
	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public long getHomeTeamId() {
		return homeTeamId;
	}

	public void setHomeTeamId(long homeTeamId) {
		this.homeTeamId = homeTeamId;
	}

	public long getAwayTeamId() {
		return awayTeamId;
	}

	public void setAwayTeamId(long awayTeamId) {
		this.awayTeamId = awayTeamId;
	}

	public int getHomeTeamScore() {
		return homeTeamScore;
	}

	public void setHomeTeamScore(int homeTeamScore) {
		this.homeTeamScore = homeTeamScore;
	}

	public int getAwayTeamScore() {
		return awayTeamScore;
	}

	public void setAwayTeamScore(int awayTeamScore) {
		this.awayTeamScore = awayTeamScore;
	}

	public String getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(String matchDate) {
		this.matchDate = matchDate;
	}

	public static final class Matches implements BaseColumns {
		public static final Uri CONTENT_URI = 
				Uri.parse("content://" + AUTHORITY + "/matches");
		public static final String CONTENT_TYPE = 
				"vnd.android.cursor.dir/vnd.google.matches";
		public static final String CONTENT_ITEM_TYPE = 
				"vnd.android.cursor.item/vnd.google.matches";
		public static final String DEFAULT_SORT_ORDER = "_id ASC";
		public static final String HOME_TEAM_ID = "homeTeamId";
		public static final String AWAY_TEAM_ID = "awayTeamId";
		public static final String HOME_TEAM_SCORE = "homeTeamScore";
		public static final String AWAY_TEAM_SCORE = "awayTeamScore";
		public static final String MATCH_DATE = "matchDate";
	
		public Uri getUri(long id) {
			Uri uriTeam = ContentUris.withAppendedId(Matches.CONTENT_URI, id);
			return uriTeam;
		}				
	}
	
	@Override
	public String toString() {
		return "Partida: " + _id +
				"\nTime Local: " + homeTeamId +
				"\nTime Visitante: " + awayTeamId +
				"\nPlacar: " + homeTeamScore + "x" + awayTeamScore +
				"\nData: " + matchDate;
				
	}
	
}
