package pt.ipp.isep.dei.repository;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import pt.ipp.isep.dei.domain.Connection;
import pt.ipp.isep.dei.domain.Station;

import java.util.Set;
import java.util.stream.Collectors;

public class RailwayRepository {
    private final Graph railway;

    public RailwayRepository() {
        System.setProperty("org.graphstream.ui", "swing");
        this.railway = new SingleGraph("Railway");
    }

    public void displayGraph(Graph customGraph) {
        customGraph.setAttribute("ui.quality");
        customGraph.setAttribute("ui.antialias");
        Viewer viewer = customGraph.display();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
    }

    public void displayGraph() {
        displayGraph(this.railway);
    }
    public Node addNode(Station station) {
        Node node = this.railway.addNode(station.getName());
        node.setAttribute("ui.label", station.getName());
        node.setAttribute("station", station);
        node.setAttribute("ui.style", "size: 20px; fill-color: #3366ff;");
        return node;
    }

    public Edge addEdge(Connection connection) {
        Edge edge = this.railway.addEdge(connection.getName(), connection.getOrigin().getName(), connection.getTarget().getName(), true);
        edge.setAttribute("ui.label", connection.getLenght());
        edge.setAttribute("connection", connection);
        return edge;
    }

    public Node getNode(int id) {
        return this.railway.getNode(id);
    }

    public Node getNode(String value) {
        return this.railway.getNode(value);
    }

    public Edge getEdge(int id) {
        return this.railway.getEdge(id);
    }

    public Edge getEdge(String value) {
        return this.railway.getEdge(value);
    }

    public Node removeNode(int index) {
        return this.railway.removeNode(index);
    }

    public Node removeNode(String name) {
        return this.railway.removeNode(name);
    }

    public Node removeNode(Node node) {
        return this.railway.removeNode(node);
    }

    public Edge removeEdge(int index) {
        return this.railway.removeEdge(index);
    }

    public Edge removeEdge(String from, String to) {
        return this.railway.removeEdge(from, to);
    }

    public Edge removeEdge(Edge edge) {
        return this.railway.removeEdge(edge);
    }

    public Set<Node> getAllNodes() {
        return this.railway.nodes().collect(Collectors.toSet());
    }

    public Station getStation(Node node) {
        return (Station) node.getAttribute("station");
    }

    public Connection getConnection(Edge edge) {
        return (Connection) edge.getAttribute("connection");
    }

    public double getLenght(Edge edge) {
        return (double) edge.getAttribute("ui.label");
    }

    public Set<Edge> getAllEdges() {
        return this.railway.edges().collect(Collectors.toSet());
    }
}
