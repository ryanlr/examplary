package raykernel.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import raykernel.config.Config;
import raykernel.io.Out;

public class ObjectStore
{
	private static String location = Config.get("objectstore") + "/";

	private static String sanitizeKey(String key)
	{
		return key.replaceAll("[\\\\=:;@$%&\\*/\\?\\+\"]", "");
	}

	public static Object get(String key)
	{
		if (Config.getBoolean("obstore_output"))
		{
			raykernel.io.Out.println("OBSTORE", "reading: " + sanitizeKey(key));
		}

		// Read from disk using FileInputStream
		Object in = null;
		try
		{
			Tools.makeDir(location);

			File f = new File(location + sanitizeKey(key) + ".dat");

			if (!f.exists())
				return null;

			FileInputStream f_in = new FileInputStream(f);

			// Read object using ObjectInputStream
			ObjectInputStream obj_in = new ObjectInputStream(f_in);

			// Read an object
			in = obj_in.readObject();

		}
		catch (Exception e)
		{
			raykernel.io.Out.exception("Obstore get error: ", e);
			return null;
		}

		if (Config.getBoolean("obstore_output"))
		{
			raykernel.io.Out.println("OBSTORE", "loaded successfully! ");
		}

		return in;
	}

	public static void main(String[] args)
	{
		/*
		String s = "hello";

		ObjectStore.put("yo", s);

		String out = (String) ObjectStore.get("yo");

		io.Out.println("world? " + out);
		*/
		//ExceptionInstance ei = new ExceptionInstance(new JClass("hi"), new JMethod(
		//"foo.util.TestB void computeMore(int,)"));
		raykernel.io.Out.println("worked");

	}
	public static void put(String key, Object o)
	{
		if (Config.getBoolean("obstore_output"))
		{
			raykernel.io.Out.println("OBSTORE", "writing: " + sanitizeKey(key));
		}

		// Write to disk with FileOutputStream
		FileOutputStream f_out;
		try
		{
			f_out = new FileOutputStream(location + sanitizeKey(key) + ".dat");

			// Write object with ObjectOutputStream
			ObjectOutputStream obj_out = new ObjectOutputStream(f_out);

			// Write object out to disk
			obj_out.writeObject(o);
		}
		catch (IOException e)
		{
			raykernel.io.Out.println("failed to write data: " + e);
		}

		if (Config.getBoolean("obstore_output"))
		{
			raykernel.io.Out.println("OBSTORE", "put: " + sanitizeKey(key));
		}
	}

	public static boolean has(String key)
	{
		if (Config.getBoolean("obstore_output"))
		{
			raykernel.io.Out.println("OBSTORE", "looking for: " + sanitizeKey(key));
		}

		// Read from disk using FileInputStream
		Object in = null;
		try
		{
			Tools.makeDir(location);

			FileInputStream f_in = new FileInputStream(location + sanitizeKey(key) + ".dat");

			// Read object using ObjectInputStream
			ObjectInputStream obj_in = new ObjectInputStream(f_in);

			// Read an object
			in = obj_in.readObject();

		}
		catch (Exception e)
		{
			Out.println("OBSTORE", "Not found");
			return false;
		}

		if (Config.getBoolean("obstore_output"))
		{
			raykernel.io.Out.println("OBSTORE", "Found!");
		}

		return true;
	}

	public static List<Object> getMatching(String regex)
	{
		if (Config.getBoolean("obstore_output"))
		{
			raykernel.io.Out.println("OBSTORE", "looking for matches for: " + regex);
		}

		File dir = Tools.makeDir(location);

		List<Object> ret = new LinkedList<Object>();

		List<File> files = Tools.ls(dir);

		Pattern p = Pattern.compile(regex);

		for (File f : files)
		{
			//System.out.println("matches? " + regex + " with " + f.getName());

			if (p.matcher(f.getName()).find())
			{
				String key = f.getName().substring(0, f.getName().length() - 4);

				//System.out.println("yes: " + key);
				ret.add(get(key));
			}
		}

		if (Config.getBoolean("obstore_output"))
		{
			raykernel.io.Out.println("OBSTORE", "found matches: " + ret);
		}

		return ret;
	}
}
