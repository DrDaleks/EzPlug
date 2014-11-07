package plugins.adufour.vars.lang;

import plugins.adufour.vars.util.VarListener;
import ij.ImagePlus;

public class VarImagePlus extends Var<ImagePlus>
{
    /**
     * 
     * @param name
     * @param defaultValue
     */
    public VarImagePlus(String name, ImagePlus defaultValue)
    {
        this(name, defaultValue, null);
    }

    /**
     * 
     * @param name
     * @param defaultValue
     * @param defaultListener
     *            A listener to add to this variable immediately after creation
     */
    public VarImagePlus(String name, ImagePlus defaultValue, VarListener<ImagePlus> defaultListener)
    {
        super(name, ImagePlus.class, defaultValue, defaultListener);
    }
}
