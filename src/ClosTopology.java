import java.util.*;

public class ClosTopology {
    private final int numServers;
    private final int switchDegree;
    private final int serverBandwidth;
    private final List<Pod> pods = new ArrayList<>();
    private final List<CoreSwitch> coreSwitches = new ArrayList<>();
    private final Map<String, EdgeSwitch> serverToEdgeMap = new LinkedHashMap<>();
    private boolean isThreeLayer = true;

    public ClosTopology(int numServers, int switchDegree, int serverBandwidth, boolean isThreeLayer) {
        this.numServers = numServers;
        this.switchDegree = switchDegree;
        this.serverBandwidth = serverBandwidth;
        this.isThreeLayer = isThreeLayer;
        buildTopology();
    }
    
    private void buildTopology() {
        if (isThreeLayer) {
            buildThreeLayerTopology();
        } else {
            buildTwoLayerTopology();
        }
    }

    private void buildThreeLayerTopology() {
        int serversPerPod = (switchDegree * switchDegree) / 4;
        int numPods = (int) Math.ceil((double) numServers / serversPerPod);
        
        // pods and switches
        for (int podId = 0; podId < numPods; podId++) {
            Pod pod = new Pod(podId);
            // edge/aggregation switches
            for (int i = 0; i < switchDegree / 2; i++) {
                EdgeSwitch edge = new EdgeSwitch("E-" + podId + "-" + i);
                AggregationSwitch agg = new AggregationSwitch("A-" + podId + "-" + i);
                pod.addEdgeSwitch(edge);
                pod.addAggregationSwitch(agg);
            }
            pods.add(pod);
        }
        
        // core switches
        int numCoreGroups = switchDegree / 2;
        int coresPerGroup = switchDegree / 2;
        for (int i = 0; i < numCoreGroups; i++) {
            for (int j = 0; j < coresPerGroup; j++) {
                coreSwitches.add(new CoreSwitch("C-" + i + "-" + j));
            }
        }
        
        wireIntraPodConnections();
        wireCoreConnections();
        connectServers();
    }

    private void buildTwoLayerTopology() {
        int serversPerEdge = switchDegree / 2;
        int numEdges = (int) Math.ceil((double) numServers / serversPerEdge);

        // pods (each with one edge switch)
        for (int podId = 0; podId < numEdges; podId++) {
            Pod pod = new Pod(podId);
            EdgeSwitch edge = new EdgeSwitch("E-" + podId);
            pod.addEdgeSwitch(edge);
            pods.add(pod);
        }

        // core switches (d/2 cores)
        int numCore = switchDegree / 2;
        for (int i = 0; i < numCore; i++) {
            coreSwitches.add(new CoreSwitch("C-" + i));
        }

        wireEdgeToCore();
        connectServers();
    }
    
    private void wireEdgeToCore() {
        for (Pod pod : pods) {
            EdgeSwitch edge = pod.getEdgeSwitches().get(0);
            for (CoreSwitch core : coreSwitches) {
                edge.addConnection(core.getId(), serverBandwidth);
                core.addConnection(edge.getId(), serverBandwidth);
            }
        }
    }

    // edge to aggregation switches in pod
    private void wireIntraPodConnections() {
        for (Pod pod : pods) {
            for (EdgeSwitch edge : pod.getEdgeSwitches()) {
                for (AggregationSwitch agg : pod.getAggregationSwitches()) {
                    edge.addConnection(agg.getId(), serverBandwidth);
                    agg.addConnection(edge.getId(), serverBandwidth);
                }
            }
        }
    }
    
    private void wireCoreConnections() {
        int numCoreGroups = switchDegree / 2;
        int coresPerGroup = switchDegree / 2;

        // For each pod, connect aggregation switches to their core groups
        for (Pod pod : pods) {
            List<AggregationSwitch> aggs = pod.getAggregationSwitches();
            for (int aggIndex = 0; aggIndex < aggs.size(); aggIndex++) {
                AggregationSwitch agg = aggs.get(aggIndex);
                // Connect to all cores in group "aggIndex"
                for (int j = 0; j < coresPerGroup; j++) {
                    String coreId = "C-" + aggIndex + "-" + j;
                    CoreSwitch core = findCoreSwitchById(coreId);
                    if (core != null) {
                        agg.addConnection(core.getId(), serverBandwidth);
                        core.addConnection(agg.getId(), serverBandwidth);
                    }
                }
            }
        }
    }
    
    public Switch findSwitchById(String switchId) {
        for (Pod pod : pods) {
            for (EdgeSwitch sw : pod.getEdgeSwitches()) {
                if (sw.getId().equals(switchId)) return sw;
            }
            for (AggregationSwitch sw : pod.getAggregationSwitches()) {
                if (sw.getId().equals(switchId)) return sw;
            }
        }
        for (CoreSwitch sw : coreSwitches) {
            if (sw.getId().equals(switchId)) return sw;
        }
        return null;
    }
    
    private void connectServers() {
        int serverId = 0;
        for (Pod pod : pods) {
            List<EdgeSwitch> edgeSwitches = pod.getEdgeSwitches();
            for (EdgeSwitch edge : edgeSwitches) {
                for (int i = 0; i < switchDegree / 2; i++) {
                    if (serverId >= numServers)
                        return;
                    Server server = new Server("S-" + serverId);
                    edge.connectServer(server);
                    serverToEdgeMap.put(server.getId(), edge);
                    edge.addConnection(server.getId(), serverBandwidth);
                    serverId++;
                }
            }
        }
    }
    
    private List<AggregationSwitch> getAllAggregationSwitches() {
        List<AggregationSwitch> allAgg = new ArrayList<>();
        for (Pod pod : pods) allAgg.addAll(pod.getAggregationSwitches());
        return allAgg;
    }

    private CoreSwitch findCoreSwitchById(String id) {
        return coreSwitches.stream()
            .filter(c -> c.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    
    // test getters
    public List<Pod> getPods() { return pods; }
    public List<CoreSwitch> getCoreSwitches() { return coreSwitches; }
    public EdgeSwitch getEdgeSwitchForServer(String serverId) {
        return serverToEdgeMap.get(serverId);
    }
    public boolean isThreeLayer() {return isThreeLayer;}
    public Map<String, EdgeSwitch> getServerToEdgeMap() {return serverToEdgeMap;}
    
}
