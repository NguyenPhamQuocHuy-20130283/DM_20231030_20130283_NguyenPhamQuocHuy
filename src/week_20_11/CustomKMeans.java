package week_20_11;

import weka.clusterers.Clusterer;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.DenseInstance;
import weka.core.EuclideanDistance;
import weka.core.neighboursearch.LinearNNSearch;

public class CustomKMeans {

	private int k;
	private Instances centroids;
	private Instances data;

	public CustomKMeans(int k) {
		this.k = k;
	}

	public void buildClusterer(Instances data) throws Exception {
		this.data = data;
		initializeCentroids();
		int maxIterations = 100; // You can adjust the maximum number of iterations
		for (int iteration = 0; iteration < maxIterations; iteration++) {
			assignInstancesToClusters();
			updateCentroids();
		}
	}

	private void initializeCentroids() throws Exception {
		// Use LinearNNSearch for finding initial centroids
		LinearNNSearch nnSearch = new LinearNNSearch(data);
		centroids = new Instances(data, k);

		// Randomly select k instances as initial centroids
		for (int i = 0; i < k; i++) {
			Instance randomInstance = data.instance((int) (Math.random() * data.numInstances()));
			centroids.add(randomInstance);
		}
	}

	private void assignInstancesToClusters() throws Exception {
		EuclideanDistance euclideanDistance = new EuclideanDistance(data);
		for (int i = 0; i < data.numInstances(); i++) {
			Instance instance = data.instance(i);
			int assignedCluster = findNearestCentroid(instance, euclideanDistance);

			// Add a new class attribute if it doesn't exist
			if (data.classIndex() < 0) {
				data.insertAttributeAt(new weka.core.Attribute("cluster"), data.numAttributes());
				data.setClassIndex(data.numAttributes() - 1);
			}

			instance.setClassValue(assignedCluster);
		}
	}

	private int findNearestCentroid(Instance instance, EuclideanDistance distanceMetric) throws Exception {
		int nearestCluster = -1;
		double minDistance = Double.MAX_VALUE;

		for (int i = 0; i < centroids.numInstances(); i++) {
			double distance = distanceMetric.distance(instance, centroids.instance(i));
			if (distance < minDistance) {
				minDistance = distance;
				nearestCluster = i;
			}
		}

		return nearestCluster;
	}

	private void updateCentroids() {
		Instances newCentroids = new Instances(centroids, 0);

		// Update centroids based on the mean of instances in each cluster
		for (int i = 0; i < k; i++) {
			Instances clusterInstances = getInstancesInCluster(i);
			if (clusterInstances.numInstances() > 0) {
				Instance meanInstance = calculateMeanInstance(clusterInstances);
				newCentroids.add(meanInstance);
			}
		}

		if (newCentroids.numInstances() == k) {
			centroids.clear();
			for (int i = 0; i < k; i++) {
				centroids.add(new DenseInstance(newCentroids.instance(i)));
			}
		} else {
			System.err.println("Error: Number of new centroids does not match the expected number of clusters.");
		}
	}

	private Instances getInstancesInCluster(int clusterIndex) {
		Instances clusterInstances = new Instances(data, 0);
		for (int i = 0; i < data.numInstances(); i++) {
			Instance instance = data.instance(i);
			if (instance.classValue() == clusterIndex) {
				clusterInstances.add(instance);
			}
		}
		return clusterInstances;
	}

	private Instance calculateMeanInstance(Instances instances) {
		Instance meanInstance = new DenseInstance(instances.get(0)); // Copy the structure

		for (int i = 0; i < instances.numAttributes(); i++) {
			if (instances.attribute(i).isNumeric()) {
				double sum = 0;
				for (int j = 0; j < instances.numInstances(); j++) {
					sum += instances.instance(j).value(i);
				}
				double meanValue = sum / instances.numInstances();
				meanInstance.setValue(i, meanValue);
			}
		}

		return meanInstance;
	}

	public Instances getCentroids() {
		return centroids;
	}

	public static void main(String[] args) {
		try {
			// Load dataset
			DataSource source = new DataSource("/DM_20231030_20130283_NguyenPhamQuocHuy/data/ketqua_hoctap_sv.arff");
			Instances data = source.getDataSet();

			int k = 3;

			// Initialize and build the custom KMeans clusterer
			CustomKMeans customKMeans = new CustomKMeans(k);
			customKMeans.buildClusterer(data);

			// Print the final centroids
			Instances centroids = customKMeans.getCentroids();
			System.out.println("Final Centroids:");
			System.out.println(centroids);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
