package plugins.adufour.ezplug;

import icy.system.thread.ThreadUtil;

import java.awt.Container;

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
	public final String	name;
	
	protected boolean	visible		= true;
	
	private EzDialog		gui;
	
	/**
	 * The group to which this component belongs, or null if this component is top-level
	 */
	private EzGroup		parentGroup	= null;
	
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
		
		ThreadUtil.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				addTo(container);
			}
		});
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
	 * Gets the group to which this variable belongs (or null if this variable is top-level)
	 * 
	 * @return the group to which this variable belongs (or null if this variable is top-level)
	 */
	protected final EzGroup getGroup()
	{
		return parentGroup;
	}
	
	/**
	 * Sets the group to which this variable belongs
	 * 
	 * @param group
	 *            the parent group of this variable
	 * @throws IllegalArgumentException
	 *             if this component already belongs to a group
	 */
	protected void setGroup(EzGroup group) throws IllegalArgumentException
	{
		if (parentGroup != null && group != null) throw new IllegalArgumentException("component " + name + " already belongs to group " + parentGroup.name);
		this.parentGroup = group;
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
		this.parentGroup = null;
	}
}
