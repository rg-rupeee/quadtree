package com.rupesh.quadtree;

import com.rupesh.quadtree.geometry.Rectangle;

public class GlobalQuadTree<T> extends SimpleQuadTree<T>{
	private static final Rectangle WORLD_BOUNDARY = new Rectangle(
			-180, // -180 to +180
			-90,     // -90 to +90
			360,
			180
	);

	GlobalQuadTree(int splitThreshold){
		super(splitThreshold, WORLD_BOUNDARY);
	}
}
