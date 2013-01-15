package raykernel.util;

public abstract class CachedString
{

	protected String cached_str;
	protected boolean dirty = true;

	protected abstract String createString();

	protected void inhertCache(CachedString cs)
	{
		this.dirty = cs.dirty;
		this.cached_str = cs.cached_str + "";
	}

	public void recordChange()
	{
		dirty = true;
	}

	public String toString()
	{

		if (dirty)
		{
			cached_str = createString();
			dirty = false;
		}
		else
		{
			//io.Out.println("used cache!");
		}

		return cached_str;
	}

}
