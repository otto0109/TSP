package de.nordakademie;

import de.nordakademie.datasource.Node;
import de.nordakademie.datasource.Path;
import de.nordakademie.datasource.nodeType;
import de.nordakademie.helper.RouteMap;
import de.nordakademie.helper.TourSorter;
import org.jgrapht.alg.tour.*;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        RouteMap routeMap = null;
        try {
            for(int j = 0; j < 10; j ++) {
                Node start = new Node(49, 42, nodeType.Start);
                Node end = new Node(49, 0, nodeType.End);
                routeMap = new RouteMap(start, end);
                DefaultUndirectedWeightedGraph<Node, DefaultWeightedEdge> graph = routeMap.getSimpleGraph();

                HamiltonianCycleAlgorithmBase<Node, DefaultWeightedEdge> nna = new NearestNeighborHeuristicTSP<>();

                HamiltonianCycleAlgorithmBase<Node, DefaultWeightedEdge> nni = new NearestInsertionHeuristicTSP<>();

                HamiltonianCycleAlgorithmBase<Node, DefaultWeightedEdge> christofidesTour = new ChristofidesThreeHalvesApproxMetricTSP<>();

                HamiltonianCycleAlgorithmBase<Node, DefaultWeightedEdge> heldKarp = new HeldKarpTSP<>();

                List<Node> currentTour = new ArrayList<>(christofidesTour.getTour(graph).getVertexList());

                currentTour = TourSorter.sortTour(currentTour, start, end);

                Path tour = new Path();

                for (int i = 0; i < currentTour.size() - 1; i++) {
                    tour.addPath(routeMap.findPath(currentTour.get(i),currentTour.get(i + 1)));
                }

                tour.addNodeToEnd(end);


                routeMap.print(tour);
                System.out.println();
                System.out.println("-----------------------------------------------------------------Nächste Tour---------------------------------------");
                System.out.println();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}