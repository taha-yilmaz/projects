public class Route {
    private String route_short_name;
    private String route_long_name;
    public Route(String routeShortName, String routeLongName){
        route_short_name = routeShortName;
        route_long_name = routeLongName;
    }
    public String getRoute_short_name() {
        return route_short_name;
    }

    public String getRoute_long_name() {
        return route_long_name;
    }

    public void setRoute_short_name(String route_short_name) {
        this.route_short_name = route_short_name;
    }

    public void setRoute_long_name(String route_long_name) {
        this.route_long_name = route_long_name;
    }

    @Override
    public boolean equals(Object obj) {
        Route route = (Route) obj;
        return route_short_name.equals(route.route_short_name);
    }

    @Override
    public String toString() {
        return route_short_name;
    }
}
