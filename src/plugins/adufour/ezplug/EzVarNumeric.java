package plugins.adufour.ezplug;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import plugins.adufour.vars.lang.Constraint;
import plugins.adufour.vars.lang.ConstraintByRange;
import plugins.adufour.vars.lang.Var;

/**
 * Superclass of all variables holding a number-type variable. This class provides generic methods and graphical
 * interface elements common to all number-type variables
 * 
 * @author Alexandre Dufour
 * 
 * @param <N>
 *            the type of number to use in this variable
 */
public abstract class EzVarNumeric<N extends Number> extends EzVar<N>
{
    protected EzVarNumeric(Var<N> variable, Constraint<N> constraint)
    {
        super(variable, constraint);
    }

    protected EzVarNumeric(Var<N> variable, N[] defaultValues, int defaultValueIndex, boolean allowUserInput)
    {
        super(variable, defaultValues, defaultValueIndex, allowUserInput);
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
        Constraint<N> cons = variable.getConstraint();

        if (cons instanceof ConstraintByRange)
        {
            return (N) ((ConstraintByRange<N>) cons).getMinimum();
        }
        else
            throw new UnsupportedOperationException("This variable is not constrained by range");
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
        JComponent comp = getVarEditor().editorComponent;

        if (comp instanceof JSpinner)
        {
            ((SpinnerNumberModel) ((JSpinner) comp).getModel()).setMinimum(minValue);
            // FIXME adjust the variable constraint accordingly
        }
        else
            throw new UnsupportedOperationException("Input component is not a JSpinner object");
    }

    /**
     * Gets the step value of this variable
     * 
     * @throws UnsupportedOperationException
     *             If the graphical component of this variable is not a JSpinner object
     */
    public N getStep() throws UnsupportedOperationException
    {
        Constraint<N> cons = variable.getConstraint();

        if (cons instanceof ConstraintByRange)
        {
            return ((ConstraintByRange<N>) cons).getStepSize();
        }
        else
            throw new UnsupportedOperationException("This variable is not constrained by range");
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
        JComponent comp = getVarEditor().editorComponent;

        if (comp instanceof JSpinner)
        {
            ((SpinnerNumberModel) ((JSpinner) comp).getModel()).setStepSize(step);
            // FIXME adjust the variable constraint accordingly
        }
        else
            throw new UnsupportedOperationException("Input component is not a JSpinner object");
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
        Constraint<N> cons = variable.getConstraint();

        if (cons instanceof ConstraintByRange)
        {
            return (N) ((ConstraintByRange<N>) cons).getMaximum();
        }
        else
            throw new UnsupportedOperationException("This variable is not constrained by range");
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
        JComponent comp = getVarEditor().editorComponent;

        if (comp instanceof JSpinner)
        {
            ((SpinnerNumberModel) ((JSpinner) comp).getModel()).setMaximum(maxValue);
            // FIXME adjust the variable constraint accordingly
        }
        else
            throw new UnsupportedOperationException("Input component is not a JSpinner object");
    }

    /**
     * Sets the news value of the variable. This method only works if the component is a JSpinner object
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
        JComponent comp = getVarEditor().editorComponent;

        if (comp instanceof JSpinner)
        {
            ((SpinnerNumberModel) ((JSpinner) comp).getModel()).setMinimum(min);
            ((SpinnerNumberModel) ((JSpinner) comp).getModel()).setMaximum(max);
            ((SpinnerNumberModel) ((JSpinner) comp).getModel()).setStepSize(step);
            ((SpinnerNumberModel) ((JSpinner) comp).getModel()).setValue(value);
            // FIXME adjust the variable constraint accordingly
        }
        else

            throw new UnsupportedOperationException("Input component is not a JSpinner object");
    }
}