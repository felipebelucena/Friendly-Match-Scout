package org.cesar.fmsl.models;


public final class DatabaseCreateScripts {
	public static final String[] createScripts = new String[] {
		"CREATE TABLE team (_id integer primary key autoincrement, " +
		"name text not null);",
		
		"CREATE TABLE player (_id integer primary key autoincrement, " +
		"teamId integer, name text not null, cellPhone text not null);",
		
		"CREATE TABLE match (_id integer primary key autoincrement, " +
		"homeTeamId integer not null, awayTeamId integer not null, " +
		"homeTeamScore integer not null, awayTeamScore integer not null, " +
		"matchDate date not null default(date('now')));",
		
		"CREATE TABLE stats(_id integer primary key autoincrement, " +
		"matchId integer not null, playerId integer not null, " +
		"faults integer not null, goals integer not null, goalShoot integer not null, " +
		"redCard integer not null, yellowCard integer not null, " +
		"assists integer not null, plays integer not null);",
		
		"insert into team (name) values ('Time1');",
		"insert into team (name) values ('Time2');",
		"insert into player (teamId, name, cellPhone) values (1, 'Roberto Coracao de Leao', '5556')",
		"insert into player (teamId, name, cellPhone) values (1, 'Ribamar', '5556')",
		"insert into player (teamId, name, cellPhone) values (1, 'Manga', '8567581')",
		"insert into player (teamId, name, cellPhone) values (1, 'Leonardo', '8567581')",
		"insert into player (teamId, name, cellPhone) values (1, 'Jackson', '8567581')",
		"insert into player (teamId, name, cellPhone) values (1, 'Nildo', '8567581')",
		"insert into player (teamId, name, cellPhone) values (1, 'Durval', '8567581')",
		"insert into player (teamId, name, cellPhone) values (2, 'p1', '8567581')",
		"insert into player (teamId, name, cellPhone) values (2, 'p2', '8567581')",
		"insert into player (teamId, name, cellPhone) values (2, 'p3', '8567581')",
		"insert into player (teamId, name, cellPhone) values (2, 'p4', '8567581')",
		"insert into player (teamId, name, cellPhone) values (2, 'p5', '8567581')",
		"insert into player (teamId, name, cellPhone) values (2, 'p6', '8567581')",
		"insert into player (teamId, name, cellPhone) values (2, 'p7', '8567581')"
				
	};
	
	public static final String[] deleteScripts = new String[] {
		"DROP TABLE IF EXISTS player",
		"DROP TABLE IF EXISTS match",
		"DROP TABLE IF EXISTS stats",
		"DROP TABLE IF EXISTS team"
	};
}
