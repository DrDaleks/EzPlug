package plugins.adufour.connectedcomponents;

import icy.image.IcyBufferedImage;
import icy.sequence.Sequence;
import icy.type.DataType;
import icy.type.collection.array.Array1DUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.vecmath.Point3d;
import javax.vecmath.Point3i;
import javax.vecmath.Vector3d;

import plugins.nchenouard.spot.Detection;

public class ConnectedComponent extends Detection implements Iterable<Point3i>
{
    /**
     * True if the component is on the image edge along X
     */
    boolean                          onEdgeX;
    
    /**
     * True if the component is on the image edge along Y
     */
    boolean                          onEdgeY;
    
    /**
     * True if the component is on the image edge along Z
     */
    boolean                          onEdgeZ;
    
    private boolean                  coordsDirty;
    
    /**
     * the array of points in this object
     */
    private final ArrayList<Point3i> points;
    
    /**
     * the array of contour points of this object
     */
    private Point3i[]                contourPoints;
    
    /**
     * Sum of all points coordinates. The mass center is given by the ratio of each component by the
     * total component size
     */
    private final Point3d            coordsSum;
    
    private final ConnectedComponentDescriptor    shapeDescriptor = new ConnectedComponentDescriptor();
    
    /**
     * Creates a new connected component with given initial capacity
     * 
     * @param initialCapacity
     */
    public ConnectedComponent(int initialCapacity)
    {
        super(0, 0, 0, 0);
        this.points = new ArrayList<Point3i>(initialCapacity);
        coordsSum = new Point3d();
    }
    
    void addPointInternal(Point3i point)
    {
        points.add(point);
        
        // accumulate coordinates to compute the mass center
        coordsSum.x += point.x;
        coordsSum.y += point.y;
        coordsSum.z += point.z;
        
        coordsDirty = true;
    }
    
    /**
     * Adds a point to this component and updates the mass center
     * 
     * @param point
     *            the point to add
     */
    public void addPoint(Point3i point)
    {
        addPointInternal(point);
        updateDetectionCoords();
    }
    
    /**
     * Adds a point to this component. This method also accumulates coordinates to optimize the mass
     * center computation
     * 
     * @param point
     *            the point to add
     */
    void removePointInternal(Point3i point)
    {
        if (!points.remove(point)) return;
        
        // accumulate coordinates to compute the mass center
        coordsSum.x -= point.x;
        coordsSum.y -= point.y;
        coordsSum.z -= point.z;
        
        coordsDirty = true;
    }
    
