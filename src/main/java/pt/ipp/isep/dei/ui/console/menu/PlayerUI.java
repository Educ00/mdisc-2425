package pt.ipp.isep.dei.ui.console.menu;

import pt.ipp.isep.dei.ui.console.*;
import pt.ipp.isep.dei.ui.console.utils.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerUI implements Runnable{

    public PlayerUI() {}

    @Override
    public void run() {
        List<MenuItem> options = new ArrayList<>();
        options.add(new MenuItem("Import Railway Map", new ImportRailwayMapUI()));
        options.add(new MenuItem("Display Railway Map", new ShowRailwayMapUI()));
        options.add(new MenuItem("US13", new US13UI()));
        options.add(new MenuItem("US14", new US14UI()));
        options.add(new MenuItem("US27", new US27UI()));

        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options, "\n\n--- Player MENU -------------------------");

            if ((option >= 0) && (option < options.size())) {
                options.get(option).run();
            }
        } while (option != -1);
    }
}
