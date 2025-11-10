# clos-topology-analyzer
A Java-based analyzer for Clos (Fat-Tree) datacenter network topologies.

This project programmatically constructs both **two-layer (folded Clos)** and **three-layer (fat-tree)** networks and computes key structural and performance characteristics such as:

- **Switch counts** and scalability with switch degree `d`
- **Redundant paths** between server pairs
- **Bisection bandwidth** across network partitions

---

## Features

- **Two-layer and three-layer topology generation**
  - Automatically builds pods, edge, aggregation, and core layers based on switch degree and server count.
- **Path analysis**
  - Enumerates unique paths between arbitrary server pairs to quantify redundancy.
- **Bisection bandwidth analyzer**
  - Computes total bandwidth between two halves of the network.
- **Switch and link statistics**
  - Counts total switches and verifies theoretical relationships between `d`, number of pods, and servers supported.

---

## Implementation Overview

- **Language:** Java
- **Core classes:**
  - `ClosTopology`: Constructs and wires the network
  - `PathAnalyzer`: Enumerates redundant paths
  - `BisectionAnalyzer`: Calculates bisection bandwidth
  - `SwitchAnalyzer`: Reports switch counts and hierarchy
- **Execution:**
  ```bash
  javac src/*.java
  java -cp src Main

---

## Example Output
```yaml
Three-Layer Topology Switch Analysis:
Switch Counts:
  Edge switches:        128
  Aggregation switches: 128
  Core switches:        128
  Total:                384

Three-layer bisection bandwidth: 512
Two-layer bisection bandwidth: 64
```

---

## Authors
Lebin Huang - Three-layer topology, constructor algorithm.
Andrew Yang - Two-layer toppology, analyzer modules, writeup, testing.

---

## Acknowledgements
Feedback from course staff:
> "Nice job! This project demonstrates a thorough understanding of Clos networks and a significant implementation effort to model and analyze them."

Referenced materials
* [Demystifying DCN Topologies](https://packetpushers.net/blog/demystifying-dcn-topologies-clos-fat-trees-part1/)
* [CS168: The Datacenter Topology Chapter](https://textbook.cs168.io/datacenter/topology.html)
