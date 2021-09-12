package de.nordakademie;

import de.nordakademie.datasource.Node;
import de.nordakademie.datasource.nodeType;
import de.nordakademie.helper.RouteMap;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import javax.imageio.ImageIO;
import org.jgrapht.alg.tour.ChristofidesThreeHalvesApproxMetricTSP;
import org.jgrapht.alg.tour.HamiltonianCycleAlgorithmBase;
import org.jgrapht.alg.tour.HeldKarpTSP;
import org.jgrapht.alg.tour.NearestInsertionHeuristicTSP;
import org.jgrapht.alg.tour.NearestNeighborHeuristicTSP;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Main {
  public static void main(String[] args) throws IOException {
    RouteMap routeMap = null;
    long totalNnaWeight = 0;
    long totalNnaTime = 0;
    long totalNniWeight = 0;
    long totalNniTime = 0;
    long totalChristofidesWeight = 0;
    long totalChristofidesTime = 0;
    long totalHeldWeight = 0;
    long totalHeldTime = 0;
    int tries = 10;
    for (int j = 0; j < tries; j++) {

      Files.createDirectories(Paths.get("./resultMaps/resultNumber" + j));

      Node start = new Node(49, 42, nodeType.Start);
      Node end = new Node(49, 0, nodeType.End);
      routeMap = new RouteMap(start, end);
      Boolean printImage = false;
      DefaultUndirectedWeightedGraph<Node, DefaultWeightedEdge> graph = routeMap.getSimpleGraph();

      HamiltonianCycleAlgorithmBase<Node, DefaultWeightedEdge> nna = new NearestNeighborHeuristicTSP<>();

      Map<String, Long> nnaMap = routeMap.computePath(nna, "nna", j, start, end, graph, printImage);

      totalNnaWeight += nnaMap.get("weight");
      totalNnaTime += nnaMap.get("time");

      HamiltonianCycleAlgorithmBase<Node, DefaultWeightedEdge> nni = new NearestInsertionHeuristicTSP<>();

      Map<String, Long> nniMap = routeMap.computePath(nni, "nni", j, start, end, graph, printImage);

      totalNniWeight += nniMap.get("weight");
      totalNniTime += nniMap.get("time");

      HamiltonianCycleAlgorithmBase<Node, DefaultWeightedEdge> christofides =
          new ChristofidesThreeHalvesApproxMetricTSP<>();

      Map<String, Long> christofidesMap = routeMap.computePath(christofides, "christofides", j, start, end, graph,
          printImage);

      totalChristofidesWeight += christofidesMap.get("weight");
      totalChristofidesTime += christofidesMap.get("time");

      HamiltonianCycleAlgorithmBase<Node, DefaultWeightedEdge> heldKarp = new HeldKarpTSP<>();

      Map<String, Long> heldMap = routeMap.computePath(heldKarp, "heldKarp", j, start, end, graph, printImage);

      totalHeldWeight += heldMap.get("weight");
      totalHeldTime += heldMap.get("time");
    }

    System.out.println("NNA: Weight: " + (totalNnaWeight / tries) + " Time: " + (double) (totalNnaTime / tries) / 1000000000 + "sec");
    System.out.println("NNI: Weight: " + (totalNniWeight / tries) + " Time: " + (double) (totalNniTime / tries) / 1000000000 + "sec");
    System.out.println("Christofides: Weight: " + (totalChristofidesWeight / tries) + " Time: " + (double) (totalChristofidesTime / tries) / 1000000000 + "sec");
    System.out.println("Held: Weight: " + (totalHeldWeight / tries) + " Time: " + (double) (totalHeldTime / tries) / 1000000000 + "sec");

  }
}
