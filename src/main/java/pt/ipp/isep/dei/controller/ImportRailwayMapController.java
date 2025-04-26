package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.Railway;
import pt.ipp.isep.dei.domain.RailwayType;
import pt.ipp.isep.dei.domain.Station;
import pt.ipp.isep.dei.domain.StationType;
import pt.ipp.isep.dei.repository.MapRepository;
import pt.ipp.isep.dei.repository.RailwayRepository;
import pt.ipp.isep.dei.repository.Repositories;
import pt.ipp.isep.dei.repository.StationRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ImportRailwayMapController {

    private MapRepository mapRepository;
    private StationRepository stationRepository;
    private RailwayRepository railwayRepository;

    public ImportRailwayMapController(){
        this.mapRepository = getMapRepository();
        this.stationRepository = getStationRepository();
        this.railwayRepository = getRailwayRepository();
    }

    private MapRepository getMapRepository() {
        if (mapRepository == null) {
            this.mapRepository = Repositories.getInstance().getMapRepository();
        }
        return this.mapRepository;
    }
    private StationRepository getStationRepository() {
        if (stationRepository == null) {
            this.stationRepository = Repositories.getInstance().getStationRepository();
        }
        return this.stationRepository;
    }
    private RailwayRepository getRailwayRepository() {
        if (railwayRepository == null) {
            this.railwayRepository = Repositories.getInstance().getRailwayRepository();
        }
        return this.railwayRepository;
    }


    public boolean importFile(String stationsFilepath, String railwaysFilepath) {
        System.out.println("Reading stations file...");
        Path path = Paths.get(stationsFilepath);
        List<Station> stationList = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path)){
            String line;
            while ((line = reader.readLine()) != null){
                String[] lineArr = line.split(";");
                if (lineArr.length != 1) {
                    throw new IllegalArgumentException("Invalid entry for station: " + line);
                }
                lineArr = line.split("_");

                String stationPrefix = lineArr[0].toUpperCase();
                // estou a guardar a linha toda, se quisermos tirar o prefixo é so fazer String stationName = line[1]
                // mas se fizermos isso temos de ajustar a parte onde lemos o ficheiro das railways para ler da mesma forma
                String stationName = line;

                StationType stationType;
                switch (stationPrefix) {
                    case "S":
                        stationType = StationType.Station;
                        break;
                    case "D":
                        stationType = StationType.Depot;
                        break;
                    case "T":
                        stationType = StationType.Terminal;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid station name format: " + stationName + " on this line: " + line);
                }

                stationList.add(new Station(stationType, stationName));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Done!");

        System.out.println("Reading railways filepath...");
        path = Paths.get(railwaysFilepath);
        List<Railway> railwayList = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(path)){
            String line;
            while ((line = reader.readLine()) != null){
                String[] lineArr = line.split(";");

                if (lineArr.length != 4) {
                    throw new IllegalArgumentException("Invalid entry for railway: " + line);
                }

                String originStationName = lineArr[0];
                String targetStationName = lineArr[1];
                int type = Integer.parseInt(lineArr[2]);
                double distance = Double.parseDouble(lineArr[3]);

                if (originStationName.equals(targetStationName)) {
                    throw new IllegalArgumentException("Origin station and target station cannot be the same: " + line);
                }

                RailwayType railwayType;
                switch (type){
                    case 0:
                        railwayType = RailwayType.Non_Electrified;
                        break;
                    case 1:
                        railwayType = RailwayType.Electrified;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid railway type: " + type + " on this line: " + line);
                }

                if (distance < 0 ) {
                    throw new IllegalArgumentException("Invalid distance: " + distance + " on this line: " + line);
                }

                Station originStation = null;
                Station targetStation = null;
                for (Station station : stationList) {
                    if (station.getName().equals(originStationName)) {
                        originStation = station;
                        break;
                    }
                }
                for (Station station : stationList) {
                    if (station.getName().equals(targetStationName)) {
                        targetStation = station;
                        break;
                    }
                }

                if (originStation == null ) {
                    throw new IllegalArgumentException("Could not match the origin station" + originStationName + "in both files");
                }
                if (targetStation == null) {
                    throw new IllegalArgumentException("Could not match the target station" + targetStationName + "in both files");
                }

                railwayList.add(new Railway(railwayType, distance, originStation, targetStation));

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Done!");

        System.out.println("Adding stations...");
        for (Station station : stationList) {
            this.stationRepository.addStation(station);
        }
        System.out.println("Done!");
        System.out.println("Adding railways...");
        for (Railway railway : railwayList) {
            this.railwayRepository.addRailway(railway);
        }
        System.out.println("Done!");

        this.mapRepository.addGraph(this.stationRepository.getAllStations(), this.railwayRepository.getAllRailways());
        return true;
    }
}
