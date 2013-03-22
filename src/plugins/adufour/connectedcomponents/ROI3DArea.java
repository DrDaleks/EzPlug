package plugins.adufour.connectedcomponents;

import icy.canvas.IcyCanvas;
import icy.roi.ROI2DArea;
import icy.roi.ROI2DArea.ROI2DAreaPainter;
import icy.roi.ROI3D;
import icy.sequence.Sequence;
import icy.type.rectangle.Rectangle3D;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.TreeMap;

public class ROI3DArea extends ROI3D
{
	private final TreeMap<Integer, ROI2DArea> slices = new TreeMap<Integer, ROI2DArea>();

	public ROI3DArea()
	{
		// TODO Auto-generated constructor stub
	}

	public ROI3DArea(Point2D pt)
	{
		// addPoint(pt.getX(), pt.getY(), 0.0);
	}

	@Override
	public Rectangle3D getBounds3D()
	{
		Rectangle2D xyBounds = slices.firstEntry().getValue().getBounds2D();
		int z = slices.firstKey();
		int sizeZ = slices.size();

		for (ROI2DArea slice : slices.subMap(1, sizeZ).values())
			xyBounds = xyBounds.createUnion(slice.getBounds2D());

		return new Rectangle3D.Double(xyBounds.getMinX(), xyBounds.getMinY(), z, xyBounds.getWidth(), xyBounds.getHeight(), sizeZ);
	}

	@Override
	protected ROIPainter createPainter()
	{
		return new ROI3DAreaPainter();
	}

	@Override
	public boolean contains(double x, double y, double z, double t, double c)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(double x, double y, double z, double t, double c, double sizeX, double sizeY, double sizeZ, double sizeT, double sizeC)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getPerimeter()
	{
		// perimeter = sum of perimeters over all slices except first and last,
		// for which the volume is added

		double perimeter = 0;

		if (slices.size() <= 2)
		{
			for (ROI2DArea slice : slices.values())
				perimeter += slice.getVolume();
		}
		else
		{

			perimeter = slices.firstEntry().getValue().getVolume();

			for (ROI2DArea slice : slices.subMap(1, slices.lastKey() - 1).values())
				perimeter += slice.getPerimeter();

			perimeter += slices.lastEntry().getValue().getVolume();
		}
		return perimeter;

	}

	@Override
	public double getVolume()
	{
		double volume = 0;

		for (ROI2DArea slice : slices.values())
			volume += slice.getVolume();

		return volume;
	}

	/**
	 * Adds the specified point to this ROI
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void addPoint(int x, int y, int z)
	{
		if (!slices.containsKey(z))
		{
			ROI2DArea area = new ROI2DArea();
			area.setZ(z);
			slices.put(z, area);
		}

		slices.get(z).addPoint(x, y);
	}

	public class ROI3DAreaPainter extends ROIPainter
	{
		@Override
		public void paint(Graphics2D g, Sequence sequence, IcyCanvas canvas)
		{
			int z = canvas.getPositionZ();

			if (z >= 0 && slices.containsKey(z))
			{
				ROI2DAreaPainter painter = slices.get(z).getPainter();
				if (!painter.isAttached(sequence)) painter.attachTo(sequence);
				painter.paint(g, sequence, canvas);
			}
		}
	}
}
