package raykernel.util;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex
{
	public static List<String> getMatches(String input, String regex)
	{
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);

		List<String> ret = new LinkedList<String>();

		while (matcher.find())
		{
			String match = input.substring(matcher.start(), matcher.end());
			ret.add(match);
		}

		return ret;
	}

	public static void main(String[] args)
	{

		String regex = "General Terms\\s*:?\\s+[^:\\n]+[:\\n]";
		String target = "ABSTRACT\r\n"
				+ "In unit testing, a program is decomposed into units which are collections of functions. A part of unit can be tested by generating inputs for a single entry function. The entry function may contain pointer arguments, in which case the inputs to the unit are memory graphs. The paper addresses the problem of automating unit testing with memory graphs as inputs. The approach used builds on previous work combining symbolic and concrete execution, and more specifically, using such a combination to generate test inputs to explore all feasible execution paths. The current work develops a method to represent and track constraints that capture the behavior of a symbolic execution of a unit with memory graphs as inputs. Moreover, an efficient constraint solver is proposed to facilitate incremental generation of such test inputs. Finally, CUTE, a tool implementing the method is described together with the results of applying CUTE to real-world examples of C code. Categories and Subject Descriptors: D.2.5 [Software Engineering]: Testing and Debugging General Terms: Reliability,Verification Keywords: concolic testing, random testing, explicit path model-checking, data structure testing, unit testing, testing C programs.\r\n"
				+ "\r\n" + "1. INTRODUCTION\r\n" + "";
		String target2 = "General Terms:\r\nasd, dsgfwerg miomf, wefmfwef\r\nsefwef kemfwe";

		List<String> matches = getMatches(target, regex);

		if (matches.isEmpty())
		{
			System.out.println("no match");
		}

		for (String terms : matches)
		{
			terms = terms.replaceAll("[\\w]+:", "").replaceAll("General", "").replaceAll("Terms", "")
					.replaceAll("[\\r\\n\\.]", "");

			for (String term : terms.split(","))
			{
				System.out.println("Term: " + term);
			}
		}

	}

}
