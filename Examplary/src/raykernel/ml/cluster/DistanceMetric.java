package raykernel.ml.cluster;

public interface DistanceMetric<T>
{
	public double getDistance(T t1, T t2);
}
