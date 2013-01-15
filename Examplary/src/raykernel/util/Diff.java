package raykernel.util;

public class Diff
{

	public static String diff(String string1, String string2)
	{
		StringBuffer result = new StringBuffer();

		String[] x = string1.split("\\n");
		String[] y = string2.split("\\n");

		// number of lines of each file
		int M = x.length;
		int N = y.length;

		// opt[i][j] = length of LCS of x[i..M] and y[j..N]
		int[][] opt = new int[M + 1][N + 1];

		// compute length of LCS and all subproblems via dynamic programming
		for (int i = M - 1; i >= 0; i--)
		{
			for (int j = N - 1; j >= 0; j--)
			{
				if (x[i].equals(y[j]))
				{
					opt[i][j] = opt[i + 1][j + 1] + 1;
				}
				else
				{
					opt[i][j] = Math.max(opt[i + 1][j], opt[i][j + 1]);
				}
			}
		}

		// recover LCS itself and print out non-matching lines to standard output
		int i = 0, j = 0;
		while (i < M && j < N)
		{
			if (x[i].equals(y[j]))
			{
				i++;
				j++;
			}
			else if (opt[i + 1][j] >= opt[i][j + 1])
			{
				result.append("< " + x[i++] + "\n");
			}
			else
			{
				result.append("> " + y[j++] + "\n");
			}
		}

		// dump out one remainder of one string if the other is exhausted
		while (i < M || j < N)
		{
			if (i == M)
			{
				result.append("> " + y[j++] + "\n");
			}
			else if (j == N)
			{
				result.append("< " + x[i++] + "\n");
			}
		}

		return result.toString();
	}

}