package org.cesar.fmsl.dao.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

	private String[] createScript;
	private String deleteScript;
	
	public SQLiteHelper(Context context, String dbName, int version, 
			String[] createScript, String deleteScript) {
		super(context, dbName, null, version);
		this.createScript = createScript;
		this.deleteScript = deleteScript;		
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("FMSL", "onCreate started. executing createScripts");
		for (int i = 0; i < createScript.length; i++) {
			String sql = createScript[i];
			Log.i("FMSL", sql);
			db.execSQL(sql);
		}
	}

	@Override	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("FMSL", "onUpgrade started. script exec (deleteScript): " + deleteScript);
		db.execSQL(deleteScript);
		onCreate(db);
	}

}
