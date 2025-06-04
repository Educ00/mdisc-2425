# Algorithm Complexity Analysis Report

## US13 - Train Route Reachability Verification

### Dijkstra's Algorithm

**Purpose**: Find the shortest path between two stations considering train type restrictions.

#### Pseudocode

```
DIJKSTRA(train, origin, target):
1.  tempGraph = CLONE(railwayGraph)                           // O(V + E)
2.  IF train.type == Electric THEN                            // O(1)
3.      edgesToRemove = []                                     // O(1)
4.      FOR each edge in tempGraph.edges DO                   // O(E)
5.          railway = edge.getAttribute("railway")             // O(1)
6.          IF railway.type == Non_Electrified THEN           // O(1)
7.              edgesToRemove.add(edge)                        // O(1)
8.      FOR each edge in edgesToRemove DO                     // O(E)
9.          tempGraph.removeEdge(edge)                         // O(1)
10. nodesToRemove = []                                         // O(1)
11. FOR each node in tempGraph.nodes DO                       // O(V)
12.     IF node.edges.count == 0 THEN                         // O(degree(node))
13.         nodesToRemove.add(node)                            // O(1)
14. FOR each node in nodesToRemove DO                         // O(V)
15.     tempGraph.removeNode(node)                             // O(1)
16. originNode = tempGraph.getNode(origin.name)               // O(1)
17. IF originNode == null THEN RETURN []                      // O(1)
18. targetNode = tempGraph.getNode(target.name)               // O(1)
19. IF targetNode == null THEN RETURN []                      // O(1)
20. distances = new HashMap()                                  // O(1)
21. predecessors = new HashMap()                               // O(1)
22. priorityQueue = new PriorityQueue()                       // O(1)
23. visited = new HashSet()                                    // O(1)
24. FOR each node in tempGraph.nodes DO                       // O(V)
25.     distances.put(node, INFINITY)                          // O(1)
26. distances.put(originNode, 0)                              // O(1)
27. priorityQueue.add(originNode)                             // O(log V)
28. WHILE priorityQueue is not empty DO                       // O(V) iterations
29.     currentNode = priorityQueue.poll()                     // O(log V)
30.     IF currentNode in visited THEN CONTINUE               // O(1)
31.     visited.add(currentNode)                               // O(1)
32.     FOR each edge in currentNode.edges DO                 // O(E) total across all iterations
33.         railway = getRailway(edge)                         // O(1)
34.         neighbor = edge.getOpposite(currentNode)           // O(1)
35.         IF neighbor in visited THEN CONTINUE              // O(1)
36.         newDistance = distances[currentNode] + railway.length  // O(1)
37.         IF newDistance < distances[neighbor] THEN          // O(1)
38.             distances[neighbor] = newDistance              // O(1)
39.             predecessors[neighbor] = currentNode           // O(1)
40.             priorityQueue.add(neighbor)                    // O(log V)
41. path = []                                                  // O(1)
42. step = targetNode                                          // O(1)
43. WHILE step in predecessors DO                             // O(V) worst case
44.     previous = predecessors[step]                          // O(1)
45.     edge = previous.getEdgeBetween(step)                   // O(1)
46.     path.add(getRailway(edge))                             // O(1)
47.     step = previous                                        // O(1)
48. REVERSE(path)                                              // O(V)
49. RETURN path                                                // O(1)
```

#### Complexity Analysis Table

| Line(s) | Operation                     | Time Complexity  | Explanation                                                 |
| ------- | ----------------------------- | ---------------- | ----------------------------------------------------------- |
| 1       | Graph cloning                 | O(V + E)         | Copy all vertices and edges                                 |
| 2       | Type check                    | O(1)             | Simple comparison                                           |
| 3       | List initialization           | O(1)             | Constant time                                               |
| 4-7     | Edge filtering                | O(E)             | Iterate through all edges                                   |
| 8-9     | Edge removal                  | O(E)             | Remove filtered edges                                       |
| 10      | List initialization           | O(1)             | Constant time                                               |
| 11-13   | Isolated node detection       | O(V + E)         | Check degree of each vertex                                 |
| 14-15   | Node removal                  | O(V)             | Remove isolated nodes                                       |
| 16-19   | Node existence check          | O(1)             | HashMap lookup                                              |
| 20-27   | Data structure initialization | O(V + log V)     | Initialize distances + priority queue                       |
| 28-40   | Main Dijkstra loop            | O((V + E) log V) | V iterations, E edge relaxations, log V per queue operation |
| 41-48   | Path reconstruction           | O(V)             | Backtrack through predecessors                              |

