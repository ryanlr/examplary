package raykernel.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MultiMap<K extends Comparable, V>
{
	private final Map<K, List<V>> map = new HashMap<K, List<V>>();

	public void put(K k, V v)
	{
		List<V> list = map.get(k);

		if (list == null)
		{
			list = new LinkedList<V>();
			map.put(k, list);
		}

		list.add(v);
	}

	public List<V> get(K k)
	{
		return map.get(k);
	}

	public Set<K> keySet()
	{
		return map.keySet();
	}

	public List<K> getSortedKeys()
	{
		List<K> keys = new LinkedList<K>();
		keys.addAll(keySet());
		Collections.sort(keys);
		return keys;
	}

	public void putAll(K key, List<V> vals)
	{
		for (V val : vals)
		{
			put(key, val);
		}
	}
}
