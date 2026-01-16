package com.rupesh.quadtree;

import com.rupesh.quadtree.geometry.Point;
import com.rupesh.quadtree.geometry.Rectangle;
import com.rupesh.quadtree.util.Entry;

import java.util.List;

public class SimpleQuadTree<T> implements QuadTree<T>{
	private final int splitThreshold;
	private final QuadTreeNode<T> root;

	SimpleQuadTree(int splitThreshold, Rectangle boundary){
		this.splitThreshold = splitThreshold;
		this.root = new QuadTreeNode<T>(boundary);
	}

	@Override
	public Entry<T> insert(Point point, T data) {
		if(!root.getBoundary().contains(point)){
			// TODO: Throw custom exception
			throw new Error("Point does not lie withing the QuadTree Boundaries");
		}

		return this._insert(this.root, point, data);
	}

	@Override
	public boolean exists(Point point) {
		if(!root.getBoundary().contains(point)){
			// TODO: Throw custom exception
			throw new Error("Point does not lie withing the QuadTree Boundaries");
		}

		Entry<T> entry = this.root.findPoint(point);

		return entry != null;
	}

	@Override
	public boolean remove(Point point) {
		if(!root.getBoundary().contains(point)){
			// TODO: Throw custom exception
			throw new Error("Point does not lie withing the QuadTree Boundaries");
		}

		return this._remove(this.root, point);
	}

	@Override
	public List<Entry<T>> query(Point point, double width, double height) {
		return List.of();
	}

	@Override
	public List<Entry<T>> query(Point point, double radius) {
		return List.of();
	}

	private Entry<T> _insert(QuadTreeNode<T> node, Point point, T data){
		if(node == null){
			// TODO: throw custom exception
			throw new Error("Unexpected: current node is null. [Context - _insert]");
		}

		// If node is leaf node and capacity is not full
		if(node.isLeafNode() && node.getEntriesCount() < this.splitThreshold){
			Entry<T> entry = new Entry<>(point, data);
			node.addEntry(entry);
			return entry;
		}

		// If node is leaf node then subdivide
		if(node.isLeafNode()){
			node.subdivide();
		}

		QuadTreeNode<T> targetNode = node.getContainer(point);

		return _insert(targetNode, point, data);
	}

	private boolean _remove(QuadTreeNode<T> node, Point point){
		Entry<T> entry = node.findPoint(point);
		if(entry != null){
			node.removeEntry(entry);
			return true;
		}

		if(node.isLeafNode()){
			return false;
		}

		QuadTreeNode<T> targetNode = node.getContainer(point);

		return _remove(targetNode, point);
	}
}
