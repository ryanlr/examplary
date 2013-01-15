package raykernel.util;

public class PredicateNOT<T> implements Predicate<T>
{

	Predicate<T> not;

	public PredicateNOT(Predicate<T> not)
	{
		this.not = not;
	}

	public boolean getBoolean(T t)
	{
		return !(not.getBoolean(t));
	}

}