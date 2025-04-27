package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.US14Controller;
import pt.ipp.isep.dei.domain.Railway;
import pt.ipp.isep.dei.domain.Station;
import pt.ipp.isep.dei.ui.console.utils.Utils;

import java.util.Comparator;
import java.util.List;

public class US14UI implements Runnable {

    private final US14Controller controller;

    public US14UI() {
        this.controller = new US14Controller();
    }

    @Override
    public void run() {
        if (this.controller.isMapEmpty()) {
            System.out.println("You need to import a map first!");
        } else {
            handleMaintenanceRoute();
        }

    }

    private void handleMaintenanceRoute() {
        System.out.println("\nMaintenance Options:");
        System.out.println("1. All lines");
        System.out.println("2. Only electrified lines");

        int choice = Utils.readIntegerFromConsole("Choose an option: ");
        boolean onlyElectrified = (choice == 2);

        if (!controller.isEulerianTrailPossible(onlyElectrified)) {
            System.out.println("\nIt is not possible to create a maintenance route.");
            return;
        }

        List<Station> possibleStarts = controller.getPossibleStartStations(onlyElectrified);
        if (possibleStarts.isEmpty()) {
            System.out.println("\nThere are no available stations to start the maintenance route.");
            return;
        }

        // Sort primeiro por tipo de estação e depois por nome
        possibleStarts.sort(Comparator
                .comparing((Station s) -> s.getStationType().toString())
                .thenComparing(Station::getName));

        System.out.println("\nPossible starting stations:");
        int index = 1;
        for (Station station : possibleStarts) {
            System.out.printf("  %d - %s%n", index++, station.getName());
        }
        System.out.println("  0 - Cancel");

        int selectedOption = Utils.readIntegerFromConsole("Type your option: ");
        if (selectedOption == 0) {
            System.out.println("Operation canceled.");
            return;
        }
        if (selectedOption < 1 || selectedOption > possibleStarts.size()) {
            System.out.println("Invalid option selected.");
            return;
        }

        Station chosenStart = possibleStarts.get(selectedOption - 1);

        List<Railway> path = controller.findMaintenancePathFromStation(onlyElectrified, chosenStart);

        if (path.isEmpty()) {
            System.out.println("\nCould not find a valid maintenance path from the selected station.");
        } else {
            System.out.println("\nMaintenance path:");
            for (Railway railway : path) {
                System.out.println(railway.getOrigin().getName() + " -> " + railway.getTarget().getName()
                        + " (" + railway.getRailwayType() + ")");
            }
            controller.showRailwayMap();
        }
    }

}
