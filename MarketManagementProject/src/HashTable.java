import java.util.Iterator;
import java.util.NoSuchElementException;


enum HashingMode {
    SUM, POLYNOMIAL}

enum ProbingMode {
    LINEAR, DOUBLE
}


public final class HashTable<K, V> implements IHashTable<K, V>, Iterable<HashEntry<K, V>>{
    private int numberOfEntries;
    private static final int DEFAULT_CAPACITY = 571; // Must be prime
    private static final int MAX_CAPACITY = 10000;
    private HashEntry<K, V>[] table;
    private static final int MAX_SIZE = 2 * MAX_CAPACITY;
    private boolean initialized = false;
    private static double maxLoadFactor;
    private static final double DEFAULT_LOAD_FACTOR = 0.8;

    private HashingMode hashingMode;
    private ProbingMode probingMode;
    private long collCount;


    public HashTable()
    {
        this (DEFAULT_CAPACITY, HashingMode.SUM, ProbingMode.LINEAR, DEFAULT_LOAD_FACTOR); // Call next constructor
    }
    public HashTable(HashingMode hashingMode, ProbingMode probingMode)
    {
        this (DEFAULT_CAPACITY, hashingMode, probingMode, DEFAULT_LOAD_FACTOR); // Call next constructor
    }
    public HashTable(HashingMode hashingMode, ProbingMode probingMode, double loadFactor)
    {
        this (DEFAULT_CAPACITY, hashingMode, probingMode, loadFactor); // Call next constructor
    }

    public HashTable(int initialCapacity, HashingMode hashingMode, ProbingMode probingMode, double loadFactor)
    {
        maxLoadFactor = loadFactor;
        checkCapacity(initialCapacity);
        collCount = 0;
        this.hashingMode = hashingMode;
        this.probingMode = probingMode;
        numberOfEntries = 0; // Dictionary is empty
        // Set up hash table:
        // Initial size of hash table is same as initialCapacity if it is prime;
        // otherwise increase it until it is prime size
        int tableSize = getNextPrime(initialCapacity);

        checkSize(tableSize); // Check for max array size
        // The cast is safe because the new array contains null entries
        @SuppressWarnings ("unchecked")
        HashEntry<K, V>[] temp = (HashEntry<K, V>[])new HashEntry[tableSize];
        table = temp;
        initialized = true;
        }

        public V get(K key){
            checkInitialization();
            V result = null;
            int index = getHashIndex(key);
            index = locate(index, key);
            if (index != -1 && table[index] != null)
                result = table[index].getValue(); // Key found; get value

            return result;
    }

    public V add (K key, V value)
    {
        checkInitialization ();
        if ((key == null) || (value == null))
            throw new IllegalArgumentException ("Cannot add null to a dictionary.");
        else
            {
                V oldValue;
                int index = getHashIndex (key) ;
                index = probe (index, key);
        // Value to return
        // Assertion: index is within legal range for hashIable
                assert (index > 0) && (index < table.length);
                if((table[index] == null) || table[index].isRemoved() )
                {// key not found, so insert new entry
                    table[index] = new HashEntry<> (key, value);
                    numberOfEntries++;
                    oldValue = null;
                }
                else
                {// Key found; get old value for return and then replace it
                    oldValue = table[index].getValue();
                    table[index].setValue(value);// this is unique for this project
                }
        // Ensure that hash table is large enough for another add
                if (isHashTableTooFull())
                    enlargeHashTable ();
                return oldValue;
        } // end if
    } // end add

    @Override
    public V remove(K key){
        checkInitialization();
        V removedValue = null;
        int index = getHashIndex(key);
        index = locate(index, key);
        if (index != -1 && table[index] != null)
        {// Key found; flag entry as removed and return its value
            removedValue = table[index].getValue();
            table[index].setToRemoved();
            numberOfEntries--;
        } // end if
        // Else key not found; return null
        return removedValue;
    } //


