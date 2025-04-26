package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.Railway;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RailwayRepository {
    private final Set<Railway> railwaySet;

    public RailwayRepository() {
        this.railwaySet = new HashSet<>();
    }

    public boolean addRailway(Railway railway) {
        return this.railwaySet.add(railway);
    }

    public boolean removeRailway(Railway railway) {
        return this.railwaySet.remove(railway);
    }

    public boolean existsRailway(Railway railway) {
        return this.railwaySet.contains(railway);
    }

    public Optional<Railway> getRailway(String name) {
        for (Railway railway : railwaySet) {
            if (railway.getName().equals(name)){
                return Optional.of(railway);
            }
        }
        return Optional.empty();
    }

    public boolean replaceAllRailway(Set<Railway> connectionsSet) {
        this.railwaySet.clear();
        return this.railwaySet.addAll(connectionsSet);
    }

    public Set<Railway> getAllRailways() {
        return new HashSet<>(this.railwaySet);
    }

    public boolean isEmpty() {
        return this.railwaySet.isEmpty();
    }

    public void clearAll() {
        this.railwaySet.clear();
    }
}
