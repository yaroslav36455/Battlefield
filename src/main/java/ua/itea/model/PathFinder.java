package ua.itea.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;

import ua.itea.model.util.CardinalPoints;
import ua.itea.model.util.MutablePosition;
import ua.itea.model.util.Position;
import ua.itea.model.util.Size;

public class PathFinder {
	private DisplacementField field;
	private Front front;
	private ArrayList<Position> satisfied;
	private ArrayList<CardinalPoints> path;
	private Predicate<Position> isDestination;
	private Function<Position[], Integer> chooseDestination;
	
	public PathFinder(Size size) {
		field = new DisplacementField(size);
		front = new Front();
		
		satisfied = new ArrayList<>();
		path = new ArrayList<>();
	}
	
	public void setMovementAllowedPredicate(Predicate<Position> movementAllowed) {
		field.setMovementAllowedPredicate(movementAllowed);
	}
	
	public void setIsDestinationPredicate(Predicate<Position> isDestination) {
		this.isDestination = isDestination;
	}
	
	public void setChooseDestinationFunction(Function<Position[], Integer> chooseDestination) {
		this.chooseDestination = chooseDestination;
	}
	
	public ArrayList<CardinalPoints> find(Position start, int maxPathLengh) {
		LocalTime begin = LocalTime.now();
		
		path.clear();
		satisfied.clear();
		
		field.refresh();
		front.refresh(start);
		
		do {
			front.expand();
			
			for (Position position : front.getArray()) {
				if (isDestination.test(position)) {
					satisfied.add(field.get(position).getPosition());
				}
			}
			
		} while(satisfied.isEmpty() && front.isExists());
		
		
		if (!satisfied.isEmpty()) {
			updatePath(start, maxPathLengh);
		}
		
//		System.out.println(begin + " - " + LocalTime.now());
		return path;
	}
	
	private void updatePath(Position start, int pathLength) {
		
		// TODO сделать специальный функциональный класс для выбора цели
		Position s = satisfied.get((int)(Math.random() * satisfied.size()));
		MutablePosition position = new MutablePosition(s.getX(), s.getY());
		
		CardinalPoints direction = field.get(position).getDirection();
		CardinalPoints oposite;
		
		while (!position.equalTo(start)) {
//			System.out.println(start + " | " + position);
//			try {
//				Thread.sleep(200);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			oposite = direction.oposite();
			
			position.move(direction);
			direction = field.get(position).getDirection();
			
			field.get(position).setDirection(oposite);
		}
		
		CardinalPoints dir;
		for(int i = 0; i < pathLength && !position.equalTo(s); i++) {
//			System.out.println(s + " | " + position);
//			try {
//				Thread.sleep(200);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			dir = field.get(position).getDirection();
			
			path.add(dir);
			position.move(dir);
		}
	}

	private CardinalPoints[] getCardinalPoints() {
		/* first */
		return CardinalPoints.randomArray();
		
		/* second */
//		ArrayList<CardinalPoints> source = new ArrayList<>();
//		CardinalPoints[] arr = new CardinalPoints[4];
//		
//		for (CardinalPoints cardinalPoints : CardinalPoints.values()) {
//			source.add(cardinalPoints);
//		}
//		
//		int destIndex = 0;
//		while(!source.isEmpty()) {
//			int sourceIndex = (int) (Math.random() * source.size());
//			
//			arr[destIndex++] = source.get(sourceIndex);
//			source.remove(sourceIndex);
//		}
//		
//		return arr;
		
		/* third (default) */
//		return CardinalPoints.values();
	}

	private class Front {
		private NearbyPositions neighbours;
		private ArrayList<Position> front;
		private ArrayList<Position> newFront;
		
		public Front() {
			neighbours = new NearbyPositions();
			front = new ArrayList<>();
			newFront = new ArrayList<>();
		}
		
		public void refresh(Position start) {
			front.clear();
			
			front.add(start);
			field.visit(start);
		}
		
		public void expand() {
			
			for (Position position : front) {
				neighbours.setPosition(position);
				
				for (CardinalPoints direction : getCardinalPoints()) {
					Position neighbour = neighbours.getPosition(direction);
					
					if (field.isMovementAllowed(neighbour)) {
						DisplacementCell displacementCell = field.get(neighbour);
						Position cellPos = displacementCell.getPosition();
						
						displacementCell.setDirection(direction.oposite());
						field.visit(cellPos);
						
						newFront.add(cellPos);
					}
				}
			}
			
			ArrayList<Position> tmp = front;
			front = newFront;
			newFront = tmp;
			
			newFront.clear();
		}
		
		public ArrayList<Position> getArray() {
			return front;
		}
		
		public boolean isExists() {
			return !front.isEmpty();
		}
	}
}
