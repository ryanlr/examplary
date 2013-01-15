package raykernel.util;

public class PredicateOR<T> implements Predicate<T>
{

	Predicate<T> a, b;

	public PredicateOR(Predicate<T> a, Predicate<T> b)
	{
		this.a = a;
		this.b = b;
	}

	public boolean getBoolean(T t)
	{
		return a.getBoolean(t) || b.getBoolean(t);
	}

}
