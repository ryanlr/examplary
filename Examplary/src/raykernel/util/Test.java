package raykernel.util;

//
//Test.java
//
//
//Created by Timothy W. Hnat on 1/24/08.
//

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Test
{
	public static void main(String[] args) throws IOException
	{
		Scanner s = null;
		try
		{
			s = new Scanner(new BufferedReader(new FileReader("resources")));

			while (s.hasNext())
			{
				raykernel.io.Out.println(s.next());
			}
		}
		catch (Exception e)
		{

		}
		finally
		{
			if (s != null)
			{
				s.close();
			}
		}
	}
}