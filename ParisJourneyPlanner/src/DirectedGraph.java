import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.PriorityQueue;
import java.util.Comparator;

public class DirectedGraph<T> {
    private IDictionary<T, IVertex<T>> vertices;
    private int edgeCount;

    public DirectedGraph()
    {
        vertices = new LinkedDictionary<>();
        edgeCount = 0;
    } // end default constructor


    public int getEdgeCount(){
        return edgeCount;
    }

    public boolean addVertex(T vertexLabel)
    {
        IVertex<T> addOutcome =
                vertices.add(vertexLabel, new Vertex<>(vertexLabel));
        return addOutcome == null; // Was addition to dictionary successful?
    } // end addVertex

    public boolean addEdge(T begin, T end, double edgeWeight) {
        boolean result = false;
        IVertex<T> beginVertex = vertices.getValue(begin);
        IVertex<T> endVertex = vertices.getValue(end);
        if ((beginVertex != null) && (endVertex != null))
            result = beginVertex.connect(endVertex, edgeWeight);
        if (result)
            edgeCount++;

        return result;
    }
    public boolean addEdge (T begin, T end){
        return addEdge(begin, end, 0);
    }
    public boolean hasEdge(T begin, T end)
    {
        boolean found = false;
        IVertex<T> beginVertex = vertices.getValue(begin);
        IVertex<T> endVertex = vertices.getValue(end);
        if ( (beginVertex != null) && (endVertex != null) ) {
            Iterator<IVertex<T>> neighbors =
                    beginVertex.getNeighborIterator();
            while (!found && neighbors. hasNext())
            {
                IVertex<T> nextNeighbor = neighbors.next();
                if (endVertex.equals(nextNeighbor))
                    found = true;
            } // end while
        } // end if
    return found;
    } // end hasEdge

    public boolean hasVertex(T vertexLabel) {
        return vertices.contains(vertexLabel);
    }
    public Edge getEdge(T begin, T end) {
        IVertex<T> beginVertex = vertices.getValue(begin);
        IVertex<T> endVertex = vertices.getValue(end);

        if (beginVertex != null && endVertex != null) {
            Iterator<Edge> edges = ((Vertex<T>) beginVertex).getEdges().iterator();
            while (edges.hasNext()) {
                Edge edge = edges.next();
                if (edge.getDestination().equals(endVertex)) {
                    return edge;
                }
            }
        }
        return null; // Kenar bulunamadı
    }

    public T getValue(T value)
    {
        Iterator keys = vertices.getKeyIterator();
        T var = null;

        while (keys.hasNext()){
            T next = (T) keys.next();
            if(value.equals(next))
                var = next;
        }
        return var;
    }

    public void print() {
        Iterator<IVertex<T>> vertexIterator = vertices.getValueIterator();

        while (vertexIterator.hasNext()) {
            IVertex<T> v = vertexIterator.next();
            System.out.print(v.getName() + " -> ");

            Iterator<IVertex<T>> neighbors = v.getNeighborIterator();
            while (neighbors.hasNext()) {
                IVertex<T> n = neighbors.next();
                System.out.print(n.getName() + " ");
            }
            System.out.println();
        }
    }

    public Iterable<Vertex> vertices() {
        return (Iterable<Vertex>) vertices.getValueIterator();
    }

    public int size() {
        return vertices.getSize();
    }

    private void resetVertices() {
        Iterator<IVertex<T>> vertexIterator = vertices.getValueIterator();

        while (vertexIterator.hasNext()) {
            IVertex v = vertexIterator.next();
            v.unvisit();
            v.setCost(0);
            v.setPredecessor(null);
        }
    }

    public Queue<T> getBreadthFirstTraversal(T origin)
    {
        resetVertices();
        Queue<T> traversalOrder = new LinkedList<>(); // Queue of vertex labels
        Queue<Vertex> vertexQueue = new LinkedList<>(); // Queue of Vertex objects

        Vertex originVertex = (Vertex) vertices.getValue((T) origin);
        originVertex.visit();

        traversalOrder.add(origin);    // Enqueue vertex label
        vertexQueue.add(originVertex); // Enqueue vertex

        while (!vertexQueue.isEmpty())
        {
            Vertex frontVertex = vertexQueue.remove();
            Iterator<Vertex> neighbors = frontVertex.getNeighborIterator();

            while (neighbors.hasNext())
            {
                Vertex nextNeighbor = neighbors.next();
                if (!nextNeighbor.isVisited())
                {
                    nextNeighbor.visit();
                    traversalOrder.add((T) nextNeighbor.getName());
                    vertexQueue.add(nextNeighbor);
                } // end if
            } // end while
        } // end while

        return traversalOrder;
    } // end getBreadthFirstTraversal

