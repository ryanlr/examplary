package raykernel.io;

import java.io.File;
import java.io.FileFilter;

public class FileStartEndFilter implements FileFilter
{

	String start, end;

	public FileStartEndFilter(String start, String end)
	{
		this.start = start;
		this.end = end;
	}

	public boolean accept(File f)
	{
		return f.getName().startsWith(start) && f.getName().endsWith(end);
	}

}
