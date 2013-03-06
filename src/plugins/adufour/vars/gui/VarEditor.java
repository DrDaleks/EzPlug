package plugins.adufour.vars.gui;

import icy.system.thread.ThreadUtil;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

import plugins.adufour.vars.gui.swing.CheckBox;
import plugins.adufour.vars.gui.swing.ComboBox;
import plugins.adufour.vars.gui.swing.FileChooser;
import plugins.adufour.vars.gui.swing.FilesChooser;
import plugins.adufour.vars.gui.swing.SequenceChooser;
import plugins.adufour.vars.gui.swing.Spinner;
import plugins.adufour.vars.gui.swing.SwimmingObjectChooser;
import plugins.adufour.vars.gui.swing.TextField;
import plugins.adufour.vars.lang.Var;
import plugins.adufour.vars.util.VarListener;

/**
 * Class handling the graphical component used to view or modify the value of a {@link Var}iable.
 * This class provides an abstraction layer between a {@link Var}iable object (i.e. the model in the
 * MVC sense) and its associated graphical user interface (i.e. the view/controller, represented by
 * a {@link JComponent} object). This abstraction has two main purposes:
 * <ul>
 * <li>MVC-like behavior: {@link Var}iables are detached from their graphical interface counterpart
 * (allowing any scenario from headless manipulation to multiple synchronized editors handling the
 * same {@link Var}iable).</li>
 * <li>Facilitate the integration of the graphical component within a larger user interface, by
 * avoiding the container to handle interface updates and user input events.</li>
 * </ul>
 * A number of default editors are available for the most common {@link Var}iable types (see links
 * below). However, these editors can be extended or replaced by more advanced ones to support
 * custom {@link Var}iable types.
 * 
 * @see CheckBox
 * @see ComboBox
 * @see FileChooser
 * @see FilesChooser
 * @see SequenceChooser
 * @see Spinner
 * @see SwimmingObjectChooser
 * @see TextField
 * 
 * @author Alexandre Dufour
 * 
 * @param <V>
 *            the inner type of the variable controlled by this editor
 * @param <T>
 *            the top-level class from the chosen graphics toolkit
 */
public abstract class VarEditor<V> implements VarListener<V>
{
    /**
     * The {@link Var}iable handled by this editor. Note that multiple editors can handle the same
     * variable simultaneously.
     */
    protected final Var<V> variable;
    
    /**
     * The graphical component associated with the local {@link #variable}. This component is used
     * to display or edit the variable value via a graphical user interface.
     */
    private final Object   editorComponent;
    
    private boolean        nameVisible         = true;
    
    private boolean        componentFocusable  = true;
    
    private boolean        componentResizeable = false;
    
    /**
     * Constructs a new editor for the specified {@link Var}iable. <br>
     * Note that multiple editors be created for the same variable (see the
     * {@linkplain #createEditorComponent()} method for more details).
     * 
     * @param variable
     */
    public VarEditor(final Var<V> variable)
    {
        this.variable = variable;
        this.editorComponent = createEditorComponent();
    }
    
    /**
     * Creates the component to display on the graphical user interface, allowing the user to modify
     * the variable's value via appropriate listeners (listeners should be added or removed via the
     * {@link #activateListeners()} and {@link #deactivateListeners()} methods).<br>
     * In order to allow multiple editors to modify the same variable, this method should always
     * return a newly constructed component (by Swing design, a same component can only be used once
     * per interface).<br>
     * Once created, the component is stored in the final {@link #editorComponent} field to avoid
     * unnecessary re-creations (e.g. during interface refresh). To bypass this optimization and
     * force the creation of a new component for every interface refresh, consider overriding the
     * {@link #getEditorComponent()} method. <br>
     * <br>
     * <u>WARNING</u>: this method is called within the {@link #VarEditor(Var)} constructor,
     * therefore all instance fields besides the final {@link #variable} field will be
     * <code>null</code> until this method returns.
     * 
     * @return a Swing {@link JComponent} that is linked to the variable and can be used to view or
     *         adjust the variable value
     */
    protected abstract JComponent createEditorComponent();
    
    /**
     * @return the graphical component used to view or modify the variable's value. By default, the
     *         returned component is the one created by the {@link #createEditorComponent()} method
     *         during object construction. This method can be overridden to return a new component
     *         for each call (note however that this method may be called many times, e.g. during
     *         interface refresh)
     */
    public Object getEditorComponent()
    {
        return editorComponent;
    }
    
    /**
     * @return The preferred dimension of the local {@link #editorComponent}. By default, this
     *         method calls the component's {@link JComponent#getPreferredSize()} method. However,
     *         this method can be overridden to provide a custom dimension, e.g. to fix the size of
     *         the component regardless of its content
     */
    public abstract Dimension getPreferredSize();
    
    /**
     * Indicates whether and how this component should resize horizontally if the container panel
     * allows resizing. If multiple components in the same panel support resizing, the amount of
     * extra space available will be shared between all components depending on the returned weight
     * (from 0 for no resizing to 1 for maximum resizing).<br/>
     * By default, this value is 1.0 (horizontal resizing is always allowed to fill up the maximum
     * amount of space)
     * 
     * @return a value from 0 (no resize allowed) to 1 (resize as much as possible)
     */
    public double getComponentHorizontalResizeFactor()
    {
        return 1.0;
    }
    
