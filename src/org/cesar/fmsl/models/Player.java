package org.cesar.fmsl.models;

import java.io.Serializable;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class Player implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1342776918647024878L;
	private long _id;
	private long teamId;
	private String name;
	private String cellPhone;
	public static String[] columns = new String[] {
		Players._ID, Players.TEAM_ID, Players.NAME, Players.CELL_PHONE };
	public static final String AUTHORITY = "org.cesar.fmsl.provider.player";
	
	public Player() {
		
	}
	
	public Player(long teamId, String name, String cellPhone) {
		this.setTeamId(teamId);
		this.name = name;
		this.cellPhone = cellPhone;
	}
	
	public Player(long _id, long teamId, String name, String cellPhone) {
		this._id = _id;
		this.teamId = teamId;
		this.name = name;
		this.cellPhone = cellPhone;
	}
	
	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public long getTeamId() {
		return teamId;
	}

	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}

	public static final class Players implements BaseColumns {
		public static final Uri CONTENT_URI = 
				Uri.parse("content://" + AUTHORITY + "/players");
		public static final String CONTENT_TYPE = 
				"vnd.android.cursor.dir/vnd.google.players";
		public static final String CONTENT_ITEM_TYPE = 
				"vnd.android.cursor.item/vnd.google.players";
		public static final String DEFAULT_SORT_ORDER = "name ASC";
		public static final String TEAM_ID = "teamId";
		public static final String NAME = "name";
		public static final String CELL_PHONE = "cellPhone";
	
		public Uri getUri(long id) {
			Uri uriPlayer = ContentUris.withAppendedId(Players.CONTENT_URI, id);
			return uriPlayer;
		}				
	}
}
