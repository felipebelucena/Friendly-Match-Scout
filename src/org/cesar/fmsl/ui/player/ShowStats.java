package org.cesar.fmsl.ui.player;

import java.util.Map;

import org.cesar.fmsl.ui.Main;
import org.cesar.fmsl.utils.Sms;
import org.cesar.lfbl.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ShowStats extends Activity {
	
	private static final int[] textViews = new int[] {R.id.faultsTextView, R.id.goalsTextView, R.id.goalShootTextView,
		R.id.redCardTextView, R.id.yellowCardTextView, R.id.assistsTextView, R.id.playsTextView};
	private static final String[] hashMapKeys = new String[] {"faults", "goals", "goalShoot", "redCard", "yellowCard", 
		"assists", "plays"};
	private Map<String, Integer> statsMap;
	private Long playerId = null;
	private String playerPhone = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.layout_player_stats);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			playerId = extras.getLong("playerId");
			playerPhone = extras.getString("playerPhone");
			Log.i("FMSL", "playerPhone: " + playerPhone);
			if (playerId != null) {
				statsMap = Main.statsRep.getStatsOfAPlayer(playerId);
				fillPlayerStats();
			}	
		}
		
		
		final Button sendStats = (Button) findViewById(R.id.sendStatsBtn);
		sendStats.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				sendSms();				
			}
		});		
		
		
	}
	
	private void fillPlayerStats() {
		for (int i = 0; i < textViews.length; i++) {
			final TextView tv = (TextView) findViewById(textViews[i]);
			tv.setText(statsMap.get(hashMapKeys[i]).toString());
		}		
	}
	
	private void sendSms() {
		Sms sms = new Sms();
		if (sms.sendSms(this, playerPhone, buildMessage())){
			Toast.makeText(this, R.string.sms_send_success, Toast.LENGTH_SHORT).show();
			finish();
		} else {
			Toast.makeText(this, R.string.sms_send_failed, Toast.LENGTH_SHORT).show();
		}
		
	}

	private String buildMessage() {
		//String header = getResources().getString(R.string.sms_header_message);
		StringBuilder sb = new StringBuilder();
		sb.append("F, G, CG, CV, CA, A, P");
		sb.append(" [");
		sb.append(statsMap.get("faults"));
		sb.append(", ");
		sb.append(statsMap.get("goals"));
		sb.append(", ");
		sb.append(statsMap.get("goalShoot"));
		sb.append(", ");
		sb.append(statsMap.get("redCard"));
		sb.append(", ");
		sb.append(statsMap.get("yellowCard"));
		sb.append(", ");
		sb.append(statsMap.get("assists"));
		sb.append(", ");
		sb.append(statsMap.get("plays"));
		sb.append("]");
		Log.i("FMSL", "message= "+ sb.toString());
		return sb.toString();
	}

}
