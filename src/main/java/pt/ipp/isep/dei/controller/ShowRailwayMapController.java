package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.repository.MapRepository;
import pt.ipp.isep.dei.repository.Repositories;

public class ShowRailwayMapController {
    private MapRepository mapRepository;

    public ShowRailwayMapController() {
        this.mapRepository = getMapRepository();
    }
    private MapRepository getMapRepository() {
        if (mapRepository == null) {
            this.mapRepository = Repositories.getInstance().getMapRepository();
        }
        return this.mapRepository;
    }
    public void showRailwayMap() {
        this.mapRepository.displayGraph();
    }
}
