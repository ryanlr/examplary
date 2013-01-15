package raykernel.util;

public class PredicateAND<T> implements Predicate<T>
{

	Predicate<T> a, b;

	public PredicateAND(Predicate<T> a, Predicate<T> b)
	{
		this.a = a;
		this.b = b;
	}

	public boolean getBoolean(T t)
	{
		return a.getBoolean(t) && b.getBoolean(t);
	}

}
