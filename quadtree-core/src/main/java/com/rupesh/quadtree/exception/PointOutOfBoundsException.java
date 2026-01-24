package com.rupesh.quadtree.exception;

import com.rupesh.quadtree.geometry.Point;

public class PointOutOfBoundsException extends QuadTreeException {
	public PointOutOfBoundsException(Point point) {
		super("Point " + point + " does not lie within the QuadTree boundaries");
	}
}

