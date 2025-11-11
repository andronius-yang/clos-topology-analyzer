
// Tests
public class Main {
    public static void main(String[] args) {
        ClosTopology threeLayer = new ClosTopology(1024, 16, 1, true);
        // for (int i = 0; i < 1024; i++){
        //     System.out.println("Paths between S-0 and S-" + i + ": "  + 
        //         PathAnalyzer.countPaths(threeLayer, "S-0", "S-" + i)); 
        // }
        ClosTopology twoLayer = new ClosTopology(128, 16, 1, false);
        // for (int i = 0; i < 128; i++) {
        //     System.out.println("Paths between S-0 and S-" + i + ": " +
        //             PathAnalyzer.countPaths(twoLayer, "S-0", "S-" + i));
        // }
        System.out.println("\nThree-Layer Topology Switch Analysis:");
        SwitchAnalyzer.countSwitches(threeLayer);

        System.out.println("\nTwo-Layer Topology Switch Analysis:");
        SwitchAnalyzer.countSwitches(twoLayer);

        System.out.println(
                "Three-layer bisection bandwidth: " + BisectionAnalyzer.calculateBisectionBandwidth(threeLayer));
        System.out.println(
                "Two-layer bisection bandwidth: " + BisectionAnalyzer.calculateBisectionBandwidth(twoLayer));

        // stress test
        int d = 20;
        int n = (d * d * d) / 4; // 2000
        ClosTopology threeLayerLarge = new ClosTopology(n, d, 1, true);

        System.out.println("\nThree-Layer Topology Switch Analysis (d=20, n=2000):");
        SwitchAnalyzer.countSwitches(threeLayerLarge);

        int bw = BisectionAnalyzer.calculateBisectionBandwidth(threeLayerLarge);
        int expectedBw = (d * d * d) / 8; // d^3/8 w/ r=1
        System.out.println("Three-layer bisection bandwidth (d=20): " + bw + " (expected " + expectedBw + ")");
    }

}
