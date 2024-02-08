public class Road<T> {
    private T length;
    private Route route;
    public Road(T length, Route route)
    {
        this.length = length;
        this.route = route;
    }

    public T getLength() {
        return length;
    }
    public Route getRoute() {
        return route;
    }

    public void setLength(T length) {
        this.length = length;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    @Override
    public String toString() {
        return route.getRoute_long_name();
    }

}
