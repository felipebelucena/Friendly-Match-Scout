package org.cesar.fmsl.dao.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cesar.fmsl.dao.IStatisticsDAO;
import org.cesar.fmsl.models.Stats;
import org.cesar.fmsl.models.Stats.StatsInner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class StatisticsDAOSQL implements IStatisticsDAO {

	protected SQLiteDatabase db;
	private SQLiteHelper dbHelper;
	private static final String TABLE_NAME = "stats";
	private static final String DATABASE_NAME = "fmsl";
	private static final int DATABASE_VERSION = 1;
	private static final String[] createScript = DatabaseCreateScripts.createScripts ;
	private static final String deleteScript = "DROP TABLE IF EXISTS " + TABLE_NAME;

	public StatisticsDAOSQL(Context ctx) {
		dbHelper = new SQLiteHelper(ctx, StatisticsDAOSQL.DATABASE_NAME, 
				StatisticsDAOSQL.DATABASE_VERSION, StatisticsDAOSQL.createScript, 
				StatisticsDAOSQL.deleteScript);
		db = dbHelper.getWritableDatabase();
	}
	
	public long save(Stats stats) {
		long statsId = stats.get_id();
		if (statsId != 0) {
			return update(stats);
		} else {
			return insert(stats);
		}
	}
	
	public long insert(Stats stats) {
		ContentValues values = new ContentValues();
		values.put("matchId", stats.getMatchId());
		values.put("playerId", stats.getPlayerId());
		values.put("faults", stats.getFaults());
		values.put("goals", stats.getGoals());
		values.put("goalShoot", stats.getGoalShoot());
		values.put("redCard", stats.getRedCard());
		values.put("yellowCard", stats.getYellowCard());
		values.put("assists", stats.getAssists());
		values.put("plays", stats.getPlays());
		return db.insert(TABLE_NAME, null, values);
	}
	
	public long update(Stats stats) {
		ContentValues values = new ContentValues();
		values.put("matchId", stats.getMatchId());
		values.put("playerId", stats.getPlayerId());
		values.put("faults", stats.getFaults());
		values.put("goals", stats.getGoals());
		values.put("goalShoot", stats.getGoalShoot());
		values.put("redCard", stats.getRedCard());
		values.put("yellowCard", stats.getYellowCard());
		values.put("assists", stats.getAssists());
		values.put("plays", stats.getPlays());
		return db.update(TABLE_NAME, values, "_id = ?", 
				new String[] { String.valueOf(stats.get_id()) });
	}	
	public int delete(long id) {
		return db.delete(TABLE_NAME, "_id = ?", 
				new String[] {String.valueOf(id)});
	}
	
	public Cursor getCursor(boolean distinct, String selection, String[] selectionArgs, String groupBy, 
			String having, String orderBy, String limit) {
		try {
			return db.query(distinct, TABLE_NAME, Stats.columns, selection, selectionArgs, 
					groupBy, having, orderBy, limit);
		} catch (SQLException e) {
			Log.e("FMSL", "Error while finding Stats: " + e.toString());
			return null;
		}
	}
	
	
	public List<Stats> listStats() {
		Cursor c = getCursor(true, null, null, null, null, null, null);
		List<Stats> allStats = new ArrayList<Stats>();
		if (c.moveToFirst()) {
			do {				
				allStats.add(fillStats(c));				
			} while (c.moveToNext());
		}
		c.close();
		return allStats;
	}
		
	private Stats fillStats(Cursor c) {
		Stats stats = new Stats();
		stats.set_id(c.getLong(c.getColumnIndex(BaseColumns._ID)));
		stats.setMatchId(c.getLong(c.getColumnIndex(StatsInner.MATCH_ID)));
		stats.setPlayerId(c.getLong(c.getColumnIndex(StatsInner.PLAYER_ID)));
		stats.setFaults(c.getInt(c.getColumnIndex(StatsInner.FAULTS)));
		stats.setGoals(c.getInt(c.getColumnIndex(StatsInner.GOALS)));
		stats.setGoalShoot(c.getInt(c.getInt(c.getColumnIndex(StatsInner.GOAL_SHOOT))));
		stats.setRedCard(c.getInt(c.getColumnIndex(StatsInner.RED_CARD)));
		stats.setYellowCard(c.getInt(c.getColumnIndex(StatsInner.YELLOW_CARD)));
		stats.setAssists(c.getInt(c.getColumnIndex(StatsInner.ASSISTS)));
		stats.setPlays(c.getInt(c.getColumnIndex(StatsInner.PLAYS)));
		
		return stats;
	}
	
	public Map<String, Integer> getStatsOfAPlayer(long playerId) {
		Map<String, Integer> hashMap = new HashMap<String, Integer>();
		int faults, goals, goalShoot, redCard , yellowCard, assists, plays;
		faults = goals = goalShoot = redCard = yellowCard = assists = plays = 0;
		Cursor c = getCursor(false, "playerId=?", new String[] {String.valueOf(playerId)}, null, 
				null, null, null); 
		if(c.moveToFirst()) {
			do {
				faults 		+= c.getInt(c.getColumnIndex(StatsInner.FAULTS));
				goals 		+= c.getInt(c.getColumnIndex(StatsInner.GOALS));
				goalShoot 	+= c.getInt(c.getColumnIndex(StatsInner.GOAL_SHOOT));
				redCard 	+= c.getInt(c.getColumnIndex(StatsInner.RED_CARD));
				yellowCard	+= c.getInt(c.getColumnIndex(StatsInner.YELLOW_CARD));
				assists		+= c.getInt(c.getColumnIndex(StatsInner.ASSISTS));
				plays 		+= 1;				
			} while(c.moveToNext());
		}
		hashMap.put("faults", faults);
		hashMap.put("goals", goals);
		hashMap.put("goalShoot", goalShoot);
		hashMap.put("redCard", redCard);
		hashMap.put("yellowCard", yellowCard);
		hashMap.put("assists", assists);
		hashMap.put("plays", plays);
		c.close();
		return hashMap;
		
	}
	
	public void close() {
		if (db != null)
			db.close();
		
		if (dbHelper != null)
			dbHelper.close();
	}
}

