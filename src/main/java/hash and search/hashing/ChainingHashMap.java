package hw7.hashing;

import hw7.Map;
import java.util.ArrayList;
import java.util.Iterator;


public class ChainingHashMap<K, V> implements Map<K, V> {

  //use linked list for auxiliary data structure

  private Entry<K,V>[] data;

  private int capacity;

  private int size;

  /**
   Constructor of the Chaining hashmap.
   Initial capacity is set to 8 and doubles when load factor reached
   */
  public ChainingHashMap() {
    capacity = 8;
    size = 0;
    //has unchecked warning, but I guess it is fine according to hw description
    data = new Entry[capacity];
  }

  @Override
  public void insert(K k, V v) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }

    if (has(k)) {
      throw new IllegalArgumentException("cannot insert duplicate key");
    }

    //if load factor is beyond 0.75, resize
    if ((double)size / capacity >= 0.75) {
      resize();
    }

    int index = hashCode(k);

    //creates the new node and put it in the front
    Entry<K,V> a = new Entry<>(k,v);
    a.next = data[index];
    data[index] = a;
    size++;
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

  //helper function that resizes the hashmap when load factor is exceeded
  private void resize() {
    capacity = capacity * 2;
    Entry<K,V>[] buffer = data;
    data = new Entry[capacity];
    for (Entry<K,V> entry: buffer) {
      Entry<K,V> cursor = entry;
      while (cursor != null) {
        insert(cursor.key, cursor.value);
        cursor = cursor.next;
      }
    }
  }

  @Override
  public V remove(K k) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }

    int hashIndex = hashCode(k);

    //store the removed value
    V removed;

    if (data[hashIndex] == null) {
      throw new IllegalArgumentException("key does not exist");
    } else if (data[hashIndex].key == k) {
      removed = data[hashIndex].value;
      data[hashIndex] = null;
    } else if (find(k) ==  null) {
      throw new IllegalArgumentException("key does not exist");
    } else {
      //there is a warning, but our find implementation avoids null pointer
      //can't get rid of the warning easily
      removed = find(k).next.value;
      //delete our target
      find(k).next = find(k).next.next;
    }
    size--;
    return removed;
  }

  //helper method that finds and returns the previous entry
  //precondition: it has previous (ie, not in the array itself)
  //if the item doesn't exist, returns null
  private Entry<K,V> find(K k) {
    //index of the array
    int hashIndex = hashCode(k);

    //by precondition, data[hashIndex] can't be our target
    Entry<K,V> cur = data[hashIndex];
    if (cur ==  null) { // so empty linked list, return -1
      return null;
    }

    //iterate through to find the target
    while (cur.next != null) {
      if (cur.next.key == k) {
        return cur;
      }
      cur = cur.next;
    }
    return null;
  }


  @Override
  public void put(K k, V v) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }
    //index of the array
    int hashIndex = hashCode(k);
    if (data[hashIndex] == null) {
      throw new IllegalArgumentException("key does not exist");
    } else if (data[hashIndex].key == k) {
      data[hashIndex].value = v;
    } else if (find(k) ==  null) {
      throw new IllegalArgumentException("key does not exist");
    } else {
      //there is a warning, but our find implementation avoids null pointer
      //can't get rid of the warning easily
      //update value
      find(k).next.value = v;
    }
  }

  @Override
  public V get(K k) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }
    //index of the array
    int hashIndex = hashCode(k);

    if (data[hashIndex] == null) {
      throw new IllegalArgumentException("key does not exist");
    } else if (data[hashIndex].key == k) {
      return data[hashIndex].value;
    } else if (find(k) ==  null) {
      throw new IllegalArgumentException("key does not exist");
    } else {
      //there is a warning, but our find implementation avoids null pointer
      //can't get rid of the warning easily
      //update value
      return find(k).next.value;
    }
  }

  @Override
  public boolean has(K k) {
    //index of the array
    int hashIndex = hashCode(k);

    if (data[hashIndex] == null) {
      return false;
    }

    return data[hashIndex].key == k || find(k) != null;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public Iterator<K> iterator() {
    ArrayList<K> keys = new ArrayList<>();
    for (Entry<K,V> entry: data) {
      Entry<K,V> cursor = entry;
      while (cursor != null) {
        keys.add(cursor.key);
        cursor = cursor.next;
      }
    }
    return keys.iterator();
  }


  //set up entry of the map
  private static class Entry<K, V> {
    K key;
    V value;
    Entry<K,V> next;

    Entry(K key, V value) {
      this.key = key;
      this.value = value;
      next = null;
    }
  }


}
