package plugins.adufour.ezplug;

import icy.system.thread.ThreadUtil;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Superclass of all variables holding a number-type variable. This class provides generic methods
 * and graphical interface elements common to all number-type variables
 * 
 * @author Alexandre Dufour
 * 
 * @param <N>
 *            the type of number to use in this variable
 */
public abstract class EzVarNumeric<N extends Number> extends EzVar<N> implements MouseWheelListener, EzVar.Storable<N>, ChangeListener, EzTextParser<N>
{
	private static final long	serialVersionUID	= 1L;
	
	private static final int	MAX_SPINNER_WIDTH	= 100;
	
	private SpinnerNumberModel	spinnerModel;
	
	protected EzVarNumeric(String varName, final SpinnerNumberModel model)
	{
		super(varName);
		
		initJSpinner(model.getNumber(), model.getMinimum(), model.getMaximum(), model.getStepSize(), false);
	}
	
	protected EzVarNumeric(String varName, N[] defaultValues, int defaultValueIndex, boolean allowUserInput)
	{
		super(varName, defaultValues, defaultValueIndex, allowUserInput);
	}
	
	private void initJSpinner(Number value, Comparable<?> min, Comparable<?> max, Number step, final boolean repackOwnerPlug)
	{
		spinnerModel = new SpinnerNumberModel(value, min, max, step);
		
		if (getComponent() != null)
		{
			getComponent().removeMouseWheelListener(this);
			((JSpinner) getComponent()).removeChangeListener(this);
		}
		
		ThreadUtil.invoke(new Runnable()
		{
			@Override
			public void run()
			{
				final JSpinner jSpinner = new JSpinner(spinnerModel);
				
				jSpinner.addChangeListener(EzVarNumeric.this);
				
				jSpinner.addMouseWheelListener(EzVarNumeric.this);
				
				// Assign a maximum size to the spinner to avoid huge-ass interfaces
				Dimension dim = jSpinner.getPreferredSize();
				dim.setSize(Math.min(dim.width, MAX_SPINNER_WIDTH), dim.height);
				jSpinner.setPreferredSize(dim);
				
				setComponent(jSpinner);
				if (getUI() != null && repackOwnerPlug) getUI().repack(true);
			}
		}, !SwingUtilities.isEventDispatchThread());
	}
	
	/**
	 * Gets the minimum value of this variable
	 * 
	 * @throws UnsupportedOperationException
	 *             If the graphical component of this variable is not a JSpinner object
	 */
	@SuppressWarnings("unchecked")
	public N getMinValue()
	{
		Component comp = getComponent();
		
		if (comp instanceof JSpinner)
		{
			return (N) spinnerModel.getMinimum();
		}
		else throw new UnsupportedOperationException("Input component is not a JSpinner object");
	}
	
	/**
	 * Sets the minimum value for the JSpinner component
	 * 
	 * @param minValue
	 * @throws UnsupportedOperationException
	 *             If the graphical component of this variable is not a JSpinner object
	 */
	public void setMinValue(Comparable<N> minValue) throws UnsupportedOperationException
	{
		Component comp = getComponent();
		
		if (comp instanceof JSpinner)
		{
			// dev-note:
			// changing spinner bounds does not affect the spinner size on screen
			// model.setMinimum(minValue);
			// instead, replace the spinner with a new one and update the entire interface
			
			// TODO figure out if spinner size can be adjusted to reflect bounds changes
			
			initJSpinner(spinnerModel.getNumber(), minValue, spinnerModel.getMaximum(), spinnerModel.getStepSize(), true);
		}
		else throw new UnsupportedOperationException("Input component is not a JSpinner object");
	}
	
	/**
	 * Gets the step value of this variable
	 * 
	 * @throws UnsupportedOperationException
	 *             If the graphical component of this variable is not a JSpinner object
	 */
	@SuppressWarnings("unchecked")
	public N getStep() throws UnsupportedOperationException
	{
		Component comp = getComponent();
		
		if (comp instanceof JSpinner)
		{
			return (N) spinnerModel.getStepSize();
		}
		else throw new UnsupportedOperationException("Input component is not a JSpinner object");
	}
	
	/**
	 * Sets the step between consecutive values
	 * 
	 * @param step
	 * @throws UnsupportedOperationException
	 *             If the graphical component of this variable is not a JSpinner object
	 */
	public void setStep(N step) throws UnsupportedOperationException
	{
		Component comp = getComponent();
		
		if (comp instanceof JSpinner)
		{
			spinnerModel.setStepSize(step);
		}
		else throw new UnsupportedOperationException("Input component is not a JSpinner object");
	}
	
