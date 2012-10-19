package org.cesar.fmsl.ui.player;

import java.util.List;

import org.cesar.fmsl.models.Player;
import org.cesar.fmsl.models.PlayerRepository;
import org.cesar.fmsl.ui.Main;
import org.cesar.lfbl.R;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class ListPlayer extends ListActivity {

	public static PlayerRepository playerRep;
	private static final int INSERT_EDIT = 1;
	private static final int INSERT_FROM_CONTACTS = 2;
	private List<Player> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//playerRep = new PlayerRepository(this);
		updatePlayerList();
	}
	
	private void updatePlayerList() {
		list = Main.playerRep.listPlayers(null, null, null, null, null);
		setListAdapter(new PlayerAdapter(this, list, true));
		if (list.isEmpty()) {
			Toast.makeText(this, R.string.no_player_toast_message,
					Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_EDIT, 0, R.string.menu_insert_new).setIcon(R.drawable.novo);
		menu.add(0, INSERT_FROM_CONTACTS, 0, R.string.menu_insert_new_contact).setIcon(R.drawable.novo);
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			Uri uri = data.getData();
			Cursor c = managedQuery(uri, null, null, null, null);
			if (c != null){
				int idIndex =  c.getColumnIndex(ContactsContract.Contacts._ID);				
				int nameIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
				//int hasPhoneIndex = c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER); 
				if(c.moveToNext()) {						
					String playerName = c.getString(nameIndex);
					String cell = "";
					//String hasPhone = c.getString(hasPhoneIndex);
					String id = c.getString(idIndex);				
					
					Uri phoneUri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, id);
					Cursor phoneCursor = managedQuery(phoneUri, null, null, null, null);
				
					if (phoneCursor != null) {
						int cellIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
						if(phoneCursor.moveToFirst()) {
							cell = phoneCursor.getString(cellIndex);
							Log.i("FMSL", "cell: " + cell);
						}					
					}
					Intent it = new Intent(this, EditPlayer.class);
					it.putExtra("insertByContact", true);
					it.putExtra("playerName", playerName);
					it.putExtra("cellPhone", cell);
					Log.i("FMSL", "ListPlayer passing to EditPlayer >> name: " + playerName + " cell: " + cell);
					startActivityForResult(it, INSERT_EDIT);				
					
				}			
			}			
		}
		if (resultCode == RESULT_OK) {
			updatePlayerList();
		}
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case INSERT_EDIT:
				startActivityForResult(new Intent(this, EditPlayer.class), INSERT_EDIT);
				break;
			case INSERT_FROM_CONTACTS:
				Uri uri = Uri.parse("content://com.android.contacts/contacts/");
				Intent it = new Intent(Intent.ACTION_PICK, uri);
				startActivityForResult(it, INSERT_FROM_CONTACTS);
		}
		return true;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		editPlayer(position);
	}
	
	private void editPlayer(int position) {
		Player p = list.get(position);
		Intent it = new Intent(this, EditPlayer.class);
		it.putExtra("player", p.get_id());
		startActivityForResult(it, INSERT_EDIT);
	}
	
	
}
