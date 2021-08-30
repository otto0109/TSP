package de.nordakademie.helper;

import de.nordakademie.datasource.Node;

import java.util.ArrayList;
import java.util.List;

public class TourSorter {

    public static List<Node> sortTour(List<Node> tour, Node start, Node end) {

        List<Node> sortedTour = new ArrayList<>();
        Integer indexStart = tour.indexOf(start);
        if (tour.indexOf(start) < tour.indexOf(end) || tour.indexOf(end) == 0) {
            while (tour.size() > 0) {
                Node entry = tour.get(indexStart);
                tour.remove(entry);
                if (indexStart == 0) {
                    indexStart = tour.size();
                }
                indexStart--;
                if (!sortedTour.contains(entry)) {
                    sortedTour.add(entry);
                }
            }
        } else {
            while (tour.size() > 0) {
                Node entry = tour.get(indexStart);
                tour.remove(entry);
                if (indexStart == tour.size()) {
                    indexStart = 0;
                }
                if (!sortedTour.contains(entry)) {
                    sortedTour.add(entry);
                }
            }
        }

        return sortedTour;
    }
}
