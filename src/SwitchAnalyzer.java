class SwitchAnalyzer {
    public static void countSwitches(ClosTopology topology) {
        int edgeCount = 0;
        int aggregationCount = 0;
        int coreCount = 0;

        // Count core switches
        coreCount = topology.getCoreSwitches().size();

        // Count edge and aggregation switches from each pod
        for (Pod pod : topology.getPods()) {
            edgeCount += pod.getEdgeSwitches().size();
            aggregationCount += pod.getAggregationSwitches().size();
        }

        System.out.println("Switch Counts:");
        System.out.println("  Edge switches:        " + edgeCount);
        System.out.println("  Aggregation switches: " + aggregationCount);
        System.out.println("  Core switches:        " + coreCount);
        System.out.println("  Total:                " + (edgeCount+coreCount+aggregationCount));
    }
}