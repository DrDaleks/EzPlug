package plugins.adufour.vars.lang;

import java.io.File;

import org.w3c.dom.Node;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.swing.FilesChooser;
import plugins.adufour.vars.util.VarListener;

/**
 * Specialized implementation of Var to manipulate file arrays
 * 
 * @author Alexandre Dufour
 */
public class VarFileArray extends VarGenericArray<File[]>
{
    /**
     * @param name
     * @param defaultValues
     */
    public VarFileArray(String name, File[] defaultValues)
    {
        this(name, defaultValues, null);
    }
    
    /**
     * @param name
     * @param defaultValues
     * @param defaultListener
     *            A listener to add to this variable immediately after creation
     */
    public VarFileArray(String name, File[] defaultValues, VarListener<File[]> defaultListener)
    {
        super(name, File[].class, defaultValues, defaultListener);
    }
    
    @Override
    public String getSeparator(int dimension)
    {
        return File.pathSeparator;
    }
    
    /**
     * Saves the current variable to the specified node
     * 
     * @throws UnsupportedOperationException
     *             if the functionality is not supported by the current variable type
     */
    @Override
    public boolean saveToXML(Node node) throws UnsupportedOperationException
    {
        // TODO a pointed variable will be replaced by its value. Does this make sense ?
        node.setNodeValue(getValueAsString());
        return true;
    }
    
    @Override
    public VarEditor<File[]> createVarEditor()
    {
        return new FilesChooser(this);
    }
    
    @Override
    public Object parseComponent(String s)
    {
        return new java.io.File(s);
    }
}
