package raykernel.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class SortedBinSet<K, V> implements Iterable<SortedSet<V>>
{
	Map<K, SortedSet<V>> binMap = new HashMap<K, SortedSet<V>>();
	SortedSet<SortedSet<V>> binList; // = new TreeSet<SortedSet<V>>();
	int size = 0;

	public SortedBinSet(Comparator<SortedSet<V>> comparator)
	{
		binList = new TreeSet<SortedSet<V>>(comparator);
	}

	public void add(K key, V value)
	{
		SortedSet<V> bin = binMap.get(key);
		if (bin == null)
		{
			bin = new TreeSet<V>();
			binMap.put(key, bin);
			binList.add(bin);
		}

		bin.add(value);

		size++;
	}

	public Iterator<SortedSet<V>> iterator()
	{
		return binList.iterator();
	}

	public int size()
	{
		return size;
	}

	public Set<K> keySet()
	{
		return binMap.keySet();
	}

	public boolean isEmpty()
	{
		return binList.isEmpty();
	}

	public SortedSet<V> get(K key)
	{
		return binMap.get(key);
	}

}
