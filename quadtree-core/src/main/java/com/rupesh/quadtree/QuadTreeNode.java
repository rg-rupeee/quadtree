package com.rupesh.quadtree;

import com.rupesh.quadtree.geometry.Point;
import com.rupesh.quadtree.geometry.Rectangle;
import com.rupesh.quadtree.util.Entry;

import java.util.ArrayList;
import java.util.List;

public class QuadTreeNode<T> {
	private final Rectangle boundary;

	private final List<Entry<T>> entries;

	private QuadTreeNode<T> topLeftNode;
	private QuadTreeNode<T> topRightNode;
	private QuadTreeNode<T> bottomLeftNode;
	private QuadTreeNode<T> bottomRightNode;

	private QuadTreeNode<T> parent;

	public QuadTreeNode(Rectangle boundary) {
		this.boundary = boundary;
		this.entries = new ArrayList<>();

		this.topLeftNode = null;
		this.topRightNode = null;
		this.bottomLeftNode = null;
		this.bottomRightNode = null;
		this.parent = null;
	}


	public Rectangle getBoundary() {
		return boundary;
	}

	public List<Entry<T>> getEntries() {
		return entries;
	}

	public int getEntriesCount() {
		return entries.size();
	}

	public Entry<T> findPoint(Point p){
		// Return null if point is outside this node's boundary
		if (!this.boundary.contains(p)) {
			return null;
		}

		// If this is a leaf, search entries directly
		if (isLeafNode()) {
			for (Entry<T> entry : this.entries) {
				if (entry.point().equals(p)) {
					return entry;
				}
			}
			return null;
		}

		// Otherwise, delegate to the appropriate child node
		return this.getContainer(p).findPoint(p);
	}

	public void addEntry(Entry<T> entry) {
		this.entries.add(entry);
	}

	public void removeEntry(Entry<T> entry) {
		this.entries.remove(entry);
	}

	public void clearEntries(){
		this.entries.clear();
	}

	public QuadTreeNode<T> getTopLeftNode() {
		return topLeftNode;
	}

	public void setTopLeftNode(QuadTreeNode<T> topLeftNode) {
		this.topLeftNode = topLeftNode;
	}

	public QuadTreeNode<T> getTopRightNode() {
		return topRightNode;
	}

	public void setTopRightNode(QuadTreeNode<T> topRightNode) {
		this.topRightNode = topRightNode;
	}

	public QuadTreeNode<T> getBottomLeftNode() {
		return bottomLeftNode;
	}

	public void setBottomLeftNode(QuadTreeNode<T> bottomLeftNode) {
		this.bottomLeftNode = bottomLeftNode;
	}

	public QuadTreeNode<T> getBottomRightNode() {
		return bottomRightNode;
	}

	public void setBottomRightNode(QuadTreeNode<T> bottomRightNode) {
		this.bottomRightNode = bottomRightNode;
	}

	public QuadTreeNode<T> getParent() {
		return parent;
	}

	public void setParent(QuadTreeNode<T> parent) {
		this.parent = parent;
	}

	public boolean isLeafNode() {
		return topLeftNode == null && topRightNode == null && bottomLeftNode == null && bottomRightNode == null;
	}

	public void subdivide(){
		double x = boundary.getX();
		double y = boundary.getY();
		double w = boundary.getWidth() / 2;
		double h = boundary.getHeight() / 2;

		this.topLeftNode = new QuadTreeNode<>(new Rectangle(x, y, w, h));
		this.topRightNode = new QuadTreeNode<>(new Rectangle(x+w, y, w, h));
		this.bottomLeftNode = new QuadTreeNode<>(new Rectangle(x, y+h, w, h));
		this.bottomRightNode = new QuadTreeNode<>(new Rectangle(x+w, y+h, w, h));

		this.topLeftNode.setParent(this);
		this.topRightNode.setParent(this);
		this.bottomLeftNode.setParent(this);
		this.bottomRightNode.setParent(this);

		// move all points
		for(Entry<T> entry : entries){
			this.getContainer(entry.point()).addEntry(entry);
		}

		this.clearEntries();
	}

	public QuadTreeNode<T> getContainer(Point point){
		if(this.topLeftNode.getBoundary().contains(point)){
			return this.topLeftNode;
		}

		if(this.topRightNode.getBoundary().contains(point)){
			return this.topRightNode;
		}

		if(this.bottomLeftNode.getBoundary().contains(point)){
			return this.bottomLeftNode;
		}

		if(this.bottomRightNode.getBoundary().contains(point)){
			return this.bottomRightNode;
		}

		throw new Error("Point does not lie withing any child nodes");
	}
}
