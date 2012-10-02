package plugins.adufour.vars.gui.model;

import java.io.File;

public class TypeSelectionModel extends ValueSelectionModel<Class<?>>
{
    public TypeSelectionModel()
    {
        super(new Class<?>[] { null, Integer.class, Double.class, int[].class, double[].class, String.class, File.class, File[].class });
    }
}
