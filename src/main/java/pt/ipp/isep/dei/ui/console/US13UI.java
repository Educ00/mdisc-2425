package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.US13Controller;
import pt.ipp.isep.dei.domain.Railway;
import pt.ipp.isep.dei.domain.Station;
import pt.ipp.isep.dei.domain.Train;
import pt.ipp.isep.dei.ui.console.utils.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class US13UI implements Runnable {

    private final US13Controller us13Controller;

    public US13UI() {
        this.us13Controller = new US13Controller();
    }

    @Override
    public void run() {
        System.out.println("\n\n--- US 13 ------------------------");
        if (this.us13Controller.isMapEmpty()){
            System.out.println("You need to import a map first!");
            return;
        }
        List<Train> trainList = new ArrayList<>(us13Controller.getAllTrains());
        List<Station> stationList = new ArrayList<>(us13Controller.getAllStations());
        stationList.sort(Comparator.comparing(Station::getName));
        Train chosenTrain;
        Station originStation;
        Station targetStation;
        do {
            List<Train> tempTrainList = new ArrayList<>(trainList);
            List<Station> tempStationList = new ArrayList<>(stationList);

            chosenTrain = (Train) Utils.showAndSelectOne(tempTrainList, "Choose Train: ");

            originStation = (Station) Utils.showAndSelectOne(tempStationList, "Choose Origin Station: ");

            tempStationList.remove(originStation);
            targetStation = (Station) Utils.showAndSelectOne(tempStationList, "Choose Target Station: ");

        } while (chosenTrain == null || originStation == null || targetStation == null);

        List<Railway> path = us13Controller.checkRouteForTrain(chosenTrain, originStation, targetStation);
        if (path.isEmpty()) {
            System.out.println("Não é possivel ir de " + originStation.getName() + " até " + targetStation.getName() + " com o comboio " + chosenTrain.getName());
        }else {
            for (Railway railway : path) {
                System.out.println(railway);
            }
        }
    }
}