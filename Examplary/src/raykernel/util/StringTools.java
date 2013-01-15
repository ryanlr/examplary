package raykernel.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StringTools
{
	static double similarity(String a, String b)
	{
		int sim = 0;
		
		a = a.toLowerCase();
		b = b.toLowerCase();
		
		// System.out.print("Similarity of: " + a + ":" + b);
		
		double points = 1;
		
		while (true)
		{
			String lcs = longestCommonSubstring(a, b);
			
			if (lcs.length() == 0)
			{
				break;
			}
			
			sim += lcs.length() * points;
			
			a = a.replace(lcs, "");
			b = b.replace(lcs, "");
			
			points = points / 2;
		}
		
		// System.out.println("= " + sim);
		
		return sim;
	}
	
	public static Map<String, String> matchStrings(Collection<String> list1,
			Collection<String> list2)
	{
		Map<String, String> ret = new HashMap<String, String>();
		
		HashSet<String> as = new HashSet<String>();
		HashSet<String> bs = new HashSet<String>();
		
		as.addAll(list1);
		bs.addAll(list2);
		
		as.remove(null);
		bs.remove(null);
		
		System.out.println("as: " + as);
		System.out.println("bs: " + bs);
		
		if (as.isEmpty() || bs.isEmpty())
			return ret;
		
		while (as.size() > 0 && bs.size() > 0)
		{
			String mostSimA = null, mostSimB = null;
			double maxSim = Double.MIN_VALUE;
			
			for (String a : as)
			{
				for (String b : bs)
				{
					double sim = similarity(a, b);
					
					if (sim > maxSim)
					{
						maxSim = sim;
						mostSimA = a;
						mostSimB = b;
					}
				}
			}
			
			if (mostSimA == null || mostSimB == null)
			{
				System.out.println("no more email matches");
				return ret;
			}
			
			System.out.println("best match: " + mostSimA + "->" + mostSimB
					+ " @ " + maxSim);
			
			ret.put(mostSimA, mostSimB);
			as.remove(mostSimA);
			bs.remove(mostSimB);
		}
		
		return ret;
	}
	
	public static Map<String, String> matchNamesToEmails(
			Collection<String> names, Collection<String> emails)
	{
		Map<String, String> stubToEmailMap = new HashMap<String, String>();
		List<String> stubs = new LinkedList<String>();
		
		for (String email : emails)
		{
			String stub = email.substring(0, email.indexOf("@"));
			stubToEmailMap.put(stub, email);
			stubs.add(stub);
		}
		
		Map<String, String> matches = matchStrings(names, stubs);
		
		for (String name : names)
		{
			String matchedStub = matches.get(name);
			matches.put(name, stubToEmailMap.get(matchedStub));
			
			// System.out.println(matchedStub + " -> " +
			// stubToEmailMap.get(matchedStub));
		}
		
		return matches;
	}
	
	public static String longestCommonSubstring(String first, String second)
	{
		
		String tmp = "";
		String max = "";
		
		for (int i = 0; i < first.length(); i++)
		{
			for (int j = 0; j < second.length(); j++)
			{
				for (int k = 1; (k + i) <= first.length()
						&& (k + j) <= second.length(); k++)
				{
					
					if (first.substring(i, k + i).equals(
							second.substring(j, k + j)))
					{
						tmp = first.substring(i, k + i);
					}
					else
					{
						if (tmp.length() > max.length())
						{
							max = tmp;
						}
						tmp = "";
					}
				}
				if (tmp.length() > max.length())
				{
					max = tmp;
				}
				tmp = "";
			}
		}
		
		return max;
	}
	
	public static void main(String[] args)
	{
		String[] names = { "Raymond Buse", "Aubry Verret", "Pieter Hooimeijer",
				"Il-Chul Yoon", "Atif Memon" };
		String[] emails = { "buse@cs.virginia.edu" }; // , "awv@gmail.com",
		// "phooi@gmail.com", "atif@cs.umd.edu", "iyoon@cs.umd.edu" };
		
		Map<String, String> map = matchNamesToEmails(makeList(names),
				makeList(emails));
		
		System.out.println(map);
	}
	
	private static Collection<String> makeList(String[] array)
	{
		LinkedList<String> list = new LinkedList<String>();
		
		for (String s : array)
		{
			list.add(s);
		}
		
		return list;
	}
	
}
