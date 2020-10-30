package ua.itea.model;

import java.util.ArrayList;

import ua.itea.model.Team.Squad.Unit;
import ua.itea.model.util.CardinalPoints;
import ua.itea.model.util.Position;

public class Actor {
	private Placement<Unit> thisUnit;
	private NearbyPositions nearbyPositions;
	private PathFinder pathFinder;

	public void set(Placement<Unit> placementUnit) {
		this.thisUnit = placementUnit;
		nearbyPositions = new NearbyPositions();
	}

	public Target findTarget(Field<Cell> field) {
		Target target = findNearbyTarget(field);

		return target != null ? target : findRemoteTarget(field);
	}

	public Target findNearbyTarget(Field<Cell> field) {
		int priority = 0;
		Placement<Unit> priorityOpponent = null;
		
		for (CardinalPoints direction : CardinalPoints.values()) {
			Position position = nearbyPositions.getPosition(direction);
			
			if (field.isWithin(position)) {
				Placement<Unit> otherUnit = field.get(position).getUnit();
				
				if (otherUnit != null && isOpponent(otherUnit)) {
					int currentPriority = getPriority(otherUnit, direction);
					
					if (currentPriority > priority) {
						priority = currentPriority;
						priorityOpponent = otherUnit;
					}
				}
			}
		}
		
		return new Target(priorityOpponent);
	}
	
	private boolean isOpponent(Placement<Unit> otherUnit) {
		Team thisUnitTeam = thisUnit.get().getSquad().getTeam();
		Team otherUnitTeam = otherUnit.get().getSquad().getTeam();
		
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
	
	public Target findRemoteTarget(Field<Cell> field) {
		ArrayList<CardinalPoints> path = pathFinder.find(thisUnit.getPosition(),
														 thisUnit.get().getSquad().getStats().getVelocity());
		return null;
	}
}
