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
		
		public void setStats(Stats stats) {
			this.stats = stats;
		}
		
		public void disposeAllUnits() {
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
		
		public boolean equalTo(Squad squad) {
			return Team.this.equalTo(squad.getTeam())
					&& getId() == squad.getId();
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
					return units.get(index++);
				}
				
			};
		}
		
		public class Unit {
			private int id;
			private float health;
			private Placement placement;
			
			public Unit(Placement placement) {
				this(getStats().getHealth(), placement);
			}
			
			public Unit(float health, Placement placement) {
				this.id = Squad.this.generateId();
				this.health = health;
				this.placement = placement;
				
				add();
			}
			
			public int getId() {
				return id;
			}
			
			public Placement getPlacement() {
				return placement;
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
			
			public Unit copy(Placement placement) {
				return new Unit(health, placement);
			}
			
			public void dispose() {
				units.set(this.getId(), null);
				size--;
			}
			
			private void add() {
				units.add(this);
				size++;
			}
			
			public boolean equalTo(Unit unit) {
				return Squad.this.equalTo(unit.getSquad())
						&& getId() == unit.getId();
			}
		}
	}
}
