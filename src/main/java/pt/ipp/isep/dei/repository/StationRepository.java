package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.Station;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class StationRepository {
    private final Set<Station> stationSet;

    public StationRepository() {
        this.stationSet = new HashSet<>();
    }

    public boolean addStation(Station station) {
        return this.stationSet.add(station);
    }

    public boolean existsStation(Station station) {
        return this.stationSet.contains(station);
    }

    public boolean removeStation(Station station) {
        return this.stationSet.remove(station);
    }

    public Optional<Station> getStation(String name) {
        for (Station station : this.stationSet) {
            if (station.getName().equals(name)) {
                return Optional.of(station);
            }
        }
        return Optional.empty();
    }

    public boolean replaceAllStations(Set<Station> stationSet) {
        this.stationSet.clear();
        return this.stationSet.addAll(stationSet);
    }

    public Set<Station> getAllStations() {
        return new HashSet<>(this.stationSet);
    }

    public boolean isEmpty(){
        return this.stationSet.isEmpty();
    }

    public void clearAll() {
        this.stationSet.clear();
    }
}
