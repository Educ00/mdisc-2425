package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.Connection;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ConnectionRepository {
    private final Set<Connection> connectionsSet;

    public ConnectionRepository() {
        this.connectionsSet = new HashSet<>();
    }

    public boolean addConnection(Connection connection) {
        return this.connectionsSet.add(connection);
    }

    public boolean removeConnection(Connection connection) {
        return this.connectionsSet.remove(connection);
    }

    public boolean existsConnection(Connection connection) {
        return this.connectionsSet.contains(connection);
    }

    public Optional<Connection> getConnection(String name) {
        for (Connection connection : connectionsSet) {
            if (connection.getName().equals(name)){
                return Optional.of(connection);
            }
        }
        return Optional.empty();
    }

    public boolean replaceAllConnections(Set<Connection> connectionsSet) {
        this.connectionsSet.clear();
        return this.connectionsSet.addAll(connectionsSet);
    }

    public Set<Connection> getAllConnections() {
        return new HashSet<>(this.connectionsSet);
    }
}
