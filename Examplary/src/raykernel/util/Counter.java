package raykernel.util;

import java.util.HashMap;

public class Counter
{

	private static HashMap<String, Integer> countMap = new HashMap<String, Integer>();
	private static HashMap<String, Distribution> distMap = new HashMap<String, Distribution>();

	public static void add(String key, float f)
	{
		Distribution d = distMap.get(key);

		if (d == null)
		{
			d = new Distribution();
			distMap.put(key, d);
		}

		d.addFloat(f);
	}

	public static void add(String key, int i)
	{
		add(key, (float) i);
	}

	public static int getCount(String key)
	{
		Integer i = countMap.get(key);

		if (i == null)
			return 0;

		return i.intValue();
	}

	public static float getMean(String key)
	{
		// TODO Auto-generated method stub
		Distribution d = distMap.get(key);
		return d.getMean();
	}

	public static void inc(String key)
	{
		Integer i = countMap.get(key);

		if (i == null)
		{
			i = new Integer(1);
		}
		else
		{
			i = new Integer(i.intValue() + 1);
		}

		countMap.put(key, i);
	}

	public static void printAll()
	{
		for (String s : countMap.keySet())
		{
			printCount(s);
		}

		for (String s : distMap.keySet())
		{
			printDist(s);
		}
	}

	public static void printCount(String key)
	{
		raykernel.io.Out.println(key + " : " + getCount(key));
	}

	public static void printDist(String key)
	{
		Distribution d = distMap.get(key);

		if (d == null)
		{
			raykernel.io.Out.println(key + " : not seen");
		}
		else
		{
			raykernel.io.Out.println(key + " : " + d);
		}
	}

}
