package com.rupesh.quadtree.util;

import com.rupesh.quadtree.geometry.Point;

public record Entry<T>(Point point, T data) {
}
