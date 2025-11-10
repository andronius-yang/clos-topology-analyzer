class PathAnalyzer {
    public static int countPaths(ClosTopology topology, String srcServerId, String destServerId) {
        EdgeSwitch srcEdge = topology.getEdgeSwitchForServer(srcServerId);
        EdgeSwitch destEdge = topology.getEdgeSwitchForServer(destServerId);
        if (srcEdge == null || destEdge == null) return 0;

        Pod srcPod = findPodForEdgeSwitch(topology, srcEdge);
        Pod destPod = findPodForEdgeSwitch(topology, destEdge);
        if (srcPod == null || destPod == null) return 0;

        // check if source and destination are in the same pod
        if (srcPod.getId() == destPod.getId()) {
            // check if servers are on the same edge switch
            if (srcEdge.getId().equals(destEdge.getId())) {
                return 1;
            } else {
                // path via aggregation
                return srcPod.getAggregationSwitches().size();
            }
        } else {
            return countCrossPodPaths(topology, srcPod, destPod, destEdge);
        }
    }

    private static int countCrossPodPaths(ClosTopology topology, Pod srcPod, Pod destPod, EdgeSwitch destEdge) {
        int pathCount = 0;

        if (srcPod.getAggregationSwitches().isEmpty()) {
            EdgeSwitch srcEdge = srcPod.getEdgeSwitches().get(0);
            // iterate through all core switches connected to the source edge
            for (Connection srcEdgeConn : srcEdge.getConnections()) {
                Switch coreSwitch = topology.findSwitchById(srcEdgeConn.getDestId());
                if (coreSwitch instanceof CoreSwitch) {
                    // check if the core switch connects to the destination edge
                    for (Connection coreConn : coreSwitch.getConnections()) {
                        if (coreConn.getDestId().equals(destEdge.getId())) {
                            pathCount++;
                            break; // count once per core switch
                        }
                    }
                }
            }
        } else {
            // iterate through all aggregation switches in the source pod
            for (AggregationSwitch srcAgg : srcPod.getAggregationSwitches()) {
                // find core switches connected to this source aggregation
                for (Connection conn : srcAgg.getConnections()) {
                    Switch sw = topology.findSwitchById(conn.getDestId());
                    if (!(sw instanceof CoreSwitch))
                        continue;
                    CoreSwitch core = (CoreSwitch) sw;

                    // find destination aggregation switches connected to this core
                    for (Connection coreConn : core.getConnections()) {
                        Switch destSw = topology.findSwitchById(coreConn.getDestId());
                        if (!(destSw instanceof AggregationSwitch))
                            continue;
                        AggregationSwitch destAgg = (AggregationSwitch) destSw;

                        // check if this aggregation switch is in the destination pod
                        if (isAggregationInPod(destAgg, destPod)) {
                            // check if destination aggregation is connected to destination edge
                            if (isConnected(destAgg, destEdge)) {
                                pathCount++;
                            }
                        }
                    }
                }
            }
        }
        return pathCount;
    }

    private static boolean isAggregationInPod(AggregationSwitch agg, Pod pod) {
        return pod.getAggregationSwitches().stream()
            .anyMatch(a -> a.getId().equals(agg.getId()));
    }

    private static boolean isConnected(Switch from, Switch to) {
        return from.getConnections().stream()
            .anyMatch(conn -> conn.getDestId().equals(to.getId()));
    }

    private static Pod findPodForEdgeSwitch(ClosTopology topology, EdgeSwitch edge) {
        for (Pod pod : topology.getPods()) {
            if (pod.getEdgeSwitches().stream()
                .anyMatch(e -> e.getId().equals(edge.getId()))) {
                return pod;
            }
        }
        return null;
    }
}