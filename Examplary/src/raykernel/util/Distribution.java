package raykernel.util;

public class Distribution
{

	private float count = 0;
	private float max = Float.MIN_VALUE;
	private float min = Float.MAX_VALUE;
	private float sum = 0;

	public void addFloat(float f)
	{
		if (f > max)
		{
			max = f;
		}

		if (f < min)
		{
			min = f;
		}

		count++;

		sum += f;
	}

	public void addInt(int i)
	{
		addFloat(i);
	}

	public float getMax()
	{
		return max;
	}

	public float getMean()
	{
		return sum / count;
	}

	public float getMin()
	{
		return min;
	}

	public String toString()
	{
		return "count= " + count + " max= " + max + " mean = " + getMean() + " sum = " + sum;
	}

}
