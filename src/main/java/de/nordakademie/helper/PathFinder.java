package de.nordakademie.helper;

import de.nordakademie.datasource.Node;
import de.nordakademie.datasource.Path;
import de.nordakademie.datasource.nodeType;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class PathFinder {

    private int height, width;
    private PriorityQueue<Node> openList;
    private List<Node> closedList, borders;
    private final int STRAIGHT_COST = 1;

    public PathFinder(int height, int width) {
        this.height = height;
        this.width = width;
        openList = new PriorityQueue<>();
        closedList = new ArrayList<>();
        borders = new ArrayList<>();
    }

    public PathFinder(int height, int width, List<Node> borders) {
        this.height = height;
        this.width = width;
        openList = new PriorityQueue<>();
        closedList = new ArrayList<>();
        this.borders = borders;
    }

    public Path findPath(Node start, Node end) {
        openList = new PriorityQueue<>();
        closedList = new ArrayList<>();

        Path path = new Path();

        start.setG(0);
        start.setH(0);

        openList.add(start);

        while (openList.size() > 0 && !openList.contains(end)) {
            Node parent = openList.poll();

            closedList.add(parent);

            if (parent.getY() > 0) {
                Node top = computeCost(new Node(parent.getY() - 1, parent.getX(), nodeType.Path), parent, end);
                if (!closedList.contains(top) && !openList.contains(top) && !borders.contains(top)) {
                    top.setParent(parent);
                    openList.add(top);
                }
            }

            if (parent.getX() < width - 1) {
                Node right = computeCost(new Node(parent.getY(), parent.getX() + 1, nodeType.Path), parent, end);
                if (!closedList.contains(right) && !openList.contains(right) && !borders.contains(right)) {
                    right.setParent(parent);
                    openList.add(right);
                }
            }

            if (parent.getX() > 0) {
                Node left = computeCost(new Node(parent.getY(), parent.getX() - 1, nodeType.Path), parent, end);
                if (!closedList.contains(left) && !openList.contains(left) && !borders.contains(left)) {
                    left.setParent(parent);
                    openList.add(left);
                }
            }

            if (parent.getY() < height - 1) {
                Node bottom = computeCost(new Node(parent.getY() + 1, parent.getX(), nodeType.Path), parent, end);
                if (!closedList.contains(bottom) && !openList.contains(bottom) && !borders.contains(bottom)) {
                    bottom.setParent(parent);
                    openList.add(bottom);
                }
            }
        }

        if (openList.contains(end)) {
            Node currentNode = openList.stream().filter(node -> node.equals(end)).collect(Collectors.toList()).get(0);

            while (currentNode != null) {
                path.addNodeToBeginning(currentNode);
                currentNode = currentNode.getParent();
            }
        }

        return path;
    }

    private Node computeCost(Node node, Node parent, Node end) {

        node.setH(STRAIGHT_COST * (Math.abs(node.getX()) - end.getX()) + (Math.abs(node.getY()) - end.getY()));
        node.setG(STRAIGHT_COST + parent.getG());

        return node;
    }

    public void addBorder(Node border) {
        borders.add(border);
    }

}