    private void enlargeHashTable()
    {
        HashEntry<K, V>[] oldTable = table;
        int oldSize = table.length;
        int newSize = getNextPrime(oldSize + oldSize);
        // The cast is safe because the new array contains null entries
        @SuppressWarnings ("unchecked")
        HashEntry<K, V>[] temp = (HashEntry<K, V>[])new HashEntry[newSize];
        table = temp;
        numberOfEntries = 0; // Reset number of dictionary entries, since
        // it will be incremented by add during rehash
        // Rehash dictionary entries from old array to the new and bigger
        // array; skip both null locations and removed entries
        for (int index = 0; index < oldSize; index++)
        {
            if ( (oldTable[index] != null) && oldTable[index].isIn())
                add(oldTable[index].getKey(), oldTable[index].getValue()) ;
        } // end for
    } // end enlargeHashTable

    private int getHashIndex(K key) {
        if (hashingMode == HashingMode.SUM) {
            return hashSum((String) key);
        } else if (hashingMode == HashingMode.POLYNOMIAL) {
            return polynomialHash((String) key);
        } else {
            throw new IllegalArgumentException("Unsupported hashing mode");
        }
    }
    public int hashSum(String key) {
        int sum = 0;
        for (char c : key.toCharArray()) {
            sum += c;
        }
        return sum % table.length;
    }

    public int polynomialHash(String key) {
        long hash = 0;
        int base = 31; // Asal sayı seçilebilir

        for (int i = 0; i < key.length(); i++) {
            char currentChar = key.charAt(i);
            int charValue = currentChar - 'a' + 1;

            // Üs alma işlemi için tekrar tekrar Math.pow kullanmak yerine döngü içinde hesapla
            long power = 1;
            for (int j = 0; j < key.length() - i - 1; j++) {
                power *= base;
            }
            hash += charValue * power;
        }
        int index = (int) (hash % table.length);
        return index < 0 ? -1 * index : index;
    }
    private int locate(int index, K key)
    {
        if(probingMode == ProbingMode.LINEAR)
            return Linearlocate(index, key);
        if(probingMode == ProbingMode.DOUBLE)
            return doubleHashingLocate(index, key);
        throw new IllegalArgumentException("Unsupported probing mode");
    }


    private int Linearlocate(int index, K key)
    {
        boolean found = false;
        while ( !found && (table[index] != null) )
        {
            if (table[index].isIn() &&
                    key.equals(table[index].getKey()))
            found = true; // Key found
            else    // Follow probe sequence
                index = (index + 1) % table.length; // Linear probing
        } // end while
        // Assertion: Either key or null is found at hashTable[index]
        int result = -1;
        if (found)
            result = index;
        return result;
} // end locate
    private int doubleHashingLocate(int index, K key) {
        int originalIndex = index;
        int stepSize = calculateStepSize(key);

        while (table[index] != null) {
            if (table[index].isIn() && key.equals(table[index].getKey())) {
                return index; // Key found
            } else {
                // Follow double hashing probe sequence
                index = getNextDoubleHashingProbeIndex(index, stepSize);

                if (index == originalIndex) {
                    // Avoid infinite loop, return or handle as needed
                    return -1; // Key not found
                }
            }
        }

        // Key not found, or an available slot in the table is found
        return index;
    }


    private int probe(int index, K key)
    {
        if(probingMode == ProbingMode.LINEAR)
            index = linearProbe(index, key);
        else if(probingMode == ProbingMode.DOUBLE)
            index = doubleHashingProbe(index, key);
        else
            throw new IllegalArgumentException("Unsupported probing mode");

        return index;
    }
    private int linearProbe(int index, K key) {
        boolean keyFound = false;
        int firstRemovedIndex = -1; // Index of the first location in the removed state
        int tableLength = table.length;

        while (!keyFound && table[index] != null) {
            if (table[index].isIn() && key.equals(table[index].getKey())) {
                keyFound = true; // Key found
            }
            else {
                // Follow probe sequence (Linear probing)
                if (table[index].isIn()) {
                    index = getNextLinearProbeIndex(index, tableLength);
                }
                else {
                    // Skip entries that were removed
                    // Save index of the first location in the removed state
                    if (firstRemovedIndex == -1) {
                        firstRemovedIndex = index;
                    }
                    index = getNextLinearProbeIndex(index, tableLength);
                }
            }
        }
        // Assertion: Either key or null is found at hashTable[index]
        return keyFound || firstRemovedIndex == -1 ? index : firstRemovedIndex;
    }


