package raykernel.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Date;

public class Runner
{
	public static double runCommand(String command)
	{
		return runCommand(command, null);
	}

	public static double runCommand(String command, File dir)
	{
		Process proc;
		BufferedReader bri, bre;
		String linei, linee = null;
		Date start = null, end = null;

		try
		{
			raykernel.io.Out.println("running: " + command);

			start = new Date();

			if (dir == null)
			{
				proc = Runtime.getRuntime().exec(command);
			}
			else
			{
				proc = Runtime.getRuntime().exec(command, null, dir);
			}

			//proc.waitFor();

			bri = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			bre = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

			while ((linei = bri.readLine()) != null)
			{
				raykernel.io.Out.println(command + ": " + linei);
			}

			while ((linee = bre.readLine()) != null)
			{
				raykernel.io.Out.println(command + "[err]: " + linee);
			}

			raykernel.io.Out.println("done");

			end = new Date();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return ((end.getTime() - start.getTime())) / 1000.0;

	}
}
