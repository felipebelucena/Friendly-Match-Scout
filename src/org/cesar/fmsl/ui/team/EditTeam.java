package org.cesar.fmsl.ui.team;

import org.cesar.fmsl.models.Team;
import org.cesar.fmsl.ui.Main;
import org.cesar.lfbl.R;

import android.app.Activity;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class EditTeam extends Activity {

	static final int RESULT_SAVE = 1;
	static final int RESULT_DELETE = 2;
	private static final int SAVE_TEAM = 3;
	private static final int CANCEL = 4;
	private static final int DELETE_TEAM = 5;
	private EditText teamNameField;
	private Long id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_edit_team);
		teamNameField = (EditText) findViewById(R.id.teamNameField);
		id = null;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			id = extras.getLong(BaseColumns._ID);
			
			if (id != null) {
				Team team = ListTeam.teamRep.findTeam(id);
				teamNameField.setText(team.getName());	
			}			
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, SAVE_TEAM, 0, R.string.menu_save).setIcon(android.R.drawable.ic_menu_add);
		menu.add(0, DELETE_TEAM, 0, R.string.menu_delete).setIcon(android.R.drawable.ic_delete);
		menu.add(0, CANCEL, 0, R.string.menu_cancel).setIcon(android.R.drawable.ic_dialog_alert);
		
		return true;
	}
	
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
			case SAVE_TEAM:
				saveTeam();
				break;
			case DELETE_TEAM:
				deleteTeam();
				break;
			case CANCEL:
				setResult(RESULT_CANCELED);
				finish();
				break;
		}
		return true;	
	}
	
	
	private void saveTeam() {
		Team t = new Team();
		if (id != null) {
			t.set_id(id);			
		}		
		t.setName(teamNameField.getText().toString());
		ListTeam.teamRep.save(t);
		setResult(RESULT_OK);
		finish();		
	}
	
	private void deleteTeam() {
		if (id != null) {
			Main.teamRep.delete(id);		
			Main.playerRep.deleteAllPlayersOfAteam(id);
		}
		setResult(RESULT_OK);
		finish();
	}	
}
