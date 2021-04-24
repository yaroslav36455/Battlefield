package ua.itea.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import ua.itea.model.Team.Squad.Unit;

public class TurnSequence implements Iterable<Unit> {
	private ArrayList<Team> teams;
	private ArrayList<Unit> units;
	private Random random;

	public TurnSequence(ArrayList<Team> teams) {
		this.teams = teams;
		units = new ArrayList<>();
		random = new Random(LocalTime.now().toNanoOfDay());
	}
	
	@Override
	public Iterator<Unit> iterator() {		
		return new TurnIterator();
	}

	private class TurnIterator implements Iterator<Unit> {
		private Iterator<Unit> arrayIterator;
		
		public TurnIterator() {
			units.clear();		
			for (Team team : teams) {
				for (Team.Squad squad : team) {
					for (Unit unit : squad) {
						units.add(unit);
					}
				}
			}
			Collections.shuffle(units, random);
			
			arrayIterator = units.iterator();
		}
		
		@Override
		public boolean hasNext() {
			return arrayIterator.hasNext();
		}

		@Override
		public Unit next() {
			return arrayIterator.next();
		}	
	}
	
}
