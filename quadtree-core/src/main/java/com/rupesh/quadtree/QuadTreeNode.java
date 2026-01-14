package com.rupesh.quadtree;

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


	/*
		Getters And Setters
	*/
	public Rectangle getBoundary() {
		return boundary;
	}

	public List<Entry<T>> getEntries() {
		return entries;
	}

	public void addEntry(Entry<T> entry) {
		this.entries.add(entry);
	}

	public void removeEntry(Entry<T> entry) {
		this.entries.remove(entry);
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


	/*
		Utility Methods
	*/
	public boolean isLeafNode() {
		return topLeftNode == null && topRightNode == null && bottomLeftNode == null && bottomRightNode == null;
	}
}
