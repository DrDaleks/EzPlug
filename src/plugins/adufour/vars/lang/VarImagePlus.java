package plugins.adufour.vars.lang;

import ij.ImagePlus;

public class VarImagePlus extends Var<ImagePlus>
{
    public VarImagePlus(String name, ImagePlus defaultValue)
    {
        super(name, ImagePlus.class, defaultValue);
    }
}
