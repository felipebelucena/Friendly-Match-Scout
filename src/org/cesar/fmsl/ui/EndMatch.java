package org.cesar.fmsl.ui;

import org.cesar.fmsl.models.Match;
import org.cesar.fmsl.models.Team;
import org.cesar.lfbl.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class EndMatch extends Activity {
	private Match match;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_match_result);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			long matchId = extras.getLong("matchId");
			match = Main.matchRep.findMatch(matchId);
			Log.i("FMSL", "match <endMatch>: " + match);
			if (match != null) {
				fillMatchInfo();
				//setNewMatchButton();
			} else {
				Toast.makeText(this, R.string.match_info_not_found, 
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}
	
	private void fillMatchInfo() {
		Team team = Main.teamRep.findTeam(match.getHomeTeamId());
		TextView homeTeam = (TextView) findViewById(R.id.homeTeamTV);
		homeTeam.setText(team.getName());
		
		team = Main.teamRep.findTeam(match.getAwayTeamId());
		
		TextView awayTeam = (TextView) findViewById(R.id.awayTeamTV);
		awayTeam.setText(team.getName());
		TextView homeScore = (TextView) findViewById(R.id.homeScoreTV);		
		homeScore.setText(String.valueOf(match.getHomeTeamScore()));		
		TextView awayScore = (TextView) findViewById(R.id.awayScoreTV);
		awayScore.setText(String.valueOf(match.getAwayTeamScore()));		
	}
	
//	private void setNewMatchButton() {
//		final Button button = (Button) findViewById(R.id.btNewMatch);
//		button.setOnClickListener(new OnClickListener() {			
//			public void onClick(View v) {
//				startActivity(new Intent(EndMatch.this,NewMatch.class));				
//			}
//		});
//	}
	
}
