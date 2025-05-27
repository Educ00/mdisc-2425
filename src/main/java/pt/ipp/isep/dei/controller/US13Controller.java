package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.Utils.Pair;
import pt.ipp.isep.dei.domain.Railway;
import pt.ipp.isep.dei.domain.Station;
import pt.ipp.isep.dei.domain.StationType;
import pt.ipp.isep.dei.domain.Train;
import pt.ipp.isep.dei.repository.MapRepository;
import pt.ipp.isep.dei.repository.Repositories;
import pt.ipp.isep.dei.repository.StationRepository;
import pt.ipp.isep.dei.repository.TrainRepository;

import java.util.*;

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

    public Set<StationType> getAllStationTypes() {
        return this.stationRepository.getAllStationTypes();
    }

    public List<Station> checkRouteForTrain(Train chosenTrain, Station originStation, Station targetStation, boolean displayGraph, boolean removeAloneNodes) {
        return this.mapRepository.Dijkstra(chosenTrain, originStation, targetStation, displayGraph, removeAloneNodes);
    }

    public Map<Pair<Station, Station>, List<Station>> checkRoutesForTrain(Train chosenTrain, StationType originStationType, StationType targetStationType, boolean displayGraph) {
        if (displayGraph) {
            this.mapRepository.displayCustomGraph(this.mapRepository.getGraphForTrain(chosenTrain, true));
        }
        Map<Pair<Station, Station>, List<Station>> mapToReturn = new HashMap<>();
        Set<Station> originStations = this.mapRepository.getStationsByType(originStationType);
        Set<Station> targetStations = this.mapRepository.getStationsByType(targetStationType);

        for (Station origin : originStations) {
            for (Station target : targetStations) {
                if (origin != target) {
                    Pair<Station, Station> pair = new Pair<>(origin, target);
                    if (!mapToReturn.containsKey(pair) && !mapToReturn.containsKey(new Pair<>(target, origin))) {
                        mapToReturn.put(pair, new ArrayList<>());
                    }
                }
            }
        }

        for (Map.Entry<Pair<Station, Station>, List<Station>> a : mapToReturn.entrySet()) {
            List<Station> path = this.checkRouteForTrain(chosenTrain, a.getKey().getFirst(), a.getKey().getSecond(), false, true);
            mapToReturn.replace(a.getKey(), path);
        }

        return mapToReturn;
    }
}
