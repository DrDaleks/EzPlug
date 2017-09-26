package plugins.adufour.ezplug;

import plugins.adufour.vars.lang.VarDouble;
import plugins.adufour.vars.lang.VarString;
import plugins.adufour.vars.util.VarListener;

/**
 * Utility class to monitor the status and progress of a task
 * 
 * @author Alexandre Dufour
 */
public class EzStatus
{
    /**
     * A completion level between 0 and 1
     */
    private final VarDouble progress = new VarDouble("Progress value", 0.0);
    
    /**
     * A text indicating what the task is currently doing
     */
    private final VarString message = new VarString("Progress message", "");
    
    /**
     * Registers a listener to the progress value
     * 
     * @param listener
     *            the listener to register
     */
    public void addProgressListener(VarListener<Double> listener)
    {
        progress.addListener(listener);
    }
    
    /**
     * Registers a listener to the message value
     * 
     * @param listener
     *            the listener to register
     */
    public void addMessageListener(VarListener<String> listener)
    {
        message.addListener(listener);
    }
    
    /**
     * Notify this status object (and potential listeners) that the task has completed (and
     * therefore resets the completion and message)
     */
    public void done()
    {
        setCompletion(0.0);
        setMessage("");
    }
    
    /**
     * @return the variable containing the completion value
     */
    public VarDouble getProgressVariable()
    {
        return progress;
    }
    
    /**
     * @return The progress value (between 0 and 1)
     */
    public double getProgressValue()
    {
        return progress.getValue();
    }
    
    /**
     * @return <code>true</code> if the task is completed
     */
    public boolean isCompleted()
    {
        return getProgressValue() == 1.0;
    }
    
    /**
     * @return the variable containing the message value
     */
    public VarString getMessageVariable()
    {
        return message;
    }
    
    /**
     * @return A text indicating what the task is currently doing
     */
    public String getMessageValue()
    {
        return message.getValue();
    }
    
    /**
     * Unregisters a listener of the progress value
     * 
     * @param listener
     *            the listener to unregister
     */
    public void removeProgressListener(VarListener<Double> listener)
    {
        progress.addListener(listener);
    }
    
    /**
     * Unregisters a listener of the message value
     * 
     * @param listener
     *            the listener to unregister
     */
    public void removeMessageListener(VarListener<String> listener)
    {
        message.addListener(listener);
    }
    
    /**
     * Sets the status message
     * 
     * @param message
     */
    public void setMessage(String message)
    {
        this.message.setValue(message);
    }
    
    /**
     * Sets the progression stage as a [0-1] percentage
     * 
     * @param completion
     *            (between 0 and 1)
     */
    public void setCompletion(double completion)
    {
        this.progress.setValue(completion);
    }
}
