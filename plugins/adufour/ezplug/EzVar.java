package plugins.adufour.ezplug;

import icy.system.thread.ThreadUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.ComboBoxEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

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
public abstract class EzVar<T> extends EzComponent
{
	private static final long	serialVersionUID	= 1L;
	
	/**
	 * Interface allowing variables to be saved and loaded from disk
	 * 
	 * @author Alexandre Dufour
	 * 
	 * @param <T>
	 *            The type of object to store on disk. Developers are recommended to use simple
	 *            object types to avoid huge XML parameters files
	 */
	public interface Storable<T>
	{
		/**
		 * Gets the value that should be used to store into the XML parameter file
		 * 
		 */
		T getXMLValue();
		
		/**
		 * Reads the value from the XML file and sets the corresponding interface value
		 * 
		 * @param value
		 */
		void setXMLValue(T value);
	}
	
	private final List<EzVarListener<T>>	varChangeListeners	= new ArrayList<EzVarListener<T>>();
	
	private JLabel							jLabelName;
	
	private JComponent						userInputComponent;
	
	private HashMap<EzComponent, T[]>		visibilityTriggers	= new HashMap<EzComponent, T[]>();
	
	/**
	 * Constructs a new variable
	 * 
	 * @param varName
	 *            The variable name
	 */
	EzVar(String varName)
	{
		super(varName);
		
		ThreadUtil.invoke(new Runnable()
		{
			@Override
			public void run()
			{
				jLabelName = new JLabel(name);
			}
		}, !SwingUtilities.isEventDispatchThread());
	}
	
