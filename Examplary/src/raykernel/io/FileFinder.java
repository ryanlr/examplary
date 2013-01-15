package raykernel.io;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;
import java.util.List;

public class FileFinder
{

	/**
	 * Recursively find all java files
	 * 
	 * @param directory
	 * @return
	 */
	public static List<File> findAll(File directory, FileFilter filter)
	{
		File[] javaFiles = directory.listFiles(filter);
		File[] directories = directory.listFiles(new DirectoryFilter());

		LinkedList<File> srcs = new LinkedList<File>();

		if (javaFiles != null)
		{
			for (File f : javaFiles)
			{
				srcs.add(f);
			}
		}

		if (directories != null)
		{
			for (File d : directories)
			{
				srcs.addAll(findAll(d, filter));
			}
		}

		return srcs;
	}

	public static List<File> findAll(File directory, String exn)
	{
		return findAll(directory, new FileExtensionFilter(exn));
	}

	public static List<File> findFile(File searchdirectory, String startswith, String endswith)
	{
		return findAll(searchdirectory, new FileStartEndFilter(startswith, endswith));
	}
}
