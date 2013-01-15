package raykernel.lang.dom.condition.simplify;

import java.io.PrintStream;

/**
 * A subclass of PrintStream whose methods ignore the content 
 * being written. 
 */
public class NullPrintStream extends PrintStream
{
	
	/**
	 * Creates a null print stream that does not print anything.
	 */
	public NullPrintStream()
	{
		super(System.out);
	}
	
	/**
	 * This methods does not print anything.
	 */
	@Override
	public synchronized void write(byte[] b, int off, int len)
	{
	}
	
	/**
	 * This methods does not print anything.
	 */
	@Override
	public synchronized void write(int b)
	{
	}
	
	/**
	 * This methods does not print anything.
	 */
	private void printLine()
	{
	}
	
}