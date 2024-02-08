import java.util.Iterator;
import java.util.LinkedList;

public class LinkedDictionary<K, V> implements IDictionary<K, V> {

    private LinkedList<Entry> entries;

    public LinkedDictionary() {
        entries = new LinkedList<>();
    }

    private class Entry {
        private K key;
        private V value;

        public Entry(K searchKey, V dataValue) {
            key = searchKey;
            value = dataValue;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

    @Override
    public V add(K key, V value) {
        for (Entry entry : entries) {
            if (entry.getKey().equals(key)) {
                V oldValue = entry.getValue();
                entry.value = value;
                return oldValue;
            }
        }
        entries.add(new Entry(key, value));
        return null;
    }

    @Override
    public V remove(K key) {
        Iterator<Entry> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            if (entry.getKey().equals(key)) {
                iterator.remove();
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public V getValue(K key) {
        for (Entry entry : entries) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public boolean contains(K key) {
        for (Entry entry : entries) {
            if (entry.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<K> getKeyIterator() {
        LinkedList<K> keys = new LinkedList<>();
        for (Entry entry : entries) {
            keys.add(entry.getKey());
        }
        return keys.iterator();
    }

    @Override
    public Iterator<V> getValueIterator() {
        LinkedList<V> values = new LinkedList<>();
        for (Entry entry : entries) {
            values.add(entry.getValue());
        }
        return values.iterator();
    }

    @Override
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    @Override
    public int getSize() {
        return entries.size();
    }

    @Override
    public void clear() {
        entries.clear();
    }
}
