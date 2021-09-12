package de.nordakademie.datasource;

public class Node implements Comparable<Node> {
	private int x, y, g, h, f;
	private Node parent;
	private nodeType type;
	private String name;

	public Node(int y, int x, nodeType type) {
		this.y = y;
		this.x = x;
		this.type = type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getG() {
		return g;
	}

	public int getH() {
		return h;
	}

	public int getF() {
		return f;
	}

	public Node getNode() {
		return parent;
	}
	
	public Node getParent() {
		return parent;
	}

	public nodeType getType() {
		return type;
	}

	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setG(int g) {
		this.g = g;
		this.f = this.g + this.h;
	}

	public void setH(int h) {
		this.h = h;
		this.f = this.g + this.h;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	@Override
	public boolean equals(Object o) {
		Node node = (Node) o;

		if(node == null) {
			return false;
		} else if (this.getX() == node.getX() && this.getY() == node.getY()) {
			return true;
		}
		return false;
	}

	@Override
	public String toString () {
		return type.toString();
	}

	@Override
	public int compareTo(Node o) {
		if (this.getF() > o.getF()) {
			return 1;
		} else if (this.getF() == o.getF()) {
			return 0;
		}
		return -1;
	}
}
