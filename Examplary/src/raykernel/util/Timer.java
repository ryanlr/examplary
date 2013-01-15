package raykernel.util;

import java.util.Date;
import java.util.Vector;

public class Timer
{

	static Date begin;

	static Vector<Tuple<String, Float>> laps = new Vector<Tuple<String, Float>>();

	public static void lap(String s)
	{
		if (begin == null)
		{
			start();
		}

		Date now = new Date();

		laps.add(new Tuple<String, Float>(s, ((now.getTime() - begin.getTime())) / 1000f));

		begin = now;
	}

	public static void printAll()
	{
		raykernel.io.Out.println("\n    Run Time Summary");
		raykernel.io.Out.println("-------------------------");
		for (Tuple<String, Float> t : laps)
		{
			raykernel.io.Out.println(t.first + ":\t" + t.second + "s");
		}
		raykernel.io.Out.println("-------------------------");
	}

	public static void start()
	{
		begin = new Date();
	}

}
