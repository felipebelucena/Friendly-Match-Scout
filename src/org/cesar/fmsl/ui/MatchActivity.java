package org.cesar.fmsl.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cesar.fmsl.models.Match;
import org.cesar.fmsl.models.Player;
import org.cesar.fmsl.models.Stats;
import org.cesar.fmsl.ui.player.PlayerAdapter;
import org.cesar.fmsl.utils.NumberPicker;
import org.cesar.lfbl.R;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

public class MatchActivity extends TabActivity implements OnTabChangeListener, OnItemClickListener{

	private TabHost tabHost;
	private LinearLayout matchLayout;
	private List<Player> homePlayersList;
	private List<Player> awayPlayersList;
	public int homeScore = 0;
	public int awayScore = 0;
	private long homeTeamId;
	private long awayTeamId;
	private long matchId;
	private AlertDialog.Builder statsAlertDialog;
	private NumberPicker faults;
	private NumberPicker goals;
	private NumberPicker goalShoot;
	private NumberPicker redCard;
	private NumberPicker yellowCard;
	private NumberPicker assists;
	private Map<Long, Map<String, Integer>> stats;
	public static int matchState;
	private static final int MATCH_STARTED = 1;
	private static final int MATCH_FINNISHED = 2;
	private static final int MATCH_PAUSED = 3;
	private static final int MATCH_TAB_ID = 1;
	private static final int AWAY_TEAM_TAB_ID = 2;
	private Button endMatch;
	private long matchTime;
	private TextView timeTextView;
	private Handler handler= new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tabHost = getTabHost();
		tabHost.setOnTabChangedListener(this);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			homeTeamId = extras.getLong("homeTeamId");
			awayTeamId = extras.getLong("awayTeamId");
			matchTime = extras.getInt("matchTime");
			Log.i("FMSL", "matchTime before create handler thread" + matchTime);
			setTeamPlayersList();
		}
		
		LayoutInflater.from(this).inflate(R.layout.layout_match, tabHost.getTabContentView(), true);
		timeTextView = (TextView) findViewById(R.id.timeTextView);
		createTabHomeTeam();		
		createTabPartida();		
		createTabAwayTeam();
		tabHost.setCurrentTab(AWAY_TEAM_TAB_ID);
		tabHost.setCurrentTab(MATCH_TAB_ID);
		matchState = MATCH_STARTED;
		startChronometer();
	}

	private void startChronometer() {
		new Chronometer().start();		
	}

	private void createTabAwayTeam() {
		String label = getResources().getString(R.string.tab_label_away_team);
		tabHost.addTab(tabHost.newTabSpec(label).setIndicator(label).setContent(new TabContentFactory() {
			
			public View createTabContent(String tag) {
				// TODO Auto-generated method stub
				ListView awayPlayersListView = (ListView) findViewById(R.id.awayPlayersListView);
				awayPlayersListView.setAdapter(new PlayerAdapter(
						MatchActivity.this, awayPlayersList, false));
				awayPlayersListView.setOnItemClickListener(MatchActivity.this);
				return awayPlayersListView;
			}
		}));
	}

	private void createTabPartida() {
		String label = getResources().getString(R.string.tab_label_match);
		tabHost.addTab(tabHost.newTabSpec(label).setIndicator(label).setContent(new TabContentFactory() {
			
			public View createTabContent(String tag) {
				matchLayout = (LinearLayout) findViewById(R.id.abaPartida);			
				endMatch = (Button) matchLayout.findViewById(R.id.endMatchButton);
				endMatch.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						endMatch();						
					}
				});				
				return matchLayout;
			}
		}));
	}
	
	private void createTabHomeTeam() {
		String label = getResources().getString(R.string.tab_label_home_team);
		tabHost.addTab(tabHost.newTabSpec(label).setIndicator(label)
				.setContent(new TabContentFactory() {
					public View createTabContent(String tag) {
						ListView homePlayersListView = (ListView) findViewById(R.id.homePlayersListView);
						homePlayersListView.setAdapter(new PlayerAdapter(
								MatchActivity.this, homePlayersList, false));
						homePlayersListView
								.setOnItemClickListener(MatchActivity.this);
						return homePlayersListView;
					}
				}));
	}
	
	private void endMatch() {
		matchState = MATCH_FINNISHED;
		savingPlayersStats();
		endMatch.setClickable(false);
		Intent it = new Intent();
		it.putExtra("matchId", matchId);
		//it.putExtra("matchId", matchId);
		//startActivity(it);
		setResult(RESULT_OK, it);
		finish();
	}
	
	private void savingPlayersStats() {
		Match match = new Match(homeTeamId, awayTeamId, homeScore, awayScore, new Date().toString());
		matchId = Main.matchRep.save(match);
		Log.i("FMSL", "matchId=" + matchId);
		
		for (Entry<Long, Map<String, Integer>> map : stats.entrySet()) {
			Stats s = new Stats();
			s.setPlayerId(map.getKey());
			Map<String, Integer> values = map.getValue();
			s.setFaults(values.get("faults"));
			s.setGoals(values.get("goals"));
			s.setAssists(values.get("assists"));
			s.setGoalShoot(values.get("goalShoot"));
			s.setRedCard(values.get("redCard"));
			s.setYellowCard(values.get("yellowCard"));
			s.setMatchId(matchId);
			Main.statsRep.save(s);
		}
	}
	
	private void setNumberPickersAtributes(View view) {
		faults 		= (NumberPicker) view.findViewById(R.id.faultsNP);
		goals 		= (NumberPicker) view.findViewById(R.id.goalsNP);
		goalShoot 	= (NumberPicker) view.findViewById(R.id.goalShootNP);
		redCard 	= (NumberPicker) view.findViewById(R.id.redCardNP);
		yellowCard	= (NumberPicker) view.findViewById(R.id.yellowCardNP);
		assists		= (NumberPicker) view.findViewById(R.id.assistsNP);
	}
	
	private void setTeamPlayersList() {
		homePlayersList = Main.playerRep.listPlayers("teamId=?", new String[] { String.valueOf(homeTeamId)}, null, null, "name");
		awayPlayersList = Main.playerRep.listPlayers("teamId=?", new String[] {String.valueOf(awayTeamId)}, null, null, "name");		
		stats = new HashMap<Long, Map<String,Integer>>();
		createStatsMapForListPlayers(homePlayersList);		
		createStatsMapForListPlayers(awayPlayersList);
	}
	

	private void createStatsMapForListPlayers(List<Player> playerList) {
		for (Player player : playerList) {
			Map<String, Integer> hm = new HashMap<String, Integer>();
			hm.put("faults", 0);
			hm.put("goals", 0);
			hm.put("goalShoot", 0);
			hm.put("redCard", 0);
			hm.put("yellowCard", 0);
			hm.put("assists", 0);			
			stats.put(player.get_id(), hm);
		}
	}

	public void onTabChanged(String tabId) {
		
	}

	

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		final Player player = (Player) arg0.getItemAtPosition(arg2);
		
		if (matchState == MATCH_STARTED) {
			LayoutInflater inflater = LayoutInflater.from(this);
			View view = inflater.inflate(R.layout.custom_dialog_stats, null);
			setNumberPickersAtributes(view);
			setNumberPickerValues(player);
			statsAlertDialog = new AlertDialog.Builder(this);
			statsAlertDialog.setTitle(R.string.label_stats).setView(view).create();
			statsAlertDialog.setPositiveButton(R.string.alert_dialog_ok, new OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					saveNumberPickerValues(player);

				}
			});
			statsAlertDialog.setNegativeButton(R.string.alert_dialog_cancel, null);
			statsAlertDialog.create().show();
		}
		
	}

	private void setNumberPickerValues(Player player) {
		Map<String, Integer> hm = stats.get(player.get_id());
		faults.setValue(hm.get("faults"));
		goals.setValue(hm.get("goals"));
		goalShoot.setValue(hm.get("goalShoot"));
		redCard.setValue(hm.get("redCard"));
		yellowCard.setValue(hm.get("yellowCard"));
		assists.setValue(hm.get("assists"));
	}
	
	private void saveNumberPickerValues(Player player) {
		Map<String, Integer> hm = stats.get(player.get_id());
		hm.put("faults", faults.getValue());
		int previousScore = hm.get("goals");  
		int atualScore = goals.getValue();
		hm.put("goals", goals.getValue());		
		hm.put("goalShoot", goalShoot.getValue());
		hm.put("redCard", redCard.getValue());
		hm.put("yellowCard", yellowCard.getValue());
		hm.put("assists", assists.getValue());
		stats.put(player.get_id(), hm);
		atualizeScore(atualScore - previousScore, player.getTeamId());
		
	}
	
	private void atualizeScore(int score, long teamId) {
		if (teamId == homeTeamId) {
			homeScore += score;
		} else if (teamId == awayTeamId) {
			awayScore += score;
		}
		final TextView tv = (TextView) matchLayout.findViewById(R.id.matchScore);
		tv.setText("[ Local " + homeScore + "x" + awayScore + " Visitante ]");
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle(R.string.back_alert_dialog_title);
		ad.setMessage(R.string.back_key_pressed);
		ad.setPositiveButton(R.string.alert_dialog_ok, new OnClickListener() {			
			public void onClick(DialogInterface dialog, int which) {
				matchState = MATCH_FINNISHED;
				finish();
			}
		});
		ad.setNegativeButton(R.string.alert_dialog_cancel, null);
		ad.create().show();		
	}	
	
	public class Chronometer extends Thread {
		long elapsedTime;
		final DateFormat df = new SimpleDateFormat("mm':'ss");
		long startTime = System.currentTimeMillis();
		public void run() {			
			while(matchState == MATCH_STARTED && 
					(elapsedTime =  System.currentTimeMillis() - startTime) <= matchTime){
				handler.post(new Runnable() {						
					public void run() {							
						timeTextView.setText(df.format(new Date(elapsedTime)));
						Log.i("FMSL", df.format(new Date(elapsedTime)));
					}
				});
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(matchState == MATCH_STARTED)
				endMatch();
		}
	}
}
