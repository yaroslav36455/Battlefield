package ua.itea.model;

public class DogTag {
//	private int teamId;
//	private int squadId;
//	
//	public DogTag(int teamId, int squadId) {
//		this.teamId = teamId;
//		this.squadId = squadId;
//	}
//	
//	public int getTeamId() {
//		return teamId;
//	}
//	
//	public int getSquadId() {
//		return squadId;
//	}
	
	private Team team;
	private Squad squad;
	
	public DogTag(Team team, Squad squad) {
		this.team = team;
		this.squad = squad;
	}

	public Team getTeam() {
		return team;
	}

	public Squad getSquad() {
		return squad;
	}
}
