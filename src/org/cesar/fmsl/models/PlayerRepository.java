package org.cesar.fmsl.models;

import java.util.ArrayList;
import java.util.List;

import org.cesar.fmsl.models.Player.Players;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class PlayerRepository {

	protected SQLiteDatabase db;
	private SQLiteHelper dbHelper;
	private static final String TABLE_NAME = "player";
	private static final String DATABASE_NAME = "fmsl";
	private static final int DATABASE_VERSION = 1;
	private static final String[] createScript = DatabaseCreateScripts.createScripts;
	private static final String deleteScript = "DROP TABLE IF EXISTS " + TABLE_NAME;
	
	
	public PlayerRepository(Context ctx) {
		dbHelper = new SQLiteHelper(ctx, PlayerRepository.DATABASE_NAME, 
				PlayerRepository.DATABASE_VERSION, PlayerRepository.createScript, 
				PlayerRepository.deleteScript);
		db = dbHelper.getWritableDatabase();
	}
	
	public long save(Player player) {
		long id = player.get_id();
		if (id != 0) {
			return update(player);
		} else {
			return insert(player);
		}
	}
	public long insert(Player player) {
		ContentValues values = new ContentValues();
		values.put("TeamId", player.getTeamId());
		values.put("name", player.getName());
		values.put("cellPhone", player.getCellPhone());
		return db.insert(TABLE_NAME, null, values);
	}
	
	public long update(Player player) {
		ContentValues values = new ContentValues();
		values.put("TeamId", player.getTeamId());
		values.put("name", player.getName());
		values.put("cellPhone", player.getCellPhone());
		return db.update(TABLE_NAME, values, "_id = ?", 
				new String[] { String.valueOf(player.get_id()) });
	}
	
	public int delete(long id) {
		return db.delete(TABLE_NAME, "_id = ?", 
				new String[] {String.valueOf(id)});
	}
	
	public Cursor getCursor(String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy) {
		try {
			return db.query(TABLE_NAME, Player.columns, selection, selectionArgs, 
					groupBy, having, orderBy);
			
		} catch (SQLException e) {
			Log.e("FMSL", "Error while getting a cursor for Players: " + e.toString());
			return null;
		}
	}	
	
	public List<Player> listPlayers(String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy) {
		Cursor c = getCursor(selection, selectionArgs, groupBy, having, orderBy);
		List<Player> players = new ArrayList<Player>();
		if (c.moveToFirst()) {
			do {				
				players.add(fillPlayer(c));				
			} while (c.moveToNext());
		}
		c.close();
		return players;
	}
	
	public List<Player> findPlayerByName(String name) {
		List<Player> list = new ArrayList<Player>();
		String[] args = new String[] { name + "%"};
		Cursor c = db.query(TABLE_NAME, Player.columns, "name like ?", args, 
				null, null, "name");
		c.moveToFirst();
		while(!c.isAfterLast()) {
			Player player = fillPlayer(c);
			list.add(player);
			c.moveToNext();
		}
		c.close();
		return list;
	}
	
	public Player findPlayer(long id) {
		Cursor c = db.query(true, TABLE_NAME, Player.columns, BaseColumns._ID + "=" + id, 
				null, null, null, null, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			Player player = fillPlayer(c);
			c.close();
			return player;			
		}
		c.close();
		return null;
	}
	
	private Player fillPlayer(Cursor c) {
		Player player = new Player();
		player.set_id(c.getLong(c.getColumnIndex(BaseColumns._ID)));
		player.setTeamId(c.getLong(c.getColumnIndex(Players.TEAM_ID)));
		player.setName(c.getString(c.getColumnIndex(Players.NAME)));
		player.setCellPhone(c.getString(c.getColumnIndex(Players.CELL_PHONE)));
		
		return player;
	}
	
	public int numberOfPlayersOfATeam(long teamId) {
		if (teamId > 0) {
			Cursor c = getCursor("teamId = ?", new String[] { String.valueOf(teamId) }, 
				null, null, null);
			int count = c.getCount();
			c.close();
			return count;
		}
		return 0;
	}
	
	public int deleteAllPlayersOfAteam(long teamId) {
		return db.delete(TABLE_NAME, "teamId=?", 
				new String[] {String.valueOf(teamId)});
	}
	
	public void close() {
		if (db != null)
			db.close();
		
		if (dbHelper != null)
			dbHelper.close();
	}
	
}
