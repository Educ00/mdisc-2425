package pt.ipp.isep.dei.domain;

import java.util.Objects;

public class Railway {
    private RailwayType railwayType;
    private double length;
    private Station origin;
    private Station target;
    private String name;

    public Railway(RailwayType railwayType, double length, Station origin, Station target) {
        this.railwayType = railwayType;
        this.length = length;
        this.origin = origin;
        this.target = target;
        this.name = origin.getName().toLowerCase().strip() + "-" + target.getName().toLowerCase().strip();
    }

    public RailwayType getRailwayType() {
        return railwayType;
    }

    public double getLength() {
        return length;
    }

    public Station getOrigin() {
        return origin;
    }

    public Station getTarget() {
        return target;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Railway railway = (Railway) o;
        return Double.compare(length, railway.length) == 0 && railwayType == railway.railwayType && Objects.equals(origin, railway.origin) && Objects.equals(target, railway.target) && Objects.equals(name, railway.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(railwayType, length, origin, target, name);
    }

    @Override
    public String toString() {
        return "Railway{" +
                "name='" + name + '\'' +
                ", target=" + target +
                ", railwayType=" + railwayType +
                '}';
    }
}
