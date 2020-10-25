package ua.itea.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import ua.itea.model.util.CardinalPoints;
import ua.itea.model.util.Position;
import ua.itea.model.util.Size;

public class Engine {
	private State state;
	private Random rand;
	private ArrayList<SelectedCell> base;
	private ArrayList<SelectedCell> waiting;
	private ArrayList<SelectedCell> stuck;
	
	public Engine(State state) {
		this.state = state;
	}
	
	public boolean isOver() {
		return state.numberOfTeams() < 2;
	}
	
	public void iterate() {
		/*
		 * Team id
		 * Squad id, damage, defence
		 * Field cell position
		 * Unit full
		 */
		
		/*
		 * loop
		 * Если есть рядом противник:
		 * 		Бьём противника. Приоритет - повёрнутые спиной/боком и с низким здоровьем.
		 * В противном случае:
		 * 		Поиск пути к ближайшему противнику и шаг в его сторону.
		 * 			Если сделали шаг и теперь рядом противник - сразу бьём его повышеным уроном.
		 * Если был выполнен ход:
		 * 		Отправляем в базу. Застрявших добавляем в начало очереди. Начинаем цикл сначала.
		 * В противном случае(путь блокирован):
		 * 		Отправляем к застрявшим.
		 * pool Повторять до тех пор, пока не останутся только застрявшие и за весь цикл никто не сделал шагу.
		 *  
		 * loop
		 * Если путь блокирован союзниками (или другими перемещаемыми/разрушаемыми объектами):
		 * 		Делаем шаг в сторону к одному из этих объектов, от корого путь к какому-либо противнику минимальный.  
		 * pool Повторять до тех пор, пока не останутся только застрявшие и за весь цикл никто не сделал шагу.
		 * */
		
		ArrayList<SelectedCell> tmp = base;
		base = waiting;
		waiting = tmp;

		base.clear();
		
		rand.setSeed(LocalTime.now().toNanoOfDay());
		Collections.shuffle(waiting, rand);
		
		boolean anyoneCanMakeATurn;
		do {
			anyoneCanMakeATurn = false;
			
			for (int i = 0; i < waiting.size(); i++) {
				if (turn(waiting.get(i))) {
					anyoneCanMakeATurn = true;
					base.add(waiting.get(i));
					waiting.remove(i);
					break;
				}
			}
		} while(anyoneCanMakeATurn);
		
		for (SelectedCell selectedCell : waiting) {
			base.add(selectedCell);
		}
		waiting.clear();
	}

	private boolean turn(SelectedCell selectedCellUnit) {
		Field<Cell> field = state.getField();
		
		Neighbours neighbours = new Neighbours();
		neighbours.setPosition(selectedCellUnit.getPosition());
		
		Unit currentUnit = selectedCellUnit.getCell().getUnit();
		DogTag currentUnitDogTag = currentUnit.getDogTag();
		Team currentUnitTeam = currentUnitDogTag.getTeam();
		
		for (CardinalPoints direction : CardinalPoints.values()) {
			Position pos = neighbours.getNeighbour(direction);
			Cell cell = field.get(pos);
			
			if (!cell.isFree()) {
				Unit otherUnit = cell.getUnit();
				DogTag otherUnitDogTag = otherUnit.getDogTag();
				Team otherUnitTeam = otherUnitDogTag.getTeam();
				
				if (currentUnitTeam.equals(otherUnitTeam)) {
					
				}
			}
		}
		
		return false;
	}
	
	private ArrayList<Cell> nearbyOpponents(Unit unit, Position position) {
		ArrayList<Cell> opponents = new ArrayList<>();
		Field<Cell> field = state.getField();
		Neighbours neighbours = new Neighbours();
		
		neighbours.setPosition(position);
		
		DogTag currentUnitDogTag = unit.getDogTag();
		Team currentUnitTeam = currentUnitDogTag.getTeam();
		
		for (CardinalPoints direction : CardinalPoints.values()) {
			Position pos = neighbours.getNeighbour(direction);
			Cell cell = field.get(pos);
			
			if (!cell.isFree()) {
				Unit otherUnit = cell.getUnit();
				DogTag otherUnitDogTag = otherUnit.getDogTag();
				Team otherUnitTeam = otherUnitDogTag.getTeam();
				
				if (currentUnitTeam.equals(otherUnitTeam)) {
					opponents.add(cell);
				}
			}
		}
		
		return opponents;
	}

	public State getState() {
		return state;
	}
}
