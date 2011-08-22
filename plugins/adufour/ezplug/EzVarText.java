package plugins.adufour.ezplug;

import icy.system.thread.ThreadUtil;

import java.awt.Component;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Class defining a variable of type String, embarking a textfield as graphical component
 * 
 * @author Alexandre Dufour
 * 
 */
public class EzVarText extends EzVar<String> implements EzTextParser<String>, EzVar.Storable<String>
{
	private static final long	serialVersionUID	= 1L;
	private JTextField			jTextField;
	
	/**
	 * Creates a new Text variable
	 * 
	 * @param varName
	 *            the name of the variable (as it will appear on the interface)
	 */
	public EzVarText(String varName)
	{
		super(varName);
		
		ThreadUtil.invoke(new Runnable()
		{
			public void run()
			{
				jTextField = new JTextField();
				setComponent(jTextField);
			}
		}, !SwingUtilities.isEventDispatchThread());
	}
	
	/**
	 * Creates a new Text input variable with a list of default values
	 * 
	 * @param varName
	 *            the variable name
	 * @param defaultValues
	 *            the list of default values the user may choose from
	 * @param allowUserInput
	 *            set to true if the user may input text manually instead of selecting a default
	 *            value, or false to forbid user input
	 */
	public EzVarText(String varName, String[] defaultValues, Boolean allowUserInput)
	{
		this(varName, defaultValues, -1, allowUserInput);
	}
	
	/**
	 * Creates a new Text input variable with a list of default values and a default selection
	 * 
	 * @param varName
	 *            the variable name
	 * @param defaultValues
	 *            the list of default values the user may choose from
	 * @param defaultValueIndex
	 *            the index of the default selected value
	 * @param allowUserInput
	 *            set to true if the user may input text manually instead of selecting a default
	 *            value, or false to forbid user input
	 */
	public EzVarText(String varName, String[] defaultValues, int defaultValueIndex, Boolean allowUserInput)
	{
		super(varName, defaultValues, defaultValueIndex, allowUserInput);
	}
	
	@Override
	public String parseInput(String s)
	{
		return s;
	}
	
	@Override
	public void setValue(String value)
	{
		try
		{
			super.setValue(value);
		}
		catch (UnsupportedOperationException uoE)
		{
			Component comp = getComponent();
			
			if (comp instanceof JTextField)
			{
				((JTextField) comp).setText(value);
			}
			
			else throw uoE;
		}
	}
	
	@Override
	public String getXMLValue()
	{
		return getValue();
	}
	
	@Override
	public void setXMLValue(String value)
	{
		setValue(value);
	}
}