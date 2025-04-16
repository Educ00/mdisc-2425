package pt.ipp.isep.dei.ui.console.menu;

import pt.ipp.isep.dei.ui.console.ImportRailwayMapUI;
import pt.ipp.isep.dei.ui.console.ShowRailwayMapUI;
import pt.ipp.isep.dei.ui.console.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PlayerUI implements Runnable{

    public PlayerUI() {}

    @Override
    public void run() {
        List<MenuItem> options = new ArrayList<>();
        options.add(new MenuItem("Import Railway Map", new ImportRailwayMapUI()));
        options.add(new MenuItem("Display Railway Mao", new ShowRailwayMapUI()));

        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options, "\n\n--- Player MENU -------------------------");

            if ((option >= 0) && (option < options.size())) {
                options.get(option).run();
            }
        } while (option != -1);
    }
}
