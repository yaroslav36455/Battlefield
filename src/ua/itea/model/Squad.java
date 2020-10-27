package ua.itea.model;

public class Squad extends Formation {	
	private Team team;
	private Stats stats;
	
	public Squad(Team team) {
		super(team);
		setName("Squad #" + getId());
		setFlag(Color.random());
		this.team = team;
		this.stats = new Stats(8, 6, 1);
	}
	
	public Team getTeam() {
		return team;
	}
	
	public Stats getStats() {
		return stats;
	}
}
