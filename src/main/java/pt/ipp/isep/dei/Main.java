package pt.ipp.isep.dei;

import pt.ipp.isep.dei.ui.console.menu.PlayerUI;

public class Main {

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.run();

        try {
            PlayerUI menu = new PlayerUI();
            menu.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}