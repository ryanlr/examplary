package raykernel.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class WeightedEnumerativeSet<T> implements Iterable<T>, Comparator<T>, Serializable
{
	HashMap<T, Double> map = new HashMap<T, Double>();

	public void add(T t)
	{
		add(t, 1);
	}

	public void add(T t, double weight)
	{
		Double current = getCount(t);

		current += weight;

		map.put(t, current);
	}

	public void addAll(Collection<? extends T> ts)
	{
		for (T t : ts)
		{
			add(t);
		}
	}

	public void normalizeWeights()
	{
		Double total = 0.0;

		for (T t : this)
		{
			total += getCount(t);
		}

		for (T t : this)
		{
			Double newWeight = getCount(t) / total;
			setCount(t, newWeight);
		}
	}

	public void addFromMap(WeightedEnumerativeSet<T> ts)
	{
		for (T t : ts)
		{
			add(t, ts.getCount(t));
		}
	}

	boolean reverseSort = false;

	public int compare(T arg0, T arg1)
	{
		if (reverseSort)
			return (int) (getCount(arg1) - getCount(arg0));

		return (int) (getCount(arg0) - getCount(arg1));
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

	public List<T> getSortedList()
	{
		List<T> keys = new LinkedList<T>();
		keys.addAll(keySet());

		Collections.sort(keys, this);

		return keys;
	}

	public List<T> getReverseSortedList()
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

	}

	public void setCount(T t, Double val)
	{
		map.put(t, val);
	}

	public int size()
	{
		return keySet().size();
	}

	public void clear()
	{
		this.map.clear();
	}

	public Double getFrequency(T t)
	{
		Double total = 0.0;

		for (T t2 : this)
		{
			total += getCount(t2);
		}

		return this.getCount(t) / total;
	}

}
