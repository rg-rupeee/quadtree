package com.rupesh.quadtree.exception;

public class QuadTreeException extends RuntimeException {
	public QuadTreeException(String message) {
		super(message);
	}

	public QuadTreeException(String message, Throwable cause) {
		super(message, cause);
	}
}
