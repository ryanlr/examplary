package raykernel.io;

import raykernel.config.Config;

public class Out
{
	public static final String DEBUG = "DEBUG";
	public static final String DEFAULT = "DEFAULT";
	public static final String ERROR = "ERROR";
	public static final String EXCEPTION = "EXCEPTION";

	public static void debug(String line)
	{
		if (Config.debugMode())
		{
			println(DEBUG, line);
		}
	}

	public static void error(String string)
	{
		println(ERROR, string);
	}

	public static void exception(String string, Throwable e)
	{
		println(EXCEPTION, string + " : " + e);
	}

	public static String getCaller()
	{
		StackTraceElement[] frames = Thread.currentThread().getStackTrace();

		StackTraceElement caller = frames[3];

		return caller.getClassName() + ":" + caller.getMethodName();

	}

	public static void print(String key, String line)
	{
		if (key == DEFAULT)
		{
			System.out.print(line);
		}
		else
		{
			System.out.print("[" + key + "] " + line);
		}
	}

	public static void println()
	{
		System.out.println();
	}

	public static void println(Object o)
	{
		if (Config.getBoolean("print_caller"))
		{
			println(getCaller(), o.toString());
		}
		else
		{
			println(DEFAULT, o.toString());
		}
	}

	public static void println(String key, String line)
	{
		print(key, line + "\n");
	}

	public static void verbose(Object o)
	{
		if (Config.getBoolean("verbose"))
		{
			println(getCaller(), o.toString());
		}
	}

	public static void print(String line)
	{
		print("", line);

	}

}