    public Queue<String> getDepthFirstTraversal(String origin)
    {
        resetVertices();
        Queue<String> traversalOrder = new LinkedList<>();
        Stack<Vertex> vertexStack = new Stack<>();

        Vertex originVertex = (Vertex) vertices.getValue((T) origin);
        originVertex.visit();
        traversalOrder.add(origin);

        vertexStack.push(originVertex);

        while (!vertexStack.isEmpty()) {
            Vertex currentVertex = vertexStack.peek();
            Vertex nextNeighbor = (Vertex) currentVertex.getUnvisitedNeighbor();

            if (nextNeighbor != null) {
                nextNeighbor.visit();
                traversalOrder.add((String) nextNeighbor.getName());
                vertexStack.push(nextNeighbor);
            } else {
                vertexStack.pop();
            }
        }

        return traversalOrder;
    } // end getDepthFirstTraversal

    public int getShortestPath(T begin, T end, Stack<T> path) {
        resetVertices(); // Assuming this is a method that resets the state of vertices
        boolean done = false;
        Queue<IVertex<T>> vertexQueue = new LinkedList<>();
        IVertex<T> originVertex = vertices.getValue(begin);
        IVertex<T> endVertex = vertices.getValue(end);
        originVertex.visit(); // Assuming visit() is a method that marks the vertex as visited
        // Assertion: resetVertices() has executed setCost(0) and setPredecessor(null) for originVertex
        vertexQueue.offer(originVertex); // Enqueue

        while (!done && !vertexQueue.isEmpty()) {

            IVertex<T> frontVertex = vertexQueue.poll(); // Dequeue
            Iterator<IVertex<T>> neighbors = frontVertex.getNeighborIterator();

            while (!done && neighbors.hasNext()) {
                IVertex<T> nextNeighbor = neighbors.next();

                if (!nextNeighbor.isVisited()) {
                    nextNeighbor.visit();
                    nextNeighbor.setCost(1 + frontVertex.getCost());
                    nextNeighbor.setPredecessor(frontVertex);
                    vertexQueue.offer(nextNeighbor); // Enqueue

                    // Check if the destination vertex is reached
                    if (nextNeighbor.equals(endVertex))
                        done = true;
                }
            } // end while
        } // end while

        // Traversal ends; construct the shortest path
        int pathLength = (int) endVertex.getCost();
        path.push(endVertex.getName());
        IVertex<T> vertex = endVertex;

        while (vertex.hasPredecessor()) {
            vertex = vertex.getPredecessor();
            path.push(vertex.getName());
        } // end while

        return pathLength;
    }
    public double getCheapestPath(T begin, T end, Stack<T> path) {
        resetVertices();
        boolean done = false;
        PriorityQueue<EntryPQ<T>> priorityQueue = new PriorityQueue<>();

        IVertex<T> originVertex = vertices.getValue(begin);
        IVertex<T> endVertex = vertices.getValue(end);

        priorityQueue.add(new EntryPQ<>(originVertex, 0, null));

        while (!done && !priorityQueue.isEmpty()) {
            EntryPQ<T> frontEntry = priorityQueue.remove();
            IVertex<T> frontVertex = frontEntry.getVertex();

            if (!frontVertex.isVisited()) {
                frontVertex.visit();
                frontVertex.setCost(frontEntry.getCost());
                frontVertex.setPredecessor(frontEntry.getPredecessor());

                if (frontVertex.equals(endVertex)) {
                    done = true;
                } else {
                    Iterator<IVertex<T>> neighborIterator = frontVertex.getNeighborIterator();

                    while (neighborIterator.hasNext()) {
                        Vertex<T> nextNeighbor = (Vertex<T>) neighborIterator.next();
                        double weightOfEdgeToNeighbor = (double) frontVertex.getWeightIterator().next();

                        if (!nextNeighbor.isVisited()) {
                            double nextCost = weightOfEdgeToNeighbor + frontVertex.getCost();
                            priorityQueue.add(new EntryPQ<>(nextNeighbor, nextCost, frontVertex));
                        }
                    }
                }
            }
        }

        // Traversal ends; construct cheapest path
        double pathCost = endVertex.getCost();
        path.push(end);

        IVertex<T> vertex = endVertex;
        while (vertex.hasPredecessor()) {
            vertex = (Vertex<T>) vertex.getPredecessor();
            path.push(vertex.getName());
        }

        return pathCost;
    }
}
//getCheapestPath