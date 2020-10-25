package ua.itea.model.util;

import java.util.Objects;

/* immutable class */
public class Size {
	private int width;
	private int height;
	
	public Size(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public int hashCode() {
		return Objects.hash(height, width);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Size other = (Size) obj;
		return height == other.height && width == other.width;
	}
	
	
}
