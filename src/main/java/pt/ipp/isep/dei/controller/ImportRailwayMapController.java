package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.repository.RailwayRepository;
import pt.ipp.isep.dei.repository.Repositories;

public class ImportRailwayMapController {

    private RailwayRepository railwayRepository;

    public ImportRailwayMapController(){
        this.railwayRepository = getRailwayRepository();
    }

    private RailwayRepository getRailwayRepository() {
        if (railwayRepository == null) {
            this.railwayRepository = Repositories.getInstance().getRailwayRepository();
        }
        return this.railwayRepository;
    }

    public boolean importFile(String filepath) {
        return this.railwayRepository.importFile(filepath);
    }
}
