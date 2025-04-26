package pt.ipp.isep.dei.repository;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import pt.ipp.isep.dei.domain.Railway;
import pt.ipp.isep.dei.domain.Station;

import java.util.Set;
import java.util.stream.Collectors;

public class MapRepository {
    private final Graph railwayGraph;

    public MapRepository() {
        System.setProperty("org.graphstream.ui", "swing");
        this.railwayGraph = new SingleGraph("Railway");
    }

    public boolean addGraph(Iterable<Station> stations, Iterable<Railway> railways) {
        if (this.railwayGraph.getNodeCount() != 0){
            System.out.println("A eliminar grafo antigo...");
            this.railwayGraph.clear();
            System.out.println("Done!");
        }
        for (Station station : stations) {
            this.addNode(station);
        }
        for (Railway railway : railways) {
            this.addEdge(railway);
        }
        return true;
    }

    public void displayCustomGraph(Graph graph) {
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
        Viewer viewer = graph.display();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
    }

    public void displayGraph() {
        displayCustomGraph(this.railwayGraph);
    }
    public Node addNode(Station station) {
        Node node = this.railwayGraph.addNode(station.getName());
        node.setAttribute("ui.label", station.getName());
        node.setAttribute("station", station);
        String color = "fill-color: ";
        switch (station.getStationType()){
            case Station:
                color += "#eb4034;"; // vermelho
                break;
            case Depot:
                color += "#0e3fe3;"; // azull
                break;
            case Terminal:
                color += "#0ee311;"; // verde
                break;
        }
        node.setAttribute("ui.style", "size: 20px;" + color);
        return node;
    }

    public Edge addEdge(Railway railway) {
        Edge edge = this.railwayGraph.addEdge(railway.getName(), railway.getOrigin().getName(), railway.getTarget().getName(), true);
        edge.setAttribute("ui.label", railway.getLength());
        edge.setAttribute("railway", railway);
        String color = "fill-color: ";
        switch (railway.getRailwayType()){
            case Non_Electrified:
                color += "#e3830e;"; // laranja
                break;
            case Electrified:
                color += "#c30ee3;"; // roxo
                break;
        }
        edge.setAttribute("ui.style", color);
        return edge;
    }

    public Node getNode(int id) {
        return this.railwayGraph.getNode(id);
    }

    public Node getNode(String value) {
        return this.railwayGraph.getNode(value);
    }

    public Edge getEdge(int id) {
        return this.railwayGraph.getEdge(id);
    }

    public Edge getEdge(String value) {
        return this.railwayGraph.getEdge(value);
    }

    public Node removeNode(int index) {
        return this.railwayGraph.removeNode(index);
    }

    public Node removeNode(String name) {
        return this.railwayGraph.removeNode(name);
    }

    public Node removeNode(Node node) {
        return this.railwayGraph.removeNode(node);
    }

    public Edge removeEdge(int index) {
        return this.railwayGraph.removeEdge(index);
    }

    public Edge removeEdge(String from, String to) {
        return this.railwayGraph.removeEdge(from, to);
    }

    public Edge removeEdge(Edge edge) {
        return this.railwayGraph.removeEdge(edge);
    }

    public Set<Node> getAllNodes() {
        return this.railwayGraph.nodes().collect(Collectors.toSet());
    }

    public Station getStation(Node node) {
        return (Station) node.getAttribute("station");
    }

    public Railway getRailway(Edge edge) {
        return (Railway) edge.getAttribute("railway");
    }

    public double getLenght(Edge edge) {
        return (double) edge.getAttribute("ui.label");
    }

    public Set<Edge> getAllEdges() {
        return this.railwayGraph.edges().collect(Collectors.toSet());
    }
}
