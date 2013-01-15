package raykernel.io;

import java.io.File;
import java.io.FileFilter;

public class DirectoryFilter implements FileFilter
{

	public boolean accept(File f)
	{
		return f.isDirectory();
	}

}
