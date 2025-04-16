package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.Connection;
import pt.ipp.isep.dei.domain.Station;
import pt.ipp.isep.dei.repository.RailwayRepository;
import pt.ipp.isep.dei.repository.Repositories;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        // TODO: implementar o parser para o ficheiro que ainda nos vão dar
        // para já, só criei umas stations e connections de teste
        Set<Station> stationSet = new HashSet<>();
        Station station1 = new Station("Station1", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0 );
        Station station2 = new Station("Station2", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0 );
        Station station3 = new Station("Station3", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0 );
        Station station4 = new Station("Station4", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0 );
        Station station5 = new Station("Station5", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0 );
        stationSet.add(station1);
        stationSet.add(station2);
        stationSet.add(station3);
        stationSet.add(station4);
        stationSet.add(station5);
        for (Station station : stationSet) {
            this.railwayRepository.addNode(station);
        }

        Set<Connection> connectionSet = new HashSet<>();
        Connection connection1 = new Connection(1, station1, station2);
        Connection connection2 = new Connection(1, station2, station3);
        Connection connection3 = new Connection(1, station3, station4);
        Connection connection4 = new Connection(1, station4, station5);
        Connection connection5 = new Connection(1, station2, station5);
        Connection connection6 = new Connection(1, station5, station2);
        connectionSet.add(connection1);
        connectionSet.add(connection2);
        connectionSet.add(connection3);
        connectionSet.add(connection4);
        connectionSet.add(connection5);
        connectionSet.add(connection6);
        for (Connection connection : connectionSet) {
            this.railwayRepository.addEdge(connection);
        }

        return true;
    }
}
