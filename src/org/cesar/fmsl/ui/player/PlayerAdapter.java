package org.cesar.fmsl.ui.player;

import java.util.List;

import org.cesar.fmsl.models.Player;
import org.cesar.fmsl.models.Team;
import org.cesar.fmsl.ui.Main;
import org.cesar.lfbl.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PlayerAdapter extends BaseAdapter {

	private Context context;
	private List<Player> list;
	private boolean completeInfo;
	
	public PlayerAdapter(Context ctx, List<Player> list, boolean completeInfo) {
		this.context = ctx;
		this.list = list;
		// if completeInfo is true, the player will be list with more info(name, team, phone)
		// if false, just the name will be showed.
		this.completeInfo = completeInfo;
	}
	public int getCount() {
		return list.size();
	}

	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	public long getItemId(int position) {
		return list.get(position).get_id();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		Player p = list.get(position);

		LayoutInflater inflater = 
				(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view;
		if (completeInfo) {
			view = inflater.inflate(R.layout.list_player, null);
			TextView name = (TextView) view.findViewById(R.id.playerName);
			name.setText(p.getName());			
			Team team = Main.teamRep.findTeam(p.getTeamId());
			TextView teamName = (TextView) view.findViewById(R.id.teamNameField);
			teamName.setText(team.getName());
			TextView contact = (TextView) view.findViewById(R.id.playerContact);
			contact.setText(p.getCellPhone());
		} else {
			view = inflater.inflate(android.R.layout.simple_list_item_1, null);
			TextView name = (TextView) view.findViewById(android.R.id.text1);
			name.setText(p.getName());
			name.setGravity(Gravity.CENTER);
		}
		
		return view;
	}

}
