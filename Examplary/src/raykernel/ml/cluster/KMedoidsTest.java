package raykernel.ml.cluster;

import java.util.LinkedList;
import java.util.List;

public class KMedoidsTest
{
	public static List<String> makeList(String... strings)
	{
		LinkedList<String> list = new LinkedList<String>();

		for (String s : strings)
		{
			list.add(s);
		}

		return list;
	}

	public static void main(String[] args)
	{
		List<List<String>> instances = new LinkedList<List<String>>();

		instances.add(makeList("<init>", "readLine", "close"));
		//instances.add(makeList("<init>", "readLine", "close"));
		instances.add(makeList("<init>", "ready", "readLine", "close"));
		instances.add(makeList("<init>", "ready", "close"));
		//instances.add(makeList("<init>", "ready", "readLine", "close"));
		//instances.add(makeList("<init>", "ready", "close"));
		instances.add(makeList("<init>", "ready", "readLine"));
		//instances.add(makeList("<init>", "ready", "close"));
		//instances.add(makeList("<init>", "ready", "close"));
		//instances.add(makeList("<init>", "readLine", "close"));
		//instances.add(makeList("<init>", "readLine", "close"));
		//instances.add(makeList("<init>", "ready", "readLine", "close"));
		//instances.add(makeList("<init>", "ready", "close"));
		//instances.add(makeList("<init>", "ready", "readLine"));
		//instances.add(makeList("<init>", "ready", "close"));
		instances.add(makeList("<init>", "readLine"));

		KMedoids<List<String>> kmedoids = new KMedoids<List<String>>(new KendallsTauDistanceMetric<String>(), 2);

		kmedoids.buildClusterer(instances);

		kmedoids.printClusters(instances);
	}
}
