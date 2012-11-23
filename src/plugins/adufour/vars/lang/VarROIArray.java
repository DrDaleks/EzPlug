package plugins.adufour.vars.lang;

import icy.roi.ROI;

public class VarROIArray extends VarArray<ROI>
{
    public VarROIArray(String name)
    {
        super(name, ROI[].class, new ROI[0]);
    }
}
