package org.cesar.fmsl.ui.team;

import java.util.List;

import org.cesar.fmsl.models.Team;
import org.cesar.fmsl.models.TeamRepository;
import org.cesar.lfbl.R;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class ListTeam extends ListActivity {

	public static TeamRepository teamRep;
	private static final int INSERT_EDIT = 1;
	
	
	private List<Team> list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		teamRep = new TeamRepository(this);
		updateTeamList();
		
	}

	private void updateTeamList() {
		list = teamRep.listTeams();  
		setListAdapter(new TeamAdapter(this, list, false));
		if (list.isEmpty()) {
			Toast.makeText(this, R.string.no_team_toast_message,Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		teamRep.close();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_EDIT, 0, R.string.menu_insert_new).setIcon(R.drawable.novo);
		return true;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		editTeam(position);
	}
	
	protected void editTeam(int position) {
		Team team = list.get(position);
		Intent it = new Intent(this, EditTeam.class);
		it.putExtra(BaseColumns._ID, team.get_id());
		startActivityForResult(it, INSERT_EDIT);
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			updateTeamList();
		}
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case INSERT_EDIT:
				startActivityForResult(new Intent(this, EditTeam.class), INSERT_EDIT);
				break;
		}
		return true;
	}
}