package raykernel.util;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import raykernel.config.Config;
import raykernel.io.Out;

public class ParallelRunner implements Runnable
{
	static int corePoolSize = Integer.parseInt(Config.get("max_threads"));
	static int maxPoolSize = 9999;
	static long keepAliveTime = Long.MAX_VALUE;
	static BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();

	static ThreadPoolExecutor executer = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime,
			TimeUnit.MINUTES, queue);

	String command;
	File dir;

	private ParallelRunner(String command, File dir)
	{
		this.command = command;
		this.dir = dir;
	}

	public static void runCommand(String command, File dir)
	{
		executer.submit(new ParallelRunner(command, dir));
		Out.println("submitted job: " + command + " queuesize = " + executer.getTaskCount());

	}

	public void run()
	{
		Runner.runCommand(command, dir);
	}

	/**
	 * Test
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		for (int i = 0; i < 100; i++)
		{
			ParallelRunner.runCommand("C:\\Windows\\System32\\ping.exe www.google.com", null);
		}
	}

	public static void runCommand(String command)
	{
		runCommand(command, null);
	}

}
