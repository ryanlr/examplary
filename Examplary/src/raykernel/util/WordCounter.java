package raykernel.util;

import java.util.HashMap;

public class WordCounter
{

	HashMap<String, Integer> countMap = new HashMap<String, Integer>();
	//keep track of max
	String max = "";

	int max_val = 0;

	public void countWord(String s)
	{
		if (s.length() < 2)
			return;

		Integer current = countMap.get(s);
		int newval = 1;

		if (current != null)
		{
			newval = current.intValue() + 1;
		}

		countMap.put(s, newval);

		if (newval > max_val)
		{
			max_val = newval;
			max = s;
		}
	}

	public int getMaxVal()
	{
		return max_val;
	}

	public String getMaxWord()
	{
		return max;
	}
}