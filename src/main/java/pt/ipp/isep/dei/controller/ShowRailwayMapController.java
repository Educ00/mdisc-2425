package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.repository.RailwayRepository;
import pt.ipp.isep.dei.repository.Repositories;

public class ShowRailwayMapController {
    private RailwayRepository railwayRepository;

    public ShowRailwayMapController() {
        this.railwayRepository = getRailwayRepository();
    }
    private RailwayRepository getRailwayRepository() {
        if (railwayRepository == null) {
            this.railwayRepository = Repositories.getInstance().getRailwayRepository();
        }
        return this.railwayRepository;
    }
    public void showRailwayMap() {
        this.railwayRepository.displayGraph();
    }
}
