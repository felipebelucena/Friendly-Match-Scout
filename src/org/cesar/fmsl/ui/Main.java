	package org.cesar.fmsl.ui;

import org.cesar.fmsl.dao.sql.MatchDAOSQL;
import org.cesar.fmsl.dao.sql.PlayerDAOSQL;
import org.cesar.fmsl.dao.sql.StatisticsDAOSQL;
import org.cesar.fmsl.dao.sql.TeamDAOSQL;
import org.cesar.fmsl.ui.player.ListPlayer;
import org.cesar.fmsl.ui.team.ListTeam;
import org.cesar.lfbl.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class Main extends Activity implements OnClickListener{
    
	public static TeamDAOSQL teamRep;
	public static PlayerDAOSQL playerRep;
	public static MatchDAOSQL matchRep;
	public static StatisticsDAOSQL statsRep;
	private CheckBox cb;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setAllButtons();
        initializeReps();        
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final Editor editor = prefs.edit(); 
        
		if (prefs.getBoolean("fmslDialog", true)) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			alertDialog.setTitle(R.string.main_alert_dialog_title);
			alertDialog.setMessage(R.string.fmsl_message);
			cb = new CheckBox(this);
			cb.setText(R.string.checkbox_text);
			alertDialog.setView(cb);
			alertDialog.setPositiveButton(R.string.alert_dialog_ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {							
							editor.putBoolean("fmslDialog", !cb.isChecked());
							editor.commit();
						}
					});
			alertDialog.setNegativeButton(R.string.alert_dialog_cancel, null);
			alertDialog.create().show();
		}       
    }

	private void setAllButtons() {
        final Button manageTeamsBtn = (Button) findViewById(R.id.manageTeamsBtn);
        manageTeamsBtn.setOnClickListener(this);
        final Button managePlayersBtn = (Button) findViewById(R.id.managePlayersBtn);
        managePlayersBtn.setOnClickListener(this);
		final Button newMatchBtn = (Button) findViewById(R.id.newMatchBtn);
        newMatchBtn.setOnClickListener(this);
	}

	private void initializeReps() {
		playerRep	= new PlayerDAOSQL(this);
		matchRep 	= new MatchDAOSQL(this);
		teamRep 	= new TeamDAOSQL(this);
		statsRep 	= new StatisticsDAOSQL(this);
	}
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.newMatchBtn:
				startActivity(new Intent(this, NewMatch.class));
				break;
			case R.id.manageTeamsBtn:
				startActivity(new Intent(this, ListTeam.class));
				break;
			case R.id.managePlayersBtn:
				if (hasTeams()) {
					startActivity(new Intent(this, ListPlayer.class));
				} else {
					Toast.makeText(this, R.string.no_team_toast_message, 
							Toast.LENGTH_SHORT).show();
				}
				break;
		}		
	}
	
	// if the getCount() method of cursor returns a value greater than zero
	// then, a team was already recorded in the database.
	private boolean hasTeams() {
		return teamRep.hasTeams();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		teamRep.close();
		playerRep.close();
		statsRep.close();
		matchRep.close();
	}
}