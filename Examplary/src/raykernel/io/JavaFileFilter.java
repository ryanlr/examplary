package raykernel.io;

import java.io.File;
import java.io.FileFilter;

public class JavaFileFilter implements FileFilter
{

	public boolean accept(File f)
	{
		return f.getName().endsWith(".java");
	}

}
