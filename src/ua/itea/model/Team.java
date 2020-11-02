package ua.itea.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Predicate;

public class Team extends Formation implements Iterable<Team.Squad> {
	private static IdGenerator teamsIdGenerator = new IdGenerator();
	private ArrayList<Squad> squads;
	
	public Team() {
		super(teamsIdGenerator.getNewId());
		setName("Team #" + getId());
		setColor(new Color((int) (Math.random() * Integer.MAX_VALUE)));
		
		squads = new ArrayList<>();
	}
	
	@Override
	public int getSize() {
		return squads.size();
	}
	
	public void addSquad(Squad squad) {
		squads.add(squad);
	}
	
	public boolean removeSquad(Squad squad) {
		
		return squads.removeIf(new Predicate<Squad>() {

			@Override
			public boolean test(Squad t) {
				return getId() == t.getTeam().getId()
						&& t.getId() == squad.getId();
			}
		});
	}
	
	public void removeAllSquads() {
		squads.clear();
	}

	@Override
	public Iterator<Squad> iterator() {
		return squads.iterator();
	}
	
	public class Squad extends Formation implements Iterable<Squad.Unit> {
		private Stats stats;
		private ArrayList<Unit> units;
		private int size;
		
		public Squad() {
			super(Team.this.generateId());
			setName("Squad #" + getId());
			setColor(new Color((int) (Math.random() * Integer.MAX_VALUE)));
			this.stats = new Stats(100, 8, 6, 1);
			this.units = new ArrayList<>();
			this.size = 0;
			
			Team.this.squads.add(this);
		}
		
		public Team getTeam() {
			return Team.this;
		}
		
		public Stats getStats() {
			return stats;
		}
		
		public void removeAllUnits() {
			for (int i = 0; i < units.size(); i++) {
				units.set(i, null);
			}
			
			size = 0;
		}

		@Override
		public int getSize() {
			return size;
		}
		
		public int getTotal() {
			return units.size();
		}

		@Override
		public Iterator<Unit> iterator() {
			
			return new Iterator<Unit>() {
				private int index = 0;

				@Override
				public boolean hasNext() {
					while(index < getTotal()) {
						if (units.get(index) != null) {
							return true;
						}
						index++;
					}
					
					return false;
				}

				@Override
				public Unit next() {
					return units.get(index);
				}
				
			};
		}
		
		public class Unit {
			private int id;
			private float health;
			
			public Unit(float health) {
				this.id = Squad.this.generateId();
				this.health = health;
				
				add();
			}
			
			public int getId() {
				return id;
			}
			
			public Squad getSquad() {
				return Squad.this;
			}
			
			public float getHealth() {
				return health;
			}

			public void setHealth(float health) {
				this.health = health;
			}
			
			public Unit copy() {
				return new Unit(health);
			}
			
			public void dispose() {
				units.set(this.getId(), null);
				size--;
			}
			
			private void add() {
				units.add(this);
				size++;
			}
		}
	}
}
