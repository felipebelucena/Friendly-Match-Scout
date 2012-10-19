package org.cesar.fmsl.ui;

import java.util.List;

import org.cesar.fmsl.models.Player;
import org.cesar.lfbl.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MatchAdapter extends BaseAdapter {
	
	private List<Player> playersList;
	private Context context;
	
	public MatchAdapter(Context context, List<Player> playersList) {
		this.context = context;
		this.playersList = playersList;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return playersList.size();
	}

	public Object getItem(int arg0) {
		return playersList.get(arg0);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return playersList.get(position).get_id();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Player p = playersList.get(position);
		
		LayoutInflater inflater = 
				(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(android.R.layout.simple_list_item_1, null);
		
		final TextView playerName = (TextView) view.findViewById(android.R.id.text1);
		playerName.setText(p.getName());
		return view;
	}

}
