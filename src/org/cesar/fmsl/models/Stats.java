package org.cesar.fmsl.models;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class Stats {

	private long _id;
	private long matchId;
	private long playerId;
	private int faults;
	private int goals;
	private int goalShoot;
	private int redCard;
	private int yellowCard;
	private int assists;
	private int plays;
	public static final String AUTHORITY = "org.cesar.fmsl.provider.stats";
	public static final String[] columns = new String[] {StatsInner._ID, StatsInner.MATCH_ID,
		StatsInner.PLAYER_ID, StatsInner.FAULTS, StatsInner.GOALS, StatsInner.GOAL_SHOOT, 
		StatsInner.RED_CARD, StatsInner.YELLOW_CARD, StatsInner.ASSISTS, StatsInner.PLAYS};
	
	
	public Stats() {		
	}
	
	public Stats(long matchId, long playerId, int faults, int goals, 
			int goalShoot, int redCard, int yellowCard, int assists, int plays) {
		this.matchId = matchId;
		this.playerId = playerId;
		this.faults = faults;
		this.goals = goals;
		this.goalShoot = goalShoot;
		this.redCard = redCard;
		this.yellowCard = yellowCard;
		this.assists = assists;
		this.plays = plays;
	}
	
	public Stats(long _id, long matchId, long playerId, int faults, int goals, 
			int goalShoot, int redCard, int yellowCard, int assists, int plays) {
		this._id = _id;
		this.matchId = matchId;
		this.playerId = playerId;
		this.faults = faults;
		this.goals = goals;
		this.goalShoot = goalShoot;
		this.redCard = redCard;
		this.yellowCard = yellowCard;
		this.assists = assists;
		this.plays = plays;
	}
	
	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public long getMatchId() {
		return matchId;
	}

	public void setMatchId(long matchId) {
		this.matchId = matchId;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getFaults() {
		return faults;
	}

	public void setFaults(int faults) {
		this.faults = faults;
	}

	public int getGoals() {
		return goals;
	}

	public void setGoals(int goals) {
		this.goals = goals;
	}

	public int getGoalShoot() {
		return goalShoot;
	}

	public void setGoalShoot(int goalShoot) {
		this.goalShoot = goalShoot;
	}

	public int getRedCard() {
		return redCard;
	}

	public void setRedCard(int redCard) {
		this.redCard = redCard;
	}

	public int getYellowCard() {
		return yellowCard;
	}

	public void setYellowCard(int yellowCard) {
		this.yellowCard = yellowCard;
	}

	public int getAssists() {
		return assists;
	}

	public void setAssists(int assists) {
		this.assists = assists;
	}

	public int getPlays() {
		return plays;
	}

	public void setPlays(int plays) {
		this.plays = plays;
	}

	public static final class StatsInner implements BaseColumns {
		public static final Uri CONTENT_URI = 
				Uri.parse("content://" + AUTHORITY + "/stats");
		public static final String CONTENT_TYPE = 
				"vnd.android.cursor.dir/vnd.google.stats";
		public static final String CONTENT_ITEM_TYPE = 
				"vnd.android.cursor.item/vnd.google.statistics";
		public static final String DEFAULT_SORT_ORDER = "_id ASC";
		public static final String MATCH_ID = "matchId";
		public static final String PLAYER_ID = "playerId";
		public static final String FAULTS = "faults";
		public static final String GOALS = "goals";
		public static final String GOAL_SHOOT = "goalShoot";
		public static final String RED_CARD = "redCard";
		public static final String YELLOW_CARD = "yellowCard";
		public static final String ASSISTS = "assists";
		public static final String PLAYS = "plays";
	
		public Uri getUri(long id) {
			Uri uriStats = ContentUris.withAppendedId(StatsInner.CONTENT_URI, id);
			return uriStats;
		}				
	}
}
