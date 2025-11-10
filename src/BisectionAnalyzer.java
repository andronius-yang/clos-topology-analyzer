import java.util.*;

class BisectionAnalyzer {

    public static int calculateBisectionBandwidth(ClosTopology topology) {
        List<String> serverIds = new ArrayList<>(topology.getServerToEdgeMap().keySet());
        int totalServers = serverIds.size();
        if (totalServers == 0) return 0;

        List<String> groupA = serverIds.subList(0, totalServers / 2);

        // Collect all edge switches connected to group A servers
        Set<EdgeSwitch> edgeSwitchesA = new HashSet<>();
        for (String serverId : groupA) {
            EdgeSwitch edge = topology.getEdgeSwitchForServer(serverId);
            if (edge != null) {
                edgeSwitchesA.add(edge);
            }
        }

        int bisectionBandwidth = 0;

        if (topology.isThreeLayer()) {
            // three-layer, find all aggregation switches in the same pods
            Set<Pod> podsA = new HashSet<>();
            for (EdgeSwitch edge : edgeSwitchesA) {
                for (Pod pod : topology.getPods()) {
                    if (pod.getEdgeSwitches().contains(edge)) {
                        podsA.add(pod);
                        break;
                    }
                }
            }

            // sum connections from aggregation switches in podsA to core switches
            for (Pod pod : podsA) {
                for (AggregationSwitch agg : pod.getAggregationSwitches()) {
                    for (Connection conn : agg.getConnections()) {
                        if (conn.getDestId().startsWith("C")) { // Core switch connection
                            bisectionBandwidth += conn.getBandwidth();
                        }
                    }
                }
            }
        } else {
            // two-layer, sum connections from edge switches to core switches
            for (EdgeSwitch edge : edgeSwitchesA) {
                for (Connection conn : edge.getConnections()) {
                    if (conn.getDestId().startsWith("C")) { // Core switch connection
                        bisectionBandwidth += conn.getBandwidth();
                    }
                }
            }
        }

        return bisectionBandwidth;
    }
}