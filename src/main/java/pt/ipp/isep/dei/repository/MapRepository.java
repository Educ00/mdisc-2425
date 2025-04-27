package pt.ipp.isep.dei.repository;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.Graphs;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import pt.ipp.isep.dei.domain.*;

import java.util.*;
import java.util.stream.Collectors;

public class MapRepository {
    private final Graph railwayGraph;

    public MapRepository() {
        System.setProperty("org.graphstream.ui", "swing");
        this.railwayGraph = new SingleGraph("Railway", false, false, 128, 1024);
    }

    public boolean addGraph(Iterable<Station> stations, Iterable<Railway> railways) {
        if (!this.isEmpty()){
            System.out.println("A eliminar grafo antigo...");
            this.clearAll();
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
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
    }

    public void displayGraph() {
        displayCustomGraph(Graphs.clone(this.railwayGraph));
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
        Edge edge = this.railwayGraph.addEdge(railway.getName(), railway.getOrigin().getName(), railway.getTarget().getName(), false);
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


    public Node getNode(String id) {
        return this.railwayGraph.getNode(id);
    }

    public Node getNode(Station station) {
        return this.getNode(station.getName());
    }

    public Edge getEdge(String id) {
        return this.railwayGraph.getEdge(id);
    }

    public Edge getEdge(Railway railway) {
        return this.getEdge(railway.getName());
    }

    public Station getStation(Node node) {
        return (Station) node.getAttribute("station");
    }

    public Railway getRailway(Edge edge) {
        return (Railway) edge.getAttribute("railway");
    }

    public void clearAll() {
        this.railwayGraph.clear();
    }

    public boolean isEmpty() {
        return this.railwayGraph.getNodeCount() == 0 && this.railwayGraph.getEdgeCount() == 0;
    }

    public List<Railway> Dijkstra(Train train, Station origin, Station target) {
        Graph tempGraph = Graphs.clone(this.railwayGraph);

        // Se for elétrico excluimos todas as linhas não eletricas da copia do grafo
        if (train.getType().equals(TrainType.Electric)) {
            List<Edge> edgesToRemove = tempGraph.edges()
                    .filter(edge -> {
                        Railway railway = (Railway) edge.getAttribute("railway");
                        return railway.getRailwayType().equals(RailwayType.Non_Electrified);
                    })
                    .collect(Collectors.toList());
            edgesToRemove.forEach(tempGraph::removeEdge);
        }

        // Remove nós sem arestas
        List<Node> nodesToRemove = tempGraph.nodes()
                .filter(node -> node.edges().count() == 0)
                .collect(Collectors.toList());
        nodesToRemove.forEach(tempGraph::removeNode);

        this.displayCustomGraph(tempGraph);

        // Verifica se as estações existem no grafo copiado
        // se não existirem, depois de remover os vertices sem arestas, quer dizer que não se consegue chegar lá
        // ou então, foi passada uma estação invalida (acho que é impossivel mas pode acontecer)
        Node originNode = tempGraph.getNode(origin.getName());
        if (originNode == null) {
            return new ArrayList<>();
        }
        Node targetNode = tempGraph.getNode(target.getName());
        if (targetNode == null) {
            return new ArrayList<>();
        }

        // Inicializa estruturas de dados
        Map<Node, Double> distances = new HashMap<>();
        Map<Node, Node> predecessors = new HashMap<>();
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));
        Set<Node> visited = new HashSet<>();

        // Configura distâncias iniciais para o valor máximo
        for (Node node : tempGraph.nodes().collect(Collectors.toList())) {
            distances.put(node, Double.MAX_VALUE);
        }
        distances.put(originNode, 0.0);
        priorityQueue.add(originNode);

        // Executa o algoritmo de Dijkstra
        while (!priorityQueue.isEmpty()) {
            // vamos buscar o primeiro node da priority queue
            Node currentNode = priorityQueue.poll();
            if (!visited.add(currentNode)) {
                continue;
            }

            // Verifica as arestas adjacentes
            // Para cada aresta:
            for (Edge edge : currentNode.edges().collect(Collectors.toList())) {
                // vamos buscar o objeto Railway to edge -> para termos acesso à distancia etc
                Railway railway = this.getRailway(edge);

                // vamos buscar o node de destino do edge
                Node neighbor = edge.getOpposite(currentNode);

                // se já foi visitado não fazemos mais nada e processamos o proximo edge/node de destino
                if (visited.contains(neighbor)) {
                    continue;
                }

                // calculamos a nova distancia
                double newDistance = distances.get(currentNode) + railway.getLength();

                // se for menor
                if (newDistance < distances.get(neighbor)) {
                    // atualizamos a distancia
                    distances.put(neighbor, newDistance);
                    // adicionamos os nodes
                    predecessors.put(neighbor, currentNode);
                    // adicionamos o node novo na priority queue
                    priorityQueue.add(neighbor);
                }
            }
        }

        // Reconstrói o caminho
        List<Railway> path = new ArrayList<>();
        // Começamos na Estação de chegada e vamos para tras
        Node step = targetNode;
        while (predecessors.containsKey(step)) {
            // vamos buscar o node anterior
            Node previous = predecessors.get(step);
            // vamos buscar a aresta
            Edge edge = previous.getEdgeBetween(step);
            // vamos buscar o objeto railway
            path.add(this.getRailway(edge));
            step = previous;
        }

        // Inverte o caminho para a ordem correta
        Collections.reverse(path);
        return path;
    }
}
