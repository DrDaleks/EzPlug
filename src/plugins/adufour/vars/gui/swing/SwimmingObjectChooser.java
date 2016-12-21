package plugins.adufour.vars.gui.swing;

import icy.main.Icy;
import icy.swimmingPool.SwimmingObject;
import icy.swimmingPool.SwimmingPoolEvent;
import icy.swimmingPool.SwimmingPoolListener;
import icy.system.thread.ThreadUtil;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListDataListener;

import plugins.adufour.vars.lang.Var;

public class SwimmingObjectChooser extends SwingVarEditor<SwimmingObject> implements SwimmingPoolListener
{
    private JComboBoxListener jComboBoxListener;
    
    private JComboBoxModel jComboBoxModel;
    
    private JComboBoxRenderer jComboBoxRenderer;
    
    private ArrayList<SwimmingObject> validObjects;
    
    /**
     * The combo box model is slightly different from that of SequenceSelector with the following
     * modifications: <br>
     * - the list of pointers to this variable is added to the combo box<br>
     * - no local reference to the currently selected sequence
     */
    private final class JComboBoxModel extends DefaultComboBoxModel<SwimmingObject>
    {
        private static final long serialVersionUID = 1L;
        
        @Override
        public void addListDataListener(ListDataListener l)
        {
        }
        
        @Override
        public SwimmingObject getElementAt(int index)
        {
            // first is the "no" selection
            if (index == 0) return null;
            
            return validObjects.get(index - 1);
        }
        
        @Override
        public int getSize()
        {
            // slot 0 is dedicated to "no selection", everything else is shifted up by one increment
            return 1 + validObjects.size();
        }
        
    }
    
    private final class JComboBoxRenderer implements ListCellRenderer<SwimmingObject>
    {
        public Component getListCellRendererComponent(JList<? extends SwimmingObject> list, SwimmingObject value, int index, boolean isSelected, boolean cellHasFocus)
        {
            if (value == null) return new JLabel("No selection");
            
            String name = value.getName();
            return new JLabel(name);
        }
    }
    
    private final class JComboBoxListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            SwimmingObject newValue = (SwimmingObject) getEditorComponent().getSelectedItem();
            
            if (newValue != null) getEditorComponent().setToolTipText(newValue.getName());
            
            variable.setValue(newValue);
        }
    }
    
    public SwimmingObjectChooser(Var<SwimmingObject> variable)
    {
        super(variable);
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        validObjects.clear();
        
        JComboBox<SwimmingObject> jComboBox = getEditorComponent();
        // replace custom instances by new empty ones for garbage collection
        jComboBox.setRenderer(new DefaultListCellRenderer());
        jComboBox.setModel(new DefaultComboBoxModel<SwimmingObject>());
    }
    
    @Override
    public JComponent createEditorComponent()
    {
        validObjects = new ArrayList<SwimmingObject>();
        jComboBoxListener = new JComboBoxListener();
        jComboBoxModel = new JComboBoxModel();
        jComboBoxRenderer = new JComboBoxRenderer();
        
        JComboBox<SwimmingObject> jComboBox = new JComboBox<SwimmingObject>(jComboBoxModel);
        
        for (SwimmingObject swObj : Icy.getMainInterface().getSwimmingPool().getObjects())
            if (variable.getDefaultEditorModel().isValid(swObj))
            {
                validObjects.add(swObj);
            }
        
        if (validObjects.size() > 0) jComboBox.setSelectedIndex(1);
        
        jComboBox.setRenderer(jComboBoxRenderer);
        
        return jComboBox;
    }
    
    @Override
    protected void updateInterfaceValue()
    {
        if (variable.getReference() != null)
        {
            getEditorComponent().setSelectedItem(variable.getValue());
            getEditorComponent().repaint();
        }
        else if (variable.getValue() == null)
        {
            getEditorComponent().setSelectedIndex(0);
            getEditorComponent().repaint();
        }
        
        String valueAsString = variable.getValueAsString(true);
        
        getEditorComponent().setToolTipText(valueAsString.length() == 0 ? null : "<html><pre><font size=3>" + valueAsString + "</font></pre></html>");
    }
    
    @Override
    public void swimmingPoolChangeEvent(SwimmingPoolEvent event)
    {
        SwimmingObject object = event.getResult();
        
        if (!variable.getDefaultEditorModel().isValid(object)) return;
        
        switch (event.getType())
        {
            case ELEMENT_ADDED:
                validObjects.add(object);
            break;
            
            case ELEMENT_REMOVED:
                validObjects.remove(object);
            break;
        }
        
        ThreadUtil.invokeLater(new Runnable()
        {
            public void run()
            {
                getEditorComponent().repaint();
                getEditorComponent().updateUI();
            }
        });
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public JComboBox<SwimmingObject> getEditorComponent()
    {
        return (JComboBox<SwimmingObject>) super.getEditorComponent();
    }
    
    @Override
    protected void activateListeners()
    {
        getEditorComponent().addActionListener(jComboBoxListener);
        Icy.getMainInterface().getSwimmingPool().addListener(SwimmingObjectChooser.this);
    }
    
    @Override
    protected void deactivateListeners()
    {
        getEditorComponent().removeActionListener(jComboBoxListener);
        Icy.getMainInterface().getSwimmingPool().removeListener(SwimmingObjectChooser.this);
    }
    
}
