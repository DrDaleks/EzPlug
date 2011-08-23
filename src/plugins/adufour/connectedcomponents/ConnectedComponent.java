package plugins.adufour.connectedcomponents;

import icy.image.IcyBufferedImage;
import icy.sequence.Sequence;
import icy.type.TypeUtil;
import icy.type.collection.array.Array1DUtil;

import java.util.ArrayList;
import java.util.Iterator;

import javax.vecmath.Point3d;
import javax.vecmath.Point3i;
import javax.vecmath.Vector3d;

import plugins.fab.trackmanager.Detection;

public class ConnectedComponent extends Detection implements Iterable<Point3i>
{
	/**
	 * True if the component is on the image edge along X
	 */
	boolean								onEdgeX;
	
	/**
	 * True if the component is on the image edge along Y
	 */
	boolean								onEdgeY;
	
	/**
	 * True if the component is on the image edge along Z
	 */
	boolean								onEdgeZ;
	
	/**
	 * the array of points in this object
	 */
	private final ArrayList<Point3i>	points;
	
	/**
	 * Sum of all points coordinates. The mass center is given by the ratio of each component by the
	 * total component size
	 */
	private final Point3d				coordsSum;
	
	/**
	 * Creates a new connected component with the specified time and size
	 * 
	 * @param t
	 * @param size
	 */
	public ConnectedComponent(int size)
	{
		super(0, 0, 0, 0);
		this.points = new ArrayList<Point3i>(size);
		coordsSum = new Point3d();
	}
	
	/**
	 * Adds a point to this component. This method also accumulates coordinates to optimize the mass
	 * center computation
	 * 
	 * @param point
	 *            the point to add
	 */
	public void addPoint(Point3i point)
	{
		points.add(point);
		
		// accumulate coordinates to compute the mass center
		coordsSum.x += point.x;
		coordsSum.y += point.y;
		coordsSum.z += point.z;
	}
	
	/**
	 * Adds a point to this component. This method also accumulates coordinates to optimize the mass
	 * center computation
	 * 
	 * @param point
	 *            the point to add
	 */
	public void removePoint(Point3i point)
	{
		points.remove(point);
		
		// accumulate coordinates to compute the mass center
		coordsSum.x -= point.x;
		coordsSum.y -= point.y;
		coordsSum.z -= point.z;
	}
	
	/**
	 * @return true is the object touches the image edge along X
	 */
	public boolean isOnEdgeX()
	{
		return onEdgeX;
	}
	
	/**
	 * @return true is the object touches the image edge along Y
	 */
	public boolean isOnEdgeY()
	{
		return onEdgeY;
	}
	
	/**
	 * @return true is the object touches the image edge along Z (always true for 2D images)
	 */
	public boolean isOnEdgeZ()
	{
		return onEdgeZ;
	}
	
	/**
	 * Computes the bounding box of this component, and stores the result into the given arguments
	 * 
	 * @param start
	 *            the first corner of the bounding box in X-Y-Z order (Upper-Left hand-Top)
	 * @param end
	 *            the second corner of the bounding box in X-Y-Z order (Lower-Right hand-Bottom)
	 */
	public void computeBoundingBox(Point3i start, Point3i end)
	{
		start.x = Integer.MAX_VALUE;
		start.y = Integer.MAX_VALUE;
		start.z = Integer.MAX_VALUE;
		
		end.x = 0;
		end.y = 0;
		end.z = 0;
		
		for (Point3i point : points)
		{
			start.x = Math.min(start.x, point.x);
			end.x = Math.max(end.x, point.x);
			
			start.y = Math.min(start.y, point.y);
			end.y = Math.max(end.y, point.y);
			
			start.z = Math.min(start.z, point.z);
			end.z = Math.max(end.z, point.z);
		}
	}
	
	/**
	 * Computes the average intensity of the component on each band of the given sequence, at the
	 * time point where this component was found
	 * 
	 * @param sequence
	 * @return an array containing the average intensity of the component in each band
	 */
	public double[] computeMeanIntensity(Sequence sequence)
	{
		return computeMeanIntensity(sequence, t);
	}
	
	/**
	 * Computes the average intensity of the component on each band of the given sequence and the
	 * specified time point
	 * 
	 * @param sequence
	 * @return an array containing the average intensity of the component in each band
	 */
	public double[] computeMeanIntensity(Sequence sequence, int t)
	{
		double[] intensitySum = new double[sequence.getSizeC()];
		
		for (Point3i point : points)
		{
			int offsetXY = point.x + point.y * sequence.getSizeX();
			
			Object dataCXY = sequence.getImage(t, point.z).getDataXYC();
			
			for (int c = 0; c < intensitySum.length; c++)
				intensitySum[c] += Array1DUtil.getValue(((Object[]) dataCXY)[c], offsetXY, sequence.isSignedDataType());
		}
		
		for (int i = 0; i < intensitySum.length; i++)
			intensitySum[i] /= points.size();
		return intensitySum;
	}
	
	/**
	 * Computes the maximum intensity of the component on each band of the given sequence, at the
	 * time point where this component was found
	 * 
	 * @param sequence
	 * @return an array containing the average intensity of the component in each band
	 */
	public double[] computeMaxIntensity(Sequence sequence)
	{
		return computeMaxIntensity(sequence, t);
	}
	
