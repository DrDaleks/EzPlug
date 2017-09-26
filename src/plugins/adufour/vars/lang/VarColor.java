package plugins.adufour.vars.lang;

import java.awt.Color;

import org.w3c.dom.Node;

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
     * Creates a new variable holding {@link Color} object
     * 
     * @param name
     * @param defaultColor
     * @param defaultListener
     *            A listener to add to this variable immediately after creation
     */
    public VarColor(String name, Color defaultColor, VarListener<Color> defaultListener)
    {
        super(name, defaultColor, defaultListener);
    }
        
    @Override
    public VarEditor<Color> createVarEditor()
    {
        return VarEditorFactory.getDefaultFactory().createColorChooser(this);
    }
    
    @Override
    public boolean saveToXML(Node node) throws UnsupportedOperationException
    {
        return super.saveToXML(node);
    }
    
    @Override
    public Color parse(String text)
    {
        if (text == null) return Color.black;
        
        // text should look like:
        // java.awt.Color[r=0,g=255,b=255]
        text = text.substring(text.indexOf('[') + 1, text.indexOf(']'));
        String[] rgb = text.split(",");
        int r = Integer.parseInt(rgb[0].substring(rgb[0].indexOf('=') + 1));
        int g = Integer.parseInt(rgb[1].substring(rgb[1].indexOf('=') + 1));
        int b = Integer.parseInt(rgb[2].substring(rgb[2].indexOf('=') + 1));
        return new Color(r, g, b);
    }
}
