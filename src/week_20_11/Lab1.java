package week_20_11;

import weka.core.DistanceFunction;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.EuclideanDistance;
import weka.core.*;

public class Lab1 {
	public static void main(String[] args) {
		try {
			// Load ARFF data
			String arffFilePath = "/DM_20231030_20130283_NguyenPhamQuocHuy/data/cluster_data1.arff";
			Instances data = DataSource.read(arffFilePath);

			// Print data matrix
			System.out.println("Data Matrix:");
			System.out.println(data);

			// Calculate distance matrices
			calculateDistanceMatrix(data, new EuclideanDistance(), "Euclidean Distance");
			calculateDistanceMatrix(data, new ManhattanDistance(), "Manhattan Distance");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void calculateDistanceMatrix(Instances data, DistanceFunction distanceFunction,
			String distanceName) {
		distanceFunction.setInstances(data);
		int numInstances = data.numInstances();

		System.out.println("\n" + distanceName + " Matrix:");

		// Print column headers
		System.out.print("\t");
		for (int i = 0; i < numInstances; i++) {
			System.out.print(i + "\t");
		}
		System.out.println();

		// Calculate and print distance matrix
		for (int i = 0; i < numInstances; i++) {
			System.out.print(i + "\t");
			for (int j = 0; j < numInstances; j++) {
				double distance = distanceFunction.distance(data.instance(i), data.instance(j));
				System.out.print(String.format("%.2f", distance) + "\t");
			}
			System.out.println();
		}
	}

}
