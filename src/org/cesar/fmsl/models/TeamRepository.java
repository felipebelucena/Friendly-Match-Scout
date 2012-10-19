package org.cesar.fmsl.models;

import java.util.ArrayList;
import java.util.List;

import org.cesar.fmsl.models.Team.Teams;
import org.cesar.fmsl.ui.Main;
import org.cesar.lfbl.R;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class TeamRepository {

	protected SQLiteDatabase db;
	private SQLiteHelper dbHelper;
	private static final String TABLE_NAME = "team";
	private static final String DATABASE_NAME = "fmsl";
	private static final int DATABASE_VERSION = 1;
	private static final String[] createScript = DatabaseCreateScripts.createScripts;
	private static final String deleteScript = "DROP TABLE IF EXISTS " + TABLE_NAME;
	
	
	public TeamRepository(Context ctx) {
		dbHelper = new SQLiteHelper(ctx, TeamRepository.DATABASE_NAME, 
				TeamRepository.DATABASE_VERSION, TeamRepository.createScript, 
				TeamRepository.deleteScript);
		db = dbHelper.getWritableDatabase();
	}
	
	public long save(Team team) {
		long id = team.get_id();
		if (id != 0) {
			update(team);
		} else {
			// new team
			insert(team);
		}
		return id;
	}
	
	public long insert(Team team) {
		ContentValues values = new ContentValues();
		values.put("name", team.getName());
		return db.insert(TABLE_NAME, null, values);
	}
	
	public long update(Team team) {
		ContentValues values = new ContentValues();
		values.put("name", team.getName());
		return db.update(TABLE_NAME, values, "_id = ?", 
				new String[] { String.valueOf(team.get_id()) });
	}
	
	public int delete(long id) {
		// deleting all players
		Main.playerRep.deleteAllPlayersOfAteam(id);
		return db.delete(TABLE_NAME, "_id = ?", 
				new String[] {String.valueOf(id)});
	}
	
	public Cursor getCursor() {
		try {
			return db.query(TABLE_NAME, Team.columns, null, null, null, 
					null, null);
			
		} catch (SQLException e) {
			Log.e("FMSL", "Error while finding Teams: " + e.toString());
			return null;
		}
	}
	
	public List<Team> listTeams() {
		Cursor c = getCursor();
		List<Team> teams = new ArrayList<Team>();
		if (c.moveToFirst()) {
			int idxId = c.getColumnIndex(BaseColumns._ID);
			int idxName = c.getColumnIndex(Teams.NAME);
			do {
				Team team = new Team();
				team.set_id(c.getLong(idxId));
				team.setName(c.getString(idxName));
				teams.add(team);
			} while (c.moveToNext());
		}
		c.close();
		return teams;
	}
	
	public List<String> listTeamArray() {
		List<Team> list = listTeams();
		List<String> teamListArray = new ArrayList<String>();
		for (Team team : list) {
			teamListArray.add(team.getName());		
		}
		return teamListArray;
	}
	public List<Team> findTeamByName(String name) {
		List<Team> list = new ArrayList<Team>();
		String[] columns = new String[] {"_id", "name"};
		String[] args = new String[] { name + "%"};
		Cursor c = db.query(TABLE_NAME, columns, "name like ?", args, 
				null, null, "name");
		c.moveToFirst();
		while(!c.isAfterLast()) {
			Team team = fillTeam(c);
			list.add(team);
			c.moveToNext();
		}
		c.close();
		return list;
	}
	
	private Team fillTeam(Cursor c) {
		Team team = new Team();
		team.set_id(c.getLong(c.getColumnIndex(Teams._ID)));
		team.setName(c.getString(c.getColumnIndex(Teams.NAME)));
		
		return team;
	}
	
	public Team findTeam(long id) {
		Team team = null;
		Cursor c = db.query(true, TABLE_NAME, Team.columns, BaseColumns._ID + "=" + id, 
				null, null, null, null, null);
		if (c.moveToFirst()) {
			team = fillTeam(c);	
		}
		c.close();
		return team;
	}
	
	public void close() {
		if (db != null)
			db.close();
		
		if (dbHelper != null)
			dbHelper.close();
	}
	
	public boolean hasTeams() {
		Cursor c = getCursor();
		boolean ret = c.getCount() > 0;
		c.close();
		return ret;		
	}
}
