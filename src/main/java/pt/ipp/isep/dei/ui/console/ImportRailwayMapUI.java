package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.ImportRailwayMapController;
import pt.ipp.isep.dei.ui.console.utils.Utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImportRailwayMapUI implements Runnable {

    private final ImportRailwayMapController controller;

    public ImportRailwayMapUI() {
        this.controller = new ImportRailwayMapController();
    }

    @Override
    public void run() {
        System.out.println("\n\n--- Import Railway Map ------------------------");

        String stationsFilepath = "";
        String railwaysFilepath = "";
        do {
            stationsFilepath = Utils.readLineFromConsole("Stations File Path: ");
            railwaysFilepath = Utils.readLineFromConsole("Railways File Path: ");
            try {
                Path path = Paths.get(stationsFilepath);
                if (!Files.exists(path) || !Files.isRegularFile(path)) {
                    System.out.println("Invalid File path: " + stationsFilepath);
                    stationsFilepath = "";
                }
                path = Paths.get(railwaysFilepath);
                if (!Files.exists(path) || !Files.isRegularFile(path)) {
                    System.out.println("Invalid File path: " + railwaysFilepath);
                    railwaysFilepath = "";
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } while (stationsFilepath == null || stationsFilepath.isEmpty() || stationsFilepath.isBlank() || railwaysFilepath == null || railwaysFilepath.isEmpty() || railwaysFilepath.isBlank());

        boolean result = this.controller.importFile(stationsFilepath, railwaysFilepath);
        if (result) {
            System.out.println("Files imported!");
        } else {
            System.out.println("Files not imported!");
        }
    }
}
