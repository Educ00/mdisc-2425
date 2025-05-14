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

**How it works**:
- Iterate over all railway lines.
- If `onlyElectrified` is true, skip non-electrified lines.
- For each railway line:
    - Increment the degree count of the origin station.
    - Increment the degree count of the target station.
- The resulting map contains each station with its degree count.


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

**How it works**:
- Build an undirected graph of the railway system.
- Perform DFS from a station with at least one connection.
- Check if all connected stations are visited → ensures connectivity.
- Count stations with an odd number of edges.
- Return true if the count is 0 or 2.

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

**How it works**:
- For each railway line:
    - Skip it if filtering by electrified lines and it doesn't qualify.
    - Add the line to both the origin and target station’s adjacency list.
- The result is a bidirectional graph where each station knows its connected lines.


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

**How it works**:
- Perform a Depth-First Search (DFS) starting from a connected station.
- Mark each visited station.
- After traversal, verify that all stations with at least one railway line were visited.


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

**How it works**:
- First, check if an Eulerian trail is possible.
- Build the undirected graph.
- Start DFS traversal from a given station.
- At each step:
    - Prefer a non-bridge edge (i.e., one that doesn't disconnect the graph).
    - Remove the used edge from the graph.
    - Continue recursively.
- Finally, reverse the path to get the correct order of traversal.

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

**How it works**:
- For the current station:
    - If only one edge is available, use it.
    - Otherwise, iterate over all available edges and pick the first one that is **not a bridge**.
    - If all are bridges, pick any edge.
- Remove the selected edge from both connected stations.
- Recursively traverse to the next station.
- Add the edge to the final path **after** recursive call (post-order traversal).

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

**How it works**:
- Count the number of stations reachable from the current station (via DFS).
- Temporarily remove the edge in question.
- Count again the reachable stations.
- If the count decreases, the edge is a bridge.
- Restore the edge after the check.

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

**How it works**:
- Perform a DFS, counting each newly visited station.
- This gives the size of the connected component.
- Used to compare connectivity **before and after** removing an edge.

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