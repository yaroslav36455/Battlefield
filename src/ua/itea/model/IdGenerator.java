package ua.itea.model;

public class IdGenerator {
	private int lastId = 0;
	
	public int getNewId() {
		return ++lastId;
	}
}