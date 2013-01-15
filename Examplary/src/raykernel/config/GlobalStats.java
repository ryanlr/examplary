package raykernel.config;

import java.util.Date;

public class GlobalStats
{

	public static int bugCount = 0;

	public static boolean classifyMode = true;
	//static public Logger log = Logger.getLogger("bugs");
	public static int max_class_per_src = 1;
	public static int max_src_files = 200;
	public static float min_score = (float) 0.0001;
	public static int newVersions = 0;

	public static String PATH_DIFF = "\"C:\\Program Files\\GnuWin32\\bin\\diff.exe\"";

	private static long start = -1;

	public static long endTimer()
	{

		if (start < 0)
			throw new IllegalStateException("Tried to end timer before starting it!");

		Date d = new Date();
		long finish = d.getTime();

		long total = finish - start;

		start = -1;

		return total;
	}

	public static float minimiseScore(float score)
	{
		if (score < min_score)
			return 0;
		return score;
	}

	public static void startTimer()
	{
		Date d = new Date();
		start = d.getTime();
	}

}
