package plugins.adufour.vars.gui.swing;

import icy.system.thread.ThreadUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.List;

import javax.swing.ComboBoxEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import org.jdesktop.swingx.JXComboBox;

import plugins.adufour.vars.gui.model.ValueSelectionModel;
import plugins.adufour.vars.gui.model.VarEditorModel;
import plugins.adufour.vars.lang.Var;

public class ComboBox<T> extends SwingVarEditor<T>
{
    private ActionListener actionListener;
    
    /**
     * Creates a new combo box component for the specified variable. Note that the specified
     * variable must have a constraint of type {@link ValueSelectionModel}
     * 
     * @param variable
     *            the variable to attach to this component
     * @throws IllegalArgumentException
     *             if the variable has no constraint, or if the constraint is not a
     *             {@link ValueSelectionModel}
     */
    public ComboBox(Var<T> variable) throws IllegalArgumentException
    {
        super(variable);
    }
    
    private String arrayToString(Object array)
    {
        String s;
        int length = Array.getLength(array);
        s = length == 0 ? "" : Array.get(array, 0).toString();
        for (int i = 1; i < length; i++)
            s += " " + Array.get(array, i).toString();
        return s;
    }
    
    public JComponent createEditorComponent() throws IllegalArgumentException
    {
        VarEditorModel<T> cons = variable.getDefaultEditorModel();
        
        List<T> defaultValues = null;
        boolean freeInput = true;
        T defaultValue = variable.getDefaultValue();
        
        if (cons != null)
        {
            if (cons instanceof ValueSelectionModel)
            {
                ValueSelectionModel<T> constraint = (ValueSelectionModel<T>) cons;
                defaultValues = constraint.getValidValues();
                freeInput = constraint.isFreeInput();
                defaultValue = constraint.getDefaultValue();
            }
            else throw new IllegalArgumentException("Variable " + variable.getName() + " must have a value-type constraint");
        }
        
        final JXComboBox jComboBox = (defaultValues == null) ? new JXComboBox() : new JXComboBox(defaultValues.toArray());
        jComboBox.setEditable(freeInput);
        jComboBox.setSelectedItem(defaultValue);
        
        actionListener = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (variable.getReference() == null) updateVariableValue();
            }
        };
        
        // Override the default renderer to support array-type items
        jComboBox.setRenderer(createRenderer());
        
        // if the combo box allows user input, override the editor to support array-type items
        if (jComboBox.isEditable()) jComboBox.setEditor(createEditor());
        
        return jComboBox;
    }
    
    protected ListCellRenderer createRenderer()
    {
        return new ListCellRenderer()
        {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                String s = "";
                if (value != null)
                {
                    if (value.getClass().isArray())
                        s = arrayToString(value);
                    else s = value.toString();
                }
                return new JLabel(s);
            }
        };
    }
    
    protected ComboBoxEditor createEditor()
    {
        return new ComboBoxEditor()
        {
            final JTextField jTextField   = new JTextField();
            final Color      defaultColor = jTextField.getForeground();
            final Color      errorColor   = Color.red;
            
            @Override
            public void addActionListener(ActionListener l)
            {
                
            }
            
            @Override
            public Component getEditorComponent()
            {
                jTextField.setFocusable(true);
                Dimension dim = jTextField.getPreferredSize();
                dim.height = 20;
                jTextField.setPreferredSize(dim);
                return jTextField;
            }
            
            @Override
            public T getItem()
            {
                T item = null;
                
                try
                {
                    item = variable.parse(jTextField.getText());
                    jTextField.setForeground(defaultColor);
                    jTextField.setToolTipText(null);
                }
                catch (NumberFormatException nfE)
                {
                    item = null;
                    jTextField.setForeground(errorColor);
                    jTextField.setToolTipText("Cannot parse input into a " + getVariable().getClass().getSimpleName());
                }
                
                return item;
            }
            
            @Override
            public void removeActionListener(ActionListener l)
            {
                
            }
            
            @Override
            public void selectAll()
            {
                jTextField.selectAll();
            }
            
            @Override
            public void setItem(Object item)
            {
                if (item == null)
                {
                    jTextField.setText("");
                }
                else
                {
                    jTextField.setText(item.getClass().isArray() ? arrayToString(item) : item.toString());
                }
            }
        };
    }
    
    /**
     * Replaces the list of values available in the combo box of this variable<br>
     * NOTE: this method will replace the current constraint on the variable
     * 
     * @param values
     * @param defaultValueIndex
     * @param allowUserInput
     */
    public void setDefaultValues(final T[] values, final int defaultValueIndex, final boolean allowUserInput)
    {
        final JComboBox jComboBox = getEditorComponent();
        
        ThreadUtil.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                jComboBox.removeAllItems();
                for (T value : values)
                    jComboBox.addItem(value);
                variable.setDefaultEditorModel(new ValueSelectionModel<T>(values, defaultValueIndex, allowUserInput));
                // jComboBox.setSelectedIndex(defaultValueIndex);
                jComboBox.setEditable(allowUserInput);
            }
        });
    }
    
    @SuppressWarnings("unchecked")
    protected void updateVariableValue()
    {
        variable.setValue((T) getEditorComponent().getSelectedItem());
    }
    
    @Override
    protected void updateInterfaceValue()
    {
        getEditorComponent().getModel().setSelectedItem(variable.getValue());
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        // replace custom instances by new empty ones for garbage collection
        final JComboBox jComboBox = getEditorComponent();
        jComboBox.setRenderer(new DefaultListCellRenderer());
        jComboBox.setModel(new DefaultComboBoxModel());
    }
    
    @Override
    public JComboBox getEditorComponent()
    {
        return (JComboBox) super.getEditorComponent();
    }
    
    @Override
    protected void activateListeners()
    {
        getEditorComponent().addActionListener(actionListener);
    }
    
    @Override
    protected void deactivateListeners()
    {
        getEditorComponent().removeActionListener(actionListener);
    }
}
