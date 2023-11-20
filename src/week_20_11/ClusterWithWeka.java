package week_20_11;

import weka.core.Instance;
import weka.core.Instances;
import weka.clusterers.SimpleKMeans;
import weka.core.converters.ConverterUtils.DataSource;

public class ClusterWithWeka {

	public static void main(String[] args) {
		try {
			// Load dataset
			DataSource source = new DataSource("/DM_20231030_20130283_NguyenPhamQuocHuy/data/ketqua_hoctap_sv.arff");
			Instances data = source.getDataSet();

			// Specify the number of clusters (k)
			int k = 3;

			// Initialize SimpleKMeans with Euclidean Distance
			SimpleKMeans kMeansEuclidean = new SimpleKMeans();
			kMeansEuclidean.setNumClusters(k);
			kMeansEuclidean.setDistanceFunction(new weka.core.EuclideanDistance());
			kMeansEuclidean.buildClusterer(data);

			// Print the cluster assignments
			System.out.println("Euclidean Distance:");
			printClusterAssignments(kMeansEuclidean, data);

			// Calculate and print the sum of squared errors for Euclidean Distance
			double sumOfSquaredErrorsEuclidean = kMeansEuclidean.getSquaredError();
			System.out.println("Sum of Squared Errors (Euclidean): " + sumOfSquaredErrorsEuclidean);

			// Repeat the process for Manhattan Distance
			SimpleKMeans kMeansManhattan = new SimpleKMeans();
			kMeansManhattan.setNumClusters(k);
			kMeansManhattan.setDistanceFunction(new weka.core.ManhattanDistance());
			kMeansManhattan.buildClusterer(data);

			System.out.println("\nManhattan Distance:");
			printClusterAssignments(kMeansManhattan, data);

			double sumOfSquaredErrorsManhattan = kMeansManhattan.getSquaredError();
			System.out.println("Sum of Squared Errors (Manhattan): " + sumOfSquaredErrorsManhattan);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printClusterAssignments(SimpleKMeans kMeans, Instances data) throws Exception {
		System.out.printf("%-6s%-30s%-8s\n", "inst#", "instance", "cluster");

		for (int i = 0; i < data.numInstances(); i++) {
			int cluster = kMeans.clusterInstance(data.instance(i));
			System.out.printf("%-6d%-30s%-8d\n", (i + 1), instanceToString(data.instance(i)), (cluster + 1));
		}
	}

	private static String instanceToString(Instance instance) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < instance.numAttributes(); i++) {
			sb.append(Math.round(instance.value(i)));
			if (i < instance.numAttributes() - 1) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}

}
