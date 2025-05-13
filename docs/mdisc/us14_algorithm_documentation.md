# US14 - Maintenance Route Covering Each Railway Line Exactly Once

## Objective

Implement a mechanism that, given a railway with stations and railway lines, allows the player to view a **route that traverses each railway line exactly once**, either:
- for **all lines**, or
- only for **electrified lines**.

This route must be based on **Eulerian trail** logic (i.e., visit all edges exactly once). A warning should be shown if such a path is not possible.

---

## Algorithms Used

### 1. Degree Map Construction

**Purpose**: Calculate the degree of each station in the railway graph (i.e., the number of connected lines), optionally filtering only electrified lines.

```java
private Map<Station, Integer> buildDegreeMap(boolean onlyElectrified) {
    Map<Station, Integer> degreeMap = new HashMap<>();
    for (Railway railway : railwayRepository.getAllRailways()) {
        if (onlyElectrified && railway.getRailwayType() != RailwayType.Electrified) {
            continue;
        }
        degreeMap.put(railway.getOrigin(), degreeMap.getOrDefault(railway.getOrigin(), 0) + 1);
        degreeMap.put(railway.getTarget(), degreeMap.getOrDefault(railway.getTarget(), 0) + 1);
    }
    return degreeMap;
}
```

---

### 2. Eulerian Trail Verification

**Purpose**: Determine whether an Eulerian trail is possible.
Conditions:
- The subgraph must be **connected**
- Must have exactly **0 or 2 stations of odd degree**

```java
public boolean isEulerianTrailPossible(boolean onlyElectrified) {
    Map<Station, List<Railway>> graph = buildUndirectedGraph(onlyElectrified);
    Set<Station> visited = new HashSet<>();

    Station start = null;
    for (Map.Entry<Station, List<Railway>> entry : graph.entrySet()) {
        if (!entry.getValue().isEmpty()) {
            start = entry.getKey();
            break;
        }
    }
    if (start == null) return false;

    dfsConnectivityCheck(graph, start, visited);
    for (Map.Entry<Station, List<Railway>> entry : graph.entrySet()) {
        if (!entry.getValue().isEmpty() && !visited.contains(entry.getKey())) {
            return false;
        }
    }

    int oddCount = 0;
    for (List<Railway> edges : graph.values()) {
        if (edges.size() % 2 != 0) {
            oddCount++;
        }
    }

    return oddCount == 0 || oddCount == 2;
}
```

---

### 3. Graph Construction (Undirected)

**Purpose**: Build an undirected graph of stations and railway lines, optionally filtering electrified lines.

```java
private Map<Station, List<Railway>> buildUndirectedGraph(boolean onlyElectrified) {
    Map<Station, List<Railway>> graph = new HashMap<>();
    for (Railway railway : railwayRepository.getAllRailways()) {
        if (onlyElectrified && railway.getRailwayType() != RailwayType.Electrified) {
            continue;
        }
        graph.putIfAbsent(railway.getOrigin(), new ArrayList<>());
        graph.putIfAbsent(railway.getTarget(), new ArrayList<>());
        graph.get(railway.getOrigin()).add(railway);
        graph.get(railway.getTarget()).add(railway);
    }
    return graph;
}
```

---

### 4. Graph Connectivity Check (DFS)

**Purpose**: Verify if the graph is connected using a basic depth-first traversal.

```java
private void dfsConnectivityCheck(Map<Station, List<Railway>> graph, Station current, Set<Station> visited) {
    visited.add(current);
    for (Railway railway : graph.get(current)) {
        Station neighbor = railway.getOrigin().equals(current) ? railway.getTarget() : railway.getOrigin();
        if (!visited.contains(neighbor)) {
            dfsConnectivityCheck(graph, neighbor, visited);
        }
    }
}
```

---

### 5. Eulerian Path Construction (Fleury’s Algorithm)

**Purpose**: Construct a path that visits every line once using a recursive DFS based on Fleury's algorithm.

```java
public List<Railway> findMaintenancePathFromStation(boolean onlyElectrified, Station startStation) {
    if (!isEulerianTrailPossible(onlyElectrified)) {
        return Collections.emptyList();
    }
    Map<Station, List<Railway>> graph = buildUndirectedGraph(onlyElectrified);
    List<Railway> path = new ArrayList<>();
    dfsFleuryUndirected(graph, startStation, path);
    Collections.reverse(path);
    return path;
}
```

---

### 6. Recursive DFS for Eulerian Path

**Purpose**: Perform Fleury traversal recursively.

```java
private void dfsFleuryUndirected(Map<Station, List<Railway>> graph, Station station, List<Railway> path) {
    List<Railway> edges = graph.get(station);
    if (edges == null) return;

    while (!edges.isEmpty()) {
        Railway nextRailway = null;

        if (edges.size() == 1) {
            nextRailway = edges.get(0);
        } else {
            for (Railway railway : new ArrayList<>(edges)) {
                if (!isBridge(graph, station, railway)) {
                    nextRailway = railway;
                    break;
                }
            }
            if (nextRailway == null) {
                nextRailway = edges.get(0);
            }
        }

        removeEdgeFromGraph(graph, nextRailway);
        Station nextStation = nextRailway.getOrigin().equals(station) ? nextRailway.getTarget() : nextRailway.getOrigin();
        dfsFleuryUndirected(graph, nextStation, path);
        path.add(nextRailway);
    }
}
```

---

### 7. Bridge Detection

**Purpose**: Decide whether an edge is a bridge using a reachability count.

```java
private boolean isBridge(Map<Station, List<Railway>> graph, Station station, Railway railway) {
    int initialReachable = dfsCount(graph, station, new HashSet<>());
    removeEdgeFromGraph(graph, railway);
    int afterReachable = dfsCount(graph, station, new HashSet<>());
    graph.get(railway.getOrigin()).add(railway);
    graph.get(railway.getTarget()).add(railway);
    return afterReachable < initialReachable;
}
```

---

### 8. Reachability Count (DFS)

**Purpose**: Count reachable nodes using standard DFS (used in bridge check).

```java
private int dfsCount(Map<Station, List<Railway>> graph, Station station, Set<Station> visited) {
    visited.add(station);
    int count = 1;
    List<Railway> edges = graph.get(station);
    if (edges != null) {
        for (Railway railway : edges) {
            Station neighbor = railway.getOrigin().equals(station) ? railway.getTarget() : railway.getOrigin();
            if (!visited.contains(neighbor)) {
                count += dfsCount(graph, neighbor, visited);
            }
        }
    }
    return count;
}
```