**Overall Time Complexity**: O((V + E) log V)

The dominant factor is the main Dijkstra loop where we process each vertex once and each edge once, with priority queue operations taking O(log V) time.

---

## US14 - Maintenance Route Covering Each Railway Line Exactly Once

### 1. Degree Map Construction

**Purpose**: Calculate the degree of each station in the railway graph.

#### Pseudocode

```
BUILD_DEGREE_MAP(onlyElectrified):
1.  degreeMap = new HashMap()                                  // O(1)
2.  FOR each railway in railwayRepository.getAllRailways() DO // O(E)
3.      IF onlyElectrified AND railway.type != Electrified THEN // O(1)
4.          CONTINUE                                           // O(1)
5.      degreeMap[railway.origin] += 1                        // O(1)
6.      degreeMap[railway.target] += 1                        // O(1)
7.  RETURN degreeMap                                           // O(1)
```

#### Complexity Analysis Table

| Line(s) | Operation                             | Time Complexity | Explanation            |
| ------- | ------------------------------------- | --------------- | ---------------------- |
| 1       | HashMap initialization                | O(1)            | Constant time          |
| 2-6     | Railway iteration and degree counting | O(E)            | Process each edge once |
| 7       | Return                                | O(1)            | Constant time          |

**Overall Time Complexity**: O(E)

### 2. Undirected Graph Construction

**Purpose**: Build an undirected graph representation of the railway system.

#### Pseudocode

```
BUILD_UNDIRECTED_GRAPH(onlyElectrified):
1.  graph = new HashMap()                                      // O(1)
2.  FOR each railway in railwayRepository.getAllRailways() DO // O(E)
3.      IF onlyElectrified AND railway.type != Electrified THEN // O(1)
4.          CONTINUE                                           // O(1)
5.      graph.putIfAbsent(railway.origin, new ArrayList())    // O(1)
6.      graph.putIfAbsent(railway.target, new ArrayList())    // O(1)
7.      graph[railway.origin].add(railway)                    // O(1)
8.      graph[railway.target].add(railway)                    // O(1)
9.  RETURN graph                                               // O(1)
```

#### Complexity Analysis Table

| Line(s) | Operation              | Time Complexity | Explanation                               |
| ------- | ---------------------- | --------------- | ----------------------------------------- |
| 1       | HashMap initialization | O(1)            | Constant time                             |
| 2-8     | Graph construction     | O(E)            | Process each edge, add to adjacency lists |
| 9       | Return                 | O(1)            | Constant time                             |

**Overall Time Complexity**: O(E)

### 3. DFS Connectivity Check

**Purpose**: Verify if the graph is connected using depth-first search.

#### Pseudocode

```
DFS_CONNECTIVITY_CHECK(graph, current, visited):
1.  visited.add(current)                                       // O(1)
2.  FOR each railway in graph[current] DO                     // O(degree(current))
3.      neighbor = (railway.origin == current) ?              // O(1)
                 railway.target : railway.origin
4.      IF neighbor NOT in visited THEN                       // O(1)
5.          DFS_CONNECTIVITY_CHECK(graph, neighbor, visited)  // Recursive call
```

#### Complexity Analysis Table

| Line(s) | Operation               | Time Complexity    | Explanation                        |
| ------- | ----------------------- | ------------------ | ---------------------------------- |
| 1       | Add to visited set      | O(1)               | HashSet add operation              |
| 2-3     | Neighbor identification | O(degree(current)) | Check all edges of current vertex  |
| 4-5     | Recursive traversal     | O(1) per call      | Condition check and recursive call |

**Overall Time Complexity**: O(V + E)

Each vertex is visited once, and each edge is traversed once.

### 4. Eulerian Trail Verification

**Purpose**: Determine if an Eulerian trail is possible.

#### Pseudocode

```
IS_EULERIAN_TRAIL_POSSIBLE(onlyElectrified):
1.  graph = BUILD_UNDIRECTED_GRAPH(onlyElectrified)           // O(E)
2.  visited = new HashSet()                                    // O(1)
3.  start = null                                               // O(1)
4.  FOR each entry in graph DO                                // O(V)
5.      IF entry.value is not empty THEN                      // O(1)
6.          start = entry.key                                  // O(1)
7.          BREAK                                              // O(1)
8.  IF start == null THEN RETURN false                        // O(1)
9.  DFS_CONNECTIVITY_CHECK(graph, start, visited)            // O(V + E)
10. FOR each entry in graph DO                                // O(V)
11.     IF entry.value is not empty AND entry.key NOT in visited THEN // O(1)
12.         RETURN false                                       // O(1)
13. oddCount = 0                                               // O(1)
14. FOR each edges in graph.values() DO                       // O(V)
15.     IF edges.size() % 2 != 0 THEN                         // O(1)
16.         oddCount++                                         // O(1)
17. RETURN oddCount == 0 OR oddCount == 2                     // O(1)
```

