package br.com.caelum.restfulie.mediatype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class EnhancedList<T> implements List<T> {

	private List<T> elements = new ArrayList<T>();

	public void add(int index, T element) {
		elements.add(index, element);
	}

	public boolean add(T e) {
		return elements.add(e);
	}

	public boolean addAll(Collection<? extends T> c) {
		return elements.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends T> c) {
		return elements.addAll(index, c);
	}

	public void clear() {
		elements.clear();
	}

	public boolean contains(Object o) {
		return elements.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return elements.containsAll(c);
	}

	@Override
	public boolean equals(Object o) {
		return elements.equals(o);
	}

	public T get(int index) {
		return elements.get(index);
	}

	@Override
	public int hashCode() {
		return elements.hashCode();
	}

	public int indexOf(Object o) {
		return elements.indexOf(o);
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}

	public Iterator<T> iterator() {
		return elements.iterator();
	}

	public int lastIndexOf(Object o) {
		return elements.lastIndexOf(o);
	}

	public ListIterator<T> listIterator() {
		return elements.listIterator();
	}

	public ListIterator<T> listIterator(int index) {
		return elements.listIterator(index);
	}

	public T remove(int index) {
		return elements.remove(index);
	}

	public boolean remove(Object o) {
		return elements.remove(o);
	}

	public boolean removeAll(Collection<?> c) {
		return elements.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return elements.retainAll(c);
	}

	public T set(int index, T element) {
		return elements.set(index, element);
	}

	public int size() {
		return elements.size();
	}

	public List<T> subList(int fromIndex, int toIndex) {
		return elements.subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return elements.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return elements.toArray(a);
	}

	@Override
	public String toString() {
		return elements.toString();
	}
}
