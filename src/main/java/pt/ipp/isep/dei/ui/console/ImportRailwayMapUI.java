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

        String filepath = "";
        do {
            filepath = Utils.readLineFromConsole("File Path (Neste momento não faz nada, cria um grafo de teste): ");
            try {
                Path path = Paths.get(filepath);
                if (Files.exists(path) && Files.isRegularFile(path)) {
                    System.out.println("Inválid File path!");
                    filepath = "";
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } while (filepath == null || filepath.isEmpty() || filepath.isBlank());

        boolean result = this.controller.importFile(filepath);
        if (result) {
            System.out.println("File imported!");
        } else {
            System.out.println("File not imported!");
        }
    }
}
