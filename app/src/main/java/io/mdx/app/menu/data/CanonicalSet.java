package io.mdx.app.menu.data;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;

// @todo Could probably use a single TreeMap for backing.
// @todo Some methods probably broken. Needs testing.

/**
 * Created by moltendorf on 16/5/21.
 */
public class CanonicalSet<T> implements List<T>, Set<T> {
  private ArrayList<Node<T>>        list;
  private HashMap<Node<T>, Node<T>> map;

  public CanonicalSet() {
    list = new ArrayList<>();
    map = new HashMap<>();
  }

  public CanonicalSet(int capacity) {
    list = new ArrayList<>(capacity);
    map = new HashMap<>(capacity);
  }

  public T get(Object object) {
    Node<T> node = map.get(object);

    if (node != null) {
      return node.value;
    }

    return null;
  }

  @Override
  public void add(int index, T object) {
    if (!map.containsKey(object)) {
      Node<T> node = new Node<>(index, object);

      map.put(node, node);
      list.add(index, node);

      for (int i = index + 1, j = list.size(); i < j; ++i) {
        list.get(i).index++;
      }
    }
  }

  @Override
  public boolean add(T object) {
    if (!map.containsKey(object)) {
      Node<T> node = new Node<>(list.size(), object);

      map.put(node, node);
      list.add(node);

      return true;
    }

    return false;
  }

  @Override
  public boolean addAll(int index, Collection<? extends T> collection) {
    int size = list.size();

    // @todo Fix terrible performance; but this method is unused for now.
    for (T object : collection) {
      add(index++, object);
    }

    return list.size() != size;
  }

  @Override
  public boolean addAll(Collection<? extends T> collection) {
    int size = list.size();

    for (T object : collection) {
      add(object);
    }

    return list.size() != size;
  }

  @Override
  public void clear() {
    list.clear();
    map.clear();
  }

  @Override
  public boolean contains(Object object) {
    return map.containsKey(object);
  }

  @Override
  public boolean containsAll(Collection<?> collection) {
    for (Object object : collection) {
      if (!map.containsKey(collection)) {
        return false;
      }
    }

    return true;
  }

  @Override
  public T get(int index) {
    return list.get(index).value;
  }

  @Override
  public int indexOf(Object object) {
    return map.get(object).index;
  }

  @Override
  public boolean isEmpty() {
    return list.isEmpty();
  }

  @NonNull
  @Override
  public Iterator<T> iterator() {
    return listIterator();
  }

  @Override
  public int lastIndexOf(Object object) {
    return indexOf(object);
  }

  @Override
  public ListIterator<T> listIterator() {
    return new CanonicalSetIterator<>(this);
  }

  @NonNull
  @Override
  public ListIterator<T> listIterator(int location) {
    return new CanonicalSetIterator<>(location, this);
  }

  @Override
  public T remove(int index) {
    Node<T> node = list.remove(index);
    map.remove(node);

    return node.value;
  }

  @Override
  public boolean remove(Object object) {
    Node<T> node = map.remove(object);

    if (node != null) {
      list.remove(node.index);

      return true;
    }

    return false;
  }

  @Override
  public boolean removeAll(Collection<?> collection) {
    int size = list.size();

    for (Object object : collection) {
      remove(object);
    }

    return list.size() != size;
  }

  @Override
  public boolean retainAll(Collection<?> collection) {
    int size = list.size();

    TreeSet<Node<T>> retained = new TreeSet<>();

    for (Object object : collection) {
      Node<T> node = map.get(object);

      if (node != null) {
        retained.add(node);
      }
    }

    int newSize = list.size();

    list = new ArrayList<>(retained);
    map = new HashMap<>(newSize);

    int index = 0;
    for (Node<T> node : list) {
      node.index = index++;
      map.put(node, node);
    }

    return newSize != size;
  }

  @Override
  public T set(int index, T object) {
    Node<T> node = list.get(index);

    T previous = node.value;

    map.remove(node);
    node.value = object;
    map.put(node, node);

    return previous;
  }

  @Override
  public int size() {
    return list.size();
  }

  @NonNull
  @Override
  public List<T> subList(int start, int end) {
    List<Node<T>> nodes = list.subList(start, end);
    ArrayList<T>  sub   = new ArrayList<>(nodes.size());

    for (Node<T> node : nodes) {
      sub.add(node.value);
    }

    return sub;
  }

  @NonNull
  @Override
  public Object[] toArray() {
    Object[] array = new Object[list.size()];

    for (int i = 0, j = array.length; i < j; ++i) {
      array[i] = list.get(i);
    }

    return array;
  }

  @NonNull
  @Override
  public <T1> T1[] toArray(@NonNull T1[] array) {
    int size = list.size();

    if (array.length < size) {
      array = Arrays.copyOf(array, size);
    }

    for (int i = 0; i < size; ++i) {
      array[i] = (T1) list.get(i).value;
    }

    for (int i = size, j = array.length; i < j; ++i) {
      array[i] = null;
    }

    return array;
  }

  public static class CanonicalSetIterator<T> implements ListIterator<T> {
    private CanonicalSet<T>       set;
    private ListIterator<Node<T>> iterator;
    private Node<T>               node;

    public CanonicalSetIterator(CanonicalSet<T> set) {
      this.set = set;
      iterator = set.list.listIterator();
    }

    public CanonicalSetIterator(int location, CanonicalSet<T> set) {
      this.set = set;
      iterator = set.list.listIterator(location);
      node = set.list.get(location);
    }

    @Override
    public boolean hasNext() {
      return iterator.hasNext();
    }

    @Override
    public T next() {
      node = iterator.next();
      return node.value;
    }

    @Override
    public void remove() {
      iterator.remove();
      set.map.remove(node);
    }

    @Override
    public void add(T object) {
      node = new Node<>(iterator.previousIndex() + 1, object);

      iterator.add(node);
      set.map.put(node, node);
    }

    @Override
    public boolean hasPrevious() {
      return iterator.hasPrevious();
    }

    @Override
    public int nextIndex() {
      return iterator.nextIndex();
    }

    @Override
    public T previous() {
      node = iterator.previous();
      return node.value;
    }

    @Override
    public int previousIndex() {
      return iterator.previousIndex();
    }

    @Override
    public void set(T object) {
      // This will change the hash, so we need to replace it in the map.
      set.map.remove(node);
      node.value = object;
      set.map.put(node, node);
    }
  }

  private static class Node<T> implements Comparable<Node<T>> {
    int index;
    T   value;

    public Node(int index, T value) {
      this.index = index;
      this.value = value;
    }

    @Override
    public int compareTo(Node<T> another) {
      return index - another.index;
    }

    @Override
    public boolean equals(Object o) {
      return value == o || !(o == null || value.getClass() != o.getClass()) && value.equals(o);
    }

    @Override
    public int hashCode() {
      return value.hashCode();
    }
  }
}
