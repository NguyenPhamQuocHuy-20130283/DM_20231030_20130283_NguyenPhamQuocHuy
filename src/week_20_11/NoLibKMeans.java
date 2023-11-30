package week_20_11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NoLibKMeans {
	private int k;
	private int iterations;
	private List<Point> points;
	private List<Cluster> clusters;

	public NoLibKMeans(int k, int iterations, List<Point> points, List<Cluster> clusters) {
		this.k = k;
		this.iterations = iterations;
		this.points = points;
		this.clusters = new ArrayList<>();
	}

	public void run() {
		init();
		for (int i = 0; i < iterations; i++) {
			assignPoints();
			repositionCentroids();
		}
	}

	private void init() {
		for (int i = 0; i < k; i++) {
			Cluster cluster = new Cluster(i);
			Point centroid = points.get(i);
			cluster.setCentroid(centroid);
			clusters.add(cluster);
		}
	}

	private void assignPoints() {
		for (Point point : points) {
			double minDistance = Double.MAX_VALUE;
			int clusterIndex = 0;
			for (int i = 0; i < k; i++) {
				Cluster cluster = clusters.get(i);
				double distance = Point.distance(point, cluster.getCentroid());
				if (distance < minDistance) {
					minDistance = distance;
					clusterIndex = i;
				}
			}
			clusters.get(clusterIndex).addPoint(point);
		}
	}

	private void repositionCentroids() {
		for (Cluster cluster : clusters) {
			List<Point> points = cluster.getPoints();
			double[] centroid = new double[points.get(0).getDimensions()];
			for (Point point : points) {
				for (int i = 0; i < point.getDimensions(); i++) {
					centroid[i] += point.getValues()[i];
				}
			}
			for (int i = 0; i < centroid.length; i++) {
				centroid[i] /= points.size();
			}
			cluster.setCentroid(new Point(centroid));
			cluster.clear();
		}
	}

	public static void main(String[] args) {
		// Load data from ARFF file
		List<Point> dataPoints = loadDataFromARFF(
				"D:\\webHomework\\DM_20231030_20130283_NguyenPhamQuocHuy\\data\\ketqua_hoctap_sv.arff");

		// Create an instance of your NoLibKMeans class
		NoLibKMeans kMeans = new NoLibKMeans(3, 10, dataPoints, new ArrayList<>());

		// Run the k-means algorithm
		kMeans.run();

		// Display or analyze the results as needed
		// For example, you can print the final centroids and cluster assignments
		for (Cluster cluster : kMeans.clusters) {
			System.out.println("Cluster " + cluster.getId() + " Centroid: " + cluster.getCentroid());
			System.out.println("Points in Cluster " + cluster.getId() + ": " + cluster.getPoints());
			System.out.println();
		}
	}

	private static List<Point> loadDataFromARFF(String filePath) {
		List<Point> dataPoints = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("@data")) {
					// Start reading data instances
					while ((line = reader.readLine()) != null) {
						String[] values = line.split(",");
						double[] pointValues = new double[values.length];
						for (int i = 0; i < values.length; i++) {
							pointValues[i] = Double.parseDouble(values[i]);
						}
						dataPoints.add(new Point(pointValues));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dataPoints;
	}
}
