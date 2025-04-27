package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.Railway;
import pt.ipp.isep.dei.domain.RailwayType;
import pt.ipp.isep.dei.domain.Station;
import pt.ipp.isep.dei.repository.MapRepository;
import pt.ipp.isep.dei.repository.RailwayRepository;
import pt.ipp.isep.dei.repository.Repositories;
import pt.ipp.isep.dei.repository.StationRepository;

import java.util.*;

public class US14Controller {
    private final MapRepository mapRepository;
    private final RailwayRepository railwayRepository;
    private final StationRepository stationRepository;

    public US14Controller() {
        this.mapRepository = Repositories.getInstance().getMapRepository();
        this.railwayRepository = Repositories.getInstance().getRailwayRepository();
        this.stationRepository = Repositories.getInstance().getStationRepository();
    }

    public boolean isMapEmpty() {
        return this.mapRepository.isEmpty();
    }

    public Set<Station> getAllStations() {
        return this.stationRepository.getAllStations();
    }

    public Set<Railway> getAllRailways() {
        return this.railwayRepository.getAllRailways();
    }

    public List<Station> getPossibleStartStations(boolean onlyElectrified) {
        Map<Station, Integer> degreeMap = buildDegreeMap(onlyElectrified);
        List<Station> possibleStarts = new ArrayList<>();
        for (Map.Entry<Station, Integer> entry : degreeMap.entrySet()) {
            if (entry.getValue() % 2 != 0) {
                possibleStarts.add(entry.getKey());
            }
        }
        if (possibleStarts.isEmpty()) {
            possibleStarts.addAll(degreeMap.keySet());
        }
        return possibleStarts;
    }

    public boolean isEulerianTrailPossible(boolean onlyElectrified) {
        Map<Station, Integer> degreeMap = buildDegreeMap(onlyElectrified);

        int oddCount = 0;
        for (int degree : degreeMap.values()) {
            if (degree % 2 != 0) {
                oddCount++;
            }
        }
        return oddCount == 0 || oddCount == 2;
    }

    private Map<Station, Integer> buildDegreeMap(boolean onlyElectrified) {
        Map<Station, Integer> degreeMap = new HashMap<>();
        for (Railway railway : this.railwayRepository.getAllRailways()) {
            if (onlyElectrified && railway.getRailwayType() != RailwayType.Electrified) {
                continue;
            }
            degreeMap.put(railway.getOrigin(), degreeMap.getOrDefault(railway.getOrigin(), 0) + 1);
            degreeMap.put(railway.getTarget(), degreeMap.getOrDefault(railway.getTarget(), 0) + 1);
        }
        return degreeMap;
    }


    public List<Railway> findMaintenancePathFromStation(boolean onlyElectrified, Station startStation) {
        if (!isEulerianTrailPossible(onlyElectrified)) {
            return Collections.emptyList();
        }

        Map<Station, List<Railway>> graph = buildUndirectedGraph(onlyElectrified);

        List<Railway> path = new ArrayList<>();
        dfsFleuryUndirected(graph, startStation, path);

        Collections.reverse(path);
        return path;
    }

    private Map<Station, List<Railway>> buildUndirectedGraph(boolean onlyElectrified) {
        Map<Station, List<Railway>> graph = new HashMap<>();
        for (Railway railway : this.railwayRepository.getAllRailways()) {
            if (onlyElectrified && railway.getRailwayType() != RailwayType.Electrified) {
                continue;
            }
            graph.putIfAbsent(railway.getOrigin(), new ArrayList<>());
            graph.putIfAbsent(railway.getTarget(), new ArrayList<>());
            graph.get(railway.getOrigin()).add(railway);
            graph.get(railway.getTarget()).add(railway);
        }
        return graph;
    }

    private void dfsFleuryUndirected(Map<Station, List<Railway>> graph, Station station, List<Railway> path) {
        List<Railway> edges = graph.get(station);
        if (edges == null) return;

        while (!edges.isEmpty()) {
            Railway nextRailway = null;

            if (edges.size() == 1) {
                nextRailway = edges.get(0);
            } else {
                for (Railway railway : new ArrayList<>(edges)) {
                    if (!isBridge(graph, station, railway)) {
                        nextRailway = railway;
                        break;
                    }
                }
                if (nextRailway == null) {
                    nextRailway = edges.get(0);
                }
            }

            removeEdgeFromGraph(graph, nextRailway);
            Station nextStation = nextRailway.getOrigin().equals(station) ? nextRailway.getTarget() : nextRailway.getOrigin();
            dfsFleuryUndirected(graph, nextStation, path);
            path.add(nextRailway);
        }
    }

    private void removeEdgeFromGraph(Map<Station, List<Railway>> graph, Railway railway) {
        graph.get(railway.getOrigin()).remove(railway);
        graph.get(railway.getTarget()).remove(railway);
    }

    private boolean isBridge(Map<Station, List<Railway>> graph, Station station, Railway railway) {
        int initialReachable = dfsCount(graph, station, new HashSet<>());

        removeEdgeFromGraph(graph, railway);
        int afterReachable = dfsCount(graph, station, new HashSet<>());
        // Restore the edge
        graph.get(railway.getOrigin()).add(railway);
        graph.get(railway.getTarget()).add(railway);

        return afterReachable < initialReachable;
    }

    private int dfsCount(Map<Station, List<Railway>> graph, Station station, Set<Station> visited) {
        visited.add(station);
        int count = 1;
        List<Railway> edges = graph.get(station);
        if (edges != null) {
            for (Railway railway : edges) {
                Station neighbor = railway.getOrigin().equals(station) ? railway.getTarget() : railway.getOrigin();
                if (!visited.contains(neighbor)) {
                    count += dfsCount(graph, neighbor, visited);
                }
            }
        }
        return count;
    }

    public void showRailwayMap() {
        this.mapRepository.displayGraph();
    }
}
