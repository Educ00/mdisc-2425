package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.US27Controller;
import pt.ipp.isep.dei.domain.Railway;
import pt.ipp.isep.dei.domain.Station;
import pt.ipp.isep.dei.ui.console.utils.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class US27UI implements Runnable{
    private final US27Controller us27Controller;

    public US27UI() {
        this.us27Controller = new US27Controller();
    }


    @Override
    public void run() {
        System.out.println("\n\n--- US 13 ------------------------");
        if (this.us27Controller.isMapEmpty()){
            System.out.println("You need to import a map first!");
            return;
        }
        List<Station> stationList = new ArrayList<>(this.us27Controller.getAllStations());
        stationList.sort(Comparator.comparing(Station::getName));
        List<Station> intermidiateStations = new ArrayList<>();
        Station originStation = null;
        Station targetStation = null;

        do {
            List<Station> tempStationList = new ArrayList<>(stationList);
            originStation = (Station) Utils.showAndSelectOne(tempStationList, "Select Origin Station: ");
            tempStationList.remove(originStation);
            targetStation = (Station) Utils.showAndSelectOne(tempStationList, "Select Target station: ");
        } while (originStation == null || targetStation == null);

        Station chosenStation = null;
        List<Station> tempStationList = new ArrayList<>(stationList);
        tempStationList.remove(originStation);
        tempStationList.remove(targetStation);
        do {
            System.out.println("Current path: ");
            System.out.print(originStation.getName() + " -> ");
            if (!intermidiateStations.isEmpty()) {
                for (Station a : intermidiateStations) {
                    System.out.print(a.getName() + " -> ");
                }
            }
            System.out.print(targetStation.getName());
            System.out.println();

            chosenStation = (Station) Utils.showAndSelectOne(tempStationList, "Select Intermediate Stop: ");
            if (chosenStation != null) {
                intermidiateStations.add(chosenStation);
                tempStationList = new ArrayList<>(stationList);
                tempStationList.remove(chosenStation);
                tempStationList.remove(targetStation);
            }

        } while (chosenStation != null);

        List<Station> path = us27Controller.getPathWithIntermediateStops(originStation, targetStation, intermidiateStations);
        for (int i = 0; i < path.size(); i++) {
            if (i == path.size() - 1) {
                System.out.print(path.get(i).getName());
            } else {
                System.out.print(path.get(i).getName() + " -> ");
            }
        }
    }
}