	/**
	 * Computes the maximum intensity pixel of the component on each band of the given sequence and
	 * the specified time point
	 * 
	 * @param sequence
	 * @return an array containing the average intensity of the component in each band
	 */
	public double[] computeMaxIntensity(Sequence sequence, int t)
	{
		double[] maxIntensity = new double[sequence.getSizeC()];
		
		for (Point3i point : points)
		{
			int offsetXY = point.x + point.y * sequence.getSizeX();
			
			Object dataCXY = sequence.getImage(t, point.z).getDataXYC();
			
			for (int c = 0; c < maxIntensity.length; c++)
			{
				double val = Array1DUtil.getValue(((Object[]) dataCXY)[c], offsetXY, sequence.isSignedDataType());
				if (val > maxIntensity[c])
					maxIntensity[c] = val;
			}
		}
		return maxIntensity;
	}
	
	/**
	 * Computes the closest euclidean distance between any two points of this component and the
	 * given component
	 * 
	 * @param component
	 *            the component to compute the distance to
	 * @return
	 */
	public double distanceTo(ConnectedComponent component)
	{
		double distance = Double.MAX_VALUE;
		for (Point3i srcPt : this)
		{
			Vector3d srcV = new Vector3d(srcPt.x, srcPt.y, srcPt.z);
			for (Point3i dstPt : component)
			{
				Vector3d dstV = new Vector3d(dstPt.x, dstPt.y, dstPt.z);
				dstV.sub(srcV);
				double length = dstV.length();
				if (length < distance)
					distance = length;
			}
		}
		return distance;
	}
	
	/**
	 * Returns an iterator over the internal points array. Useful to browse all points without
	 * duplicating the array
	 * 
	 * @return
	 */
	public Iterable<Point3i> getIterablePoints()
	{
		return points;
	}
	
	/**
	 * Computes the major axis of the object, which is the vector between the two most distant
	 * points of this component. Note that the result vector orientation is meaningless if the
	 * component is spherical.<br/>
	 * NOTE: this is an potentially expensive operation: O(N log N)
	 * 
	 * @return
	 */
	public Vector3d getMajorAxis()
	{
		double maxDist = 0;
		
		Vector3d vector = new Vector3d();
		Point3d pid = new Point3d(), pjd = new Point3d();
		
		int n = points.size();
		for (int i = 0; i < n - 1; i++)
		{
			Point3i pi = points.get(i);
			pid.set(pi.x, pi.y, pi.z);
			
			for (int j = i + 1; j < n; j++)
			{
				Point3i pj = points.get(j);
				pjd.set(pj.x, pj.y, pj.z);
				
				// compare the squared distance to save the square root operation
				double dist = pid.distanceSquared(pjd);
				
				if (dist > maxDist)
				{
					maxDist = dist;
					vector.sub(pjd, pid);
				}
			}
		}
		
		return vector;
	}
	
	/**
	 * Computes the mass center of this component
	 * 
	 * @return
	 */
	public Point3d getMassCenter()
	{
		Point3d massCenter = new Point3d(coordsSum);
		massCenter.scale(1.0 / getSize());
		return massCenter;
	}
	
	/**
	 * Returns the maximum distance between any point of this component and the specified point
	 * 
	 * @param point
	 * @return
	 */
	public double getMaxDistanceTo(Point3d point)
	{
		double maxDist = 0;
		
		for (Point3i p : this)
		{
			double dist = point.distance(new Point3d(p.x, p.y, p.z));
			if (dist > maxDist)
				maxDist = dist;
		}
		
		return maxDist;
	}
	
	/**
	 * The size in pixels (or voxels) of this component
	 * 
	 * @return The size in pixels (or voxels) of this component
	 */
	public int getSize()
	{
		return points.size();
	}
	
	/**
	 * Creates an array all pixels of this component. This array is a copy of the internal point
	 * array. To add or remove points, use the appropriate methods instead
	 * 
	 * @return
	 */
	public Point3i[] getPoints()
	{
		return points.toArray(new Point3i[getSize()]);
	}
	
	/**
	 * Creates a new component with the intersection between the current and specified components
	 * 
	 * @param component
	 * @return
	 */
	public ConnectedComponent intersection(ConnectedComponent component)
	{
		ConnectedComponent intersection = new ConnectedComponent(0);
		
		for (Point3i srcPt : this)
			for (Point3i dstPt : component)
				if (srcPt.equals(dstPt))
					intersection.addPoint(srcPt);
		
		return intersection;
	}
	
	@Override
	public Iterator<Point3i> iterator()
	{
		return points.iterator();
	}
	
	/**
	 * Creates a new byte sequence filled with this component. The sequence has a unique time point,
	 * and the image size is equal to the bounding box of this component.
	 * 
	 * @return
	 */
	public Sequence toSequence()
	{
		Sequence seq = new Sequence();
		
		// get the bounding box first
		
		Point3i start = new Point3i(), end = new Point3i();
		
		computeBoundingBox(start, end);
		
		// initialize the sequence
		
		int depth = 1 + end.z - start.z;
		int height = 1 + end.y - start.y;
		int width = 1 + end.x - start.x;
		
		for (int z = 0; z < depth; z++)
		{
			seq.setImage(0, z, new IcyBufferedImage(width, height, 1, TypeUtil.TYPE_BYTE));
		}
		
		// fill up the created sequence
		
		byte[][] z_xy = seq.getDataXYZAsByte(0, 0);
		
		for (Point3i point : points)
			z_xy[point.z - start.z][(point.y - start.y) * width + (point.x - start.x)] = (byte) 1;
		
		return seq;
	}
	
	@Override
	public String toString()
	{
		Point3d center = getMassCenter();
		return "[" + center.x + ", " + center.y + ", " + center.z + "], " + getSize() + " points";
	}
}