    /**
     * Indicates whether and how this component should resize vertically if the container panel
     * allows resizing. If multiple components in the same panel support resizing, the amount of
     * extra space available will be shared between all components depending on the returned weight
     * (from 0 for no resizing to 1 for maximum resizing).<br/>
     * By default, this value is 0.0 (no vertical resizing)
     * 
     * @return a value from 0 (no resize allowed) to 1 (resize as much as possible)
     */
    public double getComponentVerticalResizeFactor()
    {
        return 0.0;
    }
    
    public abstract void setComponentToolTipText(String s);
    
    /**
     * @return The {@link Var}iable controlled by this editor
     */
    public Var<V> getVariable()
    {
        return variable;
    }
    
    /**
     * @return true if the editor is enabled, i.e. whether the graphical component is active and may
     *         receive user input. This method can be overridden to force the editor status to a
     *         given state
     */
    public boolean isComponentEnabled()
    {
        return getEditorComponent() instanceof JLabel || variable.getReference() == null;
    }
    
    /**
     * @return true if the editor is opaque, or false if the component is transparent. This method
     *         can be overridden to force the opacity to a given state
     */
    public boolean isComponentOpaque()
    {
        return getEditorComponent() instanceof JTextComponent;
    }
    
    /**
     * @return true if the editor is focusable (i.e. the component may capture a mouse event and
     *         become active).
     */
    public boolean isComponentFocusable()
    {
        return componentFocusable;
    }
    
    /**
     * @return <code>true</code> if the container panel should allow this component to be resized,
     *         <code>false</code> otherwise
     */
    public boolean isComponentResizeable()
    {
        return componentResizeable;
    }
    
    /**
     * @return <code>true</code> if the name should appear alongside the editor component, false
     *         otherwise
     */
    public boolean isNameVisible()
    {
        return nameVisible;
    }
    
    /**
     * Activates listeners on the editor component. Listeners should be activated here rather than
     * in the {@link #createEditorComponent()} method, in order to allow the target containers to
     * optimize the scheduling and load of events to fire, and to ensure proper garbage collection
     * when the interface is destroyed.
     */
    protected abstract void activateListeners();
    
    /**
     * Deactivates listeners on the editor component. Listeners should be deactivated here rather
     * than in the {@link #createEditorComponent()} method, in order to allow the target containers
     * to optimize the scheduling and load of events to fire, and to ensure proper garbage
     * collection when the interface is destroyed.
     */
    protected abstract void deactivateListeners();
    
    /**
     * @param componentFocusable
     *            <code>true</code> if the editor is focusable (i.e. the component may capture a
     *            mouse event and become active), <code>false</code> otherwise
     */
    public void setComponentFocusable(boolean componentFocusable)
    {
        this.componentFocusable = componentFocusable;
    }
    
    /**
     * @param componentResizeable
     *            <code>true</code> if the container panel should allow this component to be
     *            resized, <code>false</code> otherwise
     */
    public void setComponentResizeable(boolean componentResizeable)
    {
        this.componentResizeable = componentResizeable;
    }
    
    /**
     * Enables (or disables) the graphical component associated with the {@link Var}iable object.
     * This method should be used by the enclosing graphical interface instead of the traditional
     * {@link JComponent#setEnabled(boolean)}, since listeners are automatically activated (or
     * deactivated) to avoid excessive event firing and ensure proper garbage collection.
     */
    public void setEnabled(boolean enabled)
    {
        // deactivate listeners first
        variable.removeListener(this);
        deactivateListeners();
        
        // enable or disable the component
        if (editorComponent != null) setEditorEnabled(enabled);
        
        if (enabled && editorComponent != null)
        {
            // activate listeners if necessary
            variable.addListener(this);
            activateListeners();
            
            updateInterfaceValue();
        }
    }
    
    protected void setEditorEnabled(boolean enabled)
    {
        Object component = getEditorComponent();
        
        if (component instanceof JComponent)
            ((JComponent) component).setEnabled(enabled);
        
        else throw new UnsupportedOperationException("VarEditor.setEditorEnabled(boolean) is not implemented for the current toolkit");
    }
    
    /**
     * @param nameVisible
     *            <code>true</code> if the name should appear alongside the editor component,
     *            <code>false</code> otherwise
     */
    public void setNameVisible(boolean nameVisible)
    {
        this.nameVisible = nameVisible;
    }
    
    /**
     * Updates the graphical interface component to reflect the new value of the underlying variable
     * (accessible via the {@link Var#getValue()} method).
     */
    protected abstract void updateInterfaceValue();
    
    // VarListener
    
    /**
     * Dispose of resources (e.g. listeners, IO etc.) before releasing the editor. This method must
     * be called by overriding implementations to ensure proper cleaning
     */
    public void dispose()
    {
        setEnabled(false);
    }
    
    public void valueChanged(Var<V> source, V oldValue, final V newValue)
    {
        ThreadUtil.invokeLater(new Runnable()
        {
            public void run()
            {
                updateInterfaceValue();
            }
        });
    }
    
    public void referenceChanged(Var<V> source, Var<? extends V> oldReference, final Var<? extends V> newReference)
    {
        ThreadUtil.invokeLater(new Runnable()
        {
            public void run()
            {
                updateInterfaceValue();
            }
        });
    }
}
