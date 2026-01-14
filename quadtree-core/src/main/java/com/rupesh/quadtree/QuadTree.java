package com.rupesh.quadtree;

import java.util.List;

public interface QuadTree<T> {
	boolean insert(double x, double y, Object data);

	List<T> query(double x, double y, double width, double height);
}
