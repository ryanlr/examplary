package raykernel.util;

public class Env
{

	public static boolean runningWindows()
	{
		// io.Out.println("os: " + System.getProperty("os.name"));
		return System.getProperty("os.name").contains("Windows");
	}

}
