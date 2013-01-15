package raykernel.lang.dom.condition;

import raykernel.config.Config;

public class ReadabilityEnhancer
{

	public static String enhance(String in)
	{
		if (!Config.getBoolean("policy-readenhance"))
			return in;

		String out = in.replace(".iterator().hasNext()", " non empty");
		out = out.replace(".iterator().next()", "->{some element}");
		out = out.replace(".this$0.", ".");
		out = out.replace("this.", "");
		out = out.replace(" .", " ");
		out = out.replace(".iterator().next() instanceof", " contains type");

		if (out.contains("StringBuilder.append(") && out.contains("toString()"))
		{
			out = out.replace("StringBuilder.append(", "");
			out = out.replace(".toString()", "");
			out = out.replace(").append(", " + ");

			out = out.trim();

			if (out.endsWith(")"))
			{
				out = out.substring(0, out.length() - 1);
			}

		}

		out = out.replace("\\", "");

		return out;
	}

}
