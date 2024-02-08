import java.util.Iterator;
public interface IVertex<T>{
    T getName() ;
    void visit();
    void unvisit();
    boolean isVisited();
    boolean connect (IVertex<T> endVertex, double edgeWeight);
    boolean connect (IVertex<T> endVertex) ;
    Iterator<IVertex<T>> getNeighborIterator () ;
    public Iterator<Double> getWeightIterator();
    boolean hasNeighbor () ;
    IVertex<T> getUnvisitedNeighbor () ;
    void setPredecessor (IVertex<T> predecessor) ;
    public IVertex<T> getPredecessor () ;
    public boolean hasPredecessor () ;
    void setCost (double newCost);
    double getCost () ;
}