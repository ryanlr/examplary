package raykernel.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileReader
{
	public static String readFile(File file)
	{
		BufferedReader br;
		try
		{
			br = new BufferedReader(new java.io.FileReader(file));
		}
		catch (FileNotFoundException e)
		{
			//System.err.println("File: " + file + "Not Found!");
			return "";
		}
		StringBuffer contents = new StringBuffer();

		try
		{
			while (br.ready())
			{
				contents.append(br.readLine() + "\n");
			}
		}
		catch (IOException e)
		{
			System.err.println("ioexception: " + e);
			return "";
		}

		return contents.toString();
	}
}
