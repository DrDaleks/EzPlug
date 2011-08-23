package plugins.adufour.connectedcomponents;

import icy.canvas.IcyCanvas;
import icy.painter.PainterAdapter;
import icy.roi.ROI2D;
import icy.sequence.Sequence;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Map;

public class ConnectedComponentsPainter extends PainterAdapter
{
	private final Map<Integer, List<ConnectedComponent>>	components;
	
	public ConnectedComponentsPainter(Map<Integer, List<ConnectedComponent>> components)
	{
		this.components = components;
	}
	
	@Override
	public void paint(Graphics2D g, Sequence sequence, IcyCanvas canvas)
	{
		int fontSize = (int) ROI2D.canvasToImageLogDeltaX(canvas, 20);
		if (fontSize < 1)
			fontSize = 1;
		Font font = new Font("Arial", Font.BOLD, fontSize);
		g.setFont(font);
		
		int cpt = 1;
		for (ConnectedComponent cc : components.get(canvas.getT()))
		{
			g.drawString("#" + cpt++, (int) cc.getMassCenter().x, (int) cc.getMassCenter().y);
		}
	}
}
