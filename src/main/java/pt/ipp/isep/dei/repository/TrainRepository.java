package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.Train;
import pt.ipp.isep.dei.domain.TrainType;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class TrainRepository {
    private final Set<Train> trainSet;

    public TrainRepository() {
        this.trainSet = new HashSet<>();
    }

    public boolean addTrain(Train train) {
        return this.trainSet.add(train);
    }

    public boolean existsTrain(Train train) {
        return this.trainSet.contains(train);
    }

    public boolean removeTrain(Train train) {
        return this.trainSet.remove(train);
    }

    public Optional<Train> getTrain(String name) {
        for (Train train : this.trainSet) {
            if (train.getName().equals(name)) {
                return Optional.of(train);
            }
        }
        return Optional.empty();
    }

    public boolean replaceAllTrains(Set<Train> trainSet) {
        this.trainSet.clear();
        return this.trainSet.addAll(trainSet);
    }

    public Set<Train> getAllTrains() {
        return new HashSet<>(this.trainSet);
    }

    public boolean isEmpty() {
        return this.trainSet.isEmpty();
    }

    public void clearAll() {
        this.trainSet.clear();
    }

    public boolean containsTrain(Train train) {
        return this.trainSet.contains(train);
    }
}
