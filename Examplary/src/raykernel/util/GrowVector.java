package raykernel.util;

import java.util.AbstractList;
import java.util.Vector;

public class GrowVector<T> extends AbstractList<T>
{

	Vector<T> elements = new Vector<T>();

	private void sizeTo(int size)
	{
		if (elements.size() < size)
		{
			elements.setSize(size);
		}
	}

	@Override
	public boolean add(T t)
	{
		return elements.add(t);
	}

	@Override
	public T get(int index)
	{
		sizeTo(index + 1);
		return elements.get(index);
	}

	@Override
	public T set(int index, T element)
	{
		sizeTo(index + 1);
		return elements.set(index, element);
	}

	@Override
	public int size()
	{
		return elements.size();
	}

	@Override
	public Object clone()
	{
		GrowVector<T> newVec = new GrowVector<T>();

		newVec.elements = (Vector<T>) elements.clone();

		return newVec;
	}

	public Vector<T> toVector()
	{
		return elements;
	}

}
