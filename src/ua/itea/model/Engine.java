package ua.itea.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.TreeMap;
import java.util.function.Predicate;

import ua.itea.model.Team.Squad;
import ua.itea.model.Team.Squad.Unit;
import ua.itea.model.util.CardinalPoints;
import ua.itea.model.util.MutablePosition;
import ua.itea.model.util.Position;
import ua.itea.model.util.Size;

public class Engine {
	private State state;
	private PathFinder pathFinder;
//	private TurnSequence turnSequence;
//	private Actor actor;
	
	private Random random;
	private ArrayList<Placement<Unit>> base;
	private ArrayList<Placement<Unit>> turnSequence;
	private NearbyPositions nearbyPositions;
	private IsMovementAllowed isMovementAllowed;
	private IsDestination isDestination;
	
	public Engine(State state) {
		this.state = state;
	
		random = new Random(LocalTime.now().toNanoOfDay());
		
		base = state.getUnits();
		turnSequence = new ArrayList<>(base.size());
		
		isMovementAllowed = new IsMovementAllowed();
		isDestination = new IsDestination();
		
		pathFinder = new PathFinder(state.getField().getSize());
		pathFinder.setMovementAllowedPredicate(isMovementAllowed);
		pathFinder.setIsDestinationPredicate(isDestination);
		
		nearbyPositions = new NearbyPositions();
	}
	
	public boolean isOver() {
		return state.numberOfTeams() < 2;
	}
	
	public void iterate() {
		Field<Cell> field = state.getField();
		
		ArrayList<Placement<Unit>> tmp = turnSequence;
		turnSequence = base;
		base = tmp;
		
		Collections.shuffle(turnSequence, random);
		
		for (Placement<Unit> thisUnit : turnSequence) {
			Unit unit = thisUnit.get();
			
			if (isAlive(unit)) {
				Placement<Unit> target = null;
				
				/* try hit */
				target = nearbyTarget(thisUnit.get(), thisUnit.getPosition());
				if (target != null) {
					hit(1.f, thisUnit, target);
				} else {
					/* find path */
					isDestination.set(thisUnit.get());
					ArrayList<CardinalPoints> path = pathFinder.find(thisUnit.getPosition(),
							thisUnit.get().getSquad().getStats().getVelocity());
					
					/* move */
					field.get(thisUnit.getPosition()).setUnit(null);
					for (CardinalPoints direction : path) {
						thisUnit.setDirection(direction);
						thisUnit.getPosition().move(direction);
					}
					field.get(thisUnit.getPosition()).setUnit(thisUnit);
					
					/* try hit */
					target = nearbyTarget(thisUnit.get(), thisUnit.getPosition());
					if (target != null) {
						hit(2.f, thisUnit, target);
					}
				}
			}
		}
		
		for (Placement<Unit> placementUnit : turnSequence) {
			if (isAlive(placementUnit.get())) {
				base.add(placementUnit);
			}
		}
		
		turnSequence.clear();
		state.setUnits(base);
		
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
		 * 		Отправляем юнит в базу. Застрявших добавляем в начало очереди. Начинаем цикл сначала.
		 * В противном случае(путь блокирован):
		 * 		Отправляем юнит к застрявшим.
		 * pool Повторять до тех пор, пока не останутся только застрявшие и за весь цикл никто не сделал шагу.
		 *  
		 * loop
		 * Если путь блокирован союзниками (или другими перемещаемыми/разрушаемыми объектами):
		 * 		Делаем шаг в сторону к одному из этих объектов, от корого путь к какому-либо противнику минимальный.  
		 * pool Повторять до тех пор, пока не останутся только застрявшие и за весь цикл никто не сделал шагу.
		 * */

	}

	private Placement<Unit> nearbyTarget(Unit thisUnit, Position position) {
		Field<Cell> field = state.getField();
		Placement<Unit> target = null;
		
		/* choose random*/
		nearbyPositions.setPosition(position);
		for (CardinalPoints direction : CardinalPoints.randomArray()) {
			Position nearbyPosition = nearbyPositions.getPosition(direction);
			
			if (field.isWithin(nearbyPosition)) {
				Placement<Unit> otherUnit = field.get(nearbyPosition).getUnit();
				
				if (otherUnit != null && isOpponent(thisUnit, otherUnit.get())
						&& isAlive(otherUnit.get())) {
					target = otherUnit;
					break;
				}
			}
		}
		
		return target;
	}

	private void hit(float damageMod, Placement<Unit> thisUnit, Placement<Unit> opponentUnit) {
		CardinalPoints direction = thisUnit.getDirection();
		CardinalPoints opponentDirection = opponentUnit.getDirection();
		float damage;
		float defence;
		float opponentDefenceMod;
		
		if (opponentDirection == direction.oposite()) {
			
			/* Face to face */
			opponentDefenceMod = 1.4f;
			if (Math.random() < 0.5) {
				opponentDefenceMod *= 1.5f;
			}
		} else if (opponentDirection == direction) {
			
			/* Face to back */
			opponentDefenceMod = 0.25f;
		} else {
			
			/* Face to side */
			opponentDefenceMod = 0.5f;
		}
		
		
		damage = thisUnit.get().getSquad().getStats().getDamage() * damageMod;
		if (Math.random() < 0.25) {
			damage *= 1.25;
		}
		
		defence = opponentUnit.get().getSquad().getStats().getDefence() * opponentDefenceMod;
		
		float opponentHealth = opponentUnit.get().getHealth();
		opponentHealth -= Math.max(damage - defence, 0);
		
		opponentUnit.get().setHealth(opponentHealth);
		
		if (!isAlive(opponentUnit.get())) {
			opponentUnit.get().dispose();
			state.getField().get(opponentUnit.getPosition()).setUnit(null);
		}
	}

	private boolean isAlive(Unit unit) {
		return unit.getHealth() > 0;
	}

	public State getState() {
		return state;
	}
	
	private boolean isOpponent(Unit thisUnit, Unit otherUnit) {
		Team thisUnitTeam = thisUnit.getSquad().getTeam();
		Team otherUnitTeam = otherUnit.getSquad().getTeam();
		
		return thisUnitTeam.getId() != otherUnitTeam.getId();
	}
	
	private int getPriority(Placement<Unit> opponent, CardinalPoints direction) {
		CardinalPoints opponentDirection = opponent.getDirection();
		int priority = 0;
		
		if (opponentDirection == direction.oposite()) {
			
			/* Face to face */
			priority = 1;
		} else if (opponentDirection == direction) {
			
			/* Face to back */
			priority = 3;
		} else {
			
			/* Face to side */
			priority = 2;
		}
		
		return priority;
	}
	
	private class IsDestination implements Predicate<Position> {
		private Unit thisUnit;
		
		public void set(Unit thisUnit) {
			this.thisUnit = thisUnit;
		}

		@Override
		public boolean test(Position position) {
			return nearbyTarget(thisUnit, position) != null;
		}
	}
	
	private class IsMovementAllowed implements Predicate<Position> {
		
		@Override
		public boolean test(Position pos) {
			Field<Cell> field = state.getField();
			
			return !field.get(pos).hasUnit();
		}
	}
}
