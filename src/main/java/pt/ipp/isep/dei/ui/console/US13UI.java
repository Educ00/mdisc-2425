package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.Utils.Pair;
import pt.ipp.isep.dei.controller.US13Controller;
import pt.ipp.isep.dei.domain.Railway;
import pt.ipp.isep.dei.domain.Station;
import pt.ipp.isep.dei.domain.StationType;
import pt.ipp.isep.dei.domain.Train;
import pt.ipp.isep.dei.ui.console.utils.Utils;

import java.util.*;

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
        List<StationType> stationTypeList = new ArrayList<>(us13Controller.getAllStationTypes());
        stationTypeList.sort(Comparator.comparing(StationType::name));

        Train chosenTrain = null;
        Station originStation = null;
        Station targetStation = null;
        StationType originStationType = null;
        StationType targetStationType = null;

        List<String> options = new ArrayList<>();
        options.add("Select stations");
        options.add("Select station types");
        int option = -1;

        do {
            List<Train> tempTrainList = new ArrayList<>(trainList);
            List<Station> tempStationList = new ArrayList<>(stationList);
            List<StationType> tempStationTypeList = new ArrayList<>(stationTypeList);

            chosenTrain = (Train) Utils.showAndSelectOne(tempTrainList, "Choose Train: ");

            for (int i = 0; i < options.size(); i++) {
                System.out.println(i+1 + " - " + options.get(i));
            }
            option = Utils.readIntegerFromConsole("Option: ");

            switch (option){
                case 1:
                    originStation = (Station) Utils.showAndSelectOne(tempStationList, "Choose Origin Station: ");
                    tempStationList.remove(originStation);
                    targetStation = (Station) Utils.showAndSelectOne(tempStationList, "Choose Target Station: ");
                    break;
                case 2:
                    originStationType = (StationType) Utils.showAndSelectOne(tempStationTypeList, "Choose Origin Station Type: ");
                    //tempStationTypeList.remove(originStationType);
                    targetStationType = (StationType) Utils.showAndSelectOne(tempStationTypeList, "Choose Target Station Type: ");
                    break;
                default:
                    System.out.println("Invalid option");
                    option = -1;
                    break;
            }

        } while (chosenTrain == null || ((originStation == null || targetStation == null) && option == 1) || ((originStationType == null || targetStationType == null) && option == 2));

        switch (option){
            case 1:
                List<Railway> path = us13Controller.checkRouteForTrain(chosenTrain, originStation, targetStation, true, true);
                if (path.isEmpty()) {
                    System.out.println("Não é possivel ir de " + originStation.getName() + " até " + targetStation.getName() + " com o comboio " + chosenTrain.getName());
                }else {
                    for (int i = 0; i < path.size(); i++) {
                        if (i == path.size() - 1) {
                            System.out.print(path.get(i).getName());
                        } else {
                            System.out.print(path.get(i).getName() + " -> ");
                        }
                    }
                }
                break;
            case 2:
                Map<Pair<Station, Station>, List<Railway>> results = us13Controller.checkRoutesForTrain(chosenTrain, originStationType, targetStationType, true);
                for (Map.Entry<Pair<Station, Station>, List<Railway>> a : results.entrySet()) {
                    Pair<Station, Station> left = a.getKey();
                    List<Railway> right = a.getValue();
                    if (right.isEmpty()){
                        System.out.println(left.getFirst().getName() + " to " + left.getSecond().getName() + "  with " + chosenTrain.getName() + " is impossible with" + chosenTrain.getName());
                    } else {
                        System.out.println(left.getFirst().getName() + " to " + left.getSecond().getName() + ": ");
                        for (int i = 0; i < right.size(); i++) {
                            if (i == right.size() - 1) {
                                System.out.print(right.get(i).getName());
                            } else {
                                System.out.print(right.get(i).getName() + " -> ");
                            }
                        }
                        System.out.println();

                    }
                }
                break;
            default:
                System.out.println("Error in option");
                break;
        }

    }
}