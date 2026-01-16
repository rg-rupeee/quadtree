package com.rupesh.quadtree;

import com.rupesh.quadtree.geometry.Point;
import com.rupesh.quadtree.util.Entry;

import java.util.List;

public interface QuadTree<T> {
	Entry<T> insert(Point point, T data);

	boolean exists(Point point);

	boolean remove(Point point);

	List<Entry<T>> query(Point point, double width, double height);

	List<Entry<T>> query(Point point, double radius);
}
