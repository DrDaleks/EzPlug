package plugins.adufour.ezplug;

import icy.system.thread.ThreadUtil;

import javax.swing.JOptionPane;

/**
 * Utility class providing various ways for EzPlugs to send out messages to the user
 * 
 * @author Alexandre Dufour
 */
public class EzMessage
{
    private EzMessage()
    {

    }

    /**
     * Enumerates the different types of messages which may be sent
     * 
     * @author Alexandre Dufour
     */
    public enum MessageType
    {
        /**
         * A default message, nothing special about it.
         */
        DEFAULT,
        /**
         * An error message, will be tagged "Error" and use an error icon if dialog output is
         * chosen.
         */
        ERROR,
        /**
         * An error message, will be tagged "Information" and use an information icon if dialog
         * output is chosen.
         */
        INFORMATION,
        /**
         * An error message, will be tagged "Warning" and use a warning icon if dialog output is
         * chosen.
         */
        WARNING
    }

    /**
     * Enumerates the different ways to output the message
     * 
     * @author Alexandre Dufour
     */
    public enum OutputType
    {
        /**
         * Message will be printed in the standard system output stream (default).
         */
        SYSTEM_OUT,
        /**
         * Message will be printed in the standard system error stream (prints in red by default).
         */
        SYSTEM_ERR,
        /**
         * Message will be shown in a graphical message box with an appropriate title and icon.
         */
        DIALOG
    }

    /**
     * Sends a default message to the default standard output stream
     * 
     * @param message
     *        the message to display
     */
    public static void message(String message)
    {
        message(message, MessageType.DEFAULT, OutputType.SYSTEM_OUT);
    }

    /**
     * Sends a message to the user via the interface, with specified type of message and display
     * 
     * @param message
     *        the message to display
     * @param messageType
     *        the type of message
     * @param outputType
     *        indicates how to display the message on the interface
     */
    public static void message(final String message, MessageType messageType, OutputType outputType)
    {
        final int dialogMessageType;// = JOptionPane.PLAIN_MESSAGE;
        final String dialogTitle;// = "";

        switch (messageType)
        {
            case WARNING:
                dialogMessageType = JOptionPane.WARNING_MESSAGE;
                dialogTitle = "Warning";
                break;

            case ERROR:
                dialogMessageType = JOptionPane.ERROR_MESSAGE;
                dialogTitle = "Error";
                break;

            case INFORMATION:
                dialogMessageType = JOptionPane.INFORMATION_MESSAGE;
                dialogTitle = "Information";
                break;

            default:
                dialogMessageType = JOptionPane.PLAIN_MESSAGE;
                dialogTitle = "";
                break;
        }

        switch (outputType)
        {
            case SYSTEM_OUT:
                System.out.println(dialogTitle + ": " + message);
                break;
            case SYSTEM_ERR:
                System.err.println(dialogTitle + ": " + message);
                break;
            case DIALOG:
                ThreadUtil.bgRun(new Runnable()
                {
                    public void run()
                    {
                        JOptionPane.showMessageDialog(null, message, dialogTitle, dialogMessageType);
                    }
                }, true);
                break;
        }
    }
}
