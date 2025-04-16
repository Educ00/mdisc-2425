package pt.ipp.isep.dei.repository;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import java.util.Set;
import java.util.stream.Collectors;

public class RailwayRepository {
    private final Graph graph;

    public RailwayRepository() {
        System.setProperty("org.graphstream.ui", "swing");
        this.graph = new SingleGraph("Railway");
    }

    public boolean importFile(String filepath) {
        // TODO: Implementar a logica para importar do ficheiro
        // para já, criamos um grafo de teste atoa.
        Node node1 = addNode("Node1");
        Node node2 = addNode("Node2");
        Node node3 = addNode("Node3");
        Node node4 = addNode("Node4");
        Node node5 = addNode("Node5");
        Edge edge1 = addEdge("1-2", node1, node2);
        Edge edge2 = addEdge("2-3", node2, node3);
        Edge edge3 = addEdge("3-4", node3, node4);
        Edge edge5 = addEdge("4-5", node4, node5);
        Edge edge6 = addEdge("2-5", node2, node5);
        Edge edge7 = addEdge("5-2", node5, node2);

        return true;
    }

    public void displayGraph() {
        this.graph.setAttribute("ui.quality");
        this.graph.setAttribute("ui.antialias");
        Viewer viewer = this.graph.display();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
        //viewer.enableAutoLayout();
    }
    public Node addNode(String name) {
        Node node = this.graph.addNode(name);
        node.setAttribute("ui.label", name);
        node.setAttribute("ui.style", "size: 20px; fill-color: #3366ff;");
        return node;
    }

    public Edge addEdge(String value, Node node1, Node node2) {
        Edge edge = this.graph.addEdge(value, node1, node2, true);
        edge.setAttribute("ui.label", value);
        return edge;
    }

    public Node getNode(int id) {
        return this.graph.getNode(id);
    }

    public Node getNode(String value) {
        return this.graph.getNode(value);
    }

    public Edge getEdge(int id) {
        return this.graph.getEdge(id);
    }

    public Edge getEdge(String value) {
        return this.graph.getEdge(value);
    }

    public Node removeNode(int index) {
        return this.graph.removeNode(index);
    }

    public Node removeNode(String name) {
        return this.graph.removeNode(name);
    }

    public Node removeNode(Node node) {
        return this.graph.removeNode(node);
    }

    public Edge removeEdge(int index) {
        return this.graph.removeEdge(index);
    }

    public Edge removeEdge(String from, String to) {
        return this.graph.removeEdge(from, to);
    }

    public Edge removeEdge(Edge edge) {
        return this.graph.removeEdge(edge);
    }

    public Set<Node> getAllNodes() {
        return this.graph.nodes().collect(Collectors.toSet());
    }

    public Set<Edge> getAllEdges() {
        return this.graph.edges().collect(Collectors.toSet());
    }
}
