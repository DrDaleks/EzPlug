package plugins.adufour.vars.lang;

import java.io.File;

import org.w3c.dom.Node;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.swing.FileChooser;
import plugins.adufour.vars.util.VarListener;

public class VarFile extends Var<File>
{
    /**
     * @param name
     * @param defaultValue
     */
    public VarFile(String name, File defaultValue)
    {
        this(name, defaultValue, null);
    }
    
    /**
     * @param name
     * @param defaultValue
     * @param defaultListener
     *            A listener to add to this variable immediately after creation
     */
    public VarFile(String name, File defaultValue, VarListener<File> defaultListener)
    {
        super(name, File.class, defaultValue, defaultListener);
    }
    
    @Override
    public File parse(String s)
    {
        return new File(s);
    }
    
    @Override
    public String toString()
    {
        return getValue() == null ? null : getValue().getAbsolutePath();
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
        node.setNodeValue(toString());
        return true;
    }
    
    @Override
    public VarEditor<File> createVarEditor()
    {
        return new FileChooser(this);
    }
}
