package plugins.adufour.vars.gui.swing;

import icy.file.FileUtil;
import icy.gui.main.ActiveSequenceListener;
import icy.gui.main.GlobalSequenceListener;
import icy.main.Icy;
import icy.sequence.Sequence;
import icy.sequence.SequenceEvent;
import icy.util.StringUtil;

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
import plugins.adufour.vars.lang.VarSequence;

public class SequenceChooser extends SwingVarEditor<Sequence>
{
    private abstract class SequenceListener implements GlobalSequenceListener, ActiveSequenceListener
    {};
    
    private SequenceListener          listener;
    
    private JComboSequenceBoxListener jComboSequenceBoxListener;
    
    public SequenceChooser(Var<Sequence> variable)
    {
        super(variable);
    }
    
    private final class JComboSequenceBoxListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            final JComboBox jComboSequences = getEditorComponent();
            
            Object o = jComboSequences.getSelectedItem();
            
            if (o == variable.getValue()) return;
            
            if (o == null)
            {
                variable.setValue(null);
            }
            else if (o == VarSequence.NO_SEQUENCE)
            {
                ((VarSequence) variable).setNoSequenceSelection();
            }
            else if (o.toString().equals(VarSequence.ACTIVE_SEQUENCE))
            {
                variable.setValue(Icy.getMainInterface().getActiveSequence());
            }
            else
            {
                Sequence newValue = (Sequence) o;
                
                jComboSequences.setToolTipText(newValue.getName());
                
                variable.setValue(newValue);
            }
        }
    }
    
    private final class SequenceChooserModel extends DefaultComboBoxModel
    {
        private static final long serialVersionUID = 1L;
        
        public SequenceChooserModel()
        {
            setSelectedItem(VarSequence.ACTIVE_SEQUENCE);
        }
        
        @Override
        public int getSize()
        {
            // index 0: no sequence
            // index 1: active sequence
            return 2 + Icy.getMainInterface().getSequences().size();
        }
        
        @Override
        public Object getElementAt(int index)
        {
            if (index <= 0) return VarSequence.NO_SEQUENCE;
            
            if (index == 1) return VarSequence.ACTIVE_SEQUENCE;
            
            return Icy.getMainInterface().getSequences().get(index - 2);
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
        
        jComboSequenceBoxListener = new JComboSequenceBoxListener();
        
        final JComboBox jComboSequences = new JComboBox(new SequenceChooserModel());
        
        jComboSequences.setRenderer(new ListCellRenderer()
        {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                if (value == VarSequence.ACTIVE_SEQUENCE) return new JLabel(VarSequence.ACTIVE_SEQUENCE);
                
                if (value == null || value == VarSequence.NO_SEQUENCE) return new JLabel(VarSequence.NO_SEQUENCE);
                
                if (value instanceof Sequence)
                {
                    String s = FileUtil.getFileName(((Sequence) value).getName());
                    JLabel label = new JLabel(StringUtil.limit(s, 24));
                    return label;
                }
                
                throw new IllegalArgumentException(value.toString());
            }
        });
        
        listener = new SequenceListener()
        {
            @Override
            public void sequenceClosed(Sequence sequence)
            {
                if (variable.getReference() != null) return;
                
                if (variable.getValue() == sequence)
                {
                    jComboSequences.setSelectedIndex(1);
                }
                
                if (jComboSequences.getModel().getSize() == 2)
                {
                    variable.setValue(null);
                }
                
                jComboSequences.repaint();
                jComboSequences.updateUI();
            }
            
            @Override
            public void sequenceOpened(Sequence sequence)
            {
                jComboSequences.repaint();
                jComboSequences.updateUI();
            }
            
            @Override
            public void sequenceActivated(Sequence sequence)
            {
                if (variable.getReference() != null) return;
                
                // active sequence selection ?
                if (jComboSequences.getSelectedIndex() == 1 && variable.getReference() == null) variable.setValue(sequence);
            }
            
            @Override
            public void sequenceDeactivated(Sequence sequence)
            {
            }
            
            @Override
            public void activeSequenceChanged(SequenceEvent event)
            {
            }
        };
        
        if (variable.getReference() == null) variable.setValue(Icy.getMainInterface().getActiveSequence());
        
        return jComboSequences;
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
        if (variable.getReference() != null)
        {
            getEditorComponent().setSelectedItem(variable.getValue());
            getEditorComponent().repaint();
        }
        else if (variable.getValue() == null && ((VarSequence) variable).isNoSequenceSelected())
        {
            getEditorComponent().setSelectedIndex(0);
            getEditorComponent().repaint();
        }
        
        getEditorComponent().setToolTipText("<html><pre><font size=3>" + variable.getValueAsString(true) + "</font></pre></html>");
    }
    
    @Override
    public JComboBox getEditorComponent()
    {
        return (JComboBox) super.getEditorComponent();
    }
    
    @Override
    protected void activateListeners()
    {
        Icy.getMainInterface().addGlobalSequenceListener(listener);
        Icy.getMainInterface().addActiveSequenceListener(listener);
        getEditorComponent().addActionListener(jComboSequenceBoxListener);
    }
    
    @Override
    protected void deactivateListeners()
    {
        Icy.getMainInterface().removeGlobalSequenceListener(listener);
        Icy.getMainInterface().removeActiveSequenceListener(listener);
        getEditorComponent().removeActionListener(jComboSequenceBoxListener);
    }
}
