package raykernel.util;

public class MathBox
{
	public static float max(float first, float second)
	{
		if (first > second)
			return first;
		return second;
	}

	public static float min(float first, float second)
	{
		if (first < second)
			return first;
		return second;
	}
}
