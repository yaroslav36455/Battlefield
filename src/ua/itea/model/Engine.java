package ua.itea.model;

import java.util.ArrayList;
import java.util.function.Predicate;

import ua.itea.model.Team.Squad.Unit;
import ua.itea.model.util.CardinalPoints;
import ua.itea.model.util.Position;

public class Engine {
	private State state;
	
	private ArrayList<Team> teams;
	private TurnSequence turnSequence;
	
	private PathFinder pathFinder;
	private NearbyPositions nearbyPositions;
	private IsMovementAllowed isMovementAllowed;
	private IsDestination isDestination;
	
	public Engine(State state) {
		this.state = state;
		this.teams = state.getTeams();
		turnSequence = new TurnSequence(teams);
		
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
		boolean iterated = false;
		
		for (Unit thisUnit : turnSequence) {
			if (isAlive(thisUnit)) {
				Unit target = null;
				
				/* try hit */
				target = nearbyTarget(thisUnit, thisUnit.getPlacement().getPosition());
				if (target != null) {
					hit(1.f, thisUnit, target);
					iterated = true;
				} else {
					/* find path */
					isDestination.setClient(thisUnit);
					ArrayList<CardinalPoints> path = pathFinder.find(thisUnit.getPlacement().getPosition(),
							thisUnit.getSquad().getStats().getVelocity());
					
					/* move */
					field.get(thisUnit.getPlacement().getPosition()).setUnit(null);
					for (CardinalPoints direction : path) {
						thisUnit.getPlacement().setDirection(direction);
						thisUnit.getPlacement().getPosition().move(direction);
						iterated = true;
					}
					field.get(thisUnit.getPlacement().getPosition()).setUnit(thisUnit);
					
					/* try hit */
					target = nearbyTarget(thisUnit, thisUnit.getPlacement().getPosition());
					if (target != null) {
						hit(2.f, thisUnit, target);
					}
				}
			}
		}
		
		if(iterated) {
			state.iterate();
		}
	}

	private Unit nearbyTarget(Unit thisUnit, Position position) {
		Field<Cell> field = state.getField();
		Unit target = null;
		
		/* choose random*/
		nearbyPositions.setPosition(position);
		for (CardinalPoints direction : CardinalPoints.randomArray()) {
			Position nearbyPosition = nearbyPositions.getPosition(direction);
			
			if (field.isWithin(nearbyPosition)) {
				Unit otherUnit = field.get(nearbyPosition).getUnit();
				
				if (otherUnit != null && isOpponent(thisUnit, otherUnit)) {
					target = otherUnit;
					break;
				}
			}
		}
		
		return target;
	}

	private void hit(float damageMod, Unit thisUnit, Unit opponentUnit) {
		CardinalPoints direction = thisUnit.getPlacement().getDirection();
		CardinalPoints opponentDirection = opponentUnit.getPlacement().getDirection();
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
		
		
		damage = thisUnit.getSquad().getStats().getDamage() * damageMod;
		if (Math.random() < 0.25) {
			damage *= 1.25;
		}
		
		defence = opponentUnit.getSquad().getStats().getDefence() * opponentDefenceMod;
		
		float opponentHealth = opponentUnit.getHealth();
		opponentHealth -= Math.max(damage - defence, 0);
		
		opponentUnit.setHealth(opponentHealth);
		
		if (!isAlive(opponentUnit)) {
			opponentUnit.dispose();
			state.getField().get(opponentUnit.getPlacement().getPosition()).setUnit(null);
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
	
	private int getPriority(Unit opponent, CardinalPoints direction) {
		CardinalPoints opponentDirection = opponent.getPlacement().getDirection();
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
		
		public void setClient(Unit thisUnit) {
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
