package raykernel.stats;

import java.util.LinkedList;
import java.util.List;

public class KendallsTau
{
	public static double computeDistance(List l1, List l2)
	{
		return computeDistance(new Ranking(l1), new Ranking(l2));
	}

	public static double computeDistance(List l1, List l2, int numBins)
	{
		if (numBins > l1.size())
		{
			numBins = l1.size();
		}

		//io.Out.println("list 1: " + l1 );
		//io.Out.println("list 2: " + l2 );
		//io.Out.println("bins: " + numBins );

		return (computeDistance(new Ranking(l1), new Ranking(l2), numBins));
	}

	public static double computeDistance(Ranking r1, Ranking r2)
	{
		return computeDistance(r1, r2, r1.size());
	}

	public static double computeDistance(Ranking r1, Ranking r2, int numBins)
	{
		if (numBins == 1)
			return 0;

		int count = 0;

		int[] map = makeBinMap(r1.size(), numBins);

		//printBinMap( map );

		for (Object x : r1)
		{
			for (Object y : r2) //for every (unique) pair
			{
				if (x == y)
				{
					continue;
				}

				int dif1 = map[r1.getRank(x)] - map[r1.getRank(y)];
				int dif2 = map[r2.getRank(x)] - map[r2.getRank(y)];

				if (dif1 * dif2 < 0)
				{
					//io.Out.println("count");
					count++;
				}
			}
		}

		double n = r1.size();

		return (count / 2.0) / (n * (n - 1.0) / 2.0);
	}

	public static void main(String[] args)
	{
		//test

		int[] h = {2, 1, 3};
		int[] w = {3, 2, 1};

		List<Integer> height = makeList(h);
		List<Integer> weight = makeList(w);

		raykernel.io.Out.println("dist = " + KendallsTau.computeDistance(new Ranking(height), new Ranking(weight), 2));

		printBinMap(makeBinMap(31, 2));
	}

	private static int[] makeBinMap(int n, int b)
	{
		if (b > n)
		{
			b = n;
		}

		int[] ret = new int[n];
		int[] binsizes = new int[b];

		int base = n / b;
		int remainder = n % b;

		for (int i = 0; i < b; i++)
		{
			binsizes[i] = base;
		}

		int c = 0;
		while (remainder > 0)
		{
			binsizes[c]++;
			remainder--;
			c = (c + 1) % b;
		}

		int current = 0;
		//for each bin
		for (int bin = 0; bin < b; bin++)
		{
			//for each element
			for (int el = 0; el < binsizes[bin]; el++)
			{
				ret[current++] = bin;
			}
		}

		return ret;
	}

	public static List<Integer> makeList(int[] array)
	{
		LinkedList<Integer> list = new LinkedList<Integer>();

		for (int i : array)
		{
			list.add(i);
		}

		return list;
	}

	private static void printBinMap(int[] arr)
	{
		System.out.print("{ ");
		for (int element : arr)
		{
			System.out.print(element + ", ");
		}
		raykernel.io.Out.println("}");
	}

}
