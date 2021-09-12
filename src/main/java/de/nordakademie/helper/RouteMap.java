package de.nordakademie.helper;


import de.nordakademie.datasource.Node;
import de.nordakademie.datasource.Path;
import de.nordakademie.datasource.nodeType;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.tour.HamiltonianCycleAlgorithmBase;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;


public class RouteMap {

  DefaultUndirectedWeightedGraph<Node, DefaultWeightedEdge> jGraph =
      new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);
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
            jGraph.setEdgeWeight(jEdge,
                pathFinder.findPath(objectsToCollect.get(key), objectsToCollect.get(key2)).getCost());
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

        if (tour.getNodes().contains(node)) {
          List<Node> foundNodes = tour.getNodes().stream().filter(n -> n.equals(node)).collect(Collectors.toList());
          nodeType nType = de.nordakademie.datasource.nodeType.Path;

          for (Node n : foundNodes) {
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

  public void saveAsImage(Path tour, String path, String name, List<Node> order, Integer weight) throws IOException {
    int width = 15 * image.getWidth();
    int height = 15 * image.getHeight() + 15;

    // Constructs a BufferedImage of one of the predefined image types.
    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    // Create a graphics which can be used to draw into the buffered image
    Graphics2D g2d = bufferedImage.createGraphics();

    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        int x = 15 * j;
        int y = 15 * i;
        Node node = new Node(i, j, nodeType.Any);

        if (tour.getNodes().contains(node)) {
          List<Node> foundNodes = tour.getNodes().stream().filter(n -> n.equals(node)).collect(Collectors.toList());
          nodeType nType = de.nordakademie.datasource.nodeType.Path;
          Node foundNode = foundNodes.get(0);

          for (Node n : foundNodes) {
            if (nType == nodeType.Path) {
              nType = n.getType();
              foundNode = n;
            }
          }

          if (nType == nodeType.Start) {
            drawRectWithText(g2d, x, y, 15, 15, Color.green, "S");
          } else if (nType == nodeType.End) {
            drawRectWithText(g2d, x, y, 15, 15, Color.red, "E");
          } else if (nType == nodeType.Collectable) {
            Integer position = order.indexOf(node);
            drawRectWithText(g2d, x, y, 15, 15, Color.blue, position.toString());
          } else if (nType == nodeType.Path) {
            drawRectWithText(g2d, x, y, 15, 15, Color.gray, "");
          }
        } else {
          if (borders.contains(node)) {
            drawRectWithText(g2d, x, y, 15, 15, Color.black, "");
          } else {
            drawRectWithText(g2d, x, y, 15, 15, Color.white, "");
          }
        }
      }
    }


    drawRectWithText(g2d,0, image.getHeight() * 15,15, image.getWidth() * 15, Color.black, "weight: " + weight.toString());

    // Disposes of this graphics context and releases any system resources that it is using.
    g2d.dispose();

    // Save as PNG
    File file = new File(path + "/" + name + ".png");
    ImageIO.write(bufferedImage, "png", file);
  }

  public void drawRectWithText(Graphics2D g2d, int x, int y, int height, int width, Color color, String text) {
    // fill all the image with white
    g2d.setColor(color);
    Rectangle rectangle = new Rectangle(x, y, width, height);
    g2d.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);

    Font font = new Font("Verdana", Font.PLAIN, 10);

    // Get the FontMetrics
    FontMetrics metrics = g2d.getFontMetrics(font);
    // Determine the X coordinate for the text
    int fX = rectangle.x + (rectangle.width - metrics.stringWidth(text)) / 2;
    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
    int fY = rectangle.y + ((rectangle.height - metrics.getHeight()) / 2) + metrics.getAscent();
    // Set the font
    g2d.setFont(font);

    g2d.setColor(Color.white);
    // Draw the String
    g2d.drawString(text, fX, fY);
  }

  public Map<String, Long> computePath(
      HamiltonianCycleAlgorithmBase<Node, DefaultWeightedEdge> algorithmBase,
      String name,
      Integer resultNumber,
      Node start, Node end,
      DefaultUndirectedWeightedGraph<Node, DefaultWeightedEdge> graph,
      Boolean printImage) throws IOException {

    long startTime = System.nanoTime();
    GraphPath<Node, DefaultWeightedEdge> tspTour = algorithmBase.getTour(graph);
    long runningTime = System.nanoTime() - startTime;

    List<Node> order = TourSorter.sortTour(new ArrayList<>(tspTour.getVertexList()), start, end);

    Path tour = new Path();

    for (int i = 0; i < order.size() - 1; i++) {
      tour.addPath(this.findPath(order.get(i), order.get(i + 1)));
    }

    tour.addNodeToEnd(end);

    //this.logTour(tour, "./resultMaps/resultNumber" + resultNumber, name + "Log", order);
    //this.saveAsImage(tour, "./resultMaps/resultNumber" + resultNumber, name, order, tour.getCost());

    Map<String, Long> r = new HashMap<>();
    r.put("weight", (long) tour.getCost());
    r.put("time", runningTime);
    return r;
  }

  public void logTour (Path tour, String path, String name, List<Node> order) throws IOException {
    PrintWriter printWriter = new PrintWriter(path + "/" + name + ".txt", "UTF-8");
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        Node node = new Node(i, j, nodeType.Any);

        if (tour.getNodes().contains(node)) {
          List<Node> foundNodes = tour.getNodes().stream().filter(n -> n.equals(node)).collect(Collectors.toList());
          nodeType nType = de.nordakademie.datasource.nodeType.Path;

          for (Node n : foundNodes) {
            if (nType == nodeType.Path) {
              nType = n.getType();
            }
          }

          if (nType == nodeType.Start) {
            printWriter.print("s");
          } else if (nType == nodeType.End) {
            printWriter.print("e");
          } else if (nType == nodeType.Collectable) {
            printWriter.print("o");
          } else if (nType == nodeType.Path) {
            printWriter.print("r");
          }
        } else {
          if (borders.contains(node)) {
            printWriter.print("m");
          } else {
            printWriter.print(0);
          }
        }
      }
      printWriter.println();
    }

    for (int i = 0; i < order.size() - 1; i++) {
      printWriter.println("Von: " + order.get(i) + " Bis: " + order.get(i+1) + " Braucht es:" + this.findPath(order.get(i), order.get(i + 1)).getCost());;
    }
    printWriter.close();
  }
}
