package plugins.adufour.connectedcomponents;

import icy.canvas.IcyCanvas;
import icy.painter.PainterAdapter;
import icy.roi.ROI2D;
import icy.sequence.Sequence;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Map;

import javax.vecmath.Point3d;

public class ConnectedComponentsPainter extends PainterAdapter
{
	private final Map<Integer, List<ConnectedComponent>> components;

	public ConnectedComponentsPainter(Map<Integer, List<ConnectedComponent>> components)
	{
		this.components = components;
	}

	@Override
	public void paint(Graphics2D g, Sequence sequence, IcyCanvas canvas)
	{
		int fontSize = (int) Math.round(ROI2D.canvasToImageLogDeltaX(canvas, 20));
		if (fontSize < 1) fontSize = 1;
		Font font = new Font("Arial", Font.BOLD, fontSize);
		g.setFont(font);
		g.setColor(Color.red);

		int cpt = 1;
		for (ConnectedComponent cc : components.get(canvas.getPositionT()))
		{
			Point3d center = cc.getMassCenter();
			g.drawString("x " + cpt++, Math.round(center.x), 1 + Math.round(center.y));
		}
	}
}
