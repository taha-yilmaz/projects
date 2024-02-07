public interface IHashTable <K, V>{
    V add(K key, V val);
    V get(K key);
    V remove(K key);
    String toString();
}
