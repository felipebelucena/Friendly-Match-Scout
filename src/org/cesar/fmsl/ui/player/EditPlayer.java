package org.cesar.fmsl.ui.player;

import java.util.List;

import org.cesar.fmsl.models.Player;
import org.cesar.fmsl.models.Team;
import org.cesar.fmsl.models.Team.Teams;
import org.cesar.fmsl.ui.Main;
import org.cesar.lfbl.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class EditPlayer extends Activity {
	
	static final int RESULT_SAVE = 1;
	static final int RESULT_DELETE = 2;
	private static final int SAVE_PLAYER = 3;
	private static final int DELETE_PLAYER = 4;
	private static final int CANCEL = 5;
	private static final int SHOW_PLAYER_STATS = 6;
	private EditText playerNameField;
	private EditText playerContactField;
	List<Team> teams;
	private Spinner team;
	private Long id;
	private Long teamId;
	private Cursor teamsCursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_edit_player);		
		id = null;
		teamId = null;
		setAllEditTexts();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			id = extras.getLong("player");			
			Log.i("FMSL", "id: " + id);
			if (id != 0) {
				fillEditTexts(id);			
			} else if ( extras.getBoolean("insertByContact")){
				String name = extras.getString("playerName");
				String phone = extras.getString("cellPhone");
				playerNameField.setText(name);
				playerContactField.setText(phone);
			}
		}
	}

	private void setAllEditTexts() {
		playerNameField = (EditText) findViewById(R.id.playerNameField);
		playerContactField = (EditText) findViewById(R.id.playerContactField);
		team = (Spinner) findViewById(R.id.teamFieldSpinner);
		teamsCursor = Main.teamRep.getCursor();
		
		String[] from = new String[] {Teams.NAME};
		int[] to = new int[] {android.R.id.text1};
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, 
				android.R.layout.simple_spinner_item, teamsCursor, from, to);
		adapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		team.setAdapter(adapter);		
		
	}
	
	private void fillEditTexts(Long id) {
		if (id != 0) {
			Player p = Main.playerRep.findPlayer(id);
			Log.i("FMSL", "name: " + p.getName() + "phone: " + p.getCellPhone());
			playerNameField.setText(p.getName());
			playerContactField.setText(p.getCellPhone());	
			
			// sets the right position in the spinner of the player's team
			for (int i = 0; i < team.getCount(); i++) {              
				long itemIdAtPosition = team.getItemIdAtPosition(i);
				if (itemIdAtPosition == p.getTeamId()) {
					team.setSelection(i);
					break;
				}
			}
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, SAVE_PLAYER, 0, R.string.menu_save).setIcon(android.R.drawable.ic_menu_add);
		menu.add(0, DELETE_PLAYER, 0, R.string.menu_delete).setIcon(android.R.drawable.ic_delete);
		menu.add(0, CANCEL, 0, R.string.menu_cancel).setIcon(android.R.drawable.ic_dialog_alert);
		menu.add(0, SHOW_PLAYER_STATS, 0, R.string.menu_stats).setIcon(android.R.drawable.btn_star);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
			case SAVE_PLAYER:
				savePlayer();
				break;
			case DELETE_PLAYER:
				deletePlayer();
				break;
			case CANCEL:
				setResult(RESULT_CANCELED);
				finish();
				break;
			case SHOW_PLAYER_STATS:
				Intent it = new Intent(this, ShowStats.class);
				it.putExtra("playerId", id);
				it.putExtra("playerPhone", playerContactField.getText().toString());
				startActivity(it);
				break;
		}
		return true;
	}
	
	private void deletePlayerStats() {
		// TODO Auto-generated method stub
		
	}

	private void savePlayer() {
		Player p = new Player();
		if (id != null) {
			p.set_id(id);
		}
		if (teamId != null) {
			p.setTeamId(teamId);
		}
		
		p.setCellPhone(playerContactField.getText().toString());
		p.setName(playerNameField.getText().toString());
		Cursor c = (Cursor) team.getSelectedItem();
		long teamId = c.getLong(c.getColumnIndex(Teams._ID));
		p.setTeamId(teamId);
		Main.playerRep.save(p);
		setResult(RESULT_OK);
		finish();		
	}
	
	private void deletePlayer() {
		if (id != null) {
			Main.playerRep.delete(id);			
		}
		setResult(RESULT_OK);
		finish();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		teamsCursor.close();
		super.onDestroy();
	}
}
