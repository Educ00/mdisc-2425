package pt.ipp.isep.dei.domain;

import java.util.Objects;

public class Train {
    private String name;
    private TrainType type;

    public Train(String name, TrainType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public TrainType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Train train = (Train) o;
        return Objects.equals(name, train.name) && type == train.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return "Train{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
