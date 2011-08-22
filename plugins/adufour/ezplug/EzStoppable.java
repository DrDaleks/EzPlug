package plugins.adufour.ezplug;

/**
 * Interface implemented by EzPlugs which handle a stop operation during the process. <br>
 * When implemented, a stop button will appear on the graphical interface.
 * 
 * @author Alexandre Dufour
 * @version 2010-11-09: First implementation<br>
 * 
 */
public interface EzStoppable
{
	/**
	 * Requests the implementing EzPlug to stop the currently running process.
	 */
	void stopExecution();
}
