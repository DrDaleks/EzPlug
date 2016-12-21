package plugins.adufour.ezplug;

import icy.system.thread.ThreadUtil;

import java.awt.Container;

import javax.swing.SwingUtilities;

/**
 * Abstract class defining a component of an EzPlug and its behavior in the graphical user
 * interface. <br>
 * An EzComponent can either be an input variable (through which users can select or input values),
 * or a graphical object allowing to produce high-quality graphical interfaces to the EzPlug.<br>
 * Methods in this class provide the ability to show and hide components directly from the interface
 * using triggers from other EzComponents
 * 
 * @author Alexandre Dufour
 */
public abstract class EzComponent
{
    /**
     * The name of this variable (as it will appear in the interface). This field is read-only (it
     * is set once upon component creation)
     */
    public final String name;
    
    protected boolean   visible = true;
    
    private EzDialog    gui;
    
    /**
     * The group to which this component belongs, or null if this component is top-level
     */
    private EzPanel     parent  = null;
    
    protected EzComponent(String name)
    {
        this.name = name;
    }
    
    /**
     * @return true if this component is visible on the user interface
     */
    public boolean isVisible()
    {
        return visible;
    }
    
    /**
     * @deprecated use {@link #isVisible()} instead
     */
    @Deprecated
    public boolean getVisible()
    {
        return isVisible();
    }
    
    /**
     * Sets the component's visibility
     * 
     * @param state
     *            the new visibility state
     */
    public void setVisible(boolean state)
    {
        visible = state;
    }
    
    void addToContainer(final Container container)
    {
        if (!visible) return;
        
        ThreadUtil.invoke(new Runnable()
        {
            @Override
            public void run()
            {
                addTo(container);
            }
        }, !SwingUtilities.isEventDispatchThread());
    }
    
    protected abstract void addTo(Container container);
    
    /**
     * Gets a reference to the graphical interface containing this variable
     * 
     * @return
     */
    final EzDialog getUI()
    {
        return this.gui;
    }
    
    /**
     * Assign the interface which owns this component
     * 
     * @param ezDialog
     *            The parent frame of this component
     */
    final void setUI(EzDialog ezDialog)
    {
        this.gui = ezDialog;
    }
    
    /**
     * @return the group to which this variable belongs (or null if this variable is top-level)
     * @deprecated use {@link #getParentPanel()} instead
     */
    @Deprecated
    protected final EzGroup getGroup()
    {
        return (EzGroup) parent;
    }
    
    /**
     * @return the panel to which this component belongs (or null if this component is top-level)
     */
    protected EzPanel getParentPanel()
    {
        return parent;
    }
    
    /**
     * Sets the group to which this variable belongs
     * 
     * @param group
     *            the parent group of this variable
     * @throws IllegalArgumentException
     *             if this component already belongs to a group
     * @deprecated use {@link #setParentPanel(EzPanel)} instead
     */
    @Deprecated
    protected void setGroup(EzGroup group) throws IllegalArgumentException
    {
        setParentPanel(group);
    }
    
    /**
     * Sets the parent panel of this component
     * 
     * @param parent
     *            the parent of this component
     * @throws IllegalArgumentException
     *             if this component already belongs to another parent
     */
    protected void setParentPanel(EzPanel parent)
    {
        if (this.parent != null && parent != null) throw new IllegalArgumentException("component " + name + " already belongs to group " + parent.name);
        this.parent = parent;
    }
    
    /**
     * Assigns a tool-tip text to the variable, which pops up when the user hovers the mouse on it.
     * 
     * @param text
     *            the text to display (usually no more than 20 words)
     */
    public abstract void setToolTipText(String text);
    
    protected void dispose()
    {
        this.gui = null;
        this.parent = null;
    }
}
