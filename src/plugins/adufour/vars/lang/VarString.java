package plugins.adufour.vars.lang;

import org.w3c.dom.Node;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.VarEditorFactory;
import plugins.adufour.vars.gui.model.PasswordModel;
import plugins.adufour.vars.util.VarListener;

public class VarString extends Var<String>
{
    private final int nbLines;
    
    /**
     * Creates a new text variable with a default text field editor
     * 
     * @param name
     * @param defaultValue
     */
    public VarString(String name, String defaultValue)
    {
        this(name, defaultValue, 1, null);
    }
    
    /**
     * Creates a new text variable with a default text field editor
     * 
     * @param name
     * @param defaultValue
     * @param defaultListener
     *            A listener to add to this variable immediately after creation
     */
    public VarString(String name, String defaultValue, VarListener<String> defaultListener)
    {
        this(name, defaultValue, 1, null);
    }
    
    /**
     * Creates a new text variable with a default text field editor
     * 
     * @param name
     *            the name of this variable (*not* the actual text to store and/or display)
     * @param defaultValue
     *            the initial text to store and/or display
     * @param nbLines
     *            The number of lines (used only to create the graphical interface)
     */
    public VarString(String name, String defaultValue, int nbLines)
    {
        this(name, defaultValue, nbLines, null);
    }
    
    /**
     * Creates a new text variable with a default text field editor
     * 
     * @param name
     *            the name of this variable (*not* the actual text to store and/or display)
     * @param defaultValue
     *            the initial text to store and/or display
     * @param nbLines
     *            The number of lines (used only to create the graphical interface)
     * @param defaultListener
     *            A listener to add to this variable immediately after creation
     */
    public VarString(String name, String defaultValue, int nbLines, VarListener<String> defaultListener)
    {
        super(name, String.class, defaultValue, defaultListener);
        this.nbLines = nbLines;
    }
    
    @Override
    public String parse(String s)
    {
        return s;
    }
    
    @Override
    public VarEditor<String> createVarEditor()
    {
        VarEditorFactory factory = VarEditorFactory.getDefaultFactory();
        
        if (getDefaultEditorModel() == null) return nbLines > 1 ? factory.createTextArea(this, nbLines) : factory.createTextField(this);
        
        if (getDefaultEditorModel() instanceof PasswordModel) return factory.createPasswordField(this);
        
        return super.createVarEditor();
    }
    
    @Override
    public String getValueAsString()
    {
        // don't show passwords
        if (getDefaultEditorModel() instanceof PasswordModel) return "";
        
        return super.getValueAsString();
    }
    
    @Override
    public boolean saveToXML(Node node) throws UnsupportedOperationException
    {
        // don't save passwords
        if (getDefaultEditorModel() instanceof PasswordModel) return true;
        
        return super.saveToXML(node);
    }
}
