import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
public class Vertex<T> implements IVertex<T> {
    private T name;
    private ArrayList<Edge> edges; // Edges to neighbors private boolean visited;
    private boolean visited; // True if visited
    private IVertex<T> parent;// On path to this vertex
    private double cost;// Of path to this vertex
    public Vertex(T vertexLabel) {
        name = vertexLabel;
        edges = new ArrayList<>();
        visited = false;
        parent = null;
        cost = 0;
    }
    public void addEdge(Edge e) {
        edges.add(e);
    }
    public ArrayList<Edge> getEdges() {
        return this.edges;
    }
    public T getName() {
        return name;
    }
    public void setName(T name) {
        this.name = name;
    }
    public IVertex<T> getParent() {
        return parent;
    }
    public double getCost() {
        return cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }
    public void visit() {
        this.visited = true;
    }
    public void unvisit() {
        this.visited = false;
    }
    public boolean isVisited() {
        return this.visited;
    }
    public boolean connect (IVertex<T> endVertex, double edgeWeight) {
        boolean result = false;
        if (!this.equals(endVertex)) {// Vertices are distinct
            Iterator<IVertex<T>> neighbors = getNeighborIterator();
            boolean duplicateEdge = false;
            while (!duplicateEdge && neighbors.hasNext()) {
                IVertex<T> nextNeighbor = neighbors.next();
                if (endVertex.equals(nextNeighbor))
                    duplicateEdge = true;
            }
            if (!duplicateEdge) {
                edges.add(new Edge(this, (Vertex<T>) endVertex, edgeWeight));
                result = true;
            }
        }
        return result;
    }
    @Override
    public boolean connect(IVertex<T> endVertex) {
        return connect(endVertex, 0.0);
    }
    public IVertex<T> getUnvisitedNeighbor() {
        Vertex<T> result = null;

        Iterator<IVertex<T>> neighbors = getNeighborIterator();
        while (neighbors.hasNext() && (result == null)) {
            Vertex<T> nextNeighbor = (Vertex<T>) neighbors.next();
            if (!nextNeighbor.isVisited())
                result = nextNeighbor;
        } // end while

        return result;
    }
    public boolean hasNeighbor() {
        return !edges.isEmpty();
    }
    public boolean hasEdge(T neighbor) {
        boolean found = false;
        Iterator<IVertex<T>> neighbors = getNeighborIterator();
        while (neighbors.hasNext()) {
            Vertex<T> nextNeighbor = (Vertex<T>) neighbors.next();
            if (nextNeighbor.getName().equals(neighbor)) {
                found = true;
                break;
            }
        } // end while
        return found;
    }
    public void setPredecessor(IVertex<T> predecessor) {
        this.parent = predecessor;
    }
    public IVertex<T> getPredecessor() {
        return this.parent;
    }
    public boolean hasPredecessor() {
        return this.parent != null;
    }
    public Iterator<IVertex<T>> getNeighborIterator() {
        return new NeighborIterator();
    } // end getNeighborIterator
    public Iterator getWeightIterator() {return new WeightIterator();}
    private class NeighborIterator implements Iterator<IVertex<T>> {
        int edgeIndex = 0;
        private NeighborIterator() {
            edgeIndex = 0;
        } // end default constructor

        public boolean hasNext() {
            return edgeIndex < edges.size();
        } // end hasNext

        public IVertex<T> next() {
            Vertex<T> nextNeighbor = null;

            if (hasNext()) {
                nextNeighbor = edges.get(edgeIndex).getDestination();
                edgeIndex++;
            } else
                throw new NoSuchElementException();

            return nextNeighbor;
        } // end next

        public void remove() {
            throw new UnsupportedOperationException();
        } // end remove
    } // end NeighborIterator
    private class WeightIterator implements Iterator {
        private int edgeIndex = 0;

        private WeightIterator() {
            edgeIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return edgeIndex < edges.size();
        }
        @Override
        public Object next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            double weight = edges.get(edgeIndex).getWeight();
            edgeIndex++;

            return weight;
        }
    }



}