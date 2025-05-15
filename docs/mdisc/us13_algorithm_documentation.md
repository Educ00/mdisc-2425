## US13 – Train Route Reachability Verification

### Objective

Implement a mechanism that, given a railway graph with stations and lines connecting pairs of stations, allows the player to verify whether a specific train (steam-powered, diesel, or electric) can travel between any two stations in the network. The solution should consider train-type restrictions and station-type filters, using Dijkstra's algorithm to find the minimum-cost path (total distance) on the filtered graph.

### Acceptance Criteria

- **AC01:** The player must be able to choose, in real time, the train type (steam, diesel, or electric) and the station type (depot, station, or terminal).
- **AC02:** A visualization of the graph should be displayed (for example, using Graphviz or GraphStream), with electrified lines drawn in a different color from the others.
- **AC03:** All implemented procedures (except the graphic visualization routines) must rely only on primitive Java operations and not use existing library functions for routing.
- **AC04:** The algorithm(s) implemented to solve this problem must be documented in detail in the repository documentation, in a Markdown file.

### Algorithms Used

#### Dijkstra
**Purpose**: Determine if a path exists between two stations for a given train type by finding the shortest path in terms of distance.

```java
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
                // vamos buscar o objeto Railway do edge -> para termos acesso à distancia etc
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
```

### Detailed Algorithm Explanation

1. **Preparation of the Graph:**
    - **Cloning the original graph** to avoid mutating it.
    - **Filtering edges** based on train type (i.e. removing non-electrified lines for electric trains).
    - **Removing isolated nodes** with no remaining edges.
```java
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
```

2. **Graph Visualization:**
    - Display the filtered graph with a method that highlights electrified lines in a distinct color.
```java
this.displayCustomGraph(tempGraph);
```
3. **Station Existence Check:**
    - Verify that both origin and target stations exist in the filtered graph. If either is missing, return an empty path immediately.
    - This check is crucial as it determines if the stations are reachable with the given train type. If a station is isolated after filtering (e.g., an electric train cannot reach a station only connected by non-electrified lines), an empty path is returned indicating no route is possible.
```java
Node originNode = tempGraph.getNode(origin.getName());
if (originNode == null) {
    return new ArrayList<>();
}
Node targetNode = tempGraph.getNode(target.getName());
if (targetNode == null) {
    return new ArrayList<>();
}
```
4. **Data Structure Initialization:**
    - Initialize `distances`, `predecessors`, and a priority queue. Set all distances to infinity except the origin, which is zero.
    - The priority queue is a key component of Dijkstra's algorithm, as it efficiently selects the unvisited node with the smallest distance at each step. This ensures that we always process the most promising nodes first, leading to optimal path discovery.
```java
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
```
5. **Dijkstra Execution:**
    - Repeatedly extract the unvisited node with the smallest distance, update its neighbors, and record predecessors.
    - The algorithm continues until the priority queue is empty, meaning all reachable nodes have been processed.
    - If the target node is not reachable from the origin, it will not have an entry in the predecessors map, resulting in an empty path being returned.
```java
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
        // vamos buscar o objeto Railway do edge -> para termos acesso à distancia etc
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
```
6. **Path Reconstruction:**
    - Backtrack from the target using the `predecessors` map, collect edges, reverse the list, and return the path.
    - If no path exists (i.e., the target is not reachable from the origin), the while loop will not execute, and an empty path will be returned.
```java
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
```