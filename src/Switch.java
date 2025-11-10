import java.util.*;

abstract class Switch {
    protected final String id;
    protected final List<Connection> connections = new ArrayList<>();
    private int capacity = 0;

    public Switch(String id) {
        this.id = id;
    }

    public void addConnection(String destId, int bandwidth) {
        connections.add(new Connection(this.id, destId, bandwidth));
        capacity += bandwidth;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getId() {
        return id;
    }
}