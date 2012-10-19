package org.cesar.fmsl.models;

import java.util.ArrayList;
import java.util.List;

import org.cesar.fmsl.models.Match.Matches;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class MatchRepository {

	protected SQLiteDatabase db;
	private SQLiteHelper dbHelper;
	private static final String TABLE_NAME = "match";
	private static final String DATABASE_NAME = "fmsl";
	private static final int DATABASE_VERSION = 1;
	private static final String[] createScript = DatabaseCreateScripts.createScripts;
	private static final String deleteScript = "DROP TABLE IF EXISTS " + TABLE_NAME;
	
	public MatchRepository(Context ctx) {
		dbHelper = new SQLiteHelper(ctx, MatchRepository.DATABASE_NAME, 
				MatchRepository.DATABASE_VERSION, MatchRepository.createScript, 
				MatchRepository.deleteScript);
		db = dbHelper.getWritableDatabase();
	}
	
	public long save(Match match) {
		long matchId = match.get_id();
		if (matchId != 0) {
			return update(match);
		} else {
			return insert(match);
		}
	}
	
	public long insert(Match match) {
		ContentValues values = new ContentValues();		
		values.put("homeTeamId", match.getHomeTeamId());
		values.put("awayTeamId", match.getAwayTeamId());
		values.put("homeTeamScore", match.getHomeTeamScore());
		values.put("awayTeamScore", match.getAwayTeamScore());
		values.put("matchDate", match.getMatchDate());
		return db.insert(TABLE_NAME, null,values);
	}
	
	public long update(Match match) {
		ContentValues values = new ContentValues();
		values.put("homeTeamId", match.getHomeTeamId());
		values.put("awayTeamId", match.getAwayTeamId());
		values.put("homeTeamScore", match.getHomeTeamScore());
		values.put("awayTeamScore", match.getAwayTeamScore());
		values.put("matchDate", match.getMatchDate().toString());
		return db.update(TABLE_NAME, values, "_id = ?", 
				new String[] { String.valueOf(match.get_id()) });
	}
	
	public int delete(long id) {
		return db.delete(TABLE_NAME, "_id = ?", 
				new String[] {String.valueOf(id)});
	}
	
	public Cursor getCursor(String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy) {
		try {
			return db.query(TABLE_NAME, Match.columns, selection, selectionArgs, 
					groupBy, having, orderBy);
			
		} catch (SQLException e) {
			Log.e("FMSL", "Error while getting a cursor for Matches: " + e.toString());
			return null;
		}
	}
	
	public List<Match> listMatches() {
		Cursor c = getCursor(null, null, null, null, null);
		List<Match> matches = new ArrayList<Match>();
		if (c.moveToFirst()) {
			do {				
				matches.add(fillMatch(c));				
			} while (c.moveToNext());
		}
		return matches;
	}
	
	public Match findMatch(long id) {
		Match match = null;
		Cursor c = db.query(true, TABLE_NAME, Match.columns, "_id=?", 
				new String[] {String.valueOf(id)}, null, null, null, null);
		if (c != null && c.moveToFirst()) {
			match = fillMatch(c);
			c.close();
		}		
		return match;
	}
		
	private Match fillMatch(Cursor c) {
		Match match = new Match();
		match.set_id(c.getLong(c.getColumnIndex(Matches._ID)));
		match.setHomeTeamId(c.getLong(c.getColumnIndex(Matches.HOME_TEAM_ID)));
		match.setAwayTeamId(c.getLong(c.getColumnIndex(Matches.AWAY_TEAM_ID)));
		match.setHomeTeamScore(c.getInt(c.getColumnIndex(Matches.HOME_TEAM_SCORE)));
		match.setAwayTeamScore(c.getInt(c.getColumnIndex(Matches.AWAY_TEAM_SCORE)));
		match.setMatchDate(c.getString(c.getColumnIndex(Matches.MATCH_DATE)));
		
		return match;
	}
	
	public void close() {
		if (db != null)
			db.close();
		
		if (dbHelper != null)
			dbHelper.close();
	}
}
