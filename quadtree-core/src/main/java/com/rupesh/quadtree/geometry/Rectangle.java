package com.rupesh.quadtree.geometry;

public class Rectangle {
	private final double x;
	private final double y;
	private final double width;
	private final double height;

	public Rectangle(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Point getTopLeftPoint() {
		return new Point(x, y);
	}

	public Point getTopRightPoint() {
		return new Point(x + width, y);
	}

	public Point getBottomLeftPoint() {
		return new Point(x, y + height);
	}

	public Point getBottomRightPoint() {
		return new Point(x + width, y + height);
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public boolean contains(Point point) {
		return point.x() >= x &&
			   point.x() <= x + width &&
			   point.y() >= y &&
			   point.y() <= y + height;
	}

	public boolean intersects(Rectangle other) {
		return !doesNotIntersect(other);
	}

	private boolean doesNotIntersect(Rectangle other) {
		return other.x > this.x + this.width ||
			   other.x + other.width < this.x ||
			   other.y > this.y + this.height ||
			   other.y + other.height < this.y;
	}
}
