package raykernel.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TagFile
{
	public static void main(String[] args)
	{
		TagFile t = new TagFile(new File(args[0]));
	}

	String[] comment_starters = {"%", "#"};

	private String currentTag;

	HashMap<String, List<String>> tagMap = new HashMap<String, List<String>>();

	public TagFile(File file)
	{
		readIn(file);
	}

	private void addValues(String tag, String[] values)
	{
		List<String> valueList = tagMap.get(tag);

		if (valueList == null)
		{
			valueList = new LinkedList<String>();
			tagMap.put(tag, valueList);
		}

		for (String v : values)
		{
			v = v.trim();
			if (v.length() != 0)
			{
				valueList.add(v);
			}
		}

	}

	public boolean getBoolean(String tag)
	{
		List<String> vals = tagMap.get(tag);

		if (vals == null)
			return false;

		return vals.get(0).trim().equals("true");
	}

	public String getValue(String tag)
	{
		List<String> vals = tagMap.get(tag);

		if (vals == null)
			return null;

		return vals.get(0);
	}

	public String[] getValueArray(String tag)
	{
		List<String> vals = tagMap.get(tag);

		if (vals == null)
			return null;

		String[] ret = new String[vals.size()];

		int i = 0;
		for (String s : vals)
		{
			ret[i++] = s;
		}

		return ret;
	}

	public List<String> getValues(String tag)
	{
		return tagMap.get(tag);
	}

	private void process(String line)
	{
		line = remComments(line);

		for (String tag : tagMap.keySet())
		{
			line = line.replace("$" + tag, getValue(tag));
		}

		String[] values;

		if (line.contains(":"))
		{
			String before = line.substring(0, line.indexOf(':')).trim();
			String after = line.substring(line.indexOf(':') + 1).trim();

			currentTag = before;
			values = after.split(",");
		}
		else
		{
			values = line.split(",");
		}

		if (currentTag != null)
		{
			addValues(currentTag, values);
		}

	}

	public String prune(String line, String comment)
	{
		int index = line.indexOf(comment);

		if (index < 0)
			return line;

		if (index == 0)
			return "";

		return line.substring(0, index).trim();
	}

	private void readIn(File file)
	{

		BufferedReader br;
		try
		{
			br = new BufferedReader(new FileReader(file));

			while (br.ready())
			{
				process(br.readLine());
			}

		}
		catch (Exception e)
		{
			//System.out.println("error reading benchmark file: " + e);
		}
	}

	public String remComments(String line)
	{
		for (String s : comment_starters)
		{
			line = prune(line, s);
		}

		return line;
	}

}
