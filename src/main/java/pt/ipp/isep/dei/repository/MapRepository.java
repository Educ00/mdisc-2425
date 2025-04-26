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
        //graph.setAttribute("layout.repulsion", Integer.MAX_VALUE);
        graph.setAttribute("layout.force", 0.26);
        graph.setAttribute("layout.quality", 4);
        graph.setAttribute("layout.stabilization-limit", 0.9);

        graph.setAutoCreate(true);
        graph.setStrict(false);
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
        String colorHex = "";
        switch (station.getStationType()) {
            case Station:
                colorHex = "#eb4034"; // vermelho
                break;
            case Depot:
                colorHex = "#0e3fe3"; // azul
                break;
            case Terminal:
                colorHex = "#0ee311"; // verde
                break;
        }
        node.setAttribute("ui.style", "size: 20px; text-size: 12px; text-offset: 0px, -5px; fill-color: " + colorHex + ";");
        node.setAttribute("layout.weight", 40);
        return node;
    }

    public Edge addEdge(Railway railway) {
        Edge edge = this.railwayGraph.addEdge(railway.getName(), railway.getOrigin().getName(), railway.getTarget().getName(), true);
        edge.setAttribute("ui.label", railway.getLength());
        edge.setAttribute("railway", railway);
        String colorHex = "";
        switch (railway.getRailwayType()) {
            case Non_Electrified:
                colorHex = "#e3830e"; // laranja
                break;
            case Electrified:
                colorHex = "#c30ee3"; // roxo
                break;
        }
        edge.setAttribute("ui.style", "fill-color: " + colorHex + "; size: 2px; text-color: " + colorHex + "; text-size: 12px;");
        edge.setAttribute("layout.weight", 1.7);
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

    public double getLength(Edge edge) {
        return (double) edge.getAttribute("ui.label");
    }

    public Set<Edge> getAllEdges() {
        return this.railwayGraph.edges().collect(Collectors.toSet());
    }

    public void clearAll() {
        this.railwayGraph.clear();
    }

    public boolean isEmpty() {
        return this.railwayGraph.getNodeCount() == 0 && this.railwayGraph.getEdgeCount() == 0;
    }
}
