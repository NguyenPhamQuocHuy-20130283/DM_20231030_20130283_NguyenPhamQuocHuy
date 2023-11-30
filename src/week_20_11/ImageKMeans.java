package week_20_11;

import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.InaccessibleObjectException;
import java.util.ArrayList;

public class ImageKMeans {
	public static void main(String[] args) {
		try {
			// Step 1: Load and display the image
			BufferedImage originalImage = loadImage(
					"D:\\webHomework\\DM_20231030_20130283_NguyenPhamQuocHuy\\data\\image2.jpg");
			int displayWidth = 600; // Adjust this value as needed
			int displayHeight = 600; // Adjust this value as needed
			displayImage(originalImage, "Original Image", displayWidth, displayHeight);

			System.out.println("Original Image Size: " + originalImage.getWidth() + "x" + originalImage.getHeight());

			// Step 2: Convert the image to a pixel matrix
			int width = originalImage.getWidth();
			int height = originalImage.getHeight();
			int[][][] pixelMatrix = new int[width][height][3];

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					Color color = new Color(originalImage.getRGB(x, y));
					pixelMatrix[x][y][0] = color.getRed();
					pixelMatrix[x][y][1] = color.getGreen();
					pixelMatrix[x][y][2] = color.getBlue();
				}
			}

			// Step 3: Apply k-means clustering
			int k = 5; // Change this to the desired number of clusters
			Instances instances = createInstances(pixelMatrix);
			SimpleKMeans kMeans = new SimpleKMeans();

			System.out.println("Clustering...");

			long startTime = System.currentTimeMillis();

			kMeans.setNumClusters(k);
			kMeans.buildClusterer(instances);

			long endTime = System.currentTimeMillis();
			System.out.println("Clustering Time: " + (endTime - startTime) + " milliseconds");

			// Step 4: Replace pixels with cluster centroids
			System.out.println("Image compressing...");

			BufferedImage compressedImage = createCompressedImage(originalImage, kMeans, pixelMatrix);

			System.out.println(
					"Compressed Image Size: " + compressedImage.getWidth() + "x" + compressedImage.getHeight());

			// Step 5: Display the compressed image with reduced width and height
			displayImage(compressedImage, "Compressed Image", displayWidth, displayHeight);
		} catch (Exception e) {
			// Handle specific exceptions if needed
			System.err.println("An error occurred: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static BufferedImage loadImage(String filePath) throws Exception {
		return javax.imageio.ImageIO.read(new File(filePath));
	}

	private static void displayImage(BufferedImage image, String title, int displayWidth, int displayHeight) {
		System.out.println("Success");
		javax.swing.JFrame frame = new javax.swing.JFrame(title);
		javax.swing.ImageIcon icon = new javax.swing.ImageIcon(
				image.getScaledInstance(displayWidth, displayHeight, Image.SCALE_DEFAULT));
		javax.swing.JLabel label = new javax.swing.JLabel(icon);
		frame.getContentPane().add(label);
		frame.pack();
		frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private static Instances createInstances(int[][][] pixelMatrix) {
		int width = pixelMatrix.length;
		int height = pixelMatrix[0].length;

		// Create attributes for R, G, and B
		Attribute redAttr = new Attribute("R");
		Attribute greenAttr = new Attribute("G");
		Attribute blueAttr = new Attribute("B");

		// Create an ArrayList to hold attributes
		ArrayList<Attribute> attributes = new ArrayList<>();
		attributes.add(redAttr);
		attributes.add(greenAttr);
		attributes.add(blueAttr);

		// Create Instances with the specified name, attributes, and capacity
		Instances instances = new Instances("PixelInstances", attributes, width * height);

		// Set the index of the class attribute (none in this case)
		instances.setClassIndex(-1);

		// Add instances to the dataset
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				DenseInstance instance = new DenseInstance(3);
				instance.setValue(redAttr, pixelMatrix[x][y][0]); // R
				instance.setValue(greenAttr, pixelMatrix[x][y][1]); // G
				instance.setValue(blueAttr, pixelMatrix[x][y][2]); // B
				instances.add(instance);
			}
		}

		return instances;
	}

	private static BufferedImage createCompressedImage(BufferedImage originalImage, SimpleKMeans kMeans,
			int[][][] pixelMatrix) {
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		// Step 1: Create an Instances dataset
		Instances dataset = createInstances(pixelMatrix);

		// Step 2: Apply the ReplaceMissingValues filter
		try {
			ReplaceMissingValues filter = new ReplaceMissingValues();
			filter.setInputFormat(dataset);
			Instances filteredDataset = Filter.useFilter(dataset, filter);

			// Step 3: Replace pixels with cluster centroids
			BufferedImage compressedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					int clusterIndex = kMeans.clusterInstance(filteredDataset.instance(x * height + y));
					int[] centroidValues = getCentroidValues(kMeans, clusterIndex);
					int rgb = new Color(centroidValues[0], centroidValues[1], centroidValues[2]).getRGB();
					compressedImage.setRGB(x, y, rgb);
				}
			}

			return compressedImage;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static int[] getCentroidValues(SimpleKMeans kMeans, int clusterIndex) {
		Instance centroidInstance = kMeans.getClusterCentroids().instance(clusterIndex);
		double[] centroid = centroidInstance.toDoubleArray();
		int[] centroidValues = new int[3];

		for (int i = 0; i < 3; i++) {
			centroidValues[i] = (int) (centroid[i]);
		}

		return centroidValues;
	}

}
