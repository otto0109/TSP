package de.nordakademie.datasource;

import java.util.ArrayList;
import java.util.List;

public class Path {
    List<Node> nodeList = new ArrayList<>();
    Integer cost = 0;

    public void addNodeToBeginning(Node node) {
        cost += 1;
        nodeList.add(0, node);
    }

    public void addNodeToEnd(Node node) {
        cost += 1;
        nodeList.add(node);
    }

    public int getCost() {
       return cost;
    }
    public List<Node> getNodes() {
        return nodeList;
    }

    public void addPath (Path path) {
        path.getNodes().forEach(this::addNodeToEnd);
    }
}
