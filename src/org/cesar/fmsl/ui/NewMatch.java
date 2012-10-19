package org.cesar.fmsl.ui;

import java.util.List;

import org.cesar.fmsl.models.Team;
import org.cesar.fmsl.models.Team.Teams;
import org.cesar.fmsl.ui.player.ListPlayer;
import org.cesar.fmsl.ui.team.ListTeam;
import org.cesar.fmsl.utils.NumberPicker;
import org.cesar.lfbl.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class NewMatch extends Activity implements OnClickListener {

	private List<Team> listHome;
	private static Spinner homeTeamSpinner;
	private static Spinner awayTeamSpinner;
	private static final int MANAGE_TEAMS_MENU = 1;
	private static final int MANAGE_PLAYERS_MENU = 2;
	private final int MILLISECONDS_IN_ONE_MINUTE = 60 * 1000;
	private static final int INSERT_EDIT_TEAM = 3;
	private static final int START_MATCH = 4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_new_match);
		setAllButtonsListeners();
		setAllSpinners();
	}

	private void setAllSpinners() {
		listHome = Main.teamRep.listTeams();	
		if (listHome.isEmpty()) {
			Toast.makeText(this, R.string.no_team_toast_message, Toast.LENGTH_SHORT)
					.show();
			setStartButtonVisibility(View.INVISIBLE);
		} else {			
			//TeamAdapter adapter = new TeamAdapter(this, listHome, true);	
			
			// setting the spinner for home team
			Cursor c = Main.teamRep.getCursor();
			String[] from = new String[] {Teams.NAME};
			int[] to = new int[] {android.R.id.text1};
			SimpleCursorAdapter homeAdapter = new SimpleCursorAdapter(this, 
					android.R.layout.simple_spinner_item, c, from, to);
			homeAdapter.setDropDownViewResource(
					android.R.layout.simple_spinner_dropdown_item);
			homeTeamSpinner = (Spinner) findViewById(R.id.homeTeamSpinner);
			homeTeamSpinner.setAdapter(homeAdapter);		
			
			// setting the spinner for away team
			Cursor c2 = Main.teamRep.getCursor();
			SimpleCursorAdapter awayAdapter = new SimpleCursorAdapter(this, 
					android.R.layout.simple_spinner_item, c2, from, to);
			awayAdapter.setDropDownViewResource(
					android.R.layout.simple_spinner_dropdown_item);
			awayTeamSpinner = (Spinner) findViewById(R.id.awayTeamSpinner);
			awayTeamSpinner.setAdapter(awayAdapter);					
		}
	}

	private void setAllButtonsListeners() {
		final Button startBtn = (Button) findViewById(R.id.startBtn);
		startBtn.setOnClickListener(this);
	}

	private void setStartButtonVisibility(int visibility) {
		Button startBtn = (Button) findViewById(R.id.startBtn);
		startBtn.setVisibility(visibility);
	}
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.startBtn:				
				//long t1Id = homeTeamSpinner.getSelectedItemId();
				//long t2Id = awayTeamSpinner.getSelectedItemId();
				Cursor c = (Cursor) homeTeamSpinner.getSelectedItem();
				long t1Id = c.getLong(c.getColumnIndex(Teams._ID));
				Cursor c2 = (Cursor) awayTeamSpinner.getSelectedItem();
				long t2Id = c2.getLong(c2.getColumnIndex(Teams._ID));
				NumberPicker matchTime = (NumberPicker) findViewById(R.id.matchTime);
				
				if (bothTeamsCanPlay(t1Id, t2Id)) {
					Intent it = new Intent(this, MatchActivity.class);
					it.putExtra("homeTeamId", t1Id);
					it.putExtra("awayTeamId", t2Id);
					it.putExtra("matchTime", (matchTime.getValue() * MILLISECONDS_IN_ONE_MINUTE));
					startActivityForResult(it, START_MATCH);
				} else {					
					Toast.makeText(this, R.string.invalid_team_toast_message, 
							Toast.LENGTH_LONG).show();
				}				
				break;
		}		
	}
	
	/* checks if both teams can play the match (if they're valid 
	   different teams and with seven players each.
	*/
	private boolean bothTeamsCanPlay(long t1Id, long t2Id) {
		boolean ret = false;
		if ((t1Id > 0 && t2Id > 0) && t1Id != t2Id) {
			int n1 = Main.playerRep.numberOfPlayersOfATeam(t1Id);
			int n2 = Main.playerRep.numberOfPlayersOfATeam(t2Id);
			if (n1 == 7 && n2 == 7)
				ret = true;
		}
		return ret;
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MANAGE_TEAMS_MENU, 0, R.string.menu_manageTeams);
		menu.add(0, MANAGE_PLAYERS_MENU, 0, R.string.menu_managePlayers);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
			case MANAGE_TEAMS_MENU:
				Intent it = new Intent(this, ListTeam.class);
				startActivityForResult(it, INSERT_EDIT_TEAM);
				break;
			case MANAGE_PLAYERS_MENU:
				startActivity(new Intent(this, ListPlayer.class));
		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == START_MATCH && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			Intent it = new Intent(this, EndMatch.class);
			it.putExtra("matchId", extras.getLong("matchId"));
			startActivity(it);
		} else if (requestCode == INSERT_EDIT_TEAM && resultCode == RESULT_OK) {
			// updating the team's spinners...
			setAllSpinners();
		}
	}
}
