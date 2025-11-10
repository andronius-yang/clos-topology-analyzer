
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
    }
}