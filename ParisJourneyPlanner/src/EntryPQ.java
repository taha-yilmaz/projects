public class EntryPQ<T> implements Comparable<EntryPQ<T>> {
    private IVertex<T> vertex;
    private double cost;
    private IVertex<T> predecessor;

    public EntryPQ(IVertex<T> vertex, double cost, IVertex<T> predecessor) {
        this.vertex = vertex;
        this.cost = cost;
        this.predecessor = predecessor;
    }

    public IVertex<T> getVertex() {
        return vertex;
    }

    public double getCost() {
        return cost;
    }

    public IVertex<T> getPredecessor() {
        return predecessor;
    }

    @Override
    public int compareTo(EntryPQ<T> other) {
        return Double.compare(this.cost, other.cost);
    }
}
