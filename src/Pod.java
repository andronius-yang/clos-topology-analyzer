import java.util.*;

class Pod {
    private final int id;
    private final List<EdgeSwitch> edgeSwitches = new ArrayList<>();
    private final List<AggregationSwitch> aggregationSwitches = new ArrayList<>();
    
    public Pod(int id) {
        this.id = id;
    }
    
    public void addEdgeSwitch(EdgeSwitch sw) { edgeSwitches.add(sw); }
    public void addAggregationSwitch(AggregationSwitch sw) { aggregationSwitches.add(sw); }
    
    public List<EdgeSwitch> getEdgeSwitches() { return edgeSwitches; }
    public List<AggregationSwitch> getAggregationSwitches() { return aggregationSwitches; }
    public int getId() { return id; }
}
