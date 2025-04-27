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
        int option;
        List<Train> trainList = new ArrayList<>(us13Controller.getAllTrains());
        List<Station> stationList = new ArrayList<>(us13Controller.getAllStations());
        stationList.sort(Comparator.comparing(Station::getName));
        Train chosenTrain;
        Station originStation;
        Station targetStation;
        do {
            System.out.println("\n\n--- US 13 ------------------------");
            System.out.println("1. Import Railway Map");
            System.out.println("2. Show Railway Map");
            System.out.println("3. Verifications");
            System.out.println("0. Exit");

            option = Utils.readIntegerFromConsole("Choose an option: ");

            switch (option) {
                case 1:
                    new ImportRailwayMapUI().run();
                    trainList = new ArrayList<>(us13Controller.getAllTrains());
                    stationList = new ArrayList<>(us13Controller.getAllStations());
                    stationList.sort(Comparator.comparing(Station::getName));
                    break;
                case 2:
                    if (this.us13Controller.isMapEmpty()) {
                        System.out.println("You need to import a map first!");
                    }else{
                    new ShowRailwayMapUI().run();
                    }
                    break;

                case 3:
                    if (this.us13Controller.isMapEmpty()) {
                        System.out.println("You need to import a map first!");
                    }
                    else {
                        List<Train> tempTrainList = new ArrayList<>(trainList);
                        List<Station> tempStationList = new ArrayList<>(stationList);

                        chosenTrain = (Train) Utils.showAndSelectOne(tempTrainList, "Choose Train: ");
                        if (chosenTrain == null) {
                            System.out.println("Operation canceled.");
                            break;
                        }

                        originStation = (Station) Utils.showAndSelectOne(tempStationList, "Choose Origin Station: ");
                        if (originStation == null) {
                            System.out.println("Operation canceled.");
                            break;
                        }

                        tempStationList.remove(originStation);
                        targetStation = (Station) Utils.showAndSelectOne(tempStationList, "Choose Target Station: ");
                        if (targetStation == null) {
                            System.out.println("Operation canceled.");
                            break;
                        }

                        List<Railway> path = us13Controller.checkRouteForTrain(chosenTrain, originStation, targetStation);
                        if (path.isEmpty()) {
                            System.out.println("Não é possivel ir de " + originStation.getName() + " até " + targetStation.getName() + " com o comboio " + chosenTrain.getName());
                        }else {
                            for (Railway railway : path) {
                                System.out.println(railway);
                            }
                        }
                    }
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 0);
    }
}
