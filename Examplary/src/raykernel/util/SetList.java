package raykernel.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class SetList<T> implements Iterable<T>, List<T>, Serializable
{

	LinkedList<T> list = new LinkedList<T>();
	HashSet<T> set = new HashSet<T>();

	public boolean add(T t)
	{
		addLast(t);

		return true;
	}

	public boolean addAll(Collection<? extends T> methods)
	{

		for (T t : methods)
		{
			add(t);
		}

		return true;

	}

	public boolean remove(Object t)
	{
		if (!set.remove(t))
			return false;

		list.remove(t);
		return true;
	}

	public void addFirst(T t)
	{
		if (!set.contains(t))
		{
			set.add(t);
			list.addFirst(t);
		}
	}

	public void addLast(T t)
	{
		if (!set.contains(t))
		{
			set.add(t);
			list.addLast(t);
		}
	}

	public boolean contains(Object t)
	{
		return set.contains(t);
	}

	@Override
	public boolean equals(Object o)
	{
		//io.Out.println("call to equals");

		if (o instanceof SetList) //could be
		{
			SetList other = (SetList) o;

			if (this.size() == other.size()) //still could be
			{
				Iterator iter1 = this.iterator();
				Iterator iter2 = other.iterator();

				while (iter1.hasNext()) //already know same size
				{
					if (!iter1.next().equals(iter2.next()))
						return false;
				}

				//	io.Out.println("true");

				return true;

			}
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return set.hashCode() * list.hashCode();
	}

	public boolean isEmpty()
	{
		return list.isEmpty();
	}

	public Iterator<T> iterator()
	{
		return list.iterator();
	}

	public T pop()
	{
		T first = list.remove();
		set.remove(first);
		return first;
	}

	public int size()
	{
		return list.size();
	}

	public T getFirst()
	{
		return list.getFirst();
	}

	public void add(int index, T element)
	{
		throw new UnsupportedOperationException();

	}

	public boolean addAll(int index, Collection<? extends T> c)
	{
		throw new UnsupportedOperationException();
	}

	public void clear()
	{
		list.clear();
		set.clear();
	}

	public boolean containsAll(Collection<?> c)
	{
		throw new UnsupportedOperationException();
	}

	public T get(int index)
	{
		return list.get(index);
	}

	public int indexOf(Object o)
	{
		return list.indexOf(o);
	}

	public int lastIndexOf(Object o)
	{
		return list.lastIndexOf(o);
	}

	public ListIterator<T> listIterator()
	{
		throw new UnsupportedOperationException();
	}

	public ListIterator<T> listIterator(int index)
	{
		throw new UnsupportedOperationException();
	}

	public T remove(int index)
	{
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection<?> c)
	{
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection<?> c)
	{
		throw new UnsupportedOperationException();
	}

	public T set(int index, T element)
	{
		throw new UnsupportedOperationException();
	}

	public List<T> subList(int fromIndex, int toIndex)
	{
		throw new UnsupportedOperationException();
	}

	public Object[] toArray()
	{
		throw new UnsupportedOperationException();
	}

	public <T> T[] toArray(T[] a)
	{
		throw new UnsupportedOperationException();
	}

}
