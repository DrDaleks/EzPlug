package plugins.adufour.connectedcomponents;

import icy.plugin.abstract_.Plugin;
import icy.plugin.interface_.PluginBundled;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;

import plugins.adufour.blocks.lang.Block;
import plugins.adufour.blocks.util.BlockInfo;
import plugins.adufour.blocks.util.VarList;
import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.lang.VarDouble;

public class CCshape extends Plugin implements PluginBundled, Block, BlockInfo
{
	Var<ConnectedComponent>	varCC		= new Var<ConnectedComponent>("component", ConnectedComponent.class);
	
	VarDouble				sphericity	= new VarDouble("sphericity", 0.0);
	
	VarDouble				longAxis	= new VarDouble("long diameter", 0.0);
	
	VarDouble				shortAxis	= new VarDouble("short diameter", 0.0);
	
	@Override
	public void run()
	{
		ConnectedComponent cc = varCC.getValue();
		
		if (cc == null) return;
		
		sphericity.setValue(ShapeDescriptor.computeSphericity(cc));
		
		try
		{
			if (ShapeDescriptor.is2D(cc))
			{
				Point2d radii = new Point2d();
				ShapeDescriptor.computeEllipse(cc, null, radii, null, null);
				longAxis.setValue(radii.x * 2.0);
				shortAxis.setValue(radii.y * 2.0);
			}
			else
			{
				Point3d radii = new Point3d();
				ShapeDescriptor.computeEllipse(cc, null, radii, null, null);
				longAxis.setValue(radii.x * 2.0);
				shortAxis.setValue(radii.y * 2.0);
			}
		}
		catch (Exception e)
		{
			
		}
	}
	
	@Override
	public void declareInput(VarList inputMap)
	{
		inputMap.add(varCC);
	}
	
	@Override
	public void declareOutput(VarList outputMap)
	{
		outputMap.add(longAxis);
		outputMap.add(shortAxis);
		outputMap.add(sphericity);
	}

    @Override
    public String getMainPluginClassName()
    {
        return ConnectedComponents.class.getCanonicalName();
    }

    @Override
    public String getName()
    {
        return "Shape Descriptor";
    }

    @Override
    public String getDescription()
    {
        return "Simple shape descriptor (more descriptors coming soon)";
    }
	
}
