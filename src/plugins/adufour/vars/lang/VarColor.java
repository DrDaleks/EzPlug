package plugins.adufour.vars.lang;

import ome.xml.model.primitives.Color;
import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.util.VarListener;

public class VarColor extends Var<Color>
{
    /**
     * Creates a new variable holding a OME-compatible {@link Color} object
     * 
     * @param name
     * @param defaultColor
     */
    public VarColor(String name, Color defaultColor)
    {
        this(name, defaultColor, null);
    }
    
    /**
     * Convenience constructor that allows the default color to be given as a Java AWT
     * {@link java.awt.Color Color} object
     * 
     * @param name
     * @param defaultColor
     */
    public VarColor(String name, java.awt.Color defaultColor)
    {
        this(name, new Color(defaultColor.getRed(), defaultColor.getGreen(), defaultColor.getBlue(), defaultColor.getAlpha()), null);
    }
    
    /**
     * Convenience constructor that allows the default color to be given as a Java AWT
     * {@link java.awt.Color Color} object
     * 
     * @param name
     * @param defaultColor
     * @param defaultListener
     *            A listener to add to this variable immediately after creation
     */
    public VarColor(String name, java.awt.Color defaultColor, VarListener<Color> defaultListener)
    {
        this(name, new Color(defaultColor.getRed(), defaultColor.getGreen(), defaultColor.getBlue(), defaultColor.getAlpha()), defaultListener);
    }
    
    /**
     * Creates a new variable holding a OME-compatible {@link Color} object
     * 
     * @param name
     * @param defaultColor
     */
    public VarColor(String name, Color defaultColor, VarListener<Color> defaultListener)
    {
        super(name, defaultColor, defaultListener);
    }
    
    /**
     * @return the variable's value converted into a Java AWT {@link java.awt.Color Color} object
     */
    public java.awt.Color getAWTColor()
    {
        Color color = getValue();
        
        return new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
    
    @Override
    public VarEditor<Color> createVarEditor()
    {
        return VarEditorFactory.getDefaultFactory().createColorChooser(this);
    }
}
