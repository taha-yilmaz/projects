import java.util.List;

public interface IGraph {

    // Adds an edge to the graph with the given source, destination, and weight.
    void addEdge(String source, String destination, int weight);

    // Returns the number of vertices in the graph.
    int size();

    // Returns the adjacency matrix of the graph.
    int[][] getAdjacency();

    // Returns the list of vertices in the graph.
    List<String> getVertices();

    // Prints the graph representation.
    void print();

    // Performs Breadth-First Search starting from the given vertex.
    void BFS(String searched);

    // Performs Depth-First Search starting from the given vertex.
    void DFS(String searched);
}