#### Complexity Analysis Table

| Line(s) | Operation                 | Time Complexity | Explanation                 |
| ------- | ------------------------- | --------------- | --------------------------- |
| 1       | Graph construction        | O(E)            | Build undirected graph      |
| 2-3     | Initialization            | O(1)            | Constant time operations    |
| 4-7     | Find starting vertex      | O(V)            | Iterate through vertices    |
| 8       | Null check                | O(1)            | Constant time               |
| 9       | Connectivity check        | O(V + E)        | DFS traversal               |
| 10-12   | Connectivity verification | O(V)            | Check all vertices visited  |
| 13      | Counter initialization    | O(1)            | Constant time               |
| 14-16   | Odd degree counting       | O(V)            | Check degree of each vertex |
| 17      | Eulerian condition check  | O(1)            | Constant time               |

**Overall Time Complexity**: O(V + E)

### 5. Bridge Detection

**Purpose**: Determine if an edge is a bridge in the graph.

#### Pseudocode

```
IS_BRIDGE(graph, station, railway):
1.  initialReachable = DFS_COUNT(graph, station, new HashSet()) // O(V + E)
2.  REMOVE_EDGE_FROM_GRAPH(graph, railway)                     // O(1)
3.  afterReachable = DFS_COUNT(graph, station, new HashSet())  // O(V + E)
4.  graph[railway.origin].add(railway)                         // O(1)
5.  graph[railway.target].add(railway)                         // O(1)
6.  RETURN afterReachable < initialReachable                   // O(1)
```

#### Complexity Analysis Table

| Line(s) | Operation                | Time Complexity | Explanation                 |
| ------- | ------------------------ | --------------- | --------------------------- |
| 1       | DFS count before removal | O(V + E)        | Count reachable vertices    |
| 2       | Edge removal             | O(1)            | Remove from adjacency lists |
| 3       | DFS count after removal  | O(V + E)        | Count reachable vertices    |
| 4-5     | Edge restoration         | O(1)            | Add back to adjacency lists |
| 6       | Comparison               | O(1)            | Compare counts              |

**Overall Time Complexity**: O(V + E)

### 6. DFS Count (Reachability)

**Purpose**: Count the number of reachable vertices from a given starting vertex.

#### Pseudocode

```
DFS_COUNT(graph, station, visited):
1.  visited.add(station)                                       // O(1)
2.  count = 1                                                  // O(1)
3.  edges = graph[station]                                     // O(1)
4.  IF edges != null THEN                                      // O(1)
5.      FOR each railway in edges DO                          // O(degree(station))
6.          neighbor = (railway.origin == station) ?          // O(1)
                     railway.target : railway.origin
7.          IF neighbor NOT in visited THEN                   // O(1)
8.              count += DFS_COUNT(graph, neighbor, visited)  // Recursive call
9.  RETURN count                                               // O(1)
```

#### Complexity Analysis Table

| Line(s) | Operation               | Time Complexity    | Explanation                        |
| ------- | ----------------------- | ------------------ | ---------------------------------- |
| 1-2     | Initialization          | O(1)               | Mark visited and initialize count  |
| 3-4     | Edge list access        | O(1)               | HashMap lookup                     |
| 5-6     | Neighbor identification | O(degree(station)) | Process all edges                  |
| 7-8     | Recursive traversal     | O(1) per call      | Condition check and recursive call |
| 9       | Return                  | O(1)               | Constant time                      |

**Overall Time Complexity**: O(V + E)

### 7. Eulerian Path Construction (Fleury's Algorithm)

**Purpose**: Construct an Eulerian path using Fleury's algorithm.

#### Pseudocode

