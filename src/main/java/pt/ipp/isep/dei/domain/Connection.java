package pt.ipp.isep.dei.domain;

public class Connection {
    private double lenght;
    private Station origin;
    private Station target;
    private String name;

    public Connection(double lenght, Station origin, Station target) {
        this.lenght = lenght;
        this.origin = origin;
        this.target = target;
        this.name = origin.getName().toLowerCase().strip() + "-" + target.getName().toLowerCase().strip();
    }

    public double getLenght() {
        return lenght;
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
}
