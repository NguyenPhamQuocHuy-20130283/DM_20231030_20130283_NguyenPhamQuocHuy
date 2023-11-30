package week_20_11;

public class Point {
	private double[] values;

	public Point(double[] values) {
		this.values = values;
	}

	public double[] getValues() {
		return values;
	}

	public int getDimensions() {
		return values.length;
	}

	public static double distance(Point p1, Point p2) {
		if (p1.getDimensions() != p2.getDimensions()) {
			throw new IllegalArgumentException("Points must have the same number of dimensions");
		}

		double sum = 0;
		for (int i = 0; i < p1.getDimensions(); i++) {
			double diff = p1.getValues()[i] - p2.getValues()[i];
			sum += diff * diff;
		}
		return Math.sqrt(sum);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("(");
		for (int i = 0; i < values.length; i++) {
			sb.append(values[i]);
			if (i < values.length - 1) {
				sb.append(", ");
			}
		}
		sb.append(")");
		return sb.toString();
	}
}
