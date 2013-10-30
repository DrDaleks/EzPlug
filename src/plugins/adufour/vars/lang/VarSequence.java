package plugins.adufour.vars.lang;

import icy.main.Icy;
import icy.sequence.Sequence;
import icy.util.XMLUtil;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.swing.SequenceChooser;
import plugins.adufour.vars.gui.swing.SequenceViewer;

public class VarSequence extends Var<Sequence>
{
    public static final String NO_SEQUENCE         = "No Sequence";
    
    public static final String ACTIVE_SEQUENCE     = "Active Sequence";
    
    private boolean            noSequenceSelection = false;
    
    public VarSequence(String name, Sequence defaultValue)
    {
        super(name, Sequence.class, defaultValue);
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
        if (getValue() == null) XMLUtil.setAttributeValue((Element) node, Var.XML_KEY_VALUE, NO_SEQUENCE);
        
        return true;
    }
    
    @Override
    public boolean loadFromXML(Node node)
    {
        String string = XMLUtil.getAttributeValue((Element) node, XML_KEY_VALUE, null);
        
        if (NO_SEQUENCE.equalsIgnoreCase(string))
        {
            setNoSequenceSelection();
        }
        else if (ACTIVE_SEQUENCE.equalsIgnoreCase(string))
        {
            setValue(Icy.getMainInterface().getActiveSequence());
        }
        
        return true;
    }
    
    @Override
    public VarEditor<Sequence> createVarEditor()
    {
        return new SequenceChooser(this);
    }
    
    @Override
    public VarEditor<Sequence> createVarViewer()
    {
        return new SequenceViewer(this);
    }
    
    @Override
    public String getValueAsString()
    {
        Sequence s = getValue();
        
        if (s == null) return NO_SEQUENCE;
        
        if (s.getFilename() != null) return s.getFilename();
        
        return s.getName();
    }
    
    public boolean isNoSequenceSelected()
    {
        return noSequenceSelection;
    }
    
    public void setNoSequenceSelection()
    {
        noSequenceSelection = true;
        Sequence oldValue = getValue();
        setValue(null);
        if (oldValue == null) fireVariableChanged(oldValue, null);
    }
    
    @Override
    public void setValue(Sequence newValue) throws IllegalAccessError
    {
        if (newValue != null) noSequenceSelection = false;
        super.setValue(newValue);
    }
}
