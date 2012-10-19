package org.cesar.fmsl.ui.team;

import java.util.List;

import org.cesar.fmsl.models.Team;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TeamAdapter extends BaseAdapter {

	private Context context;
	private List<Team> list;
	boolean isSpinner = false;
	
	public TeamAdapter(Context context, List<Team> list, boolean isSpinner) {
		this.context = context;
		this.list = list;
		/* this adapter will be used in a list view and an adapter.
		 so, the layout have to change. The value of isSpinner sets
		 the layout to spinner if true, otherwise, to list view.
		*/
		this.isSpinner = isSpinner;
	}
	public int getCount() {
		return list.size();
	}

	public Object getItem(int location) {
		return list.get(location);
	}

	public long getItemId(int arg0) {
		return list.get(arg0).get_id();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Team team = list.get(position);
		LayoutInflater inflater = 
				(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view;
		if (isSpinner) {
			view = inflater.inflate(
					android.R.layout.simple_spinner_item, null);
		} else {
			view = inflater.inflate(
					android.R.layout.simple_list_item_1, null);
		}
		
		TextView name = (TextView) view.findViewById(android.R.id.text1);
		name.setText(team.getName());
		
		return view;
	}
		

}
