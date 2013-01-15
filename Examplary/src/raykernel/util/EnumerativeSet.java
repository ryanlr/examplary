package raykernel.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class EnumerativeSet<T> implements Iterable<T>, Comparator<T>, Serializable
{
	private static final long serialVersionUID = 5895741080487844100L;
	HashMap<T, Double> map = new HashMap<T, Double>();

	public static final double MAJORITY_FREQ = 0.2;

	Double totalCount = 0.0;

	public void add(T t)
	{
		Double current = getCount(t);

		current++;

		map.put(t, current);

		totalCount++;
	}

	public void addAll(Collection<? extends T> ts)
	{
		for (T t : ts)
		{
			add(t);
		}
	}

	public void union(EnumerativeSet<T> ts)
	{
		for (T t : ts)
		{
			for (int i = 0; i < ts.getCount(t); i++)
			{
				add(t);
			}
		}
	}

	boolean reverseSort = false;

	public int compare(T arg0, T arg1)
	{
		if (reverseSort)
			return (int) ((getCount(arg1) - getCount(arg0)) * 1000);

		return (int) ((getCount(arg0) - getCount(arg1)) * 1000);
	}

	public Double getCount(T t)
	{
		Double current = map.get(t);

		if (current == null)
		{
			current = 0.0;
		}

		return current;
	}

	public List<T> getReverseSortedList()
	{
		List<T> keys = new LinkedList<T>();
		keys.addAll(keySet());

		Collections.sort(keys, this);

		return keys;
	}

	public List<T> getSortedList()
	{
		List<T> keys = new LinkedList<T>();
		keys.addAll(keySet());

		reverseSort = true;
		Collections.sort(keys, this);
		reverseSort = false;

		return keys;
	}

	public Iterator<T> iterator()
	{
		return this.keySet().iterator();
	}

	public List<T> keys()
	{
		List<T> keys = new LinkedList<T>();

		keys.addAll(map.keySet());

		return keys;
	}

	public Set<T> keySet()
	{
		return map.keySet();
	}

	public void remove(T t)
	{
		map.remove(t);

		recomputeTotalCount();
	}

	public void setCount(T t, Double val)
	{
		map.put(t, val);

		recomputeTotalCount();
	}

	private void recomputeTotalCount()
	{
		totalCount = 0.0;

		for (T t : this)
		{
			totalCount += getCount(t);
		}
	}

	private double getFreq(T t)
	{
		return getCount(t) / totalCount;
	}

	public int size()
	{
		return keySet().size();
	}

	public T getBest(T fallBack)
	{
		T first = getFirst();

		if (getFreq(first) < MAJORITY_FREQ)
			return fallBack;

		return first;
	}

	public T getFirst()
	{
		if (isEmpty())
			return null;

		return getSortedList().get(0);
	}

	public boolean isEmpty()
	{
		return map.isEmpty();
	}

	@Override
	public String toString()
	{
		return this.getSortedList().toString();
	}

	public void printSortedCounts(int max)
	{
		int count = 0;

		for (T t : this.getSortedList())
		{
			System.out.println(t + "\t" + this.getCount(t));
			count++;

			if (count >= max)
			{
				break;
			}
		}

	}

	public List<T> getAllMinFreq(double min)
	{
		LinkedList<T> ret = new LinkedList<T>();

		for (T t : this)
		{
			if (getFreq(t) >= min)
			{
				ret.add(t);
			}
		}

		return ret;
	}

	public void multiply(Double f)
	{
		for (Entry<T, Double> e : map.entrySet())
		{
			e.setValue(e.getValue() * f);
		}
	}

	public void subtract(EnumerativeSet<T> otherSet)
	{
		for (T t : otherSet)
		{
			double otherCount = otherSet.getCount(t);
			setCount(t, getCount(t) - otherCount);
		}
	}

	public void divide(EnumerativeSet<T> otherSet)
	{
		for (T t : otherSet)
		{
			double otherCount = otherSet.getCount(t);
			double div = getCount(t) / otherCount;

			System.out.println(t + ": " + getCount(t) + " / " + otherCount + " = " + div);

			setCount(t, div);
		}
	}

	public void removeBelow(double d)
	{
		List<T> keys = new LinkedList<T>();

		keys.addAll(map.keySet());

		for (T key : keys)
		{
			if (getCount(key) < d)
			{
				map.remove(key);
			}
		}
	}

	@Override
	public EnumerativeSet<T> clone()
	{
		EnumerativeSet<T> newSet = new EnumerativeSet<T>();
		newSet.union(this);
		return newSet;
	}

}
