# Algorithm Complexity Analysis Report

## US27 – Caminho mais curto entre duas estações com paragens intermédias ordenadas

Este relatório analisa a complexidade dos algoritmos utilizados para calcular um dos caminhos mais curtos entre duas estações, passando por uma lista ordenada de estações intermédias.

---

## 1. getPathWithIntermediateStops

**Objetivo:**  
Construir o caminho mais curto entre a origem e o destino, passando por uma lista ordenada de estações intermédias, concatenando os caminhos mais curtos entre cada par consecutivo.

### Pseudocódigo
```
GET_PATH_WITH_INTERMEDIATE_STOPS(origin, target, intermediateList):
stationList ← [] // O(1)
stationList.add(origin) // O(1)
PARA cada s EM intermediateList FAÇA // O(k)
 stationList.add(s)                           // O(1)
stationList.add(target) // O(1)
path ← [] // O(1)
PARA i DE 1 ATÉ stationList.size-1 FAÇA // O(k)
 tempOrigin ← stationList[i-1]                // O(1)
 tempTarget ← stationList[i]                  // O(1)
 tempPath ← DIJKSTRA(null, tempOrigin, tempTarget, false, true) // O((V+E) log V)
 PARA j DE 0 ATÉ tempPath.size-1 FAÇA         // O(V)
    SE i == stationList.size-1 OU j != tempPath.size-1 ENTÃO
        path.add(tempPath[j])                // O(1)
RETORNAR path // O(1)
```


- k = número de estações na rota (origem + intermédias + destino)
- V = número de estações no grafo
- E = número de linhas ferroviárias no grafo

### Análise de Complexidade

| Linhas | Operação                    | Complexidade   | Observações                                                                                   |
|--------|-----------------------------|----------------|-----------------------------------------------------------------------------------------------|
| 1–5    | Construção da stationList   | O(k)           | Adiciona cada estação (origem, intermédias e destino) sequencialmente à lista                 |
| 6      | Inicialização do path       | O(1)           | Criação de uma lista vazia                                                                    |
| 7      | Loop externo (segmentos)    | O(k)           | Executa para cada par consecutivo de estações na rota (k-1 segmentos)                         |
| 10     | Chamada do Dijkstra         | O((V+E) log V) | Para cada segmento, executa o algoritmo de Dijkstra sobre o grafo completo                    |
| 11     | Loop interno (concatenação) | O(V)           | No pior caso, concatena todos os vértices do caminho temporário (caminho máximo de tamanho V) |
| 13     | Inserção na lista           | O(1)           | Adiciona cada estação ao caminho final                                                        |
| 14     | Retorno                     | O(1)           | Devolve a lista final                                                                         |

**Complexidade Total:**  
O( k * ( (V+E) log V + V ) ) ≈ **O( k * (V+E) log V )**

---

## 2. Dijkstra

**Objetivo:**  
Encontrar o caminho mais curto entre duas estações num grafo, podendo filtrar linhas e remover nós isolados.

### Pseudocódigo
```
DIJKSTRA(train, origin, target, displayGraph, removeAloneNodes):
tempGraph ← GET_GRAPH_FOR_TRAIN(train, removeAloneNodes) // O(V+E)
originNode ← tempGraph.getNode(origin) // O(1)
SE originNode == null ENTÃO RETORNAR [] // O(1)
targetNode ← tempGraph.getNode(target) // O(1)
SE targetNode == null ENTÃO RETORNAR [] // O(1)
PARA cada node EM tempGraph.nodes FAÇA // O(V)
 distances[node] ← ∞                                  // O(1)
distances[originNode] ← 0 // O(1)
priorityQueue ← nova PriorityQueue(distances) // O(1)
priorityQueue.add(originNode) // O(log V)
visited ← ∅ // O(1)
ENQUANTO priorityQueue não vazia FAÇA // O(V) iterações
 currentNode ← priorityQueue.poll()                   // O(log V)
 SE !visited.add(currentNode) CONTINUE                // O(1)
 PARA cada edge EM currentNode.edges FAÇA             // O(E) total
    neighbor ← edge.getOpposite(currentNode)         // O(1)
    SE neighbor ∈ visited CONTINUE                   // O(1)
    newDistance ← distances[currentNode] + edge.length // O(1)
    SE newDistance < distances[neighbor] ENTÃO
        distances[neighbor] ← newDistance            // O(1)
        predecessors[neighbor] ← currentNode         // O(1)
        priorityQueue.add(neighbor)                  // O(log V)
path ← [target] // O(1)
step ← targetNode // O(1)
ENQUANTO step ∈ predecessors FAÇA // O(V)
 previous ← predecessors[step]                       // O(1)
 path.add(previous)                                  // O(1)
 step ← previous                                     // O(1)
REVERSE(path) // O(V)
RETORNAR path // O(1)
```


