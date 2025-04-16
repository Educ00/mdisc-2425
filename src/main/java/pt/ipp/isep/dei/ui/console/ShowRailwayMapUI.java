package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.ShowRailwayMapController;

public class ShowRailwayMapUI implements Runnable {
    private final ShowRailwayMapController controller;

    public ShowRailwayMapUI() {
        this.controller = new ShowRailwayMapController();
    }
    @Override
    public void run() {
        System.out.println("\n\n--- Show Railway Map ------------------------");
        controller.showRailwayMap();
        System.out.println("Success!");
    }
}
