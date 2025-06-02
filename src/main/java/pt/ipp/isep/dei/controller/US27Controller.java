package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.Station;
import pt.ipp.isep.dei.repository.MapRepository;
import pt.ipp.isep.dei.repository.Repositories;
import pt.ipp.isep.dei.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class US27Controller {

    private MapRepository mapRepository;
    private StationRepository stationRepository;


    public US27Controller() {
        this.mapRepository = getMapRepository();
        this.stationRepository = getStationRepository();
    }

    private MapRepository getMapRepository() {
        if (this.mapRepository == null) {
            this.mapRepository = Repositories.getInstance().getMapRepository();
        }
        return this.mapRepository;
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

    public Set<Station> getAllStations() {
        return this.stationRepository.getAllStations();
    }

    public List<Station> getPathWithIntermediateStops(Station originStation, Station targetStation, List<Station> intermediateStations) {
        List<Station> stationList = new ArrayList<>();
        stationList.add(originStation);
        stationList.addAll(intermediateStations);
        stationList.add(targetStation);
        List<Station> path = new ArrayList<>();

        for (int i = 1; i < stationList.size(); i++) {
            Station tempOrigin = stationList.get(i-1);
            Station tempTarget = stationList.get(i);

            List<Station> tempPath = this.mapRepository.Dijkstra(null, tempOrigin, tempTarget, false, true);

            // esta marosca escreve o path direito sem duplicar o ultimo e o primeiro node de cada temp path, a não ser no final
            for (int j = 0; j < tempPath.size(); j++) {
                if (i  == stationList.size() - 1) {
                    // final
                    path.add(tempPath.get(j));
                } else {
                    // meio
                    if (j != tempPath.size() - 1) {
                        // ultimo do meio
                        path.add(tempPath.get(j));
                    }
                }
            }
        }
        this.mapRepository.DisplayGraphWithPathFromStationList(path);
        return path;
    }
}
