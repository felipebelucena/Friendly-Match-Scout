package org.cesar.fmsl.models;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class Team {

	private long _id;
	private String name;
	public static String[] columns = new String[] {Teams._ID, Teams.NAME };
	public static final String AUTHORITY = "org.cesar.fmsl.provider.team";
	public static final int DEFAULT_PLAYERS_NUMBER = 7;
	
	public Team() {		
	}
	
	public Team(String name) {
		this.name = name;
	}

	public Team(long _id, String name) {
		this._id = _id;
		this.name = name;
	}
	
	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		if (_id > 0)
			this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name != null && !name.trim().equals(""))
			this.name = name;
	}
	
	// classe interna para se usar no Content Provider.
	public static final class Teams implements BaseColumns {
		public static final Uri CONTENT_URI = 
				Uri.parse("content://" + AUTHORITY + "/teams");
		public static final String CONTENT_TYPE = 
				"vnd.android.cursor.dir/vnd.google.teams";
		public static final String CONTENT_ITEM_TYPE = 
				"vnd.android.cursor.item/vnd.google.teams";
		public static final String DEFAULT_SORT_ORDER = "_id ASC";
		public static final String NAME = "name";
	
		public Uri getUri(long id) {
			Uri uriTeam = ContentUris.withAppendedId(Teams.CONTENT_URI, id);
			return uriTeam;
		}				
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Name: " + name;
	}
}