    private int getNextLinearProbeIndex(int currentIndex, int tableLength) {
        collCount++;
        return (currentIndex + 1) % tableLength;
    }

    private int doubleHashingProbe(int index, K key) {
        int originalIndex = index;
        boolean keyFound = false;
        int firstRemovedIndex = -1; // Index of the first location in the removed state
        int tableLength = table.length;
        int stepSize = calculateStepSize(key);

        while (!keyFound && table[index] != null) {
            if (table[index].isIn() && key.equals(table[index].getKey())) {
                keyFound = true; // Key found
            } else {
                // Follow double hashing probe sequence
                index = getNextDoubleHashingProbeIndex(index, stepSize);

                if (index == originalIndex) {
                    linearProbe(index, key);
                    break;
                }

                if (table[index] != null) {
                    handleProbeResult(table[index].isIn(), index, firstRemovedIndex);
                }
            }
        }
        return keyFound || firstRemovedIndex == -1 ? index : firstRemovedIndex;
    }

    private int getNextDoubleHashingProbeIndex(int currentIndex, int stepSize) {
        return (currentIndex + stepSize) % table.length;
    }

    private int handleProbeResult(boolean isIn, int currentIndex, int firstRemovedIndex) {
        // Skip entries that were removed
        // Save index of the first location in the removed state
        if (!isIn && firstRemovedIndex == -1) {
            return currentIndex;
        }
        collCount++;
        return firstRemovedIndex;
    }

    private int calculateStepSize(K key) {
        int hashValue = getHashIndex(key);
        int prime = 2003; // Choose a prime number
        // Ensure that the step size is relatively prime to the table size
        // by adding 1 to the modulo result
        return (prime - (hashValue % prime)) % table.length;
    }


    private void checkCapacity(int capacity) {
        if (capacity < DEFAULT_CAPACITY) {
            throw new IllegalArgumentException("Capacity must not be less than the default capacity.");
        }
    }

    private int getNextPrime(int current) {
        int nextPrime = current + 1;
        while (!isPrime(nextPrime)) {
            nextPrime++;
        }
        return nextPrime;
    }

    public long getCollCount() {
        return collCount;
    }

    private boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
    private void checkSize(int size) {
        if (size > MAX_SIZE) {
            throw new IllegalStateException("Exceeding maximum array size.");
        }
    }
    private void checkInitialization() {
        if (!initialized) {
            throw new IllegalStateException("Hash table has not been initialized properly.");
        }
    }

    private boolean isHashTableTooFull() {
        double loadFactor = (double) numberOfEntries / table.length;
        return loadFactor > maxLoadFactor;
    }


    public Iterator<HashEntry<K, V>> iterator() {
        return new HashTableIterator();
    }

    private class HashTableIterator implements Iterator<HashEntry<K, V>> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            while (currentIndex < table.length && (table[currentIndex] == null || !table[currentIndex].isIn())) {
                currentIndex++;
            }
            return currentIndex < table.length;
        }

        @Override
        public HashEntry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return table[currentIndex++];
        }
    }



}









class HashEntry<K, V> {
    private K key;
    private V value;
    private enum States {CURRENT, REMOVED};
    private States state; // Flags whether this entry is in the hash table

    public HashEntry(K key, V value) {
        this.key = key;
        this.value = value;
        this.state = States.CURRENT;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public boolean isRemoved() {
        return state == States.REMOVED;
    }

    public boolean isIn() {
        return state == States.CURRENT;
    }

    public void setToRemoved() {
        this.state = States.REMOVED;
    }

}
