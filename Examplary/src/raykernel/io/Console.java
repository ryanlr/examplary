package raykernel.io;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Console
{
	static BufferedReader buffer;
	static InputStreamReader reader;

	public static void pause()
	{
		System.out.print("\nPress Enter to continue . . . ");
		try
		{
			if (reader == null)
			{
				reader = new InputStreamReader(System.in);
				buffer = new BufferedReader(reader);
			}
			buffer.readLine();
		}
		catch (Exception e)
		{
			System.exit(0);
		}
	}
}
