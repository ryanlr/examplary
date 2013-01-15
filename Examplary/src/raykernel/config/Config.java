package raykernel.config;

import java.io.File;
import java.util.List;

import raykernel.util.TagFile;
import raykernel.util.Tools;

public class Config {
	private static TagFile conf = new TagFile(new File("raykernel.conf"));

	public static boolean debugMode() {
		return conf.getBoolean("debug_output");
	}

	public static String get(String key) {
		List<String> values = conf.getValues(key);

		if (values == null || values.isEmpty())
			return null;

		return values.get(0);
	}

	public static List<String> getAll(String key) {
		return conf.getValues(key);
	}

	public static File getBenchDirectory() {
		return Tools.makeDir(Config.get("benchmarkdir"));
	}

	public static boolean getBoolean(String tag) {
		return conf.getBoolean(tag);
	}

	public static String getClassPathDelimiter() {
		if (raykernel.util.Env.runningWindows())
			return ";";
		return ":";

	}

	public static File getWorkingDirectory() {
		return Tools.makeDir(Config.get("basedir") + "/work");
	}

	public static File getBaseDirectory() {
		return Tools.makeDir(Config.get("basedir"));
	}

}
