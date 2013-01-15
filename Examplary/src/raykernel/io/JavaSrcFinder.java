package raykernel.io;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class JavaSrcFinder
{

	/**
	 * Recursively find all java files
	 * 
	 * @param directory
	 * @return
	 */
	public static List<File> findAll(File directory)
	{
		File[] javaFiles = directory.listFiles(new JavaFileFilter());
		File[] directories = directory.listFiles(new DirectoryFilter());

		LinkedList<File> srcs = new LinkedList<File>();

		for (File f : javaFiles)
		{
			srcs.add(f);
		}

		for (File d : directories)
		{
			srcs.addAll(findAll(d));
		}

		return srcs;
	}

}
