package raykernel.util;

import java.util.HashMap;

public class CharCounter
{

	HashMap<Character, Integer> countMap = new HashMap<Character, Integer>();
	//keep track of max
	char max = ' ';

	int max_val = 0;

	public void count(char c)
	{
		Integer current = countMap.get(c);
		int newval = 1;

		if (current != null)
		{
			newval = current.intValue() + 1;
		}

		countMap.put(c, newval);

		if (newval > max_val)
		{
			max_val = newval;
			max = c;
		}
	}

	public void count(String s)
	{
		for (int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			if (c != ' ' && c != '\t')
			{
				count(c);
			}
		}
	}

	public char getMaxChar()
	{
		return max;
	}

	public int getMaxVal()
	{
		return max_val;
	}
}