### Análise de Complexidade

| Linhas | Operação                 | Complexidade   | Observações                                                                                  |
|--------|--------------------------|----------------|----------------------------------------------------------------------------------------------|
| 1      | Clonagem/filtragem grafo | O(V+E)         | Cria uma cópia do grafo e remove arestas/nós conforme o tipo de comboio e opção de filtragem |
| 2–5    | Verificação de nós       | O(1)           | Verifica se os nós de origem e destino existem no grafo                                      |
| 6–8    | Inicialização distâncias | O(V)           | Inicializa o valor de distância para todos os nós                                            |
| 9–11   | Estruturas auxiliares    | O(1)           | Criação da fila de prioridade e do conjunto de visitados                                     |
| 12–22  | Loop principal Dijkstra  | O((V+E) log V) | Para cada nó, remove da fila de prioridade (O(log V)) e processa todas as arestas adjacentes |
| 23–29  | Reconstrução do caminho  | O(V)           | Percorre a lista de predecessores para reconstruir o caminho do destino até à origem         |
| 30     | Retorno                  | O(1)           | Devolve o caminho calculado                                                                  |

**Complexidade Total:**  
**O((V+E) log V)**

---

## 3. getGraphForTrain

**Objetivo:**  
Clonar o grafo base, remover linhas incompatíveis com o tipo de comboio e eliminar nós isolados.

### Pseudocódigo
```
GET_GRAPH_FOR_TRAIN(train, removeAloneNodes):
tempGraph ← clone(railwayGraph) // O(V+E)
SE train ≠ null E train.type == Electric ENTÃO
 PARA cada edge EM tempGraph.edges FAÇA           // O(E)
     SE edge.type == Non_Electrified ENTÃO
         tempGraph.removeEdge(edge)               // O(1)
SE removeAloneNodes ENTÃO
 PARA cada node EM tempGraph.nodes FAÇA           // O(V)
     SE node.degree == 0 ENTÃO
         tempGraph.removeNode(node)               // O(1)
RETORNAR tempGraph // O(1)
```


### Análise de Complexidade

| Linhas | Operação                | Complexidade | Observações                                                                         |
|--------|-------------------------|--------------|-------------------------------------------------------------------------------------|
| 1      | Clonagem do grafo       | O(V+E)       | Cria uma cópia completa do grafo original                                           |
| 2–5    | Remoção de arestas      | O(E)         | Percorre todas as arestas para remover as não compatíveis com o tipo de comboio     |
| 6–9    | Remoção de nós isolados | O(V)         | Percorre todos os nós para remover aqueles que ficaram sem arestas após a filtragem |
| 10     | Retorno                 | O(1)         | Devolve o grafo filtrado                                                            |

**Complexidade Total:**  
**O(V+E)**

---

## Resumo

| Algoritmo                    | Complexidade Temporal Pior Caso | Complexidade Espacial |
|------------------------------|---------------------------------|-----------------------|
| getPathWithIntermediateStops | O(k * (V+E) log V)              | O(V+E+k)              |
| Dijkstra                     | O((V+E) log V)                  | O(V+E)                |
| getGraphForTrain             | O(V+E)                          | O(V+E)                |

- k = número de estações na rota (origem + intermédias + destino)
- V = número de estações (nós) no grafo
- E = número de linhas ferroviárias (arestas) no grafo

## Nota
- Todos os procedimentos de visualização gráfica foram excluídos desta análise.

---