package de.nordakademie.helper;


import de.nordakademie.datasource.Node;
import de.nordakademie.datasource.Path;
import de.nordakademie.datasource.nodeType;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;


public class RouteMap {

    DefaultUndirectedWeightedGraph<Node, DefaultWeightedEdge> jGraph = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);
    BufferedImage image;
    List<Node> borders;
    PathFinder pathFinder;

    public RouteMap(Node start, Node end) throws IOException {

        File file = new File("map.png");
        image = ImageIO.read(file);
        borders = new ArrayList<>();
        Map<Integer, Node> objectsToCollect = new HashMap<>();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int clr = image.getRGB(x, y);
                int red = (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue = clr & 0x000000ff;
                if (red == 0 && green == 0 && blue == 0) {
                    borders.add(new Node(y, x, nodeType.Border));
                }
            }
        }

        int id = 0;
        while (objectsToCollect.size() < 20) {
            int randomNumY = ThreadLocalRandom.current().nextInt(0, image.getHeight());
            int randomNumX = ThreadLocalRandom.current().nextInt(0, image.getWidth());

            if (!borders.contains(new Node(randomNumY, randomNumX, nodeType.Border)) && !objectsToCollect.containsValue(new Node(randomNumY, randomNumX, nodeType.Collectable)) &&
                    (borders.contains(new Node(randomNumY, randomNumX - 1, nodeType.Border)) ||
                            borders.contains(new Node(randomNumY, randomNumX + 1, nodeType.Border)) ||
                            borders.contains(new Node(randomNumY - 1, randomNumX, nodeType.Border)) ||
                            borders.contains(new Node(randomNumY + 1, randomNumX, nodeType.Border)))) {
                objectsToCollect.put(id, new Node(randomNumY, randomNumX, nodeType.Collectable));
                id++;
            }
        }

        pathFinder = new PathFinder(image.getHeight(), image.getWidth(), borders);

        jGraph.addVertex(start);
        jGraph.addVertex(end);

        for (Integer key : objectsToCollect.keySet()) {
            jGraph.addVertex(objectsToCollect.get(key));
        }

        for (Integer key : objectsToCollect.keySet()) {
            DefaultWeightedEdge jEdge = jGraph.addEdge(objectsToCollect.get(key), start);
            if (jEdge != null) {
                jGraph.setEdgeWeight(jEdge, pathFinder.findPath(objectsToCollect.get(key), start).getCost());
            }
        }
        for (Integer key : objectsToCollect.keySet()) {
            DefaultWeightedEdge jEdge = jGraph.addEdge(objectsToCollect.get(key), end);
            if (jEdge != null) {
                jGraph.setEdgeWeight(jEdge, pathFinder.findPath(objectsToCollect.get(key), end).getCost());
            }
        }
        DefaultWeightedEdge edge = jGraph.addEdge(start, end);
        jGraph.setEdgeWeight(edge, 0);

        for (Integer key : objectsToCollect.keySet()) {
            for (Integer key2 : objectsToCollect.keySet()) {
                if (!Objects.equals(key, key2)) {
                    DefaultWeightedEdge jEdge = jGraph.addEdge(objectsToCollect.get(key), objectsToCollect.get(key2));
                    if (jEdge != null) {
                        jGraph.setEdgeWeight(jEdge, pathFinder.findPath(objectsToCollect.get(key), objectsToCollect.get(key2)).getCost());
                    }
                }
            }
        }

        /*for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Node node = new Node(i, j);
                if (start.equals(node)) {
                    System.out.print("\u001B[32m" + "s" + "\u001B[0m");
                } else if (end.equals(node)) {
                    System.out.print("\u001B[31m" + "e" + "\u001B[0m");
                } else if (objectsToCollect.containsValue(node)) {
                    objectsToCollect.forEach((integer, node1) -> {
                        if (node1.equals(node)) {
                            System.out.print("\u001B[34m" + integer.toString().substring(0, 1) + "\u001B[0m");
                        }
                    });
                } else if (path.contains(node)) {
                    System.out.print("\u001B[35m" + "r" + "\u001B[0m");
                } else if (borders.contains(node)) {
                    System.out.print("\u001B[36m" + "m" + "\u001B[0m");
                } else {
                    System.out.print(0);
                }
            }
            System.out.println();
        }*/

        /*
        jGraph.addVertex("Aachen");
        jGraph.addVertex("Bonn");
        jGraph.addVertex("Düsseldorf");
        jGraph.addVertex("Frankfurt");
        jGraph.addVertex("Köln");
        jGraph.addVertex("Wuppertal");

        DefaultWeightedEdge jEdge = jGraph.addEdge("Aachen", "Bonn");
        jGraph.setEdgeWeight(jEdge, 91);
        jEdge = jGraph.addEdge("Aachen", "Düsseldorf");
        jGraph.setEdgeWeight(jEdge, 80);
        jEdge = jGraph.addEdge("Aachen", "Frankfurt");
        jGraph.setEdgeWeight(jEdge, 259);
        jEdge = jGraph.addEdge("Aachen", "Köln");
        jGraph.setEdgeWeight(jEdge, 70);
        jEdge = jGraph.addEdge("Aachen", "Wuppertal");
        jGraph.setEdgeWeight(jEdge, 121);
        jEdge = jGraph.addEdge("Bonn", "Düsseldorf");
        jGraph.setEdgeWeight(jEdge, 77);
        jEdge = jGraph.addEdge("Bonn", "Frankfurt");
        jGraph.setEdgeWeight(jEdge, 175);
        jEdge = jGraph.addEdge("Bonn", "Köln");
        jGraph.setEdgeWeight(jEdge, 27);
        jEdge = jGraph.addEdge("Bonn", "Wuppertal");
        jGraph.setEdgeWeight(jEdge, 84);
        jEdge = jGraph.addEdge("Düsseldorf", "Frankfurt");
        jGraph.setEdgeWeight(jEdge, 232);
        jEdge = jGraph.addEdge("Düsseldorf", "Köln");
        jGraph.setEdgeWeight(jEdge, 47);
        jEdge = jGraph.addEdge("Düsseldorf", "Wuppertal");
        jGraph.setEdgeWeight(jEdge, 29);
        jEdge = jGraph.addEdge("Frankfurt", "Köln");
        jGraph.setEdgeWeight(jEdge, 189);
        jEdge = jGraph.addEdge("Frankfurt", "Wuppertal");
        jGraph.setEdgeWeight(jEdge, 236);
        jEdge = jGraph.addEdge("Köln", "Wuppertal");
        jGraph.setEdgeWeight(jEdge, 55);
         */
    }

    public DefaultUndirectedWeightedGraph<Node, DefaultWeightedEdge> getSimpleGraph() {
        return jGraph;
    }

    public Path findPath(Node start, Node end) {
        return pathFinder.findPath(start, end);
    }

    public void print(Path tour) {
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Node node = new Node(i, j, nodeType.Any);

                if(tour.getNodes().contains(node)) {
                    List<Node> foundNodes = tour.getNodes().stream().filter(n -> n.equals(node)).collect(Collectors.toList());
                    nodeType nType = de.nordakademie.datasource.nodeType.Path;

                    for (Node n: foundNodes) {
                        if (nType == nodeType.Path) {
                            nType = n.getType();
                        }
                    }

                    if (nType == nodeType.Start) {
                        System.out.print("\u001B[32m" + "s" + "\u001B[0m");
                    } else if (nType == nodeType.End) {
                        System.out.print("\u001B[31m" + "e" + "\u001B[0m");
                    } else if (nType == nodeType.Collectable) {
                        System.out.print("\u001B[34m" + "o" + "\u001B[0m");
                    } else if (nType == nodeType.Path) {
                        System.out.print("\u001B[35m" + "r" + "\u001B[0m");
                    }
                } else {
                    if (borders.contains(node)) {
                        System.out.print("\u001B[36m" + "m" + "\u001B[0m");
                    } else {
                        System.out.print(0);
                    }
                }
            }
            System.out.println();
        }
    }
}
