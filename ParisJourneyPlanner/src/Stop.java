import java.util.ArrayList;

public class Stop {
    private String stop_name;
    private ArrayList<Route> routes = new ArrayList<>();
    public Stop(String name)
    {
        stop_name = name;
    }

    public String getStop_name() {
        return stop_name;
    }
    public void setStop_name(String stop_name) {
        this.stop_name = stop_name;
    }
    public ArrayList<Route> getRoutes() {
        return routes;
    }
    public void addRoute(Route route) {
        if(!routes.contains(route))
            routes.add(route);
    }
    public boolean equals(Object obj)
    {
        Stop stop = (Stop)obj;
        return stop_name.equals(stop.stop_name);
    }
    @Override
    public String toString() {
        return stop_name + routes.toString();
    }
}
