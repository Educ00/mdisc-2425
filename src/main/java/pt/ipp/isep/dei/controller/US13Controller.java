package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.Railway;
import pt.ipp.isep.dei.domain.Station;
import pt.ipp.isep.dei.domain.Train;
import pt.ipp.isep.dei.repository.MapRepository;
import pt.ipp.isep.dei.repository.Repositories;
import pt.ipp.isep.dei.repository.StationRepository;
import pt.ipp.isep.dei.repository.TrainRepository;

import java.util.List;
import java.util.Set;

public class US13Controller {
    private MapRepository mapRepository;
    private TrainRepository trainRepository;
    private StationRepository stationRepository;

    public US13Controller() {
        this.mapRepository = getMapRepository();
        this.trainRepository = getTrainRepository();
        this.stationRepository = getStationRepository();
    }

    private MapRepository getMapRepository() {
        if (this.mapRepository == null) {
            this.mapRepository = Repositories.getInstance().getMapRepository();
        }
        return this.mapRepository;
    }

    private TrainRepository getTrainRepository() {
        if (this.trainRepository == null) {
            this.trainRepository = Repositories.getInstance().getTrainRepository();
        }
        return this.trainRepository;
    }

    private StationRepository getStationRepository() {
        if (this.stationRepository == null) {
            this.stationRepository = Repositories.getInstance().getStationRepository();
        }
        return this.stationRepository;
    }

    public boolean isMapEmpty() {
        return this.mapRepository.isEmpty();
    }

    public Set<Train> getAllTrains() {
        return this.trainRepository.getAllTrains();
    }

    public Set<Station> getAllStations() {
        return this.stationRepository.getAllStations();
    }

    public List<Railway> checkRouteForTrain(Train chosenTrain, Station originStation, Station targetStation) {
        return this.mapRepository.Dijisktra(chosenTrain, originStation, targetStation);
    }
}