```
FIND_MAINTENANCE_PATH_FROM_STATION(onlyElectrified, startStation):
1.  IF NOT IS_EULERIAN_TRAIL_POSSIBLE(onlyElectrified) THEN   // O(V + E)
2.      RETURN empty list                                      // O(1)
3.  graph = BUILD_UNDIRECTED_GRAPH(onlyElectrified)           // O(E)
4.  path = new ArrayList()                                     // O(1)
5.  DFS_FLEURY_UNDIRECTED(graph, startStation, path)         // O(E × (V + E))
6.  REVERSE(path)                                              // O(E)
7.  RETURN path                                                // O(1)
```

#### Complexity Analysis Table

| Line(s) | Operation           | Time Complexity | Explanation                     |
| ------- | ------------------- | --------------- | ------------------------------- |
| 1       | Eulerian check      | O(V + E)        | Verify if Eulerian trail exists |
| 2       | Early return        | O(1)            | Return empty list               |
| 3       | Graph construction  | O(E)            | Build undirected graph          |
| 4       | Path initialization | O(1)            | Create empty list               |
| 5       | Fleury's algorithm  | O(E × (V + E))  | Main pathfinding algorithm      |
| 6       | Path reversal       | O(E)            | Reverse the path                |
| 7       | Return              | O(1)            | Return result                   |

**Overall Time Complexity**: O(E × (V + E))

### 8. DFS Fleury (Recursive)

**Purpose**: Perform the recursive DFS traversal for Fleury's algorithm.

#### Pseudocode

```
DFS_FLEURY_UNDIRECTED(graph, station, path):
1.  edges = graph[station]                                     // O(1)
2.  IF edges == null THEN RETURN                              // O(1)
3.  WHILE edges is not empty DO                               // O(degree(station)) iterations
4.      nextRailway = null                                     // O(1)
5.      IF edges.size() == 1 THEN                             // O(1)
6.          nextRailway = edges[0]                             // O(1)
7.      ELSE                                                   // O(1)
8.          FOR each railway in edges DO                      // O(degree(station))
9.              IF NOT IS_BRIDGE(graph, station, railway) THEN // O(V + E)
10.                 nextRailway = railway                      // O(1)
11.                 BREAK                                      // O(1)
12.         IF nextRailway == null THEN                       // O(1)
13.             nextRailway = edges[0]                         // O(1)
14.     REMOVE_EDGE_FROM_GRAPH(graph, nextRailway)            // O(1)
15.     nextStation = (nextRailway.origin == station) ?       // O(1)
                      nextRailway.target : nextRailway.origin
16.     DFS_FLEURY_UNDIRECTED(graph, nextStation, path)      // Recursive call
17.     path.add(nextRailway)                                  // O(1)
```

#### Complexity Analysis Table

| Line(s) | Operation                     | Time Complexity              | Explanation                         |
| ------- | ----------------------------- | ---------------------------- | ----------------------------------- |
| 1-2     | Edge access and null check    | O(1)                         | HashMap lookup                      |
| 3       | While loop condition          | O(degree(station))           | Loop until no edges                 |
| 4-6     | Single edge case              | O(1)                         | Direct selection                    |
| 7-13    | Bridge avoidance              | O(degree(station) × (V + E)) | Check each edge for bridge property |
| 14-15   | Edge removal and next station | O(1)                         | Remove edge and find next station   |
| 16      | Recursive call                | -                            | Recursive traversal                 |
| 17      | Add to path                   | O(1)                         | Add edge to result path             |

**Overall Time Complexity**: O(E × (V + E))

The algorithm processes each edge once, but for each edge, it may need to perform bridge detection which takes O(V + E) time.

---

## Summary

| Algorithm                              | Worst-Case Time Complexity | Space Complexity |
| -------------------------------------- | -------------------------- | ---------------- |
| **US13 - Dijkstra's Algorithm**        | O((V + E) log V)           | O(V + E)         |
| **US14 - Degree Map Construction**     | O(E)                       | O(V)             |
| **US14 - Graph Construction**          | O(E)                       | O(V + E)         |
| **US14 - Connectivity Check (DFS)**    | O(V + E)                   | O(V)             |
| **US14 - Eulerian Trail Verification** | O(V + E)                   | O(V + E)         |
| **US14 - Bridge Detection**            | O(V + E)                   | O(V)             |
| **US14 - Fleury's Algorithm**          | O(E × (V + E))             | O(V + E)         |

**Legend:**

- V = Number of vertices (stations)
- E = Number of edges (railway lines)

**Key Observations:**

1. **US13 (Dijkstra)** has optimal complexity for single-source shortest path problems
2. **US14 (Fleury)** has higher complexity due to bridge detection for each edge, making it less efficient for large graphs
3. All algorithms have reasonable space complexity proportional to the input size
