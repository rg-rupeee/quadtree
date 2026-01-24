package com.rupesh.quadtree.geometry;

public class Circle {
	private final Point center;
	private final double radius;

	public Circle(Point center, double radius) {
		if (radius <= 0) {
			throw new IllegalArgumentException("Radius must be positive");
		}
		this.center = center;
		this.radius = radius;
	}

	public Point getCenter() {
		return center;
	}

	public double getRadius() {
		return radius;
	}

	public boolean contains(Point point) {
		double dx = point.x() - center.x();
		double dy = point.y() - center.y();
		return (dx * dx + dy * dy) <= (radius * radius);
	}

	public boolean intersects(Rectangle rectangle) {
		// Find the closest point on the rectangle to the circle center
		double closestX = Math.max(rectangle.getX(),
								   Math.min(center.x(), rectangle.getX() + rectangle.getWidth()));
		double closestY = Math.max(rectangle.getY(),
								   Math.min(center.y(), rectangle.getY() + rectangle.getHeight()));

		// Calculate distance from circle center to this closest point
		double dx = center.x() - closestX;
		double dy = center.y() - closestY;

		// If distance is less than radius, they intersect
		return (dx * dx + dy * dy) <= (radius * radius);
	}
}

