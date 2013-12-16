package plugins.adufour.vars.gui.swing;

import icy.sequence.Sequence;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListDataListener;

import plugins.adufour.vars.lang.Var;

public class ChannelSelector extends SwingVarEditor<Integer>
{
    private JComboListener jComboListener;
    
    private Var<Sequence>  sequence;
    
    private boolean        allowAllChannels;
    
    public ChannelSelector(Var<Integer> variable, Var<Sequence> sequence, boolean allowAllChannels)
    {
        super(variable);
        this.sequence = sequence;
        this.allowAllChannels = allowAllChannels;
    }
    
    private final class JComboListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            int index = getEditorComponent().getSelectedIndex();
            if (allowAllChannels) index--;
            variable.setValue(index);
        }
    }
    
    private final class ChannelSelectorModel extends DefaultComboBoxModel
    {
        private static final long serialVersionUID = 1L;
        
        public ChannelSelectorModel()
        {
            
        }
        
        @Override
        public int getSize()
        {
            if (sequence == null || sequence.getValue() == null) return 0;
            
            int size = sequence.getValue().getSizeC();
            
            return allowAllChannels ? size + 1 : size;
        }
        
        @Override
        public Integer getElementAt(int index)
        {
            if (allowAllChannels) index--;
            
            return index;
        }
        
        @Override
        public void addListDataListener(ListDataListener l)
        {
            // don't register anything
        }
    }
    
    /**
     * Creates a graphical component able to receive user input to change the variable value.<br>
     * Note that listeners are <b>NOT</b> registered here. This operation should be done when (and
     * if) the component is to be used (to avoid memory leaks due to improper cleaning)
     */
    @Override
    public JComponent createEditorComponent()
    {
        jComboListener = new JComboListener();
        
        final JComboBox jComboChannels = new JComboBox(new ChannelSelectorModel());
        
        jComboChannels.setRenderer(new ListCellRenderer()
        {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                if (value == null) return new JLabel("No sequence");
                
                if (value == (Integer) 0) return new JLabel("All");
                
                int channel = (Integer) value - 1;
                
                return new JLabel(sequence.getValue().getChannelName(channel));
            }
        });
        
        return jComboChannels;
    }
    
    @Override
    public Dimension getPreferredSize()
    {
        Dimension dim = super.getPreferredSize();
        dim.height = 20;
        return dim;
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        final JComboBox jComboSequences = getEditorComponent();
        
        // replace custom instances by new empty ones for garbage collection
        jComboSequences.setRenderer(new DefaultListCellRenderer());
        jComboSequences.setModel(new DefaultComboBoxModel());
        
    }
    
    @Override
    protected void updateInterfaceValue()
    {
        if (sequence == null || sequence.getValue() == null)
        {
            getEditorComponent().setSelectedIndex(-1);
        }
        else
        {
            int index = variable.getValue();
            if (allowAllChannels) index++;
            getEditorComponent().setSelectedIndex(index);
        }
        getEditorComponent().repaint();
    }
    
    @Override
    public JComboBox getEditorComponent()
    {
        return (JComboBox) super.getEditorComponent();
    }
    
    @Override
    protected void activateListeners()
    {
        getEditorComponent().addActionListener(jComboListener);
        
    }
    
    @Override
    protected void deactivateListeners()
    {
        getEditorComponent().removeActionListener(jComboListener);
    }
    
}
