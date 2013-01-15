package raykernel.io;

import java.io.File;
import java.io.FileFilter;

public class FileExtensionFilter implements FileFilter
{

	String exn = ".java";

	public FileExtensionFilter(String exn)
	{
		this.exn = "." + exn;
	}

	public boolean accept(File f)
	{
		return f.getName().endsWith(exn);
	}

}
