package plugins.adufour.ezplug;

import icy.system.thread.ThreadUtil;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import plugins.adufour.vars.gui.VarEditor;
import plugins.adufour.vars.gui.model.ValueSelectionModel;
import plugins.adufour.vars.gui.model.VarEditorModel;
import plugins.adufour.vars.gui.swing.ComboBox;
import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.util.VarListener;

/**
 * Class defining a variable for use within an EzPlug.<br>
 * <br>
 * EzVar objects are powerful components that bind each parameter type to a specific graphical
 * component that can be used to receive input from the user interface. It is designed to be used by
 * plug-in developers in a simple and fast manner with zero knowledge in graphical interface
 * programming.<br>
 * A wide-range of EzVar subclasses are available, depending on the type of data to handle
 * (numerical value, boolean flags, image sequences, file arrays, etc.). Advanced developers may
 * also contribute to the EzVar class hierarchy by implementing additional variable types to fit
 * their needs and that of others.<br>
 * <br>
 * EzVar objects are always instantiated using the subclass corresponding to the parameter type to
 * use. Once created, the variable can be added to the graphical interface of the EzPlug via the
 * {@link plugins.adufour.ezplug.EzPlug#addEzComponent(EzComponent)} method (see sample code in the
 * {@link plugins.adufour.ezplug.EzPlug} class documentation).<br>
 * 
 * @author Alexandre Dufour
 */
public abstract class EzVar<T> extends EzComponent implements VarListener<T>
{
    final Var<T>                              variable;
    
    private JLabel                            jLabelName;
    
    private VarEditor<T>                      varEditor;
    
    private final HashMap<EzComponent, T[]>   visibilityTriggers = new HashMap<EzComponent, T[]>();
    
    private final ArrayList<EzVarListener<T>> listeners          = new ArrayList<EzVarListener<T>>();
    
    /**
     * Constructs a new variable
     * 
     * @param variable
     *            The variable to attach to this object
     * @param constraint
     *            the constraint to apply on the variable when receiving input, or null if a default
     *            constraint should be applied
     */
    protected EzVar(final Var<T> variable, VarEditorModel<T> constraint)
    {
        super(variable.getName());
        this.variable = variable;
        variable.setDefaultEditorModel(constraint);
        
        ThreadUtil.invokeNow(new Runnable()
        {
            public void run()
            {
                jLabelName = new JLabel(variable.getName());
                varEditor = variable.createVarEditor();
            }
        });
    }
    
    /**
     * Creates a new variable with a JComboBox as default graphical component
     * 
     * @param variable
     *            the variable to attach to this EzVar
     * @param defaultValues
     *            the list of values to store in the combo box
     * @param defaultValueIndex
     *            the index of the default selected item
     * @param freeInput
     *            true to allow user manual input, false to restrict the selection to the given list
     */
    protected EzVar(Var<T> variable, T[] defaultValues, int defaultValueIndex, boolean freeInput)
    {
        this(variable, new ValueSelectionModel<T>(defaultValues, defaultValueIndex, freeInput));
    }
    
    /**
     * Adds a new listener that will be notified is this variable changes
     * 
     * @param listener
     *            the listener to add
     */
    public void addVarChangeListener(EzVarListener<T> listener)
    {
        listeners.add(listener);
    }
    
    /**
     * Sets a visibility trigger on the target EzComponent. The visibility state of the target
     * component is set to true whenever this variable is visible, and takes any of the trigger
     * values, and false otherwise. If no trigger value is indicated, the target component will
     * always be visible as long as the source component is both visible and enabled
     * 
     * @param targetComponent
     *            the component to hide or show
     * @param values
     *            the list of values which will set the visibility of the target component to true.
     *            If no value is indicated, the target component will be visible as long as the
     *            source component is both visible and enabled
     */
    public void addVisibilityTriggerTo(EzComponent targetComponent, T... values)
    {
        visibilityTriggers.put(targetComponent, values);
        
        updateVisibilityChain();
    }
    
