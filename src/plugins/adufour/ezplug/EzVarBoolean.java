package plugins.adufour.ezplug;

import icy.system.thread.ThreadUtil;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;

/**
 * Boolean variable for the ezPlug framework. Creates a JCheckBox component to select between true
 * and false
 * 
 * @author Alexandre Dufour
 * 
 */
public class EzVarBoolean extends EzVar<Boolean> implements EzVar.Storable<Boolean>, ActionListener
{
	private static final long	serialVersionUID	= 1L;

	/**
	 * Constructs a new input variable holding a boolean value with a JCheckBox for graphical user
	 * interface
	 * 
	 * @param varName
	 *            the name of the variable
	 * @param defaultSelectedState
	 *            the default state of the JCheckBox
	 */
	public EzVarBoolean(String varName, final boolean defaultSelectedState)
	{
		super(varName);
		
		ThreadUtil.invoke(new Runnable()
		{
			@Override
			public void run()
			{
				final JCheckBox jCheckBox = new JCheckBox();
				jCheckBox.setSelected(defaultSelectedState);
				jCheckBox.addActionListener(EzVarBoolean.this);
				
				setComponent(jCheckBox);
			}
		}, !SwingUtilities.isEventDispatchThread());
	}
	
	public void setValue(final Boolean value)
	{
		final Component comp = getComponent();
		
		if (comp instanceof JCheckBox)
		{
			ThreadUtil.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					((JCheckBox) comp).setSelected(value);
				}
			});
			fireVariableChanged(value);
		}
		else throw new UnsupportedOperationException("Variable " + name + " cannot be changed outside the interface");
	}
	
	@Override
	public Boolean getXMLValue()
	{
		return getValue();
	}
	
	@Override
	public void setXMLValue(Boolean value)
	{
		setValue(value);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		fireVariableChanged(getValue());
	}
}
