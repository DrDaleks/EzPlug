package plugins.adufour.vars.lang;

import icy.sequence.Sequence;
import icy.util.XMLUtil;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.swing.SequenceChooser;
import plugins.adufour.vars.gui.swing.SequenceViewer;

public class VarSequence extends Var<Sequence>
{
    static final String NO_SEQUENCE = "No Sequence";
    
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
        
        if (NO_SEQUENCE.equalsIgnoreCase(string)) setValue(null);
        
        return true;
    }
    
    @Override
    public VarEditor<Sequence> createVarEditor(boolean preferReadOnly)
    {
        if (preferReadOnly) return new SequenceViewer(this);
        
        return new SequenceChooser(this);
    }
    
    @Override
    public String getValueAsString()
    {
        Sequence s = getValue();
        
        if (s == null) return NO_SEQUENCE;
        
        if (s.getFilename() != null) return s.getFilename();
        
        return s.getName();
    }
}
