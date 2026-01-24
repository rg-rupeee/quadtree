package com.rupesh.quadtree;

import com.rupesh.quadtree.exception.PointOutOfBoundsException;
import com.rupesh.quadtree.exception.QuadTreeException;
import com.rupesh.quadtree.geometry.Circle;
import com.rupesh.quadtree.geometry.Point;
import com.rupesh.quadtree.geometry.Rectangle;
import com.rupesh.quadtree.util.Entry;

import java.util.ArrayList;
import java.util.List;

public class SimpleQuadTree<T> implements QuadTree<T>{
	private final int splitThreshold;
	private final QuadTreeNode<T> root;

	public SimpleQuadTree(int splitThreshold, Rectangle boundary){
		if (splitThreshold < 1) {
			throw new IllegalArgumentException("Split threshold must be at least 1");
		}
		this.splitThreshold = splitThreshold;
		this.root = new QuadTreeNode<>(boundary);
	}

	@Override
	public Entry<T> insert(Point point, T data) {
		if(!root.getBoundary().containsInclusive(point)){
			throw new PointOutOfBoundsException(point);
		}

		// Check for duplicate point
		Entry<T> existing = this.root.findPoint(point);
		if (existing != null) {
			// Update existing entry's data by removing and re-inserting
			this._remove(this.root, point);
		}

		return this._insert(this.root, point, data);
	}

	@Override
	public boolean exists(Point point) {
		if(!root.getBoundary().containsInclusive(point)){
			throw new PointOutOfBoundsException(point);
		}

		Entry<T> entry = this.root.findPoint(point);

		return entry != null;
	}

	@Override
	public boolean remove(Point point) {
		if(!root.getBoundary().containsInclusive(point)){
			throw new PointOutOfBoundsException(point);
		}

		boolean removed = this._remove(this.root, point);

		// After removal, try to merge nodes if they're under-utilized
		if (removed) {
			this._tryMerge(this.root);
		}

		return removed;
	}

	@Override
	public List<Entry<T>> query(Point point, double width, double height) {
		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException("Width and height must be positive");
		}

		Rectangle queryRange = new Rectangle(point.x(), point.y(), width, height);
		List<Entry<T>> results = new ArrayList<>();
		this.root.queryRange(queryRange, results);
		return results;
	}

	@Override
	public List<Entry<T>> query(Point point, double radius) {
		if (radius <= 0) {
			throw new IllegalArgumentException("Radius must be positive");
		}

		Circle queryCircle = new Circle(point, radius);
		List<Entry<T>> results = new ArrayList<>();
		this.root.queryCircle(queryCircle, results);
		return results;
	}

	private Entry<T> _insert(QuadTreeNode<T> node, Point point, T data){
		if(node == null){
			throw new QuadTreeException("Unexpected: current node is null. [Context - _insert]");
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

	private void _tryMerge(QuadTreeNode<T> node) {
		if (node == null || node.isLeafNode()) {
			return;
		}

		// Recursively try to merge children first
		_tryMerge(node.getTopLeftNode());
		_tryMerge(node.getTopRightNode());
		_tryMerge(node.getBottomLeftNode());
		_tryMerge(node.getBottomRightNode());

		// Try to merge this node if possible
		if (node.canMerge(this.splitThreshold / 4)) {
			node.merge();
		}
	}
}
