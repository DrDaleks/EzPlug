package plugins.adufour.ezplug;

import icy.plugin.PluginDescriptor;
import icy.system.IcyExceptionHandler;

import java.lang.Thread.UncaughtExceptionHandler;

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

    static class ForcedInterruptionHandler implements UncaughtExceptionHandler
    {
        private final PluginDescriptor source;

        public ForcedInterruptionHandler(PluginDescriptor source)
        {
            this.source = source;
        }

        @Override
        public void uncaughtException(Thread t, Throwable e)
        {
            // this method is only called when the EzPlug process has been killed
            String message = "Plug-in " + source.getName() + " has requested to kill thread " + t.getName() + " while it was still running.\n"
                    + "Implement the EzStoppable interface to handle clean exit calls and avoid this message.";
            IcyExceptionHandler.handleException(source, new InterruptedException(message), false);
        }
    }
}
