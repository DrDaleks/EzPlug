package plugins.adufour.vars.lang;

import icy.roi.ROI;

public class VarROIArray extends VarGenericArray<ROI[]>
{
    public VarROIArray(String name)
    {
        super(name, ROI[].class, null);
    }
}
