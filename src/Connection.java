class Connection {
    private final String sourceId;
    private final String destId;
    private final int bandwidth;

    public Connection(String sourceId, String destId, int bandwidth) {
        this.sourceId = sourceId;
        this.destId = destId;
        this.bandwidth = bandwidth;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getDestId() {
        return destId;
    }

    public int getBandwidth() {
        return bandwidth;
    }
}