    public void removeAllPoints()
    {
        points.clear();
        coordsSum.set(0, 0, 0);
        x = 0;
        y = 0;
        z = 0;
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
     * @deprecated Use
     *             {@link ConnectedComponentDescriptor#computeBoundingBox(ConnectedComponent, Point3i, Point3i)}
     *             instead
     */
    public void computeBoundingBox(Point3i start, Point3i end)
    {
        shapeDescriptor.computeBoundingBox(this, start, end);
    }
    
    /**
     * Computes the average intensity of the component on each channel of the given sequence, at the
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
     * Computes the average intensity of the component on each channel of the given sequence and the
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
                intensitySum[c] += Array1DUtil.getValue(((Object[]) dataCXY)[c], offsetXY, sequence.getDataType_().isSigned());
        }
        
        for (int i = 0; i < intensitySum.length; i++)
            intensitySum[i] /= points.size();
        return intensitySum;
    }
    
    /**
     * Computes the minimum intensity of the component on each channel of the given sequence, at the
     * time point where this component was found
     * 
     * @param sequence
     * @return an array containing the average intensity of the component in each band
     */
    public double[] computeMinIntensity(Sequence sequence)
    {
        return computeMinIntensity(sequence, t);
    }
    
    /**
     * Computes the minimum intensity of the component on each channel of the given sequence and the
     * specified time point
     * 
     * @param sequence
     * @return an array containing the average intensity of the component in each band
     */
    public double[] computeMinIntensity(Sequence sequence, int t)
    {
        double[] minIntensity = new double[sequence.getSizeC()];
        Arrays.fill(minIntensity, Double.MAX_VALUE);
        
        for (Point3i point : points)
        {
            int offsetXY = point.x + point.y * sequence.getSizeX();
            
            Object dataCXY = sequence.getImage(t, point.z).getDataXYC();
            
            for (int c = 0; c < minIntensity.length; c++)
            {
                double val = Array1DUtil.getValue(((Object[]) dataCXY)[c], offsetXY, sequence.getDataType_().isSigned());
                if (val < minIntensity[c]) minIntensity[c] = val;
            }
        }
        return minIntensity;
    }
    
    /**
     * Computes the maximum intensity of the component on each channel of the given sequence, at the
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
     * Computes the maximum intensity pixel of the component on each channel of the given sequence
     * and the specified time point
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
                double val = Array1DUtil.getValue(((Object[]) dataCXY)[c], offsetXY, sequence.getDataType_().isSigned());
                if (val > maxIntensity[c]) maxIntensity[c] = val;
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
                if (length < distance) distance = length;
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
    
    void updateDetectionCoords()
    {
        double factor = 1.0 / getSize();
        x = coordsSum.x * factor;
        y = coordsSum.y * factor;
        z = coordsSum.z * factor;
        coordsDirty = false;
    }
    
    @Override
    public double getX()
    {
        if (coordsDirty) updateDetectionCoords();
        return x;
    }
    
    @Override
    public double getY()
    {
        if (coordsDirty) updateDetectionCoords();
        return y;
    }
    
    @Override
    public double getZ()
    {
        if (coordsDirty) updateDetectionCoords();
        return z;
    }
    
    /**
     * Computes the mass center of this component
     * 
     * @return
     */
    public Point3d getMassCenter()
    {
        if (coordsDirty) updateDetectionCoords();
        return new Point3d(x, y, z);
        // Point3d massCenter = new Point3d(coordsSum);
        // massCenter.scale(1.0 / getSize());
        // return massCenter;
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
            if (dist > maxDist) maxDist = dist;
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
     * @return An array containing all the pixels of this component
     */
    public Point3i[] getPoints()
    {
        return points.toArray(new Point3i[getSize()]);
    }
    
    /**
     * @param outputSequence
     *            (set to null if not wanted) an output sequence to receive the extracted contour
     * @return An array containing all the contour pixels of this component
     */
    public Point3i[] getContourPoints(Sequence outputSequence)
    {
        if (contourPoints == null)
        {
            ArrayList<Point3i> list = new ArrayList<Point3i>(getSize() / 2);
            
            Point3i min = new Point3i(), max = new Point3i();
            shapeDescriptor.computeBoundingBox(this, min, max);
            
            Sequence mask = toSequence();
            int w = mask.getSizeX();
            int h = mask.getSizeY();
            int d = mask.getSizeZ();
            
            byte[][] outputMask = null;
            
            if (outputSequence != null)
            {
                outputSequence.removeAllImage();
                for (int i = 0; i < d; i++)
                {
                    outputSequence.setImage(0, i, new IcyBufferedImage(w, h, 1, DataType.UBYTE));
                }
                
                outputMask = outputSequence.getDataXYZAsByte(0, 0);
            }
            
            byte[][] mask_z_xy = mask.getDataXYZAsByte(0, 0);
            
            Point3i localP = new Point3i();
            
            if (min.z != max.z)
            {
                mainLoop: for (Point3i p : points)
                {
                    localP.sub(p, min);
                    
                    int xy = localP.y * w + localP.x;
                    
                    if (localP.x == 0 || localP.y == 0 || localP.x == w - 1 || localP.y == h - 1 || localP.z == 0 || localP.z == d - 1)
                    {
                        list.add(p);
                        if (outputMask != null) outputMask[localP.z][xy] = (byte) 1;
                        continue;
                    }
                    
                    for (byte[] z : new byte[][] { mask_z_xy[localP.z - 1], mask_z_xy[localP.z], mask_z_xy[localP.z + 1] })
                        if (z[xy - w] == 0 || z[xy - 1] == 0 || z[xy + 1] == 0 || z[xy + w] == 0 || z[xy - w - 1] == 0 || z[xy - w + 1] == 0 || z[xy + w - 1] == 0 || z[xy + w + 1] == 0)
                        {
                            list.add(p);
                            if (outputMask != null) outputMask[localP.z][xy] = (byte) 1;
                            continue mainLoop;
                        }
                    
                    // the top and bottom neighbors were forgotten in the previous loop
                    if (mask_z_xy[localP.z - 1][xy] == 0 || mask_z_xy[localP.z + 1][xy] == 0)
                    {
                        list.add(p);
                    }
                }
            }
            else
            {
                for (Point3i p : points)
                {
                    localP.sub(p, min);
                    
                    int xy = localP.y * w + localP.x;
                    
                    if (localP.x == 0 || localP.y == 0 || localP.x == w - 1 || localP.y == h - 1)
                    {
                        list.add(p);
                        if (outputMask != null) outputMask[localP.z][xy] = (byte) 1;
                        continue;
                    }
                    
                    byte[] z = mask_z_xy[localP.z];
                    
                    if (z[xy - w] == 0 || z[xy - 1] == 0 || z[xy + 1] == 0 || z[xy + w] == 0)
                    // || z[xy - w - 1] == 0
                    // || z[xy - w + 1] == 0
                    // || z[xy + w - 1] == 0
                    // || z[xy + w + 1] == 0)
                    {
                        list.add(p);
                        if (outputMask != null) outputMask[localP.z][xy] = (byte) 1;
                    }
                }
            }
            
            contourPoints = list.toArray(new Point3i[list.size()]);
        }
        
        return contourPoints;
    }
    
    /**
     * @param component
     * @return true if this component intersects (e.g. has at least one voxel overlapping with) the
     *         specified component
     */
    public boolean intersects(ConnectedComponent component)
    {
        int thisSize = getSize();
        int componentSize = component.getSize();
        
        Point3i thisPt = new Point3i();
        Point3i componentPt = new Point3i();
        
        for (int i = 0; i < thisSize; i++)
        {
            thisPt.set(points.get(i));
            for (int j = 0; j < componentSize; j++)
            {
                componentPt.set(component.points.get(j));
                if (thisPt.equals(componentPt)) return true;
            }
        }
        
        return false;
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
                if (srcPt.equals(dstPt)) intersection.addPointInternal(srcPt);
        
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
            seq.setImage(0, z, new IcyBufferedImage(width, height, 1, DataType.UBYTE));
        }
        
        // fill up the created sequence
        
        byte[][] z_xy = seq.getDataXYZAsByte(0, 0);
        
        for (Point3i point : points)
            z_xy[point.z - start.z][(point.y - start.y) * width + (point.x - start.x)] = (byte) 1;
        
        return seq;
    }
    
    /**
     * Paints this component onto the given sequence with the specified value
     * 
     * @param s
     * @param value
     */
    public void paintOnSequence(Sequence s, int t, int c, double value)
    {
        Object z_xy = s.getDataXYZ(t, c);
        
        int width = s.getSizeX();
        
        for (Point3i point : points)
            Array1DUtil.setValue(Array.get(z_xy, point.z), point.y * width + point.x, s.getDataType_(), value);
    }
    
    @Override
    public String toString()
    {
        return "[" + getX() + ", " + getY() + ", " + getZ() + "], " + getSize() + " points";
    }
}