	/**
	 * Creates a new variable with a JComboBox as default graphical component
	 * 
	 * @param varName
	 *            the variable name
	 * @param defaultValues
	 *            the list of values to store in the combo box
	 * @param defaultValueIndex
	 *            the index of the default selected item
	 * @param allowUserInput
	 *            true to allow user manual input, false to restrict the selection to the given list
	 */
	EzVar(String varName, final T[] defaultValues, final int defaultValueIndex, final boolean allowUserInput)
	{
		this(varName);
		
		ThreadUtil.invoke(new Runnable()
		{
			@Override
			public void run()
			{
				final JComboBox jComboBox = new JComboBox(defaultValues);
				jComboBox.setEditable(allowUserInput);
				jComboBox.setSelectedIndex(defaultValueIndex);
				jComboBox.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						fireVariableChanged(getValue());
					}
				});
				
				// Override the default renderer to support array-type items
				jComboBox.setRenderer(new ListCellRenderer()
				{
					@Override
					public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
					{
						return new JLabel(value == null ? "" : value.getClass().isArray() ? arrayToString(value) : value.toString());
					}
				});
				
				// if the combo box is editable by the user, override the editor to support
				// array-type items
				if (allowUserInput)
				{
					if (!(EzVar.this instanceof EzTextParser<?>))
						throw new UnsupportedOperationException("This variable type is unable to parse text input");
					
					jComboBox.setEditor(new ComboBoxEditor()
					{
						JTextField	jTextField		= new JTextField();
						final Color	defaultColor	= jTextField.getForeground();
						final Color	errorColor		= Color.red;
						
						@Override
						public void addActionListener(ActionListener l)
						{
							
						}
						
						@Override
						public Component getEditorComponent()
						{
							return jTextField;
						}
						
						@SuppressWarnings("unchecked")
						@Override
						public T getItem()
						{
							T item = null;
							
							try
							{
								item = ((EzTextParser<T>) EzVar.this).parseInput(jTextField.getText());
								jTextField.setForeground(defaultColor);
								jTextField.setToolTipText(null);
							}
							catch (NumberFormatException nfE)
							{
								item = null;
								// System.err.println("Error parsing user input :" +
								// jTextField.getText());
								jTextField.setForeground(errorColor);
								jTextField.setToolTipText("Cannot parse input into a " + EzVar.this.getClass().getSimpleName().substring(3) + " variable");
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
								return;
							jTextField.setText(item.getClass().isArray() ? arrayToString(item) : item.toString());
						}
					});
				}
				
				setComponent(jComboBox);
			}
		}, !SwingUtilities.isEventDispatchThread());
	}
	
	/**
	 * Adds a new listener that will be notified is this variable changes
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addVarChangeListener(EzVarListener<T> listener)
	{
		varChangeListeners.add(listener);
		
		// The listener is fired right away to allow the user to use the same code for
		// initialization and event listening code
		try
		{
			fireVariableChanged(getValue());
		}
		catch (EzException eze)
		{
			// thrown in case the value is null for some variables
			fireVariableChanged(null);
		}
	}
	
	/**
	 * Sets a visibility trigger on the target EzComponent. The visibility state of the target
	 * component is set to true whenever this variable is visible and takes any of the trigger
	 * values, and false otherwise.
	 * 
	 * @param targetComponent
	 *            the component to hide or show
	 * @param values
	 *            the list of values which will set the visibility of the target component to true
	 */
	public void addVisibilityTriggerTo(EzComponent targetComponent, T... values)
	{
		visibilityTriggers.put(targetComponent, values);
		
		updateVisibilityChain();
	}
	
	/**
	 * Pretty-prints an array in a human-readable form
	 * 
	 * @param array
	 *            the array to pretty-print
	 * @return a pretty-printed string of the given array
	 * @throws IllegalArgumentException
	 *             if the given argument is not an array
	 */
	private final String arrayToString(Object array) throws IllegalArgumentException
	{
		String s = "";
		int length = Array.getLength(array);
		
		if (length > 0)
			s += Array.get(array, 0);
		
		for (int i = 1; i < length; i++)
			s += " " + Array.get(array, i);
		
		return s;
	}
	
	@Override
	protected void addTo(Container container)
	{
		GridBagLayout gridbag = (GridBagLayout) container.getLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.insets = new Insets(2, 10, 2, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		//gbc.weighty = 0;
		gridbag.setConstraints(jLabelName, gbc);
		container.add(jLabelName);
		
		gbc.weightx = 1;
		//gbc.weighty = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(userInputComponent, gbc);
		container.add(userInputComponent);
	}
	
	protected void dispose()
	{
		this.visibilityTriggers.clear();
		userInputComponent = null;
		super.dispose();
	}
	
	protected final void fireVariableChanged(T value)
	{
		for (EzVarListener<T> l : varChangeListeners)
			l.variableChanged(this, value);
		
		if (getUI() != null)
		{
			updateVisibilityChain();
			getUI().repack(true);
		}
	}
	
	/**
	 * Gets the graphical component used to receive user input
	 */
	protected final Component getComponent()
	{
		return this.userInputComponent;
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
		if (userInputComponent instanceof JComboBox)
		{
			JComboBox combo = (JComboBox) userInputComponent;
			
			ArrayList<T> items = new ArrayList<T>(combo.getItemCount());
			for (int i = 0; i < combo.getItemCount(); i++)
				items.add((T) combo.getItemAt(i));
			return items.toArray(dest);
		}
		
		throw new UnsupportedOperationException("The input component is not a list of values");
	}
	
	/**
	 * Returns an EzPlug-wide unique identifier for this variable (used to save/load parameters)
	 * 
	 * @return a String identifier that is unique within the owner plug
	 */
	String getID()
	{
		String id = name;
		
		EzGroup group = getGroup();
		
		while (group != null)
		{
			id = group.name + "." + id;
			group = group.getGroup();
		}
		
		return id;
	}
	
	/**
	 * Reads the value from the interface (here the default combo box). This default cast can be
	 * overriden with an input parsing method
	 * 
	 * @return the user-selected (or -defined) value
	 */
	@SuppressWarnings("unchecked")
	public T getValue()
	{
		// instead of deferring the implementation to all Var* classes,
		// retrieve the input from the most popular components here
		
		if (userInputComponent instanceof JSpinner)
			return (T) ((JSpinner) userInputComponent).getValue();
		
		if (userInputComponent instanceof JComboBox)
			return (T) ((JComboBox) userInputComponent).getSelectedItem();
		
		if (userInputComponent instanceof JCheckBox)
			return (T) (Boolean) ((JCheckBox) userInputComponent).isSelected();
		
		if (userInputComponent instanceof JTextField)
		{
			if (!(this instanceof EzTextParser))
				throw new UnsupportedOperationException("Variable " + name + "cannot parse text input");
			
			return ((EzTextParser<T>) this).parseInput(((JTextField) userInputComponent).getText());
		}
		
		if (userInputComponent == null)
		{
			String message = "Interface value cannot be accessed. Possible reasons: \n";
			message += " - Variable " + name + " wasn't added to the interface\n";
			message += " - The application is quitting";
			throw new EzException(message, false);
		}
		
		throw new UnsupportedOperationException("Unsupported input component: " + userInputComponent);
	}
	
	/**
	 * Removes the given listener from the list
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeVarChangeListener(EzVarListener<T> listener)
	{
		varChangeListeners.remove(listener);
	}
	
	/**
	 * Removes all change listeners for this variable
	 */
	public void removeAllVarChangeListeners()
	{
		varChangeListeners.clear();
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
		if (userInputComponent instanceof JComboBox)
		{
			JComboBox combo = (JComboBox) userInputComponent;
			
			combo.removeAllItems();
			for (T value : values)
				combo.addItem(value);
			combo.setEditable(allowUserInput);
		}
	}
	
	/**
	 * Sets whether the input component is enabled or not in the interface
	 * 
	 * @param enabled
	 *            the enabled state
	 */
	public void setEnabled(boolean enabled)
	{
		jLabelName.setEnabled(enabled);
		userInputComponent.setEnabled(enabled);
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
		if (userInputComponent instanceof JComboBox)
		{
			ThreadUtil.invoke(new Runnable()
			{
				@Override
				public void run()
				{
					((JComboBox) userInputComponent).setSelectedItem(value);
				}
			}, !SwingUtilities.isEventDispatchThread());
		}
		else throw new UnsupportedOperationException("Variable " + name + " cannot be changed outside the interface");
	}
	
	/**
	 * Assigns a tool-tip text to the variable, which pops up when the user hovers the mouse on it.
	 * 
	 * @param text
	 *            the text to display (usually no more than 20 words)
	 */
	public void setToolTipText(String text)
	{
		jLabelName.setToolTipText(text);
		userInputComponent.setToolTipText(text);
	}
	
	protected void setComponent(JComponent component)
	{
		this.userInputComponent = component;
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
		return this.name;
	}
	
	private void updateVisibilityChain()
	{
		Set<EzComponent> componentsToUpdate = visibilityTriggers.keySet();
		
		// first, hide everything in the chain
		for (EzComponent component : componentsToUpdate)
			component.setVisible(false);
		
		// if "this" is not visible, do anything else
		if (!this.visible)
			return;
		
		// otherwise, one by one, show the components w.r.t. the triggers
		component: for (EzComponent component : componentsToUpdate)
		{
			T[] componentTriggerValues = visibilityTriggers.get(component);
			
			for (T triggerValue : componentTriggerValues)
			{
				if (this.getValue().equals(triggerValue))
				{
					// this call will be recursive in case of a EzVar object
					component.setVisible(true);
					continue component;
				}
			}
		}
	}
}
