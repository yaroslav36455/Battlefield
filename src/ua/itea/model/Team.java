package ua.itea.model;

public class Team extends Formation {
	
	public Team(int id) {
		super(id);
		setName("Team #" + getId());
		setFlag(Color.random());
	}
}