    @Override
    protected void addTo(Container container)
    {
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(2, 10, 2, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        if (variable.isOptional())
        {
            gbc.insets.left = 5;
            JPanel optionPanel = new JPanel(new BorderLayout(0, 0));
            
            final JCheckBox option = new JCheckBox("");
            option.setSelected(isEnabled());
            option.setFocusable(false);
            option.setToolTipText("Click here to disable this parameter");
            option.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent arg0)
                {
                    setEnabled(option.isSelected());
                    if (option.isSelected())
                    {
                        option.setToolTipText("Click here to disable this parameter");
                    }
                    else
                    {
                        option.setToolTipText("Click here to enable this parameter");
                    }
                }
            });
            optionPanel.add(option, BorderLayout.WEST);
            optionPanel.add(jLabelName, BorderLayout.CENTER);
            container.add(optionPanel, gbc);
            gbc.insets.left = 10;
        }
        else container.add(jLabelName, gbc);
        
        gbc.weightx = 1;
        
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        VarEditor<T> ed = getVarEditor();
        ed.setEnabled(true); // activates listeners
        JComponent component = (JComponent) ed.getEditorComponent();
        component.setPreferredSize(ed.getPreferredSize());
        container.add(component, gbc);
    }
    
    protected void dispose()
    {
        visibilityTriggers.clear();
        
        varEditor.dispose();
        
        // unregister the internal listener
        variable.removeListener(this);
        
        super.dispose();
    }
    
    /**
     * Privileged access to fire listeners from inside the EzPlug package. This method is called
     * after the GUI was created in order to trigger listeners declared in the
     * {@link EzPlug#initialize()} method
     */
    final void fireVariableChangedInternal()
    {
        fireVariableChanged(variable.getValue());
    }
    
    protected final void fireVariableChanged(T value)
    {
        for (EzVarListener<T> l : listeners)
            l.variableChanged(this, value);
        
        if (getUI() != null && varEditor != null)
        {
            updateVisibilityChain();
            SwingUtilities.invokeLater(getUI().fullPackingTask);
        }
    }
    
    /**
     * Retrieves the default values into the destination array, or returns a new one if dest is not
     * big enough.
     * 
     * @param dest
     *            the array to fill with the values. the array is left untouched if it is not big
     *            enough
     * @throws UnsupportedOperationException
     *             if the user input component is not a combo box
     */
    @SuppressWarnings("unchecked")
    public T[] getDefaultValues(T[] dest)
    {
        if (getVarEditor() instanceof ComboBox)
        {
            JComboBox combo = ((ComboBox<T>) getVarEditor()).getEditorComponent();
            
            ArrayList<T> items = new ArrayList<T>(combo.getItemCount());
            for (int i = 0; i < combo.getItemCount(); i++)
                items.add((T) combo.getItemAt(i));
            return items.toArray(dest);
        }
        
        throw new UnsupportedOperationException("The input component is not a list of values");
    }
    
    protected VarEditor<T> getVarEditor()
    {
        return varEditor;
    }
    
    /**
     * Returns an EzPlug-wide unique identifier for this variable (used to save/load parameters)
     * 
     * @return a String identifier that is unique within the owner plug
     */
    String getID()
    {
        String id = variable.getName();
        
        EzGroup group = getGroup();
        
        while (group != null)
        {
            id = group.name + "." + id;
            group = group.getGroup();
        }
        
        return id;
    }
    
    /**
     * Returns the variable value. By default, null is considered a valid value. In order to show an
     * error message (or throw an exception in head-less mode), use the {@link #getValue(boolean)}
     * method instead
     * 
     * @return The variable value
     */
    public T getValue()
    {
        return getValue(false);
    }
    
    /**
     * Returns the variable value (or fails if the variable is null).
     * 
     * @param forbidNull
     *            set to true to display an error message (or to throw an exception in head-less
     *            mode)
     * @return the variable value
     * @throws EzException
     *             if the variable value is null and forbidNull is true
     */
    public T getValue(boolean forbidNull)
    {
        return variable.getValue(forbidNull);
    }
    
    /**
     * @return The underlying {@link Var} object that contains the value of this EzVar
     */
    public Var<T> getVariable()
    {
        return variable;
    }
    
    /**
     * Indicates whether this variable is enabled, and therefore may be used by plug-ins. Note that
     * this method returns a simple flag, and it is up to the accessing plug-ins to behave
     * accordingly (i.e. it is not guaranteed that a disabled variable will not be used by plug-ins)
     * 
     * @return <code>true</code> if this variable is enabled, <code>false</code> otherwise
     */
    public boolean isEnabled()
    {
        return variable.isEnabled();
    }
    
    /**
     * Removes the given listener from the list
     * 
     * @param listener
     *            the listener to remove
     */
    public void removeVarChangeListener(EzVarListener<T> listener)
    {
        listeners.remove(listener);
    }
    
    /**
     * Removes all change listeners for this variable
     */
    public void removeAllVarChangeListeners()
    {
        variable.removeListeners();
    }
    
    /**
     * Replaces the list of values available in the combo box of this variable<br>
     * NOTE: this method has no effect if the user component is not already a combo box
     * 
     * @param values
     * @param defaultValueIndex
     * @param allowUserInput
     */
    public void setDefaultValues(T[] values, int defaultValueIndex, boolean allowUserInput)
    {
        if (getVarEditor() instanceof ComboBox)
        {
            ((ComboBox<T>) getVarEditor()).setDefaultValues(values, defaultValueIndex, allowUserInput);
        }
    }
    
    /**
     * Sets whether the variable is enabled, i.e. that it is usable by plug-ins, and accepts
     * modifications via the graphical interface
     * 
     * @param enabled
     *            the enabled state
     */
    public void setEnabled(boolean enabled)
    {
        variable.setEnabled(enabled);
        jLabelName.setEnabled(enabled);
        getVarEditor().setEnabled(enabled);
        updateVisibilityChain();
        SwingUtilities.invokeLater(getUI().fullPackingTask);
    }
    
    /**
     * Sets a flag indicating whether this variable should be considered "optional". This flag is
     * typically used to mark a plug-in parameter as optional, allowing plug-ins to react
     * accordingly and save potentially unnecessary computations. Setting a variable as optional
     * will add a check-box next to the variable editor to provide visual feedback
     * 
     * @param optional
     *            <code>true</code> if this parameter is optional, <code>false</code> otherwise
     */
    public void setOptional(boolean optional)
    {
        variable.setOptional(optional);
    }
    
    /**
     * Sets the new value of this variable
     * 
     * @param value
     *            the new value
     * @throws UnsupportedOperationException
     *             thrown if changing the variable value from code is not supported (or not yet
     *             implemented)
     */
    public void setValue(final T value) throws UnsupportedOperationException
    {
        variable.setValue(value);
    }
    
    @Override
    public void setToolTipText(String text)
    {
        jLabelName.setToolTipText(text);
        getVarEditor().setComponentToolTipText(text);
    }
    
    /**
     * Sets the visibility state of this variable, and updates the chain of visibility states
     * (components hiding other components)
     * 
     * @param newVisibleState
     *            the new visibility state
     */
    public void setVisible(boolean newVisibleState)
    {
        super.setVisible(newVisibleState);
        
        updateVisibilityChain();
    }
    
    public String toString()
    {
        return variable.getName() + " = " + variable.toString();
    }
    
    protected void updateVisibilityChain()
    {
        Set<EzComponent> componentsToUpdate = visibilityTriggers.keySet();
        
        // first, hide everything in the chain
        for (EzComponent component : componentsToUpdate)
            component.setVisible(false);
        
        // if "this" is not visible, do anything else
        if (!this.isVisible()) return;
        
        // otherwise, one by one, show the components w.r.t. the triggers
        for (EzComponent component : componentsToUpdate)
        {
            T[] componentTriggerValues = visibilityTriggers.get(component);
            
            if (componentTriggerValues.length == 0 && isEnabled())
            {
                component.setVisible(true);
            }
            else for (T triggerValue : componentTriggerValues)
            {
                if (triggerValue == getValue())
                {
                    // this call will be recursive in case of a EzVar object
                    component.setVisible(true);
                    break;
                }
            }
        }
    }
    
    @Override
    public void valueChanged(Var<T> source, T oldValue, T newValue)
    {
        fireVariableChanged(newValue);
    }
    
    @Override
    public void referenceChanged(Var<T> source, Var<? extends T> oldReference, Var<? extends T> newReference)
    {
        
    }
}
