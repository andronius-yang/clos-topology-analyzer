import java.util.*;

class EdgeSwitch extends Switch {
    private final List<Server> servers = new ArrayList<>();
    
    public EdgeSwitch(String id) {
        super(id);
    }
    
    public void connectServer(Server server) {
        servers.add(server);
    }
    
    public List<Server> getServers() {
        return servers;
    }
}