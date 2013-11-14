package plugins.adufour.vars.lang;

import icy.roi.ROI;

public class VarROIArray extends VarArray<ROI>
{
    public VarROIArray(String name)
    {
        super(name, ROI[].class, new ROI[0]);
    }
    
    /**
     * @return a pretty-printed text representation of the variable's local value (referenced
     *         variables are <b>not</b> followed). This text is used to display the value (e.g. in a
     *         graphical interface) or store the value into XML files. Overriding implementations
     *         should make sure that the result of this method is compatible with the
     *         {@link #parse(String)} method to ensure proper reloading from XML files.
     */
    public String getValueAsString()
    {
        ROI[] value = getValue();
        
        if (value == null || value.length == 0) return "No ROI";
        
        return value.length + " ROI";
    }
    
    @Override
    public ROI[] parse(String input)
    {
        return null;
    }
}
