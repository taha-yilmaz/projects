public final class HeapPriorityQueue<T extends Comparable<? super T>>
        implements IPriorityQueue<T>
{
    private MaxHeapInterface<T> pq;

    public HeapPriorityQueue()
    {
        pq = new MaxHeap<>();
    }

    public void add(T newEntry)
    {
        pq.add(newEntry);
    }

    public T remove()
    {
        return pq.removeMax();
    }

    public T peek()
    {
        return pq.getMax();
    }

    public boolean isEmpty()
    {
        return pq.isEmpty();
    }

    public int getSize()
    {
        return pq.getSize();
    }

    public void clear()
    {
        pq.clear();
    }
}
