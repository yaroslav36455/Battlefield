package ua.itea.model;

import ua.itea.model.util.CardinalPoints;

public class ImpactDirection {
	

	public class Target {
		private Placement<Unit> unit;
		private CardinalPoints direction;
		
		public Target(Placement<Unit> unit, CardinalPoints direction) {
			this.unit = unit;
			this.direction = direction;
		}
		
		public Placement<Unit> getUnit() {
			return unit;
		}
		
		public CardinalPoints getDirection() {
			return direction;
		}
			
	}
}