	/**
	 * Gets the maximum value of this variable
	 * 
	 * @throws UnsupportedOperationException
	 *             If the graphical component of this variable is not a JSpinner object
	 */
	@SuppressWarnings("unchecked")
	public N getMaxValue()
	{
		Component comp = getComponent();
		
		if (comp instanceof JSpinner)
		{
			return (N) spinnerModel.getMaximum();
		}
		else throw new UnsupportedOperationException("Input component is not a JSpinner object");
	}
	
	/**
	 * Sets the minimum value for the JSpinner component
	 * 
	 * @param maxValue
	 * @throws UnsupportedOperationException
	 *             If the graphical component of this variable is not a JSpinner object
	 */
	public void setMaxValue(Comparable<N> maxValue)
	{
		Component comp = getComponent();
		
		if (comp instanceof JSpinner)
		{
			// dev-note:
			// changing spinner bounds does not affect the spinner size on screen
			// model.setMaximum(maxValue);
			// instead, replace the spinner with a new one and update the entire interface
			
			// TODO figure out if spinner size can be adjusted to reflect bounds changes
			
			initJSpinner(spinnerModel.getNumber(), spinnerModel.getMinimum(), maxValue, spinnerModel.getStepSize(), true);
		}
		else throw new UnsupportedOperationException("Input component is not a JSpinner object");
	}
	
	/**
	 * Sets the news value of the variable. This method only works if the component is a JSpinner
	 * object
	 * 
	 * @param value
	 *            the new value
	 * @param max
	 *            the new max bound
	 * @param min
	 *            the new min bound
	 * @param step
	 *            the new step
	 * @throws UnsupportedOperationException
	 *             If the graphical component of this variable is not a JSpinner object
	 */
	public void setValues(N value, Comparable<N> min, Comparable<N> max, N step)
	{
		if (getComponent() instanceof JSpinner)
			initJSpinner(value, min, max, step, true);
		else throw new UnsupportedOperationException("Input component is not a JSpinner object");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setValue(final N value)
	{
		try
		{
			super.setValue(value);
		}
		catch (UnsupportedOperationException uoE)
		{
			Component comp = getComponent();
			
			if (comp instanceof JSpinner)
			{
				JSpinner spinner = (JSpinner) comp;
				final SpinnerNumberModel snm = (SpinnerNumberModel) spinner.getModel();
				
				if (snm.getMinimum().compareTo(value) <= 0 && snm.getMaximum().compareTo(value) >= 0)
				{
					ThreadUtil.invokeLater(new Runnable()
					{
						public void run()
						{
							snm.setValue(value);
						}
					});
				}
				else
				{
					String warning = "Warning: cannot assign variable " + name + ": ";
					warning += value + " is out of bounds (min=" + snm.getMinimum() + ", max=" + snm.getMaximum() + "). ";
					warning += "Continuing with previous value (" + snm.getValue() + ")";
					System.err.println(warning);
				}
			}
			
			else throw uoE;
		}
	}
	
	public void setEnabled(final boolean enabled)
	{
		if (getComponent() instanceof JSpinner) ThreadUtil.invokeLater(new Runnable()
		{
			public void run()
			{
				((JSpinner) getComponent()).setEnabled(enabled);
			}
		});
		else
			super.setEnabled(enabled);
	}
	
	// MouseWheelListener //
	
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		JSpinner jSpinner = (JSpinner) e.getSource();
		
		if (!jSpinner.isEnabled()) return;
		
		int clicks = Math.abs(e.getWheelRotation());
		
		boolean up = (e.getWheelRotation() < 0);
		Object newValue;
		
		for (int i = 0; i < clicks; i++)
		{
			newValue = (up ? jSpinner.getNextValue() : jSpinner.getPreviousValue());
			
			if (newValue == null) break;
			
			jSpinner.setValue(newValue);
		}
		
		fireVariableChanged(getValue());
	}
	
	// Var.Storable //
	
	public N getXMLValue()
	{
		return getValue();
	}
	
	public void setXMLValue(N value)
	{
		setValue(value);
	}
	
	// ChangeListener //
	
	public void stateChanged(ChangeEvent e)
	{
		fireVariableChanged(getValue());
	}
	
	// Dispose //
	
	public void dispose()
	{
		if (getComponent() instanceof JSpinner)
		{
			JSpinner spinner = (JSpinner) getComponent();
			spinner.removeMouseWheelListener(this);
			spinner.removeChangeListener(this);
		}
		
		super.dispose();
	}
}