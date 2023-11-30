package week_20_11;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
	private int id;
	private Point centroid;
	private List<Point> points;

	public Cluster(int id) {
		this.id = id;
		this.points = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public Point getCentroid() {
		return centroid;
	}

	public void setCentroid(Point centroid) {
		this.centroid = centroid;
	}

	public List<Point> getPoints() {
		return points;
	}

	public void addPoint(Point point) {
		points.add(point);
	}

	public void clear() {
		points.clear();
	}
}
