package hw7.hashing;

import hw7.Map;
import java.util.ArrayList;
import java.util.Iterator;

public class OpenAddressingHashMap<K, V> implements Map<K, V> {


  private Entry<K,V>[] data;

  private int capacity;

  private int size;


  /**
   Constructor of the linear probing hashmap.
   Initial capacity is set to 8 and doubles when load factor reached
   */
  public OpenAddressingHashMap() {
    capacity = 8;
    size = 0;
    //has unchecked warning, but I guess it is fine according to hw description
    data = new Entry[capacity];
  }

  //helper function that gives index when given key
  private int hashCode(K k) {
    int index = k.hashCode() % capacity;

    // note that index of array can't be negative, so check and change that
    if (index < 0) {
      index = index + capacity;
    }


    return index;
  }

  //helper function that finds the index to insert
  //uses linear probing
  private int findInsertIndex(K k) {

    int index = hashCode(k);
    int i = 1;
    //now check if the place is already occupied
    while (data[index] != null) {
      // tombstone is considered as not occupied, so return if it is tombstone
      if (data[index].key == null && data[index].value == null) {
        return index;
      }
      index = (index + i*i) % capacity;
      i++;
    }
    return index;
  }

  @Override
  public void insert(K k, V v) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }

    if (has(k)) {
      throw new IllegalArgumentException("cannot insert duplicate key");
    }

    //if load factor is beyond 0.5, resize
    if ((double)size / capacity >= 0.5) {
      resize();
    }

    //creates the new node and put it in its spot
    Entry<K,V> a = new Entry<>(k,v);
    data[findInsertIndex(k)] = a;
    size++;
  }

  //helper function that resizes the hashmap when load factor is exceeded
  private void resize() {
    capacity = capacity * 2;
    Entry<K,V>[] buffer = data;
    data = new Entry[capacity];
    for (Entry<K,V> entry: buffer) {
      if (entry != null && entry.key != null && entry.value != null) {
        insert(entry.key, entry.value);
      }
    }
  }

  @Override
  public V remove(K k) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }

    if (!has(k)) {
      throw new IllegalArgumentException("key does not exist");
    }

    int i = 1;
    int index = hashCode(k);
    V value = null;
    while (data[index] != null) {
      if (data[index].key == k) {
        //record the value
        value = data[index].value;

        //set it to tombstone, i.e, both key and value being null
        data[index].key = null;
        data[index].value = null;
      }
      index= index + i * i;
      i++;
    }
    size--;
    return value;
  }

  //helper function that finds the index of the given key
  //returns -1 if the key doesn't exist or given key is null
  private int find(K k) {
    if (k == null) {
      return -1;
    }
    int i = 1;
    int index = hashCode(k);

    //notice the difference in condition here compared to insert.
    //we consider tombstone as valid too
    while (data[index] != null) {
      if (data[index].key == k) {
        return index;
      }
      index = (index + i*i) % capacity;
      i++;
    }

    //if found nothing, then return -1
    return -1;
  }

  @Override
  public void put(K k, V v) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }
    if (!has(k)) {
      throw new IllegalArgumentException("key does not exist");
    }
    data[find(k)].value = v;
  }

  @Override
  public V get(K k) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }
    return data[find(k)].value;
  }

  @Override
  public boolean has(K k) {
    return find(k) != -1;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public Iterator<K> iterator() {
    ArrayList<K> keys = new ArrayList<>();
    for (Entry<K,V> entry: data) {
      if (entry != null && entry.key != null && entry.value != null) {
        keys.add(entry.key);
      }
    }
    return keys.iterator();
  }

  //set up entry of the map
  private static class Entry<K, V> {
    K key;
    V value;

    Entry(K key, V value) {
      this.key = key;
      this.value = value;
    }
  }
}
